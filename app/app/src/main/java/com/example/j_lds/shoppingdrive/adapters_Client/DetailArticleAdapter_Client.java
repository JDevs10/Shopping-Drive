package com.example.j_lds.shoppingdrive.adapters_Client;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Context;

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.object_class_Client.Article;

import java.util.ArrayList;

public class DetailArticleAdapter_Client extends RecyclerView.Adapter<DetailArticleAdapter_Client.ViewHolder>{

    private ArrayList<Article> articleBasket;
    private Context mContext;
    View view;

    public DetailArticleAdapter_Client(ArrayList<Article> articleBasket) {
        this.articleBasket = articleBasket;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_basket_articles_client,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailArticleAdapter_Client.ViewHolder holder, final int position) {
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
