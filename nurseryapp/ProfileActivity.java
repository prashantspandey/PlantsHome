package com.prashantpandey.nurseryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameText,addressLine1Text,addressLine2Text,pincodeText,phoneNumberText;
    private Button updateProfileButton;
    private DatabaseReference userReference,userName,userAddress,userPhone;
    private FirebaseAuth mAuth;
    private String name;
    private String address2;
    private String pincode;
    private String phonenumber;
    private String address1;
    private String address;
    private String tot;
    private String backText;
    private String getname,getnumber,getaddress;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        tot = getIntent().getStringExtra("total");
        dialog = new ProgressDialog(this);
        userName = userReference.child("name");
        userAddress = userReference.child("address");
        userPhone = userReference.child("phoneNumber");
        backText = getIntent().getStringExtra("backtotal");
        nameText = (EditText) findViewById(R.id.activity_profile_nameText);
        addressLine1Text = (EditText) findViewById(R.id.activity_profile_addressLineOneText);
        addressLine2Text = (EditText) findViewById(R.id.activity_profile_addressLineTwoText);
        pincodeText = (EditText) findViewById(R.id.activity_profile_pincodeText);
        phoneNumberText = (EditText) findViewById(R.id.activity_profile_phoneNumberText);
        updateProfileButton = (Button) findViewById(R.id.activity_profile_updateDetailsButton);
        updateProfileButton.setOnClickListener(this);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    getname = dataSnapshot.child("name").getValue().toString();
                    getnumber = dataSnapshot.child("phoneNumber").getValue().toString();
                    nameText.setText(getname);
                    phoneNumberText.setText(getnumber);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id ==R.id.activity_profile_updateDetailsButton){

            name = nameText.getText().toString();
            address2 = addressLine2Text.getText().toString();
            pincode = pincodeText.getText().toString();
            phonenumber = phoneNumberText.getText().toString();
            address1 = addressLine1Text.getText().toString();
            address = address1 + "," + address2 + "," + pincode;
            if (!name.equals("") || !addressLine1Text.equals("") || !addressLine2Text.equals("")|| !pincode.equals("")|| !phonenumber.equals("")){
                if (pincode.length()==6 && phonenumber.length()==10) {
                    dialog.setMessage("Updating your details....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    userName.setValue(name);
                    userAddress.setValue(address);
                    userPhone.setValue(phonenumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (backText != null) {
                                Intent checkout = new Intent(ProfileActivity.this, CheckOutActivity.class);
                                checkout.putExtra("total", backText);
                                startActivity(checkout);
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                Intent ckIntent = new Intent(ProfileActivity.this, CheckOutActivity.class);
                                ckIntent.putExtra("total", tot);
                                ckIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ckIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                dialog.dismiss();
                                startActivity(ckIntent);
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ProfileActivity.this,"Please enter a valid Pincode or Phone Number.",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ProfileActivity.this,"Please fill all the fields.",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
