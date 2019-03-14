package com.example.j_lds.shoppingdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_lds.shoppingdrive.object_class.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class FindMerchantArticlesAdapter extends RecyclerView.Adapter<FindMerchantArticlesAdapter.ViewHolder>{

    private ArrayList<Article> articles;
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

    public FindMerchantArticlesAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_find_merchant_articles,parent,false);
        return new FindMerchantArticlesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(articles.get(position).getImage()).into(holder.iv_articles);
        holder.articles_name.setText(articles.get(position).getName());
        holder.articles_price.setText(articles.get(position).getPrice()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Article Selected : ", "\n" +
                        "The Position ==> "+position+"\n" +
                        "Article name ==> "+articles.get(position).getName()+"\n" +
                        "Article price ==> "+articles.get(position).getPrice()+"\n" +
                        "Article price ==> "+articles.get(position).getImage());

                Intent intent= new Intent(mContext, FindMerchantArticles.class);
                intent.putExtra("SelectedMerchantUid", articles.get(position).getId());
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
























