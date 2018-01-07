package com.chs.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chs.app.db.DBUtilities;
import com.chs.app.entities.Location;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Location location = new Location();
    private boolean update;
    private boolean markerSet = false;
    private SeekBar widthSeekBar;
    private SeekBar heightSeekBar;
    private SeekBar rotateSeekBar;
    private CheckBox checkbox;
    private boolean dirty = false;
    private EditText editText;

    private final int LOCATION_REQ_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        Toolbar toolbar = findViewById(R.id.toolbarLocationDetails);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText = findViewById(R.id.locationTitle);
        update = this.getIntent().getExtras().getBoolean("Update");

        checkbox = findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(location != null)
                    location.setReceiveNotification(isChecked);
            }
        });

        if(update) {
            location = this.getIntent().getExtras().getParcelable("Location");
            editText.setText(location.getName());
            Button mode = findViewById(R.id.locationMode);
            mode.setText(location.getMode().getName());
            checkbox.setChecked(location.getReceiveNotification());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (checkLocationPermission())
            mMap.setMyLocationEnabled(true);
        else
            askLocationPermission();

        widthSeekBar = findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(location != null && location.getLatlng() != null && location.getMap() != null)
                    location.widenPolygon(progress);
                dirty = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        heightSeekBar = findViewById(R.id.heightSeekBar);
        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(location != null && location.getLatlng() != null && location.getMap() != null)
                    location.heightenPolygon(progress);
                dirty = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dirty = true;
                location.setReceiveNotification(checkbox.isChecked());
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                dirty = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dirty = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                dirty = true;
                location.setName(editText.getText().toString());
            }
        });

        if(update) {
            location.setMap(mMap);  //map must be set before any other operations with the location
            location.setMarker(this);
            location.setPolygon();
            heightSeekBar.setProgress((int)((location.getPolygonPoints().get(0).latitude - location.getLatlng().latitude) / Constants.POLYGON_UNIT));
            widthSeekBar.setProgress((int)((location.getPolygonPoints().get(0).longitude - location.getLatlng().longitude) / Constants.POLYGON_UNIT));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location.getLatlng()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            if(checkLocationPermission()) {
                LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                            @Override
                            public void onSuccess(android.location.Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                                }
                            }
                        });
            }
            heightSeekBar.setEnabled(false);
            widthSeekBar.setEnabled(false);
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng point) {
                    if(!markerSet) {
                        markerSet = true;
                        location.setMap(mMap);
                        location.setLatlng(point);
                        location.setMarker(getApplicationContext());
                        heightSeekBar.setEnabled(true);
                        widthSeekBar.setEnabled(true);
                    }
                    dirty = true;
                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.remove();
                    location.removePolygon();
                    location.setLatlng(null);
                    heightSeekBar.setEnabled(false);
                    widthSeekBar.setEnabled(false);
                    markerSet = false;
                    dirty = false;
                    return false;
                }
            });
        }
    }

    // Check for permission to access Location
    private boolean checkLocationPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkLocationPermission())
                        mMap.setMyLocationEnabled(true);
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {
                    Toast.makeText(this, "Application requires location permissions", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!update) {
            if(dirty) {
                location.setName(editText.getText().toString());
                location.setReceiveNotification(checkbox.isChecked());
                location.setMode(DBUtilities.getMode(1)); //TODO
                switch(getIntent().getExtras().getString("Fixed")) {
                    case "Home": {
                        location.setImage(Constants.HOME_ICON);
                        DBUtilities.saveHome(location);
                        break;
                    }
                    case "School": {
                        location.setImage(Constants.SCHOOL_BUTTON);
                        DBUtilities.saveSchool(location);
                        break;
                    }
                    case "Work": {
                        location.setImage(Constants.WORK_BUTTON);
                        DBUtilities.saveWork(location);
                        break;
                    }
                    default: {
                        location.setImage(R.drawable.ic_location_on_black_24dp);
                        DBUtilities.saveLocation(location);
                        break;
                    }
                }

            }
        } else {
            switch(getIntent().getExtras().getString("Fixed")) {
                case "Home": {
                    DBUtilities.updateHome(location);
                    break;
                }
                case "School": {
                    DBUtilities.updateSchool(location);
                    break;
                }
                case "Work": {
                    DBUtilities.updateWork(location);
                    break;
                }
                default: {
                    DBUtilities.updateLocation(location);
                    break;
                }
            }
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_preferences) {
        } else if (id == R.id.nav_howto) {

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
