package com.chs.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chs.app.db.DBUtilities;
import com.chs.app.entities.Location;
import com.chs.app.entities.Mode;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static AsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DBUtilities db = new DBUtilities();
        DBUtilities.initialize(
                getSharedPreferences("location_preferences", Context.MODE_PRIVATE),
                getSharedPreferences("mode_preferences", Context.MODE_PRIVATE)
        );

        //TODO: Temporary
        DBUtilities.deleteModes();
        DBUtilities.saveMode(new Mode("Mode1"));
        DBUtilities.saveMode(new Mode("Mode2"));
        DBUtilities.saveMode(new Mode("Mode3"));

        DBUtilities.getAllModes();

        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                while(checkLocationPermission()) {
                    LocationServices.getFusedLocationProviderClient(MainActivity.this).getLastLocation()
                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<android.location.Location>() {
                                @Override
                                public void onSuccess(android.location.Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                                        List<Location> locations = DBUtilities.getAllLocations();
                                        for (Location index : locations) {
                                            if (PolyUtil.containsLocation(currentPosition, index.getPolygonPoints(), true)) {
                                                //if(the locations mode is not set) {set the locations mode}
                                                System.out.println("I'm in " + index.getName());
                                                break;
                                            }
                                        }
                                        Location home, work, school;
                                        LatLng defaultLatLng = new LatLng(0,0);
                                        if(!(home = DBUtilities.getHome()).getLatlng().equals(defaultLatLng)) {
                                            if(PolyUtil.containsLocation(currentPosition, home.getPolygonPoints(), true)) {
                                                //if(home mode is not set) { set home mode}
                                                System.out.println("I'm home");
                                            }
                                        }
                                        if(!(work = DBUtilities.getWork()).getLatlng().equals(defaultLatLng)) {
                                            if(PolyUtil.containsLocation(currentPosition, work.getPolygonPoints(), true)) {
                                                //if(work mode is not set) {set work mode}
                                                System.out.println("I'm at work");
                                            }
                                        }
                                        if(!(school = DBUtilities.getSchool()).getLatlng().equals(defaultLatLng)) {
                                            if(PolyUtil.containsLocation(currentPosition, school.getPolygonPoints(), true)) {
                                                //if(school mode is not set) {set school mode}
                                                System.out.println("I'm at school");
                                            }
                                        }
                                    }
                                }
                            });
                }
                return null;
            }
        };
        asyncTask.execute();
    }

    public void moveToLocation(View view) {
        startActivity(new Intent(this, LocationActivity.class));
    }

    public void moveToMode(View view) {
        startActivity(new Intent(this, ModeActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private boolean checkLocationPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }
}
