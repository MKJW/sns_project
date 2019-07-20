package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"masterID", "followeeID"})
public class FolloweeData {
    @NonNull
    @SerializedName("masterID")
    String masterID;
    @NonNull
    @SerializedName("followeeID")
    String followeeID;
    @Expose
    @SerializedName("followeeName")
    String followeeName;
    @Expose
    @SerializedName("followeeImgUrl")
    String followeeImgUrl;


    public FolloweeData(@NonNull String masterID, @NonNull String followeeID, String followeeName, String followeeImgUrl) {
        this.masterID = masterID;
        this.followeeID = followeeID;
        this.followeeName = followeeName;
        this.followeeImgUrl = followeeImgUrl;
    }

    public FolloweeData(){}

    @Override
    public String toString() {
        return "FolloweeData{" +
                "masterID='" + masterID + '\'' +
                ", followeeID='" + followeeID + '\'' +
                ", followeeName='" + followeeName + '\'' +
                ", followeeImgUrl='" + followeeImgUrl + '\'' +
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

    public String getFolloweeName() {
        return followeeName;
    }

    public void setFolloweeName(String followeeName) {
        this.followeeName = followeeName;
    }

    public String getFolloweeImgUrl() {
        return followeeImgUrl;
    }

    public void setFolloweeImgUrl(String followeeImgUrl) {
        this.followeeImgUrl = followeeImgUrl;
    }
}
