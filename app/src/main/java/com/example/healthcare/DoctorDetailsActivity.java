package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {

    private String[][] doctor_details1 =
            {
                    {"Doctor Name : Deepak ","Hospital Address : Chennai","Exp : 5yrs","Mobile No:9658234855","300"},
                    {"Doctor Name : Sakshi ","Hospital Address : Coimbatore","Exp : 15yrs","Mobile No:9856147562","500"},
                    {"Doctor Name : Adhithya ","Hospital Address : Coimbatore","Exp : 3yrs","Mobile No:9856974265","200"},
                    {"Doctor Name : Apoorva ","Hospital Address : Bangalore","Exp : 12yrs","Mobile No:9789654786","800"},
                    {"Doctor Name : Seema ","Hospital Address : Pollachi","Exp : 2yrs","Mobile No:9456221789","200"}
            };
    private String[][] doctor_details2 =
            {
                    {"Doctor Name : Vinay ","Hospital Address : Salem","Exp : 9yrs","Mobile No:9434345879","550"},
                    {"Doctor Name : Zara ","Hospital Address : Coimabote","Exp : 12yrs","Mobile No:9825555662","600"},
                    {"Doctor Name : Aryan ","Hospital Address : Bangalore","Exp : 8yrs","Mobile No:9856789455","500"},
                    {"Doctor Name : Shreya ","Hospital Address : Chennai","Exp : 4yrs","Mobile No:9784521565","400"},
                    {"Doctor Name : Kiara ","Hospital Address : Palakad","Exp : 5yrs","Mobile No:8566259654","500"}
            };
    private String[][] doctor_details3 =
            {
                    {"Doctor Name : Arnav ","Hospital Address : Delhi","Exp : 11yrs","Mobile No:9373486998","1000"},
                    {"Doctor Name : Dhara ","Hospital Address : Chennai","Exp : 8yrs","Mobile No:9356837469","600"},
                    {"Doctor Name : Zoya ","Hospital Address : Coimbatore","Exp : 4yrs","Mobile No:9637469369","500"},
                    {"Doctor Name : Varun ","Hospital Address : Mumbai","Exp : 7yrs","Mobile No:9727398372","700"},
                    {"Doctor Name : Shiddharth ","Hospital Address : Bangalore","Exp : 10yrs","Mobile No:9368374693","900"}
            };
    TextView tv;
    Button btn;
    String[][] doctor_details={};
    HashMap<String,String> Item;
    ArrayList list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tv=findViewById(R.id.textviewDDTitle2);
       // btn=findViewById(R.id.buttonDDBack);

        Intent it=getIntent();
        String title=it.getStringExtra("title");
        tv.setText(title);


        if(title.compareTo("General_Medicine")==0)
            doctor_details=doctor_details1;
        else
        if(title.compareTo("Dentist")==0)
            doctor_details=doctor_details2;
        else
        if(title.compareTo("Cardiologist")==0)
            doctor_details=doctor_details3;

       /* btn.setOnClickListener(new  View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(new Intent(DoctorDetailsActivity.this,FDActivity.class));

            }
        }); */
        list = new ArrayList();
        for(int i=0;i<doctor_details.length;i++)
        {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("line1",doctor_details[i][0]);
            item.put("line2",doctor_details[i][1]);
            item.put("line3",doctor_details[i][2]);
            item.put("line4",doctor_details[i][3]);
            item.put("line5","Cons Fees:"+doctor_details[i][4]+"/-");
            list.add(item);
        }
       sa= new SimpleAdapter(this,list,
                R.layout.multi_lines,
                new String[]{"line1","line2","line3","line4","line5"},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e}
        );
        ListView lst=findViewById(R.id.listViewDD);
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view,int i,long l)
            {
                Intent it= new Intent(  DoctorDetailsActivity.this,BookActivity.class);
                it.putExtra("text1",title);
                it.putExtra("text2",doctor_details[i][0]);
                it.putExtra("text3",doctor_details[i][1]);
                it.putExtra("text4",doctor_details[i][3]);
                it.putExtra("text5",doctor_details[i][4]);
                startActivity(it);
            }

        });
    }
}