// done (9) Create a class called SunshineSyncUtils
    //  done (10) Create a public static void method called startImmediateSync
    //  done (11) Within that method, start the SunshineSyncIntentService


package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class SunshineSyncUtils{

    public static void startImmediateSync(@NonNull final Context context){
        Intent newIntent = new Intent(context,SunshineSyncIntentService.class);
        context.startService(newIntent);
    }
}