package com.example.locationtracker.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.locationtracker.data.dao.LocationDao;
import com.example.locationtracker.data.db.AppDatabase;
import com.example.locationtracker.data.entity.LocationEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationRepository {

    private static final String DATABASE_NAME = "location_tracker_db";

    private final LocationDao locationDao;
    private final ExecutorService executorService;

    public LocationRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
        ).build();

        locationDao = db.locationDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertLocation(LocationEntity location) {
        executorService.execute(() -> locationDao.insert(location));
    }

    public void deleteLocation(LocationEntity location) {
        executorService.execute(() -> locationDao.delete(location));
    }

    public void deleteAllLocations() {
        executorService.execute(locationDao::deleteAll);
    }

    public void getAllLocations(RepositoryCallback<List<LocationEntity>> callback) {
        executorService.execute(() -> {
            List<LocationEntity> locations = locationDao.getAllLocations();
            callback.onComplete(locations);
        });
    }

    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }
}