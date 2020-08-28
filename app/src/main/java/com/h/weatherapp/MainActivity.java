package com.h.weatherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private String TAG = "log";

    private TextView summary;
    private ImageView iconName;
    private TextView temperature;
    private TextView apparentTemperature;
    private TextView pressure;
    private TextView windSpeed;

    private Loader<String> loader;

    public static String COORD_A = "coord_a";
    public static String COORD_B = "coord_b";
    private String[] piterCoordinate = {"59.939095","30.315868"};

    private String summaryString;
    private String iconString;
    private String temperatureString;
    private String apparentTemperatureString;
    private String pressureString;
    private String windSpeedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //проверка подключения к интернет
        ChekInternetConnection.check(this);

        summary = (TextView)findViewById(R.id.summary);
        iconName = (ImageView)findViewById(R.id.icon);
        temperature = (TextView)findViewById(R.id.temperature);
        apparentTemperature = (TextView)findViewById(R.id.apparentTemperature);
        pressure = (TextView)findViewById(R.id.pressure);
        windSpeed = (TextView)findViewById(R.id.windSpeed);


        WeatherDb weatherDb = new WeatherDb(this);
        Cursor cursor = weatherDb.query("select * from " + WeatherCache.TABLE_NAME + " where " +
                WeatherCache.CITY + "=?",new String[]{WeatherCache.PITER});
        cursor.moveToFirst();

        int columnIndexQuery = cursor.getColumnIndex(WeatherCache.WEATHER_QUERY_NAME);
        LastWeatherQuery.LAST_WEATHER_QUERY = cursor.getString(columnIndexQuery);

        int columnIndexTime = cursor.getColumnIndex(WeatherCache.LAST_WEATHER_TIME);
        long time = Long.parseLong(cursor.getString(columnIndexTime));

        Bundle bundle = new Bundle();
        bundle.putString(COORD_A,piterCoordinate[0]);
        bundle.putString(COORD_B,piterCoordinate[1]);


        long lastUpload = System.currentTimeMillis() - time;

        if (LastWeatherQuery.LAST_WEATHER_QUERY == null || lastUpload > 3600000){
            loader = getSupportLoaderManager().initLoader(1,bundle,this);
            Toast.makeText(this,"Данные о погоде устарели\nи были обновлени",Toast.LENGTH_LONG).show();
        }

        try {

            JSONWeather jsonWeather = new JSONWeather(new JSONObject(LastWeatherQuery.LAST_WEATHER_QUERY));

            CurrentWeatherPiter.summary = jsonWeather.getSummary();
            CurrentWeatherPiter.icon = jsonWeather.getIcon();
            CurrentWeatherPiter.apparentTemperature = jsonWeather.getApparentTemperature();
            CurrentWeatherPiter.temperature = jsonWeather.getTemperature();
            CurrentWeatherPiter.pressure = jsonWeather.getPressure();
            CurrentWeatherPiter.windSpeed = jsonWeather.getWindSpeed();

            summaryString = jsonWeather.getSummary();
            iconString = jsonWeather.getIcon();
            temperatureString = jsonWeather.getTemperature();
            apparentTemperatureString = jsonWeather.getTemperature();
            pressureString = jsonWeather.getPressure();
            windSpeedString = jsonWeather.getWindSpeed();

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: ошибка json в MainActivity->Piter");
        }

        weatherDb.close();

        try {

            summary.setText(summaryString);
            iconName.setImageResource(Weather.setWeatherImage(iconString));
            temperature.setText(Weather.getCelsius(temperatureString));
            apparentTemperature.setText(Weather.getCelsius(apparentTemperatureString));
            pressure.setText(pressureString + " мм.р.ст");
            windSpeed.setText(windSpeedString + " м/c");

        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        Toolbar toolbar = (Toolbar)findViewById()

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);

        FragmentManager manager = getSupportFragmentManager();
        SimpleFragmentPageAdapter adapter = new SimpleFragmentPageAdapter(manager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                switch (i){

                    case 0:
                        summary.setText(summaryString);
                        iconName.setImageResource(Weather.setWeatherImage(iconString));
                        temperature.setText(Weather.getCelsius(CurrentWeatherPiter.temperature));
                        apparentTemperature.setText(Weather.getCelsius(CurrentWeatherPiter.apparentTemperature));
                        pressure.setText(CurrentWeatherPiter.pressure + " мм.р.ст");
                        windSpeed.setText(CurrentWeatherPiter.windSpeed + " м/c");
                        break;

                    case 1:
                        summary.setText(summaryString);
                        iconName.setImageResource(Weather.setWeatherImage(iconString));
                        temperature.setText(Weather.getCelsius(CurrentWeatherMoscow.temperature));
                        apparentTemperature.setText(Weather.getCelsius(CurrentWeatherMoscow.apparentTemperature));
                        pressure.setText(CurrentWeatherMoscow.pressure + " мм.р.ст");
                        windSpeed.setText(CurrentWeatherMoscow.windSpeed + " м/c");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // назначение анимации перелистывания пейджеру
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        loader = new WeatherAsyncTaskLoader(this,bundle);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
        //Добавление данных о времени запроса и результат запроса
        WeatherDb weatherDb = new WeatherDb(this);
        ContentValues contentValues = new ContentValues();

        contentValues.put(WeatherCache.LAST_WEATHER_TIME,String.valueOf(System.currentTimeMillis()));
        contentValues.put(WeatherCache.WEATHER_QUERY_NAME,result);

        weatherDb.update(WeatherCache.TABLE_NAME,contentValues,WeatherCache.COLUMN_ID + "=" + WeatherCache.COLUMN_ID_PITER,null);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    // адаптер для вьюпейджера
    class SimpleFragmentPageAdapter extends FragmentPagerAdapter {

        public SimpleFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new PiterFragment();
                case 1:
                    return new MoscowFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Санкт-Петербург";
                case 1: return "Москва";
            }
            return super.getPageTitle(position);
        }
    }
}