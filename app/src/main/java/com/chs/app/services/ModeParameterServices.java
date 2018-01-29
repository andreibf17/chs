package com.chs.app.services;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.chs.app.entities.Mode;

/**
 * Created by Bogdan Cristian Vlad on 09-Jan-18.
 */

public class ModeParameterServices {

    public static void enableWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static void enableBluetooth(boolean enable) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            if(enable) {
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        }
    }

    public static void setBrightness(Context context, int val) {
        if(val < 0)
            val = 0;
        if(val > 100)
            val = 100;
        val = (int) (val * 2.25);   //mapping between the values that are received 0-100 and the values for the putInt method 0-255
        if(Settings.System.canWrite(context)) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, val);
        }
    }

    public static void setNotificationVolume(Context context, int val) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, (int) (val * 0.15), 0);
    }

    public static void setRingtoneVolume(Context context, int val) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, (int) (val * 0.15), 0);
    }

    public static void setVibrate(Context context, boolean enabled) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(enabled ? 1 : 2);
    }

    public static void setModeParameters(Context context, Mode mode) {
        enableWifi(context, mode.isWifi());
        enableBluetooth(mode.isBluetooth());
        setBrightness(context, mode.getBrightness());
        setNotificationVolume(context, mode.getNotificationVolume());
        setRingtoneVolume(context, mode.getRingVolume());
        setVibrate(context, mode.isVibrateForCalls());
    }
}
