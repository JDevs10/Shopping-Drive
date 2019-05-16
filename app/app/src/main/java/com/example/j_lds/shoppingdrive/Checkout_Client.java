package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Checkout_Client extends AppCompatActivity {

    private Button btn_backWhereTheUserWas, btn_order, btn_continue_order;
    private EditText et_pwd;
    private TextView tv_checkout_p2;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_client);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_backWhereTheUserWas = (Button)findViewById(R.id.button_checkout_cancel);
        btn_order = (Button)findViewById(R.id.button_checkout_confirmOrder);

        btn_backWhereTheUserWas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_to_userHome();
            }
        });

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_pwd();
            }
        });

        et_pwd = (EditText)findViewById(R.id.editText_checkout_getPWD);
    }

    private void confirm_pwd(){
        pwd = et_pwd.getText().toString();

        // check the user password from the bd and the user input
        if (pwd.equals("hello")){
            Toast.makeText(getBaseContext(), "Good password... sending your order", Toast.LENGTH_SHORT).show();
            checkout_p2(10);

        }else{
            Toast.makeText(getBaseContext(), "Password is incorrect...", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkout_p2(int time){
        setContentView(R.layout.activity_checkout_p2_client);

        //Hides MACC bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_checkout_p2 = (TextView)findViewById(R.id.textView_checkout_p2_info);
        tv_checkout_p2.setText("We've got your order. Your Items will be ready-delivered within "+(time-3)+"-"+(time+3)+" minutes");

        btn_continue_order = (Button)findViewById(R.id.button_back_to_findMerchantList);
        btn_continue_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Checkout_Client.this, Home_Client.class);
                startActivity(intent);
            }
        });
    }

    private void back_to_userHome(){
        Intent intent= new Intent(Checkout_Client.this, Home_Client.class);
        startActivity(intent);
    }
}
