package com.saxion.sanderh.androidproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.saxion.sanderh.androidproject.adapters.SessionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private TextView trackNameTextView;
    private TextView trackLocationTextView;

    private ListView sessionListView;

    private String wikiUrl = null;
    private ArrayList<Object> coordinates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setCancelable(false);
            builder.setTitle("No internet connection!");
            builder.setMessage("This app requires internet connection to work");

            builder.setNegativeButton("Close", (dialog, which) -> finishAndRemoveTask());
            builder.show();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMenu();

        trackNameTextView = findViewById(R.id.track_name);
        trackLocationTextView = findViewById(R.id.track_location);

        Button openWikipediaButton = findViewById(R.id.open_wikipedia);
        Button showOnMapsButton = findViewById(R.id.show_on_maps);

        sessionListView = findViewById(R.id.sessions_list);

        openWikipediaButton.setOnClickListener(v -> {
            if (wikiUrl != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl));
                startActivity(browserIntent);
            }
        });

        showOnMapsButton.setOnClickListener(v -> {

            String lat = coordinates.get(0).toString();
            String lon = coordinates.get(1).toString();

            String uri = "geo:<" + lat + ">,<" + lon + ">?q=<" + lat + ">,<" + lon + ">(Label+Name)?z=80";
            if (coordinates != null) {
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(mapsIntent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        String url;

        if (id >= 0) url = DataFetcher.formatURL("current/" + (id + 1));
        else url = DataFetcher.formatURL("current/next");
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

                    if (race != null) {
                        JSONObject circuit = race.optJSONObject("Circuit");
                        if (circuit == null) return;

                        JSONObject location = circuit.optJSONObject("Location");
                        if (location == null) return;

                        toolbar.setTitle(race.optString("raceName").replace("Grand Prix", "GP"));

                        trackNameTextView.setText(circuit.optString("circuitName"));
                        String trackPosition = location.optString("locality") + ", " + location.optString("country");
                        trackLocationTextView.setText(trackPosition);
                        wikiUrl = circuit.optString("url");

                        coordinates = new ArrayList<>();
                        coordinates.add(location.optDouble("lat"));
                        coordinates.add(location.optDouble("long"));


                        SessionAdapter sessionAdapter = new SessionAdapter(this, getSessions(race));
                        sessionListView.setAdapter(sessionAdapter);


                    }
                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);

    }

    public ArrayList<JSONObject> getSessions(JSONObject race) {

        ArrayList<JSONObject> sessions = new ArrayList<>();

        JSONObject firstPractice = race.optJSONObject("FirstPractice");
        JSONObject secondPractice = race.optJSONObject("SecondPractice");
        JSONObject thirdPractice = race.optJSONObject("ThirdPractice");
        JSONObject qualifying = race.optJSONObject("Qualifying");
        JSONObject sprint = race.optJSONObject("Sprint");


        try {
            if (firstPractice != null) {
                firstPractice.put("sessionName", "1st Practice");
                sessions.add(firstPractice);
            }
            if (secondPractice != null) {
                secondPractice.put("sessionName", "2nd Practice");
                sessions.add(secondPractice);
            }
            if (thirdPractice != null) {
                thirdPractice.put("sessionName", "3rd Practice");
                sessions.add(thirdPractice);
            }
            if (qualifying != null) {
                qualifying.put("sessionName", "Qualifying");
                sessions.add(qualifying);
            }
            if (sprint != null) {
                sprint.put("sessionName", "Sprint");
                sessions.add(sprint);
            }
            JSONObject Race = new JSONObject();
            Race.put("sessionName", "Race");
            Race.put("date", race.optString("date"));
            Race.put("time", race.optString("time"));
            sessions.add(Race);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sessions;
    }

}