package com.example.goodcitizen.fragments;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.JurisdictionModel;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

import static androidx.core.content.ContextCompat.getSystemService;

public class JurisdictionFragment extends Fragment {

    public static final String TAG = "JurisdictionFragment";
    JurisdictionModel jurisdictionInfo;

    // Views
    private TextView tvStateName;
    private TextView tvStateUrl;
    private TextView tvStateAddress;
    private TextView tvLocalName;
    private TextView tvLocalPhone;
    private TextView tvLocalEmail;
    private TextView tvLocalUrl;
    private TextView tvLocalAddress;
    private ImageView ivStateFlag;
    private CardView cvStateSite;
    private CardView cvStateAddress;
    private CardView cvLocalPhone;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_jurisdiction, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);

        tvStateName = view.findViewById(R.id.tvStateName);
        tvStateUrl = view.findViewById(R.id.tvStateUrl);
        tvStateAddress = view.findViewById(R.id.tvStateAddress);
        tvLocalName = view.findViewById(R.id.tvLocalName);
        tvLocalPhone = view.findViewById(R.id.tvLocalPhone);
        tvLocalEmail = view.findViewById(R.id.tvLocalEmail);
        tvLocalUrl = view.findViewById(R.id.tvLocalUrl);
        tvLocalAddress = view.findViewById(R.id.tvLocalAddress);
        ivStateFlag = view.findViewById(R.id.ivStateFlag);
        cvStateSite = view.findViewById(R.id.cvStateWebsite);
        cvStateAddress = view.findViewById(R.id.cvStateAddress);
        cvLocalPhone = view.findViewById(R.id.cvLocalPhone);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("address", (String) ParseUser.getCurrentUser().get("address"));
        client.get(GoogleClient.getVoterInfoQueryUrl(getContext()),params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject result = jsonObject.getJSONArray("state").getJSONObject(0);
                    Log.i(TAG, "Result: " + result.toString());
                    jurisdictionInfo = (JurisdictionModel.fromJson(result));
                    Log.i(TAG, "Result: " + result.toString());
                    bindToViews(view);
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
    }

    private void bindToViews(View view) {
        tvStateName.setText(jurisdictionInfo.getStateName());
        tvStateAddress.setText(jurisdictionInfo.getCorrespondenceAddress());
        tvLocalName.setText(jurisdictionInfo.getLocalName());
        tvLocalEmail.setText(jurisdictionInfo.getLocalEmail());
        tvLocalUrl.setText(jurisdictionInfo.getLocalUrl());

        // on click listeners for clickable information
        if(jurisdictionInfo.getCorrespondenceAddress() == null || jurisdictionInfo.getCorrespondenceAddress().isEmpty()) {
            cvStateAddress.setVisibility(View.GONE);
        } else {
            tvLocalAddress.setText(jurisdictionInfo.getLocalAddress());
            cvStateAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
                    ClipData clip = ClipData.newPlainText("State Correspondence Address", jurisdictionInfo.getCorrespondenceAddress());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Copied to Clipboard", Toast.LENGTH_LONG).show();
                }
            });
        }

        if(jurisdictionInfo.getStateUrl() == null || jurisdictionInfo.getStateUrl().isEmpty()) {
            cvStateSite.setVisibility(View.GONE);
        } else {
            tvStateUrl.setText(jurisdictionInfo.getStateUrl());
            cvStateSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(jurisdictionInfo.getStateUrl()));
                    startActivity(intent);
                }
            });
        }

        if(jurisdictionInfo.getLocalPhone() == null || jurisdictionInfo.getLocalPhone().isEmpty()) {
            cvLocalPhone.setVisibility(View.GONE);
        } else {
            tvLocalPhone.setText(jurisdictionInfo.getLocalPhone());
            cvLocalPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+jurisdictionInfo.getLocalPhone()));
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }
            });
        }

        try {
            String stateFlagUrl = GoogleClient.getStateFlagURL(jurisdictionInfo.getStateName());
            Glide.with(getContext()).load(stateFlagUrl).into(ivStateFlag);
        } catch (Exception e) {

        }
    }
}