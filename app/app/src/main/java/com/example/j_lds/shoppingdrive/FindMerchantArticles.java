package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.object_class.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindMerchantArticles extends AppCompatActivity {

    private RecyclerView recyclerView_findMerchantAticles;
    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mdatabaseReference;

    private ArrayList<Article> articleList;
    private String selectedMerchantUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_merchant_articles);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        selectedMerchantUid = getIntent().getExtras().getString("SelectedMerchantUid");
        Log.d("selected Merchant Uid ", "||=> "+selectedMerchantUid);

        recyclerView_findMerchantAticles = (RecyclerView)findViewById(R.id.recyclerView_merchant_articles);
        recyclerView_findMerchantAticles.setHasFixedSize(true);
        recyclerView_findMerchantAticles.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView_findMerchantAticles.setLayoutManager(layoutManager);

        articleList = new ArrayList<Article>();
        getMerchantArticleDbData();

        adapter = new FindMerchantArticlesAdapter(selectedMerchantUid, articleList);
        recyclerView_findMerchantAticles.setAdapter(adapter);
    }

    public void getMerchantArticleDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/merchant/"+selectedMerchantUid+"/articles");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(dataSnapshot.exists()){
                            Article article = ds.getValue(Article.class);
                            article.setId(ds.getKey());
                            Log.d("Article id : ", "||=> "+article.getId());
                            Log.d("Article name : ", "||=> "+article.getName());
                            Log.d("Article image : ", "||=> "+article.getImage());
                            Log.d("Article description : ", "||=> "+article.getDescription());

                            articleList.add(article);
                            adapter.notifyDataSetChanged();
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

    public void back_from_merchantArticles_to_merchantList(View view){
        Intent intent = new Intent(FindMerchantArticles.this, FindMerchant.class);
        startActivity(intent);
    }

    public void viewUserBasket_from_FindMerchantArticles(View view){
        Intent intent= new Intent(FindMerchantArticles.this, UserBasket.class);
        intent.putExtra("where_the_user_was", FindMerchantArticles.class.getSimpleName());
        intent.putExtra("SelectedMerchantUid", selectedMerchantUid);
        startActivity(intent);
    }

}
