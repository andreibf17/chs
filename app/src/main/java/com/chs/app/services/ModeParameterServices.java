package com.chs.app.services;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.chs.app.entities.Mode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Bogdan Cristian Vlad on 09-Jan-18.
 */

public class ModeParameterServices {

    public static void turnLocationOn(Context context) {
        /*GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(context, LOCATION_REQ_PERMISSION);
                        } catch (IntentSender.SendIntentException e) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });*/
    }

    public static void enableWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    /*not working*/
    public static void enableMobileData(Context context, boolean enable) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass;
        final Field iConnectivityManagerField;
        final Object iConnectivityManager;
        final Class iConnectivityManagerClass;
        final Method setMobileDataEnabledMethod;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
            iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            iConnectivityManager = iConnectivityManagerField.get(conman);
            iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enable);
        } catch(Exception e) {}
    }

    public static void enableBluetooth(boolean enable) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            if(enable) {
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        }
    }

    public static void enableAirplaneMode(Context context, boolean enable) {
        Settings.Global.putString(context.getContentResolver(), "airplane_mode_on", enable ? "1" : "0");
    }

    public static void setBrightness(Context context, int val) {
        if(val < 0)
            val = 0;
        if(val > 255)
            val = 255;
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, val);
    }

    public static void setModeParameters(Context context, Mode mode) {
        enableWifi(context, mode.isWifi());
        enableBluetooth(mode.isBluetooth());
        enableMobileData(context, mode.isMobileData());
        //enableAirplaneMode(context, mode.isAirplaneMode());
    }
}
