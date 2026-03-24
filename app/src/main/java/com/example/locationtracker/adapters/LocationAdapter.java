package com.example.locationtracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtracker.R;
import com.example.locationtracker.data.entity.LocationEntity;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(LocationEntity location);
    }

    private final Context context;
    private final OnDeleteClickListener deleteClickListener;
    private List<LocationEntity> locations = new ArrayList<>();

    public LocationAdapter(Context context, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.deleteClickListener = deleteClickListener;
    }

    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationEntity location = locations.get(position);

        holder.txtName.setText(location.getName());
        holder.txtCoordinates.setText("Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
        holder.txtRadius.setText("Radius: " + location.getRadius() + " m");

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(location);
            }
        });

        holder.btnNavigate.setOnClickListener(v -> {
            try {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                        location.getLatitude() + "," + location.getLongitude());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Unable to launch navigation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCoordinates, txtRadius;
        Button btnNavigate, btnDelete;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_location_name);
            txtCoordinates = itemView.findViewById(R.id.txt_location_coordinates);
            txtRadius = itemView.findViewById(R.id.txt_location_radius);
            btnNavigate = itemView.findViewById(R.id.btn_navigate);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}