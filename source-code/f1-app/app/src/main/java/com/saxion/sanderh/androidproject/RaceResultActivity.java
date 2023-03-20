package com.saxion.sanderh.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.saxion.sanderh.androidproject.adapters.RaceResultAdapter;
import com.saxion.sanderh.androidproject.daos.ResultDao;
import com.saxion.sanderh.androidproject.models.Result;

import java.util.List;

public class RaceResultActivity extends BaseActivity {

    ListView resultListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result);
        createMenu();

        resultListView = findViewById(R.id.results_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        if (id == -1) return;

        ResultDao dao = AppDatabase.getInstance(this).ResultDao();

        List<Result> results = dao.getResultsById(id);

        RaceResultAdapter raceResultAdapter = new RaceResultAdapter(this, results);
        resultListView.setAdapter(raceResultAdapter);

    }
}