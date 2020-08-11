package com.example.goodcitizen.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.LocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MapsFragment extends Fragment {

    public static final String TAG = "MapsFragment";
    List<LocationModel> locations;
    LatLng currentLocation;

    ProgressBar progressBar;

    CardView cvMapNotif;

    View mapView;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // if a bundle not is passed in,query locations, otherwise show the single address
            Bundle bundle = getArguments();
            if(bundle == null || bundle.isEmpty()) {
                queryLocations(googleMap);
            } else {
                String address = bundle.getString("address");
                setMarker(address, googleMap);
                disallowParentScroll(googleMap);
            }
        }
    };

    private void disallowParentScroll(GoogleMap googleMap) {
        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                mapView = getView().findViewById(R.id.map);
                mapView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mapView = getView().findViewById(R.id.map);
                mapView.getParent().requestDisallowInterceptTouchEvent(false);
            }
        });
    }

    private void setMarker(String address, final GoogleMap googleMap) {
        LatLng latLng = getLatLngLocation(address);
        // set location marker
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        // finish loading animation
        progressBar.setVisibility(View.GONE);
    }

    private void queryLocations(final GoogleMap googleMap) {
        locations = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("address", (String) ParseUser.getCurrentUser().get("address"));
        client.get(GoogleClient.getVoterInfoQueryUrl(getContext()),params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("dropOffLocations");
                    Log.i(TAG, "Results: " + results.toString());
                    locations.addAll(LocationModel.fromJsonArray(results)); // Modify locations list
                    Log.i(TAG, "Locations: " + locations.size());
                    addMarkers(googleMap);
                } catch (JSONException | IOException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure", throwable);
                cvMapNotif.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addMarkers(final GoogleMap googleMap) throws IOException {
        currentLocation = getLatLngLocation((String)ParseUser.getCurrentUser().get("address")); // get lat/long point for user's address
        if(currentLocation != null) {
            // set user marker
            googleMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .title("Me")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            //change the view to the user location with a view of 15
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        }

        int i = 0;
        // for first 30 locations, add marker
        for(LocationModel l : locations){
            if(i == 30) {
                break;
            }
            LatLng latLng = getLatLngLocation(l.getAddress());
            // set location marker
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(l.getLocationName())
                    .snippet(l.getPollingHours()));
            i++;
        }

        // finish loading animation
        progressBar.setVisibility(View.GONE);

        // info window description
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    // converts user String address to latitude and longitude point
    private LatLng getLatLngLocation(String address) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> addressResults;

        try {
            addressResults = coder.getFromLocationName(address,5);
            if (addressResults!=null) {

                Address location = addressResults.get(0);
                Log.i(TAG, "GeoCode: " + location.getLatitude() + ", " + location.getLongitude());

                location.getLatitude();
                location.getLongitude();

                return new LatLng(location.getLatitude(),
                        location.getLongitude());

            }
        } catch (IOException e) {
            Log.e(TAG, "Hit geocode exception", e);
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        progressBar = view.findViewById(R.id.progressBar);
        cvMapNotif = view.findViewById(R.id.cvMapNotif);
    }
}