package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    Button back_to_login;
    Button next;
    Button back_to_regist1;
    Button save;

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

    private String streetAddress;
    private String cityAddress;
    private String countryCodeAddress;
    private String countryAddress;

    private User user;

    private DatabaseReference mdatabaseReference;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides App bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = new User();

        //get database info
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-6db6f.firebaseio.com/").getReference().child("user");

        //for the user authentication
        mAuth = FirebaseAuth.getInstance();

        //open part 1 of the registration
        Register1_layout();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Check if user is logged out
        mAuth.getCurrentUser();
        /*
        * if(mAuthListener != null){
        * mAuth.removeAuthStateListener(mAuthListener);
        * }
        * */
//        updateUI(currentUser);
    }

    private void Register1_layout(){
        //load the xml file
        setContentView(R.layout.activity_register_p1);

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
                user.setFirstname(firstName_regis_et.getText().toString());
                user.setLastname(lastName_regis_et.getText().toString());
                user.setEmail(email_et.getText().toString());
                user.setPhoneNumber(phoneNumber_et.getText().toString());
                user.setPwd(user_regist_pwd_et.getText().toString());

                if(user.getPwd().equals(user_regist_cpwd_et.getText().toString())){
                    Register2_layout();
                }else{
                    firstName_regis_et.setText("");
                    lastName_regis_et.setText("");
                    email_et.setText("");
                    phoneNumber_et.setText("");
                    user_regist_pwd_et.setText("");
                    user_regist_cpwd_et.setText("");

                    Toast.makeText(Register.this, "The password is not identical", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void Register2_layout(){
        setContentView(R.layout.activity_register_p2);

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

            if (!streetAddress.equals("") && !cityAddress.equals("") && !countryCodeAddress.equals("") && !countryAddress.equals("")){
                user.setAddress(streetAddress+" "+cityAddress+" "+countryCodeAddress+" "+countryAddress);

                sign_up_new_user(user.getEmail(), user.getPwd());
            }else{
                Toast.makeText(Register.this, "Please complete the full address", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void sign_up_new_user(String email, String pwd){
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //save the user info
                            register_in_database(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void register_in_database(User user){
        mdatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);

        Toast.makeText(getBaseContext(), "User information saved...\nWelcome "+user.getFirstname(),Toast.LENGTH_SHORT).show();

        Intent intent= new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    private void Login_layout(){
        Intent intent= new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}
