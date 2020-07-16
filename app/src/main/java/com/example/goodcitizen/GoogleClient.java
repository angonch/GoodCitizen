package com.example.goodcitizen;

import android.content.Context;

public class GoogleClient {

    public static final String TAG = "GoogleClient";
    public static final String ELECTIONS_URL = "https://www.googleapis.com/civicinfo/v2/elections?key=";
    public static final String VOTER_INFO_QUERY_URL = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=";


    public static String getElectionsUrl(Context context) {
        return ELECTIONS_URL + context.getResources().getString(R.string.API_KEY);
    }

    public static String getVoterInfoQueryUrl(Context context) {
        return VOTER_INFO_QUERY_URL + context.getResources().getString(R.string.API_KEY);
    }
}
