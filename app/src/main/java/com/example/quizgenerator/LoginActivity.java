package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public TextView forgotPassword;
    public EditText emailEditText;
    public EditText passwordEditText;
    public Button signUpButton;
    public Button loginButton;
    public ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        signUpButton = findViewById(R.id.sign_up_button);
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        forgotPassword = findViewById(R.id.forgot_password);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //Handle Sign Up Button Clicks.
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                if (email.equals("")) {
                    emailEditText.setError("Enter Email Address");
                    emailEditText.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid Email Address");
                    emailEditText.requestFocus();
                } else if (password.equals("")) {
                    passwordEditText.setError("Enter a password");
                    passwordEditText.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    //Sign In Using Email and Password.
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        insertDefaultUserProfileDataInFirebaseDatabase();
                                        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                        intent.putExtra("signIn", true);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        passwordEditText.setError("Incorrect Credentials");
                                        passwordEditText.requestFocus();
                                    }
                                }
                            });
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            System.out.println(mAuth.getCurrentUser().getEmail());
            finish();
        }
    }

    private void insertDefaultUserProfileDataInFirebaseDatabase() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference anotherReference = database.getReference()
                .child("users").child(user.getUid()).child("hosted");
        final DatabaseReference reference = database.getReference()
                .child("users").child(user.getUid()).child("quiz");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                checkAndAddData(reference, dataSnapshot, "test", 0);
//                checkAndAddData(anotherReference, dataSnapshot, "test", 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("WTF", databaseError.toString());
                Toast.makeText(LoginActivity.this, "Firebase Database Error Occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private <T> void checkAndAddData(DatabaseReference reference, DataSnapshot dataSnapshot, String key, T value) {
        if (!dataSnapshot.child(key).exists()) {
            reference.child(key).setValue(value);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}