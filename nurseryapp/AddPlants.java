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


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.soundcloud.android.crop.Crop;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AddPlants extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_SELECT_IMAGE = 3;
    private ImageButton imageButton;
    private RadioButton radioHerb, radioShrub, radioTree, radioFlowering, radioPalm, radioFruit,radioGrass,radioBonsai,radioOrnamental;
    private EditText addCommonName, addSceintificName, addDescription, addSizeSmall, addSizeMedium, addSizeLarge , addSizeXLarge,addSizeXXLarge;
    private CheckBox smallcheckBox,mediumCheckBox,largeCheckBox,XLargeCheckBox,XXLargeCheckBox;
    private File tempOutputFile;
    private Uri pictureUri;
    public static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    private StorageReference pictureFilePath;
    private DatabaseReference plantsReference,postPlantsReference;
    private Uri imageUri;
    private Uri downloadUri;
    private ProgressDialog progressDialog;
    private Random random;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);
        progressDialog = new ProgressDialog(this);

        plantsReference = FirebaseDatabase.getInstance().getReference().child("Plants");
        postPlantsReference = plantsReference.push();
        mStorage = FirebaseStorage.getInstance().getReference();


        imageButton = (ImageButton) findViewById(R.id.activity_add_plants_imageButton);
        Button addPlantsButton = (Button) findViewById(R.id.activity_add_plants_addPlantButton);
        smallcheckBox = (CheckBox) findViewById(R.id.activity_add_plants_sizeSmallCheckBox);
        mediumCheckBox = (CheckBox) findViewById(R.id.activity_add_plants_sizeMediumCheckBox);
        largeCheckBox = (CheckBox) findViewById(R.id.activity_add_plants_sizeLargeCheckbox);
        XLargeCheckBox = (CheckBox) findViewById(R.id.activity_add_plants_sizeXLargeCheckBox);
        XXLargeCheckBox = (CheckBox) findViewById(R.id.activity_add_plants_sizeXXLargeCheckBox);
        radioHerb = (RadioButton) findViewById(R.id.activity_add_plants_categoryHerb);
        radioShrub = (RadioButton) findViewById(R.id.activity_add_plants_categoryShrub);
        radioTree = (RadioButton) findViewById(R.id.activity_add_plants_categoryTree);
        radioFlowering = (RadioButton) findViewById(R.id.activity_add_plants_categoryFlowering);
        radioPalm = (RadioButton) findViewById(R.id.activity_add_plants_categoryPalm);
        radioFruit = (RadioButton) findViewById(R.id.activity_add_plants_categoryFruit);
        radioGrass = (RadioButton) findViewById(R.id.activity_add_plants_categoryGrass);
        radioBonsai = (RadioButton) findViewById(R.id.activity_add_plants_categoryBonsai);
        radioOrnamental = (RadioButton) findViewById(R.id.activity_add_plants_categoryOrnamental);
        addCommonName = (EditText) findViewById(R.id.activity_add_plants_commonNameText);
        addSceintificName = (EditText) findViewById(R.id.activity_add_plants_scientificNameText);
        addDescription = (EditText) findViewById(R.id.activity_add_plants_descriptionText);
        addSizeLarge = (EditText) findViewById(R.id.activity_add_plants_priceLarge);
        addSizeMedium = (EditText) findViewById(R.id.activity_add_plants_priceMedium);
        addSizeSmall = (EditText) findViewById(R.id.activity_add_plants_priceSmall);
        addSizeXLarge = (EditText) findViewById(R.id.activity_add_plants_pirceXLarge);
        addSizeXXLarge = (EditText) findViewById(R.id.activity_add_plants_priceXXLarge);


        imageButton.setOnClickListener(this);
        addPlantsButton.setOnClickListener(this);
        smallcheckBox.setOnClickListener(this);
        mediumCheckBox.setOnClickListener(this);
        largeCheckBox.setOnClickListener(this);
        XLargeCheckBox.setOnClickListener(this);
        XXLargeCheckBox.setOnClickListener(this);

        tempOutputFile = new File(getExternalCacheDir(), "temp-img.jpg");

        addSizeSmall.setVisibility(View.GONE);
        addSizeMedium.setVisibility(View.GONE);
        addSizeLarge.setVisibility(View.GONE);
        addSizeXLarge.setVisibility(View.GONE);
        addSizeXXLarge.setVisibility(View.GONE);






    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_add_plants_imageButton) {
            changePlantPicture();

        }
        if (id == R.id.activity_add_plants_addPlantButton){
            final String addCommonNametext = addCommonName.getText().toString();
            final String addScientificNametext = addSceintificName.getText().toString();
            final String addDescriptiontext = addDescription.getText().toString();
            final String priceSmall = addSizeSmall.getText().toString();
            final String priceMedium = addSizeMedium.getText().toString();
            final String priceLarge = addSizeLarge.getText().toString();
            final String priceXLarge = addSizeXLarge.getText().toString();
            final String priceXXLarge = addSizeXXLarge.getText().toString();
            final boolean s = isItSmall();
            final boolean m = isItMedium();
            final boolean l = isItLarge();
            final boolean xL = isItXLarge();
            final boolean xxL = isItXXLarge();
            final String catg = findCategory();
            IdealPlant addPlant = new IdealPlant(catg,addCommonNametext,"",
                    priceLarge,priceMedium,priceSmall,priceXLarge,priceXXLarge,"",addScientificNametext,addDescriptiontext,s,m,l,xL,xxL,"");
            postPlantsReference.setValue(addPlant);
            progressDialog.dismiss();

            }


        if (id == R.id.activity_add_plants_sizeSmallCheckBox) {
            if (smallcheckBox.isChecked()) {
                addSizeSmall.setVisibility(View.VISIBLE);
            } else {
                addSizeSmall.setVisibility(View.GONE);
                addSizeSmall.setText("");
            }
        }
        if (id == R.id.activity_add_plants_sizeMediumCheckBox){
            if (mediumCheckBox.isChecked()){
                addSizeMedium.setVisibility(View.VISIBLE);
            }else{
                addSizeMedium.setVisibility(View.GONE);
                addSizeMedium.setText("");
            }
        }
        if (id == R.id.activity_add_plants_sizeLargeCheckbox){
            if (largeCheckBox.isChecked()){
                addSizeLarge.setVisibility(View.VISIBLE);
            }else {
                addSizeLarge.setVisibility(View.GONE);
                addSizeLarge.setText("");
            }
        }
        if (id == R.id.activity_add_plants_sizeXLargeCheckBox){
            if (XLargeCheckBox.isChecked()){
                addSizeXLarge.setVisibility(View.VISIBLE);
            }else{
                addSizeXLarge.setVisibility(View.GONE);
                addSizeXLarge.setText("");
            }
        }
        if (id == R.id.activity_add_plants_sizeXXLargeCheckBox){
            if (XXLargeCheckBox.isChecked()){
                addSizeXXLarge.setVisibility(View.VISIBLE);
            }else{
                addSizeXXLarge.setVisibility(View.GONE);
                addSizeXXLarge.setText("");
            }
        }


    }

    private void changePlantPicture() {
        List<Intent> otherImageCaptureIntents = new ArrayList<>();
        List<ResolveInfo> otherImageCaputreActivities = getPackageManager()
                        .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),0);

        for(ResolveInfo info: otherImageCaputreActivities){
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName,info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempOutputFile));
            otherImageCaptureIntents.add(captureIntent);
        }

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent , "Choose Plant Picture");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,otherImageCaptureIntents.toArray(new Parcelable[otherImageCaputreActivities.size()]));
        startActivityForResult(chooser,REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_OK){
            tempOutputFile.delete();
            return;
        }
        if (requestCode==REQUEST_SELECT_IMAGE){
            Uri outputFile;
            Uri tempFileUri = Uri.fromFile(tempOutputFile);
            if (data!=null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)))
                outputFile = data.getData();
            else
                outputFile = tempFileUri;

            new Crop(outputFile).output(tempFileUri).start(this);
        }else if (requestCode==Crop.REQUEST_CROP){
            imageButton.setImageURI(Uri.fromFile(tempOutputFile));
        }
    }

    public String findCategory(){
        String category;
        if (radioHerb.isChecked()){
            category = "herb";
            return category;
        }
        if (radioPalm.isChecked()){
            category = "palm";
            return category;
        }
        if (radioFlowering.isChecked()){
            category = "flowering/Seasonal Plant";
            return category;
        }
        if (radioTree.isChecked()){
            category = "tree";
            return category;
        }

        if (radioShrub.isChecked()){
            category = "shrub";
            return category;
        }
        if (radioFruit.isChecked()){
            category = "fruit";
            return category;
        }
        if (radioGrass.isChecked()){
            category = "grass";
            return category;
        }
        if (radioBonsai.isChecked()){
            category = "bonsai";
            return category;
        }
        if (radioOrnamental.isChecked()){
            category = "ornamental";
            return category;
        }
        return null;
    }

    private boolean isItSmall(){

        return smallcheckBox.isChecked();
    }
    private boolean isItMedium(){

        return mediumCheckBox.isChecked();
    }
    private boolean isItXLarge(){

        return XLargeCheckBox.isChecked();
    }
    private boolean isItXXLarge(){

        return XXLargeCheckBox.isChecked();
    }
    private boolean isItLarge(){

        return largeCheckBox.isChecked();
    }


    public String generateRandomPictureId(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100 + rnd.nextInt(90000)) + "-");
        for (int i = 0; i < 5; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }


}