package com.example.qartal.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qartal.R;
import com.example.qartal.models.EditAddressDialog;
import com.example.qartal.models.EditEmailDialog;
import com.example.qartal.models.EditNameDialog;
import com.example.qartal.models.EditPasswordDialog;
import com.example.qartal.models.EditPhoneDialog;
import com.example.qartal.models.SchoolSessionManager;
import com.example.qartal.models.StudentSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class StudentEditProfileActivity extends AppCompatActivity implements View.OnClickListener
        , EditNameDialog.EditNameDialogListener , EditAddressDialog.EditAddressDialogListener ,
        EditEmailDialog.EditEmailDialogListener , EditPhoneDialog.EditPhoneDialogListener ,
        EditPasswordDialog.EditPasswordDialogListener {

    StudentSessionManager session ;

    private TextView name_view , email_view , address_view , phone_view , email_headLine , name_headLine ;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private String UserID;
    String old_pass = "";

    String Email_Get_2 , Name_Get_2 ;
    String email , name;

    StudentInfo studentInfo = new StudentInfo();

    private StorageReference mStorageRef;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 71 ;
    private ImageView profile_image;

    private FirebaseStorage storage_d;
    private StorageReference storageRef_d;
    private  StorageReference islandRef;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        session = new StudentSessionManager(getApplicationContext());

        name_view = findViewById(R.id.nameTextView_student);
        email_view = findViewById(R.id.emailTextView_student);
        address_view = findViewById(R.id.addressTextView_student);
        phone_view = findViewById(R.id.phoneTextView_student);
        email_headLine = findViewById(R.id.email_headline_edit_student);
        name_headLine = findViewById(R.id.name_headline_edit_student);

        findViewById(R.id.edit_name_student).setOnClickListener(this);
        findViewById(R.id.edit_email_student).setOnClickListener(this);
        findViewById(R.id.edit_address_student).setOnClickListener(this);
        findViewById(R.id.edit_password_student).setOnClickListener(this);
        findViewById(R.id.edit_phone_student).setOnClickListener(this);
        findViewById(R.id.back_to_home_btn_student).setOnClickListener(this);
        profile_image = findViewById(R.id.profile_img_edit_student);

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("StudentInfo");
        UserID = firebaseDatabase.push().getKey();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // download
        storage_d = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage_d.getReference();

        session.checkLogin();

        HashMap<String,String > schoolUser = session.getUserDetails();

         name  = schoolUser.get(StudentSessionManager.KEY_NAME);
         email = schoolUser.get(StudentSessionManager.KEY_EMAIL);

        name_headLine.setText(name);
        email_headLine.setText(email);

        ReadNiceNameFromFirebase();

        name_view.setText(studentInfo.FullName);
        studentInfo.ImageBitmapStringValue = schoolUser.get(SchoolSessionManager.KEY_IMAGE);

        studentInfo.ImageProfileID = "image/"+ name.toLowerCase()
                .replaceAll("'","").replaceAll(" ","_")+"_profile_img";



        profile_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        if(studentInfo.ImageBitmapStringValue == null) {
            boolean ImageState_l = getImageFromFirebase();
            if(!ImageState_l){
                Drawable myDrawable = getResources().getDrawable(R.drawable.profile_edit_w);
                profile_image.setImageDrawable(myDrawable);
            }
        }else {
            Bitmap image_view_rec = SchoolSessionManager.decodeBase64(studentInfo.ImageBitmapStringValue);
            profile_image.setImageBitmap(image_view_rec);
            Log.d("Image_str State : ",studentInfo.ImageBitmapStringValue);

        }




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
                profile_image.setImageBitmap(image);
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
                            studentInfo.ID = dataSnapshot2.child("ID").getValue(String.class);
                            studentInfo.FullName = dataSnapshot2.child("FullName").getValue(String.class);
                            studentInfo.Username = dataSnapshot2.child("Username").getValue(String.class);
                            studentInfo.Phone = dataSnapshot2.child("Phone").getValue(String.class);
                            studentInfo.Address = dataSnapshot2.child("Address").getValue(String.class);
                        }
                        Log.d("Firebase State","Read Name Successful" +" >> " + studentInfo.FullName );
                        Log.d("Firebase State","Read Email Successful" +" >> " + studentInfo.Username);
                        Log.d("Firebase State","Read Phone Successful" +" >> " + studentInfo.Phone);
                        Log.d("Firebase State","Read Address Successful" +" >> " + studentInfo.Address);
                        Log.d("Firebase State","Read ID Successful" +" >> " +  studentInfo.ID);

                    }
                }
                name_view.setText( studentInfo.FullName);
                email_view.setText(studentInfo.Username);
                address_view.setText(studentInfo.Address);
                phone_view.setText(studentInfo.Phone);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private boolean chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

        return true;
    }

    private void uploadImage(Uri image_path){
        if(image_path != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Upload Image ...");
            progressDialog.show();

            StorageReference ref = mStorageRef.child("image/"+ name.toLowerCase()
                    .replaceAll("'","").replaceAll(" ","_")+"_profile_img");
            ref.putFile(image_path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Image not Uploaded",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount() );
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profile_image.setImageBitmap(bitmap);
                String image_temp = SchoolSessionManager.encodeTobase64(bitmap);
                session.createImageSession(image_temp,filePath.toString());
                uploadImage(filePath);
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }






    public void updateInfo(String filed , String child){
        firebaseDatabase.child("Student").child(studentInfo.ID).child(child).setValue(filed);
    }

    public void OpenEditNameDialog(){
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(getSupportFragmentManager(),"Edit Name Dialog");

    }

    public void OpenEditEmailDialog(){
        EditEmailDialog editEmailDialog = new EditEmailDialog();
        editEmailDialog.show(getSupportFragmentManager(),"Edit Email Dialog");

    }

    public void OpenEditPhoneDialog(){
        EditPhoneDialog editPhoneDialog = new EditPhoneDialog();
        editPhoneDialog.show(getSupportFragmentManager(),"Edit Phone Dialog");

    }

    public void OpenEditAddressDialog(){
        EditAddressDialog editAddressDialog = new EditAddressDialog();
        editAddressDialog.show(getSupportFragmentManager(),"Edit Address Dialog");

    }

    public void OpenEditPasswordDialog(){
        EditPasswordDialog editPasswordDialog = new EditPasswordDialog();
        editPasswordDialog.show(getSupportFragmentManager(),"Edit Password Dialog");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_home_btn_student:{
                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                startActivity(intent);
            }break;

            case R.id.edit_name_student:{
                OpenEditNameDialog();
            }break;

            case R.id.edit_email_student:{
                OpenEditEmailDialog();
            }break;

            case R.id.edit_address_student:{
                OpenEditAddressDialog();
            }break;

            case R.id.edit_phone_student:{
                OpenEditPhoneDialog();
            }break;

            case R.id.edit_password_student:{
                OpenEditPasswordDialog();
            }break;


        }
    }

    @Override
    public void TransferNameText(String username) {
        name_view.setText(username);
        name_headLine.setText(username);
        updateInfo(username,"FullName");

    }

    @Override
    public void TransferAddressText(String address) {
        address_view.setText(address);
        updateInfo(address,"Address");
    }

    @Override
    public void TransferEmailText(String Email) {
        email_view.setText(Email);
        email_headLine.setText(Email);
        updateInfo(Email,"Username");
    }

    @Override
    public void TransferPhoneText(String phone) {
        phone_view.setText(phone);
        updateInfo(phone,"Phone");

    }

    @Override
    public void TransferPasswordText(String Old_password, String New_Password) {

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        old_pass = dataSnapshot2.child("Password").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        if(old_pass.equals(Old_password)){
            updateInfo(New_Password,"Password");
        }else{
            Toast.makeText(getApplicationContext(),"The Old Password is Wrong",Toast.LENGTH_LONG).show();
        }
    }
}

