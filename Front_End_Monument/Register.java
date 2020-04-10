package project.mcoe.monument;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText regname, regpass, regemail, regmobno, regadd;
    String rname, rpass, remail, rmob, radd;
    static String otp;
    Button btnreg, btnclear;

    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regname= (EditText) findViewById(R.id.editRegName);
        regpass= (EditText) findViewById(R.id.editRegPass);
        regemail= (EditText) findViewById(R.id.editRegEmail);
        regmobno= (EditText) findViewById(R.id.editRegMobileNo);
        regadd= (EditText) findViewById(R.id.editRegAddress);
        btnreg= (Button) findViewById(R.id.btnRegRegister);
        btnclear= (Button) findViewById(R.id.btnRegClear);
        smsManager= SmsManager.getDefault();

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clr();
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rname= regname.getText().toString().trim();
                rpass= regpass.getText().toString().trim();
                remail= regemail.getText().toString().trim();
                rmob= regmobno.getText().toString().trim();
                radd= regadd.getText().toString().trim();
                registr();
            }
        });
    }

    public void clr(){
        regname.setText("");
        regpass.setText("");
        regemail.setText("");
        regmobno.setText("");
        regadd.setText("");
    }

    public void registr(){
        IPsetting.uname=regname.getText().toString().trim();
        IPsetting.upassword=regpass.getText().toString().trim();
        IPsetting.umobile=regmobno.getText().toString().trim();
        IPsetting.uemail=(regemail.getText().toString().trim());
        IPsetting.uaddress=(regadd.getText().toString().trim());
        otpgeneration();

    }

    public void otpgeneration(){
        int randomPIN = (int)(Math.random()*9000)+1000;
        otp = ""+randomPIN;
        sendotp();
    }

    public void sendotp(){
        smsManager.sendTextMessage(rmob, null, otp, null, null);
        Intent in3=new Intent(Register.this,Verification.class);
        startActivity(in3);
    }
}