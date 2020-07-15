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
import com.parse.SaveCallback;

public class AccountActivity extends AppCompatActivity {

    public static final String TAG = "AccountActivity";
    private Button btnLogout;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etAddressLine1;
    private EditText etAddressLine2;
    private EditText etAddressLine3;
    private EditText etAddressCity;
    private EditText etAddressState;
    private EditText etAddressZip;
    private Button btnSaveChanges;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toast.makeText(getApplicationContext(), "Edit Account page!", Toast.LENGTH_SHORT).show();

        // Get the ParseUser
        final ParseUser user = ParseUser.getCurrentUser();

        //TODO: error handling, only save what is changed
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etAddressLine3 = findViewById(R.id.etAddressLine3);
        etAddressCity = findViewById(R.id.etAddressCity);
        etAddressState = findViewById(R.id.etAddressState);
        etAddressZip = findViewById(R.id.etAddressZip);

        // fill in users information
        setTextFields(user);

        // TODO: Password reset
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                try {
                    user.requestPasswordReset(email);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error getting request to update password", e);
                }
            }
        });

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String address = SignUpActivity.getAddress(etAddressLine1, etAddressLine2, etAddressLine3, etAddressCity, etAddressState, etAddressZip);
                updateUser(user, username, address);
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                goLogoutActivity();
            }
        });
    }

    private void setTextFields(ParseUser user) {
        etUsername.setText(user.getUsername());
        String[] address = ((String)user.get("address")).split(",");
        etAddressLine1.setText(address[0]);
        etAddressLine2.setText(address[1]);
        etAddressLine3.setText(address[2]);
        etAddressCity.setText(address[3]);
        etAddressState.setText(address[4]);
        etAddressZip.setText(address[5]);
    }

    private void updateUser(ParseUser user, String username, String address) {
        // Update core properties
        user.setUsername(username);
        user.put("address", address);
        // Invoke signUpInBackground
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with saving account changes ", e);
                    Toast.makeText(AccountActivity.this, "Issue with saving!", Toast.LENGTH_SHORT).show();
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
    }

    private void goLogoutActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}