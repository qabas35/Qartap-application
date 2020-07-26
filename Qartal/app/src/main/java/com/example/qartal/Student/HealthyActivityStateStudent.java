package com.example.qartal.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.models.SchoolSessionManager;
import com.example.qartal.models.StudentSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HealthyActivityStateStudent extends AppCompatActivity implements View.OnClickListener {

    StudentSessionManager session ;
    private DatabaseReference healthRef ;
    private String Data ,Student_name ;
    private TextView health_viewer_1 ,name_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_state_student);
        findViewById(R.id.health_bake_to_profile).setOnClickListener(this);

        session = new StudentSessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String,String > studentUser = session.getUserDetails();

        Student_name  = studentUser.get(StudentSessionManager.KEY_NAME);

        health_viewer_1 = findViewById(R.id.student_health_view);
        name_view = findViewById(R.id.student_name_view);

        name_view.setText(Student_name);

        healthRef = FirebaseDatabase.getInstance().getReference();
        healthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data = snapshot.child("HealthInfo").child("BPM").getValue().toString();
                health_viewer_1.setText(Data);
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
            case R.id.health_bake_to_profile: {
                Intent go = new Intent(HealthyActivityStateStudent.this, StudentProfileActivity.class);  //link two screens.
                startActivity(go);
            }break;
        }
    }
}
