package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailArticle extends AppCompatActivity {

    private TextView showBasketInfo;

    private Button showBasketButton, back;

    private RecyclerView mRecycleView;
    private DetailArticleAdapter mDetailArticleAdapter;

    private String[] article_names = {"Article 1", "Article 2", "Article 3", "Article 4", "Article 5",
            "Article 6", "Article 7", "Article 8", "Article 9", "Article 10"};
    private double[] article_prices = {2.30, 4.50, 1.20, 3.50, 5.60, 7.99, 9.39, 6.49, 8.99, 10.01};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //set recycle view / get my basket "if I have"..............................................
        showBasketInfo = (TextView)findViewById(R.id.textView_basket_recycleView_info);

        mRecycleView = (RecyclerView)findViewById(R.id.recyclerView_basket);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mDetailArticleAdapter = new DetailArticleAdapter(this, article_names, article_prices);


        back = (Button)findViewById(R.id.button_back_from_detail_article_to_find_merchant_articles);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_from_DetailArticle_to_merchantArticles();
            }
        });

        showBasketButton = (Button)findViewById(R.id.Button_detailArticle_arrow_down_black);
        showBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownBasketList();
            }
        });


    }

    public void dropDownBasketList(){
        Toast.makeText(getBaseContext(),"Show my basket",Toast.LENGTH_LONG).show();

        showBasketButton.setOnClickListener(new View.OnClickListener() {
            boolean btn_down = true;
            @Override
            public void onClick(View v) {
                if (btn_down){
                    showBasketInfo.setText("The basket has "+article_names.length+" articles");
                    mRecycleView.setVisibility(View.VISIBLE);
                    showBasketButton.setRotation(180);
                    mRecycleView.setAdapter(mDetailArticleAdapter);
                    btn_down = false;
                }else{
                    mRecycleView.setVisibility(View.GONE);
                    showBasketButton.setRotation(0);
                    btn_down = true;
                }
            }
        });
    }

    public void back_from_DetailArticle_to_merchantArticles(){
        Intent intent= new Intent(DetailArticle.this, FindMerchantArticles.class);
        startActivity(intent);
    }
}
