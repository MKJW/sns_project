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

    private Date lastRefresh = null;

    public FollowerData(@NonNull String masterID, @NonNull String followerID, String followerName, String followerImgUrl) {
        this.masterID = masterID;
        this.followerID = followerID;
    }

    public FollowerData(){

    }

    @Override
    public String toString() {
        return "FollowerData{" +
                "masterID='" + masterID + '\'' +
                ", followerID='" + followerID + '\'' +
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

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
