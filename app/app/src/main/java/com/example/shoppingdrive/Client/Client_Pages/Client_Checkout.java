package com.example.shoppingdrive.Client.Client_Pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Orders;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Client_Checkout extends AppCompatActivity {
    private String TAG = Client_Checkout.class.getSimpleName();

    private Button btn_order, btn_continue_order;
    private ImageButton btn_backWhereTheUserWas;
    private EditText et_email;
    private TextView tv_checkout_p2;
    private String email;

    private ArrayList<Orders> prepareOrderList;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserUid;
    private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_checkout);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        //When initializing your Activity, check to see if the user is currently signed in.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUserUid = currentUser.getUid();
            //Toast.makeText(mContext, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(this, Login.class);
            startActivity(intent);
        }

        prepareOrderList = (ArrayList<Orders>) getIntent().getExtras().get("prepareOrderList");
        Log.e(TAG, "Checkout => PrepareOrderList size: "+prepareOrderList.size());

        btn_backWhereTheUserWas = (ImageButton)findViewById(R.id.button_checkout_cancel);
        btn_order = (Button)findViewById(R.id.button_checkout_confirmOrder);

        btn_backWhereTheUserWas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_to_userHome();
            }
        });

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_pwd();
            }
        });

        et_email = (EditText)findViewById(R.id.editText_checkout_getEmail);
    }

    private void confirm_pwd(){
        email = et_email.getText().toString();

        // check the user password from the bd and the user input
        if (email.equals(currentUser.getEmail())){
            mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("orders/");

            for (int index=0; index<prepareOrderList.size(); index++){
                String newKey = mdatabaseReference.push().getKey();
                prepareOrderList.get(index).setId(newKey);
                mdatabaseReference.child(newKey+"/").setValue(prepareOrderList.get(index));

                Log.e(TAG, "PrepareOrderList "+(index+1)+"/"+prepareOrderList.size()+"\n" +
                        "id: "+prepareOrderList.get(index).getId()+"\n" +
                        "ref: "+prepareOrderList.get(index).getRef()+"\n" +
                        "date: "+prepareOrderList.get(index).getDate()+"\n" +
                        "client uid: "+prepareOrderList.get(index).getClientId()+"\n" +
                        "merchant uid: "+prepareOrderList.get(index).getMerchantId()+"\n" +
                        "payment: "+prepareOrderList.get(index).getPayment()+"\n" +
                        "products: "+prepareOrderList.get(index).getProducts().size()+"\n" +
                        "total price: "+prepareOrderList.get(index).getTotalPrice());
            }

            mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference();
            mdatabaseReference.child("user/client/"+currentUserUid+"/basket").removeValue();
            Toast.makeText(getBaseContext(), "Good Email... sending your order", Toast.LENGTH_SHORT).show();

            //get the merchant address
            //then show the Estimate time

            checkout_p2(14);

        }else{
            Toast.makeText(getBaseContext(), "Email is incorrect...", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkout_p2(int time){
        setContentView(R.layout.client_activity_checkout_p2);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_checkout_p2 = (TextView)findViewById(R.id.textView_checkout_p2_info);
        tv_checkout_p2.setText("We've got your order. Your Items will be ready-delivered within "+(time-3)+"-"+(time+3)+" minutes");

        btn_continue_order = (Button)findViewById(R.id.button_back_to_findMerchantList);
        btn_continue_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Client_Checkout.this, Client_Home.class);
                startActivity(intent);
            }
        });
    }

    private void back_to_userHome(){
        Intent intent= new Intent(Client_Checkout.this, Client_Home.class);
        startActivity(intent);
    }
}
