package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings_fragment2);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_screen);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++){
            Preference pref = preferenceScreen.getPreference(i);
            if(!(pref instanceof CheckBoxPreference)){
                String val = sharedPreferences.getString(pref.getKey(),"");
                setPreferenceSummary(pref,val);
            }
        }
    }

    // done (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    public void setPreferenceSummary(Preference pref, Object value){
        String stringValue = value.toString();
        //String key = pref.getKey();

        if(pref instanceof ListPreference){
            ListPreference listPreference = (ListPreference) pref;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if(prefIndex >=0 ){
                pref.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else{
            pref.setSummary(stringValue);
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences preference, String s) {
        Preference pref = findPreference(s);
        if( null != pref){
            if(!(pref instanceof CheckBoxPreference)){
                String val = preference.getString(pref.getKey(),"");
                setPreferenceSummary(pref,val);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
