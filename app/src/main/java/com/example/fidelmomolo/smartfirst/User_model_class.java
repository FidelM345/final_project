package com.example.fidelmomolo.smartfirst;

/**
 * Created by Fidel M Omolo on 5/9/2018.
 */

public class User_model_class {
    String fname,lname,imageuri;

    public User_model_class() {
    }


    public User_model_class(String fname, String lname, String imageuri) {
        this.fname = fname;
        this.lname = lname;
        this.imageuri = imageuri;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
