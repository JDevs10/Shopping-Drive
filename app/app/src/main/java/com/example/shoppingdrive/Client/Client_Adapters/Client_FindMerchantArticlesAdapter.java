package com.example.shoppingdrive.Client.Client_Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingdrive.Client.Client_Pages.Client_DetailArticle;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Client_FindMerchantArticlesAdapter extends RecyclerView.Adapter<Client_FindMerchantArticlesAdapter.ViewHolder>{

    private ArrayList<Product> articles;
    private String merchantUid;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_articles;
        TextView articles_name;
        TextView articles_price;
        Picasso picasso;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_articles = (ImageView)itemView.findViewById(R.id.article_list_image);
            articles_name = (TextView)itemView.findViewById(R.id.article_list_name);
            articles_price = (TextView)itemView.findViewById(R.id.article_list_price);
        }
    }

    public Client_FindMerchantArticlesAdapter(String merchantUid, ArrayList<Product> articles) {
        this.merchantUid = merchantUid;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_custom_find_merchant_articles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(articles.get(position).getImage()).into(holder.iv_articles);
        holder.articles_name.setText(articles.get(position).getName());
        holder.articles_price.setText(articles.get(position).getPrice()+" €");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, Client_DetailArticle.class);
                intent.putExtra("SelectedMerchantUid", merchantUid);
                intent.putExtra("SelectedArticleUid", articles.get(position).getId());
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
























