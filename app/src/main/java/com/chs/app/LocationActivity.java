package com.chs.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chs.app.db.DBUtilities;
import com.chs.app.entities.Location;
import com.chs.app.ui.LocationAdapter;

import java.util.ArrayList;
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
                Intent intent = new Intent(getApplicationContext(), LocationDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("Update", false);
                bundle.putString("Fixed", "No");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setList();
    }

    private void setList() {
        List<Location> locations = new ArrayList<Location>();
        locations.add(DBUtilities.getHome());
        locations.add(DBUtilities.getSchool());
        locations.add(DBUtilities.getWork());
        locations.addAll(DBUtilities.getAllLocations());

        ListView listLocations = findViewById(R.id.listLocations);
        final LocationAdapter adapter = new LocationAdapter(this, R.layout.item_location, locations);
        listLocations.setAdapter(adapter);
        listLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Animation animation = new AlphaAnimation(0.3f, 1.0f);
                animation.setDuration(3000);
                view.startAnimation(animation);

                Intent intent = new Intent(getApplicationContext(), LocationDetailsActivity.class);
                Bundle bundle = new Bundle();
                if(i > 2) {
                    bundle.putParcelable("Location", (Location) adapterView.getItemAtPosition(i));
                    bundle.putBoolean("Update", true);
                    bundle.putString("Fixed", "No");
                } else {
                    switch(i) {
                        case 0: {
                            bundle.putString("Fixed", "Home");
                            break;
                        }
                        case 1: {
                            bundle.putString("Fixed", "School");
                            break;
                        }
                        case 2: {
                            bundle.putString("Fixed", "Work");
                            break;
                        }
                        default:
                            break;
                    }
                    if(((Location) adapterView.getItemAtPosition(i)).getName().startsWith("Set")) {
                        bundle.putBoolean("Update", false);
                    } else {
                        bundle.putParcelable("Location", (Location) adapterView.getItemAtPosition(i));
                        bundle.putBoolean("Update", true);
                    }
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setList();
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
