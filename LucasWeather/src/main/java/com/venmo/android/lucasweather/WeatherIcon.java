package com.venmo.android.lucasweather;

/**
 * Created by thomasjeon on 7/15/14.
 */
public enum WeatherIcon {
    CLOUDY(R.drawable.weather_cloudy), DOWNPOUR(R.drawable.weather_downpour), NIGHT(R.drawable.weather_night), PARTLY_SUNNY(R.drawable.weather_partly_sunny),
    SHOWER(R.drawable.weather_shower), SNOW(R.drawable.weather_snow), STORMY(R.drawable.weather_stormy), SUNNY(R.drawable.weather_sunny),
    WINDY(R.drawable.weather_windy), WINDY_CLOUDY(R.drawable.weather_windy_cloudy);

    private int mDrawableId;

    private WeatherIcon(int drawableId) {
        mDrawableId = drawableId;
    }

    public int getDrawableId(){
        return mDrawableId;
    }
}
