package com.venmo.android.lucasweather;

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
public class WeatherLabDeserializer implements JsonDeserializer<WeatherLab> {

    private Context mAppContext;

    public WeatherLabDeserializer(Context context) {
        super();
        mAppContext = context;
    }

    @Override
    public WeatherLab deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject mainJsonObject = json.getAsJsonObject();

        final JsonObject cityJsonObject = mainJsonObject.getAsJsonObject("city");
        final String city = cityJsonObject.get("name").getAsString();
        final String country = cityJsonObject.get("country").getAsString();

        final JsonArray weatherJsonArray = mainJsonObject.getAsJsonArray("list");

        Type arrayListWeatherType = new TypeToken<ArrayList<Weather>>() {}.getType();
        ArrayList<Weather> hourlyWeathers = context.deserialize(weatherJsonArray, arrayListWeatherType);

        WeatherLab weatherLab = WeatherLab.get(mAppContext);

        weatherLab.setCity(city);
        weatherLab.setHourlyWeathers(hourlyWeathers);

        return weatherLab;
    }
}
