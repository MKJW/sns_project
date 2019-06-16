package com.mksoft.sns_project.Repository.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.mksoft.sns_project.Repository.DataType.FeedData;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FeedDataDao {
    @Insert(onConflict = REPLACE)
    void save(FeedData feedData);

    @Insert(onConflict = REPLACE)
    void saveList(List<FeedData> feedData);

    @Query("SELECT * FROM feeddata")
    LiveData<List<FeedData>> getFeedDataList();//라이브 데이터 형식으로 불러오기

    @Query("SELECT * FROM feeddata WHERE lastRefresh > :lastRefreshMax LIMIT 1")
    FeedData getFeedData(Date lastRefreshMax);//일반적인  List<FriendData>불러오기
    //초기화 기준 시간을 확인하여 불러오기 1개
    @Query("DELETE FROM feeddata")
    void deleteAll();
}
