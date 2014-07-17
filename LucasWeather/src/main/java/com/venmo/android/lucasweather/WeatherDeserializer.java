package com.venmo.android.lucasweather;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.venmo.android.lucasweather.models.Weather;

import java.lang.reflect.Type;

/**
 * Created by thomasjeon on 7/14/14.
 */
public class WeatherDeserializer implements JsonDeserializer<Weather> {

    @Override
    public Weather deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject mainJsonObject = json.getAsJsonObject();
        final JsonObject tempJsonObject = mainJsonObject.getAsJsonObject("main");
        final JsonObject weatherJsonObject = mainJsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();

        final int dt = mainJsonObject.get("dt").getAsInt();
        final int temp = tempJsonObject.get("temp").getAsInt();
        final int temp_min = tempJsonObject.get("temp_min").getAsInt();
        final int temp_max = tempJsonObject.get("temp_max").getAsInt();
        final int humidty = tempJsonObject.get("humidity").getAsInt();
        final String weatherImage = weatherJsonObject.get("main").getAsString();
        final String weatherIconId = weatherJsonObject.get("icon").getAsString();
        final int rain = mainJsonObject.getAsJsonObject("rain").get("3h").getAsInt();

        Weather weather = new Weather();
        weather.setDTID(dt);
        weather.setTemp(temp);
        weather.setTempMin(temp_min);
        weather.setTempMax(temp_max);
        weather.setHumidty(humidty);
        weather.setWeatherImage(weatherImage);
        weather.setWeatherIconId(weatherIconId);
        weather.setRain(rain);

        return weather;
    }
}
