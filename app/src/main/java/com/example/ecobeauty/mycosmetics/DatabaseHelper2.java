package com.example.ecobeauty.mycosmetics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userproduct.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "users";
    static final String TABLE_W = "wish_product";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID2 = "_id";
    public static final String COLUMN_NAME2 = "name";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_DATE + " INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_W + " (" + COLUMN_ID2
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME2
                + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }
}
