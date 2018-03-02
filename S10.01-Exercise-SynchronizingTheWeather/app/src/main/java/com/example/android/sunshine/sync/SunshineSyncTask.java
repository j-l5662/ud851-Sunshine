//  done (1) Create a class called SunshineSyncTask
//  done (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      done (3) Within syncWeather, fetch new weather data
//      done (4) If we have valid results, delete the old data and insert the new
package com.example.android.sunshine.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class SunshineSyncTask{

    synchronized public static void syncWeather(Context context){
        try{
            URL weatherLocation = NetworkUtils.getUrl(context);
            String response = NetworkUtils.getResponseFromHttpUrl(weatherLocation);

            ContentValues[] weatherData = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context,response);

            if(weatherData.length != 0 && weatherData != null){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,null,null);

                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,weatherData);


            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}