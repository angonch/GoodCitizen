package com.example.goodcitizen.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodcitizen.ImageUtils;
import com.example.goodcitizen.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private ImageView ivProfilePic;

    private File resizedFile;

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
        ivProfilePic = findViewById(R.id.ivProfilePic);

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.launchCamera(SignUpActivity.this);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick createAccount button");
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String address = getAddress(etAddressLine1, etAddressLine2, etAddressLine3, etAddressCity, etAddressState, etAddressZip);
                signUpUser(email, username, password, address, resizedFile);
            }
        });
    }

    static String getAddress(EditText etAddressLine1, EditText etAddressLine2, EditText etAddressLine3, EditText etAddressCity, EditText etAddressState, EditText etAddressZip) {
        return etAddressLine1.getText().toString() + "," + etAddressLine2.getText().toString() + "," + etAddressLine3.getText().toString() + "," +
                etAddressCity.getText().toString() + "," + etAddressState.getText().toString() + "," + etAddressZip.getText().toString();
    }

    private void signUpUser(String email, String username, String password, String address, final File photo) {
        // Create the ParseUser
        final ParseUser user = new ParseUser();
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
                    saveProfilePhoto(user, photo);
                    goMainActivity();
                }
            }
        });
    }

    private void saveProfilePhoto(ParseUser user, File photo) {
        if (resizedFile != null) {
            user.put("profileImage", new ParseFile(photo));
        }
        // Invoke signUpInBackground
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with saving account changes ", e);
                    Toast.makeText(SignUpActivity.this, "Issue with saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(ImageUtils.photoFile.getAbsolutePath());
                // Resize the bitmap to 150x100 (width x height)
                Bitmap resizedBitmap = ImageUtils.scaleToFitWidth(takenImage, 500);
                // Load the taken image into a preview
                ivProfilePic.setImageBitmap(resizedBitmap);

                // write that smaller bitmap back to disk
                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                resizedFile = ImageUtils.getPhotoFileUri(ImageUtils.photoFileName + "_resized", SignUpActivity.this);
                try {
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    // Write the bytes of the bitmap to file
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // Result was a failure
                Toast.makeText(getApplicationContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}