package com.example.quizgenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Guidance extends AppCompatActivity {

    public Button btn;
    public int CAMERA_PERMISSION_CODE = 101;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        btn = findViewById(R.id.getgoing);
    }

    public void getGoing(View v) {
        intent = new Intent(this, Name_Quiz.class);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                Guidance.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat
                    .requestPermissions(
                            Guidance.this,
                            new String[]{permission},
                            requestCode);
        } else {
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                Toast.makeText(Guidance.this,
                        "Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}