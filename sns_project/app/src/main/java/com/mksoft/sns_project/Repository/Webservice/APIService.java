package com.mksoft.sns_project.Repository.Webservice;


import androidx.core.app.NotificationCompat;

import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("/user/{userID}")
    Call<UserData> getUser(
            @Path("userID") String userID);

    @GET("/user/followers/{userID}")
    Call<List<UserData>> getFollowers(
            @Path("userID") String userID
    );

    @FormUrlEncoded
    @POST("/user/add/follower/{userID}")
    Call<Void> addFollower(
            @Field("followId") String followID,
            @Path("userID") String userID
    );

    @GET("/user/followees/{userID}")
    Call<List<UserData>> getFollowees(
            @Path("userID") String userID
    );

    @GET("/posts/{userID}")
    Call<List<FeedData>> getNewsFeed(
            @Path("userID") String userID
    );
    @POST("/posts/save")
    Call<String> postNewsFeed(
            @Body FeedData data
    );

}
