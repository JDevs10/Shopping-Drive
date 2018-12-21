package com.example.j_lds.shoppingdrive;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    Button register_button;
    Button login_button;
    Button pwd_button;

    private EditText user_et;
    private EditText user_pwd_et;

    private String user_st, pwd_st;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userDbReference;
    private DatabaseReference articleDbReference;
    private DatabaseReference userFirstnameDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //declaration on login side.................................................................
        user_et = findViewById(R.id.editText_user);
        user_pwd_et = findViewById(R.id.editText_user_pwd);

        login_button = findViewById(R.id.button_login);
        pwd_button = findViewById(R.id.button_pwd);
        register_button = findViewById(R.id.button_register);

        //Database..................................................................................
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//        userDbReference = myRef.child("user");
//        articleDbReference = myRef.child("article");
//        userFirstnameDbReference = userDbReference.child("0").child("firstname");


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message");
//
//                myRef.setValue("Hello, World!");
                user_st = user_et.getText().toString().trim();
                pwd_st = user_pwd_et.getText().toString().trim();
                Toast.makeText(getBaseContext(), "Login in",Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(Login.this,FindMerchant.class);
                startActivity(intent);

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        pwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,ChangePWD.class);
                startActivity(intent);
            }
        });
    }


}
