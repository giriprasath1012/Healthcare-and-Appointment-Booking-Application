package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BookActivity extends AppCompatActivity {

    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton,btnbook,btnback;
    Calendar cal=Calendar.getInstance();
    String phn,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tv = findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextAppFullName);
        ed2 = findViewById(R.id.editTextAppAddress);
        ed3 = findViewById(R.id.editTextAppContactNumber);
        ed4 = findViewById(R.id.editTextAppFees);
        dateButton = findViewById(R.id.buttonAppDatePicker);
        timeButton = findViewById(R.id.buttonAppTimePicker);
        btnbook = findViewById(R.id.buttonBook);
        //btnback = findViewById(R.id.buttonAppBack);

        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        ed4.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contact = it.getStringExtra("text4");
        String fees = it.getStringExtra("text5");

        tv.setText(title);
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText("Consultant fees: " + fees + "/-");


        // datepicker
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        // timepicker
        initTimePicker();
        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

      /*  btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookActivity.this,FDActivity.class));
            }
        });  */

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Database db=new Database(getApplicationContext(),"healthcare",null,1);
                SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username= sharedpreferences.getString("username","").toString();


                if(db.checkAppointmentExists(title+" =>"+fullname,address,contact,dateButton.getText().toString(),timeButton.getText().toString())==1)
                {
                    Toast.makeText(getApplicationContext(), "Appointment Already Booked...", Toast.LENGTH_LONG).show();
                }
                else
                {

                    db.addOrder(username,title+" =>"+fullname,address,contact,0,dateButton.getText().toString(),timeButton.getText().toString(),Float.parseFloat(fees),"appointment");
                    Toast.makeText(getApplicationContext(),"Your Appointment is Booked Successfully...",Toast.LENGTH_LONG).show();


                    phn=db.getContact(username);
                    msg="      HEALTH CARE  \nYour Booking Is Confirmed\n"+"\nUsername :"+username+"\n"+fullname+"\nDate :"+dateButton.getText().toString()+"\nTime :"+timeButton.getText().toString()+"\nCost : Rs "+Float.parseFloat(fees);

                    if(ContextCompat.checkSelfPermission(BookActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                    {
                        sendSMS(phn,msg);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(BookActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                    }
                    startActivity(new Intent(BookActivity.this,HomeActivity.class));
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

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                //i1=i1+1;

                cal.set(Calendar.YEAR,i);
                cal.set(Calendar.MONTH,i1);
                cal.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String sdate=dateformat.format(cal.getTime());
                dateButton.setText(sdate);

            }
        };

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog =new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }

    private void initTimePicker()
    {
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timepicker, int i, int i1) {

                //cal.set(Calendar.HOUR_OF_DAY,i);
                //cal.set(Calendar.MINUTE,i1);

               // SimpleDateFormat timeformat=new SimpleDateFormat("HH:MIN", Locale.getDefault());
               // String stime=timeformat.format(cal.getTime());

                timeButton.setText(String.format("%02d:%02d",i,i1));
            }
        };
        Calendar cal=Calendar.getInstance();
        int hrs=cal.get(Calendar.HOUR);
        int min=cal.get(Calendar.MINUTE);

        int style= AlertDialog.THEME_HOLO_DARK;
        timePickerDialog =new TimePickerDialog(this,style,timeSetListener,hrs,min,true);
    }




}


