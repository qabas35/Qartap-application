package com.example.qartal.School;



public class SchoolInfo {

    public String ID;
    public String NiceName;
    public String Phone;
    public String Email;
    public String Password ;
    public String Address ;
    public boolean Locked ;
    public String ImageProfileID;
    public boolean ImageState = false ;
    public String ImageBitmapStringValue;



    public SchoolInfo(){

    }

    public SchoolInfo(String  id ,String niceName , String email , String password , String phone ,String address , boolean locked){
        this.ID = id;
        this.NiceName = niceName;
        this.Phone = phone;
        this.Email = email;
        this.Password = password ;
        this.Address = address;
        this.Locked = locked;

    }




}
