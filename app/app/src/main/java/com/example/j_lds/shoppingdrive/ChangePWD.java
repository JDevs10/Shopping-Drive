package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePWD extends AppCompatActivity {

    private EditText email_et, pwd_et, cpwd_et;
    private Button cancel, rest;
    private String email_st, pwd_st, cpwd_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //..........................................................................................
        email_et = findViewById(R.id.editText_change_pwd_email);
        pwd_et = findViewById(R.id.editText_change_pwd_pwd);
        cpwd_et = findViewById(R.id.editText_change_pwd_cPWD);

        cancel = findViewById(R.id.button_change_pwd_cancel);
        rest = findViewById(R.id.button_change_pwd_rest);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), " cancel... back to login", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ChangePWD.this, Login.class);
                startActivity(intent);
            }
        });

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), " password rested... back to login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
