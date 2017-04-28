package com.prashantpandey.nurseryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class IndividualPotActivity extends AppCompatActivity {

    private RadioButton sixInch,eightInch,nineInch,tenInch,twelveInch,fourteenInch,sixteenInch,eighteenInch,twentyInch;
    private TextView potPriceText,quantityText,typeText;
    private ImageView potImage;
    private ImageButton decreaseButton,increaseButton;
    private FirebaseAuth mAuth;
    private DatabaseReference potReference,cartReference, cartObjectReference;
    private String potk,type,price6,price8,price9,price10,price12,price14,price16,price18,price20,pImage;
    private ProgressDialog cartDialog;
    private Button addToCart;
    private Toolbar potToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_pot);

        potToolbar = (Toolbar) findViewById(R.id.activity_individual_pot_toolbar);
        setSupportActionBar(potToolbar);

        mAuth = FirebaseAuth.getInstance();
        potReference = FirebaseDatabase.getInstance().getReference().child("Pots");
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
        cartObjectReference = cartReference.child(mAuth.getCurrentUser().getUid()).push();
        potk = getIntent().getStringExtra("PotKey");
        cartDialog = new ProgressDialog(this);

        sixInch = (RadioButton) findViewById(R.id.activity_individual_pot_6InchRad);
        eightInch = (RadioButton) findViewById(R.id.activity_individual_pot_8InchRad);
        nineInch = (RadioButton) findViewById(R.id.activity_individual_pot_9InchRad);
        tenInch = (RadioButton) findViewById(R.id.activity_individual_pot_10InchRad);
        twelveInch = (RadioButton) findViewById(R.id.activity_individual_pot_12InchRad);
        fourteenInch = (RadioButton) findViewById(R.id.activity_individual_pot_14InchRad);
        sixteenInch = (RadioButton) findViewById(R.id.activity_individual_pot_16InchRad);
        eighteenInch = (RadioButton) findViewById(R.id.activity_individual_pot_18InchRad);
        twentyInch = (RadioButton) findViewById(R.id.activity_individual_pot_20InchRad);

        potPriceText = (TextView) findViewById(R.id.activity_individual_pot_priceText);

        quantityText = (TextView) findViewById(R.id.activity_individual_pot_quantityText);
        typeText = (TextView) findViewById(R.id.activity_individual_pot_typeView);
        potImage = (ImageView) findViewById(R.id.activity_individual_pot_imageview);
        decreaseButton = (ImageButton) findViewById(R.id.activity_individual_pot_decreasequantity);
        increaseButton = (ImageButton) findViewById(R.id.activity_individual_pot_increasequantity);
        addToCart = (Button) findViewById(R.id.activity_individual_pot_addToBasket);
        setUpPrice();




        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = Integer.parseInt(quantityText.getText().toString());
                    int j = i + 1;
                    String k = String.valueOf(j);
                    quantityText.setText(k);
                } catch (Exception e) {
                    Toast.makeText(IndividualPotActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(quantityText.getText().toString()) <= 1) {
                        Toast.makeText(IndividualPotActivity.this, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                    } else {
                        int i = Integer.parseInt(quantityText.getText().toString());
                        int j = i - 1;
                        String k = String.valueOf(j);
                        quantityText.setText(k);
                    }

                } catch (Exception e) {
                    Toast.makeText(IndividualPotActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        potReference.child(potk).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("type").getValue().toString();
                pImage = dataSnapshot.child("imageurl").getValue().toString();
                price6 = dataSnapshot.child("pricesixInch").getValue().toString();
                price8 = dataSnapshot.child("priceeightInch").getValue().toString();
                price9 = dataSnapshot.child("pricenineInch").getValue().toString();
                price10 = dataSnapshot.child("pricetenInch").getValue().toString();
                price12 = dataSnapshot.child("pricetwelveInch").getValue().toString();
                price14 = dataSnapshot.child("pricefourteenInch").getValue().toString();
                price16 = dataSnapshot.child("pricesixteenInch").getValue().toString();
                price18 = dataSnapshot.child("priceeighteenInch").getValue().toString();
                price20 = dataSnapshot.child("pricetwentyInch").getValue().toString();

                Picasso.with(getApplicationContext()).load(pImage).into(potImage);


                if (dataSnapshot.child("pricesixInch").getValue().equals("")) {
                    sixInch.setVisibility(View.GONE);
                } else {
                    sixInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("priceeightInch").getValue().equals("")) {
                    eightInch.setVisibility(View.GONE);
                } else {
                    eightInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricenineInch").getValue().equals("")) {
                    nineInch.setVisibility(View.GONE);
                } else {
                    nineInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricetenInch").getValue().equals("")) {
                    tenInch.setVisibility(View.GONE);
                } else {
                    tenInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricetwelveInch").getValue().equals("")) {
                    twelveInch.setVisibility(View.GONE);
                } else {
                    twelveInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricefourteenInch").getValue().equals("")) {
                    fourteenInch.setVisibility(View.GONE);
                } else {
                    fourteenInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricesixteenInch").getValue().equals("")) {
                    sixteenInch.setVisibility(View.GONE);
                } else {
                    sixteenInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("priceeighteenInch").getValue().equals("")) {
                    eighteenInch.setVisibility(View.GONE);
                } else {
                    eighteenInch.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pricetwentyInch").getValue().equals("")) {
                    twentyInch.setVisibility(View.GONE);
                } else {
                    twentyInch.setVisibility(View.VISIBLE);
                }

                typeText.setText("Type: "+ type);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    public void setUpPrice(){
        sixInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sixInch.isChecked()){
                    potPriceText.setText("₹"+price6);
                }
            }
        });
        eightInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eightInch.isChecked()){
                    potPriceText.setText("₹"+price8);
                }
            }
        });
        nineInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nineInch.isChecked()){
                    potPriceText.setText("₹"+price9);
                }
            }
        });
        tenInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tenInch.isChecked()){
                    potPriceText.setText("₹"+price10);
                }
            }
        });
        twelveInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twelveInch.isChecked()){
                    potPriceText.setText("₹"+price12);
                }
            }
        });
        fourteenInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourteenInch.isChecked()){
                    potPriceText.setText("₹"+price14);
                }
            }
        });
        sixteenInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sixteenInch.isChecked()){
                    potPriceText.setText("₹"+price16);
                }
            }
        });
        eighteenInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eighteenInch.isChecked()){
                    potPriceText.setText("₹"+price18);
                }
            }
        });
        twentyInch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twentyInch.isChecked()){
                    potPriceText.setText("₹"+price20);
                }
            }
        });
    }
    public void addToCart(){
        if (sixInch.isChecked()||eightInch.isChecked()||nineInch.isChecked() || tenInch.isChecked() || twelveInch.isChecked() ||
                    fourteenInch.isChecked() || sixteenInch.isChecked() || eighteenInch.isChecked() || twentyInch.isChecked()) {


            cartDialog.setMessage("Adding to Cart...");
            cartDialog.show();
            cartObjectReference.child("commonName").setValue(type+" Pot");
            if (sixInch.isChecked()) {
                cartObjectReference.child("size").setValue(sixInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (eightInch.isChecked()) {
                cartObjectReference.child("size").setValue(eightInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (nineInch.isChecked()) {
                cartObjectReference.child("size").setValue(nineInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (tenInch.isChecked()) {
                cartObjectReference.child("size").setValue(tenInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (twelveInch.isChecked()) {
                cartObjectReference.child("size").setValue(twelveInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (fourteenInch.isChecked()) {
                cartObjectReference.child("size").setValue(fourteenInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (sixteenInch.isChecked()) {
                cartObjectReference.child("size").setValue(sixteenInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (eighteenInch.isChecked()) {
                cartObjectReference.child("size").setValue(eighteenInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }
            if (twentyInch.isChecked()) {
                cartObjectReference.child("size").setValue(twentyInch.getText().toString());
                cartObjectReference.child("price").setValue(potPriceText.getText().toString().substring(1));
            }




            cartObjectReference.child("imageUrl").setValue(pImage);
            cartObjectReference.child("Quantity").setValue(quantityText.getText().toString());
            String totalCost = String.valueOf(Integer.parseInt(quantityText.getText().toString()) * Integer.parseInt(potPriceText.getText().toString().substring(1)));
            cartObjectReference.child("Total").setValue(totalCost).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    cartDialog.dismiss();
                    Toast.makeText(IndividualPotActivity.this, type+" Pot successfully added to cart! ", Toast.LENGTH_SHORT).show();
                }

            });
        }else {
            Toast.makeText(IndividualPotActivity.this,"Please select the size of the Pot.",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_individual__pot_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.pot_menu_cart){
            Intent cartIntent = new Intent(IndividualPotActivity.this,CartActivity.class);
            startActivity(cartIntent);
        }
        if (item.getItemId()==R.id.pot_menu_addtocart){
            addToCart();
        }
        if (item.getItemId()==R.id.pot_menu_logout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
