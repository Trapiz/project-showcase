package com.saxion.sanderh.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.saxion.sanderh.androidproject.adapters.StandingsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandingsActivity extends BaseActivity {

    Spinner spinner;
    ListView standingsListView;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        createMenu();

        spinner = findViewById(R.id.spinner);
        standingsListView = findViewById(R.id.standings_list);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.standings, R.layout.spinner_item_layout);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    query = "current/driverStandings";
                    toolbar.setTitle("Driver Standings");

                }
                if (position == 1) {
                    query = "current/constructorStandings";
                    toolbar.setTitle("Constructor Standings");
                }

                updateStandings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void updateStandings() {

        String url = DataFetcher.formatURL(query);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    JSONObject standings = null;
                    try {
                        standings = response.getJSONObject("MRData")
                                .getJSONObject("StandingsTable")
                                .getJSONArray("StandingsLists")
                                .getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray participants;

                    if (standings.optJSONArray("DriverStandings") != null) {
                        participants = standings.optJSONArray("DriverStandings");
                    } else {
                        participants = standings.optJSONArray("ConstructorStandings");
                    }

                    StandingsAdapter standingsAdapter = new StandingsAdapter(this, participants);
                    standingsListView.setAdapter(standingsAdapter);


                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);

    }
}