package com.example.goodcitizen.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.goodcitizen.R;
import com.example.goodcitizen.fragments.OnboardingFragment1;
import com.example.goodcitizen.fragments.OnboardingFragment2;
import com.example.goodcitizen.fragments.OnboardingFragment3;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class OnboardingActivity extends FragmentActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = findViewById(R.id.vpPager);
        indicator = findViewById(R.id.stlIndicator);
        next = findViewById(R.id.btnNext);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new OnboardingFragment1();
                    case 1:
                        return new OnboardingFragment2();
                    case 2:
                    default:
                        return new OnboardingFragment3();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }
}