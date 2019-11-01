package com.example.shoppingdrive.Merchant.Merchant_FragmentProfileFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.example.shoppingdrive.Models.UserMerchant;
import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Merchant_FragmentProfileInformation extends Fragment {

    private DatabaseReference databaseReference;
    private EditText editEmail;
    private EditText editCountryCode;
    private EditText editPhoneNumber;

    private Context mContext;

    private DatabaseHelper db;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String merchantUid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(mContext);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            merchantUid = currentUser.getUid();
        }
        else{
            Toast.makeText(getContext(), "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_profile_information, container, false);

        editEmail = view.findViewById(R.id.merchant_profile_email);
        editCountryCode = view.findViewById(R.id.merchant_profile_country_code);
        editPhoneNumber = view.findViewById(R.id.merchant_profile_phone);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + merchantUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMerchant merchant = dataSnapshot.getValue(UserMerchant.class);
                editEmail.setText(merchant.getEmail());
                editCountryCode.setText(merchant.getLastname());
                editPhoneNumber.setText(merchant.getPhoneNumber());
                //Picasso.get().load(product.getImage()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
