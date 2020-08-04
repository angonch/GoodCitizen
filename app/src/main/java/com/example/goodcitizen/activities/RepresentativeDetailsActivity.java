package com.example.goodcitizen.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.RepresentativeModel;

import org.parceler.Parcels;

import java.util.Map;

public class RepresentativeDetailsActivity extends AppCompatActivity {

    public static final String TAG = "RepresentativeDetailsActivity";

    TextView tvOfficialName;
    TextView tvPosition;
    TextView tvParty;
    ImageView ivPhoto;
    ImageView ivPartyBackground;
    LinearLayout llChannels;
    ImageView ivFacebook;
    ImageView ivTwitter;
    ImageView ivYouTube;
    CardView cvWesbite;
    TextView tvUrl;
    CardView cvEmail;
    TextView tvEmail;

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
        llChannels = findViewById(R.id.llChannels);
        ivFacebook = findViewById(R.id.ivFb);
        ivTwitter = findViewById(R.id.ivTwitter);
        ivYouTube = findViewById(R.id.ivYt);
        cvWesbite = findViewById(R.id.cvWebsite);
        tvUrl = findViewById(R.id.tvWebsite);
        cvEmail = findViewById(R.id.cvEmail);
        tvEmail = findViewById(R.id.tvEmailUrl);

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

        // show social media icons/link urls
        showChannelIcons();

        // show website/link url
        if(representative.getUrl().isEmpty()) {
            cvWesbite.setVisibility(View.GONE);
        } else {
            tvUrl.setText(representative.getUrl());
            cvWesbite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(representative.getUrl()));
                    startActivity(intent);
                }
            });
        }

        // show email/link url
        if(representative.getEmail().isEmpty()) {
            cvEmail.setVisibility(View.GONE);
        } else {
            final String email = representative.getEmail();
            tvEmail.setText(email);
            cvEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });
        }
    }

    private void showChannelIcons() {
        Map<String, String> repChannels = representative.getChannels();
        if(repChannels == null) {
            llChannels.setVisibility(View.GONE);
            return;
        }
        if(!repChannels.containsKey("Facebook")) {
            // hide social media icon if rep. doesn't have one
            ivFacebook.setVisibility(View.GONE);
        } else {
            // add links to social media icons
            ivFacebook.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(representative.getFacebookUrl()));
                    startActivity(intent);
                }
            });
        }

        if(!repChannels.containsKey("Twitter")) {
            // hide social media icon if rep. doesn't have one
            ivTwitter.setVisibility(View.GONE);
        } else {
            // add links to social media icons
            ivTwitter.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(representative.getTwitterUrl()));
                    startActivity(intent);
                }
            });
        }

        if(!repChannels.containsKey("YouTube")) {
            // hide social media icon if rep. doesn't have one
            ivYouTube.setVisibility(View.GONE);
        } else {
            // add links to social media icons
            ivYouTube.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(representative.getYoutubeUrl()));
                    startActivity(intent);
                }
            });
        }
    }
}