package com.example.qartal.Student;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentInfo {
    public String ID;
    public String FullName;
    public String Phone;
    public String Age ;
    public String BloodType;
    public String Content;
    public String Username;
    public String Password;
    public String Address;
    public String ImageProfileID;
    public String ImageBitmapStringValue;
    public boolean ImageState = false;
    public String HealthState;
    public List<Integer> HeartBeat  = new ArrayList<Integer>();

    public StudentInfo(){

    }

    public StudentInfo(String id ,String fullName ,String username, String password, String phone  , String age ,
                        String bloodType ,String address ){
        this.ID = id;
        this.FullName = fullName;
        this.Username = username;
        this.Password = password;
        this.Phone = phone;
        this.Age = age ;
        this.BloodType = bloodType;
        this.Address = address;
    }



    public String UsernameGenerator(String name){
        String username = name.replace(" ","_").toLowerCase();
        String Signature = "@qartal.com";
        this.Username = username;
        return  username + Signature;
    }

    public String PasswordGenerator(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        this.Password = buffer.toString();
        return buffer.toString();
    }



}
