package com.example.j_lds.shoppingdrive;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_lds.shoppingdrive.object_class.Merchant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FindMerchantAdapter extends RecyclerView.Adapter<FindMerchantAdapter.ViewHolder> {
    private ArrayList<Merchant> merchants;

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_merchant;
        TextView merchant_name;
        Picasso picasso;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_merchant = (ImageView)itemView.findViewById(R.id.merchant_list_image);
            merchant_name = (TextView)itemView.findViewById(R.id.merchant_list_name);
        }
    }

    public FindMerchantAdapter(ArrayList<Merchant> merchants) {
        this.merchants = merchants;
    }

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
        holder.merchant_name.setText(merchants.get(position).getCompanyName());
        Picasso.get().load(merchants.get(position).getCompanyLogo()).into(holder.iv_merchant);
        Log.d("Merchant Img : ", merchants.get(position).getCompanyLogo());
    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }
}
