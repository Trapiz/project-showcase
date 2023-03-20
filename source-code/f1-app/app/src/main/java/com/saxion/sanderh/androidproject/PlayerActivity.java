package com.saxion.sanderh.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.adapters.PlayerAdapter;
import com.saxion.sanderh.androidproject.daos.PlayerDao;
import com.saxion.sanderh.androidproject.models.Player;

import java.util.List;

public class PlayerActivity extends BaseActivity {

    Button createPlayer;
    ListView playersListView;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        createMenu();

        AppDatabase db = AppDatabase.getInstance(this);
        PlayerDao dao = db.PlayerDao();

        createPlayer = findViewById(R.id.create_player);
        playersListView = findViewById(R.id.players);
        nameEditText = findViewById(R.id.player_name_input);

        createPlayer.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();

            if (name.length() == 0) return;

            dao.addPlayer(new Player(name));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppDatabase db = AppDatabase.getInstance(this);
        PlayerDao dao = db.PlayerDao();

        dao.getAll().observe(this, players -> {

            PlayerAdapter playerAdapter = new PlayerAdapter(this, players);
            playersListView.setAdapter(playerAdapter);
        });


    }
}