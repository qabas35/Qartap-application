package com.example.qartal.models;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.qartal.MainActivity;
import com.example.qartal.School.SchoolLoginActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SchoolSessionManager {

    // Shared Preferences
    SharedPreferences pref_school;

    // Editor for Shared preferences
    SharedPreferences.Editor editor_school;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref_school";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn_school";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name_school";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email_school";

    // Profile Image (make variable public to access from outside)
    public static  final String KEY_IMAGE = "Image_school";

    // Image File Path (make variable public to access from outside)
    public static final String KEY_FILE_PATH = "filePath_school";


    // Constructor
    public SchoolSessionManager(Context context){
        this._context = context;
        pref_school = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor_school = pref_school.edit();
    }



    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor_school.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor_school.putString(KEY_NAME, name);

        // Storing email in pref
        editor_school.putString(KEY_EMAIL, email);

        // commit changes
        editor_school.commit();
    }

    /**
     * Create Image session
     * */
    public void createImageSession(String image ,String filePath){
        // Storing login value as TRUE
        editor_school.putBoolean(IS_LOGIN, true);

        // Storing image value in pref
       editor_school.putString(KEY_IMAGE, image);

       // Storing filePath value in pref
       editor_school.putString(KEY_FILE_PATH,filePath);

        // commit changes
        editor_school.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SchoolLoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref_school.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref_school.getString(KEY_EMAIL, null));

        // user image
        user.put(KEY_IMAGE,pref_school.getString(KEY_IMAGE,null));

        //user image file path
        user.put(KEY_FILE_PATH,pref_school.getString(KEY_FILE_PATH,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor_school.clear();
        editor_school.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref_school.getBoolean(IS_LOGIN, false);
    }
}
