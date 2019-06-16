package com.mksoft.sns_project.Repository.DataType;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class FeedData {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id ="";


    @SerializedName("name")
    @Expose
    private String name="";


    @SerializedName("feed_img_url")
    @Expose
    private String feedImgUrl="";


    @SerializedName("user_img_url")
    @Expose
    private String userImgUrl="";

    @SerializedName("feed_contents")
    @Expose
    private String feedContents="";

    @SerializedName("feed_like")
    @Expose
    private String feedLike="";


    private Date lastRefresh = null;

    public FeedData(@NonNull String id, String name, String feedImgUrl, String feedContents
            , String feedLike, Date lastRefresh) {
        this.id = id;
        this.name = name;
        this.feedImgUrl = feedImgUrl;
        this.feedContents = feedContents;
        this.lastRefresh = lastRefresh;
        this.feedLike = feedLike;
    }

    public FeedData() {
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


    public String getFeedImgUrl() {
        return feedImgUrl;
    }

    public void setFeedImgUrl(String feedImgUrl) {
        this.feedImgUrl = feedImgUrl;
    }

    public String getFeedContents() {
        return feedContents;
    }
    public void setFeedContents(String feedContents) {
        this.feedContents = feedContents;
    }

    public String getFeedLike() {
        return feedLike;
    }

    public void setFeedLike(String feedLike) {
        this.feedLike = feedLike;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }
}
