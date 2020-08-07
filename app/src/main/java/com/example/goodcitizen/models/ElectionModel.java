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
public class ElectionModel {

    public static final String NATIONAL_DIVISION_ID = "ocd-division/country:us";

    private String electionName;
    private String date;
    private String divisionId;

    // states for converting between abbreviation and full name
    private static Map<String, String> states;

    static
    {
        states = new HashMap<>();
        states.put("AL","Alabama");
        states.put("AK","Alaska");
        states.put("AZ","Arizona");
        states.put("AR","Arkansas");
        states.put("CA","California");
        states.put("CO","Colorado");
        states.put("CT","Connecticut");
        states.put("DE","Delaware");
        states.put("DC","District Of Columbia");
        states.put("FL","Florida");
        states.put("GA","Georgia");
        states.put("HI","Hawaii");
        states.put("ID","Idaho");
        states.put("IL","Illinois");
        states.put("IN","Indiana");
        states.put("IA","Iowa");
        states.put("KS","Kansas");
        states.put("KY","Kentucky");
        states.put("LA","Louisiana");
        states.put("ME","Maine");
        states.put("MD","Maryland");
        states.put("MA","Massachusetts");
        states.put("MI","Michigan");
        states.put("MN","Minnesota");
        states.put("MS","Mississippi");
        states.put("MO","Missouri");
        states.put("MT","Montana");
        states.put("NE","Nebraska");
        states.put("NV","Nevada");
        states.put("NH","New Hampshire");
        states.put("NJ","New Jersey");
        states.put("NM","New Mexico");
        states.put("NY","New York");
        states.put("NC","North Carolina");
        states.put("ND","North Dakota");
        states.put("OH","Ohio");
        states.put("OK","Oklahoma");
        states.put("OR","Oregon");
        states.put("PA","Pennsylvania");
        states.put("RI","Rhode Island");
        states.put("SC","South Carolina");
        states.put("SD","South Dakota");
        states.put("TN","Tennessee");
        states.put("TX","Texas");
        states.put("UT","Utah");
        states.put("VT","Vermont");
        states.put("VA","Virginia");
        states.put("WA","Washington");
        states.put("WV","West Virginia");
        states.put("WI","Wisconsin");
        states.put("WY","Wyoming");
    }

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

    public static List<ElectionModel> filterNational(List<ElectionModel> allElections) {
        List<ElectionModel> filtered = new ArrayList<>();
        for(ElectionModel e : allElections) {
            if(e.divisionId.equals(NATIONAL_DIVISION_ID)) {
                filtered.add(e);
            }
        }
        return filtered;
    }

    public static List<ElectionModel> filterRelatedToUser(List<ElectionModel> allElections, String idToFilterBy) {
        List<ElectionModel> filtered = new ArrayList<>();
        for(ElectionModel e : allElections) {
            if(e.divisionId.equals(NATIONAL_DIVISION_ID) || e.divisionId.equals(idToFilterBy)) {
                filtered.add(e);
            }
        }
        return filtered;
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

    public String getState() {
        String[] divisionIdSplit = divisionId.split("/");
        if(divisionIdSplit.length == 2) {
            return null;
        }
        String[] state = divisionIdSplit[2].split(":");
        return states.get(state[1].toUpperCase()); //ex: state:wa returns washington
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
