package com.example.qartal.School;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qartal.MainActivity;
import com.example.qartal.R;
import com.example.qartal.SettingsActivity;
import com.example.qartal.models.SchoolSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class SchoolMainProfileActivity extends AppCompatActivity  implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;
    SchoolSessionManager session ;
    private ImageView Profile_image;
    public String Email_Get , Name_Get ;
    String name , email , filePath_str;
    private StorageReference mStorageRef;

    private FirebaseStorage storage_d;
    private StorageReference storageRef;
    private  StorageReference islandRef;

    SchoolInfo schoolInfo = new SchoolInfo();

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_main_profile);

        session = new SchoolSessionManager(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // upload
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // download
        storage_d = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage_d.getReference();

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("SchoolInfo");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        findViewById(R.id.add_new_btn_school).setOnClickListener(this);

        findViewById(R.id.health_btn_school).setOnClickListener(this);

        findViewById(R.id.student_location_school).setOnClickListener(this);

        findViewById(R.id.btn_Signout_school).setOnClickListener(this);



        Toast.makeText(getApplicationContext(),"User Login State : " + session.isLoggedIn(),Toast.LENGTH_LONG).show();

        session.checkLogin();

        HashMap<String,String > schoolUser = session.getUserDetails();

        name  = schoolUser.get(SchoolSessionManager.KEY_NAME);
        email = schoolUser.get(SchoolSessionManager.KEY_EMAIL);
        schoolInfo.ImageBitmapStringValue = schoolUser.get(SchoolSessionManager.KEY_IMAGE);
        filePath_str = schoolUser.get(SchoolSessionManager.KEY_FILE_PATH);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView Email_View_header = headerView.findViewById(R.id.name_view_profile_school);
        Email_View_header.setText(email);
        TextView Name_View_header = headerView.findViewById(R.id.email_view_profile_school);
        Name_View_header.setText(name);
        Profile_image = headerView.findViewById(R.id.profile_img_profile_school);

        /*
        schoolInfo.ImageProfileID = "image/"+ name.toLowerCase()
                .replaceAll("'","").replaceAll(" ","_")+"_profile_img";


        if(schoolInfo.ImageBitmapStringValue == null) {
            boolean ImageState_l = getImageFromFirebase();
            if(!ImageState_l){
                Drawable myDrawable = getResources().getDrawable(R.drawable.school_icon_background);
                Profile_image.setImageDrawable(myDrawable);
            }

        }else {
            Bitmap image_view_rec = SchoolSessionManager.decodeBase64(schoolInfo.ImageBitmapStringValue);
            Profile_image.setImageBitmap(image_view_rec);
            Log.d("Image_str State : ",schoolInfo.ImageBitmapStringValue);
        }
         */

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
                Profile_image.setImageBitmap(image);
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

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis;
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("bitmap State", "Error getting bitmap", e);
        }
        return bm;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_btn_school :{
                Intent go = new Intent(SchoolMainProfileActivity.this, AddNewStudentActivity.class);  //link two screens.
                startActivity(go);
            }break;

            case R.id.health_btn_school :{
                Intent go = new Intent(SchoolMainProfileActivity.this, HealthStateSchoolActivity.class);
                startActivity(go);
            }break;




            case R.id.student_location_school:{
                Intent go = new Intent(SchoolMainProfileActivity.this, StudentLocationActivity.class);
                startActivity(go);
            }break;

            case R.id.btn_Signout_school:{
                Intent go = new Intent(SchoolMainProfileActivity.this, MainActivity.class);
                startActivity(go);
                FirebaseAuth.getInstance().signOut();
                session.logoutUser();
            }break;


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.school_main_profile, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:{
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_Edit_Profile:{
                Intent intent = new Intent(this, SchoolEditProfileActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_Exit:{
                onExit();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onExit() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}