package com.chs.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chs.app.entities.Location;
import com.chs.app.ui.LocationAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = findViewById(R.id.toolbarLocation);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<Location> locations = new ArrayList<Location>(Arrays.asList(
                new Location(new LatLng(45.749199, 21.241054), "Camin 8", R.drawable.ic_home_black_24dp),
                new Location(new LatLng(45.7450284, 21.2275766), "Facultate", R.drawable.ic_add_location_black_24dp),
                new Location(new LatLng(45.749199, 21.241054), "Camin 8", R.drawable.ic_home_black_24dp),
                new Location(new LatLng(45.7450284, 21.2275766), "Facultate", R.drawable.ic_add_location_black_24dp),
                new Location(new LatLng(45.749199, 21.241054), "Camin 8", R.drawable.ic_home_black_24dp),
                new Location(new LatLng(45.7450284, 21.2275766), "Facultate", R.drawable.ic_add_location_black_24dp),
                new Location(new LatLng(45.749199, 21.241054), "Camin 8", R.drawable.ic_home_black_24dp),
                new Location(new LatLng(45.7450284, 21.2275766), "Facultate", R.drawable.ic_add_location_black_24dp)
        ));
        ListView listLocations = findViewById(R.id.listLocations);
        final LocationAdapter adapter = new LocationAdapter(this, R.layout.item_location, locations);
        listLocations.setAdapter(adapter);
        listLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), LocationDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Location", (Location) adapterView.getItemAtPosition(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_preferences) {
        } else if (id == R.id.nav_howto) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
