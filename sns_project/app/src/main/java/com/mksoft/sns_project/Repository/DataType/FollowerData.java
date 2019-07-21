package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = {"masterID", "followerID"})
public class FollowerData {

    @NonNull
    @SerializedName("masterID")
    String masterID;
    @NonNull
    @SerializedName("followerID")
    String followerID;
    @SerializedName("followerName")
    @Expose
    String followerName;
    @SerializedName("followerImgUrl")
    @Expose
    String followerImgUrl;

    private Date lastRefresh = null;

    public FollowerData(@NonNull String masterID, @NonNull String followerID, String followerName, String followerImgUrl) {
        this.masterID = masterID;
        this.followerID = followerID;
        this.followerName = followerName;
        this.followerImgUrl = followerImgUrl;
    }

    public FollowerData(){

    }

    @Override
    public String toString() {
        return "FollowerData{" +
                "masterID='" + masterID + '\'' +
                ", followerID='" + followerID + '\'' +
                ", followerName='" + followerName + '\'' +
                ", followerImgUrl='" + followerImgUrl + '\'' +
                '}';
    }

    @NonNull
    public String getMasterID() {
        return masterID;
    }

    public void setMasterID(@NonNull String masterID) {
        this.masterID = masterID;
    }

    @NonNull
    public String getFollowerID() {
        return followerID;
    }

    public void setFollowerID(@NonNull String followerID) {
        this.followerID = followerID;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerImgUrl() {
        return followerImgUrl;
    }

    public void setFollowerImgUrl(String followerImgUrl) {
        this.followerImgUrl = followerImgUrl;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
