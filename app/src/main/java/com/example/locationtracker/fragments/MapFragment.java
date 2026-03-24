package com.example.locationtracker.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.locationtracker.R;
import com.example.locationtracker.helpers.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MapFragment extends Fragment {

    private TextView txtLatitude, txtLongitude;
    private Button btnGetLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        txtLatitude = view.findViewById(R.id.txt_latitude);
        txtLongitude = view.findViewById(R.id.txt_longitude);
        btnGetLocation = view.findViewById(R.id.btn_get_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        btnGetLocation.setOnClickListener(v -> {
            if (PermissionHelper.hasLocationPermission(requireContext())) {
                getCurrentLocation();
            } else {
                PermissionHelper.requestLocationPermission(requireActivity());
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        updateLocationUI(location);
                    } else {
                        Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateLocationUI(Location location) {
        txtLatitude.setText("Latitude: " + location.getLatitude());
        txtLongitude.setText("Longitude: " + location.getLongitude());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PermissionHelper.hasLocationPermission(requireContext())) {
            getCurrentLocation();
        }
    }
}