package com.example.j_lds.shoppingdrive;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.fragments.FragmentFindMerchant;
import com.example.j_lds.shoppingdrive.fragments.FragmentFindMerchantArticles;
import com.example.j_lds.shoppingdrive.fragments.FragmentProfile;
import com.example.j_lds.shoppingdrive.fragments.FragmentUserBasket;
import com.example.j_lds.shoppingdrive.fragments.FragmentUserOrders;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hides App bar at the top.................................................................
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolbar);
        //starting with FragmentFindMerchant()
        toolbar.setTitle("Stores");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_homeLayout);
        NavigationView navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            //default fragment when activity is running
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchant()).commit();
            navigationView.setCheckedItem(R.id.nav_store);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentProfile()).commit();
                break;
            case R.id.nav_store:
                toolbar.setTitle("Stores");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchant()).commit();
                break;
            case R.id.nav_shelves:
                toolbar.setTitle("Kyle's Products");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentFindMerchantArticles()).commit();
                break;
            case R.id.nav_basket:
                toolbar.setTitle("My Basket");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new FragmentUserBasket()).commit();
                break;
            case R.id.nav_orders:
                toolbar.setTitle("My Orders");
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
}











































