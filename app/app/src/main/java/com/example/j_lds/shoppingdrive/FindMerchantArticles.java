package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

public class FindMerchantArticles extends AppCompatActivity {

    private GridView gridView_findMerchantAticles;
    private FindMerchantArticlesAdapter FindMerchantArticlesAdapter;

    private int[] imageView_articles = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10};
    private String[] article_names = {"Merchant 1", "Merchant 2", "Merchant 3", "Merchant 4", "Merchant 5",
            "Merchant 6", "Merchant 7", "Merchant 8", "Merchant 9", "Merchant 10"};
    private double[] article_prices = {1.30, 2.50, 3.20, 4.50, 5.60, 6.99, 7.39, 8.49, 9.99, 10.01};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_merchant_articles);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        gridView_findMerchantAticles = (GridView)findViewById(R.id.gridView_merchant_articles);

        FindMerchantArticlesAdapter = new FindMerchantArticlesAdapter(this, imageView_articles, article_names, article_prices);

        gridView_findMerchantAticles.setAdapter(FindMerchantArticlesAdapter);

    }

    public void back_from_merchantArticles_to_merchantList(View view){
        Toast.makeText(getBaseContext(), "getting articles", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(FindMerchantArticles.this, FindMerchant.class);
        startActivity(intent);
    }

    public void viewArticle(View view){
        Intent intent = new Intent(FindMerchantArticles.this, DetailArticle.class);
        startActivity(intent);

        Toast.makeText(getBaseContext(),"view this article",Toast.LENGTH_LONG).show();
    }

}
