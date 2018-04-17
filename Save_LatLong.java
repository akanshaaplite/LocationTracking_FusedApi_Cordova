package com.demo;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class Save_LatLong implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final String TAG = "not connected";
    Context ctx;
    public static String lat, log, altitude,accuracy,speed;
    public GoogleApiClient mGoogleApiClient;
    GeomagneticField geomagneticField;

    public Save_LatLong(Context ctx) {
        this.ctx = ctx;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        mGoogleApiClient.connect();
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
}
