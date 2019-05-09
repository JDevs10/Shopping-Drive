package com.example.j_lds.shoppingdrive;

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
import android.view.WindowManager;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.databaseOffline.DatabaseHelper;
import com.example.j_lds.shoppingdrive.databaseOffline.model.Settings;
import com.example.j_lds.shoppingdrive.fragments.FragmentFindMerchant;
import com.example.j_lds.shoppingdrive.fragments.FragmentFindMerchantArticles;
import com.example.j_lds.shoppingdrive.fragments.FragmentProfile;
import com.example.j_lds.shoppingdrive.fragments.FragmentUserBasket;
import com.example.j_lds.shoppingdrive.fragments.FragmentUserOrders;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = Home.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private String merchantUid;
    private String merchantCompanyName;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        //reset data settings
        db.deleteSettingsData(1);
        db.defaultSettings();

        //Hides App bar at the top..................................................................
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolbar);
        //starting with FragmentFindMerchant()
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
            toolbar.setTitle("Stores");
            navigationView.setCheckedItem(R.id.nav_store);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchant()).commit();
        }
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
                toolbar.setTitle("");
                navigationView.setCheckedItem(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentProfile()).commit();
                break;

            case R.id.nav_store:
                toolbar.setTitle("Stores");
                navigationView.setCheckedItem(R.id.nav_store);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchant()).commit();
                break;

            case R.id.nav_shelves:
                if(getMerchantUidFromDB()) {
                    toolbar.setTitle(merchantCompanyName+"'s Products");
                    navigationView.setCheckedItem(R.id.nav_shelves);
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchantArticles()).commit();
                }else{
                    Toast.makeText(this, "Please select a store !!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_basket:
                toolbar.setTitle("My Basket");
                navigationView.setCheckedItem(R.id.nav_basket);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentUserBasket()).commit();
                break;

            case R.id.nav_orders:
                toolbar.setTitle("My Orders");
                navigationView.setCheckedItem(R.id.nav_orders);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentUserOrders()).commit();
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











































