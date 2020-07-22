package com.example.qartal.School;

import android.annotation.SuppressLint;
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


public class SchoolEditProfileActivity extends AppCompatActivity implements View.OnClickListener
        , EditNameDialog.EditNameDialogListener , EditAddressDialog.EditAddressDialogListener ,
        EditEmailDialog.EditEmailDialogListener , EditPhoneDialog.EditPhoneDialogListener ,
        EditPasswordDialog.EditPasswordDialogListener {

    SchoolSessionManager session ;

    private TextView name_view , email_view , address_view , phone_view , email_headLine , name_headLine ;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    private String UserID;
    String old_pass = "";

    String Email_Get_2 , Name_Get_2 ;

    String email , name ;

    SchoolInfo schoolInfo = new SchoolInfo();

    private StorageReference mStorageRef;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 71 ;
    private static final int CAMERA_REQUEST_CODE = 1 ;
    private ImageView profile_image;

    private StorageReference storageRef;

    private FirebaseStorage storage_d;
    private StorageReference storageRef_d;
    private  StorageReference islandRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_edit_profile);

        session = new SchoolSessionManager(getApplicationContext());

        name_view = findViewById(R.id.nameTextView_school);
        email_view = findViewById(R.id.emailTextView_school);
        address_view = findViewById(R.id.addressTextView_school);
        phone_view = findViewById(R.id.phoneTextView_school);
        email_headLine = findViewById(R.id.email_headline_edit_school);
        name_headLine = findViewById(R.id.name_headline_edit_school);

        findViewById(R.id.edit_name_school).setOnClickListener(this);
        findViewById(R.id.edit_email_school).setOnClickListener(this);
        findViewById(R.id.edit_address_school).setOnClickListener(this);
        findViewById(R.id.edit_password_school).setOnClickListener(this);
        findViewById(R.id.edit_phone_school).setOnClickListener(this);
        findViewById(R.id.back_to_home_btn_school).setOnClickListener(this);
        profile_image = findViewById(R.id.profile_img_edit_school);

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("SchoolInfo");
        UserID = firebaseDatabase.push().getKey();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // download
        storage_d = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage_d.getReference();

        session.checkLogin();

        HashMap<String,String > schoolUser = session.getUserDetails();

         name  = schoolUser.get(SchoolSessionManager.KEY_NAME);
         email = schoolUser.get(SchoolSessionManager.KEY_EMAIL);

        name_headLine.setText(name);
        email_headLine.setText(email);


        ReadNiceNameFromFirebase();

        name_view.setText(schoolInfo.NiceName);

        schoolInfo.ImageBitmapStringValue = schoolUser.get(SchoolSessionManager.KEY_IMAGE);

        schoolInfo.ImageProfileID = "image/"+ name.toLowerCase()
                .replaceAll("'","").replaceAll(" ","_")+"_profile_img";

        profile_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        if(schoolInfo.ImageBitmapStringValue == null) {
            boolean ImageState_l = getImageFromFirebase();
            if(!ImageState_l){
                Drawable myDrawable = getResources().getDrawable(R.drawable.profile_edit_w);
                profile_image.setImageDrawable(myDrawable);
            }
        }else {
            Bitmap image_view_rec = SchoolSessionManager.decodeBase64(schoolInfo.ImageBitmapStringValue);
            profile_image.setImageBitmap(image_view_rec);
            Log.d("Image_str State : ",schoolInfo.ImageBitmapStringValue);

        }


    }

    private boolean getImageFromFirebase(){
        // [START download_to_memory]
        islandRef = storageRef.child(schoolInfo.ImageProfileID);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                schoolInfo.ImageBitmapStringValue = image.toString();
                profile_image.setImageBitmap(image);
                schoolInfo.ImageState = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                schoolInfo.ImageState = false;
                Log.e("image_str Value State", String.valueOf(schoolInfo.ImageState));
            }
        });
        return schoolInfo.ImageState;
        // [END download_to_memory]
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    void ReadNiceNameFromFirebase(){
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        if(email.equals(dataSnapshot2.child("Email").getValue(String.class))){
                            schoolInfo.ID = dataSnapshot2.child("ID").getValue(String.class);
                            schoolInfo.NiceName = dataSnapshot2.child("NiceName").getValue(String.class);
                            schoolInfo.Email = dataSnapshot2.child("Email").getValue(String.class);
                            schoolInfo.Phone = dataSnapshot2.child("Phone").getValue(String.class);
                            schoolInfo.Address = dataSnapshot2.child("Address").getValue(String.class);
                        }
                        Log.d("Firebase State","Read Name Successful" +" >> " + schoolInfo.NiceName );
                        Log.d("Firebase State","Read Email Successful" +" >> " + schoolInfo.Email);
                        Log.d("Firebase State","Read Phone Successful" +" >> " + schoolInfo.Phone);
                        Log.d("Firebase State","Read Address Successful" +" >> " + schoolInfo.Address);
                        Log.d("Firebase State","Read ID Successful" +" >> " + schoolInfo.ID);

                    }
                }
                name_view.setText( schoolInfo.NiceName);
                email_view.setText(schoolInfo.Email);
                address_view.setText(schoolInfo.Address);
                phone_view.setText(schoolInfo.Phone);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
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
        firebaseDatabase.child("School").child(schoolInfo.ID).child(child).setValue(filed);
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
            case R.id.back_to_home_btn_school:{
                Intent intent = new Intent(getApplicationContext(), SchoolMainProfileActivity.class);
                startActivity(intent);
            }break;

            case R.id.edit_name_school:{
                OpenEditNameDialog();
            }break;

            case R.id.edit_email_school:{
                OpenEditEmailDialog();
            }break;

            case R.id.edit_address_school:{
                OpenEditAddressDialog();
            }break;

            case R.id.edit_phone_school:{
                OpenEditPhoneDialog();
            }break;

            case R.id.edit_password_school:{
                OpenEditPasswordDialog();
            }break;



        }
    }

    @Override
    public void TransferNameText(String username) {
        name_view.setText(username);
        name_headLine.setText(username);
        updateInfo(username,"NiceName");

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
        updateInfo(Email,"Email");
    }

    @Override
    public void TransferPhoneText(String phone) {
        phone_view.setText(phone);
        updateInfo(phone,"Phone");

    }

    private void ReadPasswordFromFirebase(){
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){  // row read
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){ // column read
                        schoolInfo.Password = dataSnapshot2.child("Password").getValue(String.class);
                    }
                }
                Log.d("Firebase State","Read Password Successful" +" >> " + schoolInfo.Password );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void TransferPasswordText(String Old_password, String New_Password) {

        ReadPasswordFromFirebase();

        if(schoolInfo.Password != null) {
            if (Old_password.equals(schoolInfo.Password)) {
                updateInfo(New_Password, "Password");
                Log.d("Password Change State : ", "Done, The New Password is : " + New_Password);
            } else {
                Toast.makeText(getApplicationContext(), "The Old Password is Wrong", Toast.LENGTH_LONG).show();
                Log.d("Password Change State : ", "The Old Password is Wrong");
            }
        }else {
            Log.d("Password Change State : ", "schoolInfo.Password is null");
        }

    }
}
