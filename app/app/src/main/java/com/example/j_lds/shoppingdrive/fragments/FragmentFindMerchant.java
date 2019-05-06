package com.example.j_lds.shoppingdrive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j_lds.shoppingdrive.R;

public class FragmentFindMerchant extends Fragment {
    private String TAG = FragmentFindMerchant.class.getSimpleName();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_merchant, container, false);

        return view;
    }
}
