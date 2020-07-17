package com.example.goodcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodcitizen.R;
import com.example.goodcitizen.activities.LocationDetailsActivity;
import com.example.goodcitizen.models.LocationModel;

import org.parceler.Parcels;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    public static final String TAG = "LocationAdapter";
    Context context;
    List<LocationModel> locations;

    public LocationAdapter(Context context, List<LocationModel> locations) {
        this.context = context;
        this.locations = locations;
    }

    //  inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Inflate item_election XML to get View
        View locationView = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        // Wrap view inside a holder
        return new LocationAdapter.ViewHolder(locationView);
    }

    // Populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        // Get the election at the position
        LocationModel location = locations.get(position);
        // Bind election data to holder
        holder.bind(location);
    }

    // Returns total count of items in the list
    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvLocationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            itemView.setOnClickListener(this);
        }

        public void bind(LocationModel location) {
            tvLocationName.setText(location.getLocationName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                LocationModel location = locations.get(position);
                Intent i = new Intent(context, LocationDetailsActivity.class);
                i.putExtra(LocationModel.class.getSimpleName(), Parcels.wrap(location));
                context.startActivity(i);
            }
        }
    }
}
