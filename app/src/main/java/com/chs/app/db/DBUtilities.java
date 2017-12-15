package com.chs.app.db;

import android.content.SharedPreferences;

import com.chs.app.R;
import com.chs.app.entities.Location;
import com.chs.app.entities.Mode;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan Cristian Vlad on 13-Dec-17.
 */

public class DBUtilities {

    private static SharedPreferences locationDb;
    private static SharedPreferences modeDb;
    private static int locationLastIndex;
    private static int modeLastIndex;

    public static void initialize(SharedPreferences locationDbParam, SharedPreferences modeDbParam) {
        locationDb = locationDbParam;
        modeDb = modeDbParam;
        locationLastIndex = locationDb.getInt("LocationLastIndex", 0);
        modeLastIndex = modeDb.getInt("ModeLastIndex", 0);
    }

    private static String pointsToString(List<LatLng> points) {
        String tmp = "";
        for(LatLng index : points) {
            tmp += index.latitude + "," + index.longitude + ",";
        }
        return tmp.substring(0, tmp.length()-1);
    }

    private static List<LatLng> stringToPoints(String pointsAsString) {
        List<LatLng> stringsAsPoints = new ArrayList<LatLng>();
        if(pointsAsString != null) {
            String[] coordinates = pointsAsString.split(",");
            for (int i = 0; i < coordinates.length; i += 2) {
                stringsAsPoints.add(new LatLng(
                        Double.parseDouble(coordinates[i]),
                        Double.parseDouble(coordinates[i + 1])
                ));
            }
        }
        return stringsAsPoints;
    }

    public static void saveLocation(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        if(!location.getName().equals("Title")) {
            location.setIndex(locationLastIndex);
            editor.putString("Location" + locationLastIndex + "Name", location.getName()).apply();
            editor.putFloat("Location" + locationLastIndex + "Latitude", (float) location.getLatlng().latitude).apply();
            editor.putFloat("Location" + locationLastIndex + "Longitude", (float) location.getLatlng().longitude).apply();
            editor.putInt("Location" + locationLastIndex + "Image", location.getImage()).apply();
            editor.putBoolean("Location" + locationLastIndex + "ReceiveNotification", location.getReceiveNotification()).apply();
            editor.putString("Location" + locationLastIndex + "Polygon", pointsToString(location.getPolygon().getPoints())).apply();
            editor.putInt("Location" + locationLastIndex + "ModeIndex", location.getMode().getIndex()).apply();
            locationLastIndex++;
            editor.putInt("LocationLastIndex", locationLastIndex);
        }
    }

    public static void updateLocation(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        editor.putString("Location" + location.getIndex() + "Name", location.getName()).apply();
        editor.putFloat("Location" + location.getIndex() + "Latitude", (float) location.getLatlng().latitude).apply();
        editor.putFloat("Location" + location.getIndex() + "Longitude", (float) location.getLatlng().longitude).apply();
        editor.putInt("Location" + location.getIndex() + "Image", location.getImage()).apply();
        editor.putBoolean("Location" + location.getIndex() + "ReceiveNotification", location.getReceiveNotification()).apply();
        editor.putString("Location" + location.getIndex() + "Polygon", pointsToString(location.getPolygon().getPoints())).apply();
        editor.putInt("Location" + location.getIndex() + "ModeIndex", location.getMode().getIndex()).apply();
    }

    public static void deleteLocation(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        editor.remove("Location" + location.getIndex() + "Name").apply();
        editor.remove("Location" + location.getIndex() + "Latitude").apply();
        editor.remove("Location" + location.getIndex() + "Longitude").apply();
        editor.remove("Location" + location.getIndex() + "Image").apply();
        editor.remove("Location" + location.getIndex() + "ReceiveNotification").apply();
        editor.remove("Location" + location.getIndex() + "Polygon").apply();
        editor.remove("Location" + location.getIndex() + "ModeIndex").apply();
    }

    public static Location getLocation(int index) {
        return new Location(
                locationDb.getString("Location" + index + "Name", null),
                new LatLng(
                    locationDb.getFloat("Location" + index + "Latitude", 0),
                    locationDb.getFloat("Location" + index + "Longitude", 0)
                ),
                locationDb.getInt("Location" + index + "Image", 0),
                locationDb.getBoolean("Location" + index + "ReceiveNotification", false),
                stringToPoints(locationDb.getString("Location" + index + "Polygon", null)),
                getMode(locationDb.getInt("Location" + locationLastIndex + "ModeIndex", 0))
        );
    }

    public static List<Location> getAllLocations() {
        Location tmp;
        List<Location> locations = new ArrayList<Location>();
        for(int i = 0; i < locationLastIndex; i++) {
            tmp = getLocation(i);
            if(tmp.getName() != null)
                locations.add(tmp);
        }
        return locations;
    }

    public static void saveHome(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        if(!location.getName().equals("Title")) {
            editor.putString("HomeName", location.getName()).apply();
            editor.putFloat("HomeLatitude", (float) location.getLatlng().latitude).apply();
            editor.putFloat("HomeLongitude", (float) location.getLatlng().longitude).apply();
            editor.putInt("HomeImage", location.getImage()).apply();
            editor.putBoolean("HomeReceiveNotification", location.getReceiveNotification()).apply();
            editor.putString("HomePolygon", pointsToString(location.getPolygon().getPoints())).apply();
            editor.putInt("HomeModeIndex", location.getMode().getIndex()).apply();
        }
    }

    public static Location getHome() {
        return new Location(
                locationDb.getString("HomeName", "Set Home"),
                new LatLng(
                        locationDb.getFloat("HomeLatitude", 0),
                        locationDb.getFloat("HomeLongitude", 0)
                ),
                locationDb.getInt("HomeImage", R.drawable.ic_home_black_24dp),
                locationDb.getBoolean("HomeReceiveNotification", false),
                stringToPoints(locationDb.getString("HomePolygon", null)),
                getMode(locationDb.getInt("HomeModeIndex", 0))
        );
    }

    public static void updateHome(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        editor.putString("HomeName", location.getName()).apply();
        editor.putFloat("HomeLatitude", (float) location.getLatlng().latitude).apply();
        editor.putFloat("HomeLongitude", (float) location.getLatlng().longitude).apply();
        editor.putInt("HomeImage", location.getImage()).apply();
        editor.putBoolean("HomeReceiveNotification", location.getReceiveNotification()).apply();
        editor.putString("HomePolygon", pointsToString(location.getPolygon().getPoints())).apply();
        editor.putInt("HomeModeIndex", location.getMode().getIndex()).apply();
    }

    public static void saveSchool(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        if(!location.getName().equals("Title")) {
            editor.putString("SchoolName", location.getName()).apply();
            editor.putFloat("SchoolLatitude", (float) location.getLatlng().latitude).apply();
            editor.putFloat("SchoolLongitude", (float) location.getLatlng().longitude).apply();
            editor.putInt("SchoolImage", location.getImage()).apply();
            editor.putBoolean("SchoolReceiveNotification", location.getReceiveNotification()).apply();
            editor.putString("SchoolPolygon", pointsToString(location.getPolygon().getPoints())).apply();
            editor.putInt("SchoolModeIndex", location.getMode().getIndex()).apply();
        }
    }

    public static Location getSchool() {
        return new Location(
                locationDb.getString("SchoolName", "Set School"),
                new LatLng(
                        locationDb.getFloat("SchoolLatitude", 0),
                        locationDb.getFloat("SchoolLongitude", 0)
                ),
                locationDb.getInt("SchoolImage", R.drawable.ic_school_black_24dp),
                locationDb.getBoolean("SchoolReceiveNotification", false),
                stringToPoints(locationDb.getString("SchoolPolygon", null)),
                getMode(locationDb.getInt("SchoolModeIndex", 0))
        );
    }

    public static void updateSchool(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        editor.putString("SchoolName", location.getName()).apply();
        editor.putFloat("SchoolLatitude", (float) location.getLatlng().latitude).apply();
        editor.putFloat("SchoolLongitude", (float) location.getLatlng().longitude).apply();
        editor.putInt("SchoolImage", location.getImage()).apply();
        editor.putBoolean("SchoolReceiveNotification", location.getReceiveNotification()).apply();
        editor.putString("SchoolPolygon", pointsToString(location.getPolygon().getPoints())).apply();
        editor.putInt("SchoolModeIndex", location.getMode().getIndex()).apply();
    }

    public static void saveWork(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        if(!location.getName().equals("Title")) {
            editor.putString("WorkName", location.getName()).apply();
            editor.putFloat("WorkLatitude", (float) location.getLatlng().latitude).apply();
            editor.putFloat("WorkLongitude", (float) location.getLatlng().longitude).apply();
            editor.putInt("WorkImage", location.getImage()).apply();
            editor.putBoolean("WorkReceiveNotification", location.getReceiveNotification()).apply();
            editor.putString("WorkPolygon", pointsToString(location.getPolygon().getPoints())).apply();
            editor.putInt("WorkModeIndex", location.getMode().getIndex()).apply();
        }
    }

    public static Location getWork() {
        return new Location(
                locationDb.getString("WorkName", "Set Work"),
                new LatLng(
                        locationDb.getFloat("WorkLatitude", 0),
                        locationDb.getFloat("WorkLongitude", 0)
                ),
                locationDb.getInt("WorkImage", R.drawable.ic_work_black_24dp),
                locationDb.getBoolean("WorkReceiveNotification", false),
                stringToPoints(locationDb.getString("WorkPolygon", null)),
                getMode(locationDb.getInt("WorkModeIndex", 0))
        );
    }

    public static void updateWork(Location location) {
        SharedPreferences.Editor editor = locationDb.edit();
        editor.putString("WorkName", location.getName()).apply();
        editor.putFloat("WorkLatitude", (float) location.getLatlng().latitude).apply();
        editor.putFloat("WorkLongitude", (float) location.getLatlng().longitude).apply();
        editor.putInt("WorkImage", location.getImage()).apply();
        editor.putBoolean("WorkReceiveNotification", location.getReceiveNotification()).apply();
        editor.putString("WorkPolygon", pointsToString(location.getPolygon().getPoints())).apply();
        editor.putInt("WorkModeIndex", location.getMode().getIndex()).apply();
    }

    public static void saveMode(Mode mode) {
        SharedPreferences.Editor editor = modeDb.edit();
        editor.putString("Mode" + modeLastIndex + "Name", mode.getName());
        //TODO: Add here other fields for mode
        modeLastIndex++;
        editor.putInt("ModeLastIndex", modeLastIndex);
    }

    public static Mode getMode(int index) {
        return new Mode(
                modeDb.getString("Mode" + index + "Name", null)
                //TODO: Add here other fields for mode
        );
    }
}
