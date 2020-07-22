package com.example.qartal.School;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.models.SchoolSessionManager;

public class HealthStateSchoolActivity extends AppCompatActivity implements View.OnClickListener {

    SchoolSessionManager session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_state_student);

        session = new SchoolSessionManager(getApplicationContext());

        session.checkLogin();

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.go_back_school_profile_2 : {
//                Intent go = new Intent(HealthStateSchoolActivity.this, SchoolMainProfileActivity.class);  //link two screens.
//                startActivity(go);
//            }break;
//        }
    }
}
