package com.example.goodcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepresentativeModel {

    private String position;
    private String divisionId;
    private String officialName;
    private String party;
    private String photoUrl;
    //Stretch goal: private String websiteUrl;
    //Stretch goal variables:
    //channels variable
    //emails variable
    //roles variable

    // returns list of models representing officials and their positions
    public static List<RepresentativeModel> fromJson(JSONObject jsonObject, JSONArray officialsJsonArray) throws JSONException{
        // for each office position, create a Representative model for each
        // official holding that office position and add to the list
        List<RepresentativeModel> representatives = new ArrayList<>();
        List<Integer> indices = indicesFromJsonArray(jsonObject.getJSONArray("officialIndices"));
        for (Integer i : indices) {
            RepresentativeModel representative = new RepresentativeModel();
            representative.position = jsonObject.getString("name");
            representative.divisionId = jsonObject.getString("divisionId");
            representative.officialName = officialsJsonArray.getJSONObject(i).getString("name");
            representative.party = officialsJsonArray.getJSONObject(i).getString("party");
            try {
                representative.photoUrl = officialsJsonArray.getJSONObject(i).getString("photoUrl");
            } catch (Exception e) {
                representative.photoUrl = "";
            }
            representatives.add(representative);
        }
        return representatives;
    }

    private static List<Integer> indicesFromJsonArray(JSONArray officialIndices) throws JSONException {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < officialIndices.length(); i++) {
            indices.add((Integer) officialIndices.get(i));
        }
        return indices;
    }

    // Passed in: office jsonArray and officials jsonArray
    // Loop will go through office jsonArray and match offices to officials in the officials jsonArray
    // by indexes stored
    public static List<RepresentativeModel> fromJsonArray(JSONArray officesJsonArray, JSONArray officialsJsonArray) throws JSONException {
        List<RepresentativeModel> representatives = new ArrayList<>();
        for(int i = 0; i < officesJsonArray.length(); i++) {
            representatives.addAll(fromJson(officesJsonArray.getJSONObject(i), officialsJsonArray));
        }
        return representatives;
    }

    public String getPosition() {
        return position;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getParty() {
        return party;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
//offices (jsonArray)
//  name
//  divisionId
//  officialIndices(int list)

//officials
//  name
//  party
//  urls(string array)
//  photoUrl
//  emails(string array)

