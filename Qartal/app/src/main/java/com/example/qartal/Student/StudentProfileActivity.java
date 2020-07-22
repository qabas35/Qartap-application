package com.example.qartal.Student;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.MainActivity;
import com.example.qartal.R;
import com.example.qartal.SettingsActivity;
import com.example.qartal.models.SchoolSessionManager;
import com.example.qartal.models.StudentSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class StudentProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    StudentSessionManager session ;

    private static final int PICK_IMAGE_REQUEST = 71 ;
    private TextView Name_View , Email_View ;

    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;

    private FirebaseStorage storage_d;
    private StorageReference storageRef;
    private  StorageReference islandRef;

    private ImageView profile_img;

    String email , name  ,filePath_str;

    StudentInfo studentInfo = new StudentInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studen_profile);

        session = new StudentSessionManager(getApplicationContext());

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("StudentInfo");


        // download
        storage_d = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage_d.getReference();

        // set On Click Listener nav
        findViewById(R.id.health_btn_student).setOnClickListener(this);
        findViewById(R.id.student_location_student).setOnClickListener(this);
        findViewById(R.id.go_back_profile_student).setOnClickListener(this);
        findViewById(R.id.btn_setting_profile_student).setOnClickListener(this);
        findViewById(R.id.btn_edit_profile_student).setOnClickListener(this);
        findViewById(R.id.btn_Signout_student).setOnClickListener(this);
        Name_View = findViewById(R.id.name_view_profile_student);
        Email_View = findViewById(R.id.email_view_profile_student);
        profile_img = findViewById(R.id.profile_img_profile_student);

        Toast.makeText(getApplicationContext(),"User Login State : " + session.isLoggedIn(),Toast.LENGTH_LONG).show();

        session.checkLogin();

        HashMap<String,String > schoolUser = session.getUserDetails();

        name  = schoolUser.get(StudentSessionManager.KEY_NAME);
        email = schoolUser.get(StudentSessionManager.KEY_EMAIL);
        studentInfo.ImageBitmapStringValue = schoolUser.get(SchoolSessionManager.KEY_IMAGE);
        filePath_str = schoolUser.get(SchoolSessionManager.KEY_FILE_PATH);

        // ReadNiceNameFromFirebase();

        Name_View.setText(name);
        Email_View.setText(email);

//        studentInfo.ImageProfileID = "image/"+ name.toLowerCase()
//                .replaceAll("'","").replaceAll(" ","_")+"_profile_img";
//
//        if(studentInfo.ImageBitmapStringValue == null) {
//            boolean ImageState_l = getImageFromFirebase();
//            if(!ImageState_l){
//                Drawable myDrawable = getResources().getDrawable(R.drawable.person_icon);
//                profile_img.setImageDrawable(myDrawable);
//            }
//
//        }else {
//            Bitmap image_view_rec = SchoolSessionManager.decodeBase64(studentInfo.ImageBitmapStringValue);
//            profile_img.setImageBitmap(image_view_rec);
//            Log.d("Image_str State : ",studentInfo.ImageBitmapStringValue);
//        }



    }

    private boolean getImageFromFirebase(){
        // [START download_to_memory]
        islandRef = storageRef.child(studentInfo.ImageProfileID);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                studentInfo.ImageBitmapStringValue = image.toString();
                profile_img.setImageBitmap(image);
                studentInfo.ImageState = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                studentInfo.ImageState = false;
                Log.e("image_str Value State", String.valueOf(studentInfo.ImageState));
            }
        });
        return studentInfo.ImageState;
        // [END download_to_memory]
    }

    void ReadNiceNameFromFirebase(){

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        if(email.equals(dataSnapshot2.child("Username").getValue(String.class))){
                            studentInfo.FullName = dataSnapshot2.child("FullName").getValue(String.class);
                        }
                        Log.d("Firebase State","Read Name Successful" +" >> " + studentInfo.FullName );
                    }
                }
                Name_View.setText(studentInfo.FullName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.health_btn_student :{

                Intent i = new Intent(getApplicationContext(), HealthyActivityStateStudent.class);
                startActivity(i);

            }break;
            case R.id.student_location_student :{
                Intent i = new Intent(getApplicationContext(), ViewMapStudentActivity.class);
                startActivity(i);


            }break;


            case R.id.go_back_profile_student:{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }break;

            case R.id.btn_setting_profile_student:{
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }break;

            case R.id.btn_edit_profile_student:{
                Intent i = new Intent(getApplicationContext(), StudentEditProfileActivity.class);
                startActivity(i);
            }break;

            case  R.id.btn_Signout_student:{
                Intent go = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(go);
                FirebaseAuth.getInstance().signOut();
                session.logoutUser();
            }break;
        }

    }
}
