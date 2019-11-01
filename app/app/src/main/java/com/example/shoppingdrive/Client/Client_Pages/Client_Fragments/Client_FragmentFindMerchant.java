package com.example.shoppingdrive.Client.Client_Pages.Client_Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.UserClient;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_Adapters.Client_FindMerchantAdapter;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.example.shoppingdrive.Models.Merchant;
import com.example.shoppingdrive.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Client_FragmentFindMerchant extends Fragment {
    private String TAG = Client_FragmentFindMerchant.class.getSimpleName();
    private View view;
    private Context mContext;

    private RecyclerView recyclerView_findMerchant;
    private RecyclerView.Adapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabaseReference;
    private String clientUid;
    private DatabaseHelper db;

    private ArrayList<Merchant> merchants;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(mContext);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            clientUid = currentUser.getUid();
        }
        else{
            Toast.makeText(mContext, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(mContext, Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_fragment_find_merchant, container, false);

        recyclerView_findMerchant = view.findViewById(R.id.fragment_find_merchant_recyclerView_findMerchant);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        merchants = new ArrayList<Merchant>();
        getMerchantDbData();

        adapter = new Client_FindMerchantAdapter(mContext, merchants);
        recyclerView_findMerchant.setHasFixedSize(true);
        recyclerView_findMerchant.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_findMerchant.setAdapter(adapter);
    }

    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(mContext, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(mContext, Login.class);
            startActivity(intent);
        }
    }


    public void getMerchantDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/merchant");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        //Log.d("Firebase : ", user.getRole());

                        if (user.getRole().equals("Merchant")) {
                            Merchant merchant = new Merchant();
                            merchant.setId(ds.getKey());
                            merchant.setCompanyName(user.getCompanyName());
                            merchant.setCompanyLogo(user.getCompanyLogo());

                            Log.d("Merchant id key : ", "||=> "+merchant.getId());
                            merchants.add(merchant);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "ERROR : "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}