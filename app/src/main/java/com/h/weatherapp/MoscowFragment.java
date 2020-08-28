package com.h.weatherapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoscowFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "log";

    private String[] moscowCoordinate = {"55.7522200","37.6155600"};
    public static String COORD_A = "coord_a";
    public static String COORD_B = "coord_b";
    private Bundle bundle;
    private static int LOADER_ID = 1;
    private Loader<String> loader = null;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;
    private String lastWeatherQuery;
    private SwipeRefreshLayout swipeRefreshMoscow;
    private JSONArray days;
    private List<WeatherItem> weatherDayList = new ArrayList<>();

    public String SUMMARY = "summary";
    private String ICON = "icon" ;
    private String TEMPERATURE_LOW = "temperatureLow";
    private String TEMPERATURE_HIGH = "temperatureHigh";
    private String APPARENT_TEMPERATURE_LOW = "apparentTemperatureLow";
    private String APPARENT_TEMPERATURE_HIGH = "apparentTemperatureHigh";
    private String PRESSURE = "pressure";
    private String WIND_SPEED = "windSpeed";

    private JSONObject jsonQueryObject;

    public MoscowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

        bundle = new Bundle();
        bundle.putString(COORD_A,moscowCoordinate[0]);
        bundle.putString(COORD_B,moscowCoordinate[1]);

        sqLiteOpenHelper = new WeatherCache(getContext());
        db = sqLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + WeatherCache.TABLE_NAME + " where " +
                WeatherCache.CITY + "=?",new String[]{WeatherCache.MOSCOW});

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(WeatherCache.LAST_WEATHER_TIME);
        long time = Long.parseLong(cursor.getString(columnIndex));

        long lastUpload = System.currentTimeMillis() - time;

        if (lastUpload > 3600000){
            loader = getLoaderManager().initLoader(LOADER_ID,bundle,this);
        } else {
            int columnIndexQuery = cursor.getColumnIndex(WeatherCache.WEATHER_QUERY_NAME);
            lastWeatherQuery = cursor.getString(columnIndexQuery);

            LastWeatherQuery.LAST_WEATHER_QUERY = lastWeatherQuery;        }

        cursor.close();
        db.close();

        try {
            jsonQueryObject = new JSONObject(LastWeatherQuery.LAST_WEATHER_QUERY);
            //получение вложенно
            JSONObject jsonCurrently = new JSONObject(jsonQueryObject.getString("currently"));
            JSONObject jsonWeekly = new JSONObject(jsonQueryObject.getString("daily"));

            days = jsonWeekly.getJSONArray("data");

            CurrentWeatherMoscow.summary = jsonCurrently.getString(SUMMARY);
            CurrentWeatherMoscow.icon = jsonCurrently.getString(ICON);

            double a = Double.parseDouble(jsonCurrently.getString(TEMPERATURE_LOW));
            double b = Double.parseDouble(jsonCurrently.getString(TEMPERATURE_HIGH));
            double c = (a + b) / 2;
            Log.d(TAG, "CurrentWeatherMoscow: c" + c);
            CurrentWeatherMoscow.temperature = String.valueOf(c);

            double d = Double.parseDouble(jsonCurrently.getString(APPARENT_TEMPERATURE_LOW));
            double e = Double.parseDouble(jsonCurrently.getString(APPARENT_TEMPERATURE_HIGH));
            double f = (d + e) /2;
            Log.d(TAG, "CurrentWeatherMoscow: f" + f);
            CurrentWeatherMoscow.apparentTemperature = String.valueOf(f);

            CurrentWeatherMoscow.pressure = jsonCurrently.getString(PRESSURE);
            CurrentWeatherMoscow.windSpeed = jsonCurrently.getString(WIND_SPEED);

//            Log.d(TAG, "onCreate MoscowFragment: " + lastWeatherQuery);

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: ошибка json в MoscowFragment");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        for (int i = 0; i < days.length();i++) {
//            try {
//                JSONObject day = new JSONObject(days.getString(i));
//
//                double a = Double.parseDouble(day.getString("temperatureLow"));
//                double b = Double.parseDouble(day.getString("temperatureHigh"));
//                double result = (a + b) / 2;
//
//                String temp = String.valueOf(result);
//                Log.d(TAG, "onCreateView: result " + result);
//
//                double c = Double.parseDouble(day.getString("apparentTemperatureLow"));
//                double d = Double.parseDouble(day.getString("apparentTemperatureHigh"));
//                double apparentResult = (c + d)/2;
//
//                String apparentTemp = String.valueOf(apparentResult);
//                Log.d(TAG, "onCreateView: result " + apparentTemp);
//
//                weatherDayList.add(new WeatherItem(day.getString("summary"),day.getString("icon"),
//                        temp,apparentTemp,
//                        day.getString("pressure"),day.getString("windSpeed")));
//
//            } catch (JSONException e) {
//                Log.d(TAG, "onCreateView: Ошибка создания объекста weatherDayList" );
//            }
//        }
//

        View view = inflater.inflate(R.layout.moscow_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewMoscow);
        WeatherAdapter weatherAdapter = new WeatherAdapter(getContext(),weatherDayList);
        recyclerView.setAdapter(weatherAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshMoscow = getActivity().findViewById(R.id.swipeToRefreshMoscow);
        swipeRefreshMoscow.setOnRefreshListener(this);

    }

    // колбэки лоадерменеджера
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        Loader<String> loader = new WeatherAsyncTaskLoader(getContext(),bundle);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
//

        // swipeToRefresh, его обновление после загрузки
        swipeRefreshMoscow.setRefreshing(false);

        //Добавление данных о времени запроса и результат запроса
        sqLiteOpenHelper = new WeatherCache(getContext());
        db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(WeatherCache.LAST_WEATHER_TIME,String.valueOf(System.currentTimeMillis()));
        contentValues.put(WeatherCache.WEATHER_QUERY_NAME,result);

        db.update(WeatherCache.TABLE_NAME,contentValues,
                WeatherCache.COLUMN_ID + "=" + WeatherCache.COLUMN_ID_MOSCOW,null);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
//        loader.onContentChanged();
    }

    @Override
    public void onRefresh() {
        loader = getLoaderManager().initLoader(LOADER_ID,bundle,this);
        Toast.makeText(getContext(),"Данные обновлены",Toast.LENGTH_LONG).show();
    }

}
