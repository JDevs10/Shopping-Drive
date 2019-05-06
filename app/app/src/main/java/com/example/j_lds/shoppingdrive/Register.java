package com.example.j_lds.shoppingdrive;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.j_lds.shoppingdrive.object_class.User;
import com.example.j_lds.shoppingdrive.object_class.UserClient;
import com.example.j_lds.shoppingdrive.object_class.UserMerchant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button back_to_login;
    Button next;
    Button back_to_regist1;
    Button save;
    Button btn_getImage;

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
    private TextView imageName_tv;
    private EditText campanyName_et;

    private Spinner roles;

    private String streetAddress;
    private String cityAddress;
    private String countryCodeAddress;
    private String countryAddress;
    private String roleSet = "";
    private String campanyName;

    private UserClient userClient;
    private UserMerchant userMerchant;

    private DatabaseReference mdatabaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri selectedImage;
    private String picName;

    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides App bar at the top.................................................................
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get database / user section info
        mdatabaseReference = FirebaseDatabase.getInstance("https://shopping-drive-4bdce.firebaseio.com/").getReference().child("user");

        //get Shopping Drive Firebase Storage
        mStorageRef = FirebaseStorage.getInstance("gs://shopping-drive-4bdce.appspot.com").getReference();

        //for the user authentication
        mAuth = FirebaseAuth.getInstance();

        //open part 1 of the registration
        Register1_layout();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        roleSet = parent.getItemAtPosition(position).toString();
        if (roleSet.equals("Merchant")){
            campanyName_et.setVisibility(View.VISIBLE);
        }else{
            campanyName_et.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.getCurrentUser();
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
        campanyName_et = (EditText)findViewById(R.id.editText_companyName);

        roles = (Spinner)findViewById(R.id.spinner_user_role);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roles.setAdapter(arrayAdapter);
        roles.setOnItemSelectedListener(this);

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
                if (!roleSet.equals("Select role")) {
                    switch (roleSet) {
                        case "Client":
                            userClient = new UserClient();
                            userClient.setFirstname(firstName_regis_et.getText().toString());
                            userClient.setLastname(lastName_regis_et.getText().toString());
                            userClient.setEmail(email_et.getText().toString());
                            userClient.setPhoneNumber(phoneNumber_et.getText().toString());
                            userClient.setPwd(user_regist_pwd_et.getText().toString());
                            userClient.setRole(roleSet);

                            if (!firstName_regis_et.getText().equals("") && !lastName_regis_et.getText().equals("")
                                    && !email_et.getText().equals("") && !phoneNumber_et.getText().equals("") && !user_regist_pwd_et.getText().equals("")) {
                                if (userClient.getPwd().equals(user_regist_cpwd_et.getText().toString())) {
                                    Register2_layout();
                                } else {
                                    firstName_regis_et.setText("");
                                    lastName_regis_et.setText("");
                                    email_et.setText("");
                                    phoneNumber_et.setText("");
                                    user_regist_pwd_et.setText("");
                                    user_regist_cpwd_et.setText("");

                                    Toast.makeText(Register.this, "The password is not identical", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Merchant":
                            userMerchant = new UserMerchant();
                            userMerchant.setFirstname(firstName_regis_et.getText().toString());
                            userMerchant.setLastname(lastName_regis_et.getText().toString());
                            userMerchant.setEmail(email_et.getText().toString());
                            userMerchant.setPhoneNumber(phoneNumber_et.getText().toString());
                            userMerchant.setPwd(user_regist_pwd_et.getText().toString());
                            userMerchant.setRole(roleSet);
                            userMerchant.setCompanyName(campanyName_et.getText().toString());

                            if (!firstName_regis_et.getText().equals("") && !lastName_regis_et.getText().equals("")
                                    && !email_et.getText().equals("") && !phoneNumber_et.getText().equals("") && !user_regist_pwd_et.getText().equals("")) {
                                if (userMerchant.getPwd().equals(user_regist_cpwd_et.getText().toString())) {
                                    Register2_layout();
                                } else {
                                    firstName_regis_et.setText("");
                                    lastName_regis_et.setText("");
                                    email_et.setText("");
                                    phoneNumber_et.setText("");
                                    user_regist_pwd_et.setText("");
                                    user_regist_cpwd_et.setText("");

                                    Toast.makeText(Register.this, "The password is not identical", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }else{
                    Toast.makeText(Register.this, "Please select your role !", Toast.LENGTH_SHORT).show();
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
        imageName_tv = (TextView)findViewById(R.id.textView_image);

        btn_getImage = (Button)findViewById(R.id.button_getImage);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        if(roleSet.equals("Merchant")){
            imageName_tv.setVisibility(View.VISIBLE);
            btn_getImage.setVisibility(View.VISIBLE);
        }else {
            imageName_tv.setVisibility(View.GONE);
            btn_getImage.setVisibility(View.GONE);
        }

        back_to_regist1 = findViewById(R.id.button_back_regist1);
        save = findViewById(R.id.button_save);

        back_to_regist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register1_layout();
            }
        });

        switch (roleSet){
            case "Client":
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        streetAddress = streetAddress_et.getText().toString().trim();
                        cityAddress = cityAddress_et.getText().toString().trim();
                        countryCodeAddress = countryCodeAddress_et.getText().toString().trim();
                        countryAddress = countryAddress_et.getText().toString().trim();

                        if (!streetAddress.equals("") && !cityAddress.equals("") &&
                                !countryCodeAddress.equals("") && !countryAddress.equals("")){

                            userClient.setAddress(streetAddress+" "+cityAddress+" "+countryCodeAddress+" "+countryAddress);
                            sign_up_new_user(userClient.getEmail(), userClient.getPwd());

                        }else{
                            Toast.makeText(Register.this, "Please complete the full address", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Merchant":
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        streetAddress = streetAddress_et.getText().toString().trim();
                        cityAddress = cityAddress_et.getText().toString().trim();
                        countryCodeAddress = countryCodeAddress_et.getText().toString().trim();
                        countryAddress = countryAddress_et.getText().toString().trim();

                        if (!streetAddress.equals("") && !cityAddress.equals("") &&
                                !countryCodeAddress.equals("") && !countryAddress.equals("")){

                            userMerchant.setAddress(streetAddress+" "+cityAddress+" "+countryCodeAddress+" "+countryAddress);

                            final StorageReference filePath = mStorageRef.child("Merchant_store_image").child(picName);

                            filePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            userMerchant.setCompanyLogo(uri.toString());
                                            Log.d("Merchant Img || ", "onSuccess: uri= "+ uri.toString());
                                            Log.d("Merchant Img || ", "onSuccess: user= "+ userMerchant.getCompanyLogo());
                                        }
                                    });
                                    sign_up_new_user(userMerchant.getEmail(), userMerchant.getPwd());

                                    Toast.makeText(Register.this, "Upload finish... ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Toast.makeText(Register.this, "Upload ERROR: Something went wrong...\n"+exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }else{
                            Toast.makeText(Register.this, "Please complete the full address", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                selectedImage = data.getData();

                picName = selectedImage.getLastPathSegment().replace(selectedImage.getLastPathSegment(), userMerchant.getCompanyName() + "_logo");
                imageName_tv.setText("Image : " + picName);

            } else {
                Toast.makeText(this, "You haven't picked any Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR : getting Image => Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void sign_up_new_user(String email, String pwd){
        switch (roleSet){
            case "Client":
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //save the user info
                                    register_in_database();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case "Merchant":
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //save the user info
                                    register_in_database();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }

    }

    private void register_in_database(){
        switch (roleSet) {
            case "Client":
                mdatabaseReference.child("client/"+mAuth.getCurrentUser().getUid()).setValue(userClient);

                Toast.makeText(getBaseContext(), "User information saved...\nWelcome "+userClient.getFirstname(),Toast.LENGTH_SHORT).show();
                break;
            case "Merchant":
                mdatabaseReference.child("merchant/"+mAuth.getCurrentUser().getUid()).setValue(userMerchant);

                Toast.makeText(getBaseContext(), "User information saved...\nWelcome "+userMerchant.getFirstname(),Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent= new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    private void Login_layout(){
        Intent intent= new Intent(Register.this, Login.class);
        startActivity(intent);
    }


}