package com.saxion.sanderh.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    void createMenu() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    public static Intent getIntent(MenuItem item, Context context) {

        switch (item.toString()) {
            case "Next Session":
                return new Intent(context, MainActivity.class);
            case "Calendar":
                return new Intent(context, CalendarActivity.class);
            case "Standings":
                return new Intent(context, StandingsActivity.class);
            case "Play":
                return new Intent(context, PlayerActivity.class);
            case "Profile":
                return new Intent(context, ProfileActivity.class);
            default:
                return null;
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = getIntent(item, getApplicationContext());
        if (intent != null) startActivity(intent);

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    protected abstract void onCreate(Bundle savedInstanceState);
}
