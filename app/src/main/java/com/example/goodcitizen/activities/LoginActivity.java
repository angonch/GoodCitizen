package com.example.goodcitizen.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnForgotPass;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null) {
            Log.i(TAG, "Success logging in " + ParseUser.getCurrentUser().getUsername() + ", " + ParseUser.getCurrentUser().get("address"));
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        progressBar = findViewById(R.id.progressBar);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                progressBar.setVisibility(View.VISIBLE);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSignUpActivity();
            }
        });

        btnForgotPass = findViewById(R.id.btnForgotPass);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordResetDialog();
            }
        });
    }

    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Reset");

        View viewInflated = this.getLayoutInflater().inflate(R.layout.dialog_password_reset, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText etEmail = viewInflated.findViewById(R.id.etEmail);

        builder.setView(viewInflated);

        // Set up the buttons
        // TODO: doesnt work
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String inputEmail = etEmail.getText().toString();
                if(!inputEmail.isEmpty()) {
                    ParseUser.requestPasswordResetInBackground(inputEmail,
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        // An email was successfully sent with reset instructions.
                                        Toast.makeText(LoginActivity.this, "Password reset e-mail sent", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else {
                                        // Something went wrong. Look at the ParseException to see what's up.
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this, "Password reset e-mail failed to send", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Email Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void goSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        // navigate to the main activity if user has signed in properly
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: better error handling
                    Log.e(TAG, "Issue with login ", e);
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    // go to main activity
                    Log.i(TAG, "Success logging in " + ParseUser.getCurrentUser().getUsername());
                    progressBar.setVisibility(View.GONE);
                    goMainActivity();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}