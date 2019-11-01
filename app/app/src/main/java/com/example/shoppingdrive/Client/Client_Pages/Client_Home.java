package com.example.shoppingdrive.Client.Client_Pages;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Merchant.Merchant_MainActivity;
import com.example.shoppingdrive.Models.UserClient;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.Model.Settings;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentFindMerchant;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentFindMerchantArticles;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfile;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentUserBasket;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentUserOrders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Client_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = Client_Home.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private String merchantCompanyName;
    private DatabaseHelper db;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String clientUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_home);

        db = new DatabaseHelper(this);
        //reset data settings
        db.deleteSettingsData(1);
        db.defaultSettings();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            clientUid = currentUser.getUid();
        }
        else{
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, Login.class);
            startActivity(intent);
        }

        //Hides App bar at the top..................................................................
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolbar);
        //starting with Client_FragmentFindMerchant()
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_homeLayout);
        navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle("Stores");
        if (savedInstanceState == null){
            //default fragment when activity is running
            navigationView.setCheckedItem(R.id.nav_store);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentFindMerchant()).commit();
        }
    }

    private void initMenuHeaderInfo(ViewGroup mViewGroup) {
        final TextView userName = mViewGroup.findViewById(R.id.nav_header_currentUserName);
        final TextView userEmail = mViewGroup.findViewById(R.id.nav_header_currentUserEmail);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClient client = dataSnapshot.getValue(UserClient.class);
                userName.setText(client.getFirstname()+" "+client.getLastname());
                userEmail.setText(client.getEmail());
                Log.e(TAG, " Side Menu || userName: "+userName.getText().toString()+" && userEmail: "+userEmail.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                //init Side Menu user information
                initMenuHeaderInfo(navigationView);

                toolbar.setTitle("");
                navigationView.setCheckedItem(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentProfile()).commit();
                break;

            case R.id.nav_store:
                //init Side Menu user information
                initMenuHeaderInfo(navigationView);

                toolbar.setTitle("Stores");
                navigationView.setCheckedItem(R.id.nav_store);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentFindMerchant()).commit();
                break;

            case R.id.nav_shelves:
                if(getMerchantUidFromDB()) {
                    //init Side Menu user information
                    initMenuHeaderInfo(navigationView);

                    toolbar.setTitle(merchantCompanyName+"'s Products");
                    navigationView.setCheckedItem(R.id.nav_shelves);
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentFindMerchantArticles()).commit();
                }else{
                    Toast.makeText(this, "Please select a store !!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_basket:
                //init Side Menu user information
                initMenuHeaderInfo(navigationView);

                toolbar.setTitle("My Basket");
                navigationView.setCheckedItem(R.id.nav_basket);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentUserBasket()).commit();
                break;

            case R.id.nav_orders:
                //init Side Menu user information
                initMenuHeaderInfo(navigationView);

                toolbar.setTitle("My Orders");
                navigationView.setCheckedItem(R.id.nav_orders);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentUserOrders()).commit();
                break;

            case R.id.nav_logout:
                Intent intent= new Intent(Client_Home.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_conditions:
                Toast.makeText(this, "Our use of Conditions", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_legalMentions:
                Toast.makeText(this, "Our Legal Mentions", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private boolean getMerchantUidFromDB(){
        Cursor res = db.getAllSettingsData();
        if (res.moveToFirst()){
            if(!res.getString(1).equals("") ){
                this.merchantCompanyName = res.getString(2);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        Log.e(TAG, "onDestroy 1");
        Settings settings = new Settings();
        settings.setId(1);
        settings.setSelectedMerchantUid("");
        settings.setSelectedMerchantCompanyName("");

        if (db.updateSettingsData(settings)){
            finish();
        }else{
            Log.e(TAG, "onDestroy: the ");
        }
        finish();
        Log.e(TAG, "onDestroy 3");
    }
}











































