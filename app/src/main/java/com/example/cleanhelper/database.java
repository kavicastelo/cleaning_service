package com.example.cleanhelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class database extends SQLiteOpenHelper {

    public static final String dbName = "CHelper"; //set database name
    public static final String uTBL = "userDetails"; //set user details table name
    public static final String lTBL = "loginDetails"; //set login details table name
    public static final String hTBL = "homes";  //set home details table name
    public static final String pTBL = "projects"; //set project details table name
    public static final String cTBL = "cost"; //set prices table name
    public static final String fTBL = "feedback"; //set feedbacks table name
    public static final String bTBL = "budget"; // set budget table name

    //user table columns
    public static final String uCOL1 = "uID";
    public static final String uCOL2 = "name";
    public static final String uCOL3 = "email";
    public static final String uCOL4 = "tp";
    public static final String uCOL5 = "addr";
    public static final String uCOL6 = "city";
    public static final String uCOL7 = "pcode";

    //login table columns
    public static final String lCOL1 = "lID";
    public static final String lCOL2 = "uName";
    public static final String lCOL3 = "uPass";
    public static final String lCOL4 = "uLevel";

    //home table columns
    public static final String hCOL1 = "hID";
    public static final String hCOL2 = "uName";
    public static final String hCOL3 = "rooms";
    public static final String hCOL4 = "bathrooms";
    public static final String hCOL5 = "ftype";
    public static final String hCOL6 = "image";

    //project table columns
    public static final String pCOL1 = "pID";
    public static final String pCOL2 = "user";
    public static final String pCOL3 = "date";
    public static final String pCOL4 = "time";
    public static final String pCOL5 = "accept";

    //cost table columns
    public static final String cCOL1 = "cID";
    public static final String cCOL2 = "rooms";
    public static final String cCOL3 = "bathrooms";
    public static final String cCOL4 = "tile";
    public static final String cCOL5 = "cement";
    public static final String cCOL6 = "carpet";

    //feedbacks table columns
    public static final String fCOL1 = "fID";
    public static final String fCOL2 = "name";
    public static final String fCOL3 = "feedback";

    //budget table columns
    public static final String bCOL1 = "bID";
    public static final String bCOL2 = "user";
    public static final String bCOL3 = "income";
    public static final String bCOL4 = "profit";

    public database(@Nullable Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+lTBL+"(lID INTEGER PRIMARY KEY AUTOINCREMENT, uName TEXT, uPass TEXT, uLevel INTEGER)");
        db.execSQL("CREATE TABLE "+uTBL+"(uID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, tp TEXT, addr TEXT," +
                "city TEXT, pcode TEXT)");
        db.execSQL("CREATE TABLE "+hTBL+"(hID INTEGER PRIMARY KEY AUTOINCREMENT, uName TEXT, rooms INTEGER, bathrooms INTEGER," +
                "ftype INTEGER, image BLOB)");
        db.execSQL("CREATE TABLE "+pTBL+"(pID INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, date TEXT, time TEXT, accept INTEGER)");
        db.execSQL("CREATE TABLE "+cTBL+"(cID INTEGER PRIMARY KEY,rooms REAL, bathrooms REAL, tile TEXT, cement TEXT, carpet TEXT)");
        db.execSQL("CREATE TABLE "+fTBL+"(fID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, feedback TEXT)");
        db.execSQL("CREATE TABLE "+bTBL+"(bID INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, income REAL, profit REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+uTBL);
        db.execSQL("DROP TABLE IF EXISTS "+lTBL);
        db.execSQL("DROP TABLE IF EXISTS "+hTBL);
        db.execSQL("DROP TABLE IF EXISTS "+pTBL);
        db.execSQL("DROP TABLE IF EXISTS "+cTBL);
        db.execSQL("DROP TABLE IF EXISTS "+fTBL);
        db.execSQL("DROP TABLE IF EXISTS "+bTBL);
        onCreate(db);
    }

    //store login details
    public boolean setLogin(String uName, String uPass, String uLevel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(lCOL2,uName);
        contval.put(lCOL3,uPass);
        contval.put(lCOL4,uLevel);
        long result = db.insert(lTBL,null,contval);
        return result != -1;
    }

    //store user details
    public boolean setUser(String name, String email, String tp, String addr, String city, String pcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(uCOL2,name);
        contval.put(uCOL3,email);
        contval.put(uCOL4,tp);
        contval.put(uCOL5,addr);
        contval.put(uCOL6,city);
        contval.put(uCOL7,pcode);
        long result = db.insert(uTBL,null,contval);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //get login detail
    public Cursor getLogin(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+lTBL,null);
        return cursor;
    }

    //store project date to database
    public boolean setProject(String user, String date, String time, String accept){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(pCOL2,user);
        contval.put(pCOL3,date);
        contval.put(pCOL4,time);
        contval.put(pCOL5,accept);
        long result = db.insert(pTBL,null,contval);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //accept project
    public boolean acceptProject(String pID, String accept){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(pCOL5,accept);
        long result = db.update(pTBL,contval,"pID=?", new String[]{pID});
        return result != -1;
    }

    //store feedbacks
    public boolean setFeedback(String name, String feedback){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(fCOL2,name);
        contval.put(fCOL3,feedback);
        long result = db.insert(fTBL,null,contval);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //store home details
    public boolean setHome(String uName, String rooms, String bathrooms, String ftype, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(hCOL2,uName);
        contval.put(hCOL3,rooms);
        contval.put(hCOL4,bathrooms);
        contval.put(hCOL5,ftype);

        // Convert the image into byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] buffer=byteArrayOutputStream.toByteArray();
        contval.put(hCOL6, buffer);

        long result = db.insert(hTBL,null,contval);
        return result != -1;
    }

    //get details from home table
    public Cursor getHome(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor request = db.rawQuery("SELECT * FROM "+hTBL,null);
        return request;
    }

    //get home details to spinner
    public Cursor getHomeInfoToSpinner(String hid)
    {
        SQLiteDatabase dbspn = this.getReadableDatabase();
        Cursor getHomeInfo = dbspn.rawQuery("SELECT * FROM "+hTBL+" WHERE hID="+hid,null);
        return getHomeInfo;
    }

    //get project date and time
    public Cursor getProject(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor request = db.rawQuery("SELECT * FROM "+pTBL,null);
        return request;
    }

    //get home IDs to spinner
    public ArrayList<String> getHomeID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String strSQL = "SELECT hID FROM "+hTBL;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount() != 0)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String hid = cursor.getString(cursor.getColumnIndex("hID"));
                list.add(hid);
            }
        }
        return list;
    }

    //get user details
    public Cursor getUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor request = db.rawQuery("SELECT * FROM "+uTBL,null);
        return request;
    }

    //get feedbacks from table
    public Cursor getFeedbacks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor request = db.rawQuery("SELECT * FROM "+fTBL,null);
        return request;
    }

    //insert prices
    public boolean setPrices(String rooms, String bathrooms, String tile, String cement, String carpet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(cCOL2,rooms);
        contval.put(cCOL3,bathrooms);
        contval.put(cCOL4,tile);
        contval.put(cCOL5,cement);
        contval.put(cCOL6,carpet);
        long result = db.insert(cTBL,null,contval);
        return result != -1;
    }

    //get prices
    public Cursor getPrices(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor request = db.rawQuery("SELECT * FROM " +cTBL,null);
        return request;
    }

    //update prices
    public boolean updatePrices(String cID, String rooms, String bathrooms, String tile, String cement, String carpet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(cCOL2,rooms);
        contval.put(cCOL3,bathrooms);
        contval.put(cCOL4,tile);
        contval.put(cCOL5,cement);
        contval.put(cCOL6,carpet);
        long result = db.update(cTBL,contval,"cID=?", new String[]{cID});
        return result != -1;
    }

    //get login level to spinner
    public Cursor getLoginInfoToSpinner(String lid)
    {
        SQLiteDatabase dbspn = this.getReadableDatabase();
        Cursor getLoginInfo = dbspn.rawQuery("SELECT * FROM "+lTBL+" WHERE lID="+lid,null);
        return getLoginInfo;
    }

    //get login IDs to spinner
    public ArrayList<String> getLoginID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String strSQL = "SELECT lID FROM "+lTBL;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount() != 0)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String lid = cursor.getString(cursor.getColumnIndex("lID"));
                list.add(lid);
            }
        }
        return list;
    }

    //update user level
    public boolean updateLevel(String lID, String uLevel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(lCOL4,uLevel);
        long result = db.update(lTBL,contval,"lID=?", new String[]{lID});
        return result != -1;
    }

    //change admin username and password
    public boolean changeAdmin(String Alevel, String uname, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(lCOL2,uname);
        contval.put(lCOL3,password);
        long result = db.update(lTBL,contval,"uLevel=?", new String[]{Alevel});
        return result != -1;
    }

    //add budget to database
    public boolean addBudget(String name, String income, String profit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();
        contval.put(bCOL2,name);
        contval.put(bCOL3,income);
        contval.put(bCOL4,profit);
        long result = db.insert(bTBL,null, contval);
        return result != -1;
    }

    //get budget details from database
    public Cursor getBudget(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor get = db.rawQuery("SELECT * FROM "+bTBL,null);
        return get;
    }

    public Bitmap getImage(String uname)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Bitmap bt=null;
        Cursor searchimg=db.rawQuery("SELECT image FROM "+hTBL +" WHERE uName ='"+uname+"'",null);
        if(searchimg.moveToNext())
        {
            byte[] img=searchimg.getBlob(0);
            bt= BitmapFactory.decodeByteArray(img,0,img.length);
        }
        return bt;
    }
}
