package com.example.shoppingdrive;


import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import com.example.shoppingdrive.Client.Client_Pages.Client_Home;
import com.example.shoppingdrive.Merchant.Merchant_MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button register_button;
    private Button login_button;
    private Button pwd_button;

    private EditText user_email_et;
    private EditText user_pwd_et;

    private Spinner spinner;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabaseReference;

    private ProgressDialog mProgressDialog;
    private int tries = 0;

    private boolean onLoginIsSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hides App bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //declaration on login side.................................................................
        user_email_et = findViewById(R.id.editText_user_email);
        user_pwd_et = findViewById(R.id.editText_user_pwd);

        //initialize the FirebaseAuth instance......................................................
        mAuth = FirebaseAuth.getInstance();

        spinner = (Spinner)findViewById(R.id.spinner_login);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Login_Spinner_Item_Roles, R.layout.activity_login_spinner_role_custom);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        login_button = findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(user_email_et.getText().toString().trim(), user_pwd_et.getText().toString().trim());
            }
        });

        register_button = findViewById(R.id.button_register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        pwd_button = findViewById(R.id.button_pwd);
        pwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this, ChangePWD.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null){
            currentUser = mAuth.getCurrentUser();
        }
    }

    private void login(String email, String password) {
        showProgressDialog(true, "Login", "Connection...");

        if (user_email_et.getText().toString().isEmpty()){
            Toast.makeText(Login.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
            showProgressDialog(false,"","");
            return;
        }
        if (user_pwd_et.getText().toString().isEmpty()){
            Toast.makeText(Login.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
            showProgressDialog(false,"","");
            return;
        }
        if (spinner.getSelectedItem().toString().equals("Select role")){
            Toast.makeText(Login.this, "Please select your role!", Toast.LENGTH_SHORT).show();
            showProgressDialog(false,"","");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(tries < 3){
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                currentUser = mAuth.getCurrentUser();
                                getUserRole(spinner.getSelectedItem().toString(), currentUser.getUid());

                            } else {
                                // If sign in fails, display a message to the user.
                                tries++;
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                showProgressDialog(false,"","");
                            }
                        }else {
                            Toast.makeText(Login.this, "Authentication failed.\nWe advise you to reset your password", Toast.LENGTH_SHORT).show();
                            showProgressDialog(false,"","");
                        }
                    }
                });

    }

    private void getUserRole(final String role, String userUid){
        char c;
        final String newRole;
        final String[] getUserRole = new String[1];

        //replace firt character to LowerCase
        c = Character.toLowerCase(role.charAt(0));
        newRole = role.replace(role.charAt(0), c);
        //Log.e("getUserRole() ", "newRole: "+newRole);

        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/")
                .getReference().child("user/"+newRole+"/"+userUid+"/role");

        //Log.e("getUserRole() ", " database root: "+mdatabaseReference);
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    getUserRole[0] = dataSnapshot.getValue().toString();

                    if (getUserRole[0].equals("Client")) {
                        showProgressDialog(false, "", "");
                        Toast.makeText(Login.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(Login.this, Client_Home.class);
                        intent1.putExtra("user_logged_id", currentUser.getUid());
                        startActivity(intent1);

                    } else if (getUserRole[0].equals("Merchant")) {
                        showProgressDialog(false, "", "");
                        Toast.makeText(Login.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                        Intent intent2 = new Intent(Login.this, Merchant_MainActivity.class);
                        intent2.putExtra("user_logged_id", currentUser.getUid());
                        startActivity(intent2);
                    }

                }else{
                    Toast.makeText(Login.this, "Account not found", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false,"","");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "ERROR : "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showProgressDialog(boolean show, String title, String message) {

        if (show) {
            mProgressDialog = new ProgressDialog(this);
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
}
