package com.venmo.android.lucasweather;

import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Created by thomasjeon on 7/11/14.
 */
public class OpenWeatherMapApi {

    private static final String HOURLY_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String DAILY_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast/daily";

    private OkHttpClient mOkHttpClient;

    public OpenWeatherMapApi() {
        mOkHttpClient = new OkHttpClient();
    }

    public String[] getForecast(String location, String units, String days) throws IOException {

        Uri hourlyRequestUri = Uri.parse(HOURLY_ENDPOINT).buildUpon()
                .appendQueryParameter("q", location)
                .appendQueryParameter("units", units).build();

        Uri dailyRequestUri = Uri.parse(DAILY_ENDPOINT).buildUpon()
                .appendQueryParameter("q", location)
                .appendQueryParameter("units", units)
                .appendQueryParameter("cnt", days).build();

        String[] responses = new String[2];
        responses[0] = getApiResponse(hourlyRequestUri);
        responses[1] = getApiResponse(dailyRequestUri);

        return responses;
    }

    private String getApiResponse(Uri requestUri) throws IOException{
        Request request = new Request.Builder()
                .url(requestUri.toString())
                .build();
        String response = mOkHttpClient.newCall(request)
                .execute()
                .body()
                .string();

        return response;
    }
}
