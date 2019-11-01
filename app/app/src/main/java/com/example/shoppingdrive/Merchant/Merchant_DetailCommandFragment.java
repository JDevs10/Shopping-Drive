package com.example.shoppingdrive.Merchant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Merchant_DetailCommandFragment extends Fragment {

    RecyclerView detailCommandListView;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_detail_command, container, false);

        final String id = getArguments().getString("commandId");
        final String clientName = getArguments().getString("commandClientName");
        final String commandMerchantId = getArguments().getString("commandMerchantId");


        final Toolbar toolbar = ((Merchant_MainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(clientName);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.navigation_commands);



        detailCommandListView = view.findViewById(R.id.detailCommandListView);
        detailCommandListView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Product> productsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child(id).child("products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();

                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    Product product = postSnapShot.getValue(Product.class);

                    productsList.add(product);
                 }

                Log.e("productsList.size()", String.valueOf(productsList.size()));
                adapter = new Merchant_ProductsCommandAdapter(Merchant_DetailCommandFragment.this, productsList, commandMerchantId);

                detailCommandListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}

class Merchant_ProductsCommandAdapter extends RecyclerView.Adapter<Merchant_ProductsCommandAdapter.ViewHolder>{

    Fragment context;
    List<Product> products;
    String commandMerchantId;

    public Merchant_ProductsCommandAdapter(Fragment context, List<Product> products, String commandMerchantId) {
        this.context = context;
        this.products = products;
        this.commandMerchantId = commandMerchantId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_layout_detail_command_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Product product = products.get(position);
        holder.productId = product.getId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + commandMerchantId + "/products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Product theProduct = dataSnapshot.child(product.getId()).getValue(Product.class);
                float pricePerProduct = product.getQuantity() * theProduct.getPrice();
                holder.productName.setText(theProduct.getName());
                holder.productQuantity.setText(product.getQuantity() + " x "+ theProduct.getPrice() + " € = " + pricePerProduct + " €");
                /*Picasso.get().load(product.getImage()).into(image);*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public String productId;
        public TextView productName;
        public TextView productQuantity;

        public ViewHolder(final View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.name);
            productQuantity = itemView.findViewById(R.id.quantity);
        }
    }
}