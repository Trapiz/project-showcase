package com.saxion.sanderh.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.saxion.sanderh.androidproject.adapters.ResultAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RaceActivity extends BaseActivity {

    ListView resultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        createMenu();

        resultsListView = findViewById(R.id.results_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id == -1) return;

        String url = DataFetcher.formatURL("current/" + (id + 1) + "/results");

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    JSONObject race = null;
                    try {
                        race = response.getJSONObject("MRData")
                                .getJSONObject("RaceTable")
                                .getJSONArray("Races")
                                .getJSONObject(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (race == null) {
                        Intent FutureRaceIntent = new Intent(this, MainActivity.class);
                        FutureRaceIntent.putExtra("id", id);

                        startActivity(FutureRaceIntent);
                    } else {
                        JSONArray results = race.optJSONArray("Results");
                        toolbar.setTitle(race.optString("raceName").replace("Grand Prix", "GP") + " Results");

                        ResultAdapter resultAdapter = new ResultAdapter(this, results);
                        resultsListView.setAdapter(resultAdapter);
                    }


                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);
    }

}