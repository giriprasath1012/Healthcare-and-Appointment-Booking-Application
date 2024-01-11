package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthArticledetailsactivity extends AppCompatActivity {

    TextView tv;
    Button btnback;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articledetailsactivity);

        //btnback=findViewById(R.id.buttonHADBack);
        tv=findViewById(R.id.textviewHADTitle2);
        img=findViewById(R.id.imageviewHAD);

        Intent intent=getIntent();
        tv.setText(intent.getStringExtra("text1"));

        Bundle bundle =getIntent().getExtras();
        if(bundle!= null)
        {
            int resid= bundle.getInt("text2");
            img.setImageResource(resid);
        }

       /* btnback.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                startActivity(new Intent(  HealthArticledetailsactivity.this,HealthArticleActivity.class));
            }
        }); */

    }
}