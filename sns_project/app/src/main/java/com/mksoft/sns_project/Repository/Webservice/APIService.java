package com.mksoft.sns_project.Repository.Webservice;


import androidx.core.app.NotificationCompat;

import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.ListData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("/user/{userID}")
    Call<UserData> getUser(
            @Path("userID") String userID);

    @POST("/user/followers")
    Call<List<UserData>> getFollowers(
            @Body List<Long> followersList
    );
    @POST("/user/followees")
    Call<List<UserData>> getFollowees(
            @Body List<Long> followeesList
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
