package com.prashantpandey.nurseryapp;

import android.app.FragmentTransaction;
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

public class IndividualExtraActivity extends AppCompatActivity {

    private ImageView extraImageView;
    private TextView commonNameView,sizeView,quantityView,descriptionView,priceView,typeView;
    private ImageButton decreaseQuantity,increaseQuantity;
    private Button addToCartButton;
    private String commonName,size,quantity,description,imageUrl,price,type;
    private Toolbar extraToolbar;
    private DatabaseReference extraReference;
    private DatabaseReference cartReference, cartobject;
    private FirebaseAuth mAuth;
    private String extraPostKey = null;
    private ShowImageDialog imageDialog;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_extra);

        extraToolbar = (Toolbar) findViewById(R.id.activity_extra_toolbar);
        setSupportActionBar(extraToolbar);
        getSupportActionBar().setTitle("Item Description");
        progressDialog= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        extraReference = FirebaseDatabase.getInstance().getReference().child("Extras");
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
        cartobject = cartReference.child(mAuth.getCurrentUser().getUid()).push();
        extraPostKey = getIntent().getStringExtra("ExtraKey");
        extraImageView = (ImageView) findViewById(R.id.activity_individual_extra_imageView);
        commonNameView = (TextView) findViewById(R.id.activity_individual_extra_commonNameView);
        sizeView = (TextView) findViewById(R.id.activity_individual_extra_sizeView);
        quantityView = (TextView) findViewById(R.id.activity_individual_extra_quantityText);
        descriptionView = (TextView) findViewById(R.id.activity_individual_extra_descriptionView);
        addToCartButton = (Button) findViewById(R.id.activity_individual_extra_addToCartButton);
        decreaseQuantity = (ImageButton) findViewById(R.id.activity_individual_extra_decreaseButton);
        increaseQuantity = (ImageButton) findViewById(R.id.activity_individual_extra_increaseButton);
        priceView = (TextView)findViewById(R.id.activity_individual_extra_priceView);
        typeView = (TextView) findViewById(R.id.activity_individual_extra_typeView);


        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = Integer.parseInt(quantityView.getText().toString());
                    int j = i + 1;
                    String k = String.valueOf(j);
                    quantityView.setText(k);
                } catch (Exception e) {
                    Toast.makeText(IndividualExtraActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(quantityView.getText().toString()) <= 1) {
                        Toast.makeText(IndividualExtraActivity.this, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                    } else {
                        int i = Integer.parseInt(quantityView.getText().toString());
                        int j = i - 1;
                        String k = String.valueOf(j);
                        quantityView.setText(k);
                    }

                } catch (Exception e) {
                    Toast.makeText(IndividualExtraActivity.this, "Quantity can only be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        extraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle imageLink = new Bundle();
                imageLink.putString("imageLink",imageUrl);
                FragmentTransaction transac = getFragmentManager().beginTransaction();
                imageDialog = new ShowImageDialog();
                imageDialog.setArguments(imageLink);
                imageDialog.show(transac,null);


            }
        });
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        populateIndividualExtra();


    }
    public void populateIndividualExtra(){
        extraReference.child(extraPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commonName = dataSnapshot.child("name").getValue().toString();
                description = dataSnapshot.child("description").getValue().toString();
                size = dataSnapshot.child("weight").getValue().toString();
                price = dataSnapshot.child("price").getValue().toString();
                type = dataSnapshot.child("type").getValue().toString();
                imageUrl = dataSnapshot.child("imageUrl").getValue().toString();

                commonNameView.setText("Name: "+ commonName);
                descriptionView.setText("Description: " + description);
                sizeView.setText("Minimum Size/Weight: " + size);
                priceView.setText("₹"+ price);
                typeView.setText("Type:" + type);
                Picasso.with(IndividualExtraActivity.this).load(imageUrl).into(extraImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void addToCart(){
        progressDialog.setMessage("Adding to Cart/Basket");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        cartobject.child("commonName").setValue(commonName);
        cartobject.child("price").setValue(priceView.getText().toString().substring(1));

        cartobject.child("imageUrl").setValue(imageUrl);
        cartobject.child("Quantity").setValue(quantityView.getText().toString());
        String totalCost = String.valueOf(Integer.parseInt(quantityView.getText().toString()) * Integer.parseInt(priceView.getText().toString().substring(1)));
        cartobject.child("Total").setValue(totalCost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(IndividualExtraActivity.this, commonName+" successfully added to cart! ", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.extra_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_extra_addToCart){
            addToCart();
        }
        if (item.getItemId() == R.id.menu_extra_goToCart){
            startActivity(new Intent(IndividualExtraActivity.this,CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
