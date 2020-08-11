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
        try {
            jurisdictionModel.correspondenceAddress = addressFromJson(jsonObject.getJSONObject("electionAdministrationBody").getJSONObject("correspondenceAddress"));
        } catch (Exception e) {
            jurisdictionModel.correspondenceAddress = "";
        }
        jurisdictionModel.localName = jsonObject.getJSONObject("local_jurisdiction").getString("name");
        jurisdictionModel.localUrl = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getString("electionInfoUrl");
        try {
            jurisdictionModel.localPhone = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONArray("electionOfficials").getJSONObject(0).getString("officePhoneNumber");
            jurisdictionModel.localEmail = jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONArray("electionOfficials").getJSONObject(0).getString("emailAddress");
        } catch (Exception e) {
            jurisdictionModel.localPhone = "";
            jurisdictionModel.localEmail = "";
        }
        jurisdictionModel.localAddress = addressFromJson(jsonObject.getJSONObject("local_jurisdiction").getJSONObject("electionAdministrationBody").getJSONObject("physicalAddress"));
        return jurisdictionModel;
    }

    static String addressFromJson(JSONObject jsonObject) throws JSONException{
        return jsonObject.getString("line1") + " " + jsonObject.getString("city") + " "
                + jsonObject.getString("state") + " " + jsonObject.getString("zip");
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateUrl() {
        return stateUrl;
    }

    public String getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    public String getLocalName() {
        return localName;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public String getLocalPhone() {
        return localPhone;
    }

    public String getLocalEmail() {
        return localEmail;
    }

    public String getLocalAddress() {
        return localAddress;
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

