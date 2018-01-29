package com.chs.app;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chs.app.video.LocationVideoActivity;
import com.chs.app.video.ModeVideoActivity;

public class HowtoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);

        Toolbar toolbar = findViewById(R.id.toolbarHowto);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button addLocationButton = findViewById(R.id.locationVideoButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HowtoActivity.this, LocationVideoActivity.class));
            }
        });
        /*addLocationButton.setCompoundDrawablesRelative(
                null,
                getApplicationContext().getResources().getDrawable(R.drawable.add_location),
                null,
                null );*/

        Button addModeButton = findViewById(R.id.modeVideoButton);
        addModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HowtoActivity.this, ModeVideoActivity.class));
            }
        });
        /*Drawable modeImg = getApplicationContext().getResources().getDrawable(R.drawable.add_mode);
        modeImg.setBounds( 0, 50, 0, 150 );
        addModeButton.setCompoundDrawables( modeImg, null, null, null );*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (id == R.id.nav_howto) {
            startActivity(new Intent(this, HowtoActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_exit) {
            notificationManager.cancel(Constants.APP_NOTIFICATION_ID);
            notificationManager.cancel(Constants.LOCATION_NOTIFICATION_ID);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
