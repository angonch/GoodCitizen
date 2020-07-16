package com.example.goodcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.goodcitizen.models.JurisdictionModel.addressFromJson;

// locations where voter is eligible to drop off a completed ballot
public class LocationModel {

    private String locationName;
    private String address;
    private String pollingHours;
    private String voterService;

    public static LocationModel fromJson(JSONObject jsonObject) throws JSONException {
        LocationModel location = new LocationModel();
        location.locationName = jsonObject.getJSONObject("address").getString("locationName");
        location.address = addressFromJson(jsonObject.getJSONObject("address"));
        location.pollingHours = jsonObject.getString("pollingHours");
        location.voterService = jsonObject.getString("voterServices");
        return location;
    }

    public static List<LocationModel> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<LocationModel> locations = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            locations.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return locations;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getAddress() {
        return address;
    }

    public String getPollingHours() {
        return pollingHours;
    }

    public String getVoterService() {
        return voterService;
    }
}
