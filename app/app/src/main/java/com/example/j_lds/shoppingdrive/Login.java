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

import com.example.j_lds.shoppingdrive.object_class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button register_button;
    Button login_button;
    Button pwd_button;

    private EditText user_email_et;
    private EditText user_pwd_et;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private int tries = 0;

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
                /*
                Intent intent= new Intent(Login.this,Register.class);
                startActivity(intent);
                */
                Intent intent= new Intent(Login.this,Home.class);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null){
            currentUser = mAuth.getCurrentUser();
        }
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(tries < 3){
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                currentUser = mAuth.getCurrentUser();
                                Toast.makeText(Login.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                                Intent intent= new Intent(Login.this, FindMerchant.class);
                                intent.putExtra("user_logged_id", currentUser.getUid());
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                tries++;
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Login.this, "Authentication failed.\nWe advise you to reset your password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
