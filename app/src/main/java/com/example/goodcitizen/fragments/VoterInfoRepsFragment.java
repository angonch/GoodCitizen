package com.example.goodcitizen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.goodcitizen.R;
import com.google.android.material.tabs.TabLayout;

public class VoterInfoRepsFragment extends Fragment {

    private TabLayout tabLayout;
    private FragmentManager fragmentManager;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_voter_info_reps, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();
        tabLayout = view.findViewById(R.id.tlVoterInfo);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private boolean alreadyReselected = false;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                if(tab.getPosition() == 0) {
                    fragment = new JurisdictionFragment();
                    Toast.makeText(getContext(), "Jurisdiction info page!", Toast.LENGTH_SHORT).show();
                } else {
                    fragment = new RepresentativesFragment();
                    Toast.makeText(getContext(), "Representatives page!", Toast.LENGTH_SHORT).show();
                }
                fragmentManager.beginTransaction().replace(R.id.flVoterInfoContainer, fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        // sets default selected tab to be the first one (need to select one and switch back to tab needed)
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();
    }
}