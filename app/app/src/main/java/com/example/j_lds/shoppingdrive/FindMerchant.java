package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.object_class.Merchant;
import com.example.j_lds.shoppingdrive.object_class.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindMerchant extends AppCompatActivity {
    private RecyclerView recyclerView_findMerchant;
    private RecyclerView.Adapter adapter;

    private Button back_btn;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabaseReference;

    private ArrayList<Merchant> merchants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_merchant);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back_btn = (Button)findViewById(R.id.button_back_from_find_merchant_to_login);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                back();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        recyclerView_findMerchant = (RecyclerView)findViewById(R.id.recyclerView_findMerchant);
        recyclerView_findMerchant.setHasFixedSize(true);
         recyclerView_findMerchant.setLayoutManager(new LinearLayoutManager(this));
//        layoutManager = new GridLayoutManager(this, 2);
//        recyclerView_findMerchant.setLayoutManager(layoutManager);

        merchants = new ArrayList<Merchant>();
        getMerchantDbData();

        adapter = new FindMerchantAdapter(merchants);
        recyclerView_findMerchant.setAdapter(adapter);

    }

    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(this, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    public void getMerchantDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        //Log.d("Firebase : ", user.getRole());

                        if (user.getRole().equals("Merchant")) {
                            Merchant merchant = new Merchant();
                            merchant.setId(ds.getKey());
                            merchant.setCompanyName(user.getCompanyName());
                            merchant.setCompanyLogo(user.getCompanyLogo());

                            Log.d("Merchant id key : ", "||=> "+merchant.getId());
                            merchants.add(merchant);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "ERROR : "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void viewUserBasket_from_FindMerchant(View view){
        Intent intent= new Intent(FindMerchant.this, UserBasket.class);
        intent.putExtra("where_the_user_was", FindMerchant.class.getSimpleName());
        startActivity(intent);
    }

    private void back(){
        Toast.makeText(getBaseContext(), "Login", Toast.LENGTH_LONG).show();

        Intent intent= new Intent(FindMerchant.this, Login.class);
        startActivity(intent);
    }

    public void viewMerchantArticles(){
        Toast.makeText(getBaseContext(), "getting articles", Toast.LENGTH_LONG).show();

        Intent intent= new Intent(FindMerchant.this, FindMerchantArticles.class);
        startActivity(intent);
    }
}
