package com.h.weatherapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

public class PiterFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "log";

    public static String COORD_A = "coord_a";
    public static String COORD_B = "coord_b";
    private String[] piterCoordinate = {"59.939095","30.315868"};
    private Loader<String> loader;
    private TextView piterWeather;
    private static int LOADER_ID = 2;
    private Bundle bundle;

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;

    private String lastWeatherQuery;
    private JSONObject jsonQueryObject;

    //массив с json-ами дней недели
    private JSONArray days;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshPiter;

    public String SUMMARY = "summary";
    private String ICON = "icon" ;
    private String TEMPERATURE_LOW = "temperatureLow";
    private String TEMPERATURE_HIGH = "temperatureHigh";
    private String APPARENT_TEMPERATURE_LOW = "apparentTemperatureLow";
    private String APPARENT_TEMPERATURE_HIGH = "apparentTemperatureHigh";
    private String PRESSURE = "pressure";
    private String WIND_SPEED = "windSpeed";

    private List<WeatherItem> weatherDayList = new ArrayList<>();

    public PiterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создание объекта bundle для передачи координат
        bundle = new Bundle();
        bundle.putString(COORD_A,piterCoordinate[0]);
        bundle.putString(COORD_B,piterCoordinate[1]);

        lastWeatherQuery = LastWeatherQuery.LAST_WEATHER_QUERY;

        // парсинг json

//        try {
//
//            JSONWeather jsonWeather = new JSONWeather(new JSONObject(LastWeatherQuery.LAST_WEATHER_QUERY));
//
//            days = jsonWeather.getDays();
//
//        } catch (JSONException e) {
//            Log.d(TAG, "JSONException: ошибка json в Piter");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        for (int i = 0; i < days.length();i++) {
//            try {
//
//                JSONObject day = new JSONObject(days.getString(i));
//
//                String temp = day.getString("temperature");
//                String apparentTemp = day.getString("apparentTemperature");
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
//
//        View view = inflater.inflate(R.layout.piter_fragment, container, false);
//
//        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewPiter);
//        WeatherAdapter weatherAdapter = new WeatherAdapter(getContext(),weatherDayList);
//        recyclerView.setAdapter(weatherAdapter);
//
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);

//        return view;

        return inflater.inflate(R.layout.piter_fragment, container, false);
    }

    /*
    контекст активити для метода onStart уже существует
     */
    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshPiter = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipeRefreshPiter);
        swipeRefreshPiter.setOnRefreshListener(this);

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        Loader<String> loader = new WeatherAsyncTaskLoader(getContext(),bundle);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {

        // swipeToRefresh, его обновление после загрузки
        swipeRefreshPiter.setRefreshing(false);

        //Добавление данных о времени запроса и результат запроса
        WeatherDb weatherDb = new WeatherDb(getContext());
        ContentValues contentValues = new ContentValues();

        contentValues.put(WeatherCache.LAST_WEATHER_TIME,String.valueOf(System.currentTimeMillis()));
        contentValues.put(WeatherCache.WEATHER_QUERY_NAME,result);

        weatherDb.update(WeatherCache.TABLE_NAME,contentValues,WeatherCache.COLUMN_ID + "=" + WeatherCache.COLUMN_ID_PITER,null);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        loader.onContentChanged();
    }

    @Override
    public void onRefresh() {
        loader = getLoaderManager().initLoader(LOADER_ID,bundle,this);
        Toast.makeText(getContext(),"Данные обновлены",Toast.LENGTH_LONG).show();
    }

}