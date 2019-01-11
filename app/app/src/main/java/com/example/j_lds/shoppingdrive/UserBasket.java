package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserBasket extends AppCompatActivity {

    private String WhereTheUserWasClass;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basket);

        WhereTheUserWasClass = getIntent().getStringExtra("where_the_user_was");

        btn_back = (Button)findViewById(R.id.button_back_from_basket_to_whereTheUserWas);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWhereTheUserWas(WhereTheUserWasClass);
            }
        });
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
