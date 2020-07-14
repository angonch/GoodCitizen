package com.example.goodcitizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.R;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick createAccount button");
//                String email = etEmail.getText().toString();
//                String username = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//                signUpUser(email, username, password);
                signUpUser("", "", "");
            }
        });
    }

    private void signUpUser(String email, String username, String password) {
        // Create the ParseUser
//        ParseUser user = new ParseUser();
//        // Set core properties
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setEmail(email);
//        // Invoke signUpInBackground
//        user.signUpInBackground(new SignUpCallback() {
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Issue with login ", e);
//                    Toast.makeText(SignUpActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                goMainActivity();
//            }
//        });
        goMainActivity();
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}