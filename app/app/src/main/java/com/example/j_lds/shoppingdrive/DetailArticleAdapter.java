package com.example.j_lds.shoppingdrive;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailArticleAdapter extends RecyclerView.Adapter<DetailArticleAdapter.ViewHolder>{

    public String[] article_basket_names = {"Article 1", "Article 2", "Article 3", "Article 4", "Article 5",
            "Article 6", "Article 7", "Article 8", "Article 9", "Article 10"};

    private double[] article_basket_price = {1.20, 6.51, 3.25, 9.86, 9.70,
            84.6, 995.0, 1234.56, 0.99, 0.01};

    public int getArticle_basket_names_length() {
        return article_basket_names.length;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_basket_articles,parent,false);
        return new DetailArticleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String listItem_NAMES = article_basket_names[position];
        double listItem_PRICE = article_basket_price[position];

        holder.article_name.setText(listItem_NAMES);
        holder.article_price.setText(listItem_PRICE+" €");
    }

    @Override
    public int getItemCount() {
        return article_basket_names.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView article_name;
        public TextView article_price;

        public ViewHolder(View itemView) {
            super(itemView);

            article_name = (TextView) itemView.findViewById(R.id.custom_basket_article_name);
            article_price = (TextView) itemView.findViewById(R.id.custom_basket_article_price);
        }
    }
}
