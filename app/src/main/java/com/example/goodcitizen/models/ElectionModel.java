package com.example.goodcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class ElectionModel {

    private String electionName;
    private String date;
    private String divisionId;

    public static ElectionModel fromJson(JSONObject jsonObject) throws JSONException {
        ElectionModel election = new ElectionModel();
        election.electionName = jsonObject.getString("name");
        election.date = jsonObject.getString("electionDay");
        election.divisionId = jsonObject.getString("ocdDivisionId");
        return election;
    }

    public static List<ElectionModel> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<ElectionModel> elections = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            elections.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return elections;
    }

    public String getElectionName() {
        return electionName;
    }

    public String getDate() {
        return date;
    }

    public String getDivisionId() {
        return divisionId;
    }

    // integer array of date - {YYYY,MM,DD}
    public List<Integer> getDateArray() {
        String[] dateArray = date.split("-");
        List<Integer> arr = new ArrayList<>();
        for(String s : dateArray) {
            arr.add(Integer.parseInt(s));
        }
        return arr;
    }
}
