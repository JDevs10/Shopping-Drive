package com.example.shoppingdrive.Merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.Models.UserMerchant;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Merchant_MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {
    private Toolbar toolbar;
    private DrawerLayout drawer_layout;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String merchantUid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer_layout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.title_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Merchant_HomeFragment()).addToBackStack(null).commit();

            navigationView.setCheckedItem(R.id.navigation_home);
        }



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            merchantUid = currentUser.getUid();
        } else {
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + merchantUid);
        View headerView = navigationView.getHeaderView(0);

        final CircleImageView merchant_profile_image = headerView.findViewById(R.id.navigation_info_image);
        final TextView merchant_profile_email = headerView.findViewById(R.id.navigation_info_email);
        final TextView merchant_profile_fullname = headerView.findViewById(R.id.navigation_info_fullname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMerchant userMerchant = dataSnapshot.getValue(UserMerchant.class);
                //editName.setText(product.getName());
                //
                Log.e("merchantUid", merchantUid);
                Log.e("logo", userMerchant.getCompanyLogo());
                Picasso.get().load(userMerchant.getCompanyLogo()).into(merchant_profile_image);
                merchant_profile_email.setText(userMerchant.getEmail());
                merchant_profile_fullname.setText(userMerchant.getFirstname() + " " + userMerchant.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                toolbar.setTitle(R.string.title_home);
                selectedFragment = new Merchant_HomeFragment();
                break;
            case R.id.navigation_my_products:
                toolbar.setTitle(R.string.title_my_products);
                selectedFragment = new Merchant_MyProductsFragment();
                break;
            case R.id.navigation_commands:
                toolbar.setTitle(R.string.title_commands);
                selectedFragment = new Merchant_CommandsFragment();
                break;
            case R.id.navigation_my_profile:
                toolbar.setTitle(R.string.title_my_profile);
                selectedFragment = new Merchant_MyProfileFragment();
                break;
            case R.id.navigation_terms:
                Toast.makeText(this, "Our use of Conditions", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_privacy:
                Toast.makeText(this, "Our Legal Mentions", Toast.LENGTH_SHORT).show();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
