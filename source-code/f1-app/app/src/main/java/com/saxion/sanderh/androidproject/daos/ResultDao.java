package com.saxion.sanderh.androidproject.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.saxion.sanderh.androidproject.models.Player;
import com.saxion.sanderh.androidproject.models.Result;

import java.util.List;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM Result")
    List<Result> getAll();

    @Query("SELECT * FROM Result WHERE raceId = :id")
    List<Result> getResultsById(long id);

    @Insert
    long addResult(Result result);

}
