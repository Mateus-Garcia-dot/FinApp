package com.example.finapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "financa.db";
    private static final int DB_VERSION = 8;

    public static final String TABLE_NAME = "financa";
    public static final String c1 = "value";
    public static final String c2 = "date";
    public static final String c3 = "tipo";
    public static final String c4 = "filtro";

    private static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER primary key autoincrement, value REAL, date BIGINT, tipo TEXT, filtro TEXT)";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
