package com.example.qartal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.School.SchoolLoginActivity;
import com.example.qartal.School.SchoolMainProfileActivity;
import com.example.qartal.Student.StudenLoginActivity;
import com.example.qartal.Student.StudentProfileActivity;
import com.example.qartal.models.SchoolSessionManager;
import com.example.qartal.models.StudentSessionManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SchoolSessionManager session_school ;
    StudentSessionManager session_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session_school = new SchoolSessionManager(getApplicationContext());
        session_student = new StudentSessionManager(getApplicationContext());


        findViewById(R.id.all_user_student).setOnClickListener(this);
        findViewById(R.id.all_user_school).setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_user_student:{
                if(session_student.isLoggedIn()){
                    Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(getApplicationContext(), StudenLoginActivity.class);
                    startActivity(i);
                }

            }break;

            case R.id.all_user_school:{
                if(session_school.isLoggedIn()){
                    Intent i2 = new Intent(getApplicationContext(), SchoolMainProfileActivity.class);
                    startActivity(i2);
                }else {
                    Intent i2 = new Intent(getApplicationContext(), SchoolLoginActivity.class);
                    startActivity(i2);
                }

            }break;

        }
    }
}
