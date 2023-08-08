package com.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Date;

public class BD  extends SQLiteOpenHelper{

    public static final String TeamID="_idT";
    public static final String ID="_id";
    public static final String Name="name";
    public static final String Image="image";
    public static final String Assists="assists";
    private static final String DATABASE="Tablas";

    private static final String TABLE1="Team";
    private static final String TABLE2="Students";
    private static final String TABLE3="Assists";

    private static final int Database_Version=1;

    public BD(Context context){
        super(context, DATABASE, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.execSQL("create table " + TABLE1 + " (" +
                TeamID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Name + " TEXT)");
        BaseDeDatos.execSQL("create table " + TABLE2 + " (" +
                ID + " TEXT PRIMARY KEY AUTOINCREMENT, " +
                Name + " TEXT," + Image + " TEXT," +
                TeamID  + " TEXT, FOREIGN KEY (" + TeamID + ") REFERENCES " + TABLE1 + "("+TeamID+"))");
        BaseDeDatos.execSQL("create table " + TABLE3 + " (" +
                ID + " TEXT PRIMARY KEY, " +
                Assists + " BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        onCreate(sqLiteDatabase);
    }

    public void Add(String name){
        ContentValues values = new ContentValues();
        values.put(Name, name);
        this.getWritableDatabase().insert(TABLE1, null, values);
    }

    public void Add(String Id, String name, String image, String IdT){
        ContentValues values = new ContentValues();
        values.put(ID, Id);
        values.put(Name, name);
        values.put(Image, image);
        values.put(TeamID, IdT);
        this.getWritableDatabase().insert(TABLE2, null, values);
    }

    public void Add(String TID, String SID, Date Date, Boolean Assist){
        ContentValues values = new ContentValues();
        values.put(ID, TID+SID+Date.toString());
        values.put(Assists, Assist);
        this.getWritableDatabase().insert(TABLE3, null, values);
    }

    public Cursor get(String TABLE, String condition){
        String[] args= new String[]{condition};
        Cursor c= this.getReadableDatabase().query(TABLE, null, Name+"=?",args,null,null, null);
        return c;
    }

    public void delete(String TABLE, String condition){
        String args[]= {condition};
        this.getWritableDatabase().delete(TABLE,Name +"=?", args);
    }

    public void update(String name, String condition){
        ContentValues values = new ContentValues();
        values.put(Name, name);
        String args[]= {condition};
        this.getWritableDatabase().update(TABLE1, values,Name +"=?",args);
    }

    public void update(String name, String image, String Id, String condition){
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Image, image);
        values.put(ID, Id);
        String args[]= {condition};
        this.getWritableDatabase().update(TABLE2, values,Name +"=?",args);
    }

    public void update(String TID, String SID, Date Date, Boolean Assist){
        ContentValues values = new ContentValues();
        values.put(Assists, Assist);
        String args[]= {TID+SID+Date.toString()};
        this.getWritableDatabase().update(TABLE3, values,ID +"=?",args);
    }

    public Cursor Get(String TABLE){
        Cursor c= this.getReadableDatabase().query(TABLE, null, null, null,null,null, null);
        return c;
    }

    public void deleteTablas(String TABLE){
        this.getWritableDatabase().delete(TABLE,null,null);
    }

}
