package com.h.weatherapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeather {

    private String TAG = "log";

    private String DATA = "data";
    private String CURRENTLY = "currently";
    private String DAILY = "daily";
    private String SUMMARY = "summary";
    private String ICON = "icon";
    private String TEMPERATURE = "temperature";
    private String APPARENT_TEMPERATURE = "apparentTemperature";
    private String PRESSURE = "pressure";
    private String WIND_SPEED = "windSpeed";


    private String summary;
    private String icon;
    private String temperature;
    private String apparentTemperature;
    private String pressure;
    private String windSpeed;

    private JSONArray days;
    private JSONObject jsonQueryObject;
    private JSONObject jsonCurrently;

    JSONWeather(JSONObject jsonObject) {
        //получение вложенного

        this.jsonQueryObject = jsonObject;

        try {

            jsonCurrently = new JSONObject(jsonQueryObject.getString(CURRENTLY));
            JSONObject jsonWeekly = new JSONObject(jsonQueryObject.getString(DAILY));

            // вернут массив days для recucleView
            days = jsonWeekly.getJSONArray(DATA);

            summary = jsonCurrently.getString(SUMMARY);
            icon = jsonCurrently.getString(ICON);

            temperature = jsonCurrently.getString(TEMPERATURE);
            apparentTemperature = jsonCurrently.getString(APPARENT_TEMPERATURE);
            windSpeed = jsonCurrently.getString(WIND_SPEED);
            pressure = jsonCurrently.getString(PRESSURE);

        }catch (JSONException e){
            e.printStackTrace();
            Log.d(TAG, "JSONWeather: " + e.toString());
        }
    }

    public String getPressure() {
        return pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getApparentTemperature() {
        return apparentTemperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public JSONArray getDays() {
        return days;
    }

    public String getIcon() {
        return icon;
    }

    public String getSummary() {
        return summary;
    }
}
