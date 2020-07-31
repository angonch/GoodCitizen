package com.example.goodcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class RepresentativeModel {

    public static final String NATIONAL_DIVISION_ID = "ocd-division/country:us";

    private String position;
    private String divisionId;
    private String officialName;
    private String party;
    private String photoUrl;
    private Map<String, String> channels;
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
            try {
                JSONArray channelsArray = officialsJsonArray.getJSONObject(i).getJSONArray("channels");
                representative.channels = new HashMap<>();
                representative.channels.putAll(channelsFromJsonArray(channelsArray));
            } catch (Exception e) {
                representative.channels = null;
            }
            representatives.add(representative);
        }
        return representatives;
    }

    private static Map<String, String> channelsFromJsonArray(JSONArray channelsArray) throws JSONException {
        Map<String, String> channels = new HashMap<>();
        for (int i = 0; i < channelsArray.length(); i++) {
            channels.put(channelsArray.getJSONObject(i).getString("type"), channelsArray.getJSONObject(i).getString("id"));
        }
        return channels;
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

    public static List<RepresentativeModel> filter(List<RepresentativeModel> allRepresentatives, String idToFilterBy) {
        List<RepresentativeModel> filtered = new ArrayList<>();
        for(RepresentativeModel r : allRepresentatives) {
            if(r.divisionId.equals(idToFilterBy)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public static List<RepresentativeModel> filterByCounty(List<RepresentativeModel> allRepresentatives, String idToFilterBy) {
        List<RepresentativeModel> filtered = new ArrayList<>();
        for(RepresentativeModel r : allRepresentatives) {
            if(r.divisionId.contains(idToFilterBy) && !r.divisionId.contains("place")) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public static List<RepresentativeModel> filterByPlace(List<RepresentativeModel> allRepresentatives) {
        List<RepresentativeModel> filtered = new ArrayList<>();
        for(RepresentativeModel r : allRepresentatives) {
            if(r.divisionId.contains("place")) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public String getPosition() { return position; }

    public String getOfficialName() { return officialName; }

    public String getParty() { return party; }

    public String getPhotoUrl() { return photoUrl; }

    public String getFacebookUrl() {
        return "https://www.facebook.com/" + channels.get("Facebook") + "/";
    }

    public String getTwitterUrl() {
        return "https://www.twitter.com/" + channels.get("Twitter") + "/";
    }

    public String getYoutubeUrl() {
        return "https://www.youtube.com/" + channels.get("YouTube") + "/";
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

