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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingdrive.Client.Client_Interfaces.Client_PaymentTypeListener;
import com.example.shoppingdrive.Login;
import com.example.shoppingdrive.Models.ClientPaymentInfo;
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

public class Client_FragmentProfilePaymentInfo extends Fragment {
    private String TAG = Client_FragmentProfilePaymentInfo.class.getSimpleName();
    private View View;
    private Context mContext;

    private LinearLayout creditCard_ly, discover_ly, paypal_ly;
    private CheckBox creditCard_cb, discover_cb, paypal_cb;
    private ImageView creditCard_btn, discover_btn, paypal_btn;
    private EditText number_cb_et, crypto_cb_et, xdate_cb_et;
    private EditText number_dis_et, crypto_dis_et, xdate_dis_et;
    private EditText number_pp_et, crypto_pp_et, xdate_pp_et;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String clientUid;
    private UserClient client;

    private DatabaseHelper db;

    private Client_PaymentTypeListener mClient_PaymentTypeListener;

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
        View = inflater.inflate(R.layout.client_fragment_profile_payment, container, false);

        creditCard_cb = (CheckBox)View.findViewById(R.id.client_profile_payment_cb_checkbox);
        creditCard_ly = (LinearLayout)View.findViewById(R.id.client_profile_payment_cb_layout);
        creditCard_btn = (ImageView)View.findViewById(R.id.client_profile_payment_cb_edit);

        discover_cb = (CheckBox)View.findViewById(R.id.client_profile_payment_discover_checkbox);
        discover_ly = (LinearLayout)View.findViewById(R.id.client_profile_payment_discover_layout);
        discover_btn = (ImageView)View.findViewById(R.id.client_profile_payment_discover_edit);

        paypal_cb = (CheckBox)View.findViewById(R.id.client_profile_payment_paypal_checkbox);
        paypal_ly = (LinearLayout)View.findViewById(R.id.client_profile_payment_paypal_layout);
        paypal_btn = (ImageView)View.findViewById(R.id.client_profile_payment_paypal_edit);

        showProgressDialog(true,"Information", "Retrieving payment information...");
        getUserPaymentInfo(View);
        showProgressDialog(false,null, null);

        return View;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setPaymentTypeListener();

        creditCard_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (creditCard_cb.isChecked()){
                    creditCard_ly.setVisibility(View.VISIBLE);
                    discover_cb.setChecked(false);
                    discover_ly.setVisibility(View.GONE);
                    paypal_cb.setChecked(false);
                    paypal_ly.setVisibility(View.GONE);

                    mClient_PaymentTypeListener.CreditCardChecked(creditCard_cb.isChecked());
                }else {
                    creditCard_ly.setVisibility(View.GONE);
                }
            }
        });

        discover_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discover_cb.isChecked()){
                    discover_ly.setVisibility(View.VISIBLE);
                    creditCard_cb.setChecked(false);
                    creditCard_ly.setVisibility(View.GONE);
                    paypal_cb.setChecked(false);
                    paypal_ly.setVisibility(View.GONE);

                    mClient_PaymentTypeListener.DiscoverCardChecked(discover_cb.isChecked());
                }else {
                    discover_ly.setVisibility(View.GONE);
                }
            }
        });

        paypal_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paypal_cb.isChecked()){
                    paypal_ly.setVisibility(View.VISIBLE);
                    creditCard_cb.setChecked(false);
                    creditCard_ly.setVisibility(View.GONE);
                    discover_cb.setChecked(false);
                    discover_ly.setVisibility(View.GONE);

                    mClient_PaymentTypeListener.PaypalCardChecked(paypal_cb.isChecked());
                }else {
                    paypal_ly.setVisibility(View.GONE);
                }
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

    private void getUserPaymentInfo(final View view){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/"+clientUid+"/paymentType");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "DataSnapshot: "+dataSnapshot);
                ClientPaymentInfo paymentInfo = dataSnapshot.getValue(ClientPaymentInfo.class);
                switch (paymentInfo.getType()){
                    case "Credit Card":
                        creditCard_cb.setChecked(true);
                        creditCard_ly.setVisibility(View.VISIBLE);
                        discover_cb.setChecked(false);
                        discover_ly.setVisibility(View.GONE);
                        paypal_cb.setChecked(false);
                        paypal_ly.setVisibility(View.GONE);

                        mClient_PaymentTypeListener.CreditCardChecked(creditCard_cb.isChecked());

                        number_cb_et = view.findViewById(R.id.client_profile_payment_cb_number_et);
                        crypto_cb_et = view.findViewById(R.id.client_profile_payment_cb_crypto_et);
                        xdate_cb_et = view.findViewById(R.id.client_profile_payment_cb_xdate_et);

                        number_dis_et = view.findViewById(R.id.client_profile_payment_discover_number_et);
                        crypto_dis_et = view.findViewById(R.id.client_profile_payment_discover_crypto_et);
                        xdate_dis_et = view.findViewById(R.id.client_profile_payment_discover_xdate_et);

                        number_pp_et = view.findViewById(R.id.client_profile_payment_paypal_number_et);
                        crypto_pp_et = view.findViewById(R.id.client_profile_payment_paypal_crypto_et);
                        xdate_pp_et = view.findViewById(R.id.client_profile_payment_paypal_xdate_et);

                        number_cb_et.setText(""+paymentInfo.getNumber());
                        crypto_cb_et.setText(""+paymentInfo.getCrypto());
                        xdate_cb_et.setText(paymentInfo.getExpireDate());

                        number_dis_et.setText("");
                        crypto_dis_et.setText("");
                        xdate_dis_et.setText("");

                        number_pp_et.setText("");
                        crypto_pp_et.setText("");
                        xdate_pp_et.setText("");
                        break;
                    case "Discover Card":
                        discover_cb.setChecked(true);
                        discover_ly.setVisibility(View.VISIBLE);
                        creditCard_cb.setChecked(false);
                        creditCard_ly.setVisibility(View.GONE);
                        paypal_cb.setChecked(false);
                        paypal_ly.setVisibility(View.GONE);

                        mClient_PaymentTypeListener.DiscoverCardChecked(discover_cb.isChecked());

                        number_cb_et = view.findViewById(R.id.client_profile_payment_cb_number_et);
                        crypto_cb_et = view.findViewById(R.id.client_profile_payment_cb_crypto_et);
                        xdate_cb_et = view.findViewById(R.id.client_profile_payment_cb_xdate_et);

                        number_dis_et = view.findViewById(R.id.client_profile_payment_discover_number_et);
                        crypto_dis_et = view.findViewById(R.id.client_profile_payment_discover_crypto_et);
                        xdate_dis_et = view.findViewById(R.id.client_profile_payment_discover_xdate_et);

                        number_pp_et = view.findViewById(R.id.client_profile_payment_paypal_number_et);
                        crypto_pp_et = view.findViewById(R.id.client_profile_payment_paypal_crypto_et);
                        xdate_pp_et = view.findViewById(R.id.client_profile_payment_paypal_xdate_et);

                        number_dis_et.setText(""+paymentInfo.getNumber());
                        crypto_dis_et.setText(""+paymentInfo.getCrypto());
                        xdate_dis_et.setText(paymentInfo.getExpireDate());

                        number_cb_et.setText("");
                        crypto_cb_et.setText("");
                        xdate_cb_et.setText("");

                        number_pp_et.setText("");
                        crypto_pp_et.setText("");
                        xdate_pp_et.setText("");
                        break;
                    case "Paypal Card":
                        paypal_cb.setChecked(true);
                        paypal_ly.setVisibility(View.VISIBLE);
                        creditCard_cb.setChecked(false);
                        creditCard_ly.setVisibility(View.GONE);
                        discover_cb.setChecked(false);
                        discover_ly.setVisibility(View.GONE);

                        mClient_PaymentTypeListener.PaypalCardChecked(paypal_cb.isChecked());

                        number_cb_et = view.findViewById(R.id.client_profile_payment_cb_number_et);
                        crypto_cb_et = view.findViewById(R.id.client_profile_payment_cb_crypto_et);
                        xdate_cb_et = view.findViewById(R.id.client_profile_payment_cb_xdate_et);

                        number_dis_et = view.findViewById(R.id.client_profile_payment_discover_number_et);
                        crypto_dis_et = view.findViewById(R.id.client_profile_payment_discover_crypto_et);
                        xdate_dis_et = view.findViewById(R.id.client_profile_payment_discover_xdate_et);

                        number_pp_et = view.findViewById(R.id.client_profile_payment_paypal_number_et);
                        crypto_pp_et = view.findViewById(R.id.client_profile_payment_paypal_crypto_et);
                        xdate_pp_et = view.findViewById(R.id.client_profile_payment_paypal_xdate_et);

                        number_pp_et.setText(""+paymentInfo.getNumber());
                        crypto_pp_et.setText(""+paymentInfo.getCrypto());
                        xdate_pp_et.setText(paymentInfo.getExpireDate());

                        number_dis_et.setText("");
                        crypto_dis_et.setText("");
                        xdate_dis_et.setText("");

                        number_cb_et.setText("");
                        crypto_cb_et.setText("");
                        xdate_cb_et.setText("");
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setPaymentTypeListener(){
        mClient_PaymentTypeListener = new Client_PaymentTypeListener() {
            @Override
            public void CreditCardChecked(boolean isChecked) {
                creditCard_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_payment_edit,null,false);
                        InitCreditCardEditPopup(v);
                        builder.setView(v);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void DiscoverCardChecked(boolean isChecked) {
                discover_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_payment_edit,null,false);
                        InitDiscoverCardEditPopup(v);
                        builder.setView(v);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void PaypalCardChecked(boolean isChecked) {
                paypal_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_payment_edit,null,false);
                        InitPaypalCardEditPopup(v);
                        builder.setView(v);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        };
    }

    private void InitCreditCardEditPopup(View view){
        final EditText cardNumber = view.findViewById(R.id.client_dialog_profile_edittext_number);
        final EditText cardSecurityCode = view.findViewById(R.id.client_dialog_profile_edittext_securitycode);
        final EditText cardXDate = view.findViewById(R.id.client_dialog_profile_edittext_xdate);
        TextView title = view.findViewById(R.id.client_dialog_textView_detail_order_toolbar);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        title.setText("Credit Card Info");
        cardNumber.setHint("Card number");
        cardSecurityCode.setHint("Crypto Code");
        cardXDate.setHint("Expire date");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber.setText("");
                cardSecurityCode.setText("");
                cardXDate.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Card number field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardSecurityCode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Crypto Code field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardXDate.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Expire date field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(true,"Information", "Updating payment information...");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/type");
                databaseReference.setValue("Credit Card");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/number");
                databaseReference.setValue(Long.valueOf(cardNumber.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/crypto");
                databaseReference.setValue(Long.valueOf(cardSecurityCode.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/expireDate");
                databaseReference.setValue(cardXDate.getText().toString());
                getUserPaymentInfo(View);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }

    private void InitDiscoverCardEditPopup(View view){
        final EditText cardNumber = view.findViewById(R.id.client_dialog_profile_edittext_number);
        final EditText cardSecurityCode = view.findViewById(R.id.client_dialog_profile_edittext_securitycode);
        final EditText cardXDate = view.findViewById(R.id.client_dialog_profile_edittext_xdate);
        TextView title = view.findViewById(R.id.client_dialog_textView_detail_order_toolbar);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        title.setText("Discover Card Info");
        cardNumber.setHint("Card number");
        cardSecurityCode.setHint("Crypto Code");
        cardXDate.setHint("Expire date");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber.setText("");
                cardSecurityCode.setText("");
                cardXDate.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Card number field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardSecurityCode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Crypto Code field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardXDate.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Expire date field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(true,"Information", "Updating payment information...");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/type");
                databaseReference.setValue("Discover Card");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/number");
                databaseReference.setValue(Long.valueOf(cardNumber.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/crypto");
                databaseReference.setValue(Long.valueOf(cardSecurityCode.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/expireDate");
                databaseReference.setValue(cardXDate.getText().toString());
                getUserPaymentInfo(View);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }

    private void InitPaypalCardEditPopup(View view){
        final EditText cardNumber = view.findViewById(R.id.client_dialog_profile_edittext_number);
        final EditText cardSecurityCode = view.findViewById(R.id.client_dialog_profile_edittext_securitycode);
        final EditText cardXDate = view.findViewById(R.id.client_dialog_profile_edittext_xdate);
        TextView title = view.findViewById(R.id.client_dialog_textView_detail_order_toolbar);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        title.setText("Paypal Card Info");
        cardNumber.setHint("Card number");
        cardSecurityCode.setHint("Crypto Code");
        cardXDate.setHint("Expire date");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber.setText("");
                cardSecurityCode.setText("");
                cardXDate.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Card number field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardSecurityCode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Crypto Code field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardXDate.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The Expire date field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(true,"Information", "Updating payment information...");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/type");
                databaseReference.setValue("Paypal Card");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/number");
                databaseReference.setValue(Long.valueOf(cardNumber.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/crypto");
                databaseReference.setValue(Long.valueOf(cardSecurityCode.getText().toString()));

                databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid+"/paymentType/expireDate");
                databaseReference.setValue(cardXDate.getText().toString());
                getUserPaymentInfo(View);
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });
    }
}
