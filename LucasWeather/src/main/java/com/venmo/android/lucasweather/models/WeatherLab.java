package com.venmo.android.lucasweather.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by thomasjeon on 7/12/14.
 */
public class WeatherLab {


    private ArrayList<Weather> mHourlyWeathers;
    private ArrayList<Weather> mDailyWeathers;
    private String city;

    private static WeatherLab sWeatherLab;
    private Context mAppContext;

    private WeatherLab(Context appContext) {
        mAppContext = appContext;
        mHourlyWeathers = new ArrayList<Weather>();
        mDailyWeathers = new ArrayList<Weather>();
    }

    public static WeatherLab get(Context c) {
        if (sWeatherLab == null) {
            sWeatherLab = new WeatherLab(c.getApplicationContext());
        }
        return sWeatherLab;
    }

    public ArrayList<Weather> getNext24HWeathers() {
        ArrayList<Weather> next24Hours = new ArrayList<Weather>();

        for (int i=0; i<8 && i<mHourlyWeathers.size(); i++) {
            next24Hours.add(mHourlyWeathers.get(i));
        }

        return next24Hours;
    }

    public ArrayList<Weather> getDay24HWeathers(String selectedDay) {
        ArrayList<Weather> weather24Hours = new ArrayList<Weather>();
        int count = 0;

        for (Weather w : mHourlyWeathers){
            if(w.getDayOfWeek().equals(selectedDay) || (count > 0 && count < 8)) {
                weather24Hours.add(w);
                count++;
            }
        }

        return weather24Hours;
    }

    public ArrayList<Weather> getHourlyWeathers() {
        return mHourlyWeathers;
    }

    public ArrayList<Weather> getDailyWeathers() {
        return mDailyWeathers;
    }

    public Weather getHourlyWeather(int dt) {
        for (Weather w : mHourlyWeathers) {
            if (w.getDTID() == dt)
                return w;
        }
        return null;
    }

    public Weather getDailyWeather(int dt) {
        for (Weather w : mDailyWeathers) {
            if (w.getDTID() == dt)
                return w;
        }
        return null;
    }

    public void setHourlyWeathers(ArrayList<Weather> weathers) {
        mHourlyWeathers = weathers;
    }

    public void setDailyWeathers(ArrayList<Weather> dailyWeathers) {
        mDailyWeathers = dailyWeathers;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void addHourlyWeather(Weather weather) {
        mHourlyWeathers.add(weather);
    }

    public void addDailyWeather(Weather weather) {
        mDailyWeathers.add(weather);
    }


}
