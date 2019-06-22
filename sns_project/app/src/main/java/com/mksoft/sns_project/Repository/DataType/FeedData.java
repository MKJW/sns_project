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
    @SerializedName("postId")
    @Expose
    private Integer postId = 0;


    @SerializedName("writer")
    @Expose
    private String writer="";


    @SerializedName("feed_img_url")
    @Expose
    private String feedImgUrl="";


    @SerializedName("user_img_url")
    @Expose
    private String userImgUrl="";

    @SerializedName("content")
    @Expose
    private String content="";

    @SerializedName("numOfLikes ")
    @Expose
    private Integer numOfLikes = 0;


    private Date lastRefresh = null;

    public FeedData(@NonNull Integer postId, String writer, String feedImgUrl, String userImgUrl, String content, Integer numOfLikes, Date lastRefresh) {
        this.postId = postId;
        this.writer = writer;
        this.feedImgUrl = feedImgUrl;
        this.userImgUrl = userImgUrl;
        this.content = content;
        this.numOfLikes = numOfLikes;
        this.lastRefresh = lastRefresh;
    }

    public FeedData() {
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }
    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }


    @NonNull
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(@NonNull Integer postId) {
        this.postId = postId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(Integer numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public String getFeedImgUrl() {
        return feedImgUrl;
    }
    public void setFeedImgUrl(String feedImgUrl) {
        this.feedImgUrl = feedImgUrl;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }
    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }
}
