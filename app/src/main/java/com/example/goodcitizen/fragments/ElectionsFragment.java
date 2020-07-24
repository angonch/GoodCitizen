package com.example.goodcitizen.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.adapters.ElectionAdapter;
import com.example.goodcitizen.models.ElectionModel;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Headers;

public class ElectionsFragment extends Fragment {

    public static final String TAG = "ElectionsFragment";
    List<ElectionModel> elections;
    List<ElectionModel> allElections;
    List<ElectionModel> filteredElections;
    ElectionAdapter adapter;

    Spinner spFilter;
    String[] filterOptions = {"Show All", "National", "Related to Me"};

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_elections, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvElections = view.findViewById(R.id.rvElections);

        elections = new ArrayList<>();
        allElections = new ArrayList<>();
        filteredElections = new ArrayList<>();

        // create adapter
        adapter = new ElectionAdapter(getContext(), elections);
        // set adapter on recycler view
        rvElections.setAdapter(adapter);
        // set layout manager on recycler view
        rvElections.setLayoutManager(new LinearLayoutManager(getContext()));
        // set Item Touch Helper to RV - for swiping
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvElections);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GoogleClient.getElectionsUrl(getContext()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("elections");
                    Log.i(TAG, "Results: " + results.toString());
                    allElections.addAll(ElectionModel.fromJsonArray(results));
                    showAllElections();
                    Log.i(TAG, "Elections: " + elections.size());
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
                        showAllElections();
                        break;
                    case 1:
                        filterByNational();
                        break;
                    case 2:
                    default:
                        filterByUserRelated();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterByUserRelated() {
        filteredElections = ElectionModel.filterRelatedToUser(allElections, (String)ParseUser.getCurrentUser().get("divisionId"));
        elections.clear();
        elections.addAll(filteredElections);
        adapter.notifyDataSetChanged();
    }

    private void filterByNational() {
        filteredElections = ElectionModel.filterNational(allElections);
        elections.clear();
        elections.addAll(filteredElections);
        adapter.notifyDataSetChanged();
    }

    private void showAllElections() {
        elections.clear();
        elections.addAll(allElections);
        adapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // get election at item position
            int position = viewHolder.getAdapterPosition();
            ElectionModel election = elections.get(position);
            List<Integer> date = election.getDateArray();

            // new calendar event for date, set for midnight
            Calendar c = Calendar.getInstance();
            c.set(date.get(0), date.get(1) - 1, date.get(2));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);

            // start calendar event
            Intent i = new Intent(Intent.ACTION_EDIT);
            i.setType("vnd.android.cursor.item/event");
            i.putExtra(CalendarContract.Events.TITLE, election.getElectionName());
            i.putExtra(CalendarContract.Events.DESCRIPTION, "Don't Forget to Vote!");
            i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, c.getTimeInMillis());
            i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, c.getTimeInMillis());
            i.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
            startActivity(i);

            adapter.notifyItemChanged(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_baseline_notifications_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
        }
    };
}