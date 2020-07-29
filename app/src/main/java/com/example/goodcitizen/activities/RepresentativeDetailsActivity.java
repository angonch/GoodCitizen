package com.example.goodcitizen.activities;

import android.os.Bundle;
import android.view.View;
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
    ImageView ivPartyBackground;

    RepresentativeModel representative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_details);

        tvOfficialName = findViewById(R.id.tvOfficialName);
        tvPosition = findViewById(R.id.tvPosition);
        tvParty = findViewById(R.id.tvParty);
        ivPhoto = findViewById(R.id.ivPhoto);
        ivPartyBackground = findViewById(R.id.ivPartyBackground);

        // unwrap representative data
        representative = Parcels.unwrap(getIntent().getParcelableExtra(RepresentativeModel.class.getSimpleName()));

        // set data
        tvOfficialName.setText(representative.getOfficialName());
        tvPosition.setText(representative.getPosition());
        tvParty.setText(representative.getParty());
        // set color of image view for party background color
        Integer color;
        switch(representative.getParty()) {
            case "Democratic Party":
                color = getApplicationContext().getResources().getColor(R.color.democratic);
                break;
            case "Republican Party":
                color = getApplicationContext().getResources().getColor(R.color.republican);
                break;
            case "Nonpartisan":
            default:
                color = getApplicationContext().getResources().getColor(R.color.nonpartisan);
                break;
        }
        ivPartyBackground.setColorFilter(color);
        tvParty.setTextColor(color);
        if(!representative.getPhotoUrl().isEmpty()) {
            ivPhoto.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(representative.getPhotoUrl()).into(ivPhoto);
        } else {
            ivPhoto.setVisibility(View.GONE);
        }
    }
}