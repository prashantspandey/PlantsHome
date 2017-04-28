package com.prashantpandey.nurseryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView totalText, addressText;
    private RadioButton cashButton, cardButton;
    private RecyclerView recyclerView;
    private static String GRAND_TOTAL = "GRAND_TOTAL";
    private DatabaseReference orderCartReference, OrderReference, putOrder;
    private DatabaseReference Users;
    private DatabaseReference userAddress, userName, userPhone;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    public Button buyButton, changeAddress;
    private FirebaseRecyclerAdapter<IdealCart, IdealCartHolder> recyclerAdapter;
    private String timeStamp;
    private ArrayList<IdealCart> cartItems;
    private String username;
    private String add;
    private String phone;
    private String nowTime;
    private String pText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mAuth = FirebaseAuth.getInstance();
        orderCartReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(mAuth.getCurrentUser().getUid());
        Users = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        OrderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        putOrder = OrderReference.push();
        userAddress = Users.child("address");
        userName = Users.child("name");
        userPhone = Users.child("phoneNumber");

        progressDialog = new ProgressDialog(this);
        totalText = (TextView) findViewById(R.id.activity_check_out_totaltext);
        checkTotal();
        addressText = (TextView) findViewById(R.id.activity_check_out_addressText);

        recyclerView = (RecyclerView) findViewById(R.id.activity_check_out_recyclerView);
        changeAddress = (Button) findViewById(R.id.activity_check_out_changeAddressButton);
        buyButton = (Button) findViewById(R.id.activity_check_out_buyButton);
        buyButton.setOnClickListener(this);
        changeAddress.setOnClickListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
        recyclerView.setAdapter(recyclerAdapter);
        getItemsToOrders();

        GRAND_TOTAL = getIntent().getStringExtra("total");
        pText = GRAND_TOTAL;
        totalText.setText("₹" + GRAND_TOTAL);
        orderCartReference.keepSynced(true);

        userAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addressText.setText("Address: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        onStart();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_check_out_changeAddressButton) {
            Intent changeAddressIntent = new Intent(CheckOutActivity.this, ProfileActivity.class);
            changeAddressIntent.putExtra("backtotal",pText);

            startActivity(changeAddressIntent);
            finish();
        }
        if (id == R.id.activity_check_out_buyButton) {
            progressDialog.setMessage("Placing your Order. Please wait.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            checkName();
        }
    }

    public static class IdealCartHolder extends RecyclerView.ViewHolder {
        private View dView;

        public IdealCartHolder(View itemView) {
            super(itemView);
            dView = itemView;

        }

        public void setName(String commonName) {
            TextView commName = (TextView) dView.findViewById(R.id.row_cart_checkout_name);
            commName.setText(commonName);
        }

        public void setSize(String size) {
            TextView sizeText = (TextView) dView.findViewById(R.id.row_cart_checkout_size);
            sizeText.setText(size);
        }


        public void setTot(String total) {
            TextView Total = (TextView) dView.findViewById(R.id.row_cart_checkout_cost);
            Total.setText(total);
        }

        public void setQuant(String quantity) {
            TextView Quantity = (TextView) dView.findViewById(R.id.row_cart_checkout_quantity);
            Quantity.setText(quantity);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        checkTotal();


        recyclerAdapter = new FirebaseRecyclerAdapter<IdealCart, IdealCartHolder>(
                IdealCart.class, R.layout.row_cart_check_out, IdealCartHolder.class, orderCartReference
        ) {
            @Override
            protected void populateViewHolder(IdealCartHolder viewHolder, IdealCart model, int position) {

                viewHolder.setName(model.getCommonName().substring(0, 1).toUpperCase() + model.getCommonName().substring(1));
                viewHolder.setQuant("Quantity: " + model.getQuantity());
                viewHolder.setTot("Total: ₹ " + model.getTotal());
                viewHolder.setSize("Size: " + model.getSize());

                if (position % 2 == 0) {
                    viewHolder.dView.setBackgroundColor(Color.parseColor("#616161"));
                } else {
                    viewHolder.dView.setBackgroundColor(Color.parseColor("#212121"));
                }

            }
        };

        userPhone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phone = dataSnapshot.getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                add = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getItemsToOrders() {
        cartItems = new ArrayList<>();
        orderCartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    cartItems.add(snapshot.getValue(IdealCart.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void buy() {


        nowTime = DateFormat.getDateTimeInstance().format(new Date());
        IdealOrder newOrder = new IdealOrder(username, phone, add, cartItems.toString(), GRAND_TOTAL, nowTime, false, false, mAuth.getCurrentUser().getUid());

        putOrder.setValue(newOrder);
        orderCartReference.removeValue();
        startActivity(new Intent(CheckOutActivity.this, UserOrderActivity.class));
        finish();


    }

    public void checkName() {
        userName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(CheckOutActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    Intent profile = new Intent(CheckOutActivity.this, ProfileActivity.class);
                    startActivity(profile);
                    finish();
                } else {

                    buy();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkTotal() {

        if ( GRAND_TOTAL == null) {

            Intent checkintent = new Intent(CheckOutActivity.this,MainActivity.class);
            checkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(checkintent);
            finish();
        }
    }

}
