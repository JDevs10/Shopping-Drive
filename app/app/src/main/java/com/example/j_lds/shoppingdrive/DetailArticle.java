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

    private RecyclerView recyclerView_basket;
    private RecyclerView.Adapter adapter;

    private TextView showBasketInfo;

    private Button showBasketButton, back;

    private boolean active_basket;
    private DetailArticleAdapter daa = new DetailArticleAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = (Button)findViewById(R.id.button_back_from_detail_article_to_find_merchant_articles);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_from_DetailArticle_to_merchantArticles();
            }
        });

        showBasketInfo = (TextView)findViewById(R.id.textView_basket_recycleView_info);
        if(daa.getArticle_basket_names_length() == 0){
            showBasketInfo.setText("The basket is empty... "+daa.getArticle_basket_names_length()+" articles");
        }else{
            showBasketInfo.setText("The basket has "+daa.getArticle_basket_names_length()+" articles");
        }


        active_basket = false;
        showBasketButton = (Button)findViewById(R.id.Button_detailArticle_arrow_down_black);
        showBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBasket();
            }
        });


        recyclerView_basket = (RecyclerView)findViewById(R.id.recyclerView_basket);
        recyclerView_basket.setVisibility(View.GONE);
        recyclerView_basket.setHasFixedSize(true);
        recyclerView_basket.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DetailArticleAdapter();

        recyclerView_basket.setAdapter(adapter);
    }

    public void showBasket(){
        if (!active_basket){
            active_basket = true;
            Toast.makeText(getBaseContext(), "hello basket", Toast.LENGTH_LONG).show();
            recyclerView_basket.setVisibility(View.VISIBLE);
        }else{
            active_basket = false;
            Toast.makeText(getBaseContext(), "bye basket", Toast.LENGTH_LONG).show();
            recyclerView_basket.setVisibility(View.GONE);
        }
    }

    public void back_from_DetailArticle_to_merchantArticles(){
        Intent intent= new Intent(DetailArticle.this, FindMerchantArticles.class);
        startActivity(intent);
    }
}
