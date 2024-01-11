package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class LabTestActivity extends AppCompatActivity {

    private String[][] packages=
            {
                    {"Package 1: Full Body Checkup","","","","999"},
                    {"Package 2: Blood Glucose Fasting","","","","299"},
                    {"Package 3: COVID-19 Test","","","","899"},
                    {"Package 4: Thyroid Checkup","","","","499"},
                    {"Package 5: Immunity Checkup","","","","699"},
                    {"Package 6: ECG Test","","","","599"},
            };
    private String[] package_details={
            "Blood Glucose Fasting\n"+
                "Complete Hemogram\n"+
                "HbA1c\n"+
                "Iron Studies\n"+
                "Kidney Function Test\n"+
                "LDH Lactate Dehydrogenase, Serum\n"+
                "Lipid Profile\n"+
                "Liver function test",
            "Blood Glucose Fasting",
            " COVID-19 antibody-IgG",
            "Thyroid Profile-Total (T3,T4 & TSH Ultra-sensitive)",
            "Complete Hemogram\n"+
                    "CRP (C Reactive Protien) Quantitave, Serum\n"+
                    "Iron Studies\n"+
                    "Kidney Function Test\n"+
                    "Vitamin D Total-25 Hydroxy\n"+
                    "Liver Function Test\n"+
                    "Lipid Profile",
            "Heart beat Count\n"+
                    "Deduct Heart Problems"

    };
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    Button btngoToCart,btnBack;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);
        btngoToCart = findViewById(R.id.buttonLTgotocart);
        //btnBack = findViewById(R.id.buttonLTBack);
        listView = findViewById(R.id.listViewLT);

      /*  btnBack.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                startActivity(new Intent(  LabTestActivity.this,HomeActivity.class));
            }
        }); */

        btngoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LabTestActivity.this,CartlabActivity.class));
            }
        });

        list = new ArrayList();
        for(int i=0;i<packages.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", packages[i][0]);
            item.put("line2", packages[i][1]);
            item.put("line3", packages[i][2]);
            item.put("line4", packages[i][3]);
            item.put("line5", "Cost:"+packages[i][4]+"/-");
            list.add(item);
        }


        sa= new SimpleAdapter( this,list,
                R.layout.multiline_lab,
                new String[] { "line1","line2","line3","line4","line5"},
                new int[] {R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
        listView.setAdapter(sa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view,int i,long l) {
                Intent it= new Intent(  LabTestActivity.this,LabTestdetailsActivity.class);
                it.putExtra("text1",packages[i][0]);
                it.putExtra("text2",package_details[i]);
                it.putExtra("text3",packages[i][4]);
                startActivity(it);
            }
        });


    }
}