package com.prashantpandey.nurseryapp;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private String cartPostKey = null;
    private FirebaseAuth mAuth;
    private ImageView cartImage;

    private RecyclerView cartRecyclerView;
    private DatabaseReference mCartReference;
    private DatabaseReference usersReference;
    private ProgressDialog progressDialog;
    private TextView grandTotalText, delivery, minimumOrderText;
    private ModifyCartDialog cartDialog;
    private ArrayList<String> grandTotalArr;
    private String gettotal;

    private int tot,checkOutTot;
    private Intent orderIntent;
    private float del, tenPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mAuth = FirebaseAuth.getInstance();
        mCartReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(mAuth.getCurrentUser().getUid());
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        cartRecyclerView = (RecyclerView) findViewById(R.id.activity_cart_cartrecyclerView);
        grandTotalText = (TextView) findViewById(R.id.activity_cart_gradTotal);
        minimumOrderText = (TextView) findViewById(R.id.activity_cart_minimumOrderText);
        progressDialog = new ProgressDialog(this);
        delivery = (TextView) findViewById(R.id.activity_cart_delivery);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar cartToolbar = (Toolbar) findViewById(R.id.activity_cart_toolbar);
        setSupportActionBar(cartToolbar);
        getSupportActionBar().setTitle("Your Cart");


        mCartReference.keepSynced(true);







    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateTotal();

    }

    @Override
    public void onClick(View v) {

    }


    public static class IdealCartViewHolder extends RecyclerView.ViewHolder  {
            private View cView;

        public IdealCartViewHolder(View itemView) {
            super(itemView);
             cView= itemView;

        }
        public void setCommonName(String commonName ){
            TextView cartCommonName = (TextView) cView.findViewById(R.id.row_cart_plantCommonName);
            cartCommonName.setText(commonName);
        }

        public void setPrice(String Price){
            TextView cartPrice = (TextView) cView.findViewById(R.id.row_cart_plantPrice);
            cartPrice.setText(Price);
        }
        public void setTotal(String total){
            TextView cartTotal = (TextView) cView.findViewById(R.id.row_cart_plantTotalPrice);
            cartTotal.setText(total);
        }
        public void setImage(Context context,String url){
            ImageView cartImage = (ImageView) cView.findViewById(R.id.row_cart_plantImage);
            Picasso.with(context).load(url).into(cartImage);
        }
        public void setQuantity(String quantity){
            TextView cartQuantity = (TextView) cView.findViewById(R.id.row_cart_plantQuantity);
            cartQuantity.setText(quantity);
        }
        public void setSize(String size){
            TextView cartSize = (TextView) cView.findViewById(R.id.row_cart_size);
            cartSize.setText(size);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Please wait.. Loading cart items.");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mCartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.setMessage("Network connection Failure ! Please check your Internet.");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },2000);
            }
        });
        FirebaseRecyclerAdapter<IdealCart,IdealCartViewHolder> cartRecyclerViewHolder =
                new FirebaseRecyclerAdapter<IdealCart, IdealCartViewHolder>(IdealCart.class,R.layout.cartrecyclerview_row,IdealCartViewHolder.class,mCartReference) {
                    @Override
                    protected void populateViewHolder(final IdealCartViewHolder viewHolder, final IdealCart model, int position) {


                        final String cartKey = getRef(position).getKey();
                        viewHolder.setImage(getApplicationContext(),model.getImageUrl());
                        viewHolder.setPrice("Price: ₹ "+ model.getPrice());
                        viewHolder.setSize("Size: " + model.getSize());
                        viewHolder.setCommonName(model.getCommonName().substring(0,1).toUpperCase()+model.getCommonName().substring(1));
                        viewHolder.setQuantity("Quantity: "+ model.getQuantity());
                        viewHolder.setTotal("Total: ₹ "+ model.getTotal());


                        final int quant = Integer.parseInt(model.getQuantity());



                        viewHolder.cView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {




                                Bundle sendQuant = new Bundle();
                                sendQuant.putInt("previousQuantity",quant);
                                FragmentTransaction modifyTransaction = getFragmentManager().beginTransaction();
                                cartDialog = new ModifyCartDialog();
                                cartDialog.setArguments(sendQuant);
                                cartDialog.show(modifyTransaction,null);
                                cartDialog.modifyCartItem(new ModifyCartDialog.modifyItemInterface() {
                                    @Override
                                    public void changeQuant(String result) {
                                        mCartReference.child(cartKey).child("Quantity").setValue(result);
                                        String newTotal = String.valueOf(Integer.parseInt(model.getPrice()) * Integer.parseInt(result));
                                        mCartReference.child(cartKey).child("Total").setValue(newTotal);
                                        onResume();
                                        Toast.makeText(CartActivity.this,"Quantity of "+ model.getCommonName()+" changed to "+result,Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void delete() {



                                        Toast.makeText(CartActivity.this,"Item successfully removed.",Toast.LENGTH_SHORT).show();

                                        mCartReference.child(cartKey).removeValue();
                                        onResume();


                                    }
                                });

                            }
                        });





                    }
                };
        cartRecyclerView.setAdapter(cartRecyclerViewHolder);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cart_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.cart_menu_order){
            if (Float.parseFloat(gettotal)<199){
                Toast.makeText(CartActivity.this,"Order is less than ₹199,Please add few more items in cart.",Toast.LENGTH_SHORT).show();
            }else {

                AlertDialog checkOutDialog = new AlertDialog.Builder(CartActivity.this).setPositiveButton("Check Out",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Checking...");
                                progressDialog.show();
                                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() == null) {
                                            Intent profileIntent = new Intent(CartActivity.this, ProfileActivity.class);
                                            profileIntent.putExtra("total", gettotal);
                                            progressDialog.dismiss();
                                            startActivity(profileIntent);
                                            finish();
                                        } else {
                                            orderIntent = new Intent(CartActivity.this, CheckOutActivity.class);
                                            orderIntent.putExtra("total", gettotal);
                                            progressDialog.dismiss();
                                            startActivity(orderIntent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).setNegativeButton("Cancel, I need to add few more items.",null).setTitle("Checking out" +
                        ", We might need some information.").show();

            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void calculateTotal(){
        tot = 0;
        Query totquery = mCartReference.orderByChild("Total");
        totquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String kk = snapshot.child("Total").getValue().toString();

                    tot = tot + Integer.parseInt(kk);


                }
                if (tot<199){
                    minimumOrderText.setVisibility(View.VISIBLE);
                    minimumOrderText.setText("Minimum order is ₹199");
                    delivery.setText("10% delivery charge on order,Free delivery over ₹999 ");



                    grandTotalText.setText(String.valueOf("Grand Total: ₹"+ tot +", Cart less than ₹199"));
                    gettotal = String.valueOf(tot);
                }else if (tot>199 && tot<999)
                {
                    delivery.setText("10% delivery charges for orders between ₹199 and ₹999");
                    minimumOrderText.setVisibility(View.GONE);
                    tenPercent = (float) (0.1* tot);
                    del = tot + tenPercent ;
                    grandTotalText.setText(String.valueOf("Grand Total: ₹"+tot +"+"+ tenPercent+"= ₹"+del));
                    gettotal = String.valueOf(del);


                }else {
                    minimumOrderText.setVisibility(View.GONE);
                    delivery.setText("Free delivery over ₹999");
                    tenPercent = (float) (0.1* tot);

                    grandTotalText.setText(String.valueOf("Grand Total: ₹"+ tot));
                    gettotal = String.valueOf(tot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
