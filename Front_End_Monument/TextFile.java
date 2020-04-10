package project.mcoe.monument;
import android.R.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class TextFile extends AppCompatActivity {

    Button bnLeft,bnRight;
    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_text_file);
        /*bnLeft=(Button)findViewById(R.id.btnTextLeft);
        bnRight=(Button)findViewById(R.id.btnTextRight);
        txtData=(TextView)findViewById(R.id.txtDisplayData);

        bnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }); */
    }
}
