package com.example.goodcitizen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.RepresentativeModel;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class RepresentativesFragment extends Fragment {

    public static final String TAG = "RepresentativesFragment";
    List<RepresentativeModel> representatives;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_representatives, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView rvRepresentatives = view.findViewById(R.id.rvRepresentatives);
        representatives = new ArrayList<>();

//        // create adapter
//        final RepresentativeAdapter adapter = new RepresentativeAdapter(getContext(), representatives);
//        // set adapter on recycler view
//        rvRepresentatives.setAdapter(adapter);
//        // set layout manager on recycler view
//        rvRepresentatives.setLayoutManager(new LinearLayoutManager(getContext()));

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("address", (String) ParseUser.getCurrentUser().get("address"));
        client.get(GoogleClient.getRepresentativeInfoQueryUrl(getContext()),params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray resultsOffices = jsonObject.getJSONArray("offices");
                    JSONArray resultsOfficials = jsonObject.getJSONArray("officials");
                    Log.i(TAG, "Offices: " + resultsOffices.toString());
                    Log.i(TAG, "Officials: " + resultsOfficials.toString());
                    representatives.addAll(RepresentativeModel.fromJsonArray(resultsOffices, resultsOfficials));
                    //adapter.notifyDataSetChanged();
                    Log.i(TAG, "Representatives: " + representatives.size());
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
}