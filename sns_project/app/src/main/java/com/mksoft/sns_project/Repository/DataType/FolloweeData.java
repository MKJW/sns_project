package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = {"masterID", "followeeID"})
public class FolloweeData {
    @NonNull
    @SerializedName("masterID")
    String masterID;
    @NonNull
    @SerializedName("followeeID")
    String followeeID;

    private Date lastRefresh = null;

    public FolloweeData(@NonNull String masterID, @NonNull String followeeID) {
        this.masterID = masterID;
        this.followeeID = followeeID;
    }

    public FolloweeData(){}

    @Override
    public String toString() {
        return "FolloweeData{" +
                "masterID='" + masterID + '\'' +
                ", followeeID='" + followeeID + '\'' +
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
    public String getFolloweeID() {
        return followeeID;
    }

    public void setFolloweeID(@NonNull String followeeID) {
        this.followeeID = followeeID;
    }


    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
