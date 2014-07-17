package com.venmo.android.lucasweather.models;

import com.venmo.android.lucasweather.WeatherIcon;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by thomasjeon on 7/14/14.
 */
public class Weather {

    public static final String DEGREE_SIGN = "˚";

    private UUID mId;
    private int mDTID;
    private Calendar mDate;
    private Locale mLocale;
    private int mTemp;
    private int mTempMin;
    private int mTempMax;
    private int mHumidty;
    private String mWeatherImage;
    private String mWeatherIconId;
    private int mRain;

    public Weather() {
        mId = UUID.randomUUID();
        mDate = new GregorianCalendar();
        mLocale = Locale.US;
    }

    public int getHour() {
        return mDate.get(Calendar.HOUR);
    }

    public String getAMPM() {
        if (mDate.get(Calendar.AM_PM) == Calendar.AM)
            return "AM";
        return "PM";
    }

    public String getDayOfWeek() {
        return mDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, mLocale);
    }

    public WeatherIcon getWeatherIcon() {

        if(mWeatherIconId.equals("01d")){
            return WeatherIcon.SUNNY;
        }else if(mWeatherIconId.equals("02d")){
            return WeatherIcon.PARTLY_SUNNY;
        }else if(mWeatherIconId.startsWith("09")){
            return WeatherIcon.SHOWER;
        }else if(mWeatherIconId.startsWith("10")){
            return WeatherIcon.DOWNPOUR;
        }else if(mWeatherIconId.startsWith("11")){
            return WeatherIcon.STORMY;
        }else if(mWeatherIconId.startsWith("13")){
            return WeatherIcon.SNOW;
        }else if(mWeatherIconId.startsWith("50")){
            return WeatherIcon.WINDY;
        }else if(mWeatherIconId.equals("01n") || mWeatherIconId.equals("02n")){
            return WeatherIcon.NIGHT;
        }

        return WeatherIcon.CLOUDY;
    }

    public UUID getId() {
        return mId;
    }

    public int getDTID() {
        return mDTID;
    }

    public void setDTID(int DTID) {
        mDTID = DTID;
        mDate = new GregorianCalendar();

        long epochMillis = Long.parseLong(String.valueOf(DTID) + "000");
        mDate.setTimeInMillis(epochMillis);
        mDate.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(Calendar date) {
        mDate = date;
    }

    public int getTemp() {
        return mTemp;
    }

    public void setTemp(int temp) {
        mTemp = temp;
    }

    public int getTempMin() {
        return mTempMin;
    }

    public void setTempMin(int tempMin) {
        mTempMin = tempMin;
    }

    public int getTempMax() {
        return mTempMax;
    }

    public void setTempMax(int tempMax) {
        mTempMax = tempMax;
    }

    public int getHumidty() {
        return mHumidty;
    }

    public void setHumidty(int humidty) {
        mHumidty = humidty;
    }

    public void setWeatherIconId(String weatherIconId) {
        mWeatherIconId = weatherIconId;
    }

    public String getWeatherImage() {
        return mWeatherImage;
    }

    public void setWeatherImage(String weatherImage) {
        mWeatherImage = weatherImage;
    }

    public int getRain() {
        return mRain;
    }

    public void setRain(int rain) {
        mRain = rain;
    }
}
