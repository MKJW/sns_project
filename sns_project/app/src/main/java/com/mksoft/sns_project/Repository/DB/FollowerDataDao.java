package com.mksoft.sns_project.Repository.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.FollowerData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FollowerDataDao {
    @Insert(onConflict = REPLACE)
    void save(FollowerData followerData);

    @Query("SELECT COUNT(*) FROM FollowerData WHERE masterID = :masterID")
    Integer getFollowerCnt(String masterID);

    @Query("SELECT * FROM FollowerData WHERE masterID = :masterID AND lastRefresh > :lastRefreshMax LIMIT 1")
    FollowerData getFollowerDataFromMasterID(String masterID, Date lastRefreshMax);

    @Query("SELECT * FROM FollowerData WHERE masterID = :masterID")
    LiveData<List<FollowerData>> getLiveDataFollowerData(String masterID);

    @Query("DELETE FROM FollowerData WHERE masterID = :masterID")
    void deleteAllFromMasterID(String masterID);
}
