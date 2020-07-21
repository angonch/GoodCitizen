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

import com.bumptech.glide.Glide;
import com.example.goodcitizen.ImageUtils;
import com.example.goodcitizen.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private ImageView ivProfilePic;

    private File resizedFile;

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
        ivProfilePic = findViewById(R.id.ivProfilePic);

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
                updateUser(user, username, address, resizedFile);
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

        // take profile photo
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.launchCamera(AccountActivity.this);
            }
        });
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
                resizedFile = ImageUtils.getPhotoFileUri(ImageUtils.photoFileName + "_resized", AccountActivity.this);
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


    private void setTextFields(ParseUser user) {
        etUsername.setText(user.getUsername());
        String[] address = ((String)user.get("address")).split(",");
        etAddressLine1.setText(address[0]);
        etAddressLine2.setText(address[1]);
        etAddressLine3.setText(address[2]);
        etAddressCity.setText(address[3]);
        etAddressState.setText(address[4]);
        etAddressZip.setText(address[5]);
        ParseFile image = user.getParseFile("profileImage");
        if(image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivProfilePic);
        }
    }

    private void updateUser(ParseUser user, String username, String address, File photo) {
        // Update core properties
        user.setUsername(username);
        user.put("address", address);
        if (resizedFile != null) {
            user.put("profileImage", new ParseFile(photo));
        }
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