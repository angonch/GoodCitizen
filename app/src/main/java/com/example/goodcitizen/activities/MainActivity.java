package com.example.goodcitizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.goodcitizen.R;
import com.example.goodcitizen.fragments.ElectionsFragment;
import com.example.goodcitizen.fragments.LocationsMapFragment;
import com.example.goodcitizen.fragments.VoterInfoRepsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fabAccount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(i);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        final Fragment fragment1 = new ElectionsFragment();
        final Fragment fragment2 = new LocationsMapFragment();
        final Fragment fragment3 = new VoterInfoRepsFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // menu item is one of the three items - check which item has been tapped
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_elections:
                        fragment = fragment1;
                        Toast.makeText(getApplicationContext(), "Elections page!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_locations:
                        fragment = fragment2;
                        Toast.makeText(getApplicationContext(), "Locations page!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_reps:
                    default:
                        fragment = fragment3;
                        Toast.makeText(getApplicationContext(), "Voter Info page!", Toast.LENGTH_SHORT).show();
                        break;
                }
                // swap out fragments
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_elections);
    }
}