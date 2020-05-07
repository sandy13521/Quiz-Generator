package com.example.quizgenerator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class question_captured extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_captured);

        ImageView question_captured = findViewById(R.id.captured_question);
        try {
            Bundle extras = getIntent().getExtras().getBundle("data");
            Bitmap b = (Bitmap) extras.get("data");
            question_captured.setImageBitmap(b);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Error Loading Image", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
