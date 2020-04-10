%% Visualize how the classifier works
load bag;
load trainedClassifier;

%[filename, pathname] = uigetfile('*.jpg;*.bmp', 'Pick an Image file');
 %if isequal(filename,0) || isequal(pathname,0)
 %    warndlg('User pressed cancel');
 %  return;
 %else
    %disp(['User selected ', fullfile(pathname, filename)]);
     img = imread('C:\xampp\htdocs\monumentapp\uploads\myphoto.png');
 %end

% Add code here to invoke the trained classifier
imagefeatures = double(encode(bag, img));
% Find two closest matches for each feature
[bestGuess, score] = predict(trainedClassifier.ClassificationSVM,imagefeatures);

% Display the string label for img

% %%%%%Classification by simulating trained network model 

fprintf('\n');
if (strcmp(char(bestGuess),'EffilTower'))&&(1+score(1)>0.812)
%msgbox('EffilTower');
file = fopen( 'C:\xampp\htdocs\monumentapp\uploads\result.txt','w');
fprintf(file,'%6s%12s\n','Monument detected:EffilTower');
fclose(file);
% cd ..

elseif (strcmp(char(bestGuess),'jantarmantar'))&&(1+score(3)>0.812)
msgbox('jantarmantar','The Result: ');
fileID = fopen( 'C:\xampp\htdocs\monumentapp\uploads\result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:jantarmantar');
fclose(fileID);

elseif (strcmp(char(bestGuess),'Shaniwarwada'))&&(1+score(2)>0.812)
msgbox('shaniwarwada','The Result: ');
fileID = fopen( 'C:\xampp\htdocs\monumentapp\uploads\result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:shaniwarwada');
fclose(fileID); 


elseif (strcmp(char(bestGuess),'tajmahal'))&&(1+score(4)>0.812)
msgbox('Tajmahal','The Result: ');
fileID = fopen( 'C:\xampp\htdocs\monumentapp\uploads\result.txt','w');
fprintf(fileID,'%6s %12s\n','Monument detected:Tajmahal');
fclose(fileID);
else
    msgbox('Not Found','The Result: ');
file = fopen( 'C:\xampp\htdocs\monumentapp\uploads\result.txt','w');
fprintf(file,'%6s%12s\n','Monument detected:NotFound');
fclose(file);
end