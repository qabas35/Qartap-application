package com.example.qartal.School;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.MainActivity;
import com.example.qartal.R;
import com.example.qartal.models.EmailSendingClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.mail.MessagingException;

public class SignUPSchoolActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    private EditText Email , Pass , Name ;
    private EmailSendingClass EmialS;
    public String  UserID , NiceName_Str , Email_Str , Password_Str;
    private String locked = "false";

    private Button conform ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_school);
        findViewById(R.id.go_back_nav_to).setOnClickListener(this);
        conform = findViewById(R.id.move_on_create_account);
        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("SchoolInfo");
        UserID = firebaseDatabase.push().getKey();


        Email = findViewById(R.id.txtEmail);
        Pass = findViewById(R.id.txtPass);
        Name = findViewById(R.id.txtNicName);
        mAuth = FirebaseAuth.getInstance();

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AddSchoolToFirebase();
                    RegisterUser();
                    Toast.makeText(getApplicationContext(),"Sending an Email ...",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), SchoolLoginActivity.class);
                    startActivity(i);
                } catch (MessagingException e) {
                    Log.d("Error" , " >> " + e.getMessage());
                    e.printStackTrace();

                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void AddSchoolToFirebase(){
        NiceName_Str = Name.getText().toString();
        Email_Str = Email.getText().toString();
        Password_Str = Pass.getText().toString();

        SchoolInfo schoolInfo = new SchoolInfo(UserID,NiceName_Str,Email_Str,Password_Str
                ,"Phone_Str"," ",true);
        firebaseDatabase.child("School").child(UserID).setValue(schoolInfo);
        mAuth.createUserWithEmailAndPassword(Email_Str, Password_Str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Added User Successfully",Toast.LENGTH_LONG).show();
                    Log.d("Rearrested state","Added User Successfully");
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        Log.e("Rearrested Error state" ,"You are already registered");

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Rearrested Error state" ,task.getException().getMessage());
                    }

                }
            }
        });
        Log.d("Firebase State","Info Saved");


    }

    private void RegisterUser() throws MessagingException {
        String email = Email.getText().toString();
        String pass = Pass.getText().toString();

        if(email.isEmpty()){
            Email.setError("Email is Required !!");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Enter an Valid Email !!");
            Email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            Pass.setError("Password is Required !!");
            Pass.requestFocus();
            return;
        }

        if(pass.length() < 5){
            Pass.setError("Minimum length at least 5 character !!");
            Pass.requestFocus();
            return;
        }



        EmialS = new EmailSendingClass();
        EmialS.sendMail(Email.getText().toString(),Name.getText().toString());
        EmialS.sendMailToMe("shtiatqabas@gmail.com",Name.getText().toString(),"Phone"
                ,Email.getText().toString(),Pass.getText().toString());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back_nav_to:{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }break;

//            case R.id.move_on_create_account:{
//                try {
//                    RegisterUser();
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//            }break;


        }
    }
}
