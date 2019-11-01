package com.example.shoppingdrive.Merchant;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfileAddress;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfileInformation;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments.Client_FragmentProfilePaymentInfo;
import com.example.shoppingdrive.Merchant.Merchant_FragmentProfileFragments.Merchant_FragmentProfileInformation;
import com.example.shoppingdrive.R;

public class Merchant_MyProfileFragment extends Fragment {

    private Context mContext;
    private FragmentActivity myFragmentActivity;
    private FragmentManager fragManager;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.merchant_home_profile_navigation_information:
                    selectedFragment = new Client_FragmentProfileInformation();
                    break;
                case R.id.merchant_home_profile_navigation_address_info:
                    selectedFragment = new Client_FragmentProfileAddress();
                    break;
            }

            fragManager.beginTransaction().replace(R.id.merchant_fragment_profile_container, selectedFragment).commit();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_my_profile, container, false);

        Toolbar toolbar = ((Merchant_MainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_my_profile);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.navigation_my_profile);

        navigation = (BottomNavigationView) view.findViewById(R.id.merchant_home_fragment_profile_container);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragManager.beginTransaction().replace(R.id.merchant_fragment_profile_container, new Merchant_FragmentProfileInformation()).commit();

    }

}
