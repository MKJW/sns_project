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
public interface FolloweeDataDao {
    @Insert(onConflict = REPLACE)
    void save(FolloweeData followeeData);

    @Query("SELECT * FROM FolloweeData WHERE masterID = :masterID AND lastRefresh > :lastRefreshMax LIMIT 1")
    FolloweeData getFolloweeDataFromMasterID(String masterID, Date lastRefreshMax);

    @Query("SELECT COUNT(*) FROM FolloweeData WHERE masterID = :masterID")
    Integer getFolloweeCnt(String masterID);

    @Query("SELECT * FROM FolloweeData WHERE masterID = :masterID AND followeeID = :followeeID")
    FolloweeData getCheckFollowee(String masterID, String followeeID);
    @Query("SELECT * FROM FolloweeData WHERE masterID = :masterID")
    LiveData<List<FolloweeData>> getLiveDataFolloweeData(String masterID);

    @Query("DELETE FROM FolloweeData WHERE masterID = :masterID")
    void deleteAllFromMasterID(String masterID);
}
