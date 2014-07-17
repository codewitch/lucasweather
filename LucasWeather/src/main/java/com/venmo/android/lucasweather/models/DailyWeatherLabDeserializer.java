package com.venmo.android.lucasweather.models;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.venmo.android.lucasweather.models.Weather;
import com.venmo.android.lucasweather.models.WeatherLab;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by thomasjeon on 7/14/14.
 */
public class DailyWeatherLabDeserializer implements JsonDeserializer<WeatherLab> {

    private Context mAppContext;

    public DailyWeatherLabDeserializer(Context context) {
        super();
        mAppContext = context;
    }

    @Override
    public WeatherLab deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject mainJsonObject = json.getAsJsonObject();

        final JsonArray weatherJsonArray = mainJsonObject.getAsJsonArray("list");

        Type arrayListWeatherType = new TypeToken<ArrayList<Weather>>() {}.getType();
        ArrayList<Weather> dailyWeathers = context.deserialize(weatherJsonArray, arrayListWeatherType);

        WeatherLab weatherLab = WeatherLab.get(mAppContext);

        weatherLab.setDailyWeathers(dailyWeathers);

        return weatherLab;
    }
}
