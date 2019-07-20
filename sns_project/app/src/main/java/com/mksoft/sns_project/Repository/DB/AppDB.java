package com.mksoft.sns_project.Repository.DB;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.FollowerData;
import com.mksoft.sns_project.Repository.DataType.UserData;


@Database(entities = {UserData.class, FeedData.class, FolloweeData.class, FollowerData.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDB extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile AppDB INSTANCE;

    // --- DAO ---
    public abstract UserDataDao userDao();
    public abstract FeedDataDao feedDataDao();
    public abstract FolloweeDataDao followeeDataDao();
    public abstract FollowerDataDao followerDataDao();

}
