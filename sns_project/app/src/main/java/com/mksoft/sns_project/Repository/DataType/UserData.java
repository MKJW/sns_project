package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class UserData {


    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id ="";


    @SerializedName("name")
    @Expose
    private String name="";


    @SerializedName("user_img_url")
    @Expose
    private String userImgUrl="";

    private Date lastRefresh = null;

    public UserData(@NonNull String id, String name, String userImgUrl, Date lastRefresh) {
        this.id = id;
        this.name = name;
        this.userImgUrl = userImgUrl;
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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getUserImgUrl() {
        return userImgUrl;
    }
    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }
}
