package com.example.j_lds.shoppingdrive;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private Button register_button;
    private Button login_button;
    private Button pwd_button;
    private Button save;

    private EditText user_et;
    private EditText user_pwd_et;
    private EditText userName_regis_et;
    private EditText user_pwd_regis_et;
    private EditText email_et;
    private EditText phoneNumber_et;
    private EditText address_et;
    private EditText zipCode_et;

    private String user, pwd, userName, user_pwd, email, phoneNumber, address, zipCode;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //declaration on login side.................................................................
        user_et = findViewById(R.id.editText_user);
        user_pwd_et = findViewById(R.id.editText_pwd);
        login_button = findViewById(R.id.button_login);
        pwd_button = findViewById(R.id.button_pwd);
        register_button = findViewById(R.id.button_register);

        //declaration on register side..............................................................
        userName_regis_et = findViewById(R.id.editText_userName);
        user_pwd_regis_et = findViewById(R.id.editText_user_pwd);
        email_et = findViewById(R.id.editText_email);
        phoneNumber_et = findViewById(R.id.editText_phoneNumber);
        address_et = findViewById(R.id.editText_address);
        zipCode_et = findViewById(R.id.editText_zipCode);

        save = findViewById(R.id.button_save);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_layout();
            }
        });
    }

    private void register_layout(){
        setContentView(R.layout.layout_register);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        myRef.setValue("Hello, World!");
    }

    private void Register(){

    }
}
