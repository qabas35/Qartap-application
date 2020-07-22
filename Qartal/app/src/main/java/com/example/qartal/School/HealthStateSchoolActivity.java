package com.example.qartal.School;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

        findViewById(R.id.health_bake_to_profile).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.health_bake_to_profile : {
                Intent go = new Intent(HealthStateSchoolActivity.this, SchoolMainProfileActivity.class);  //link two screens.
                startActivity(go);
            }break;
        }
    }
}
