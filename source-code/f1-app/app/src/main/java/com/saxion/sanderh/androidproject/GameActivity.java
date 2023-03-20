package com.saxion.sanderh.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.saxion.sanderh.androidproject.adapters.GameStandingsAdapter;
import com.saxion.sanderh.androidproject.adapters.NotificationAdapter;
import com.saxion.sanderh.androidproject.daos.RaceDao;
import com.saxion.sanderh.androidproject.daos.ResultDao;
import com.saxion.sanderh.androidproject.models.Race;
import com.saxion.sanderh.androidproject.models.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity {

    private final String[] delays = {
            " spun of the track",
            " had to slow down for a bird",
    };

    private final int totalLaps = 60;
    private int currentLap = 0;

    Race race;


    ProgressBar progressBar;
    TextView totalLapsTextView;
    ListView notificationsListView;
    ListView standingsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createMenu();

        progressBar = findViewById(R.id.progressBar);
        totalLapsTextView = findViewById(R.id.total_laps);
        notificationsListView = findViewById(R.id.notifications);
        standingsListView = findViewById(R.id.standings);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<GameDriver> gameDrivers = new ArrayList<>();

        Intent intent = getIntent();
        int driverId = intent.getIntExtra("driverId", -1);
        long playerId = intent.getLongExtra("playerId", -1);

        if (driverId == -1 || playerId == -1) return;

        RaceDao raceDao = AppDatabase.getInstance(this).RaceDao();

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

                    if (drivers != null) {
                        for(int i = 0; i < drivers.length(); i++) {
                            JSONObject driver = drivers.optJSONObject(i);

                            String name = driver.optString("givenName") + " " + driver.optString("familyName");
                            GameDriver gameDriver = new GameDriver(i, name, driver.optString("code"), i + 1, i == driverId);
                            gameDrivers.add(gameDriver);
                        }


                    }

                    totalLapsTextView.setText(String.valueOf(totalLaps));
                    ArrayList<String> notifications = new ArrayList<>();

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentLap);

                            for (GameDriver driver : gameDrivers) {

                                int newLapTime = getRandomNumber(80000, 90000);

                                if (getRandomNumber(0, 750) == 0) {
                                    newLapTime += getRandomNumber(3000, 6000);
                                    notifications.add(currentLap + " - " + driver.name + delays[getRandomNumber(0, delays.length)]);
                                }

                                if (driver.tireAgeLeft == 0 && currentLap < totalLaps - 10) {
                                    notifications.add(currentLap + " - " + driver.name + "is entering the pit!");
                                    newLapTime += getRandomNumber(20000, 25000);
                                    driver.setTireAge();
                                    driver.pitCount++;
                                }

                                driver.addMillis(newLapTime);
                                driver.tireAgeLeft--;

                            }

                            if (currentLap == totalLaps) {
                                notifications.add(" --- Race finished! ---");
                                postNotifications(notifications);

                                Collections.sort(gameDrivers);
                                ArrayList<GameDriver> finalDrivers = updateDriverList(gameDrivers);

                                for (GameDriver driver : finalDrivers) {
                                    ResultDao resultDao = AppDatabase.getInstance(GameActivity.this).ResultDao();

                                    long raceId = 0;

                                    if(driver.id == driverId) {
                                        race = new Race(
                                                driver.name,
                                                playerId,
                                                driver.position,
                                                formatTime(driver.totalLapTime.getTimeInMillis())
                                        );

                                        raceId = raceDao.addRace(race);
                                    }

                                    resultDao.addResult(new Result(driver.name, driver.position, formatTime(driver.totalLapTime.getTimeInMillis()), raceId));
                                }

                                this.cancel();
                            }

                            postNotifications(notifications);
                            showStandings(gameDrivers);

                            currentLap++;
                        }
                    }, 0, 500);

                }, System.out::println);

        DataFetcher.getInstance(this).addToRequestQueue(request);



    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void showStandings(ArrayList<GameDriver> drivers) {
        Collections.sort(drivers);
        ArrayList<GameDriver> finalDrivers = updateDriverList(drivers);

        runOnUiThread(() -> {
            int index = standingsListView.getFirstVisiblePosition();
            GameStandingsAdapter gameStandingsAdapter = new GameStandingsAdapter(this, finalDrivers);
            standingsListView.setAdapter(gameStandingsAdapter);
            standingsListView.setSelectionFromTop(index, 0);
        });
    }

    public ArrayList<GameDriver> updateDriverList(ArrayList<GameDriver> drivers) {
        for (int i = 0; i < drivers.size(); i++) {

            GameDriver currentDriver = drivers.get(i);

            if (i == 0) {
                currentDriver.position = 1;
                currentDriver.gapToCarAhead.setTimeInMillis(0);
                continue;
            };

            GameDriver driverAhead = drivers.get(i - 1);

            currentDriver.position = i + 1;
            currentDriver.setGap(currentDriver.totalLapTime.getTimeInMillis() - driverAhead.totalLapTime.getTimeInMillis());
        }

        return drivers;
    }

    public void postNotifications(ArrayList<String> notifications) {
        runOnUiThread(() -> {
            NotificationAdapter notificationAdapter = new NotificationAdapter(GameActivity.this, notifications);
            notificationsListView.setAdapter(notificationAdapter);
        });
    }

    public static String formatTime(long millis) {
        String hours = String.valueOf((int) Math.floor(millis / 3600000));
        millis %= 3600000;

        String minutes = String.valueOf((int) Math.floor(millis / 60000));
        millis %= 60000;

        String seconds = String.valueOf((int) Math.floor(millis / 1000));
        millis %= 1000;

        String milliseconds = String.valueOf(millis);

        if (hours.length() == 1) hours = "0" + hours;
        if (minutes.length() == 1) minutes = "0" + minutes;
        if (seconds.length() == 1) seconds = "0" + seconds;

        while (true) {
            if (milliseconds.length() < 3) milliseconds = "0" + milliseconds;
            else break;
        }

        String timeString;

        if (hours.equals("00")) timeString = minutes + ":" + seconds + "." + milliseconds;
        else timeString = hours + ":" + minutes + ":" + seconds + "." + milliseconds;

        return timeString;
    }
}