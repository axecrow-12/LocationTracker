package com.example.locationtracker.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtracker.R;
import com.example.locationtracker.adapters.LocationAdapter;
import com.example.locationtracker.data.entity.LocationEntity;
import com.example.locationtracker.data.repository.LocationRepository;

import java.util.List;

public class LocationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtEmptyState;
    private LocationAdapter adapter;
    private LocationRepository repository;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LocationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        recyclerView = view.findViewById(R.id.recycler_locations);
        txtEmptyState = view.findViewById(R.id.txt_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        repository = new LocationRepository(requireContext());


        adapter = new LocationAdapter(requireContext(), location -> {
            repository.deleteLocation(location);
            Toast.makeText(requireContext(), "Location deleted", Toast.LENGTH_SHORT).show();

            mainHandler.postDelayed(this::loadLocations, 200);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(0, 4, 0, 8);

        loadLocations();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocations();
    }

    private void loadLocations() {
        repository.getAllLocations(result -> mainHandler.post(() -> updateUI(result)));
    }

    private void updateUI(List<LocationEntity> locations) {
        adapter.setLocations(locations);

        if (locations == null || locations.isEmpty()) {
            txtEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}