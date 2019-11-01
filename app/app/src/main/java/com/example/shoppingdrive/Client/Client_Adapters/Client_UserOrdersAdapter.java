package com.example.shoppingdrive.Client.Client_Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Client.Client_Interfaces.Client_OrderDetailListener;
import com.example.shoppingdrive.Models.Orders;
import com.example.shoppingdrive.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Client_UserOrdersAdapter extends RecyclerView.Adapter<Client_UserOrdersAdapter.ViewHolder> {
    private String TAG = Client_UserOrdersAdapter.class.getSimpleName();
    private String clientUid;
    private ArrayList<Orders> ordersList;
    private Context mContext;
    private DatabaseReference mdatabaseReference;

    Client_OrderDetailListener mClient_OrderDetailListener;

    private boolean init;

    public Client_UserOrdersAdapter(ArrayList<Orders> ordersList, String clientUid) {
        this.ordersList = ordersList;
        this.clientUid = clientUid;
        this.init = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout order_layout;
        TextView order_ref;
        TextView order_total_price;
        TextView order_date;
        TextView order_payment;

        Button order_delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);

            order_layout = (LinearLayout)itemView.findViewById(R.id.custom_order_list_layout);
            order_ref = (TextView)itemView.findViewById(R.id.custom_order_list_ref);
            order_total_price = (TextView)itemView.findViewById(R.id.custom_order_list_totalprice);
            order_date = (TextView)itemView.findViewById(R.id.custom_order_list_date);
            order_payment = (TextView)itemView.findViewById(R.id.custom_order_list_paymenttype);

            order_delete_btn = (Button)itemView.findViewById(R.id.custom_order_list_delete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.client_custom_order_list, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.order_ref.setText(ordersList.get(i).getRef());
        viewHolder.order_total_price.setText(""+ordersList.get(i).getTotalPrice());
        viewHolder.order_date.setText(""+ordersList.get(i).getDate());
        viewHolder.order_payment.setText(ordersList.get(i).getPayment());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient_OrderDetailListener.OnShowDetails(ordersList.get(i));
            }
        });

        viewHolder.order_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOrder(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void showPopUp(Client_OrderDetailListener mClient_OrderDetailListener){
        this.mClient_OrderDetailListener = mClient_OrderDetailListener;
    }

    public void deleteOrder(int position){
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("orders/"+ordersList.get(position).getId());
        mdatabaseReference.removeValue();

        ordersList.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
