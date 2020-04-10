package project.mcoe.monument;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayInformation extends AppCompatActivity {

    TextView textFile1;
    ImageView image1,image2,image3,image4;
    VideoView video1,video2;
    LinearLayout ll;
    ProgressDialog pDialog;
    String imageURL=IPsetting.ip+"Output/"+UserData.resultfoldername+"/Image/";
    String path;
    int count=0;
    String video1URL=IPsetting.ip+"Output/"+UserData.resultfoldername+"/Video/English/"+UserData.videoname1;
    String video2URL=IPsetting.ip+"Output/"+UserData.resultfoldername+"/Video/Hindi/"+UserData.videoname2;
    int flag1=0,flag2=0;
    Button bneng,bnhin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_information);
        ll = (LinearLayout) findViewById(R.id.lyaoutOutput);
        textFile1=(TextView)findViewById(R.id.txtfile1);
        image1=(ImageView)findViewById(R.id.imageView1);
        image2=(ImageView)findViewById(R.id.imageView2);
        image3=(ImageView)findViewById(R.id.imageView3);
        image4=(ImageView)findViewById(R.id.imageView4);
        bneng=(Button)findViewById(R.id.btnvideoeng);
        bnhin=(Button)findViewById(R.id.btnvideohin);
        //video1=(VideoView)findViewById(R.id.videoView1);
        //video2=(VideoView)findViewById(R.id.videoView2);
        video1 = (VideoView) findViewById(R.id.videoeng);
        //ll.addView(video1);

        video2 = (VideoView) findViewById(R.id.videohin);
        //ll.addView(video2);

        bneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag1==0){
                    playEnglishVideo();
                    flag1=1;}
            }
        });

        bnhin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2==0){
                    playHindiVideo();
                    flag2=1;}
            }
        });
        setTextData();
        getAllData();
    }
    public void setTextData(){
        textFile1.setText(UserData.textfiledata);
    }

    public void getAllData(){
        getImageData();

    }

    public void getImageData(){
        for(int i=0; i<4; i++){
            if(i==0){
                path = imageURL+UserData.imagename1;
                GetImage gi = new GetImage();
                gi.ivObj= image1;
                gi.execute(path);
            }
            if(i==1){
                count=1;
                path = imageURL+UserData.imagename2;
                GetImage gi1 = new GetImage();
                gi1.ivObj= image2;
                gi1.execute(path);
            }
            if(i==2){
                count=2;
                path = imageURL+UserData.imagename3;
                GetImage gi2 = new GetImage();
                gi2.ivObj= image3;
                gi2.execute(path);
            }
            if(i==3){
                count=3;
                path = imageURL+UserData.imagename4;
                GetImage gi3 = new GetImage();
                gi3.ivObj= image4;
                gi3.execute(path);
            }
        }
    }

    class GetImage extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog loading;

        ImageView ivObj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(DisplayInformation.this, "Fetching Image...", null, true, true);
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            loading.dismiss();
            /*if(count==0){
                image1.setImageBitmap(b);
            }
            if(count==1){
                image2.setImageBitmap(b);
            }
            if(count==2){
                image3.setImageBitmap(b);
            }
            if(count==3){
                image4.setImageBitmap(b);
            }*/
            ivObj.setImageBitmap(b);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String id = params[0];
//
            URL url = null;
            Bitmap image1 = null;
            try {
                url = new URL(id);
                image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Log.d("Result", String.valueOf(image1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image1;
        }
    }

    public void playEnglishVideo(){
        pDialog = new ProgressDialog(DisplayInformation.this);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(DisplayInformation.this);
            mediacontroller.setAnchorView(video1);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(video1URL);
            video1.setMediaController(mediacontroller);
            video1.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            System.out.print("Error:"+e.getMessage());
            e.printStackTrace();
        }

        video1.requestFocus();
        video1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                video2.pause();
                pDialog.dismiss();
                video1.start();
                flag2=0;
            }
        });

    }

    public void playHindiVideo(){
        pDialog = new ProgressDialog(DisplayInformation.this);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(DisplayInformation.this);
            mediacontroller.setAnchorView(video2);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(video2URL);
            video2.setMediaController(mediacontroller);
            video2.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            System.out.print("Error:"+e.getMessage());
            e.printStackTrace();
        }

        video2.requestFocus();
        video2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                video1.pause();
                flag1=0;
                pDialog.dismiss();
                video2.start();
            }
        });
    }
}
