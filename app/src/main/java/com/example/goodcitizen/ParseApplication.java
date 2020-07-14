package com.example.goodcitizen;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId and server based on values in Heroku settings
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APP_ID))
                .clientKey(null)
                .server(getString(R.string.SERVER_URL)).build());
    }
}