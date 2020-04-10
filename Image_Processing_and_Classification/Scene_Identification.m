%% Description of the Data
% The dataset contains 4 monuments:
% EffilTower,jantarmantar,shaniwarwada,tajmahal
% The images are photos of the monument that have been taken from different
% angles, positions, and different lighting conditions. These variations make 
% this a challenging task.
%%%
%% Load image data
% This assumes you have a directory: Training_Images
% with each scene in a subdirectory
imds = imageDatastore('Monument_Images',...
    'IncludeSubfolders',true,'LabelSource','foldernames')              %#ok
save imds;
%% Display Class Names and Counts
tbl = countEachLabel(imds)                                             %#ok
categories = tbl.Label;
save tbl;
save categories;
%% Partition 35 images for training and 15 for testing
[training_set, test_set] = prepareInputFiles(imds);
save training_set
save test_set
%% Create Visual Vocabulary 
tic
bag = bagOfFeatures(training_set,...
    'VocabularySize',250,'PointSelection','Detector');
scenedata = double(encode(bag, training_set));
toc
save scenedata;
save bag;
return;
%% Visualize Feature Vectors 
img = read(training_set(1), randi(training_set(1).Count));
featureVector = encode(bag, img);

subplot(4,2,1); imshow(img);
subplot(4,2,2); 
bar(featureVector);title('Visual Word Occurrences');xlabel('Visual Word Index');ylabel('Frequency');

img = read(training_set(2), randi(training_set(2).Count));
featureVector = encode(bag, img);
subplot(4,2,3); imshow(img);
subplot(4,2,4); 
bar(featureVector);title('Visual Word Occurrences');xlabel('Visual Word Index');ylabel('Frequency');

img = read(training_set(3), randi(training_set(3).Count));
featureVector = encode(bag, img);
subplot(4,2,5); imshow(img);
subplot(4,2,6); 
bar(featureVector);title('Visual Word Occurrences');xlabel('Visual Word Index');ylabel('Frequency');

img = read(training_set(4), randi(training_set(4).Count));
featureVector = encode(bag, img);
subplot(4,2,7); imshow(img);
subplot(4,2,8); 
bar(featureVector);title('Visual Word Occurrences');xlabel('Visual Word Index');ylabel('Frequency');
save featureVector
%% Create a Table using the encoded features
SceneImageData = array2table(scenedata);
sceneType = categorical(repelem({training_set.Description}', [training_set.Count], 1));
SceneImageData.sceneType = sceneType;
save SceneImageData;
%% Use the new features to train a model and assess its performance using 
%%classificationLearner;
trainClassifier(SceneImageData)
%% Test out accuracy on test set!
load bag;
load trainedClassifier;
testSceneData = double(encode(bag, test_set));
testSceneData = array2table(testSceneData,'VariableNames',trainedClassifier.RequiredVariables);
actualSceneType = categorical(repelem({test_set.Description}', [test_set.Count], 1));

predictedOutcome = trainedClassifier.predictFcn(testSceneData);

correctPredictions = (predictedOutcome == actualSceneType);
validationAccuracy = sum(correctPredictions)/length(predictedOutcome) 
%#ok

%% Visualize how the classifier works

load bag;
load trainedClassifier;
[filename, pathname] = uigetfile('*.jpg;*.bmp', 'Pick an Image file');
 if isequal(filename,0) || isequal(pathname,0)
     warndlg('User pressed cancel');
     return;
 else
    disp(['User selected ', fullfile(pathname, filename)]);
     img = imread(fullfile(pathname, filename));
 end

% Add code here to invoke the trained classifier
imagefeatures = double(encode(bag, img));
% Find two closest matches for each feature
[bestGuess, score] = predict(trainedClassifier.ClassificationSVM,imagefeatures);
% Display the string label for img

% %%%%%Classification by simulating trained network model 

fprintf('\n');
if strcmp(char(bestGuess),'EffilTower')
msgbox('EffilTower');
file = fopen( 'result.txt','w');
fprintf(file,'%6s%12s\n','Monument detected:EffilTower');
fclose(fileID);
elseif strcmp(char(bestGuess),'jantarmantar')
msgbox('jantarmantar','The Result: ');
fileID = fopen( 'result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:jantarmantar');

fclose(fileID);

elseif strcmp(char(bestGuess),'Shaniwarwada')
msgbox('shaniwarwada','The Result: ');
fileID = fopen( 'result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:shaniwarwada');
fclose(fileID); 


elseif strcmp(char(bestGuess),'tajmahal')
msgbox('Tajmahal','The Result: ');
fileID = fopen( 'result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:Tajmahal');
fclose(fileID);
end
