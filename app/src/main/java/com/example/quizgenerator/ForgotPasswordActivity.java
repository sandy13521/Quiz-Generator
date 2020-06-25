package com.example.quizgenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    public EditText emailId;
    public Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailId = findViewById(R.id.email_edit_text);
        resetPassword = findViewById(R.id.reset_password);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAddress = emailId.getText().toString();
                if (emailAddress.matches("")) {
                    emailId.setError("Enter Email Address");
                    emailId.requestFocus();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder builder
                                                = new AlertDialog
                                                .Builder(ForgotPasswordActivity.this);
                                        builder
                                                .setTitle("Reset Password")
                                                .setMessage("Password reset mail is sent to the mentioned Email Id.")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        Log.d("Reset password", "Email sent.");
                                                        finish();
                                                    }
                                                }).create().show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
