package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.adapters.FindMerchantArticles;
import com.example.j_lds.shoppingdrive.object_class.Article;
import com.example.j_lds.shoppingdrive.object_class.UserBasketAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBasket extends AppCompatActivity {

    private String WhereTheUserWasClass;
    private Button btn_back, btn_sendOrder;

    private RecyclerView mRecycleView;
    private UserBasketAdapter mUserBasketAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabaseReference;

    private ArrayList<Article> articleBasket;
    private String selectedMerchanteUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basket);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        WhereTheUserWasClass = getIntent().getStringExtra("where_the_user_was");
        selectedMerchanteUid = getIntent().getExtras().getString("SelectedMerchantUid");

        mRecycleView = (RecyclerView)findViewById(R.id.recycleView_basket_myBasketList);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        articleBasket = new ArrayList<Article>();
        getCurrentUserBasketDbData();
        mUserBasketAdapter = new UserBasketAdapter(articleBasket);
        mRecycleView.setAdapter(mUserBasketAdapter);

        btn_back = (Button)findViewById(R.id.button_back_from_basket_to_whereTheUserWas);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWhereTheUserWas(WhereTheUserWasClass);
            }
        });

        btn_sendOrder = (Button)findViewById(R.id.button_sendMyOrder);
        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
    }

    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(this, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    private void getCurrentUserBasketDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+selectedMerchanteUid+"/basket");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleBasket.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(dataSnapshot.exists()){
                            Article article = ds.getValue(Article.class);
                            article.setId(ds.getKey());
                            Log.d("Basket Article id : ", "||=> "+article.getId());
                            Log.d("Basket Article name : ", "||=> "+article.getName());
                            Log.d("Basket Article image : ", "||=> "+article.getImage());
                            Log.d("Basket Article info : ", "||=> "+article.getDescription());

                            articleBasket.add(article);
                            mUserBasketAdapter.notifyDataSetChanged();
                        }else{
                            Log.d("Article Error : ", "Can't find articles !!!");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendOrder(){
        Intent intent= new Intent(UserBasket.this, Checkout.class);
        startActivity(intent);
    }

    private void checkWhereTheUserWas(String class_name){
        Intent intent;
        switch (class_name){
            case "FindMerchant":
                intent= new Intent(UserBasket.this, FindMerchant.class);
                startActivity(intent);
                break;
            case "FindMerchantArticles":
                intent= new Intent(UserBasket.this, FindMerchantArticles.class);
                startActivity(intent);
                break;
            case "DetailArticle":
                intent= new Intent(UserBasket.this, DetailArticle.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(getBaseContext(), "Cannot find where the you where", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
