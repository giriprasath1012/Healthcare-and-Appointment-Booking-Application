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
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername,edPassword;
    Button btn;
    TextView tv,fpass;
    String phn,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername=findViewById(R.id.editTextLoginUser);
        edPassword=findViewById(R.id.editTextLoginPassword);
        btn=findViewById(R.id.buttonLogin);
        tv=findViewById(R.id.textViewNewuser);
        fpass=findViewById(R.id.textViewfpass);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edUsername.getText().toString();
                String password=edPassword.getText().toString();
                Database db=new Database(getApplicationContext(),"healthcare",null,1);

                if(username.length()==0 || password.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please Fill all details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(db.login(username,password)==1)
                    {
                        Toast.makeText(getApplicationContext(),"Login Sucessful",Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedpreferences.edit();
                        editor.putString("username",username);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Username And Password",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Register.class));
            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edUsername.getText().toString();
                Database db=new Database(getApplicationContext(),"healthcare",null,1);
                String pass=db.Pass(username);
                phn=db.getContact(username);
                msg="    HEALTH CARE  \nUsername : "+username+"\nPassword :"+pass;

                if(username.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Fill the Username...",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(db.getuser(username)==1)
                    {
                        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                        {
                            sendSMS(phn,msg);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Username not found ",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Password is send to your registered Phone Number",Toast.LENGTH_LONG).show();
        }
    }

}