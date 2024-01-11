package com.example.healthcare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CartlabActivity extends AppCompatActivity {

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton,btncheckout,btnback;
    private String[][] packages={};
    Calendar cal=Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlab);

        dateButton = findViewById(R.id.buttonCartDatePicker);
        timeButton = findViewById(R.id.buttonCartTimePicker);
        btncheckout = findViewById(R.id.buttonCartcheckout);
        //btnback = findViewById(R.id.buttonCartBack);
        tvTotal=findViewById(R.id.textViewcarttotalPrice);
        lst=findViewById(R.id.listViewCart);

        SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username= sharedpreferences.getString("username","").toString();

        Database db=new Database(getApplicationContext(),"healthcare",null,1);
        float totalamount =0;
        ArrayList dbData=db.getCartData(username,"lab");

        packages =new String[dbData.size()][];
        for(int i=0;i<packages.length;i++)
        {
            packages[i]=new String[5];
        }

        for(int i=0;i< dbData.size();i++)
        {
            String arrData=dbData.get(i).toString();
            String[] strData=arrData.split(java.util.regex.Pattern.quote("$"));
            packages[i][0]=strData[0];
            packages[i][4]="Cost : "+strData[1]+"/-";
            totalamount =totalamount+Float.parseFloat(strData[1]);
        }
        tvTotal.setText("Total Cost : "+totalamount);

        list= new ArrayList();
        for(int i=0;i<packages.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", packages[i][0]);
            item.put("line2", packages[i][1]);
            item.put("line3", packages[i][2]);
            item.put("line4", packages[i][3]);
            item.put("line5", packages[i][4]);
            list.add(item);
        }

        sa= new SimpleAdapter( this,list,
                R.layout.multi_lines,
                new String[] { "line1","line2","line3","line4","line5"},
                new int[] {R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
        lst.setAdapter(sa);

        /*btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartlabActivity.this,LabTestActivity.class));
            }
        }); */

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(  CartlabActivity.this,LabTestBookActivity.class);
                it.putExtra("price",tvTotal.getText());
                it.putExtra("date",dateButton.getText());
                it.putExtra("time",timeButton.getText());
                startActivity(it);
            }
        });

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


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {

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

        int style= AlertDialog.THEME_HOLO_DARK;
        datePickerDialog =new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }

    private void initTimePicker()
    {
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timepicker, int i, int i1) {

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