package com.example.j_lds.shoppingdrive;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FindMerchantAdapter extends RecyclerView.Adapter<FindMerchantAdapter.ViewHolder> {

    private int[] imageView_merchant = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10};
    private String[] merchant_names = {"Merchant 1", "Merchant 2", "Merchant 3", "Merchant 4", "Merchant 5",
            "Merchant 6", "Merchant 7", "Merchant 8", "Merchant 9", "Merchant 10"};


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_find_merchant,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int listItem_IMAGES = imageView_merchant[position];
        String listItem_NAMES = merchant_names[position];

        holder.iv_merchant.setImageResource(listItem_IMAGES);
        holder.merchant_name.setText(listItem_NAMES);
    }

    @Override
    public int getItemCount() {
        return imageView_merchant.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_merchant;
        public TextView merchant_name;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_merchant = (ImageView)itemView.findViewById(R.id.merchant_list_image);
            merchant_name = (TextView)itemView.findViewById(R.id.merchant_list_name);
        }
    }
}
