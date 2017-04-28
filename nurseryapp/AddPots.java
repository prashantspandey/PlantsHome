package com.prashantpandey.nurseryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class AddPots extends AppCompatActivity implements View.OnClickListener {

    private ImageButton potImage;
    private RadioButton earthenPot, cementPot, plasticPot;
    private CheckBox sixInchBox, eightInchBox, nineInchBox, tenInchBox, twelveInchBox, fourteenInchBox, sixteenInchBox, eithteenInchBox;
    private CheckBox twentyInchBox;
    private EditText sixPrice, eightPrice, ninePrice, tenPrice, twelvePrice, fourteenPrice, sixteenPrice, eighteenPrice, twentyPrice;
    private Button potToDatabase;
    private Uri imageUri,downloadUri;
    private File imageFile;
    private static final int REQUEST_SELECT = 9;
    private ProgressDialog progressDialog;
    private StorageReference pStorage, potpicture;
    private DatabaseReference potReference,postPotReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pots);

        potReference = FirebaseDatabase.getInstance().getReference().child("Pots");
        postPotReference = potReference.push();
        pStorage = FirebaseStorage.getInstance().getReference();
        potImage = (ImageButton) findViewById(R.id.activity_add_pots_imageButton);
        earthenPot = (RadioButton) findViewById(R.id.activity_add_pots_earthenRadio);
        cementPot = (RadioButton) findViewById(R.id.activity_add_pots_cementRadio);
        plasticPot = (RadioButton) findViewById(R.id.activity_add_pots_plasticRadio);
        sixInchBox = (CheckBox) findViewById(R.id.activity_add_pots_6inchCheck);
        eightInchBox = (CheckBox) findViewById(R.id.activity_add_pots_8inchCheck);
        nineInchBox = (CheckBox) findViewById(R.id.activity_add_pots_9inchCheck);
        tenInchBox = (CheckBox) findViewById(R.id.activity_add_pots_10inchCheck);
        twelveInchBox = (CheckBox) findViewById(R.id.activity_add_pots_12inchCheck);
        fourteenInchBox = (CheckBox) findViewById(R.id.activity_add_pots_14inchCheck);
        sixteenInchBox = (CheckBox) findViewById(R.id.activity_add_pots_16inchCheck);
        eithteenInchBox = (CheckBox) findViewById(R.id.activity_add_pots_18inchCheck);
        twentyInchBox = (CheckBox) findViewById(R.id.activity_add_pots_20inchCheck);
        sixPrice = (EditText) findViewById(R.id.activity_add_pots_6inchPrice);
        eightPrice = (EditText) findViewById(R.id.activity_add_pots_8inchPrice);
        ninePrice = (EditText) findViewById(R.id.activity_add_pots_9inchPrice);
        tenPrice = (EditText) findViewById(R.id.activity_add_pots_10inchPrice);
        twelvePrice = (EditText) findViewById(R.id.activity_add_pots_12inchPrice);
        fourteenPrice = (EditText) findViewById(R.id.activity_add_pots_14inchPrice);
        sixteenPrice = (EditText) findViewById(R.id.activity_add_pots_16inchPrice);
        eighteenPrice = (EditText) findViewById(R.id.activity_add_pots_18inchPrice);
        twentyPrice = (EditText) findViewById(R.id.activity_add_pots_20inchPrice);
        potToDatabase = (Button) findViewById(R.id.activity_add_pots_addtodatabase);

        potImage.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        imageFile = new File(getExternalCacheDir(), "teimg.jpg");

        sixInchBox.setOnClickListener(this);
        eightInchBox.setOnClickListener(this);
        nineInchBox.setOnClickListener(this);
        tenInchBox.setOnClickListener(this);
        twelveInchBox.setOnClickListener(this);
        fourteenInchBox.setOnClickListener(this);
        sixteenInchBox.setOnClickListener(this);
        eithteenInchBox.setOnClickListener(this);
        twentyInchBox.setOnClickListener(this);

        potToDatabase.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_add_pots_imageButton) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 5);
            changePotPicture();
        }
        if (id == R.id.activity_add_pots_6inchCheck){
            if (sixInchBox.isChecked()){
                sixPrice.setVisibility(View.VISIBLE);
            }else{
                sixPrice.setVisibility(View.GONE);
                sixPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_8inchCheck){
            if (eightInchBox.isChecked()){
                eightPrice.setVisibility(View.VISIBLE);
            }else{
                eightPrice.setVisibility(View.GONE);
                eightPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_9inchCheck){
            if (nineInchBox.isChecked()){
                ninePrice.setVisibility(View.VISIBLE);
            }else{
                ninePrice.setVisibility(View.GONE);
                ninePrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_10inchCheck){
            if (tenInchBox.isChecked()){
                tenPrice.setVisibility(View.VISIBLE);
            }else{
                tenPrice.setVisibility(View.GONE);
                tenPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_12inchCheck){
            if (twelveInchBox.isChecked()){
                twelvePrice.setVisibility(View.VISIBLE);
            }else{
                twelvePrice.setVisibility(View.GONE);
                twelvePrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_14inchCheck){
            if (fourteenInchBox.isChecked()){
                fourteenPrice.setVisibility(View.VISIBLE);
            }else{
                fourteenPrice.setVisibility(View.GONE);
                fourteenPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_16inchCheck){
            if (sixteenInchBox.isChecked()){
                sixteenPrice.setVisibility(View.VISIBLE);
            }else{
                sixteenPrice.setVisibility(View.GONE);
                sixteenPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_18inchCheck){
            if (eithteenInchBox.isChecked()){
                eighteenPrice.setVisibility(View.VISIBLE);
            }else{
                eighteenPrice.setVisibility(View.GONE);
                eighteenPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_20inchCheck){
            if (twentyInchBox.isChecked()){
                twentyPrice.setVisibility(View.VISIBLE);
            }else{
                twentyPrice.setVisibility(View.GONE);
                twentyPrice.setText("");
            }
        }
        if (id == R.id.activity_add_pots_addtodatabase){
            final String sixp = sixPrice.getText().toString();
            final String eightp = eightPrice.getText().toString();
            final String ninep = ninePrice.getText().toString();
            final String tenp = tenPrice.getText().toString();
            final String twelvep = twelvePrice.getText().toString();
            final String fourteenp = fourteenPrice.getText().toString();
            final String sixteenp = sixteenPrice.getText().toString();
            final String eighteenp = eighteenPrice.getText().toString();
            final String twentyp = twentyPrice.getText().toString();

            final String ty = findType();
            progressDialog.setMessage("Adding " + ty + "to database.");
            progressDialog.show();

            if (imageFile!=null){
                File compressedFile = Compressor.getDefault(this).compressToFile(imageFile);
                Uri imageuri = Uri.fromFile(compressedFile);
                potpicture = pStorage.child("Pot_Pictures").child(imageuri.getLastPathSegment());
                potpicture.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUri = taskSnapshot.getDownloadUrl();
                        IdealPot newPot = new IdealPot(null,ty,sixp,eightp,ninep,tenp,twelvep,fourteenp,sixteenp,eighteenp,twentyp,downloadUri.toString());
                        postPotReference.setValue(newPot);
                        progressDialog.dismiss();
                    }
                });
            }


        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) {
            imageFile.delete();
            return;
        }
        if (requestCode == REQUEST_SELECT) {
            Uri outputFile;
            Uri tempFile = Uri.fromFile(imageFile);


        }

    }

    public void changePotPicture() {
        List<Intent> otherImageCaptureIntents = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities = getPackageManager().queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
        for (ResolveInfo info : otherImageCaptureActivities) {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            otherImageCaptureIntents.add(captureIntent);
        }


        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");
        Intent chooser = Intent.createChooser(selectImageIntent, "Choose the plant picture");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntents.toArray(new Parcelable[otherImageCaptureActivities.size()]));
        startActivityForResult(chooser, REQUEST_SELECT);
    }

    public String findType(){
        String type;
        if (earthenPot.isChecked()){
            type = "Earthen";
            return  type;
        }
        if (cementPot.isChecked()){
            type = "Cement";
            return type;
        }
        if (plasticPot.isChecked()){
            type = "Plastic";
            return type;
        }
        return null;
    }

}

