package com.mksoft.sns_project.Repository.DB;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.Date;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDataDao {

    @Insert(onConflict = REPLACE)
    void save(UserData user);

    @Query("SELECT * FROM userdata WHERE userId = :userID")
    LiveData<UserData> getUserLiveData(String userID);//라이브 데이터 형식으로 불러오기

    @Query("SELECT * FROM userdata WHERE userId = :userID")
    UserData getUserDataFromUserID(String userID);

    @Query("SELECT * FROM userdata WHERE userId = :userID AND lastRefresh > :lastRefreshMax LIMIT 1")
    UserData getUser(String userID, Date lastRefreshMax);//일반적인 userdata로 불러오기
    //초기화 기준 시간을 확인하여 불러오기



}
