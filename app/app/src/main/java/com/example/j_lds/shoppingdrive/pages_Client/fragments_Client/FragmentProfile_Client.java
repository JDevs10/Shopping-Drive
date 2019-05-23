package com.example.j_lds.shoppingdrive.pages_Client.fragments_Client;

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

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.databaseOffline_Client.DatabaseHelper;
import com.example.j_lds.shoppingdrive.pages_Client.fragments_Client.FragmentProfileFragments_Client.FragmentProfileAddress_Client;
import com.example.j_lds.shoppingdrive.pages_Client.fragments_Client.FragmentProfileFragments_Client.FragmentProfileInformation_Client;
import com.example.j_lds.shoppingdrive.pages_Client.fragments_Client.FragmentProfileFragments_Client.FragmentProfilePaymentInfo_Client;

public class FragmentProfile_Client extends Fragment {
    private String TAG = FragmentProfile_Client.class.getSimpleName();
    private View view;
    private Context mContext;
    private FragmentActivity myFragmentActivity;
    private FragmentManager fragManager;

    private BottomNavigationView navigation;

    private DatabaseHelper db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedSragment = null;

            switch (item.getItemId()) {
                case R.id.home_profile_navigation_information:
                    selectedSragment = new FragmentProfileInformation_Client();
                    break;
                case R.id.home_profile_navigation_address_info:
                    selectedSragment = new FragmentProfileAddress_Client();
                    break;
                case R.id.home_profile_navigation_payment_info:
                    selectedSragment = new FragmentProfilePaymentInfo_Client();
                    break;
            }

            fragManager.beginTransaction().replace(R.id.fragment_profile_container, selectedSragment).commit();
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
        db = new DatabaseHelper(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_client, container, false);

        navigation = (BottomNavigationView) view.findViewById(R.id.home_fragment_profile_container);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, " Started");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragManager.beginTransaction().replace(R.id.fragment_profile_container, new FragmentProfileInformation_Client()).commit();

    }
}
