package com.chs.app.entities;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.chs.app.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bogdan Cristian Vlad on 08-Dec-17.
 *
 * Class that represents a location preference.
 */

public class Location implements Parcelable{

    private LatLng latlng;
    private String name;
    private GoogleMap map;
    private Polygon polygon;
    private List<LatLng> polygonPoints;
    private int image;
    private boolean receiveNotification;
    private Mode mode;

    private int index;

    public Location() {}

    public Location(String name, LatLng latlng, int image, boolean receiveNotification, List<LatLng> polygonPoints, Mode mode) {
        this.latlng = latlng;
        this.name = name;
        this.image = image;
        this.receiveNotification = receiveNotification;
        this.polygonPoints = polygonPoints;
        this.mode = mode;
    }

    public Polygon getPolygon() { return this.polygon; }

    public List<LatLng> getPolygonPoints() { return polygonPoints; }

    public void setPolygonPoints(Polygon polygon) { polygonPoints = polygon.getPoints(); }

    public void setPolygon() { polygon = map.addPolygon(new PolygonOptions().addAll(polygonPoints).strokeColor(Constants.BLUE)); }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public LatLng getLatlng() { return latlng; }

    public void setLatlng(LatLng latlng) { this.latlng = latlng; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public void widenPolygon(int units) {
        if (polygon != null) {
            List<LatLng> points = polygon.getPoints();
            polygon.remove();
            polygon = map.addPolygon(new PolygonOptions().addAll(Arrays.asList(
                    new LatLng(points.get(0).latitude, latlng.longitude + units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(points.get(1).latitude, latlng.longitude + units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(points.get(2).latitude, latlng.longitude - units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(points.get(3).latitude, latlng.longitude - units * Constants.POLYGON_UNIT *1.5)
            )).strokeColor(Constants.BLUE));
        } else {
            polygon = map.addPolygon(new PolygonOptions().addAll(Arrays.asList(
                    new LatLng(latlng.latitude, latlng.longitude + units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(latlng.latitude, latlng.longitude + units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(latlng.latitude, latlng.longitude - units * Constants.POLYGON_UNIT *1.5),
                    new LatLng(latlng.latitude, latlng.longitude - units * Constants.POLYGON_UNIT *1.5)
            )).strokeColor(Constants.BLUE));
        }
    }

    public void heightenPolygon(int units) {
        if(polygon != null) {
            polygon.remove();
            List<LatLng> points = polygon.getPoints();
            polygon = map.addPolygon(new PolygonOptions().addAll(Arrays.asList(
                    new LatLng(latlng.latitude + units * Constants.POLYGON_UNIT, points.get(0).longitude),
                    new LatLng(latlng.latitude - units * Constants.POLYGON_UNIT, points.get(1).longitude),
                    new LatLng(latlng.latitude - units * Constants.POLYGON_UNIT, points.get(2).longitude),
                    new LatLng(latlng.latitude + units * Constants.POLYGON_UNIT, points.get(3).longitude)
            )).strokeColor(Constants.BLUE));
        } else {
            polygon = map.addPolygon(new PolygonOptions().addAll(Arrays.asList(
                    new LatLng(latlng.latitude + units * Constants.POLYGON_UNIT, latlng.longitude),
                    new LatLng(latlng.latitude - units * Constants.POLYGON_UNIT, latlng.longitude),
                    new LatLng(latlng.latitude - units * Constants.POLYGON_UNIT, latlng.longitude),
                    new LatLng(latlng.latitude + units * Constants.POLYGON_UNIT, latlng.longitude)
            )).strokeColor(Constants.BLUE));
        }
    }

    /**
     * Method that creates a marker for a saved location.
     * @param context usually the current activity from where it is called
     */
    public Marker setMarker(Context context) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
        iconGenerator.setTextAppearance(Color.WHITE);
        return map.addMarker(new MarkerOptions()
                                .position(latlng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public void removePolygon() { if(polygon != null) polygon.remove(); }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

    public GoogleMap getMap() { return map; }

    public void setMap(GoogleMap map) { this.map = map; }

    public Mode getMode() { return mode; }

    public void setMode(Mode mode) { this.mode = mode; }

    public void setReceiveNotification(boolean receiveNotification) { this.receiveNotification = receiveNotification; }

    public boolean getReceiveNotification() { return receiveNotification; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(receiveNotification ? 1 : 0));
        dest.writeList(polygonPoints);
        dest.writeString(name);
        dest.writeDouble(latlng.latitude);
        dest.writeDouble(latlng.longitude);
        dest.writeInt(image);
        if(mode != null) {
            dest.writeString(mode.getName());
            dest.writeByte((byte) (mode.isWifi() ? 1 : 0));
            dest.writeByte((byte) (mode.isMobileData() ? 0 : 1));
            dest.writeByte((byte) (mode.isBluetooth() ? 0 : 1));
            dest.writeByte((byte) (mode.isAirplaneMode() ? 0 : 1));
            dest.writeInt(mode.getIndex());
            //TODO: add here other fields of mode
        }
    }

    /**
     * Method needed for sending the Location object through an Intent from LocationActivity to LocationDetailsActivity (
     */
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            boolean receiveNotif = in.readByte() == 1;
            ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
            in.readList(coordinates, LatLng.class.getClassLoader());
            return new Location(
                    in.readString(),
                    new LatLng(in.readDouble(), in.readDouble()),
                    in.readInt(),
                    receiveNotif,
                    coordinates,
                    new Mode(
                            in.readString(),
                            in.readByte() == 1,
                            in.readByte() == 1,
                            in.readByte() == 1,
                            in.readByte() == 1,
                            in.readInt()
                    )
            );
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
