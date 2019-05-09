package com.example.j_lds.shoppingdrive.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.Checkout;
import com.example.j_lds.shoppingdrive.Login;
import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.adapters.UserBasketAdapter;
import com.example.j_lds.shoppingdrive.object_class.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentUserBasket extends Fragment {
    private String TAG = FragmentUserBasket.class.getSimpleName();
    private View view;
    private Context mContext;

    private Button btn_sendOrder;

    private RecyclerView mRecycleView;
    private UserBasketAdapter mUserBasketAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabaseReference;

    private ArrayList<Article> articleBasket;
    private String selectedMerchanteUid = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basket, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView_basket_myBasketList);

        btn_sendOrder = (Button) view.findViewById(R.id.button_sendMyOrder);
        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        articleBasket = new ArrayList<Article>();
        getCurrentUserBasketDbData();
        mUserBasketAdapter = new UserBasketAdapter(articleBasket);
        mRecycleView.setAdapter(mUserBasketAdapter);
    }
    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(mContext, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(mContext, Login.class);
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
                Toast.makeText(mContext, "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendOrder(){
        Intent intent= new Intent(mContext, Checkout.class);
        startActivity(intent);
    }


}
