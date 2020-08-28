package com.h.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDb {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;
    private Cursor cursor;

    WeatherDb(Context context){
        sqLiteOpenHelper = new WeatherCache(context);
        db = sqLiteOpenHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public void update(String table, ContentValues values, String whereClause, String[] whereArgs){
        db.update(table,values,whereClause,whereArgs);
    }

    public Cursor query(String sql, String[] selectionArgs){
        cursor = db.rawQuery(sql,selectionArgs);
        return cursor;
    }

    public void close(){
        cursor.close();
        db.close();
    }
}
