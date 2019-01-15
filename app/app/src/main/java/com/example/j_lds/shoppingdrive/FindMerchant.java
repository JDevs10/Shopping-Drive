package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.object_class.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FindMerchant extends AppCompatActivity {
    private RecyclerView recyclerView_findMerchant;
    private RecyclerView.Adapter adapter;

    private Button back_btn;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

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

        recyclerView_findMerchant = (RecyclerView)findViewById(R.id.recyclerView_findMerchant);
        recyclerView_findMerchant.setHasFixedSize(true);
        recyclerView_findMerchant.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FindMerchantAdapter();

        recyclerView_findMerchant.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
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

    public void viewMerchantArticles(View view){
        Toast.makeText(getBaseContext(), "getting articles", Toast.LENGTH_LONG).show();

        Intent intent= new Intent(FindMerchant.this, FindMerchantArticles.class);
        startActivity(intent);
    }
}
