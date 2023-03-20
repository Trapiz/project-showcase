package com.saxion.sanderh.androidproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.saxion.sanderh.androidproject.daos.PlayerDao;
import com.saxion.sanderh.androidproject.daos.RaceDao;
import com.saxion.sanderh.androidproject.daos.ResultDao;
import com.saxion.sanderh.androidproject.models.Player;
import com.saxion.sanderh.androidproject.models.Race;
import com.saxion.sanderh.androidproject.models.Result;

@Database(entities = {Player.class, Race.class, Result.class}, version = 11)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlayerDao PlayerDao();
    public abstract RaceDao RaceDao();
    public abstract ResultDao ResultDao();

    static AppDatabase instance;

    static synchronized public AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "f1-manager")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

}
