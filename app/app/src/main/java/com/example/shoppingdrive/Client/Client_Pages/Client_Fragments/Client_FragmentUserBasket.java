package com.example.shoppingdrive.Client.Client_Pages.Client_Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Client.Client_Pages.Client_Checkout;
import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.BasketProduct;
import com.example.shoppingdrive.Models.Orders;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_Adapters.Client_UserBasketAdapter;
import com.example.shoppingdrive.Client.Client_Interfaces.Client_TotalCostUpdate;
import com.example.shoppingdrive.Models.Product;
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

public class Client_FragmentUserBasket extends Fragment {
    private String TAG = Client_FragmentUserBasket.class.getSimpleName();
    private View view;
    private Context mContext;

    TextView tv_totalCost;
    private Button btn_sendOrder;

    private RecyclerView mRecycleView;
    private Client_UserBasketAdapter mUserBasketAdapterClient;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserUid;
    private DatabaseReference mdatabaseReference;

    private ArrayList<BasketProduct> articleBasket;
    private String selectedMerchanteUid = "";

    private ArrayList<String> merchantUidList = new ArrayList<>();
    private String[] currentUserPaymentType;

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
            //Toast.makeText(mContext, "Welcome "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(mContext, Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_fragment_basket, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView_basket_myBasketList);
        tv_totalCost = (TextView) view.findViewById(R.id.textView_basket_totalCost);

        btn_sendOrder = (Button) view.findViewById(R.id.button_sendMyOrder);
        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        articleBasket = new ArrayList<BasketProduct>();
        getCurrentUserBasketDbData();

        mUserBasketAdapterClient = new Client_UserBasketAdapter(articleBasket, currentUserUid);
        mRecycleView.setAdapter(mUserBasketAdapterClient);

        mUserBasketAdapterClient.setTotalCostUpdateListener(new Client_TotalCostUpdate() {
            @Override
            public void OnTotalCostUpdate(double data) {
                Log.e(TAG, " OnTotalCostUpdate() => "+data);
                tv_totalCost.setText(String.valueOf(data));
            }
        });

        getMerchantUidList();
        getUserPaymentType();
    }


    private void getCurrentUserBasketDbData(){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+currentUserUid+"/basket");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                articleBasket.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(dataSnapshot.exists()){
                            BasketProduct article = ds.getValue(BasketProduct.class);
                            article.setId(ds.getKey());
                            Log.d("Basket Article id: ", "||=> "+article.getId());
                            Log.d("Basket Article name: ", "||=> "+article.getName());
                            Log.d("Basket Article image: ", "||=> "+article.getImage());
                            Log.d("Basket Article quanti: ", "||=> "+article.getQuantity());
                            Log.d("Basket Article info: ", "||=> "+article.getDescription());

                            articleBasket.add(article);
                            mUserBasketAdapterClient.notifyDataSetChanged();
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

    private void sendOrder(){
        Intent intent= new Intent(mContext, Client_Checkout.class);
        intent.putExtra("prepareOrderList", prepareOrder());
        startActivity(intent);
    }

    private void getMerchantUidList(){
        //get && save merchant uid in a list
        merchantUidList.clear();
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+currentUserUid+"/basket");
        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "DataSnapshot 1: "+dataSnapshot);
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String merchantUid = ds.child("merchantUid").getValue().toString();
                        Log.e(TAG, " merchantUid: "+merchantUid);

                        if (merchantUidList.size() == 0) {
                            Log.e(TAG, " 1st merchantUid add: "+merchantUid);
                            merchantUidList.add(merchantUid);
                        }else{
                            for (int i=0; i<merchantUidList.size(); i++){
                                if (!merchantUidList.contains(merchantUid)){
                                    Log.e(TAG, " 2st merchantUid add: "+merchantUid);
                                    merchantUidList.add(merchantUid);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserPaymentType(){
        currentUserPaymentType = new String[1];
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/")
                .getReference().child("user/client/"+currentUserUid+"/paymentType/type");
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserPaymentType[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Orders> prepareOrder(){
        Log.e(TAG, "prepare Order objs of each merchant");
        //prepare Order objs of each merchant
        ArrayList<Orders> prepareOrders = new ArrayList<>();

        for (int i=0; i<merchantUidList.size(); i++){
            Orders orders = new Orders();
            ArrayList<Product> prepareProducts = new ArrayList<>();

            double theTotalPrice = 0;
            for (int y=0; y<articleBasket.size(); y++){
                if (merchantUidList.get(i).equals(articleBasket.get(y).getMerchantUid())){
                    Product product = new Product();
                    product.setId(articleBasket.get(y).getId());
                    product.setName(articleBasket.get(y).getName());
                    product.setPrice(articleBasket.get(y).getPrice());
                    product.setImage(articleBasket.get(y).getImage());
                    product.setQuantity(articleBasket.get(y).getQuantity());
                    product.setDescription(articleBasket.get(y).getDescription());

                    theTotalPrice += (product.getPrice() * product.getQuantity());

                    prepareProducts.add(product);
                }
            }

            //get date format
            Long dateInLong = Calendar.getInstance().getTimeInMillis();

            orders.setDate(dateInLong);
            orders.setId("");   //will reset later
            orders.setClientId(currentUserUid);
            orders.setMerchantId(merchantUidList.get(i));
            orders.setPayment(currentUserPaymentType[0]);
            orders.setProducts(prepareProducts);
            orders.setRef("SHD-"+dateInLong);
            orders.setTotalPrice(theTotalPrice);

            prepareOrders.add(orders);
        }

        Log.e(TAG, "Basket => PrepareOrderList size: "+prepareOrders.size());
        /*
        Log.e(TAG, "PrepareOrderList "+index+"/"+prepareOrders.size()+"\n" +
                "id: "+prepareOrders.get(index).getId()+"\n" +
                "ref: "+prepareOrders.get(index).getRef()+"\n" +
                "date: "+prepareOrders.get(index).getDate()+"\n" +
                "client uid: "+prepareOrders.get(index).getClientId()+"\n" +
                "merchant uid: "+prepareOrders.get(index).getMerchantId()+"\n" +
                "payment: "+prepareOrders.get(index).getPayment()+"\n" +
                "products: "+prepareOrders.get(index).getProducts().size()+"\n" +
                "total price: "+prepareOrders.get(index).getTotalPrice());
        */


        return prepareOrders;
    }

    private void test(){

    }
}
