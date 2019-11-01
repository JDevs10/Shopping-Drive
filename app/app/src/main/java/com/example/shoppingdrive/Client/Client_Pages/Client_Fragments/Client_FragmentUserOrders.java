package com.example.shoppingdrive.Client.Client_Pages.Client_Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Client.Client_Adapters.Client_UserOrdersAdapter;
import com.example.shoppingdrive.Client.Client_Interfaces.Client_OrderDetailListener;
import com.example.shoppingdrive.Client.Client_Interfaces.Client_TotalCostUpdate;
import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Orders;
import com.example.shoppingdrive.Models.Product;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Client_FragmentUserOrders extends Fragment {
    private String TAG = Client_FragmentUserOrders.class.getSimpleName();
    private View view;
    private Context mContext;

    private RecyclerView mRecycleView;
    private Client_UserOrdersAdapter mUserOrdersAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserUid;
    private DatabaseReference mdatabaseReference;

    private ArrayList<Orders> orderList;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //When initializing your Activity, check to see if the user is currently signed in.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUserUid = currentUser.getUid();
        }else{
            Toast.makeText(mContext, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(mContext, Login.class);
            startActivity(intent);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_fragment_orders, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView_order_myOrderList);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        orderList = new ArrayList<Orders>();
        getCurrentUserOrdersDbData();

        mUserOrdersAdapter = new Client_UserOrdersAdapter(orderList, currentUserUid);
        mRecycleView.setAdapter(mUserOrdersAdapter);

        mUserOrdersAdapter.showPopUp(new Client_OrderDetailListener() {
            @Override
            public void OnShowDetails(Orders orders) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_detail_order,null,false);
                InitDetailPopup(view, orders);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getCurrentUserOrdersDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("orders");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.exists()){
                            Orders order = new Orders();
                            order.setId(ds.getKey());
                            order.setRef(ds.child("ref").getValue().toString());
                            order.setClientId(ds.child("clientId").getValue().toString());
                            order.setMerchantId(ds.child("merchantId").getValue().toString());
                            order.setDate((Long) ds.child("date").getValue());
                            order.setPayment(ds.child("payment").getValue().toString());
                            order.setTotalPrice(Double.parseDouble(ds.child("totalPrice").getValue().toString()));

                            //get all products
                            ArrayList<Product> productList = new ArrayList<>();
                            //Log.e("Count " ,""+ds.child("products").getChildrenCount());
                            for (DataSnapshot postSnapshot: ds.child("products").getChildren()) {
                                Product product = postSnapshot.getValue(Product.class);
                                productList.add(product);
                                //Log.e("Get Data", product.getId());
                            }
                            order.setProducts(productList);

                            Log.e("Order id: ", "||=> "+order.getId());
                            Log.e("Order ref: ", "||=> "+order.getRef());
                            Log.e("Order id client: ", "||=> "+order.getClientId());
                            Log.e("Order id merchant: ", "||=> "+order.getMerchantId());
                            Log.e("Order date: ", "||=> "+order.getDate());
                            Log.e("Order payment: ", "||=> "+order.getPayment());
                            Log.e("Order productListSize: ", "||=> "+order.getProducts().size());
                            Log.e("Order TotalPrice: ", "||=> "+order.getTotalPrice()+"\n\n");

                            orderList.add(order);
                            mUserOrdersAdapter.notifyDataSetChanged();
                        }else{
                            Log.d("Article Error : ", "Can't find articles !!!");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "ERROR 'Get Article' :\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void InitDetailPopup(View view, Orders orders){
        final TextView toolbar_name_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_toolbar);
        final TextView merchant_name_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_merchantname);
        final TextView time_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_time);
        final TextView products_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_products);
        final TextView payment_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_payment);
        final TextView total_et = (TextView) view.findViewById(R.id.client_dialog_textView_detail_order_total);
        ImageButton btn_cancel = (ImageButton) view.findViewById(R.id.client_dialog_button_detail_order_cancel);

        //get merchant company
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/")
                .getReference().child("user/merchant/"+orders.getMerchantId()+"/companyName");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                merchant_name_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get date format
        Date date = new Date(orders.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss a");
        System.out.println( sdf.format(date) );

        //list the products
        String myProducts = "";
        for (int index=0; index<orders.getProducts().size(); index++){
            myProducts +=
                    "Name: "+orders.getProducts().get(index).getName()+"\n" +
                    "Quantity: "+orders.getProducts().get(index).getQuantity()+"\n" +
                    "Prix: "+orders.getProducts().get(index).getPrice()+"\n\n";
        }


        toolbar_name_et.setText("Ref: "+orders.getRef());
        time_et.setText(sdf.format(date));
        products_et.setText(myProducts);
        payment_et.setText("Payed by "+orders.getPayment());
        total_et.setText("Total: "+orders.getTotalPrice());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
