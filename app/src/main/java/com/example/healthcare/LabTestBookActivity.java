package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LabTestBookActivity extends AppCompatActivity {

    EditText edname,edaddress,edcontact,edpincode;
    Button btnBooking;
    String phn,msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);

        edname=findViewById(R.id.editTextLTBFullName);
        edaddress=findViewById(R.id.editTextLTBAddress);
        edcontact=findViewById(R.id.editTextLTBContactNumber);
        edpincode=findViewById(R.id.editTextLTBPincode);
        btnBooking=findViewById(R.id.buttonLTBBook);

        Intent intent=getIntent();
        String[] price=intent.getStringExtra("price").toString().split(java.util.regex.Pattern.quote(":"));
        String date=intent.getStringExtra("date");
        String time=intent.getStringExtra("time");

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username= sharedpreferences.getString("username","").toString();

                Database db=new Database(getApplicationContext(),"healthcare",null,1);
                db.addOrder(username,edname.getText().toString(),edaddress.getText().toString(),edcontact.getText().toString(),Integer.parseInt(edpincode.getText().toString()),date.toString(),time.toString(),Float.parseFloat(price[1].toString()),"lab");
                db.removecart(username,"lab");


                phn=edcontact.getText().toString();
                msg="      HEALTH CARE  \nYour Lab Test Booking Is Confirmed\n\nName :"+edname.getText().toString()+"\nDate :"+date.toString()+"\nTime :"+time.toString()+"\nCost : Rs "+Float.parseFloat(price[1]);
                if(ContextCompat.checkSelfPermission(LabTestBookActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                {
                    sendSMS(phn,msg);
                }
                else
                {
                    ActivityCompat.requestPermissions(LabTestBookActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                }

                Toast.makeText(getApplicationContext(),"Your Booking is Done Successfully..",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LabTestBookActivity.this,HomeActivity.class));
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            sendSMS(phn,msg);
        }
    }

    private void sendSMS(String phn, String msg) {

        if(!phn.isEmpty())
        {
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phn,null,msg,null,null);
        }
    }


}