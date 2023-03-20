package com.saxion.sanderh.androidproject;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;
import com.saxion.sanderh.androidproject.adapters.CalendarAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class CalendarActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ListView calendarListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        createMenu();

        calendarListView = findViewById(R.id.calendar);

        calendarListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, RaceActivity.class);
            intent.putExtra("id", position);

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url = DataFetcher.formatURL("current");

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    JSONArray races = null;
                    try {
                        races = response.getJSONObject("MRData")
                                .getJSONObject("RaceTable")
                                .getJSONArray("Races");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (races == null) return;

                    CalendarAdapter calendarAdapter = new CalendarAdapter(this, races);
                    calendarListView.setAdapter(calendarAdapter);

                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);
    }

}