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
public interface UserFollowerDataDao {
    @Insert(onConflict = REPLACE)
    void save(List<UserFollowerData> follower);

    @Query("SELECT * FROM userfollowerdata")
    LiveData<UserFollowerData> getUserFollowerLiveData();

    @Query("SELECT * FROM userfollowerdata WHERE id == :id")
    UserFollowerData isFollower(Long id);
    @Query("DELETE FROM userfollowerdata")
    void deleteAll();


}
