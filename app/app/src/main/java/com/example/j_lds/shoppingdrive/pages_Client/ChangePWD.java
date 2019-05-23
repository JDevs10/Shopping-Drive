package com.example.j_lds.shoppingdrive.pages_Client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePWD extends AppCompatActivity {

    private EditText email_et;
    private Button cancel, rest;
    private String email_st;

    private FirebaseAuth mAuth;

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

        cancel = findViewById(R.id.button_change_pwd_cancel);
        rest = findViewById(R.id.button_change_pwd_rest);

        mAuth = FirebaseAuth.getInstance();

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
                email_st = email_et.getText().toString().trim();

                if (email_st.isEmpty()){
                    Toast.makeText(ChangePWD.this, "Please write a valid email", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(email_st).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ChangePWD.this, "Please check your "+email_st+" account...", Toast.LENGTH_SHORT).show();

                                Intent intent= new Intent(ChangePWD.this, Login.class);
                                startActivity(intent);
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(ChangePWD.this, "ERROR Occurred ::\n"+error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
