package com.saxion.sanderh.androidproject.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.saxion.sanderh.androidproject.models.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM Player")
    LiveData<List<Player>> getAll();

    @Insert
    long addPlayer(Player player);

    @Query("SELECT * FROM Player WHERE id = :id")
    Player getPlayerById(long id);

    @Delete
    void deletePlayer(Player player);


}
