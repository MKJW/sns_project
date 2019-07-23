package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity
public class UserData {


    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private Integer id = 0;

    @PrimaryKey
    @SerializedName("userId")
    @NonNull
    private String userId="";

    @SerializedName("username")
    @NonNull
    private String username = "";





    @SerializedName("email")
    @Expose
    private String email = "";


    @SerializedName("userImageUrl")
    @Expose
    private String userImageUrl="";


    @SerializedName("website")
    @Expose
    private String website = "";

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber = "";

    @SerializedName("gender")
    @Expose
    private String gender = "";



    private Date lastRefresh = null;

    public UserData(@NonNull Integer id, @NonNull String userId, @NonNull String username, String email, String userImageUrl, String website, String phoneNumber, String gender, Date lastRefresh) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userImageUrl = userImageUrl;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.lastRefresh = lastRefresh;
    }

    public UserData() {
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }
    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", website='" + website + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", lastRefresh=" + lastRefresh +
                '}';
    }



}
