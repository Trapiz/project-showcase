package com.saxion.sanderh.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.saxion.sanderh.androidproject.adapters.DriverSelectorAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class DriverSelectorActivity extends BaseActivity {

    ListView driverSelectorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_selector);
        createMenu();

        driverSelectorListView = findViewById(R.id.updates);

        driverSelectorListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent gameIntent = new Intent(this, GameActivity.class);
            gameIntent.putExtra("driverId", position);

            Intent intent = getIntent();
            long playerId = intent.getLongExtra("id", -1);

            gameIntent.putExtra("playerId", playerId);

            startActivity(gameIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);

        if (id == -1) return;

        ProfileActivity.selectedPlayerId = id;

        String url = DataFetcher.formatURL("current/drivers");

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    JSONArray drivers = null;
                    try {
                        drivers = response.getJSONObject("MRData")
                                .getJSONObject("DriverTable")
                                .getJSONArray("Drivers");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DriverSelectorAdapter driverSelectorAdapter = new DriverSelectorAdapter(this, drivers);
                    driverSelectorListView.setAdapter(driverSelectorAdapter);

                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);
    }
}