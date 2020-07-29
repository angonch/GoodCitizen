package com.example.goodcitizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
        final Fragment fragment1 = new ElectionsFragment();
        final Fragment fragment2 = new MapsFragment();
        final Fragment fragment3 = new VoterInfoRepsFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // menu item is one of the three items - check which item has been tapped
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_elections:
                        fragment = fragment1;
                        break;
                    case R.id.action_locations:
                        fragment = fragment2;
                        break;
                    case R.id.action_reps:
                    default:
                        fragment = fragment3;
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

    private boolean startAccountActivity() {
        Intent i = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(i);
        return true;
    }
}