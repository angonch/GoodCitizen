package com.example.goodcitizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etAddressLine1;
    private EditText etAddressLine2;
    private EditText etAddressLine3;
    private EditText etAddressCity;
    private EditText etAddressState;
    private EditText etAddressZip;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //TODO: error handling
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etAddressLine3 = findViewById(R.id.etAddressLine3);
        etAddressCity = findViewById(R.id.etAddressCity);
        etAddressState = findViewById(R.id.etAddressState);
        etAddressZip = findViewById(R.id.etAddressZip);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick createAccount button");
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String address = getAddress(etAddressLine1, etAddressLine2, etAddressLine3, etAddressCity, etAddressState, etAddressZip);
                signUpUser(email, username, password, address);
            }
        });
    }

    private String getAddress(EditText etAddressLine1, EditText etAddressLine2, EditText etAddressLine3, EditText etAddressCity, EditText etAddressState, EditText etAddressZip) {
        String address = etAddressLine1.getText().toString() + ",";
        if(!etAddressLine2.getText().toString().isEmpty()) {
            address += etAddressLine2.getText().toString() + ",";
        }
        if(!etAddressLine3.getText().toString().isEmpty()) {
            address += etAddressLine3.getText().toString() + ",";
        }
        address += etAddressCity.getText().toString() + " " + etAddressState.getText().toString() + "," + etAddressZip.getText().toString();
        return address;
    }

    private void signUpUser(String email, String username, String password, String address) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("address", address);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login ", e);
                    Toast.makeText(SignUpActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    goMainActivity();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}