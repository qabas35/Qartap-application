package com.example.qartal.School;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qartal.R;
import com.example.qartal.models.SchoolSessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HealthViewActivity extends AppCompatActivity implements View.OnClickListener {


    SchoolSessionManager session ;
    private DatabaseReference healthRef ;
    private FirebaseAuth mAuth;
    private String User_id;
    private String Data ,Student_name_health_str;
    private TextView  health_viewer_2 , Student_name_health;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_view);


        session = new SchoolSessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String,String > schoolUser = session.getUserDetails();

        Student_name_health_str  = schoolUser.get(SchoolSessionManager.KEY_NAME);

        mAuth = FirebaseAuth.getInstance();

        Student_name_health = findViewById(R.id.health_name_view);
        health_viewer_2 = findViewById(R.id.health_state_view);
        findViewById(R.id.back_to_home_btn_school).setOnClickListener(this);

        Student_name_health.setText(Student_name_health_str);


        healthRef = FirebaseDatabase.getInstance().getReference();
        healthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data = snapshot.child("irValue").getValue().toString();
                health_viewer_2.setText("BPM : " +Data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Faild To Get BPM",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to_home_btn_school:{
                Intent go = new Intent(HealthViewActivity.this, HealthStateSchoolActivity.class);  //link two screens.
                startActivity(go);
            }
        }
    }
}