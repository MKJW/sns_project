package com.mksoft.sns_project.Repository.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FolloweeDataDao {
    @Insert(onConflict = REPLACE)
    void save(FolloweeData followeeData);

    @Query("SELECT * FROM FolloweeData WHERE masterID = :masterID")
    List<FolloweeData> getFolloweeDataFromMasterID(String masterID);

    @Query("DELETE FROM FolloweeData WHERE masterID = :masterID")
    void deleteAllFromMasterID(String masterID);
}
