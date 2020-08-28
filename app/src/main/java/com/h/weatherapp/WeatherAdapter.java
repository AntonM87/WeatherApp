package com.h.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = "log";
    private LayoutInflater inflater;
    private List<WeatherItem> daysWeather;

    WeatherAdapter(Context context, List<WeatherItem> daysWeather){
        this.daysWeather = daysWeather;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.weather_item_recycler_view,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // к конкретным элементам назначить значения тут
        WeatherItem day = daysWeather.get(position);

        viewHolder.summary.setText(day.getSummary());
        viewHolder.icon.setImageResource(Weather.setWeatherImage(day.getIcon()));
        viewHolder.temperature.setText(Weather.getCelsius(day.getTemperature()));
        viewHolder.apparentTemperature.setText(Weather.getCelsius(day.getApparentTemperature()));
        viewHolder.pressure.setText(day.getPressure());
        viewHolder.windSpeed.setText(day.getWindSpeed());

    }

    // прогноз погоды будет на 7 дней
    @Override
    public int getItemCount() {
        return daysWeather.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView summary;
        final ImageView icon;
        final TextView  temperature;
        final TextView  apparentTemperature;
        final TextView  pressure;
        final TextView  windSpeed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            summary = (TextView)itemView.findViewById(R.id.summaryRecycleViewItem);
            icon = (ImageView) itemView.findViewById(R.id.iconRecycleViewItem);
            temperature = (TextView)itemView.findViewById(R.id.temperatureRecycleViewItem);
            apparentTemperature = (TextView)itemView.findViewById(R.id.apparentTemperatureRecycleViewItem);
            pressure = (TextView)itemView.findViewById(R.id.pressureRecycleViewItem);
            windSpeed = (TextView)itemView.findViewById(R.id.windSpeedRecycleViewItem);
        }
    }
}
