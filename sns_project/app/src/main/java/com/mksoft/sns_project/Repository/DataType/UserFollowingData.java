package com.mksoft.sns_project.Repository.DataType;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class UserFollowingData {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    Long id;

    public UserFollowingData(@NonNull Long id) {
        this.id = id;
    }
    public UserFollowingData() {

    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }
}
