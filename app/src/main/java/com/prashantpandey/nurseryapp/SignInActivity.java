package com.prashantpandey.nurseryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText emailView, passwordView;
    private Button signInButton;
    private FirebaseAuth signInAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        emailView = (EditText) findViewById(R.id.activity_signin_emailText);
        passwordView = (EditText) findViewById(R.id.activity_signin_passwordText);
        signInButton = (Button) findViewById(R.id.activity_signin_signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        progressDialog.setMessage("Signing In...");
        progressDialog.setCancelable(false);

        if (email.equals("")||password.equals("")){
            Toast.makeText(SignInActivity.this,"Please fill all the fields.",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            signInAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignInActivity.this,"Welcome Back! Hope you enjoy the greenery.",Toast.LENGTH_SHORT).show();
                    Intent signInIntent = new Intent(SignInActivity.this,MainActivity.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    progressDialog.dismiss();
                    startActivity(signInIntent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this,"Oops, Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
