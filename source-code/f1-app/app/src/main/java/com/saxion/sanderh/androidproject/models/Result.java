package com.saxion.sanderh.androidproject.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Result {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String driverName;
    public int position;
    public String gapToCarAhead;

    public long raceId;

    public Result(String driverName, int position, String gapToCarAhead, long raceId) {
        this.driverName = driverName;
        this.position = position;
        this.gapToCarAhead = gapToCarAhead;
        this.raceId = raceId;
    }
}
