package com.example.quizgenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

public class question_captured extends AppCompatActivity {
    public Button mTextButton;
    public ImageView mSelectedImage;
    public Bitmap b;

    public FirebaseVisionImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_captured);

        mTextButton = findViewById(R.id.fire);
        mSelectedImage = findViewById(R.id.captured_question);
        try {
            Uri uri = getIntent().getData();
            Bundle bundle = getIntent().getExtras();
//            b = bundle.get();
            //b = (Bitmap) extras.get("data");

            image = FirebaseVisionImage.fromBitmap(b);
            mSelectedImage.setImageBitmap(b);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Error Loading Image", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void generateQuiz(final View v) {

        final Intent intent = new Intent(this, Question.class);
        try {
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(image)
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
