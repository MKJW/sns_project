package com.mksoft.sns_project.Repository.Webservice;


import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @GET("/posts/{userID}")
    Call<UserData> getUser(
            @Path("userID") String userID);

    @GET("/posts/{userID}")
    Call<List<FeedData>> getNewsFeed(
            @Path("userID") String userID
    );


}
