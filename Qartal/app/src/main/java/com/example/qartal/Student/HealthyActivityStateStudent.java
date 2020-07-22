package com.example.qartal.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.models.StudentSessionManager;

public class HealthyActivityStateStudent extends AppCompatActivity implements View.OnClickListener {

    StudentSessionManager session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_state_student);
        findViewById(R.id.health_bake_to_profile).setOnClickListener(this);

        session = new StudentSessionManager(getApplicationContext());
        session.checkLogin();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.health_bake_to_profile: {
                Intent go = new Intent(HealthyActivityStateStudent.this, StudentProfileActivity.class);  //link two screens.
                startActivity(go);
            }break;
        }
    }
}
