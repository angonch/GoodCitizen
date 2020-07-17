package com.example.goodcitizen.models;

import org.json.JSONException;
import org.json.JSONObject;

// State and Local Election information for state where voter votes in
public class JurisdictionModel {

    private String stateName;
    private String stateUrl;
    private String correspondenceAddress;
    private String localName;
    private String localUrl;
    private String localPhone;
    private String localEmail;
    private String localAddress;

    // Gets state and local jurisdiction info from "state" jsonObject from voterInfoQuery
    public static JurisdictionModel fromJson(JSONObject jsonObject) throws JSONException {
        JurisdictionModel jurisdictionModel = new JurisdictionModel();
        jurisdictionModel.stateName = jsonObject.getString("name");
        jurisdictionModel.stateUrl = jsonObject.getJSONObject("electionAdministrationBody").getString("electionInfoUrl");
        jurisdictionModel.correspondenceAddress = addressFromJson(jsonObject.getJSONObject("electionAdministrationBody").getJSONObject("correspondenceAddress"));
        jurisdictionModel.localName = jsonObject.getJSONObject("local_jurisdiction").getString("name");
        jurisdictionModel.localUrl = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getString("electionInfoUrl");
        jurisdictionModel.localPhone = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONArray("electionOfficials").getJSONObject(0).getString("officePhoneNumber");
        jurisdictionModel.localEmail = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONArray("electionOfficials").getJSONObject(0).getString("emailAddress");
        jurisdictionModel.localAddress = addressFromJson(jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONObject("physicalAddress"));
        return jurisdictionModel;
    }

    static String addressFromJson(JSONObject jsonObject) throws JSONException{
        return jsonObject.getString("line1") + " " + jsonObject.getString("city") + " "
                + jsonObject.getString("state") + " " + jsonObject.getString("zip");
    }
}

//state
//  name :WA
//  body(jsonObject)
//      electionInfoURL
//      correspondenceAddress(jsonObject)
//  local (jsonObject)
//      name
//      body
//          electionInfoURL
//          physicalAddress(jsonObject)
//      officials (jsonObject)
//          phone
//          email

