package com.saxion.sanderh.androidproject.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public Player(String name) {
        this.name = name;
    }
}
