package com.chs.app.entities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by Bogdan Cristian Vlad on 08-Dec-17.
 *
 * Class that represents a location preference.
 */

public class Location implements Parcelable{

    private LatLng latlng;
    private String name;
    private GoogleMap map;
    private Circle circle;
    private int image;
    private boolean receiveNotification;
    private Mode mode;

    public Location(LatLng latlng, String name, int image, Mode mode) {
        this.latlng = latlng;
        this.name = name;
        this.image = image;
        this.mode = mode;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that creates a circle on the map, around the marker (saved location).
     * The method actually adds the circle the first time, and for the next times it deletes the previous one and creates another with the current parameters.
     * @param radius the radius of the circle
     */
    public void setCircle(int radius) {
        if(circle != null)
            circle.remove();
        circle = map.addCircle(new CircleOptions()
                            .center(latlng)
                            .radius(radius * 3)
                            .strokeColor(Color.YELLOW));
    }

    /**
     * Method that creates a marker for a saved location.
     * @param activity usually the current activity from where it is called
     */
    public void setMarker(Activity activity) {
        IconGenerator iconGenerator = new IconGenerator(activity);
        iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
        iconGenerator.setTextAppearance(Color.WHITE);
        map.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public int getImage() {
        return image;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Mode getMode() { return mode; }

    public void setReceiveNotification(boolean receiveNotification) { this.receiveNotification = receiveNotification; }

    public boolean getReceiveNotification() { return receiveNotification; }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latlng.latitude);
        dest.writeDouble(latlng.longitude);
        dest.writeString(name);
        dest.writeInt(image);
        dest.writeString(mode.getName());
    }

    /**
     * Method needed for sending the Location object through an Intent from LocationActivity to LocationDetailsActivity (
     */
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            return new Location(new LatLng(in.readDouble(), in.readDouble()), in.readString(), in.readInt(), new Mode(in.readString()));
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
