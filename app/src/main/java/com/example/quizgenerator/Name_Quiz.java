package com.example.quizgenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class Name_Quiz extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 123;
    public CharSequence quizName;
    public Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name__quiz);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        TextView name = findViewById(R.id.quiz_name);
        quizName = name.getText();
    }

    public void openCamera(View v) {
        if (quizName.length() != 0) {
            Intent intent = new Intent(this, Crop.class);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Enter the quiz Name", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
////                Bundle b = data.getExtras();
////                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                picUri = data.getData();
//                Intent intent = new Intent(this, Crop.class);
//                intent.putExtra("data", picUri);
//                startActivity(intent);
//            }
//        }
//    }


}