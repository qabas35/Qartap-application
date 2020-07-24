package com.example.qartal.School;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.Student.StudentInfo;
import com.example.qartal.models.SchoolSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewStudentActivity extends AppCompatActivity implements View.OnClickListener {

    SchoolSessionManager session ;

    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    private EditText User_FullName  ,
            User_Age , User_BloodType , User_Phone ;
    private Button banConfirm ;
    public String  UserID , Full_name  , blood_type , phone , age;
    StudentInfo studentInfo = new StudentInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        session = new SchoolSessionManager(getApplicationContext());

        findViewById(R.id.bktomain).setOnClickListener(this);

        User_FullName = findViewById(R.id.name_add_student);
        User_Age = findViewById(R.id.age_add_student);
        User_BloodType = findViewById(R.id.blood_type_add_student);
        User_Phone = findViewById(R.id.user_phone_add_student);
        banConfirm = findViewById(R.id.conf_butt);


        session.checkLogin();


        mAuth = FirebaseAuth.getInstance();
        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("StudentInfo");
        UserID = firebaseDatabase.push().getKey();

        banConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudentToFirebase();
                Toast.makeText(getApplicationContext(), "Saving Data ..", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void AddStudentToFirebase(){
        Full_name = User_FullName.getText().toString();
        blood_type = User_BloodType.getText().toString();
        phone = User_Phone.getText().toString();
        age = User_Age.getText().toString();


        String Username = studentInfo.UsernameGenerator(Full_name);
        Log.d("Username Value >> ",Username);
        String Password = studentInfo.PasswordGenerator();
        Log.d("Password Value >> ",Password);

        studentInfo = new StudentInfo(UserID,Full_name,Username,Password,phone,age
                ,blood_type,"None");
        firebaseDatabase.child("Student").child(UserID).setValue(studentInfo);

        mAuth.createUserWithEmailAndPassword(Username, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  Toast.makeText(getApplicationContext(),"The User Authenticated Successfully",Toast.LENGTH_LONG).show();
                    Log.d("Register State >> ",task.getResult().toString());
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        Log.d("Register State >> ","You are already registered");

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Register State >> ",task.getException().getMessage());
                    }

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bktomain:{
                Intent go = new Intent(AddNewStudentActivity.this, SchoolMainProfileActivity.class);
                startActivity(go);
            }break;


        }
    }
}
