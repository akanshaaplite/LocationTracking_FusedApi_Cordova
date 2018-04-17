package com.demo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import static org.apache.cordova.CordovaActivity.TAG;

/**
 * Created by akanshajain on 11/10/17.
 */

public class LocationResult extends CordovaPlugin {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private RefreshLatLong refreshLatLong;
    Intent mServiceIntent;
    long startservice_time_interval;
    SharedPreferences sharedPreferences;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("locationCheck")){
            locationCheck();
        }
         else if (action.equals("mainMethodForAll")) {
            long data = args.getLong(0);
            this.mainMethodForAll(data);
            return true;
        }
         else if(action.equals("stop"))
        {
          stopRefreshBackgroundLatLong();
        }
        return false;
    }
    public void mainMethodForAll(final Long data){
        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(cordova.getActivity())) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(cordova.getActivity());
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        cordova.getActivity().startActivity(myIntent);
                        sharedPreferences = cordova.getActivity().getSharedPreferences("timeinterval", 0);
                        try {
                            startservice_time_interval = data;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("startservice_time_interval",startservice_time_interval);
                            editor.commit();

                        }catch (NumberFormatException ex)
                        {
                            ex.printStackTrace();
                        }
                        callBackgroundServices(true);

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        callBackgroundServices(false);
                    }
                });
                callBackgroundServices(false);
                dialog.show();
            }
            else {
                sharedPreferences = cordova.getActivity().getSharedPreferences("timeinterval", 0);
                try {
                    startservice_time_interval = data;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("startservice_time_interval",startservice_time_interval);
                    editor.apply();

                }catch (NumberFormatException ex)
                {
                    ex.printStackTrace();
                }
                callBackgroundServices(true);

            }
        }

    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(cordova.getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, cordova.getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }
    public void callBackgroundServices(boolean isActive) {
        if (isActive) {
            if (isConnected(cordova.getActivity())) {
                new Save_LatLong(cordova.getActivity());
                refreshLatLong = new RefreshLatLong();
                mServiceIntent = new Intent(cordova.getActivity(), refreshLatLong.getClass());
                if (!isMyServiceRunning(refreshLatLong.getClass())) {
                    cordova.getActivity().startService(mServiceIntent);
                    Toast.makeText(cordova.getActivity(),"Service Starts",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(cordova.getActivity(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    public void stopRefreshBackgroundLatLong(){
            cordova.getActivity().stopService(new Intent(cordova.getActivity(), RefreshLatLong.class));
            Toast.makeText(cordova.getActivity(), "Service stops!", Toast.LENGTH_LONG).show();
    }
    public void locationCheck(){
        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(cordova.getActivity())) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(cordova.getActivity());
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        cordova.getActivity().startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
                dialog.show();
            }
        }
    }

}
