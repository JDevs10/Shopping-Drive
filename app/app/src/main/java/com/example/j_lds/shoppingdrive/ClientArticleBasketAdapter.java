package com.example.j_lds.shoppingdrive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClientArticleBasketAdapter extends RecyclerView.Adapter<ClientArticleBasketAdapter.ViewHolder> {

    private Context context;
    private final String[] names;
    private final double[] prices;
    View view;

    public ClientArticleBasketAdapter(Context context, String[] names, double[] prices) {
        this.context = context;
        this.names = names;
        this.prices = prices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_hidden_basket_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientArticleBasketAdapter.ViewHolder holder, int position) {

        String listItem_NAMES = names[position];
        double listItem_PRICES = prices[position];

        holder.article_name.setText(listItem_NAMES);
        holder.article_price.setText(listItem_PRICES+" €");
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView article_name;
        TextView article_price;

        public ViewHolder(View itemView) {
            super(itemView);

            article_name = (TextView)itemView.findViewById(R.id.textView_hidden_basket_list_article_name);
            article_price = (TextView)itemView.findViewById(R.id.textView_hidden_basket_list_article_price);
        }
    }
}
