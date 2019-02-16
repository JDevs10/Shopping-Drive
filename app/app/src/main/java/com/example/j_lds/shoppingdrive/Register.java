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
    private String roleSet;
    private String campanyName;

    private User user;

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

        user = new User();

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
        if(roleSet.equals("Merchant")){
            imageName_tv.setVisibility(View.VISIBLE);
            btn_getImage.setVisibility(View.VISIBLE);
        }else {
            imageName_tv.setVisibility(View.GONE);
            btn_getImage.setVisibility(View.GONE);
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

//    @Override
//    public void onStop() {
//        super.onStop();
//        // Check if user is logged out
//        mAuth.getCurrentUser();
//        /*
//        * if(mAuthListener != null){
//        * mAuth.removeAuthStateListener(mAuthListener);
//        * }
//        * */
//    }

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
                campanyName = campanyName_et.getText().toString().trim();
                user.setCompanyName(campanyName_et.getText().toString());

                if (!firstName_regis_et.getText().equals("") && !lastName_regis_et.getText().equals("")
                        && !email_et.getText().equals("") && !phoneNumber_et.getText().equals("") && !user_regist_pwd_et.getText().equals("")){
                    if(user.getPwd().equals(user_regist_cpwd_et.getText().toString())){
                        Register2_layout();
                    }else{
                        firstName_regis_et.setText("");
                        lastName_regis_et.setText("");
                        email_et.setText("");
                        phoneNumber_et.setText("");
                        user_regist_pwd_et.setText("");
                        user_regist_cpwd_et.setText("");
                        campanyName_et.setText("");

                        Toast.makeText(Register.this, "The password is not identical", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
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

        roles = (Spinner)findViewById(R.id.spinner_user_role);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roles.setAdapter(arrayAdapter);
        roles.setOnItemSelectedListener(this);

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

                if (!streetAddress.equals("") && !cityAddress.equals("") && !countryCodeAddress.equals("") &&
                        !countryAddress.equals("") && !roleSet.equals("Select role") && !campanyName.equals("")){

                    user.setAddress(streetAddress+" "+cityAddress+" "+countryCodeAddress+" "+countryAddress);
                    user.setRole(roleSet);

                    final StorageReference filePath = mStorageRef.child("Merchant_img").child(picName);

                    filePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    user.setCompanyLogo(uri.toString());
                                    Log.d("Merchant Img || ", "onSuccess: uri= "+ uri.toString());
                                    Log.d("Merchant Img || ", "onSuccess: user= "+ user.getCompanyLogo());
                                }
                            });
                            sign_up_new_user(user.getEmail(), user.getPwd());

                            Toast.makeText(Register.this, "Upload finish... ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(Register.this, "Upload ERROR: Something went wrong... ", Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(Register.this, "Please complete the full address", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

                picName = selectedImage.getLastPathSegment().replace(selectedImage.getLastPathSegment(), user.getCompanyName()+"_logo");
                imageName_tv.setText("Image : "+picName);

            } else {
                Toast.makeText(this, "You haven't picked any Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR : getting Image => Something went wrong", Toast.LENGTH_LONG).show();
        }
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
