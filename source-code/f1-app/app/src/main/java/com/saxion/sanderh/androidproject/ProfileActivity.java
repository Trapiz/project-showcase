package com.saxion.sanderh.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.saxion.sanderh.androidproject.adapters.PlayerRaceAdapter;
import com.saxion.sanderh.androidproject.daos.RaceDao;
import com.saxion.sanderh.androidproject.models.Race;

import java.util.List;

public class ProfileActivity extends BaseActivity {

    static long selectedPlayerId = -1;

    public ListView raceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        createMenu();

        raceListView = findViewById(R.id.race_list);

        raceListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent resultIntent = new Intent(this, RaceResultActivity.class);
            resultIntent.putExtra("id", position);

            startActivity(resultIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selectedPlayerId == -1) startActivity(new Intent(this, PlayerActivity.class));

        RaceDao raceDao = AppDatabase.getInstance(this).RaceDao();

        List<Race> races = raceDao.getRacesByPlayerId(selectedPlayerId);

        PlayerRaceAdapter playerRaceAdapter = new PlayerRaceAdapter(this, races);
        raceListView.setAdapter(playerRaceAdapter);

    }
}