package com.example.qartal.School;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class HealthStateSchoolActivity extends AppCompatActivity implements View.OnClickListener {

    SchoolSessionManager session ;
//    private RecyclerView health_description_school_r;
    private DatabaseReference healthRef , StudentRef;
    private FirebaseAuth mAuth;
    private String User_id;
    private String Data;
    private TextView health_viewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_state_schoole);

        session = new SchoolSessionManager(getApplicationContext());

        session.checkLogin();

        mAuth = FirebaseAuth.getInstance();
//        User_id = mAuth.getCurrentUser().getUid();
//        healthRef = FirebaseDatabase.getInstance().getReference().child("HeathInfo").child(User_id);
//        StudentRef = FirebaseDatabase.getInstance().getReference().child("StudentInfo");

        findViewById(R.id.go_back_student_profile).setOnClickListener(this);
        findViewById(R.id.health_go_recycle).setOnClickListener(this);

//        health_description_school_r = findViewById(R.id.health_recycle_list);
//        health_description_school_r.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        //  DisplayHeathInfo();

        health_viewer = findViewById(R.id.coming_health_value);
        healthRef = FirebaseDatabase.getInstance().getReference();
        healthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data = snapshot.child("irValue").getValue().toString();

                health_viewer.setText(Data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*
//    HealthState.class,
//    R.layout.all_user_display_health_layout,
//    HealthViewHolder.class,
//    healthRef

    void DisplayHeathInfo(){
        FirebaseRecyclerAdapter<HealthState,HealthViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<HealthState, HealthViewHolder>() {

            @Override
            protected void onBindViewHolder(@NonNull final HealthViewHolder holder, int position, @NonNull HealthState model) {
                String StudentIDs = getRef(position).getKey();
                StudentRef.child(StudentIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final String username = snapshot.child("FullName").getValue().toString();
                            holder.setFullName(username);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public HealthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }

    public static class HealthViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public HealthViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setFullName(String fullName){
            TextView name = mView.findViewById(R.id.health_name_view);
            name.setText(fullName);
        }
        public void setHealthState(String healthState){
            TextView health_state = mView.findViewById(R.id.health_state_view);
            health_state.setText(healthState);
        }
    }
    */
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
