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

public class FindMerchant extends AppCompatActivity {
    private RecyclerView recyclerView_findMerchant;
    private RecyclerView.Adapter adapter;

    private Button back_btn;

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
