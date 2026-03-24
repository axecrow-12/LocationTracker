package com.example.locationtracker.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.locationtracker.data.entity.LocationEntity;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insert(LocationEntity location);

    @Query("SELECT * FROM locations ORDER BY id DESC")
    List<LocationEntity> getAllLocations();

    @Delete
    void delete(LocationEntity location);

    @Query("DELETE FROM locations")
    void deleteAll();
}