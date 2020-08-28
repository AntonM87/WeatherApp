package com.h.weatherapp;

public class WeatherItem {

    // класс для recylerView

    private String summary;
    private String icon;
    private String temperature;
    private String apparentTemperature;
    private String pressure;
    private String windSpeed;

    WeatherItem(String summary, String icon, String temperature,
                String apparentTemperature, String pressure,
                String windSpeed){
        this.summary = summary;
        this.icon = icon;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(String apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public String getPressure() {
        return pressure + " мм.рт.ст.";
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindSpeed() {
        return windSpeed + " м/с";
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
