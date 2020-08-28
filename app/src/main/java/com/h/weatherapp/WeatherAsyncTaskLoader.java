package com.h.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class WeatherAsyncTaskLoader extends AsyncTaskLoader<String> {

    private Bundle bundle;
    private String query;

    public WeatherAsyncTaskLoader(@NonNull Context context, Bundle bundle){
        super(context);

        this.bundle = bundle;
    }

    // Метод onStartLoading всегда должен вызывать onForceLoad !!!!!!!!!!!
    @Override
     /*
     Проверка, завершился ли успешно запрос в loadInBackground
      */
    protected void onStartLoading() {
        super.onStartLoading();
        if (query == null){
            //Либо загружаем по новой
            forceLoad(); // вызывает OnForceLoad()
        } else {
            //либо возвращаем данные
            deliverResult(query);
        }
    }


    @Nullable
    @Override
    public String loadInBackground() {

        String coord_A = bundle.getString(MoscowFragment.COORD_A);
        String coord_B = bundle.getString(MoscowFragment.COORD_B);
        query = Weather.getWeather(coord_A,coord_B);
        return query;
    }
}