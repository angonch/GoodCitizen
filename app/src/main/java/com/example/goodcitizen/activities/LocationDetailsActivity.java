package com.example.goodcitizen.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.R;
import com.example.goodcitizen.models.LocationModel;

import org.parceler.Parcels;

public class LocationDetailsActivity extends AppCompatActivity {

    TextView tvLocationName;
    TextView tvService;
    TextView tvAddress;
    TextView tvPollingHours;

    LocationModel location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        tvLocationName = findViewById(R.id.tvLocationName);
        tvService = findViewById(R.id.tvService);
        tvAddress = findViewById(R.id.tvAddress);
        tvPollingHours = findViewById(R.id.tvPollingHours);

        // unwrap location data
        location = Parcels.unwrap(getIntent().getParcelableExtra(LocationModel.class.getSimpleName()));

        // set data
        tvLocationName.setText(location.getLocationName());
        tvService.setText(location.getVoterService());
        tvAddress.setText(location.getAddress());
        tvPollingHours.setText(location.getPollingHours());
    }
}