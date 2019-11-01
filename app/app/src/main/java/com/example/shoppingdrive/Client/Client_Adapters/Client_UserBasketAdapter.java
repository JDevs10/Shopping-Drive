package com.example.shoppingdrive.Client.Client_Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingdrive.Models.BasketProduct;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_Interfaces.Client_TotalCostUpdate;
import com.example.shoppingdrive.Models.Product;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Client_UserBasketAdapter extends RecyclerView.Adapter<Client_UserBasketAdapter.ViewHolder> {

    private String clientUid;
    private ArrayList<BasketProduct> articleBasket;
    private Context mContext;
    private View view;
    private Client_TotalCostUpdate mClientTotalCostUpdate;
    private DatabaseReference mdatabaseReference;

    private boolean init;

    public Client_UserBasketAdapter(ArrayList<BasketProduct> articleBasket, String clientUid) {
        this.articleBasket = articleBasket;
        this.clientUid = clientUid;
        this.init = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView article_name;
        TextView article_price;
        TextView article_quantity;

        Button article_quantity_minus_btn;
        Button article_quantity_plus_btn;
        Button article_delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);

            article_name = (TextView)itemView.findViewById(R.id.custom_basket_article_name);
            article_price = (TextView)itemView.findViewById(R.id.custom_basket_article_price);
            article_quantity = (TextView)itemView.findViewById(R.id.custom_basket_article_quantity_nbr);

            article_quantity_minus_btn = (Button)itemView.findViewById(R.id.custom_basket_article_minus_btn);
            article_quantity_plus_btn = (Button)itemView.findViewById(R.id.custom_basket_article_plus_btn);
            article_delete_btn = (Button)itemView.findViewById(R.id.custom_basket_article_delete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_custom_basket_articles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.article_name.setText(articleBasket.get(position).getName());
        holder.article_price.setText(articleBasket.get(position).getPrice()+" €");
        holder.article_quantity.setText(articleBasket.get(position).getQuantity()+"");

        //init total cost at first load
        if (init){
            mClientTotalCostUpdate.OnTotalCostUpdate(getTotalCost(articleBasket));
            init = false;
        }

        final int[] initQuantity = {articleBasket.get(position).getQuantity()};
        //btn actions
        holder.article_quantity_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQuantity[0]--;
                articleBasket.get(position).setQuantity(initQuantity[0]);
                holder.article_quantity.setText(articleBasket.get(position).getQuantity()+"");

                mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid+"/basket/");
                mdatabaseReference.child(articleBasket.get(position).getId()+"/quantity").setValue(initQuantity[0]);

                //Update total cost
                mClientTotalCostUpdate.OnTotalCostUpdate(getTotalCost(articleBasket));
            }
        });
        holder.article_quantity_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQuantity[0]++;
                articleBasket.get(position).setQuantity(initQuantity[0]);
                holder.article_quantity.setText(articleBasket.get(position).getQuantity()+"");

                mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid+"/basket/");
                mdatabaseReference.child(articleBasket.get(position).getId()+"/quantity").setValue(initQuantity[0]);

                //Update total cost
                mClientTotalCostUpdate.OnTotalCostUpdate(getTotalCost(articleBasket));
            }
        });
        holder.article_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid+"/basket/"+articleBasket.get(position).getId());
                mdatabaseReference.removeValue();

                articleBasket.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                //Update total cost
                mClientTotalCostUpdate.OnTotalCostUpdate(getTotalCost(articleBasket));
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleBasket.size();
    }

    public void setTotalCostUpdateListener(Client_TotalCostUpdate mClientTotalCostUpdate){
        this.mClientTotalCostUpdate = mClientTotalCostUpdate;
    }

    public double getTotalCost(ArrayList<BasketProduct> data){
        double totalCost = 0;
        for (int i=0; i<data.size(); i++){
            totalCost += (data.get(i).getQuantity() * data.get(i).getPrice());
        }
        totalCost = Math.round(totalCost * 100D) / 100D;
        return totalCost;
    }
}
