package com.prashantpandey.nurseryapp;

import android.app.FragmentTransaction;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;




public class OrderVendor extends AppCompatActivity {

    private DatabaseReference cartReference,OrderReference;
    private Query newOrders,completedOrders;
    private FirebaseAuth mAuth;
    private RecyclerView orderRecyclerView;
    private FirebaseRecyclerAdapter<IdealOrder, IdealOrderHolder> orderAdapter;
    private VenderActionsDialog orderDialog;
    private DatabaseReference orderReference;
    private ProgressBar progressBar;
    private Button completedOrdersButton,newOrdersButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_vender);
        mAuth = FirebaseAuth.getInstance();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        newOrders = orderReference.orderByChild("deliveryStatus").equalTo(false);
        completedOrders = orderReference.orderByChild("deliveryStatus").equalTo(true);
        progressBar = (ProgressBar) findViewById(R.id.order_progressBar);
        orderRecyclerView = (RecyclerView) findViewById(R.id.activity_order_vendor_recyclerView);
        completedOrdersButton =(Button) findViewById(R.id.activity_order_vender_compltedOrdersButton);
        newOrdersButton = (Button) findViewById(R.id.activity_order_vender_newOrdersButton);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpOrderAdapter(newOrders);
        orderRecyclerView.setAdapter(orderAdapter);

        completedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                setUpOrderAdapter(completedOrders);
                orderRecyclerView.setAdapter(orderAdapter);
                completedOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        newOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                setUpOrderAdapter(newOrders);
                orderRecyclerView.setAdapter(orderAdapter);
                newOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpOrderAdapter(newOrders);
        orderRecyclerView.setAdapter(orderAdapter);
        progressBar.setVisibility(View.VISIBLE);
        newOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         completedOrders.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 progressBar.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }
    public static class IdealOrderHolder extends RecyclerView.ViewHolder{

        private View orderView;

        public IdealOrderHolder(View itemView) {
            super(itemView);
            orderView = itemView;
        }
        public void setName(String name){
            TextView nameText = (TextView) orderView.findViewById(R.id.order_vendor_nameText);
            nameText.setText(name);
        }
        public void setAddress(String address){
            TextView addText = (TextView) orderView.findViewById(R.id.order_vendor_addressText);
            addText.setText(address);
        }
        public void setPhoneNumber(String number){
            TextView phoneText = (TextView) orderView.findViewById(R.id.order_vendor_phoneText);
            phoneText.setText(number);
        }
        public void setOrder(String order){
            TextView orderText = (TextView) orderView.findViewById(R.id.order_vendor_orderText);
            orderText.setText(order);
        }
        public void setAmount(String amount){
            TextView amountText = (TextView) orderView.findViewById(R.id.order_vendor_amountText);
            amountText.setText(amount);
        }
        public void setTime(String time){
            TextView timeText = (TextView) orderView.findViewById(R.id.order_vendor_timeText);
            timeText.setText(time);
        }
        public void setConfirmationStatus(String confirmationStatus){
            TextView confirmationText = (TextView) orderView.findViewById(R.id.order_vendor_confirmationText);
            confirmationText.setText(confirmationStatus);

        }

    }
    public void setUpOrderAdapter(final Query reference){
        orderAdapter = new FirebaseRecyclerAdapter<IdealOrder, IdealOrderHolder>
                (IdealOrder.class, R.layout.order_recyclerview_row,IdealOrderHolder.class, reference) {
            @Override
            protected void populateViewHolder(IdealOrderHolder viewHolder, IdealOrder model, int position) {
                final String orderKey = getRef(position).getKey();
                viewHolder.setName("Name: "+ model.getName());
                viewHolder.setAddress("Address: "+ model.getAddress());
                viewHolder.setPhoneNumber("Phone: " + model.getPhone());
                viewHolder.setOrder("Order: " + model.getOrder().trim());
                viewHolder.setAmount("Amount: ₹"+ model.getAmount());
                viewHolder.setTime("Time: "+model.getTime());
                viewHolder.setConfirmationStatus("Confirmation Status: "+model.isConfirmation());

                if (position%2==0){
                    viewHolder.orderView.setBackgroundColor(Color.parseColor("#000000"));
                }else {
                    viewHolder.orderView.setBackgroundColor(Color.parseColor("#212121"));
                }

                if (!reference.equals(completedOrders)){
                viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction orderTransaction = getFragmentManager().beginTransaction();
                        orderDialog = new VenderActionsDialog();
                        orderDialog.show(orderTransaction,null);
                        orderDialog.setConfirmations(new VenderActionsDialog.vendorDialogInterface() {
                            @Override
                            public void confirmation(boolean confirmed) {

                                orderReference.child(orderKey).child("confirmation").setValue(confirmed);

                            }

                            @Override
                            public void delivered(boolean delivery) {
                                orderReference.child(orderKey).child("deliveryStatus").setValue(delivery);
                            }
                        });
                    }
                });
                }
            }
        };
    }

}












