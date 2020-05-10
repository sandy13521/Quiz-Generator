package com.example.quizgenerator;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class Crop extends AppCompatActivity {

    public Uri mCropImageUri;
    public ImageView img;

    public FirebaseVisionImage fimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        img = findViewById(R.id.img);
        Uri imageUri = getIntent().getData();
        startCropImageActivity(imageUri);
        try {
            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            fimage = FirebaseVisionImage.fromBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null) {
                Uri x = result.getUri();
                try {
                    if (x != null) {
                        img.setImageURI(x);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void generateQuiz(final View v) {

        final Intent intent = new Intent(this, Question.class);
        try {
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(fimage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    // Task completed successfully
                                    // ...
                                    int count = 0;
                                    ArrayList<String> options = new ArrayList<>();
                                    for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                        String text = block.getText();
                                        if (count == 0) {
                                            intent.putExtra("question", text);
                                            count++;
                                        } else {
                                            options.add(text);
                                        }
                                    }
                                    intent.putExtra("options", options);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...

                                            e.printStackTrace();

                                        }
                                    });
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }


    }
}
