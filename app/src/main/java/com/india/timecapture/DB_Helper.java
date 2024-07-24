package com.india.timecapture;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DB_Helper extends SQLiteOpenHelper {

    public static final String AUTHORITY = "com.india.timecapture.db";
    private static final String DATABASE_NAME = "datetime.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "datetime_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATETIME + " INTEGER," // Store datetime as a long
                + COLUMN_LATITUDE + " TEXT,"
                + COLUMN_LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void putData(long datetime, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_DATETIME + ", " + COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE + ") VALUES (" + datetime + ", '" + latitude + "', '" + longitude + "')");
        db.close();
    }

    public ArrayList<HashMap<String, String>> getData() {
        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DATETIME + ", " + COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATETIME + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> data = new HashMap<>();
                long datetime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATETIME));
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(datetime));
                data.put(COLUMN_DATETIME, formattedDate);
                data.put(COLUMN_LATITUDE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)));
                data.put(COLUMN_LONGITUDE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE)));
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
}