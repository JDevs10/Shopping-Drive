package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DetailArticle extends AppCompatActivity {

    private ImageView show_basket_btn;
    private TextView tv_article_name;

    private RecyclerView mRecycleView;
    private ClientArticleBasketAdapter mClientArticleBasketAdapter;

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
        tv_article_name = (TextView)findViewById(R.id.textView_article_name);
        show_basket_btn = (ImageView) findViewById(R.id.arrow_down_list);

        mRecycleView = (RecyclerView)findViewById(R.id.recyclerView_drop_down_basket);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mClientArticleBasketAdapter = new ClientArticleBasketAdapter(this, article_names, article_prices);
    }

    public void back_from_DetailArticle_to_merchantArticles(View view){
        Toast.makeText(getBaseContext(),"going back to view articles",Toast.LENGTH_LONG).show();

        Intent intent= new Intent(DetailArticle.this, FindMerchantArticles.class);
        startActivity(intent);
    }

    public void dropDownBasketList(View view){
        Toast.makeText(getBaseContext(),"Show my basket",Toast.LENGTH_LONG).show();

        show_basket_btn.setOnClickListener(new View.OnClickListener() {
            boolean btn_down = true;
            @Override
            public void onClick(View v) {
                if (btn_down){
                    tv_article_name.setText("The basket has "+article_names.length+" articles");
                    mRecycleView.setVisibility(View.VISIBLE);
                    show_basket_btn.setRotation(180);
                    mRecycleView.setAdapter(mClientArticleBasketAdapter);
                    btn_down = false;
                }else{
                    mRecycleView.setVisibility(View.GONE);
                    show_basket_btn.setRotation(0);
                    btn_down = true;
                }
            }
        });
    }

//    public void getArticle() {
//        clientArticleBasket = new ArrayList<>();
//        List<ClientArticleBasket> articles = new ArrayList<>();
//
//        for (int i=0; i<4; i++){
//            articles.add(new ClientArticleBasket());
//            articles.add(new ClientArticleBasket());
//        }
//        clientArticleBasket.add();
//    }
}
