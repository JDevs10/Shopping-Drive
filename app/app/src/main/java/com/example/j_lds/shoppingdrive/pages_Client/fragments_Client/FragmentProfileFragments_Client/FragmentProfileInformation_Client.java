package com.example.j_lds.shoppingdrive.pages_Client.fragments_Client.FragmentProfileFragments_Client;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.databaseOffline_Client.DatabaseHelper;

public class FragmentProfileInformation_Client extends Fragment {
    private String TAG = FragmentProfileInformation_Client.class.getSimpleName();
    private View view;
    private Context mContext;

    private DatabaseHelper db;

    @Override
    public void onAttach(Context context) {
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
        view = inflater.inflate(R.layout.fragment_profile_information_client, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
