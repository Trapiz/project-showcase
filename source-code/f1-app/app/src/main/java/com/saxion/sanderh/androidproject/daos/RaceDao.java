package com.saxion.sanderh.androidproject.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.saxion.sanderh.androidproject.models.Race;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RaceDao {

    @Query("SELECT * FROM Race")
    List<Race> getAll();

    @Query("SELECT * FROM Race WHERE id = :id")
    Race getRaceById(long id);

    @Query("SELECT * FROM Race WHERE playerId = :id")
    List<Race> getRacesByPlayerId(long id);

    @Insert
    long addRace(Race race);

    @Update
    void updateRace(Race race);



}
