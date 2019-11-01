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
import android.widget.ImageButton;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Merchant_DetailProductFragment extends Fragment {

    private DatabaseReference databaseReference;
    CircleImageView image;
    TextView textName;
    TextView textPrice;
    public ImageButton btnUpdate;
    public ImageButton btnDelete;
    String id;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String merchantUid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            merchantUid = currentUser.getUid();
        }
        else{
            Toast.makeText(getContext(), "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_detail_product, container, false);

        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.navigation_my_products);
        setHasOptionsMenu(true);

        id = getArguments().getString("id");

        textName = view.findViewById(R.id.Name);
        textPrice = view.findViewById(R.id.Price);
        image = view.findViewById(R.id.image);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + merchantUid + "/products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Product product = dataSnapshot.child(id).getValue(Product.class);
                textName.setText(product.getName());
                toolbar.setTitle(product.getName());
                textPrice.setText(product.getPrice() + " €");
                Picasso.get().load(product.getImage()).resize(400, 400).centerCrop().into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.merchant_menu_detail_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.update_product:
                Merchant_UpdateProductFragment merchantUpdateProductFragment = new Merchant_UpdateProductFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                merchantUpdateProductFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, merchantUpdateProductFragment).addToBackStack(null).commit();
                break;
            case R.id.delete_product:
                AlertDialog deleteAlert =new AlertDialog.Builder(getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete ?")
                        .setIcon(R.drawable.ic_delete_black_24dp)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseReference.child(id).removeValue();
                                dialog.dismiss();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Merchant_MyProductsFragment()).addToBackStack(null).commit();
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        ;
                deleteAlert.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
