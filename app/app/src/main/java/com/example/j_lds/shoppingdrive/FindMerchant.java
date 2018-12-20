package com.example.j_lds.shoppingdrive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

public class FindMerchant extends AppCompatActivity {
    private RecyclerView recyclerView_findMerchant;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_merchant);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView_findMerchant = (RecyclerView)findViewById(R.id.recyclerView_findMerchant);
        recyclerView_findMerchant.setHasFixedSize(true);
        recyclerView_findMerchant.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter();

        recyclerView_findMerchant.setAdapter(adapter);
    }

}
