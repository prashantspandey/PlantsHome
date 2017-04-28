package com.prashantpandey.nurseryapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class UserOrderActivity extends AppCompatActivity {


    private RecyclerView userOrderRecyclerView;
    private FirebaseRecyclerAdapter<IdealOrder, IdealUserOrderHolder> userOrderAdapter;
    private DatabaseReference  orderReference;
    private Query myOrderQuery;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Toolbar userOrderToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        mAuth = FirebaseAuth.getInstance();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        myOrderQuery = orderReference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
        userOrderRecyclerView = (RecyclerView) findViewById(R.id.activity_user_order_recyclerView);
        userOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userOrderToolbar = (Toolbar) findViewById(R.id.activity_user_order_toolbar);
        progressBar = (ProgressBar) findViewById(R.id.activity_user_order_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        setUpUserOrderAdapter(myOrderQuery);
        userOrderRecyclerView.setAdapter(userOrderAdapter);
        setSupportActionBar(userOrderToolbar);

        getSupportActionBar().setTitle("My Orders");

        myOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class IdealUserOrderHolder extends RecyclerView.ViewHolder {
        private View userOrderView;
        private TextView confirmationText;

        public IdealUserOrderHolder(View itemView) {
            super(itemView);
            userOrderView = itemView;
        }

        public void setaddress(String address) {
            TextView addressText = (TextView) userOrderView.findViewById(R.id.user_order_addressView);
            addressText.setText(address);
        }

        public void setTime(String time) {
            TextView timeText = (TextView) userOrderView.findViewById(R.id.user_order_timeView);
            timeText.setText(time);
        }

        public void setname(String name) {
            TextView nameText = (TextView) userOrderView.findViewById(R.id.user_order_nameView);
            nameText.setText(name);
        }

        public void setOrder(String order) {
            TextView orderText = (TextView) userOrderView.findViewById(R.id.user_order_orViewView);
            orderText.setText(order);
        }

        public void setConfirmation(boolean confirmation) {
            confirmationText = (TextView) userOrderView.findViewById(R.id.user_order_confirmationView);
            if (confirmation) {
                confirmationText.setText("Order Confirmation: Order received by our Vendor.Your order will be with you soon.");
                confirmationText.setTextColor(Color.parseColor("#64DD17"));
            }

            else
                confirmationText.setText("Order Confirmation: Waiting for the vendor to respond...");


        }


        public void setDeliveryStatus(boolean deliveryStatus) {
            TextView deliveryText = (TextView) userOrderView.findViewById(R.id.user_order_deliverView);
            if (deliveryStatus) {
                deliveryText.setText("Delivery Status: Order Delivered");
                confirmationText.setText("Order Confirmation: Order Delivered");
                deliveryText.setTextColor(Color.parseColor("#64DD17"));
            }

            else
                deliveryText.setText("Delivery Status: Your Plants will be with you soon.Thanks for your patience and efforts for making the world greener.");

        }
        public void setAmount(String amount){
            TextView amountText = (TextView) userOrderView.findViewById(R.id.user_order_amountView);
            amountText.setText(amount);
        }
    }

    public void setUpUserOrderAdapter(final Query reference) {
        userOrderAdapter = new FirebaseRecyclerAdapter<IdealOrder, IdealUserOrderHolder>(IdealOrder.class, R.layout.userorder_recyclerview_row,
                IdealUserOrderHolder.class, reference) {
            @Override
            protected void populateViewHolder(IdealUserOrderHolder viewHolder, IdealOrder model, int position) {
                viewHolder.setname("Name: "+ model.getName());
                viewHolder.setTime("Time of Order: " + model.getTime());
                viewHolder.setaddress("Address: " + model.getAddress());
                viewHolder.setAmount("Total: ₹"+ model.getAmount());
                viewHolder.setConfirmation(model.isConfirmation());
                viewHolder.setDeliveryStatus(model.isDeliveryStatus());
                viewHolder.setOrder("Ordered Items: "+ model.getOrder());

                if (position%2==0){
                    viewHolder.userOrderView.setBackgroundColor(Color.parseColor("#000000"));
                }else {
                    viewHolder.userOrderView.setBackgroundColor(Color.parseColor("#212121"));
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_user_order,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id== R.id.user_order_menu_mainPage){
            startActivity(new Intent(UserOrderActivity.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

