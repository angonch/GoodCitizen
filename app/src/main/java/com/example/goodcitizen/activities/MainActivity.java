package com.example.goodcitizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.goodcitizen.R;
import com.example.goodcitizen.fragments.ElectionsFragment;
import com.example.goodcitizen.fragments.MapsFragment;
import com.example.goodcitizen.fragments.VoterInfoRepsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private ElectionsFragment fragmentElections;
    private MapsFragment fragmentMaps;
    private VoterInfoRepsFragment fragmentVoterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tool bar set up - clicking on profile icon starts account activity
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.account) {
                    // compose icon clicked
                    // navigate to compose activity
                    //TODO :ANIMATE EXPAND SCREEN
                    return startAccountActivity();
                }
                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // add fragments to the fragment manager to hide and show later
        fragmentElections = new ElectionsFragment();
        fragmentMaps = new MapsFragment();
        fragmentVoterInfo = new VoterInfoRepsFragment();
        fragmentManager.beginTransaction().add(R.id.flContainer, fragmentElections, "fragmentElections").commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, fragmentVoterInfo, "fragmentVoterInfo").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // menu item is one of the three items - check which item has been tapped
                switch (menuItem.getItemId()) {
                    case R.id.action_elections:
                        displayELectionFragment();
                        break;
                    case R.id.action_locations:
                        displayMapsFragment();
                        break;
                    case R.id.action_reps:
                    default:
                        displayVoterInfoFragment();
                        break;
                }
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_elections);
    }

    private boolean startAccountActivity() {
        Intent i = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(i);
        return true;
    }

    // show elections fragment and hide others
    protected void displayELectionFragment() {
        fragmentManager.beginTransaction().hide(fragmentMaps).commit();
        fragmentManager.beginTransaction().hide(fragmentVoterInfo).commit();
        fragmentManager.beginTransaction().show(fragmentElections).commit();
    }

    // show maps fragment and hide others
    protected void displayMapsFragment() {
        fragmentManager.beginTransaction().hide(fragmentElections).commit();
        fragmentManager.beginTransaction().hide(fragmentVoterInfo).commit();
        if(!fragmentMaps.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.flContainer, fragmentMaps, "fragmentMaps").commit();
        }
        fragmentManager.beginTransaction().show(fragmentMaps).commit();
    }

    // show voter info fragment and hide others
    protected void displayVoterInfoFragment() {
        fragmentManager.beginTransaction().hide(fragmentElections).commit();
        fragmentManager.beginTransaction().hide(fragmentMaps).commit();
        fragmentManager.beginTransaction().show(fragmentVoterInfo).commit();
    }
}