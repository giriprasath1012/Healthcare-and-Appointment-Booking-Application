package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    private String[][] history_details={};
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lst=findViewById(R.id.listviewhis);

        SharedPreferences sharedpreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username= sharedpreferences.getString("username","").toString();

        Database db=new Database(getApplicationContext(),"healthcare",null,1);

        ArrayList dbData=db.getHistory(username);


        history_details=new String[dbData.size()][];
        for(int i=0;i<history_details.length;i++)
        {
            history_details[i]=new String[5];
            String arrData=dbData.get(i).toString();
            String[] strData=arrData.split(java.util.regex.Pattern.quote("$"));
            history_details[i][0]=strData[0];
            history_details[i][1]=strData[1];//+" "+strData[3];
            if(strData[7].compareTo("medicine")==0)
            {
                history_details[i][3]="Del : "+strData[4];
            }
            else
            {
                history_details[i][3]="Del :"+strData[4]+"   "+strData[5];
            }
            history_details[i][2]="Rs."+strData[6];
            history_details[i][4]=strData[7];
        }

        list = new ArrayList();
        for(int i=0;i<history_details.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", history_details[i][0]);
            item.put("line2", history_details[i][1]);
            item.put("line3", history_details[i][2]);
            item.put("line4", history_details[i][3]);
            item.put("line5", history_details[i][4]);
            list.add(item);
        }


        sa= new SimpleAdapter( this,list,
                R.layout.multi_lines,
                new String[] { "line1","line2","line3","line4","line5"},
                new int[] {R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
        lst.setAdapter(sa);
    }
}