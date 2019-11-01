package com.example.shoppingdrive.Merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Merchant_AddProductFragment extends Fragment {

    private Button btnAdd;
    private EditText editName;
    private EditText editPrice;
    private EditText editImage;
    private DatabaseReference databaseReference;

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
        }else{
            Toast.makeText(getContext(), "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_fragment_add_product, container, false);

        btnAdd = view.findViewById(R.id.btnAdd);
        editName = view.findViewById(R.id.editName);
        editPrice = view.findViewById(R.id.editPrice);
        editImage = view.findViewById(R.id.editImage);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/merchant/" + merchantUid + "/products");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = databaseReference.push().getKey();
                String name = editName.getText().toString();
                float price = Float.parseFloat(editPrice.getText().toString());
                String image = editImage.getText().toString();
                if(image.isEmpty() || image == ""){
                    image = "https://thijs-lambooij.newdeveloper.nl/img/comingsoon.png";
                }
                Product product = new Product(id, name, price, image);
                databaseReference.child(id).setValue(product);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Merchant_MyProductsFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}