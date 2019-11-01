package com.example.shoppingdrive.Client.Client_Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Context;

import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Models.Product;


import java.util.ArrayList;

public class Client_DetailArticleAdapter extends RecyclerView.Adapter<Client_DetailArticleAdapter.ViewHolder>{

    private ArrayList<Product> articleBasket;
    private Context mContext;
    View view;

    public Client_DetailArticleAdapter(ArrayList<Product> articleBasket) {
        this.articleBasket = articleBasket;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_custom_basket_articles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.article_name.setText(articleBasket.get(position).getName());
        holder.article_price.setText(articleBasket.get(position).getPrice()+" €");
    }

    @Override
    public int getItemCount() {
        return articleBasket.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView article_name;
        TextView article_price;

        public ViewHolder(View itemView) {
            super(itemView);

            article_name = (TextView)itemView.findViewById(R.id.custom_basket_article_name);
            article_price = (TextView)itemView.findViewById(R.id.custom_basket_article_price);
        }
    }
}
