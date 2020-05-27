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

    //Declaring UI Elements
    public ImageView img;

    //Declaring variables
    public Uri mCropImageUri;
    public FirebaseVisionImage fimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        img = findViewById(R.id.img);
        Uri imageUri = getIntent().getData();
        startCropImageActivity(imageUri);
    }

    //Cropping API call
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
                        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        fimage = FirebaseVisionImage.fromBitmap(bitmap);
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

    //Passing the captured image to Firebase ML kit API to recognize the text in the image.
    public void generateQuiz(final View v) {
        final Intent intent = new Intent(this, Question.class);
        Bundle bundle = getIntent().getExtras();
        intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));

        try {
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(fimage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    int count = 0;
                                    ArrayList<String> options = new ArrayList<>();
                                    // for multiple line options.
                                    if (firebaseVisionText.getTextBlocks().size() <= 2) {
                                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                            String text = block.getText();
                                            if (count == 0) {
                                                intent.putExtra("question", text);
                                                count++;
                                            } else {
                                                for (FirebaseVisionText.Line line : block.getLines())
                                                    options.add(line.getText());
                                                break;
                                            }
                                        }
                                    }
                                    // for single line options.
                                    else if (firebaseVisionText.getTextBlocks().size() >= 4) {
                                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                            String text = block.getText();
                                            if (count == 0) {
                                                intent.putExtra("question", text);
                                                count++;
                                            } else {
                                                options.add(block.getText());
                                            }
                                        }
                                    } else {
                                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                            String text = block.getText();
                                            if (count == 0) {
                                                intent.putExtra("question", text);
                                                count++;
                                            } else {
                                                for (FirebaseVisionText.Line line : block.getLines()) {
                                                    for (FirebaseVisionText.Element elem : line.getElements()) {
                                                        options.add(elem.getText());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    intent.putExtra("options", options);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();

                                        }
                                    });
        } catch (Exception e) {
            Toast.makeText(this, "Firebase ML Kit Fucked Up!!", Toast.LENGTH_SHORT).show();
        }
    }
}
