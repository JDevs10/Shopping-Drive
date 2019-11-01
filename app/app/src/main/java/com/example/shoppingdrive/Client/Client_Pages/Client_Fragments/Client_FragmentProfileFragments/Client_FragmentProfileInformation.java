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
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Client_FragmentProfileInformation extends Fragment {
    private String TAG = Client_FragmentProfileInformation.class.getSimpleName();
    private View view;
    private Context mContext;

    private Button pwd_btn;
    private EditText editEmail;
    private EditText editPhoneNumber;

    private ImageView emailEdit_iv;
    private ImageView phoneNumberEdit_iv;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String clientUid;

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
        view = inflater.inflate(R.layout.client_fragment_profile_information, container, false);

        pwd_btn = (Button)view.findViewById(R.id.client_profile_password_btn);
        editEmail = view.findViewById(R.id.client_profile_email);
        editPhoneNumber = view.findViewById(R.id.client_profile_phone_number);

        emailEdit_iv = (ImageView)view.findViewById(R.id.client_profile_email_edit);
        phoneNumberEdit_iv = (ImageView)view.findViewById(R.id.client_profile_phonenumber_edit);

        showProgressDialog(true,"Information", "Retrieving general information...");
        getUserInfo();
        showProgressDialog(false,null, null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_email_edit,null,false);
                InitNewPasswordPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });

        emailEdit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_profile_email_edit,null,false);
                InitEmailEditPopup(v);
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });

        phoneNumberEdit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.client_dialog_country_code__phone_number_edit,null,false);
                InitPhoneEditPopup(v);
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

    private void getUserInfo(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user/client/" + clientUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClient client = dataSnapshot.getValue(UserClient.class);
                editEmail.setText(client.getEmail());
                editPhoneNumber.setText(client.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitNewPasswordPopup(View view){
        final EditText pwd_et = view.findViewById(R.id.client_dialog_profile_edittext_email);
        final EditText cpwd_et = view.findViewById(R.id.client_dialog_profile_edittext_cpwd);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        pwd_et.setHint("New password");
        cpwd_et.setHint("Confirm password");

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwd_et.setText("");
                cpwd_et.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pwd_et.getText().toString().isEmpty() || cpwd_et.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd_et.getText().toString().trim().equals(cpwd_et.getText().toString().trim())){
                    showProgressDialog(true,"Information", "Updating password...");

                    currentUser.updatePassword(pwd_et.getText().toString().trim());
                    databaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid);
                    databaseReference.child("password").setValue(pwd_et.getText().toString().trim());
                    showProgressDialog(false, null, null);

                    Toast.makeText(getContext(), "New Password Saved!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }else{
                    Toast.makeText(getContext(), "The new email is not valide!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InitEmailEditPopup(View view){
        final EditText email_et = view.findViewById(R.id.client_dialog_profile_edittext_email);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_et.setText("");
                dialog.dismiss();
            }
        });

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_et.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "The field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString().trim()).matches()){
                    showProgressDialog(true,"Information", "Updating email...");
                    currentUser.updateEmail(email_et.getText().toString().trim());
                    databaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid);
                    databaseReference.child("email").setValue(email_et.getText().toString().trim());
                    getUserInfo();
                    showProgressDialog(false, null, null);

                    Toast.makeText(getContext(), "Valide Email!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }else{
                    Toast.makeText(getContext(), "The new email is not valide!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InitPhoneEditPopup(View view){
        final EditText phoneNumber_et = view.findViewById(R.id.client_dialog_profile_edittext_phonenumber);
        ImageView cancel_iv = view.findViewById(R.id.client_dialog_profile_btn_cancel);
        Button save_et = view.findViewById(R.id.client_dialog_profile_btn_save);

        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneNumber_et.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "The field 'Phone Number' is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(true,"Information", "Updating phone number...");
                databaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user/client/"+clientUid);
                databaseReference.child("phoneNumber").setValue(phoneNumber_et.getText().toString().trim());
                getUserInfo();
                showProgressDialog(false, null, null);
                dialog.dismiss();
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber_et.setText("");
                dialog.dismiss();
            }
        });
    }
}
