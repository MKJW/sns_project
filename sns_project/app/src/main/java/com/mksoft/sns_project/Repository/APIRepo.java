package com.mksoft.sns_project.Repository;


import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.mksoft.sns_project.App;
import com.mksoft.sns_project.Repository.DB.FeedDataDao;
import com.mksoft.sns_project.Repository.DB.FolloweeDataDao;
import com.mksoft.sns_project.Repository.DB.FollowerDataDao;
import com.mksoft.sns_project.Repository.DB.UserDataDao;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.FollowerData;
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
    private final FollowerDataDao followerDataDao;
    private final FolloweeDataDao followeeDataDao;

    @Inject
    public APIRepo(APIService webservice,
                   UserDataDao userDao,
                   FeedDataDao feedDataDao,
                   FolloweeDataDao followeeDataDao,
                   FollowerDataDao followerDataDao,
                   Executor executor) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;
        this.userDao = userDao;
        this.executor = executor;
        this.feedDataDao = feedDataDao;
        this.followerDataDao = followerDataDao;
        this.followeeDataDao = followeeDataDao;

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


    public LiveData<List<FolloweeData>> getFolloweeLiveData(String masterID){
        refreshFollowee(masterID);
        return followeeDataDao.getLiveDataFolloweeData(masterID);
    }
    public LiveData<List<FollowerData>> getFollowerLiveData(String masterID){
        refreshFollower(masterID);
        return followerDataDao.getLiveDataFollowerData(masterID);
    }
    public LiveData<com.mksoft.sns_project.Repository.DataType.UserData> getUserLiveData(String userLogin) {
        refreshUser(userLogin); // try to refresh data if possible from Github Api
        return userDao.getUserLiveData(userLogin); // return a LiveData directly from the database.
        //라이브데이터를 갖고오는 과정은 쓰래드가 필요 없으니 쓰래드를 사용하지 않는다.
    }

    private void refreshFollowee(String masterID){

        executor.execute(() -> {
            // Check if user was fetched recently
            boolean followeeExists = (followeeDataDao.getFolloweeDataFromMasterID(masterID, getMaxRefreshTime(new Date())) != null);
            //최대 1분전에 룸에 유절르 저장했으면 그거 그냥 리턴 그게 아니면 서버에서 받아와서 룸을 초기화 시켜주고

            // If user have to be updated
            if (!followeeExists) {
                webservice.getFollowees(masterID).enqueue(new Callback<List<UserData>>() {
                    @Override
                    public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {

                        if(response.isSuccessful()){
                            if(response.body() == null)
                                return;
                            executor.execute(()->{
                                followeeDataDao.deleteAllFromMasterID(masterID);
                                List<UserData> userList = response.body();
                                FolloweeData followeeData = new FolloweeData();
                                followeeData.setMasterID(masterID);
                                for(int i =0; i<userList.size(); i++){
                                    followeeData.setFolloweeID(userList.get(i).getUserId());
                                    followeeDataDao.save(followeeData);
                                    userList.get(i).setLastRefresh(new Date());
                                    userDao.save(userList.get(i));
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserData>> call, Throwable t) {
                        Log.d("refreshFolloweeErr", t.toString());
                    }
                });
            }

        });



    }
    private void refreshFollower(String masterID){
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean followerExists = (followerDataDao.getFollowerDataFromMasterID(masterID, getMaxRefreshTime(new Date())) != null);
            //최대 1분전에 룸에 유절르 저장했으면 그거 그냥 리턴 그게 아니면 서버에서 받아와서 룸을 초기화 시켜주고

            // If user have to be updated
            if (!followerExists) {
                webservice.getFollowers(masterID).enqueue(new Callback<List<UserData>>() {
                    @Override
                    public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {

                        if(response.isSuccessful()){
                            if(response.body() == null)
                                return;
                            executor.execute(()->{
                                followerDataDao.deleteAllFromMasterID(masterID);
                                List<UserData> userList = response.body();

                                FollowerData followerData = new FollowerData();

                                followerData.setMasterID(masterID);
                                for(int i =0; i<userList.size(); i++){
                                    followerData.setFollowerID(userList.get(i).getUserId());

                                    followerDataDao.save(followerData);
                                    userList.get(i).setLastRefresh(new Date());
                                    userDao.save(userList.get(i));
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(Call<List<UserData>> call, Throwable t) {
                        Log.d("refreshFollowerErr", t.toString());
                    }
                });
            }

        });

    }//팔로우나 팔로이 리스트에서 상대방의 간략한 정보를 볼 수 있는 수준의 정보만 저장하여 내부에 저장하자
    //클릭하여 상대방 정보를 들어간다면 그때 상대 유저에 대한 정보를 갱신해 주자.

    public void checkFollowee(String masterID, String followeeID, Button refreshState){
        executor.execute(()->{
            boolean followeeExists = (followeeDataDao.getCheckFollowee(masterID, followeeID)!=null);
            if(followeeExists){
                //팔로위 관계이면
                refreshState.setText("메세지");
            }else{
                refreshState.setText("팔로우");
                refreshState.setTextColor(Color.WHITE);
                refreshState.setBackgroundColor(Color.BLUE);
            }
        });
    }
    private void refreshUser(final String userLogin) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean userExists = (userDao.getUser(userLogin, getMaxRefreshTime(new Date())) != null);
            //최대 1분전에 룸에 유절르 저장했으면 그거 그냥 리턴 그게 아니면 서버에서 받아와서 룸을 초기화 시켜주고

            // If user have to be updated
            if (!userExists) {
                webservice.getUser(userLogin).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {

                        executor.execute(() -> {
                            UserData user = response.body();
                            Log.d("test0706", user.toString());
                            user.setLastRefresh(new Date());
                            //유저 정보에 의존하기때문에 시간 갱신은 필요없어 본인다.
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
