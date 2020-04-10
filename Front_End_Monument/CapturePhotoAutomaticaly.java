package project.mcoe.monument;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CapturePhotoAutomaticaly extends AppCompatActivity implements SurfaceHolder.Callback, Camera.AutoFocusCallback{

    // a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;
    // a bitmap to display the captured image
    private Bitmap bmp;
    // a surface holder
    private SurfaceHolder sHolder;
    // a variable to control the camera
    private Camera mCamera;
    // the camera parameters
    private Camera.Parameters parameters;
    private Bitmap surfaceView;
    String sImage;
    String imageUploadUML=IPsetting.ip+"imageupload.php";
    String getDataUML=IPsetting.ip+"fetchdetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo_automaticaly);

        sv = (SurfaceView) findViewById(R.id.surfaceView1);
        sHolder = sv.getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getApplicationContext();

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        parameters = mCamera.getParameters();
        parameters.getFocusMode();

        // Check what resolutions are supported by your camera
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        // Iterate through all available resolutions and choose one.
        // The chosen resolution will be stored in mSize.
        Camera.Size mSize = null;
        for (Camera.Size size : sizes) {
            // Log.i(TAG, "Available resolution: "+size.width+" "+size.height);
            mSize = size;
        }

        //Log.i(TAG, "Chosen resolution: "+mSize.width+" "+mSize.height);
        parameters.setPictureSize(mSize.width, mSize.height);
        parameters.set("rotation", 90);
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        mCamera.autoFocus(this);

    }

    @SuppressLint("NewApi")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = Camera.open(findFirstBackFacingCamera());
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop the preview
        mCamera.stopPreview();
        // release the camera
        mCamera.release();
        // unbind the camera from this object
        mCamera = null;
    }
    @SuppressLint("NewApi")
    private int findFirstBackFacingCamera() {
        int foundId = -1;
        int numCams = Camera.getNumberOfCameras();
        for (int camId = 0; camId < numCams; camId++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(camId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                foundId = camId;
                break;
            }
        }
        return foundId;
    }


    public Bitmap getSurfaceView() {
        return surfaceView;
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        final Camera.PictureCallback mCall = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                String sdcardBmpPath = "sdcard//sample_text.bmp";
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(sdcardBmpPath);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    sImage= getStringImage(bmp);
                        uploadImageOnServer();
                    getDetails();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new CountDownTimer(4000, 500) {

            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mCamera.takePicture(null, null, mCall);
                Toast.makeText(CapturePhotoAutomaticaly.this, "Image Captured ... Processing", Toast.LENGTH_LONG).show();
            }

        }.start();
    }
    public void uploadImageOnServer(){
        RequestParams params= new RequestParams();
        params.put("image", sImage);
        params.put("imagename", "myphoto");
        AsyncHttpClient client= new AsyncHttpClient();
        client.post(imageUploadUML, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response =new String(responseBody);
                System.out.print(response);
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    if(obj.getString("success").equals("200"))
                    {
                        Toast.makeText(CapturePhotoAutomaticaly.this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(CapturePhotoAutomaticaly.this, "DB Error Occured", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Toast.makeText(CapturePhotoAutomaticaly.this, "Connection Error Occured", Toast.LENGTH_LONG).show();
            }
        });
        try {

            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void getDetails(){
        RequestParams params= new RequestParams();
        params.put("info", "info");

        AsyncHttpClient client= new AsyncHttpClient();
        client.post(getDataUML, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response =new String(responseBody);
                System.out.print(response);
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    if(obj.getString("success").equals("200"))
                    {
                        UserData.resultfoldername=obj.getString("resultfoldername");
                        UserData.imagename1=obj.getString("imagename1");
                        UserData.imagename2=obj.getString("imagename2");
                        UserData.imagename3=obj.getString("imagename3");
                        UserData.imagename4=obj.getString("imagename4");
                        UserData.txtfilename1=obj.getString("textfilename1");
                        UserData.videoname1=obj.getString("englishvideoname1");
                        UserData.videoname2=obj.getString("hindivideoname1");
                        UserData.textfiledata=obj.getString("textfiledata");
                        Toast.makeText(CapturePhotoAutomaticaly.this, "Information Fetch Successfully", Toast.LENGTH_LONG).show();
                        Intent inte=new Intent(CapturePhotoAutomaticaly.this,DisplayInformation.class);
                        startActivity(inte);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(CapturePhotoAutomaticaly.this, "DB Error Occured", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
              //  Toast.makeText(CapturePhotoAutomaticaly.this, "Connection Error Occured1", Toast.LENGTH_LONG).show();
            }
        });
    }
}
