package com.example.locationtracker.fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.locationtracker.R;
import com.example.locationtracker.data.entity.LocationEntity;
import com.example.locationtracker.data.repository.LocationRepository;
import com.example.locationtracker.helpers.GeofenceHelper;
import com.example.locationtracker.helpers.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class AddLocationFragment extends Fragment {

    private EditText etName, etLatitude, etLongitude, etRadius;
    private Button btnUseCurrentLocation, btnSaveLocation;

    private LocationRepository repository;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeofencingClient geofencingClient;

    public AddLocationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_location, container, false);

        etName = view.findViewById(R.id.et_name);
        etLatitude = view.findViewById(R.id.et_latitude);
        etLongitude = view.findViewById(R.id.et_longitude);
        etRadius = view.findViewById(R.id.et_radius);

        btnUseCurrentLocation = view.findViewById(R.id.btn_use_current_location);
        btnSaveLocation = view.findViewById(R.id.btn_save_location);

        repository = new LocationRepository(requireContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        geofencingClient = LocationServices.getGeofencingClient(requireActivity());

        btnUseCurrentLocation.setOnClickListener(v -> {
            if (PermissionHelper.hasLocationPermission(requireContext())) {
                fillCurrentLocation();
            } else {
                PermissionHelper.requestLocationPermission(requireActivity());
            }
        });

        btnSaveLocation.setOnClickListener(v -> saveLocation());

        return view;
    }

    @SuppressLint("MissingPermission")
    private void fillCurrentLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        etLatitude.setText(String.valueOf(location.getLatitude()));
                        etLongitude.setText(String.valueOf(location.getLongitude()));
                    } else {
                        Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                );
    }

    private void saveLocation() {
        String name = etName.getText().toString().trim();
        String latText = etLatitude.getText().toString().trim();
        String lngText = etLongitude.getText().toString().trim();
        String radiusText = etRadius.getText().toString().trim();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(latText) ||
                TextUtils.isEmpty(lngText) ||
                TextUtils.isEmpty(radiusText)) {

            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double latitude = Double.parseDouble(latText);
            double longitude = Double.parseDouble(lngText);
            float radius = Float.parseFloat(radiusText);

            if (latitude < -90 || latitude > 90) {
                Toast.makeText(requireContext(), "Latitude must be between -90 and 90", Toast.LENGTH_SHORT).show();
                return;
            }

            if (longitude < -180 || longitude > 180) {
                Toast.makeText(requireContext(), "Longitude must be between -180 and 180", Toast.LENGTH_SHORT).show();
                return;
            }

            if (radius <= 0) {
                Toast.makeText(requireContext(), "Radius must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            LocationEntity location = new LocationEntity(name, latitude, longitude, radius);
            repository.insertLocation(location);

            registerGeofence(name, latitude, longitude, radius);

            Toast.makeText(requireContext(), "Location and geofence saved", Toast.LENGTH_SHORT).show();
            clearFields();

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter valid numeric coordinates and radius", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void registerGeofence(String name, double latitude, double longitude, float radius) {
        Geofence geofence = GeofenceHelper.createGeofence(name, latitude, longitude, radius);

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Location permission required for geofencing", Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.addGeofences(
                        GeofenceHelper.createGeofencingRequest(geofence),
                        GeofenceHelper.getGeofencePendingIntent(requireContext())
                )
                .addOnSuccessListener(unused ->
                        Toast.makeText(requireContext(), "Geofence registered", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to register geofence", Toast.LENGTH_SHORT).show()
                );
    }

    private void clearFields() {
        etName.setText("");
        etLatitude.setText("");
        etLongitude.setText("");
        etRadius.setText("");
    }
}