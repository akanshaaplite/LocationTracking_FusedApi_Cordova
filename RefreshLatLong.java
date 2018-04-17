package com.demo;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RefreshLatLong extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public GoogleApiClient mGoogleApiClient;
    public static String lat, log, altitude,accuracy,speed;
    Timer timer;
    private TimerTask timerTask;
    private LocationManager mLocationManager;
    Handler h;
    Context context;
    GeomagneticField geomagneticField;
    SharedPreferences sharedPreferences;
    String recieveDataToForLocation;
    Handler mHandler;
    public RefreshLatLong() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //600000
        //schedule the timer, to wake up every 3 minute
        //180000
        sharedPreferences = this.getSharedPreferences("timeinterval", 0);
        long timeInterval = sharedPreferences.getLong("startservice_time_interval",0);
        timer.schedule(timerTask,0,timeInterval); //

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("destroy");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                        getAllDataFromLocation();
            }
        };
    }
    public void getAllDataFromLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        try {
            if (lat != null && log != null){
                try{
                    WebserviceHandler handlerService = new WebserviceHandler();
                    JSONObject jObjForRegis = new JSONObject();
                    // Log.e("json_save_current",SplashScreen.preferences.getString("usr_token","")+" "+SplashScreen.preferences.getString("usr_id","")+" "+lat+" "+log+" "+select_Location_from_user+" "+date_time);
                    if (sharedPreferences == null)
                        sharedPreferences = this.getSharedPreferences("userLoginDetail", 0);
                    try {
                        jObjForRegis.put("userid",sharedPreferences.getString("usr_id",""));
                        jObjForRegis.put("lat", lat);
                        jObjForRegis.put("long", log);
                        System.out.println(jObjForRegis.toString()+"*****");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(lat!=null && log!=null) {
                        recieveDataToForLocation = handlerService.Save_current_location(jObjForRegis);
                        JSONObject registerJsonObject = new JSONObject(recieveDataToForLocation);
                        boolean success=registerJsonObject.getBoolean("success");
                        String msg=registerJsonObject.getString("message");
                        Log.e("Save current location=",recieveDataToForLocation);
                        System.out.println(success+"success");
                        if(success==true){

                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e("Connect","done");
        if (mLastLocation != null) {
            lat=mLastLocation.getLatitude()+"";
            log=mLastLocation.getLongitude()+"";
            accuracy=mLastLocation.getAccuracy()+"";
            if(mLastLocation.hasAltitude()==true && mLastLocation.getAltitude()!=0.0)
                altitude = mLastLocation.getAltitude() + "";
            if(mLastLocation.hasSpeed()==true && mLastLocation.getSpeed()!=0.0)
                speed=mLastLocation.getSpeed()+"";
            long gpsTime = mLastLocation.getTime();
            if(mLastLocation.getAltitude()!=0.0) {
                geomagneticField = new GeomagneticField(Float.parseFloat(lat), Float.parseFloat(log), Float.parseFloat(altitude), gpsTime);
                float heading = geomagneticField.getDeclination();
                heading = mLastLocation.getBearing() - (mLastLocation.getBearing() + heading);
                System.out.println(lat + "%%%%%%" + log + "%%%%%" + accuracy + "%%%%%%" + altitude + "%%%%%" + speed + "%%%%%%" + heading);
                Toast.makeText(RefreshLatLong.this,"Latitude:-"+" "+lat+" "+"Longitude:-"+" "+log,Toast.LENGTH_LONG).show();
                //You'll then want to convert from degrees east of true north (-180 to +180) into normal degrees (0 to 360):
                Math.round(-heading / 360 + 180);
            }
        }
        else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            lat=mLastLocation.getLatitude()+"";
            log=mLastLocation.getLongitude()+"";
            accuracy=mLastLocation.getAccuracy()+"";
            if(mLastLocation.hasAltitude()==true && mLastLocation.getAltitude()!=0.0)
                altitude = mLastLocation.getAltitude() + "";
            if(mLastLocation.hasSpeed()==true && mLastLocation.getSpeed()!=0.0)
                speed=mLastLocation.getSpeed()+"";
            long gpsTime = mLastLocation.getTime();
            if(mLastLocation.getAltitude()!=0.0) {
                geomagneticField = new GeomagneticField(Float.parseFloat(lat), Float.parseFloat(log), Float.parseFloat(altitude), gpsTime);
                float heading = geomagneticField.getDeclination();
                heading = mLastLocation.getBearing() - (mLastLocation.getBearing() + heading);
                System.out.println(lat + "%%%%%%" + log + "%%%%%" + accuracy + "%%%%%%" + altitude + "%%%%%" + speed + "%%%%%%" + heading);
                //You'll then want to convert from degrees east of true north (-180 to +180) into normal degrees (0 to 360):
                Toast.makeText(RefreshLatLong.this,"Latitude:-"+" "+lat+" "+"Longitude:-"+" "+log,Toast.LENGTH_LONG).show();
                Math.round(-heading / 360 + 180);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Connect","done");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
