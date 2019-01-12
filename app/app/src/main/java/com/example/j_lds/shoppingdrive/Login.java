package com.example.j_lds.shoppingdrive;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button register_button;
    Button login_button;
    Button pwd_button;

    private EditText username_et;
    private EditText user_pwd_et;

    private String userUsernameInputString;
    private String userPwdInputString;

    public User loginUser;

    private DatabaseReference  database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hides App bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //declaration on login side.................................................................
        username_et = findViewById(R.id.editText_username);
        user_pwd_et = findViewById(R.id.editText_user_pwd);

        loginUser = new User();

        //Database..................................................................................
        database = FirebaseDatabase.getInstance("https://test-jl010.firebaseio.com/").getReference();


        login_button = findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register_button = findViewById(R.id.button_register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        pwd_button = findViewById(R.id.button_pwd);
        pwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,ChangePWD.class);
                startActivity(intent);
            }
        });
    }

    private void login() {

    }
}
