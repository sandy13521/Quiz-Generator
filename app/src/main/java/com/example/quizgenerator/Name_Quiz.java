package com.example.quizgenerator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Name_Quiz extends AppCompatActivity {
    public CharSequence quizName;
    private static final int CAMERA_REQUEST = 123;
    final int PIC_CROP = 2;
    Button btn;
    ImageView imageView;
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name__quiz);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        TextView name = findViewById(R.id.quiz_name);
        quizName = name.getText();
    }

    public void getGoing(View v) {
        if (quizName.length() != 0) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } catch (ActivityNotFoundException e) {
                String errorMsg = "Does Not Support Capturing Images";
                Toast toast = Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            String errorMsg = "Enter The Quiz Name";
            Toast toast = Toast.makeText(this, errorMsg, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                picUri = data.getData();
                performCrop();
            }
        }
    }

    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException e) {
            String errmsg = "Does Not Support Cropping Images";
            Toast toast = Toast.makeText(this, errmsg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}


