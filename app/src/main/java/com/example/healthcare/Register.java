package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText edUsername,edPassword,edconfirm,edphone;
    Button btn;
    TextView tv;
    String phn,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername=findViewById(R.id.editTextAppFullName);
        edPassword=findViewById(R.id.editTextAppContactNumber);
        edconfirm=findViewById(R.id.editTextAppFees);
        edphone=findViewById(R.id.editTextPhone);
        btn=findViewById(R.id.buttonBook);
        tv=findViewById(R.id.textViewAlreadyUser);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edUsername.getText().toString();
                String password=edPassword.getText().toString();
                String confirm=edconfirm.getText().toString();
                String phone=edphone.getText().toString();

                Database db=new Database(getApplicationContext(),"healthcare",null,1);

                if(username.length()==0 || password.length()==0 || confirm.length()==0 || phone.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please Fill all details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(password.compareTo(confirm)==0)
                    {
                        db.register(username,password,phone);

                        phn=phone;
                        msg="    HEALTH CARE  \n"+username+" your Registration is done successfully\nThank you for Registering in  HEALTHCARE. ";

                        if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                        {
                            sendSMS(phn,msg);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.SEND_SMS},100);
                        }

                        Toast.makeText(getApplicationContext(),"Registration successfull",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,LoginActivity.class));

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password And Confirm Password are not match",Toast.LENGTH_SHORT).show();
                    }
                }
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