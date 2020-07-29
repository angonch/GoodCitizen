package com.example.goodcitizen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.adapters.RepresentativeAdapter;
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
    List<RepresentativeModel> allRepresentatives;
    List<RepresentativeModel> filteredRepresentatives;
    RepresentativeAdapter adapter;

    Spinner spFilter;
    String[] filterOptions = {"Show All", "National", "State", "County", "City"};

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
        RecyclerView rvRepresentatives = view.findViewById(R.id.rvRepresentatives);

        representatives = new ArrayList<>();
        allRepresentatives = new ArrayList<>();
        filteredRepresentatives = new ArrayList<>();

        // create adapter
        adapter = new RepresentativeAdapter(getContext(), representatives);
        // set adapter on recycler view
        rvRepresentatives.setAdapter(adapter);
        // set layout manager on recycler view
        rvRepresentatives.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // api call to get representatives
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
                    allRepresentatives.addAll(RepresentativeModel.fromJsonArray(resultsOffices, resultsOfficials));
                    showAllRepresentatives();
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

        // create sorting menu
        spFilter = view.findViewById(R.id.spFilter);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, filterOptions);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spAdapter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        showAllRepresentatives();
                        break;
                    case 1:
                        filterByNational();
                        break;
                    case 2:
                        filterByState();
                        break;
                    case 3:
                        filterByCounty();
                        break;
                    case 4:
                    default:
                        filterByCity();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterByCity() {
        filteredRepresentatives = RepresentativeModel.filterByPlace(allRepresentatives);
        representatives.clear();
        representatives.addAll(filteredRepresentatives);
        adapter.notifyDataSetChanged();
    }

    private void filterByCounty() {
        // users state + county key word to filter
        filteredRepresentatives = RepresentativeModel.filterByCounty(allRepresentatives,
                (String)ParseUser.getCurrentUser().get("divisionId") + "/county:");
        representatives.clear();
        representatives.addAll(filteredRepresentatives);
        adapter.notifyDataSetChanged();
    }


    private void filterByState() {
        filteredRepresentatives = RepresentativeModel.filter(allRepresentatives,
                (String)ParseUser.getCurrentUser().get("divisionId")); //parse user stores state ID
        representatives.clear();
        representatives.addAll(filteredRepresentatives);
        adapter.notifyDataSetChanged();
    }

    private void filterByNational() {
        filteredRepresentatives = RepresentativeModel.filter(allRepresentatives, RepresentativeModel.NATIONAL_DIVISION_ID);
        representatives.clear();
        representatives.addAll(filteredRepresentatives);
        adapter.notifyDataSetChanged();
    }

    private void showAllRepresentatives() {
        representatives.clear();
        representatives.addAll(allRepresentatives);
        adapter.notifyDataSetChanged();
    }
}