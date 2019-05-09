package com.example.j_lds.shoppingdrive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.j_lds.shoppingdrive.R;
import com.example.j_lds.shoppingdrive.object_class.Article;

import java.util.ArrayList;

public class UserBasketAdapter extends RecyclerView.Adapter<UserBasketAdapter.ViewHolder> {

    private ArrayList<Article> articleBasket;
    private Context mContext;
    View view;

    public UserBasketAdapter(ArrayList<Article> articleBasket) {
        this.articleBasket = articleBasket;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_basket_articles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
