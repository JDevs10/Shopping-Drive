package com.example.j_lds.shoppingdrive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j_lds.shoppingdrive.R;

public class FragmentUserOrders_Client extends Fragment {
    private String TAG = FragmentUserOrders_Client.class.getSimpleName();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        return view;
    }
}
