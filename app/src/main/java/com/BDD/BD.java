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
                ID + " TEXT PRIMARY KEY, " +
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

    public void insertTeam(String name) {
        ContentValues values = new ContentValues();
        values.put(Name, name);
        this.getWritableDatabase().insert(TABLE1, null, values);
    }

    public void insertStudent(String Id, String name, String image, String IdT) {
        ContentValues values = new ContentValues();
        values.put(ID, Id);
        values.put(Name, name);
        values.put(Image, image);
        values.put(TeamID, IdT);
        this.getWritableDatabase().insert(TABLE2, null, values);
    }

    public void insertAssist(String TID, String SID, Date Date, Boolean Assist) {
        ContentValues values = new ContentValues();
        values.put(ID, TID + SID + Date.toString());
        values.put(Assists, Assist);
        this.getWritableDatabase().insert(TABLE3, null, values);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, whereClause, whereArgs);
    }

    public void updateTeam(String name, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        values.put(Name, name);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE1, values, whereClause, whereArgs);
    }

    public void updateStudent(String name, String image, String Id, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Image, image);
        values.put(ID, Id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE2, values, whereClause, whereArgs);
    }

    public void updateAssist(String TID, String SID, Date Date, Boolean Assist) {
        ContentValues values = new ContentValues();
        values.put(Assists, Assist);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE3, values, ID + "=?", new String[]{TID + SID + Date.toString()});
    }

    public Cursor getAll(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table, null, null, null, null, null, null);
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }

    public boolean isDataAlreadyExists(String Id, String name, String image) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE2, new String[]{ID}, ID + "=? AND " + Name + "=? AND " + Image + "=?",
                new String[]{Id, name, image}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

}
