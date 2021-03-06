package com.example.shoppingdrive.Client.Client_Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.Model.Settings;
import com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentFindMerchantArticles;
import com.example.shoppingdrive.Models.Merchant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Client_FindMerchantAdapter extends RecyclerView.Adapter<Client_FindMerchantAdapter.ViewHolder> {
    private ArrayList<Merchant> merchants;
    private Context mContext;
    private DatabaseHelper db;


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

    public Client_FindMerchantAdapter(Context context, ArrayList<Merchant> merchants) {
        this.merchants = merchants;
        this.mContext = context;
        this.db = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_custom_find_merchant,parent,false);
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

                //Intent intent= new Intent(mContext, FindMerchantArticles.class);
                //intent.putExtra("SelectedMerchantUid", merchants.get(position).getId());
                //((Activity) mContext).startActivityForResult(intent, 1);


                Bundle bundle=new Bundle();
                bundle.putString("SelectedMerchantUid", merchants.get(position).getId());
                bundle.putString("SelectedMerchantCompanyName", merchants.get(position).getCompanyName());

                //reset data settings
                db.deleteSettingsData(1);
                db.defaultSettings();

                //add new data settings
                Settings settings = new Settings();
                settings.setId(1);
                settings.setSelectedMerchantUid(merchants.get(position).getId());
                settings.setSelectedMerchantCompanyName(merchants.get(position).getCompanyName());
                db.updateSettingsData(settings);

                Client_FragmentFindMerchantArticles merchantArticles = new Client_FragmentFindMerchantArticles();
                merchantArticles.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) mContext;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, merchantArticles).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }

}
