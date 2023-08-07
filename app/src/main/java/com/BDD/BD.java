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
    public static final String Imagen="imagen";
    public static final String Assists="assists";
    public static final String DATE= "date";
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
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Name + " TEXT," + Imagen + " TEXT," +
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
        ContentValues valores = new ContentValues();
        valores.put(Name, name);
        this.getWritableDatabase().insert(TABLE1, null, valores);
    }

    public void Add(String name, String imagen, String Id){
        ContentValues valores = new ContentValues();
        valores.put(Name, name);
        valores.put(Imagen, imagen);
        valores.put(TeamID, Id);
        this.getWritableDatabase().insert(TABLE2, null, valores);
    }

    public void Add(String TID, String SID, Date Date, Boolean Assist){
        ContentValues valores = new ContentValues();
        valores.put(ID, TID+SID+Date.toString());
        valores.put(Assists, Assist);
        this.getWritableDatabase().insert(TABLE3, null, valores);
    }

    public Cursor get(String TABLE, String condicion){
        String[] args= new String[]{condicion};
        Cursor c= this.getReadableDatabase().query(TABLE, null, Name+"=?",args,null,null, null);
        return c;
    }

    public void delete(String TABLE, String condicion){
        String args[]= {condicion};
        this.getWritableDatabase().delete(TABLE,Name +"=?", args);
    }

    public void update(String name, String condicion){
        ContentValues valores = new ContentValues();
        valores.put(Name, name);
        String args[]= {condicion};
        this.getWritableDatabase().update(TABLE1, valores,Name +"=?",args);
    }

    public void update(String name, String imagen, String Id, String condicion){
        ContentValues valores = new ContentValues();
        valores.put(Name, name);
        valores.put(Imagen, imagen);
        valores.put(ID, Id);
        String args[]= {condicion};
        this.getWritableDatabase().update(TABLE2, valores,Name +"=?",args);
    }

    public void update(String TID, String SID, Date Date, Boolean Assist){
        ContentValues valores = new ContentValues();
        valores.put(Assists, Assist);
        String args[]= {TID+SID+Date.toString()};
        this.getWritableDatabase().update(TABLE3, valores,ID +"=?",args);
    }

    public Cursor Get(String TABLE){
        Cursor c= this.getReadableDatabase().query(TABLE, null, null, null,null,null, null);
        return c;
    }

    public void deleteTablas(String TABLE){
        this.getWritableDatabase().delete(TABLE,null,null);
    }

}
