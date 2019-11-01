package com.example.shoppingdrive.Client.Client_Pages.Client_Fragments.Client_FragmentProfileFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.UserClient;
import com.example.shoppingdrive.R;
import com.example.shoppingdrive.Client.Client_DataBaseOffline.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Client_FragmentProfileAddress extends Fragment {
    private String TAG = Client_FragmentProfileAddress.class.getSimpleName();
    private View view;
    private Context mContext;

    private EditText streetAddrress_et;
    private EditText city_et;
    private EditText countryCode_et;
    private EditText country_et;
    private ImageView streetAddrress_iv;
    private ImageView city_iv;
    private ImageView countryCode_iv;
    private ImageView country_iv;

    private DatabaseHelper db;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String clientUid;
    private UserClient client;

    private ProgressDialog mProgressDialog;
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
        db = new DatabaseHelper(mContext);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            clientUid = currentUser.getUid();
        }
        else{
            Toast.makeText(getContext(), "Please login to continue\nor sign up ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_fragment_profile_address, container, false);

        streetAddrress_et = (EditText)view.findViewById(R.id.client_profile_address_street_address);
        city_et = (EditText)view.findViewById(R.id.client_profile_address_city);
        countryCode_et = (EditText)view.findViewById(R.id.client_profile_address_countrycode);
        country_et = (EditText)view.findViewById(R.id.client_profile_address_country);

        streetAddrress_iv = (ImageView)view.findViewById(R.id.client_profile_address_street_address_edit);
        city_iv = (ImageView)view.findViewById(R.id.client_profile_address_city_edit);
        countryCode_iv = (ImageView)view.findViewById(R.id.client_profile_address_countrycode_edit);
        country_iv = (ImageView)view.findViewById(R.id.client_profile_address_country_edit);

        showProgressDialog(true,"Information", "Retrieving address...");
        getUserAddressInfo();
        showProgressDialog(false,null, null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        streetAddrress_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_address_edit,null,false);
                InitStreetAddrressEditPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });
        city_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_address_edit,null,false);
                InitCityEditPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });
        countryCode_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_address_edit,null,false);
                InitCountryCodeEditPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });
        country_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_address_edit,null,false);
                InitCountryEditPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void showProgressDialog(boolean show, String title, String message) {

        if (show) {
            mProgressDialog = new ProgressDialog(mContext);
            if (title != null) mProgressDialog.setTitle(title);
            if (message != null) mProgressDialog.setMessage(message);

            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null) mProgressDialog.dismiss();
        }
    }

    private void getUserAddressInfo(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "DataSnapshot: "+dataSnapshot);
                client = dataSnapshot.getValue(UserClient.class);

                String address = client.getAddress();
                String[] addressSplit = address.split(", ");

                streetAddrress_et.setText(addressSplit[0]);
                city_et.setText(addressSplit[1]);
                countryCode_et.setText(addressSplit[2]);
                country_et.setText(addressSplit[3]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveAddressParts(String xxxx, int i){
        String address = client.getAddress();
        String[] addressSplit = address.split(", ");

        switch (i){
            case 0: addressSplit[i] = xxxx; break;
            case 1: addressSplit[i] = xxxx; break;
            case 2: addressSplit[i] = xxxx; break;
            case 3: addressSplit[i] = xxxx; break;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/address");
        databaseReference.setValue(addressSplit[0]+", "+addressSplit[1]+", "+addressSplit[2]+", "+addressSplit[3]);

        getUserAddressInfo();
    }

    private void InitStreetAddrressEditPopup(View view){
        final EditText xxxx = view.findViewById(R.id.client_dialog_profile_edittext_address_et);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);
        xxxx.setHint("New street name");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xxxx.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xxxx.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(true,"Information", "Updating address...");
                saveAddressParts(xxxx.getText().toString(), 0);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }

    private void InitCityEditPopup(View view){
        final EditText xxxx = view.findViewById(R.id.client_dialog_profile_edittext_address_et);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);
        xxxx.setHint("New City");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xxxx.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xxxx.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog(true,"Information", "Updating address...");
                saveAddressParts(xxxx.getText().toString(), 0);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }

    private void InitCountryCodeEditPopup(View view){
        final EditText xxxx = view.findViewById(R.id.client_dialog_profile_edittext_address_et);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);
        xxxx.setHint("New Country Code");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xxxx.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xxxx.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog(true,"Information", "Updating address...");
                saveAddressParts(xxxx.getText().toString(), 0);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }

    private void InitCountryEditPopup(View view){
        final EditText xxxx = view.findViewById(R.id.client_dialog_profile_edittext_address_et);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);
        xxxx.setHint("New country");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xxxx.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xxxx.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog(true,"Information", "Updating address...");
                saveAddressParts(xxxx.getText().toString(), 0);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }
}
