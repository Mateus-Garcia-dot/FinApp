package com.example.finapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "financa.db";
    public static final String TABLE_NAME = "financa";
    public static final String c1 = "value";
    public static final String c2 = "date";
    public static final String c3 = "tipo";
    public static final String c4 = "filtro";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(id integer primary key autoincrement, value float, date date, tipo string, filtro string)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
