package com.example.qartal.Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.models.StudentSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudenLoginActivity extends AppCompatActivity implements View.OnClickListener {

    StudentSessionManager session ;
    private Button login;
    private TextView Signup;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private EditText Email , Pass ;
    StudentInfo studentInfo = new StudentInfo();
    private String Email_Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studen_login);

        session = new StudentSessionManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("StudentInfo");

        Email = findViewById(R.id.txtEmail_student);
        Pass = findViewById(R.id.txtPass_student);
        findViewById(R.id.twits_img).setOnClickListener(this);

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

    void ReadNiceNameFromFirebase(){

        Email_Str = Email.getText().toString();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        if(Email_Str.equals(dataSnapshot2.child("Username").getValue(String.class))){
                            studentInfo.FullName = dataSnapshot2.child("FullName").getValue(String.class);
                        }
                        Log.d("Firebase State","Read Name Successful" +" >> " + studentInfo.FullName);
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
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(StudenLoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    session.createLoginSession(studentInfo.FullName, email);
                    Toast.makeText(getApplicationContext(),"Welcome " + studentInfo.FullName,Toast.LENGTH_LONG).show();
                    Intent i = new Intent(StudenLoginActivity.this, StudentProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login Error , Login Again .. ",Toast.LENGTH_LONG).show();
                    Log.e("Login Faild State : ",task.getException().getMessage());

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
                Intent i = new Intent(StudenLoginActivity.this, StudentProfileActivity.class);
                startActivity(i);
            }break;


        }
    }
}
