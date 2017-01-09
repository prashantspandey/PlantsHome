package com.prashantpandey.nurseryapp;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class IndividualPlantActivity extends AppCompatActivity {


    private String mPostKey = null;
    private DatabaseReference plantReference;
    private FirebaseAuth mAuth;
    private ImageView plantImage;
    private TextView commonName, scientificName, descriptionView, categoryView, sizeView,quantityBox, priceView;
    private RadioButton sizeSmallRadio,sizeMediumRadio,sizeLargeRadio, sizeXLargeRadio, sizeXXLargeRadio, sizeOtherRadio;
    private Button addtocartButton;
    private ImageButton reducequantityButton, increaseQuantityButton;
    private String postImage, postCommonName, postScientificName, postDescription, postCategory, postSize;
    private ArrayList<String> postcategorylist;
    private ProgressDialog individualProgressDialog;
    private Query equalQuery;
    private DatabaseReference cartobject;
    private DatabaseReference cartReference;
    private ShowImageDialog imgdialog;
    private String sizesmall,sizemedium,sizelarge, sizexlarge, sizexxlarge , sizeOther, priceOther;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_plant);
        mAuth = FirebaseAuth.getInstance();
        individualProgressDialog = new ProgressDialog(this);
        plantReference = FirebaseDatabase.getInstance().getReference().child("Plants");
         cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
         cartobject = cartReference.child(mAuth.getCurrentUser().getUid()).push();
        equalQuery = cartobject.orderByChild("commonName").equalTo(postCommonName);
        mPostKey = getIntent().getStringExtra("PlantId");

        commonName = (TextView) findViewById(R.id.activity_individual_plantCommonName);
        scientificName = (TextView) findViewById(R.id.activity_individual_plantScientificName);
        descriptionView = (TextView) findViewById(R.id.activity_individual_descriptionText);
        categoryView = (TextView) findViewById(R.id.activity_individual_plantCategory);
        sizeXLargeRadio = (RadioButton) findViewById(R.id.activity_individual_sizeXlargeRadioButton);
        sizeXXLargeRadio = (RadioButton) findViewById(R.id.activity_individual_sizeXXlargeRadioButton);
        sizeOtherRadio = (RadioButton) findViewById(R.id.activity_individual_sizeOther);
        plantImage = (ImageView) findViewById(R.id.activity_individual_plantImage);
        reducequantityButton = (ImageButton) findViewById(R.id.activity_individual_plantdownQuantitybutton);
        increaseQuantityButton = (ImageButton) findViewById(R.id.activity_individual_plantupQuantitybutton);
        quantityBox = (TextView) findViewById(R.id.activity_individual_plantQuantityText);
        sizeSmallRadio = (RadioButton) findViewById(R.id.activity_individual_sizeSmallRadio);
        sizeMediumRadio = (RadioButton) findViewById(R.id.activity_individual_sizeMediumRadio);
        sizeLargeRadio = (RadioButton) findViewById(R.id.activity_individual_sizeLaRadioRadio);
        priceView = (TextView) findViewById(R.id.activity_individual_priceText);
        addtocartButton = (Button) findViewById(R.id.activity_individual_plantAddtobasketbutton);
        Toolbar individualToolbar = (Toolbar) findViewById(R.id.activity_individual_toolbar);
        setSupportActionBar(individualToolbar);
        getSupportActionBar().setTitle("Plant Description");
        equalQuery.keepSynced(true);
        sizeSmallRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeSmallRadio.isChecked()) {
                    priceView.setText("₹" + sizesmall);
                }
            }
        });
        sizeMediumRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeMediumRadio.isChecked()){
                    priceView.setText("₹"+ sizemedium);
                }
            }
        });
        sizeLargeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeLargeRadio.isChecked()){
                    priceView.setText("₹"+ sizelarge);
                }
            }
        });
        sizeXLargeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeXLargeRadio.isChecked()){
                    priceView.setText("₹"+ sizexlarge);
                }
            }
        });
        sizeXXLargeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeXXLargeRadio.isChecked()){
                    priceView.setText("₹"+ sizexxlarge);
                }
            }
        });
        sizeOtherRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeOtherRadio.isChecked()){
                    priceView.setText("₹"+ priceOther );
                }
            }
        });





        plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle imageLink = new Bundle();
                imageLink.putString("imageLink",postImage);
                FragmentTransaction transac = getFragmentManager().beginTransaction();
                imgdialog = new ShowImageDialog();
                imgdialog.setArguments(imageLink);
                imgdialog.show(transac,null);


            }
        });


        addtocartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                addToCart();

            }
        });

        quantityBox.setText("1");


        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = Integer.parseInt(quantityBox.getText().toString());
                    int j = i + 1;
                    String k = String.valueOf(j);
                    quantityBox.setText(k);
                } catch (Exception e) {
                    Toast.makeText(IndividualPlantActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reducequantityButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(quantityBox.getText().toString()) <= 1) {
                        Toast.makeText(IndividualPlantActivity.this, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                    } else {
                        int i = Integer.parseInt(quantityBox.getText().toString());
                        int j = i - 1;
                        String k = String.valueOf(j);
                        quantityBox.setText(k);
                    }

                } catch (Exception e) {
                    Toast.makeText(IndividualPlantActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        plantReference.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    postCommonName = dataSnapshot.child("commonName").getValue().toString();
                    postScientificName = dataSnapshot.child("scientificName").getValue().toString();
                    postDescription = dataSnapshot.child("description").getValue().toString();
                    postCategory = dataSnapshot.child("category").getValue().toString();
                    sizesmall = dataSnapshot.child("priceSmall").getValue().toString();
                    sizemedium = dataSnapshot.child("priceMedium").getValue().toString();
                    sizelarge = dataSnapshot.child("priceLarge").getValue().toString();
                    sizexlarge = dataSnapshot.child("priceXLarge").getValue().toString();
                    sizexxlarge = dataSnapshot.child("priceXXLarge").getValue().toString();
                    sizeOther = dataSnapshot.child("other").getValue().toString();
                    priceOther = dataSnapshot.child("priceOther").getValue().toString();
                    postImage = dataSnapshot.child("imageUrl").getValue().toString();

                    commonName.setText(postCommonName.substring(0, 1).toUpperCase() + postCommonName.substring(1));
                    scientificName.setText("Scientific Name: " + postScientificName);
                    descriptionView.setText("Description:  " + postDescription);
                    categoryView.setText("Category: " + postCategory);

                    Picasso.with(getApplicationContext()).load(postImage).into(plantImage);


                    if (dataSnapshot.child("priceSmall").getValue().equals("")) {
                        sizeSmallRadio.setVisibility(View.GONE);
                    } else {
                        sizeSmallRadio.setVisibility(View.VISIBLE);
                    }
                    if (dataSnapshot.child("priceMedium").getValue().equals("")) {
                        sizeMediumRadio.setVisibility(View.GONE);
                    } else {
                        sizeMediumRadio.setVisibility(View.VISIBLE);
                    }
                    if (dataSnapshot.child("priceLarge").getValue().equals("")) {
                        sizeLargeRadio.setVisibility(View.GONE);
                    } else {
                        sizeLargeRadio.setVisibility(View.VISIBLE);
                    }
                    if (dataSnapshot.child("priceXLarge").getValue().equals("")){
                        sizeXLargeRadio.setVisibility(View.GONE);
                    }else{
                        sizeXLargeRadio.setVisibility(View.VISIBLE);
                    }
                    if (dataSnapshot.child("priceXXLarge").getValue().equals("")){
                        sizeXXLargeRadio.setVisibility(View.GONE);
                    }else{
                        sizeXXLargeRadio.setVisibility(View.VISIBLE);
                    }
                    if (dataSnapshot.child("other").getValue().equals("")){
                        sizeOtherRadio.setVisibility(View.GONE);
                    }
                    else {
                        sizeOtherRadio.setVisibility(View.VISIBLE);
                        sizeOtherRadio.setText(dataSnapshot.child("other").getValue().toString());
                    }
                }catch (Exception e){
                    Log.e("itemadded","new item added");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndividualPlantActivity.this,"Error connecting to the server.Please check your internet connection",Toast.LENGTH_SHORT).show();
            }

        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_individual_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.individual_menu_cart) {
            Intent cartIntent = new Intent(IndividualPlantActivity.this, CartActivity.class);
            startActivity(cartIntent);
        }

        if (item.getItemId() == R.id.individual_menu_addToCart){
            addToCart();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {


        super.onResume();
    }
    public void addToCart(){
        if (sizeSmallRadio.isChecked()||sizeMediumRadio.isChecked()||sizeLargeRadio.isChecked()
                ||sizeXLargeRadio.isChecked()||sizeXXLargeRadio.isChecked()||sizeOtherRadio.isChecked()) {


            individualProgressDialog.setMessage("Adding to Cart...");
            individualProgressDialog.show();
            cartobject.child("commonName").setValue(postCommonName);
            if (sizeSmallRadio.isChecked()) {
                cartobject.child("size").setValue(sizeSmallRadio.getText().toString());
            }
            if (sizeMediumRadio.isChecked()) {
                cartobject.child("size").setValue(sizeMediumRadio.getText().toString());
            }
            if (sizeLargeRadio.isChecked()) {
                cartobject.child("size").setValue(sizeLargeRadio.getText().toString());
            }
            if (sizeXLargeRadio.isChecked()){
                cartobject.child("size").setValue(sizeXLargeRadio.getText().toString());
            }
            if (sizeXXLargeRadio.isChecked()){
                cartobject.child("size").setValue(sizeXXLargeRadio.getText().toString());
            }
            if (sizeOtherRadio.isChecked()){
                cartobject.child("size").setValue(sizeOtherRadio.getText().toString());
            }
            cartobject.child("price").setValue(priceView.getText().toString().substring(1));

            cartobject.child("imageUrl").setValue(postImage);
            cartobject.child("Quantity").setValue(quantityBox.getText().toString());
            String totalCost = String.valueOf(Integer.parseInt(quantityBox.getText().toString()) * Integer.parseInt(priceView.getText().toString().substring(1)));
            cartobject.child("Total").setValue(totalCost).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    individualProgressDialog.dismiss();
                    Toast.makeText(IndividualPlantActivity.this, postCommonName+" successfully added to cart! ", Toast.LENGTH_SHORT).show();
                }

            });
        }else {
            Toast.makeText(IndividualPlantActivity.this,"Please select the size of the plant.",Toast.LENGTH_SHORT).show();
        }


    }



}