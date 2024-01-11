package com.example.healthcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {

        String qry1="create table users(username text,password text,phoneno text)";
        db.execSQL(qry1);
        String qry2="create table cart(username text,product text,price float,otype text)";
        db.execSQL(qry2);
        String qry3="create table orderplace(username text,fullname text,address text,contactno text,pincode int,date text,time text,amount float,otype text)";
        db.execSQL(qry3);
        String qry4="create table history(username text,fullname text,address text,contactno text,pincode int,date text,time text,amount float,otype text)";
        db.execSQL(qry4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void register(String username, String password, String phone)
    {
        ContentValues cv=new ContentValues();
        cv.put("username",username);
        cv.put("password",password);
        cv.put("phoneno",phone);
        SQLiteDatabase db=getWritableDatabase();
        db.insert("users",null,cv);
        db.close();
    }

    public int login(String username,String password)
    {
        int result=0;
        String str[]=new String[2];
        str[0]=username;
        str[1]=password;
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from users where username=? and password=?",str );
        if(c.moveToFirst())
        {
            result=1;
        }
        return result;
    }

    public void addCart(String username, String product, float price, String otype)
    {
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("product",product);
        cv.put("price",price);
        cv.put("otype",otype);
        SQLiteDatabase db=getWritableDatabase();
        db.insert("cart",null,cv);
        db.close();
    }
    public int checkCart(String username,String product)
    {
        int result=0;
        String str[]= new String[2];
        str[0]= username;
        str[1]=product;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=db.rawQuery("select * from cart where username = ? and product = ?",str);
        if(c.moveToFirst()){
            result=1;
        }
        db.close();
        return result;
    }
    public void removecart(String username,String otype)
    {
        String str[]=new String[2];
        str[0]=username;
        str[1]=otype;
        SQLiteDatabase db=getWritableDatabase();
        db.delete("cart","username=? and otype=?",str);
        db.close();
    }

    public ArrayList getCartData(String username, String otype)
    {
        ArrayList<String> arr=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String str[]=new String[2];
        str[0]=username;
        str[1]=otype;
        Cursor c= db.rawQuery("select * from cart where username=? and otype=? ",str);
        if(c.moveToFirst())
        {
            do
            {
                String product= c.getString(1);
                String price=c.getString(2);
                arr.add(product+"$"+price);
            }
            while(c.moveToNext());
        }
        db.close();
        return arr;
    }

    public void addOrder(String username,String fullname,String address,String contact,int pincode,String date,String time,float price,String otype)
    {
        ContentValues cv=new ContentValues();
        cv.put("username",username);
        cv.put("fullname",fullname);
        cv.put("address",address);
        cv.put("contactno",contact);
        cv.put("pincode",pincode);
        cv.put("date",date);
        cv.put("time",time);
        cv.put("amount",price);
        cv.put("otype",otype);
        SQLiteDatabase db=getWritableDatabase();
        db.insert("orderplace",null,cv);
        db.close();
    }


    public ArrayList getOrderData(String username)
    {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String str[]=new String[1];
        str[0]=username;
        Cursor c =db.rawQuery("select * from orderplace where username = ? ",str);
        if(c.moveToFirst())
        {
            do
            {
                arr.add(c.getString(1)+"$"+c.getString(2)+"$"+c.getString(3)+"$"+c.getString(4)+"$"+c.getString(5)+"$"+c.getString(6)+"$"+c.getString(7)+"$"+c.getString(8));
            }
            while(c.moveToNext());
        }
        db.close();
        return arr;
    }

    public int checkAppointmentExists(String fullname,String address,String contact,String date,String time)
    {
        int result=0,t;
        //t=Integer.valueOf(time);
        String str[]=new String[5];

        str[0]=fullname;
        str[1]=address;
        str[2]=contact;
        str[3]=date;
        str[4]=time;
       // str[5]= String.valueOf(t+10);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=db.rawQuery("select * from orderplace where fullname=? and address=? and contactno=? and date=? and time=?",str);
        if(c.moveToFirst())
        {
            result=1;
        }
        db.close();
        return result;
    }


    public void DeleteOrder(String username)
    {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);

        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String sdate=dateformat.format(cal.getTime());

       // String tdate= String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        SQLiteDatabase db=getWritableDatabase();
        Cursor c= db.rawQuery("select * from orderplace where date(date) < date('"+ sdate+"')  and username ='"+username+"'",null);
        if(c.moveToFirst())
        {
            do
            {
                String user= c.getString(0);
                String fullname=c.getString(1);
                String address= c.getString(2);
                String contact=c.getString(3);
                String pincode= c.getString(4);
                String dates=c.getString(5);
                String time= c.getString(6);
                String amount=c.getString(7);
                String type=c.getString(8);

                ContentValues cv=new ContentValues();
                cv.put("username",user);
                cv.put("fullname",fullname);
                cv.put("address",address);
                cv.put("contactno",contact);
                cv.put("pincode",pincode);
                cv.put("date",dates);
                cv.put("time",time);
                cv.put("amount",amount);
                cv.put("otype",type);

                db.insert("history",null,cv);

            }
            while(c.moveToNext());
        }
        db.close();


        SQLiteDatabase dbs=getWritableDatabase();
        dbs.delete("orderplace","date(date) < date('"+ sdate+"') and username ='"+username+"'",null);
        dbs.close();

    }


    public ArrayList getHistory(String username)
    {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String str[]=new String[1];
        str[0]=username;
        Cursor c =db.rawQuery("select * from history where username = ? ",str);
        if(c.moveToFirst())
        {
            do
            {
                arr.add(c.getString(1)+"$"+c.getString(2)+"$"+c.getString(3)+"$"+c.getString(4)+"$"+c.getString(5)+"$"+c.getString(6)+"$"+c.getString(7)+"$"+c.getString(8));
            }
            while(c.moveToNext());
        }
        db.close();
        return arr;
    }

    public String getContact(String username)
    {
        String phn[]=new String[1];
        phn[0]="";
        String str[]=new String[1];
        str[0]=username;

        SQLiteDatabase db=getReadableDatabase();
        Cursor c =db.rawQuery("select phoneno from users where username = ? ",str);
        if(c.moveToFirst())
        {
            do
            {

                phn[0]=phn[0]+c.getString(0);

            }
            while(c.moveToNext());
        }
        db.close();
        return phn[0];
    }


    public String Pass(String username)
    {
        String pass[]=new String[1];
        pass[0]="";
        String str[]=new String[1];
        str[0]=username;

        SQLiteDatabase db=getReadableDatabase();
        Cursor c =db.rawQuery("select password from users where username = ? ",str);
        if(c.moveToFirst())
        {
            do
            {
                pass[0]=pass[0]+c.getString(0);

            }
            while(c.moveToNext());
        }
        db.close();
        return pass[0];
    }


    public  int getuser(String username)
    {
        int result=0;
        String str[]=new String[1];
        str[0]=username;
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from users where username=?",str );
        if(c.moveToFirst())
        {
            result=1;
        }
        return result;


    }



}
