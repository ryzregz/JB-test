package com.morris.myapplication.model;

import com.google.gson.annotations.SerializedName;
import com.morris.myapplication.network.BaseResponse;

public class User  extends BaseResponse {
    @SerializedName("fullname")
    String fullname;
    @SerializedName("email")
    String email;
    @SerializedName("gender")
    String gender;
    @SerializedName("education")
    String education;
    @SerializedName("postaladdress")
    String postaladdress;
    @SerializedName("town")
    String town;
    @SerializedName("yob")
    String yob;
    @SerializedName("isdriver")
    boolean isdriver;

    public User() {
    }

    public User(String fullname, String email, String gender, String education,
                String postaladdress, String town, String yob, boolean isdriver) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.education = education;
        this.postaladdress = postaladdress;
        this.town = town;
        this.yob = yob;
        this.isdriver = isdriver;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }

    public boolean isIsdriver() {
        return isdriver;
    }

    public void setIsdriver(boolean isdriver) {
        this.isdriver = isdriver;
    }
}
