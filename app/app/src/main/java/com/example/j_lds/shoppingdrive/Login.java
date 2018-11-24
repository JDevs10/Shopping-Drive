package com.example.j_lds.shoppingdrive;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    Button register_button;
    Button login_button;
    Button pwd_button;

    Button back_to_login;
    Button next;

    Button back_to_regist1;
    Button save;

    private EditText user_et;
    private EditText user_pwd_et;
    private EditText firstName_regis_et;
    private EditText lastName_regis_et;
    private EditText email_et;
    private EditText phoneNumber_et;
    private EditText user_regist_pwd_et;
    private EditText user_regist_cpwd_et;

    private EditText streetAddress_et;
    private EditText cityAddress_et;
    private EditText countryCodeAddress_et;
    private EditText countryAddress_et;

    private String user_st, pwd_st, firstname, lastname, email, phoneNumber, pwd, confirm_pwd, address, streetAddress, cityAddress, countryCodeAddress, countryAddress;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userDbReference;
    private DatabaseReference articleDbReference;
    private DatabaseReference userFirstnameDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Database..................................................................................
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
//        userDbReference = myRef.child("user");
//        articleDbReference = myRef.child("article");
//        userFirstnameDbReference = userDbReference.child("0").child("firstname");


        Login_layout();
    }

    private void Login_layout(){
        setContentView(R.layout.activity_login);

        //declaration on login side.................................................................
        user_et = findViewById(R.id.editText_user);
        user_pwd_et = findViewById(R.id.editText_user_pwd);

        login_button = findViewById(R.id.button_login);
        pwd_button = findViewById(R.id.button_pwd);
        register_button = findViewById(R.id.button_register);

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
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register1_layout();
            }
        });
    }

    private void Register1_layout(){
        setContentView(R.layout.layout_register_p1);

        //declaration on register side..............................................................
        firstName_regis_et = findViewById(R.id.editText_firstName);
        lastName_regis_et = findViewById(R.id.editText_lastName);
        email_et = findViewById(R.id.editText_email);
        phoneNumber_et = findViewById(R.id.editText_phoneNumber);
        user_regist_pwd_et = findViewById(R.id.editText_user_regist_pwd);
        user_regist_cpwd_et = findViewById(R.id.editText_confirm_user_pwd);

        back_to_login = findViewById(R.id.button_backToLogin);
        next = findViewById(R.id.button_next);

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_layout();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = firstName_regis_et.getText().toString();
                lastname = lastName_regis_et.getText().toString();
                email = email_et.getText().toString();
                phoneNumber = phoneNumber_et.getText().toString();
                pwd = user_regist_pwd_et.getText().toString();
                confirm_pwd = user_regist_cpwd_et.getText().toString();
                Register2_layout();
            }
        });

    }
    private void Register2_layout(){
        setContentView(R.layout.layout_register_p2);

        streetAddress_et = findViewById(R.id.editText_street_address);
        cityAddress_et = findViewById(R.id.editText_city_address);
        countryCodeAddress_et = findViewById(R.id.editText_country_code_address);
        countryAddress_et = findViewById(R.id.editText_country_address);

        back_to_regist1 = findViewById(R.id.button_back_regist1);
        save = findViewById(R.id.button_save);

        back_to_regist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register1_layout();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetAddress = streetAddress_et.getText().toString().trim();
                cityAddress = cityAddress_et.getText().toString().trim();
                countryCodeAddress = countryCodeAddress_et.getText().toString().trim();
                countryAddress = countryAddress_et.getText().toString().trim();
                address = streetAddress+" "+cityAddress+" "+countryCodeAddress+" "+countryAddress;

                Toast.makeText(getBaseContext(), "saved!!! ",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
