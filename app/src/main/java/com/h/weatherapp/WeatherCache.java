package com.h.weatherapp;
/*
кэширование строки запроса и времени запроса, для дальнейшего извлечения,
будет просиходить сверка временм запроса,
если время больше +3600 (000) секунд,то невным
способом через loadermanager будет отправляться запрос для получения
новых данных.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherCache extends SQLiteOpenHelper {

    public static final String DATA_BASE_NAME = "weatherDataBase";
    public static final String TABLE_NAME = "weather";
    private static final int VERSION = 1;

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_ID_MOSCOW = "1";
    public static final String COLUMN_ID_PITER = "2";
    public static final String CITY = "city";
    public static final String MOSCOW = "moscow";
    public static final String PITER = "piter";

    public static final String WEATHER_QUERY_NAME = "weatherQuery";
    public static final String WEATHER_QUERY_STRING = "weatherQuery";
    public static final String LAST_WEATHER_TIME = "time";
    public static final String LAST_WEATHER_TIME_VALUE = String.valueOf(0);



    public WeatherCache(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + CITY + " TEXT, "
                + WEATHER_QUERY_NAME + " TEXT, "
                + LAST_WEATHER_TIME + " TEXT);");

        insertRow(db,COLUMN_ID,COLUMN_ID_MOSCOW,CITY,MOSCOW,WEATHER_QUERY_NAME,WEATHER_QUERY_STRING,LAST_WEATHER_TIME,LAST_WEATHER_TIME_VALUE);
        insertRow(db,COLUMN_ID,COLUMN_ID_PITER,CITY,PITER,WEATHER_QUERY_NAME,WEATHER_QUERY_STRING,LAST_WEATHER_TIME,LAST_WEATHER_TIME_VALUE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertRow(SQLiteDatabase db,
                           String columnId,String columnIdValue,
                           String columnCity, String cityValue,
                           String weatherColumn,String weatherValue,
                           String timeColumn, String timeValue){
        ContentValues contentValuesMoscow = new ContentValues();
        contentValuesMoscow.put(columnId,columnIdValue);
        contentValuesMoscow.put(columnCity,cityValue);
        contentValuesMoscow.put(weatherColumn,weatherValue);
        contentValuesMoscow.put(timeColumn,timeValue);

        db.insert(TABLE_NAME,null,contentValuesMoscow);
    }
}
