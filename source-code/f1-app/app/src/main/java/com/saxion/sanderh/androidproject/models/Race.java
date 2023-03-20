package com.saxion.sanderh.androidproject.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Race {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String selectedDriver;
    public long playerId;

    public int finishedPosition;
    public String totalLapTime;

    public Race(String selectedDriver, long playerId, int finishedPosition, String totalLapTime) {
        this.selectedDriver = selectedDriver;
        this.playerId = playerId;
        this.finishedPosition = finishedPosition;
        this.totalLapTime = totalLapTime;
    }

}
