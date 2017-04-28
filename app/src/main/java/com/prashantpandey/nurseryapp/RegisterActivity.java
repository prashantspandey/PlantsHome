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

public class RegisterActivity extends AppCompatActivity {
    private EditText emailView, passwordView, confirmPasswordView;
    private Button registerButton;
    private FirebaseAuth registerAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        registerAuth = FirebaseAuth.getInstance();
        emailView = (EditText) findViewById(R.id.activity_register_emailText);
        passwordView = (EditText) findViewById(R.id.activity_register_passwordText);
        confirmPasswordView = (EditText) findViewById(R.id.activity_register_confirmpasswordText);
        registerButton = (Button) findViewById(R.id.activity_register_registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String confirmPassword = confirmPasswordView.getText().toString().trim();
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        if (email.equals("")||password.equals("")||confirmPassword.equals("")){
            Toast.makeText(RegisterActivity.this,"Please fill all the fields.",Toast.LENGTH_SHORT).show();
        }else if (!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this,"Your passwords do not match. Please fill in the same passwords",Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            registerAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                   Toast.makeText(RegisterActivity.this,"Welcome to the family!! We appretiate your efforts for making the world green.",Toast.LENGTH_LONG).show();
                    Intent maIntent = new Intent(RegisterActivity.this,MainActivity.class);
                    maIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    progressDialog.dismiss();
                    startActivity(maIntent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"Sorry there was some problem with registeration.Please try again.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
