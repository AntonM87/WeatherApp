package com.h.weatherapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Weather {

    private static final String TAG = "log";
    private static String urlBaseString = "https://api.darksky.net/forecast/3e7e519ea86c8e3fcf67c0f4870513d7/";
    private static String urlEnd = "?lang=ru";
    private static String[] piterCoordinate = {"59.939095","30.315868"};
    private static String[] moscowCoordinate = {"55.7522200", "37.6155600"};
    private static URL url;

    public static String getWeather(String coord_A, String coord_B){

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;

        try {
            url = new URL(urlBaseString + coord_A + "," + coord_B + urlEnd);

            System.out.println(url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

        } catch (IOException e) {
            Log.d("log", "getWeather:method IOException");
        }

        return buffer.toString();
    }

    public static String getCelsius(String temperature){
        double parseDouble = Double.parseDouble(temperature);
        parseDouble = (parseDouble - 32) / 1.8;
        String format = "%.1f °C";
        return String.format(Locale.getDefault(),format,parseDouble);
    }

    public static int setWeatherImage(String value){

        Log.d(TAG, "setWeatherImage: " + value);

        switch (value){
            case "clear-day": return R.drawable.clear_day;
            case "cloudy": return R.drawable.cloudy;
            case "partly-cloudy-day": return R.drawable.partly_cloudy_day;
            case "partly-cloudy-night": return R.drawable.partly_cloudy_night;
            case "rain": return R.drawable.rain;
            default: return R.drawable.ic_baseline_error_24;
        }
    }
    public static String setSummaryTranslate(String value){
        switch (value){
            case "Clear": return "Ясно";
            case "Overcast": return "Пасмурно";
            default: return "Ошибка, setSummaryTranslate" ;
        }
    }
}
