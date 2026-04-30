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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment {

    private TextView txtLatitude, txtLongitude;
    private Button btnGetLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Important for OSMDroid
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        txtLatitude = view.findViewById(R.id.txt_latitude);
        txtLongitude = view.findViewById(R.id.txt_longitude);
        btnGetLocation = view.findViewById(R.id.btn_get_location);

        mapView = view.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        mapController.setZoom(15.0);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        btnGetLocation.setOnClickListener(v -> {
            if (PermissionHelper.hasLocationPermission(requireContext())) {
                getCurrentLocation();
            } else {
                PermissionHelper.requestLocationPermission(requireActivity());
            }
        });

        if (PermissionHelper.hasLocationPermission(requireContext())) {
            enableMyLocation();
        }

        return view;
    }

    private void enableMyLocation() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        updateLocationUI(location);
                        updateMapLocation(location);
                    } else {
                        Toast.makeText(
                                requireContext(),
                                "Unable to get current location.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                requireContext(),
                                "Failed to get location",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void updateLocationUI(Location location) {
        txtLatitude.setText(String.valueOf(location.getLatitude()));
        txtLongitude.setText(String.valueOf(location.getLongitude()));
    }

    private void updateMapLocation(Location location) {
        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.animateTo(startPoint);

        // Add a marker
        mapView.getOverlays().removeIf(overlay -> overlay instanceof Marker);
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("You are here");
        mapView.getOverlays().add(startMarker);
        mapView.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (PermissionHelper.hasLocationPermission(requireContext())) {
            getCurrentLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
