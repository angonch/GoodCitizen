package com.example.goodcitizen.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.R;
import com.example.goodcitizen.models.ElectionModel;

import org.parceler.Parcels;

public class ElectionDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ElectionDetailsActivity";

    TextView tvElectionName;
    TextView tvDate;

    ElectionModel election;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_details);

        tvElectionName = findViewById(R.id.tvElectionName);
        tvDate = findViewById(R.id.tvDate);

        // unwrap election data
        election = Parcels.unwrap(getIntent().getParcelableExtra(ElectionModel.class.getSimpleName()));

        // set data
        tvElectionName.setText(election.getElectionName());
        tvDate.setText(election.getDate());
    }
}