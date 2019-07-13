package com.mksoft.sns_project.Repository.DB;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mksoft.sns_project.Repository.DataType.UserFollowerData;
import com.mksoft.sns_project.Repository.DataType.UserFollowingData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserFollowingDataDao {
    @Insert(onConflict = REPLACE)
    void save(List<UserFollowingData> following);

    @Query("SELECT * FROM userfollowingdata")
    LiveData<UserFollowingData> getUserFollowingLiveData();

    @Query("SELECT * FROM userfollowingdata WHERE id == :id")
    UserFollowingData isFollowing(Long id);
    @Query("DELETE FROM userfollowingdata")
    void deleteAll();
}
