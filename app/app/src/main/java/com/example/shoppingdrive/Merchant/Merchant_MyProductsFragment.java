package com.example.shoppingdrive.Merchant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Merchant_MyProductsFragment extends Fragment {

    private GridView gridView;
    private ListAdapter adapter;

    private DatabaseReference databaseReference;
    private List<Product> products;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String merchantUid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            merchantUid = currentUser.getUid();
        } else {
            Toast.makeText(getContext(), "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_my_products, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_my_products);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.navigation_my_products);
        setHasOptionsMenu(true);

        gridView = view.findViewById(R.id.gridViewMyProducts);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + merchantUid + "/products");
        products = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Product product = postSnapShot.getValue(Product.class);
                    products.add(product);
                }
                adapter = new Merchant_ProductsAdapter(Merchant_MyProductsFragment.this, products, merchantUid);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.merchant_menu_my_products, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_product:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Merchant_AddProductFragment()).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }
}

class Merchant_ProductsAdapter extends BaseAdapter {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private List<Product> products;
    private Fragment context;
    private LayoutInflater inflater;
    private String merchantUid;

    public Merchant_ProductsAdapter(Fragment context, List<Product> products, String merchantUid) {
        this.context = context;
        this.products = products;
        this.merchantUid = merchantUid;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.merchant_layout_products_list, null);
        }


        TextView textProductName = convertView.findViewById(R.id.textName);
        TextView textProductPrice = convertView.findViewById(R.id.textPrice);
        CircleImageView productImage = convertView.findViewById(R.id.image);
        ImageButton btnUpdate = convertView.findViewById(R.id.btnUpdate);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        Product product = products.get(position);
        final String productId = product.getId();
        textProductName.setText(product.getName());
        textProductPrice.setText(product.getPrice() + " €");
        Picasso.get().load(product.getImage()).into(productImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Merchant_DetailProductFragment merchantDetailProductFragment = new Merchant_DetailProductFragment();
                Bundle args = new Bundle();
                args.putString("id", productId);
                merchantDetailProductFragment.setArguments(args);
                context.getFragmentManager().beginTransaction().replace(R.id.fragment_container, merchantDetailProductFragment).addToBackStack(null).commit();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Merchant_UpdateProductFragment merchantUpdateProductFragment = new Merchant_UpdateProductFragment();
                Bundle args = new Bundle();
                args.putString("id", productId);
                merchantUpdateProductFragment.setArguments(args);
                context.getFragmentManager().beginTransaction().replace(R.id.fragment_container, merchantUpdateProductFragment).addToBackStack(null).commit();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog deleteAlert = new AlertDialog.Builder(context.getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete ?")
                        .setIcon(R.drawable.ic_delete_black_24dp)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseReference.child("user/merchant/" + merchantUid + "/products").child(productId).removeValue();
                                dialog.dismiss();
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                deleteAlert.show();
            }
        });

        return convertView;
    }
}