package com.example.qartal.School;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.MainActivity;
import com.example.qartal.R;
import com.example.qartal.Splash;
import com.example.qartal.models.LoadingDialoge;
import com.example.qartal.models.SchoolSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolLoginActivity extends AppCompatActivity implements View.OnClickListener , DialogInterface {

    SchoolSessionManager session ;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private Button login;
    private TextView Signup;
    private FirebaseAuth mAuth;
    private EditText Email , Pass ;
    private String  Email_Str , Password_Str ;
    public String Name_Str;
    SchoolInfo schoolInfo  = new SchoolInfo();
    final LoadingDialoge loadingDialoge = new LoadingDialoge(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        session = new SchoolSessionManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.txtEmaillog);
        Pass = findViewById(R.id.txtPasslog);
        findViewById(R.id.twits_img).setOnClickListener(this);
        findViewById(R.id.textViewSignup).setOnClickListener(this);



        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("SchoolInfo");

        login = findViewById(R.id.buttonLogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = Pass.getText().toString();
                if(email.isEmpty()){
                    Email.setError("Enter Your Email !!");
                    Email.requestFocus();
                    return;
                }
                else if(pass.isEmpty()){
                    Pass.setError("Enter Your Password !!");
                    Pass.requestFocus();
                }

                LoginSoGood(email,pass);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    void ReadNiceNameFromFirebase(){

        Email_Str = Email.getText().toString();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        if(Email_Str.equals(dataSnapshot2.child("Email").getValue(String.class))){
                            schoolInfo.NiceName = dataSnapshot2.child("NiceName").getValue(String.class);
                        }
                        Log.d("Firebase State","Read Name Successful" +" >> " + schoolInfo.NiceName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
    private void LoginSoGood(final String email, String pass){

        ReadNiceNameFromFirebase();
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Welcome " + schoolInfo.NiceName,Toast.LENGTH_LONG).show();
                            schoolInfo.Email = email;
                            session.createLoginSession(schoolInfo.NiceName, schoolInfo.Email);
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    Intent i = new Intent(getBaseContext(), SchoolMainProfileActivity.class);
                                    startActivity(i);
                                }
                            }, 5000);
                        }

                        else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.twits_img :{
                Intent i = new Intent(SchoolLoginActivity.this, SchoolMainProfileActivity.class);
                startActivity(i);
            }break;

            case R.id.textViewSignup:{
                Intent i = new Intent(SchoolLoginActivity.this, SignUPSchoolActivity.class);
                startActivity(i);
            }break;

        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }
}
