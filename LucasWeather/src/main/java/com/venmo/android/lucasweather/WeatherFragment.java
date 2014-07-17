package com.venmo.android.lucasweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.venmo.android.lucasweather.layouts.devsmart.HorizontalListView;
import com.venmo.android.lucasweather.models.Weather;
import com.venmo.android.lucasweather.models.WeatherLab;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thomasjeon on 7/12/14.
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";

    private OpenWeatherMapApi mOpenWeatherMapApi;
    private TextView mLucasHeaderText;
    private LinearLayout mSelectedDayLinearList;
    private HorizontalListView mSelectedDayListView;
    private ImageView mLucasImage;
    private LinearLayout mFiveDayListView;

    private int[] mLucasImages = {R.drawable.lucas_sunglasses, R.drawable.lucas_umbrella, R.drawable.lucas_christmas, R.drawable.lucas};
    private int mCurrentLucasImage = mLucasImages.length - 1;

    public static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        //args.putSerializable();

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new OpenWeatherMapTask().execute("New_York", "imperial", "5");
        //TODO: Initialize models and junk
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, parent, false);

        mLucasHeaderText = (TextView)v.findViewById(R.id.meme_body_text);
        mSelectedDayLinearList = (LinearLayout)v.findViewById(R.id.selected_day_section);
        mSelectedDayListView = (HorizontalListView)v.findViewById(R.id.selected_day_listview);
        mLucasImage = (ImageView)v.findViewById(R.id.meme_image);
        mLucasImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mCurrentLucasImage++;
                if(mCurrentLucasImage >= mLucasImages.length)
                    mCurrentLucasImage = 0;

                mLucasImage.setImageResource(mLucasImages[mCurrentLucasImage]);
            }
        });

        mFiveDayListView = (LinearLayout)v.findViewById(R.id.five_day_listview);

        return v;
    }

    private void updateLucasHeader(Weather w) {
        String bannerText = "likes it\nHOT!";
        int currentTemp = w.getTemp();

        if (currentTemp >= 80){
            bannerText = "likes it\nHOT!";
        }else if (currentTemp < 50){
            bannerText = "is\nFREEZING!";
        }else if (currentTemp < 80 && currentTemp > 73){
            bannerText = "plays\nSpikeBall";
        }

        if (w.getRain() > 0){
            bannerText = "needs an\nUmbrella";
        }

        if (w.getWeatherIcon().equals(WeatherIcon.SNOW)){
            bannerText = "makes\nSnowAngels";
        }

        if (w.getWeatherIcon().equals(WeatherIcon.NIGHT)){
            bannerText = "buys a\nRound";
        }

        mLucasHeaderText.setText(bannerText);
    }

    private void updateSelectedDayHeader(Weather w) {
        TextView dayTextView = (TextView)mSelectedDayLinearList.findViewById(R.id.selected_day_of_week_textview);
        TextView tempTextView = (TextView)mSelectedDayLinearList.findViewById(R.id.selected_day_current_temp_textview);

        dayTextView.setText(w.getDayOfWeek());
        tempTextView.setText(w.getTemp() + Weather.DEGREE_SIGN);
    }

    private void updateSelectedDayWeather(String selectedDay) {
        ArrayList<Weather> selected24Hours = WeatherLab.get(getActivity()).getDay24HWeathers(selectedDay);
        WeatherListAdapter adapter = new WeatherListAdapter(selected24Hours);
        mSelectedDayListView.setAdapter(adapter);

        updateLucasHeader(selected24Hours.get(0));
        updateSelectedDayHeader(selected24Hours.get(0));
    }

    private void addDailyWeatherToView(Weather w) {

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate daily weather linear layout
        RelativeLayout dayRelativeLayout = (RelativeLayout)inflater.inflate(R.layout.weather_daily_item, mFiveDayListView, false);
        dayRelativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String selectedDay = ((TextView)view.findViewById(R.id.daily_day_of_week_textview)).getText().toString();
                updateSelectedDayWeather(selectedDay);
            }
        });
        mFiveDayListView.addView(dayRelativeLayout);

        TextView dayOfWeek = (TextView)dayRelativeLayout.findViewById(R.id.daily_day_of_week_textview);
        dayOfWeek.setText(w.getDayOfWeek());

        TextView dailyLowTemp = (TextView)dayRelativeLayout.findViewById(R.id.daily_low_temp);
        dailyLowTemp.setText(w.getTempMin() + Weather.DEGREE_SIGN);

        TextView dailyHighTemp = (TextView)dayRelativeLayout.findViewById(R.id.daily_high_temp);
        dailyHighTemp.setText(w.getTempMax() + Weather.DEGREE_SIGN);

        ImageView dailyWeatherImage = (ImageView)dayRelativeLayout.findViewById(R.id.daily_weather_image);
        dailyWeatherImage.setImageResource(w.getWeatherIcon().getDrawableId());
    }

    private class OpenWeatherMapTask extends AsyncTask<String, Void, String[]> {

        //fetch weather data from openweathermap
        protected String[] doInBackground(String... params) {
            mOpenWeatherMapApi = new OpenWeatherMapApi();

            String[] responses = {};
            try{
                responses = mOpenWeatherMapApi.getForecast(params[0], params[1], params[2]);
            }catch (IOException e){
                //catch IOException from api
            }

            return responses;
        }

        protected void onPostExecute(String[] responses) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(WeatherLab.class, new WeatherLabDeserializer(getActivity()));
            gsonBuilder.registerTypeAdapter(Weather.class, new WeatherDeserializer());
            Gson gson = gsonBuilder.create();
            gson.fromJson(responses[0], WeatherLab.class);

            gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Weather.class, new DailyWeatherDeserializer());
            gsonBuilder.registerTypeAdapter(WeatherLab.class, new DailyWeatherLabDeserializer(getActivity()));
            gson = gsonBuilder.create();
            gson.fromJson(responses[1], WeatherLab.class);

            ArrayList<Weather> next24Hours = WeatherLab.get(getActivity()).getNext24HWeathers();
            WeatherListAdapter adapter = new WeatherListAdapter(next24Hours);
            mSelectedDayListView.setAdapter(adapter);

            ArrayList<Weather> next5Days = WeatherLab.get(getActivity()).getDailyWeathers();
            for ( Weather w : next5Days ) {
                addDailyWeatherToView(w);
            }

            updateSelectedDayHeader(next24Hours.get(0));
            updateLucasHeader(next24Hours.get(0));

        }
    }

    private class WeatherListAdapter extends ArrayAdapter<Weather> {

        public WeatherListAdapter(ArrayList<Weather> weather) {
            super(getActivity(), 0, weather);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.weather_hourly_listitem, null);
            }

            //Configure the view for this Weather Time
            Weather w = getItem(position);

            TextView timeTextView = (TextView) convertView.findViewById(R.id.time);
            timeTextView.setText(w.getHour() + w.getAMPM());

            ImageView weatherImageView = (ImageView) convertView.findViewById(R.id.weather_image);
            weatherImageView.setImageResource(w.getWeatherIcon().getDrawableId());

            TextView tempTextView = (TextView) convertView.findViewById(R.id.temp);
            tempTextView.setText(w.getTemp() + Weather.DEGREE_SIGN);

            return convertView;
        }
    }

}
