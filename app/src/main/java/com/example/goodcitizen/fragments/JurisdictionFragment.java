package com.example.goodcitizen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.JurisdictionModel;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class JurisdictionFragment extends Fragment {

    public static final String TAG = "JurisdictionFragment";
    JurisdictionModel jurisdictionInfo;

    // Views
    private TextView tvStateName;
    private TextView tvStateUrl;
    private TextView tvStateAddress;
    private TextView tvLocalName;
    private TextView tvLocalPhone;
    private TextView tvLocalEmail;
    private TextView tvLocalUrl;
    private TextView tvLocalAddress;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_jurisdiction, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("address", (String) ParseUser.getCurrentUser().get("address"));
        client.get(GoogleClient.getVoterInfoQueryUrl(getContext()),params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject result = jsonObject.getJSONArray("state").getJSONObject(0);
                    Log.i(TAG, "Result: " + result.toString());
                    jurisdictionInfo = (JurisdictionModel.fromJson(result));
                    Log.i(TAG, "Result: " + result.toString());
                    bindToViews(view);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure", throwable);
            }
        });
    }

    private void bindToViews(View view) {
        tvStateName = view.findViewById(R.id.tvStateName);
        tvStateUrl = view.findViewById(R.id.tvStateUrl);
        tvStateAddress = view.findViewById(R.id.tvStateAddress);
        tvLocalName = view.findViewById(R.id.tvLocalName);
        tvLocalPhone = view.findViewById(R.id.tvLocalPhone);
        tvLocalEmail = view.findViewById(R.id.tvLocalEmail);
        tvLocalUrl = view.findViewById(R.id.tvLocalUrl);
        tvLocalAddress = view.findViewById(R.id.tvLocalAddress);

        tvStateName.setText(jurisdictionInfo.getStateName());
        tvStateUrl.setText(jurisdictionInfo.getStateUrl());
        tvStateAddress.setText(jurisdictionInfo.getCorrespondenceAddress());
        tvLocalName.setText(jurisdictionInfo.getLocalName());
        tvLocalPhone.setText(jurisdictionInfo.getLocalPhone());
        tvLocalEmail.setText(jurisdictionInfo.getLocalEmail());
        tvLocalUrl.setText(jurisdictionInfo.getLocalUrl());
        tvLocalAddress.setText(jurisdictionInfo.getLocalAddress());
    }
}