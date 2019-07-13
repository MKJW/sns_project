package com.mksoft.sns_project.Repository;


import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mksoft.sns_project.Repository.DB.FeedDataDao;
import com.mksoft.sns_project.Repository.DB.UserDataDao;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.ListData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.Repository.Webservice.APIService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class APIRepo {
    private static int FRESH_TIMEOUT_IN_SECONDS = 15;

    private final APIService webservice;
    private final UserDataDao userDao;
    private final Executor executor;
    private final FeedDataDao feedDataDao;

    @Inject
    public APIRepo(APIService webservice,
                   UserDataDao userDao,
                   FeedDataDao feedDataDao,
                   Executor executor) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;
        this.userDao = userDao;
        this.executor = executor;
        this.feedDataDao = feedDataDao;
    }
    public void testLongList(){
        List<Long> temp = new ArrayList<>();
        temp.add((long) 1);
        temp.add((long) 2);
        webservice.postTestLongList(temp).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    public void postNewsFeed(FeedData feedData){
        webservice.postNewsFeed(feedData).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                    Log.d("succesful", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("posterror", t.toString());
            }
        });
    }


    public LiveData<List<FeedData>> getFeedListLiveData(String userLogin){
        refreshFeedList(userLogin);
        return feedDataDao.getFeedDataList();
    }
    private void refreshFeedList(String userLogin){
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean feedListDataExists = (feedDataDao.getFeedData(getMaxRefreshTime(new Date())) != null);
            //최대 1분전에 룸에 유절르 저장했으면 그거 그냥 리턴 그게 아니면 서버에서 받아와서 룸을 초기화 시켜주고

            // If user have to be updated
            if (!feedListDataExists) {//확인
                Log.d("testtarace","request");
                webservice.getNewsFeed(userLogin).enqueue(new Callback<List<FeedData>>() {
                    @Override
                    public void onResponse(Call<List<FeedData>> call, Response<List<FeedData>> response) {
                        Log.e("TAG", "DATA REFRESHED FROM NETWORK");
                        //Toast.makeText(App.context, "Data refreshed from network !", Toast.LENGTH_LONG).show();

                        executor.execute(() -> {
                            List<FeedData> feedDataList = response.body();
                            for(int i =0; i<feedDataList.size(); i++){
                                if(feedDataList.get(i).getFeedImgUrl()==null)
                                    Log.d("test190509", "tete");
                                else{
                                    Log.d("test190509", feedDataList.get(i).getFeedImgUrl());
                                }
                                feedDataList.get(i).setLastRefresh(new Date());

                            }
                            feedDataDao.deleteAll();
                            feedDataDao.saveList(feedDataList);
                        });
                    }

                    @Override
                    public void onFailure(Call<List<FeedData>> call, Throwable t) {
                        Log.d("test190508",t.toString());
                    }
                });
            }else{
                Log.d("testtarace","no request");

            }
        });
    }



    public LiveData<com.mksoft.sns_project.Repository.DataType.UserData> getUserLiveData(String userLogin) {
        refreshUser(userLogin); // try to refresh data if possible from Github Api
        return userDao.getUserLiveData(); // return a LiveData directly from the database.
        //라이브데이터를 갖고오는 과정은 쓰래드가 필요 없으니 쓰래드를 사용하지 않는다.
    }

    private void refreshUser(final String userLogin) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean userExists = (userDao.getUser(getMaxRefreshTime(new Date())) != null);
            //최대 1분전에 룸에 유절르 저장했으면 그거 그냥 리턴 그게 아니면 서버에서 받아와서 룸을 초기화 시켜주고

            // If user have to be updated
            if (!userExists) {
                webservice.getUser(userLogin).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {

                        executor.execute(() -> {
                            UserData user = response.body();
                            user.setFolloweeCnt(user.getFollowee().size());
                            user.setFollowerCnt(user.getFollower().size());

                            Log.d("test0706", user.toString());

                            user.setLastRefresh(new Date());
                            userDao.save(user);
                        });
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Log.d("test0706", t.toString());
                    }
                });

            }

        });
    }//유저 값을 넘겨주는 것이 아니라 데이터베이스에 유저를 저장하고 디비(room)에 저장하기 위하여 쓰래드 사용....

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.SECOND, -FRESH_TIMEOUT_IN_SECONDS);//현제 시간에서 FRESH_TIMEOUT_IN_MINUTES() 전 시간을 불러온다.
        //15초 안에 같은 요청은 내부 디비 보내기
        return cal.getTime();
    }



}
