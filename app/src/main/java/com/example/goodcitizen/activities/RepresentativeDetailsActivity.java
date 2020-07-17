package com.example.goodcitizen.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.RepresentativeModel;

import org.parceler.Parcels;

public class RepresentativeDetailsActivity extends AppCompatActivity {

    public static final String TAG = "RepresentativeDetailsActivity";

    TextView tvOfficialName;
    TextView tvPosition;
    TextView tvParty;
    ImageView ivPhoto;

    RepresentativeModel representative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_details);

        tvOfficialName = findViewById(R.id.tvOfficialName);
        tvPosition = findViewById(R.id.tvPosition);
        tvParty = findViewById(R.id.tvParty);
        ivPhoto = findViewById(R.id.ivPhoto);

        // unwrap representative data
        representative = Parcels.unwrap(getIntent().getParcelableExtra(RepresentativeModel.class.getSimpleName()));

        // set data
        tvOfficialName.setText(representative.getOfficialName());
        tvPosition.setText(representative.getPosition());
        tvParty.setText(representative.getParty());
        if(!representative.getPhotoUrl().isEmpty()) {
            Glide.with(getApplicationContext()).load(representative.getPhotoUrl()).into(ivPhoto);
        }
    }
}