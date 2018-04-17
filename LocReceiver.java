package com.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by akanshajain on 10/02/17.
 */

public class LocReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            Log.i(RefreshLatLong.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}