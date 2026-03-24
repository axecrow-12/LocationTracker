package com.example.locationtracker.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.locationtracker.data.dao.LocationDao;
import com.example.locationtracker.data.entity.LocationEntity;

@Database(entities = {LocationEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}