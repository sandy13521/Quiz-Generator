package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    public EditText emailEditText;
    public EditText passwordEditText;
    public EditText confirmPasswordEditText;
    public Button signUpButton;
    public ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();

        //Handling Sign Up Button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirmPassword = confirmPasswordEditText.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid Email Address");
                    emailEditText.requestFocus();
                } else if (!confirmPassword.equals(password)) {
                    confirmPasswordEditText.setError("Passwords do not match");
                    confirmPasswordEditText.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size() != 0) {
                                        emailEditText.setError("Email Address already Registered.");
                                        emailEditText.requestFocus();
                                    } else {
                                        //Create new user using Email and Password.
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            assert user != null;
                                                            progressBar.setVisibility(View.GONE);
                                                            //Go Back to Dashboard Page.
                                                            Intent intent = new Intent(SignUpActivity.this, Dashboard.class);
                                                            intent.putExtra("registration", true);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(SignUpActivity.this, "Password should have 8 characters", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

    }
}
