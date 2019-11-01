package com.example.shoppingdrive.Client.Client_Pages.Client_Fragments;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.UserClient;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfileAddress;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfileInformation;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfilePaymentInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Client_FragmentProfile extends Fragment {
    private String TAG = Client_FragmentProfile.class.getSimpleName();
    private View view;
    private Context mContext;
    private FragmentActivity myFragmentActivity;
    private FragmentManager fragManager;
    private BottomNavigationView navigation;

    private DatabaseReference databaseReference;
    private TextView fullName_et;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String clientUid;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.client_home_profile_navigation_information:
                    selectedFragment = new Client_FragmentProfileInformation();
                    break;
                case R.id.client_home_profile_navigation_address_info:
                    selectedFragment = new Client_FragmentProfileAddress();
                    break;
                case R.id.client_home_profile_navigation_payment_info:
                    selectedFragment = new Client_FragmentProfilePaymentInfo();
                    break;
            }

            fragManager.beginTransaction().replace(R.id.client_fragment_profile_container, selectedFragment).commit();
            return true;
        }
    };


    @Override
    public void onAttach(Context context) {
        myFragmentActivity = (FragmentActivity) context;
        fragManager = myFragmentActivity.getSupportFragmentManager();
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            clientUid = currentUser.getUid();
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
        view = inflater.inflate(R.layout.client_fragment_profile, container, false);

        navigation = (BottomNavigationView) view.findViewById(R.id.client_home_fragment_profile_container);
        fullName_et = (TextView)view.findViewById(R.id.client_home_fragment_profile_fullname);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClient client = dataSnapshot.getValue(UserClient.class);
                fullName_et.setText(client.getFirstname()+" "+client.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, " Started");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragManager.beginTransaction().replace(R.id.client_fragment_profile_container, new Client_FragmentProfileInformation()).commit();

    }

}
