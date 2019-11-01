package com.example.shoppingdrive.Merchant;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.Client;
import com.example.shoppingdrive.Models.Order;
import com.example.shoppingdrive.Models.UserClient;
import com.example.shoppingdrive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Merchant_CommandsFragment extends Fragment {

    RecyclerView commandsListView;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;

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
        View view = inflater.inflate(R.layout.merchant_fragment_commands, container, false);

        Toolbar toolbar = ((Merchant_MainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_commands);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.navigation_commands);

        commandsListView = view.findViewById(R.id.commandsListView);
        commandsListView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Order> commandsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders");

        databaseReference.orderByChild("merchantId").equalTo(merchantUid).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        commandsList.clear();

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            Order command = postSnapShot.getValue(Order.class);
                            commandsList.add(command);
                        }
                        Log.e("commands.size()", String.valueOf(commandsList.size()));
                        adapter = new Merchant_CommandAdapter(Merchant_CommandsFragment.this, commandsList);
                        commandsListView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        return view;
    }
}

class Merchant_CommandAdapter extends RecyclerView.Adapter<Merchant_CommandAdapter.ViewHolder> {

    Fragment context;
    List<Order> orders;

    public Merchant_CommandAdapter(Fragment context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_layout_commands_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Order order = orders.get(position);
        holder.commandId = order.getId();
        holder.commandMerchantId = order.getMerchantId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClient client = dataSnapshot.child(order.getClientId()).getValue(UserClient.class);
                holder.commandClientName.setText(client.getFirstname());
                holder.commandClientNameValue = client.getFirstname();
                holder.commandClientAddress.setText(client.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        holder.commandTotalPrice.setText(order.getTotalPrice() + " €");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public String commandId;
        public String commandMerchantId;
        public String commandClientNameValue;
        public TextView commandClientName;
        public TextView commandClientAddress;
        public TextView commandTotalPrice;

        public ViewHolder(final View itemView) {
            super(itemView);

            commandClientName = itemView.findViewById(R.id.clientName);
            commandClientAddress = itemView.findViewById(R.id.clientAddress);
            commandTotalPrice = itemView.findViewById(R.id.totalPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Merchant_DetailCommandFragment merchantDetailCommandFragment = new Merchant_DetailCommandFragment();
                    Bundle args = new Bundle();
                    args.putString("commandId", commandId);
                    args.putString("commandMerchantId", commandMerchantId);
                    args.putString("commandClientName", commandClientNameValue);
                    merchantDetailCommandFragment.setArguments(args);
                    context.getFragmentManager().beginTransaction().replace(R.id.fragment_container, merchantDetailCommandFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
