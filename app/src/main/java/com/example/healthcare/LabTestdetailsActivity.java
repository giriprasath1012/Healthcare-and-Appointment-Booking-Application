package com.example.healthcare;

import static com.example.healthcare.R.id.editTextLoginUser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LabTestdetailsActivity extends AppCompatActivity {

    TextView tvpackagename,tvTotalcost;
    EditText edDetails;
    Button btnAddtocart,btnLDback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_testdetails);

        tvpackagename=findViewById(R.id.textview1);
        tvTotalcost=findViewById(R.id.textViewLTDprice);
        edDetails=findViewById(R.id.editTextLDTestMultiLine);
        btnAddtocart=findViewById(R.id.buttonLTDaddtocart);
        //btnLDback=findViewById(R.id.buttonLTDBack);

        edDetails.setKeyListener(null);

        Intent intent=getIntent();
        tvpackagename.setText(intent.getStringExtra("text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        tvTotalcost.setText(intent.getStringExtra("text3")+"/-");


       /* btnLDback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LabTestdetailsActivity.this,LabTestActivity.class));
            }
        }); */

        btnAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username= sharedpreferences.getString("username","").toString();
               String product=tvpackagename.getText().toString();

                float price=Float.parseFloat(intent.getStringExtra("text3").toString());

                Database db=new Database(getApplicationContext(),"healthcare",null,1);

                if(db.checkCart(username,product)==1)
                {
                    Toast.makeText(getApplicationContext(),"Product Already Added",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.addCart(username,product,price,"lab");
                    Toast.makeText(getApplicationContext(),"Product Added to Cart",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LabTestdetailsActivity.this,LabTestActivity.class));
                }
            }
        });
    }
}