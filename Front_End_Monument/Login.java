package project.mcoe.monument;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    EditText logname, logpass;
    Button login, logreg;
    String lognm, logpas;
    String url=IPsetting.ip+"login.php";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logname=(EditText) findViewById(R.id.editLogName);
        logpass= (EditText) findViewById(R.id.editLogPass);
        login= (Button) findViewById(R.id.btnLogin);
        logreg= (Button) findViewById(R.id.btnLogRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lognm= logname.getText().toString().trim();
                logpas= logpass.getText().toString().trim();
                login();
            }
        });
        logreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 =new Intent(Login.this, Register.class);
                startActivity(in2);
            }
        });
    }
    public void login()
    {
        RequestParams params= new RequestParams();
        params.put("name", lognm);
        params.put("password", logpas);

        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Verifing Details..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        AsyncHttpClient client= new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
                String response =new String(responseBody);
                System.out.print(response);
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    if(obj.getString("success").equals("200"))
                    {
                        UserData.uid=obj.getString("uid");
                        UserData.uname=obj.getString("name");
                        UserData.upassword=obj.getString("password");
                        UserData.umobile=obj.getString("mobile");
                        UserData.uemail=obj.getString("email");
                        UserData.uaddress=obj.getString("address");
                        Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent inte=new Intent(Login.this,HomePage.class);
                        startActivity(inte);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "DB Error Occured", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Toast.makeText(Login.this, "Connection Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }
}
