package com.example.shoppingdrive.Client.Client_Pages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_Adapters.Client_DetailArticleAdapter;
import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Client_DetailArticle extends AppCompatActivity {
    private String TAG = Client_DetailArticle.class.getSimpleName();
    private TextView showBasketInfo;

    private Button addArticleToBasket, showBasketButton;
    private ImageView back;

    private RecyclerView mRecycleView;
    private Client_DetailArticleAdapter mDetailArticleAdapterClient;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserUid;
    private DatabaseReference mdatabaseReference;

    private ArrayList<Product> articleBasket;
    private String selectedMerchanteUid = "";
    private String selectedArticleUid = "";
    private Product selectedArticle;

    private boolean btn_down = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_detail_article);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        selectedMerchanteUid = getIntent().getExtras().getString("SelectedMerchantUid");
        selectedArticleUid = getIntent().getExtras().getString("SelectedArticleUid");
        Log.d("selected Merchant Uid ", "||=> "+selectedMerchanteUid);
        Log.d("selected Article Uid ", "||=> "+selectedArticleUid);

        selectedArticle = new Product();
        getArticleDbData();

        articleBasket = new ArrayList<Product>();

        back = (ImageButton)findViewById(R.id.button_back_from_detail_article_to_find_merchant_articles);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { back_from_DetailArticle_to_merchantArticles();
            }
        });

        addArticleToBasket = (Button)findViewById(R.id.button_detail_article_addToBasket);
        addArticleToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticleToBasket(selectedArticle);
            }
        });
    }

    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUserUid = currentUser.getUid();
            //Toast.makeText(this, "Welcome "+currentUser.getEmail()+"\nUid: "+currentUser.getUid(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    // Get the selected Merchant articles from db
    public void getArticleDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/merchant/" + selectedMerchanteUid + "/products");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals(selectedArticleUid)) {
                            selectedArticle = ds.getValue(Product.class);
                            selectedArticle.setId(ds.getKey());
                            Log.d("Article id : ", "||=> " + selectedArticle.getId());
                            Log.d("Article name : ", "||=> " + selectedArticle.getName());
                            Log.d("Article image : ", "||=> " + selectedArticle.getImage());
                            Log.d("Article description : ", "||=> " + selectedArticle.getDescription());
                            showArticle(selectedArticle);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "ERROR 'Get Article' :\n" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Show the selected articles from the Merchant article list
    private void showArticle(Product article){
        ImageView articleImage = findViewById(R.id.imageView_detail_article);
        TextView articleName = findViewById(R.id.textView_detail_article_name);
        TextView articlePrice = findViewById(R.id.textView_detail_article_price);
        TextView articleDescription = findViewById(R.id.textView_detail_article_description);

        Picasso.get().load(article.getImage()).into(articleImage);
        articleName.setText(article.getName());
        articlePrice.setText(article.getPrice()+" €");
        articleDescription.setText(article.getDescription());

        //Log.d("Showing Article : ", article.getId()+"\nLoaded !");
        //Toast.makeText(getBaseContext(),"Article : "+article.getId()+"\nLoaded !",Toast.LENGTH_LONG).show();
    }

    //Add an article to the current user basket
    private void addArticleToBasket(final Product article){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/" + currentUserUid + "/basket");
        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e(TAG, " DataSnapshot children: "+dataSnapshot.getChildren().toString());
                    if (!dataSnapshot.hasChild(article.getId())){
                        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference();
                        article.setQuantity(1);
                        mdatabaseReference.child("user/client/" + currentUserUid + "/basket/"+article.getId()).setValue(article);

                        mdatabaseReference.child("user/client/" + currentUserUid + "/basket/"+article.getId()+"/merchantUid").setValue(selectedMerchanteUid);

                        Log.e(TAG, " Article "+article.getId());
                        Toast.makeText(Client_DetailArticle.this, "The article "+article.getName()+" is added to your basket.", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(Client_DetailArticle.this, "Article "+article.getName()+" is already in your basket.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference();
                    mdatabaseReference.child("user/client/" + currentUserUid + "/basket/"+article.getId()).setValue(article);
                    mdatabaseReference.child("user/client/" + currentUserUid + "/basket/"+article.getId()+"/merchantUid").setValue(selectedMerchanteUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCurrentUserArticleBasketDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+currentUserUid+"/basket");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                articleBasket.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(dataSnapshot.exists()){
                            Product article = ds.getValue(Product.class);
                            article.setId(ds.getKey());
                            Log.d("Basket Article id : ", "||=> "+article.getId());
                            Log.d("Basket Article name : ", "||=> "+article.getName());
                            Log.d("Basket Article image : ", "||=> "+article.getImage());
                            Log.d("Basket Article info : ", "||=> "+article.getDescription());

                            articleBasket.add(article);
                            mDetailArticleAdapterClient.notifyDataSetChanged();
                        }else{
                            Log.d("Article Error : ", "Can't find articles !!!");
                        }
                    }
                }
                if(articleBasket.size() > 0){
                    showBasketInfo.setText("The basket have "+articleBasket.size()+" articles");
                }else{
                    showBasketInfo.setText("The basket is empty, 0 articles");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void dropDownBasketList(){
        if (btn_down){
            getCurrentUserArticleBasketDbData();

            mRecycleView.setVisibility(View.VISIBLE);
            showBasketButton.setRotation(180);
            btn_down = false;
        }else{
            mRecycleView.setVisibility(View.GONE);
            showBasketButton.setRotation(0);
            btn_down = true;
        }
    }

    public void viewUserBasket_from_DetailArticle(View view){
        Intent intent= new Intent(Client_DetailArticle.this, Client_Home.class);
        intent.putExtra("where_the_user_was", Client_DetailArticle.class.getSimpleName());
        intent.putExtra("SelectedMerchantUid", selectedMerchanteUid);
        startActivity(intent);
    }

    public void back_from_DetailArticle_to_merchantArticles(){
        Intent intent= new Intent(Client_DetailArticle.this, Client_Home.class);
        intent.putExtra("SelectedMerchantUid", selectedMerchanteUid);
        startActivity(intent);
    }
}
