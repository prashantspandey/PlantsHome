package com.prashantpandey.nurseryapp;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mainReference, extrasReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference plantImages;
    private ArrayAdapter<IdealPlant> searchAdapter;
    private RecyclerView mainRecyclerView;
    private ListView searchListView;
    private DatabaseReference newPlant, potReference, postExtraReference;
    private FirebaseRecyclerAdapter<IdealPlant, IdealPlatViewHolder> plantRecyclerAdapter;
    private FirebaseRecyclerAdapter<IdealPot, IdealPotViewHolder> potRecyclerAdapter;
    private FirebaseRecyclerAdapter<IdealExtras, IdealExtras.IdealExtraViewHolder> extraRecyclerAdapter;
    private ProgressDialog progressDialog;
    private Query categoryQuery;
    private String filterSize;
    private SearchView searchView;
    private FilterDialog dialog;
    private ArrayList<IdealPlant> plantNames;
    private ShowImageDialog imgdialog;
    private String price;
    private int psmall;
    private int pmedium;
    private int plarge;
    private int pxlarge,pxxlarte;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> categoryList;
    private String recycleState = "nothing";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView navAllPlants, navMyCart, navProfile, navallPots, navLogout, navMyOrders, navFertilizers, navHelp;
    private DrawerLayout mainDrawer;
    private ProgressBar progressBar;


    public static final String FILTER_SIZE = "FILTER_SIZE";
    private Query mainAlphaQuery;
    private Query herbQuery;
    private Query shrubQuery;
    private Query flowerQuery;
    private Query treeQuery;
    private Query palmQuery;
    private Query fruitQuery;
    private Query grassQuery;
    private Query bonsaiQuery;
    private Spinner categorySpinner;
    private Query ornamentalQuery;
    private Query fertilizerQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Toolbar category = (Toolbar) findViewById(R.id.category_toolbar);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        category.inflateMenu(R.menu.main_category);
        category.setTitle("Shop By");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipeRefresh);
       navAllPlants = (TextView) findViewById(R.id.navigation_drawer_allplantsButton);
        navMyCart = (TextView) findViewById(R.id.navigation_drawer_cartButton);
        navProfile = (TextView) findViewById(R.id.navigation_drawer_profileButton);
        navLogout = (TextView) findViewById(R.id.navigation_drawer_logoutButton);
        navallPots = (TextView) findViewById(R.id.navigation_drawer_potsButton);
        mainDrawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navMyOrders = (TextView) findViewById(R.id.navigation_drawer_orderButton);
        navFertilizers = (TextView)findViewById(R.id.navigation_drawer_fertilizerButton);
        navHelp = (TextView) findViewById(R.id.navigation_drawer_helpButton);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);

        navallPots.setOnClickListener(this);
        navMyCart.setOnClickListener(this);
        navLogout.setOnClickListener(this);
        navProfile.setOnClickListener(this);
        navAllPlants.setOnClickListener(this);
        navLogout.setOnClickListener(this);
        navMyOrders.setOnClickListener(this);
        navFertilizers.setOnClickListener(this);
        navHelp.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progressBar);
        progressBar.setVisibility(View.GONE);


        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };

        mainRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        mainReference = FirebaseDatabase.getInstance().getReference().child("Plants");
        mainAlphaQuery = mainReference.orderByChild("commonName");
        plantImages = FirebaseStorage.getInstance().getReference().child("PlantImages");
        potReference = FirebaseDatabase.getInstance().getReference().child("Pots");
        extrasReference = FirebaseDatabase.getInstance().getReference().child("Extras");
        categoryQuery = mainReference.orderByChild("commonName").startAt("G");


        mainReference.keepSynced(true);
        newPlant = mainReference.push();
        postExtraReference = extrasReference.push();

        filterSize = getIntent().getStringExtra(FILTER_SIZE);
        Toast.makeText(MainActivity.this, filterSize, Toast.LENGTH_SHORT).show();

        plantNames = new ArrayList<>();
        mainReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    plantNames.add(snapshot.getValue(IdealPlant.class));
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });
        searchAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, plantNames);
        searchListView = (ListView) findViewById(R.id.activity_main_searchListView);

        searchListView.setAdapter(searchAdapter);
        searchListView.setVisibility(View.GONE);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final IdealPlant plant = searchAdapter.getItem(position);
                assert plant != null;
                final String namePlant = plant.getCommonName();
                final Query listQuery = mainReference.orderByChild("commonName").equalTo(namePlant);

                listQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getValue().toString();
                        String val = key.substring(1, 21);

                        Intent individualPlant = new Intent(MainActivity.this, IndividualPlantActivity.class);
                        individualPlant.putExtra("PlantId", val);
                        startActivity(individualPlant);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        categoryList = new ArrayList<>();
        categoryList.add("All Plants");
        categoryList.add("Ornamental");
        categoryList.add("Herbs");
        categoryList.add("Shrubs");
        categoryList.add("Flowering/Seasonal Plants");
        categoryList.add("Tree");
        categoryList.add("Palms");
        categoryList.add("Fruits");
        categoryList.add("Grass");
        categoryList.add("Bonsai");
        categoryList.add("Pots");
        categoryList.add("Decorative Stones/Pebbles");
        categoryList.add("Fertilizers");


        spinnerAdapter = new ArrayAdapter<>(this, R.layout.category_spinner);
        spinnerAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
        spinnerAdapter.addAll(categoryList);
        categorySpinner.setAdapter(spinnerAdapter);
        chooseCategory();
        refreshRecyclerView();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainDrawer.isDrawerOpen(GravityCompat.START))
                    mainDrawer.closeDrawer(GravityCompat.START);
                else
                    mainDrawer.openDrawer(GravityCompat.START);
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.navigation_drawer_allplantsButton) {
            try {
                setUpPlantAdapter(mainAlphaQuery);
                mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);
                recycleState = "All Plants";
                categorySpinner.setSelection(categoryList.indexOf("All Plants"), true);
                mainDrawer.closeDrawer(GravityCompat.START);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
            }
            Handler doagain = new Handler();
            doagain.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        setUpPlantAdapter(mainAlphaQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);
                        recycleState = "All Plants";
                        categorySpinner.setSelection(categoryList.indexOf("All Plants"), true);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        }
        if (id == R.id.navigation_drawer_potsButton) {
            try {
                setUpPotAdapter(potReference);
                mainRecyclerView.setAdapter(potRecyclerAdapter);
                recycleState = "Pots";
                categorySpinner.setSelection(categoryList.indexOf("Pots"), true);
                mainDrawer.closeDrawer(GravityCompat.START);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
            }
            Handler doagain = new Handler();
            doagain.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        setUpPotAdapter(potReference);
                        mainRecyclerView.setAdapter(potRecyclerAdapter);
                        recycleState = "Pots";
                        categorySpinner.setSelection(categoryList.indexOf("Pots"), true);
                        mainDrawer.closeDrawer(GravityCompat.START);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        }
        if (id == R.id.navigation_drawer_fertilizerButton){
            try {
                setUpPotAdapter(extrasReference);
                mainRecyclerView.setAdapter(extraRecyclerAdapter);
                recycleState = "Pebbles";
                categorySpinner.setSelection(categoryList.indexOf("Decorative Stones/Pebbles"), true);
                mainDrawer.closeDrawer(GravityCompat.START);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
            }
            Handler doagain = new Handler();
            doagain.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        setUpPotAdapter(extrasReference);
                        mainRecyclerView.setAdapter(extraRecyclerAdapter);
                        recycleState = "Pebbles";
                        categorySpinner.setSelection(categoryList.indexOf("Decorative Stones/Pebbles"), true);
                        mainDrawer.closeDrawer(GravityCompat.START);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Trying to complete the task", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        }
        if (id == R.id.navigation_drawer_cartButton) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
            mainDrawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.navigation_drawer_profileButton) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            mainDrawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.navigation_drawer_orderButton) {
            startActivity(new Intent(MainActivity.this, UserOrderActivity.class));
            mainDrawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.navigation_drawer_logoutButton) {
            mainDrawer.closeDrawer(GravityCompat.START);
            signOut();
        }
        if (id == R.id.navigation_drawer_helpButton){
            startActivity(new Intent(MainActivity.this,HelpActivity.class));
            mainDrawer.closeDrawer(GravityCompat.START);
        }
    }

    public static class IdealPlatViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView postImage;


        public IdealPlatViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setScientificName(String scientificName) {
            TextView postScientificName = (TextView) mView.findViewById(R.id.view_scientificName);
            postScientificName.setText(scientificName);

        }

        public void setCommonName(String commonName) {
            TextView postCommonName = (TextView) mView.findViewById(R.id.view_CommonName);
            postCommonName.setText(commonName);

        }

        public void setPrice(String price) {
            TextView postPrice = (TextView) mView.findViewById(R.id.view_Price);
            postPrice.setText(price);

        }

        public void setCategory(String category) {
            TextView postCategory = (TextView) mView.findViewById(R.id.view_Category);
            postCategory.setText(category);
        }

        public void setImage(Context context, String imageUrl) {
            postImage = (ImageView) mView.findViewById(R.id.view_postImage);
            Picasso.with(context).load(imageUrl).into(postImage);
        }

    }

    public static class IdealPotViewHolder extends RecyclerView.ViewHolder {

        private View potView;
        private ImageView potImage;

        public IdealPotViewHolder(View itemView) {
            super(itemView);
            potView = itemView;
        }

        public void setType(String type) {
            TextView typeText = (TextView) potView.findViewById(R.id.potview_typeView);
            typeText.setText(type);
        }

        public void setSizes(String sizes) {
            TextView sizeText = (TextView) potView.findViewById(R.id.potview_sizeView);
            sizeText.setText(sizes);
        }

        public void setBasicPrice(String basicPrice) {
            TextView basicPriceText = (TextView) potView.findViewById(R.id.potview_priceview);
            basicPriceText.setText(basicPrice);
        }

        public void setPotImage(Context cont, String img) {
            potImage = (ImageView) potView.findViewById(R.id.potview_imageview);
            Picasso.with(cont).load(img).into(potImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

            chooseCategory();
            setUpRecyclerView();


            mAuth.addAuthStateListener(mAuthListener);

            progressDialog.setMessage("Loading... Please wait.");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);


            mainReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.findItem(R.id.main_menu_search);
        MenuItem addplant = menu.findItem(R.id.main_menu_add);
        MenuItem addpot = menu.findItem(R.id.main_menu_addpot);
        MenuItem addextras = menu.findItem(R.id.main_menu_addExtra);
        MenuItem order = menu.findItem(R.id.main_menu_Orders);
        try {
            if (mAuth.getCurrentUser().getUid().equals("6bOFY9HxhfMDoR6jQ2xQsg31wqs2") ||
                    mAuth.getCurrentUser().getUid().equals("VqeXxXupGtUSJx2TFnM0OfFZdx72") ||
                    mAuth.getCurrentUser().getUid().equals("MeQaq0gAkOW8don0ylWoPYppx9t2")) {
                addplant.setVisible(true);
                addpot.setVisible(true);
                addextras.setVisible(true);
                order.setVisible(true);
            } else {
                addplant.setVisible(false);
                addpot.setVisible(false);
                addextras.setVisible(false);
                order.setVisible(false);
            }
        }catch (Exception e){
            Log.e("option error","uid error");
        }

        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  return false;
                                              }


                                              @Override
                                              public boolean onQueryTextChange(String newText) {
                                                  searchAdapter.getFilter().filter(newText);
                                                  searchListView.setVisibility(View.VISIBLE);


                                                  Handler handler = new Handler();
                                                  handler.postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          searchListView.setVisibility(View.GONE);
                                                      }
                                                  }, 3500);

                                                  try {
                                                      if (newText != null) {

                                                          String searchText = newText.toLowerCase();

                                                          Query searchq = mainReference.orderByChild("commonName").startAt(searchText);
                                                          setUpPlantAdapter(searchq);

                                                          mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);
                                                      } else {
                                                          setUpPlantAdapter(mainReference);

                                                          mainRecyclerView.setAdapter(plantRecyclerAdapter);

                                                      }

                                                  } catch (Exception e) {
                                                      Log.e("tt", "erased");
                                                  }
                                                  return true;
                                              }
                                          }
        );
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.main_menu_add) {


            Intent addPlants = new Intent(MainActivity.this, AddPlants.class);
            startActivity(addPlants);


        }
        if (item.getItemId() == R.id.main_menu_logout) {
            signOut();
        }
        if (item.getItemId() == R.id.main_menu_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.main_menu_filter) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            dialog = new FilterDialog();
            dialog.show(transaction, null);
            dialog.setFilterBySize(new FilterDialog.OnDialogFilter() {
                @Override
                public void finish(String result) {
                    Toast.makeText(MainActivity.this, "Results filtered by Size - " + result, Toast.LENGTH_SHORT).show();
                    if (result.equals("Small")) {

                        categoryQuery = mainReference.orderByChild("small").equalTo(true);
                        setUpPlantAdapter(categoryQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);

                    }
                    if (result.equals("Medium")) {

                        categoryQuery = mainReference.orderByChild("medium").equalTo(true);
                        setUpPlantAdapter(categoryQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);

                    }
                    if (result.equals("Large")) {

                        categoryQuery = mainReference.orderByChild("large").equalTo(true);
                        setUpPlantAdapter(categoryQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter, true);

                    }
                    if (result.equals("X-Large")){
                        categoryQuery = mainReference.orderByChild("xLarge").equalTo(true);
                        setUpPlantAdapter(categoryQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter,true);
                    }
                    if (result.equals("XX-Large")){
                        categoryQuery = mainReference.orderByChild("xxLarge").equalTo(true);
                        setUpPlantAdapter(categoryQuery);
                        mainRecyclerView.swapAdapter(plantRecyclerAdapter,true);
                    }

                }
            });

            return true;
        }
        if (item.getItemId() == R.id.main_menu_removeFilter) {
            try {
                setUpPlantAdapter(mainReference);
                mainRecyclerView.setAdapter(plantRecyclerAdapter);
            }catch (Exception e){
                Log.e("problem","remove filter");
            }
        }
        if (item.getItemId() == R.id.main_menu_addpot) {
            Intent potIntent = new Intent(MainActivity.this, AddPots.class);
            startActivity(potIntent);
        }
        if (item.getItemId() == R.id.main_menu_addExtra) {
            IdealExtras fancyStone = new IdealExtras("Vermi Compost","100","20","vermi compost","http://2.wlimg.com/product_images/bc-full/dir_33/962497/vermicompost-manure-1385214.jpg","fertilizer");
            postExtraReference.setValue(fancyStone);
        }
        if (item.getItemId() == R.id.main_menu_Orders) {
            Intent orderIntent = new Intent(MainActivity.this, OrderVendor.class);
            startActivity(orderIntent);
        }
        return super.onOptionsItemSelected(item);

    }

    public void signOut() {
        mAuth.signOut();
        try {
            LoginManager.getInstance().logOut();
        }catch (Exception e){
            Log.e("error","signout error");
        }



    }


    public void setUpPlantAdapter(final Query reference) {

        plantRecyclerAdapter = new FirebaseRecyclerAdapter<IdealPlant, IdealPlatViewHolder>(IdealPlant.class
                , R.layout.mainrecyclerview_row, IdealPlatViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(IdealPlatViewHolder viewHolder, final IdealPlant model, int position) {
                try {
                    final String postKey = getRef(position).getKey();
                    boolean sm,me,la,xl,xxl;
                    sm= model.isSmall();
                    me = model.isMedium();
                    la = model.isLarge();
                    xl = model.isxLarge();
                    xxl = model.isXxLarge();
                    if (!sm&&!me&&!la&&!xl&&!xxl){
                        viewHolder.setPrice("Basic Price: ₹" + model.getPriceOther());
                    }else {

                        if (model.getPriceXLarge().equals("")) {
                            pxlarge = 50000;
                        } else {
                            pxlarge = Integer.parseInt(model.getPriceXLarge());
                        }
                        if (model.getPriceXXLarge().equals("")) {
                            pxxlarte = 500000;
                        } else {
                            pxxlarte = Integer.parseInt(model.getPriceXXLarge());
                        }
                        if (model.getPriceSmall().equals("")) {
                            psmall = 50000;
                         } else {
                            psmall = Integer.parseInt(model.getPriceSmall());
                        }
                        if (model.getPriceMedium().equals("")) {
                            pmedium = 50000;
                        } else {
                            pmedium = Integer.parseInt(model.getPriceMedium());
                        }
                        if (model.getPriceLarge().equals("")) {
                            plarge = 50000;
                        } else {
                            plarge = Integer.parseInt(model.getPriceLarge());
                        }
                        int small = Math.min(Math.min(pmedium, plarge), Math.min(pxlarge, pxxlarte));
                        int smallest = Math.min(psmall, small);
                        price = String.valueOf(smallest);
                        viewHolder.setPrice("Basic Price: ₹" + price);
                    }

                    viewHolder.setCommonName(model.getCommonName().substring(0, 1).toUpperCase() + model.getCommonName().substring(1));
                    viewHolder.setScientificName("Scientific Name: " + model.getScientificName());

                    viewHolder.setCategory("Category: " + model.getCategory());
                    viewHolder.setImage(getApplicationContext(), model.getImageUrl());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent individualPlant = new Intent(MainActivity.this, IndividualPlantActivity.class);
                            individualPlant.putExtra("PlantId", postKey);
                            startActivity(individualPlant);
                        }
                    });

                    viewHolder.postImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent individualPlant = new Intent(MainActivity.this, IndividualPlantActivity.class);
                            individualPlant.putExtra("PlantId", postKey);
                            startActivity(individualPlant);
                        }
                    });

                    viewHolder.postImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Bundle imageLink = new Bundle();
                            imageLink.putString("imageLink", model.getImageUrl());
                            FragmentTransaction transac = getFragmentManager().beginTransaction();
                            imgdialog = new ShowImageDialog();
                            imgdialog.setArguments(imageLink);
                            imgdialog.show(transac, null);
                            return true;
                        }
                    });


                }catch (Exception e){
                    Log.e("start error","useless");
                }


            }


        };

    }

    public void setUpPotAdapter(Query reference) {
        potRecyclerAdapter = new FirebaseRecyclerAdapter<IdealPot, IdealPotViewHolder>(IdealPot.class,
                R.layout.potrecyclerview_row, IdealPotViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(IdealPotViewHolder viewHolder, final IdealPot model, int position) {
                String sixin, eightin, ninein, tenin, twelvein, fourteenin, sixteenin, eighteenin, twentyin;

                final String potKey = getRef(position).getKey();

                if (model.getPricesixInch().equals("")) {
                    sixin = "";
                    viewHolder.setBasicPrice("Basic Price: ₹" + model.getPricetenInch());

                } else {
                    sixin = " 6 Inch ,";
                    viewHolder.setBasicPrice("Basic Price: ₹" + model.getPricesixInch());
                }
                if (model.getPriceeightInch().equals("")) {
                    eightin = "";
                } else {
                    eightin = " 8 Inch ,";
                }
                if (model.getPricenineInch().equals("")) {
                    ninein = "";
                    viewHolder.setBasicPrice("Basic Price: ₹" + model.getPricenineInch());
                } else {
                    ninein = " 9 Inch ,";
                    viewHolder.setBasicPrice("Basic Price: ₹" + model.getPricenineInch());
                }
                if (model.getPricetenInch().equals("")) {
                    tenin = "";
                } else {
                    tenin = " 10 Inch ,";
                }
                if (model.getPricetwelveInch().equals("")) {
                    twelvein = "";
                } else {
                    twelvein = " 12 Inch ,";
                }
                if (model.getPricefourteenInch().equals("")) {
                    fourteenin = "";
                } else {
                    fourteenin = " 14 Inch ,";
                }
                if (model.getPricesixteenInch().equals("")) {
                    sixteenin = "";
                } else {
                    sixteenin = " 16 Inch ,";
                }
                if (model.getPriceeighteenInch().equals("")) {
                    eighteenin = "";
                } else {
                    eighteenin = " 18 Inch ,";
                }
                if (model.getPricetwentyInch().equals("")) {
                    twentyin = "";
                } else {
                    twentyin = " 20 Inch ,";
                }

                viewHolder.setPotImage(getApplicationContext(), model.getImageurl());
                viewHolder.setType("Type : " + model.getType());
                viewHolder.setSizes("Available Sizes: " + sixin + eightin + ninein + tenin + twelvein + fourteenin + sixteenin + eighteenin + twentyin);


                viewHolder.potView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent potactivity = new Intent(MainActivity.this, IndividualPotActivity.class);
                        potactivity.putExtra("PotKey", potKey);
                        startActivity(potactivity);
                    }
                });
                viewHolder.potImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle imageLink = new Bundle();
                        imageLink.putString("imageLink", model.getImageurl());
                        FragmentTransaction transac = getFragmentManager().beginTransaction();
                        imgdialog = new ShowImageDialog();
                        imgdialog.setArguments(imageLink);
                        imgdialog.show(transac, null);

                    }
                });
            }
        };
    }

    public void setUpExtraAdapter(Query reference) {
        extraRecyclerAdapter = new FirebaseRecyclerAdapter<IdealExtras, IdealExtras.IdealExtraViewHolder>(IdealExtras.class,
                R.layout.extrarecyclerview_row, IdealExtras.IdealExtraViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(IdealExtras.IdealExtraViewHolder viewHolder, final IdealExtras model, int position) {
                final String extraKey = getRef(position).getKey();

                viewHolder.setType("Type: " + model.getType());
                viewHolder.setImage(getApplicationContext(), model.getImageUrl());
                viewHolder.setName("Name: " + model.getName());
                viewHolder.setWeight("Basic Size/Weight: " + model.getWeight() + " Kg");
                viewHolder.setPrice("Price: " + model.getPrice());


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent extraactivity = new Intent(MainActivity.this, IndividualExtraActivity.class);
                        extraactivity.putExtra("ExtraKey", extraKey);
                        startActivity(extraactivity);
                    }
                });

            }
        };
    }


            public void chooseCategory() {

                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String categor = spinnerAdapter.getItem(position);
                        if (categor == null)
                            return;

                        if (categor.equals("Herbs")) {
                            progressBar.setVisibility(View.VISIBLE);
                            herbQuery = mainReference.orderByChild("category").equalTo("herb");
                            setUpPlantAdapter(herbQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Herbs";
                            herbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Ornamental")){
                            progressBar.setVisibility(View.VISIBLE);
                            ornamentalQuery = mainReference.orderByChild("category").equalTo("ornamental");
                            setUpPlantAdapter(ornamentalQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Ornamental";
                            ornamentalQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        if (categor.equals("All Plants")) {
                            setUpPlantAdapter(mainAlphaQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "All Plants";

                        }
                        if (categor.equals("Shrubs")) {
                            progressBar.setVisibility(View.VISIBLE);
                            shrubQuery = mainReference.orderByChild("category").equalTo("shrub");
                            setUpPlantAdapter(shrubQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Shrubs";
                            shrubQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Flowering/Seasonal Plants")) {
                            progressBar.setVisibility(View.VISIBLE);
                            flowerQuery = mainReference.orderByChild("category").equalTo("flowering/Seasonal Plant");
                            setUpPlantAdapter(flowerQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Flowering/Seasonal Plants";
                            flowerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Tree")) {
                            progressBar.setVisibility(View.VISIBLE);
                            treeQuery = mainReference.orderByChild("category").equalTo("tree");
                            setUpPlantAdapter(treeQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Tree";
                            treeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                        if (categor.equals("Palms")) {
                            progressBar.setVisibility(View.VISIBLE);
                            palmQuery = mainReference.orderByChild("category").equalTo("palm");
                            setUpPlantAdapter(palmQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            palmQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Fruits")) {
                            progressBar.setVisibility(View.VISIBLE);
                            fruitQuery = mainReference.orderByChild("category").equalTo("fruit");
                            setUpPlantAdapter(fruitQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Fruits";
                            fruitQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Grass")) {
                            progressBar.setVisibility(View.VISIBLE);
                            grassQuery = mainReference.orderByChild("category").equalTo("grass");
                            setUpPlantAdapter(grassQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Grass";
                            grassQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Bonsai")) {
                            progressBar.setVisibility(View.VISIBLE);
                            bonsaiQuery = mainReference.orderByChild("category").equalTo("bonsai");
                            setUpPlantAdapter(bonsaiQuery);
                            mainRecyclerView.setAdapter(plantRecyclerAdapter);
                            recycleState = "Bonsai";
                            bonsaiQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Pots")) {
                            progressBar.setVisibility(View.VISIBLE);
                            setUpPotAdapter(potReference);
                            mainRecyclerView.setAdapter(potRecyclerAdapter);
                            recycleState = "Pots";
                            potReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Decorative Stones/Pebbles")) {
                            progressBar.setVisibility(View.VISIBLE);
                            setUpExtraAdapter(extrasReference);
                            mainRecyclerView.setAdapter(extraRecyclerAdapter);
                            recycleState = "Pebbles";
                            extrasReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (categor.equals("Fertilizers")){
                            fertilizerQuery = extrasReference.orderByChild("type").equalTo("fertilizer");
                            progressBar.setVisibility(View.VISIBLE);
                            setUpExtraAdapter(fertilizerQuery);
                            mainRecyclerView.setAdapter(extraRecyclerAdapter);
                            recycleState = "Fertilizers";
                            fertilizerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        setUpPlantAdapter(mainReference);
                        mainRecyclerView.setAdapter(plantRecyclerAdapter);
                    }
                });

            }

            public void setUpRecyclerView() {
                if (recycleState.equals("Herbs")) {
                    setUpPlantAdapter(herbQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Ornamental")){
                    setUpPlantAdapter(ornamentalQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Shrubs")) {
                    setUpPlantAdapter(shrubQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Tree")) {
                    setUpPlantAdapter(treeQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Palms")) {
                    setUpPlantAdapter(palmQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Bonsai")) {
                    setUpPlantAdapter(bonsaiQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Flowering/Seasonal Plants")) {
                    setUpPlantAdapter(flowerQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Grass")) {
                    setUpPlantAdapter(grassQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Fruits")) {
                    setUpPlantAdapter(fruitQuery);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Pots")) {
                    setUpPotAdapter(potReference);
                    mainRecyclerView.setAdapter(potRecyclerAdapter);
                }
                if (recycleState.equals("nothing")) {
                    setUpPlantAdapter(mainReference);
                    mainRecyclerView.setAdapter(plantRecyclerAdapter);
                }
                if (recycleState.equals("Pebbles")) {
                    setUpExtraAdapter(extrasReference);
                    mainRecyclerView.setAdapter(extraRecyclerAdapter);
                }
                if (recycleState.equals("Fertilizers")){
                    setUpExtraAdapter(fertilizerQuery);
                    mainRecyclerView.setAdapter(extraRecyclerAdapter);
                }

                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Log.e("refresh", "some error");
                }

            }

            public void refreshRecyclerView() {
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setUpRecyclerView();
                    }
                });
            }


        }



