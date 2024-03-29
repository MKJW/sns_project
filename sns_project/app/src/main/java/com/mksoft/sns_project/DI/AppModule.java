package com.mksoft.sns_project.DI;


import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DB.AppDB;
import com.mksoft.sns_project.Repository.DB.FeedDataDao;
import com.mksoft.sns_project.Repository.DB.FolloweeDataDao;
import com.mksoft.sns_project.Repository.DB.FollowerDataDao;
import com.mksoft.sns_project.Repository.DB.UserDataDao;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.Webservice.APIService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {


    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    AppDB provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDB.class, "AppDBOBJ.db")
                .build();
    }

    @Provides
    @Singleton
    UserDataDao provideUserDao(AppDB database) { return database.userDao(); }

    @Provides
    @Singleton
    FeedDataDao provideFeedDao(AppDB database) { return database.feedDataDao(); }

    @Provides
    @Singleton
    FolloweeDataDao provideFolloweeDao(AppDB database) { return database.followeeDataDao(); }

    @Provides
    @Singleton
    FollowerDataDao provideFollowerDao(AppDB database) { return database.followerDataDao(); }




// --- NETWORK INJECTION ---


    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(App.BASE_URL)
                .build();

        return retrofit;
    }

    @Provides
    @Singleton
    APIService provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(APIService.class);
    }


    // --- REPOSITORY INJECTION ---
    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }


    @Provides
    @Singleton
    APIRepo provideAPIRepository(APIService webservice, UserDataDao userDataDao,
                                 FeedDataDao feedDataDao, FolloweeDataDao followeeDataDao, FollowerDataDao followerDataDao, Executor executor) {
        return new APIRepo(webservice, userDataDao, feedDataDao, followeeDataDao, followerDataDao, executor);
    }


}
