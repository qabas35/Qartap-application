package com.example.qartal.School;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qartal.R;
import com.example.qartal.models.SchoolSessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HealthStateSchoolActivity extends AppCompatActivity implements View.OnClickListener {

    SchoolSessionManager session ;
    private DatabaseReference healthRef ;
    private FirebaseAuth mAuth;
    private String User_id;
    private String Data ;
    private TextView health_viewer_1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_state_schoole);

        session = new SchoolSessionManager(getApplicationContext());

        session.checkLogin();

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.go_back_student_profile).setOnClickListener(this);
        findViewById(R.id.health_go_recycle).setOnClickListener(this);


        health_viewer_1 = findViewById(R.id.coming_health_value);


        healthRef = FirebaseDatabase.getInstance().getReference();
        healthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data = snapshot.child("HealthInfo").child("BPM").getValue().toString();
                health_viewer_1.setText("BPM : " +Data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Faild To Get BPM",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back_student_profile : {
                Intent go = new Intent(HealthStateSchoolActivity.this, SchoolMainProfileActivity.class);  //link two screens.
                startActivity(go);
            }break;

            case R.id.health_go_recycle:{
                Intent go = new Intent(HealthStateSchoolActivity.this, HealthViewActivity.class);  //link two screens.
                startActivity(go);
            }break;

        }
    }


}
