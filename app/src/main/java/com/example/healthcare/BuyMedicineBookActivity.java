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

public class BuyMedicineBookActivity extends AppCompatActivity {

    EditText edname,edaddress,edcontact,edpincode;
    Button btnBooking;
    String phn,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);

        edname=findViewById(R.id.editTextBMBFullName);
        edaddress=findViewById(R.id.editTextBMBAddress);
        edcontact=findViewById(R.id.editTextBMBContactNumber);
        edpincode=findViewById(R.id.editTextBMBPincode);
        btnBooking=findViewById(R.id.buttonBMBBook);

        Intent intent=getIntent();
        String[] price=intent.getStringExtra("price").toString().split(java.util.regex.Pattern.quote(":"));
        String date=intent.getStringExtra("date");

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username= sharedpreferences.getString("username","").toString();

                //Toast.makeText(getApplicationContext(),"date"+date,Toast.LENGTH_LONG).show();

                Database db=new Database(getApplicationContext(),"healthcare",null,1);
                db.addOrder(username,edname.getText().toString(),edaddress.getText().toString(),edcontact.getText().toString(),Integer.parseInt(edpincode.getText().toString()),date," ",Float.parseFloat(price[1].toString()),"medicine");
                db.removecart(username,"medicine");

                phn=edcontact.getText().toString();
                msg="      HEALTH CARE  \nYour Medicine Booking Is Confirmed\n\nName :"+edname.getText().toString()+"\nDate :"+date.toString()+"\nCost : Rs "+Float.parseFloat(price[1]);

                if(ContextCompat.checkSelfPermission(BuyMedicineBookActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                {
                    sendSMS(phn,msg);
                }
                else
                {
                    ActivityCompat.requestPermissions(BuyMedicineBookActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                }

                Toast.makeText(getApplicationContext(),"Your Booking is Done Successfully..",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BuyMedicineBookActivity.this,HomeActivity.class));
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