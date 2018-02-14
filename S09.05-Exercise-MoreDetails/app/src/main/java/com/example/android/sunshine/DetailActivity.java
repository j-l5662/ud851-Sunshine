/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{
//      done (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  done (18) Create a String array containing the names of the desired data columns from our ContentProvider
//  done (19) Create constant int values representing each column name's position above
public static final String[] WEATHER_DETAIL_PROJECTION = {
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_DEGREES,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
};

    private final int[] integerColumns = {0,1,2,3};
//  done (20) Create a constant int to identify our loader used in DetailActivity
    private final static int LOADER_ID = 44;
    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  done (15) Declare a private Uri field called mUri
    private Uri mUri;
//  done (10) Remove the mWeatherDisplay TextView declaration
   // private TextView mWeatherDisplay;

//  done (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mdate;
    private TextView mdescription;
    private TextView mhigh;
    private TextView mlow;
    private TextView mhumidity;
    private TextView mwind;
    private TextView mpressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      done (12) Remove mWeatherDisplay TextView
        //mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);
//      done (13) Find each of the TextViews by ID
        mdate = (TextView) findViewById(R.id.tv_date);
        mdescription = (TextView) findViewById(R.id.tv_description);
        mhigh = (TextView) findViewById(R.id.tv_high);
        mlow = (TextView) findViewById(R.id.tv_low);
        mhumidity = (TextView) findViewById(R.id.tv_humidity);
        mwind = (TextView) findViewById(R.id.tv_wind);
        mpressure = (TextView) findViewById(R.id.tv_pressure);
//      done (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
//                mForecastSummary = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
//                mWeatherDisplay.setText(mForecastSummary);
//            }
//        }
//      done (16) Use getData to get a reference to the URI passed with this Activity's Intent
        mUri = intentThatStartedThisActivity.getData();
//      done (17) Throw a NullPointerException if that URI is null
        if(mUri == null){
            throw new NullPointerException("URI is NULL");
        }
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
//      done (35) Initialize the loader for DetailActivity
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        switch(id) {
            case LOADER_ID:
                return new CursorLoader(this, mUri,WEATHER_DETAIL_PROJECTION,null,null,null);
            default:
                throw new RuntimeException("Error in OnCreateLoader");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorhasdata = false;
        if(data != null && data.moveToFirst()){
            cursorhasdata = true;
        }
        if(!cursorhasdata)
            return;
        long date = data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
        String dateString = SunshineDateUtils.getFriendlyDateString(this,date,false);
        mdate.setText(dateString);
        mdescription.setText(SunshineWeatherUtils.getStringForWeatherCondition(this,data.getInt(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID))));
        mhigh.setText(Long.toString(data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP))));
        mlow.setText(Long.toString(data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP))));
        mhumidity.setText(Long.toString(data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY))));
        mwind.setText(SunshineWeatherUtils.getFormattedWind(this,data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)),data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DEGREES))));
        mpressure.setText(Long.toString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE)) + "hPa");
        mForecastSummary = dateString;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

//  done (22) Override onCreateLoader
//          done (23) If the loader requested is our detail loader, return the appropriate CursorLoader

//  done (24) Override onLoadFinished
//      done (25) Check before doing anything that the Cursor has valid data
//      done (26) Display a readable data string
//      done (27) Display the weather description (using SunshineWeatherUtils)
//      done (28) Display the high temperature
//      done (29) Display the low temperature
//      done (30) Display the humidity
//      done (31) Display the wind speed and direction
//      done (32) Display the pressure
//      done (33) Store a forecast summary in mForecastSummary


//  done (34) Override onLoaderReset, but don't do anything in it yet

}