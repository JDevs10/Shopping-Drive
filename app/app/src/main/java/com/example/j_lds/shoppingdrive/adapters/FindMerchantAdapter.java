package com.example.j_lds.shoppingdrive.adapters;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.object_class.Merchant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindMerchantAdapter extends RecyclerView.Adapter<FindMerchantAdapter.ViewHolder> {
    private ArrayList<Merchant> merchants;
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private String articleId = "";
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
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_find_merchant,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.merchant_name.setText(merchants.get(position).getCompanyName());
        Picasso.get().load(merchants.get(position).getCompanyLogo()).into(holder.iv_merchant);
        Log.d("Merchant Img : ", merchants.get(position).getCompanyLogo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Merchant Selected : ", "\n" +
                        "The Position ==> "+position+"\n" +
                        "Merchant id ==> "+merchants.get(position).getId());

                Intent intent= new Intent(mContext, FindMerchantArticles.class);
                intent.putExtra("SelectedMerchantUid", merchants.get(position).getId());
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }

}
