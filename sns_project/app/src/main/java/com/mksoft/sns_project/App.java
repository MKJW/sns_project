package com.mksoft.sns_project;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.mksoft.sns_project.DI.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class App extends Application implements HasActivityInjector {
    public static Context context;
    public static String BASE_URL = "http://ec2-52-78-8-248.ap-northeast-2.compute.amazonaws.com:8888";//서버 변수
    public static String userID = "mkjw";//로그인할 경우 루트권한을 갖는 사용자 아이디로 초기화
    public static Integer fromPageState = 0;
    //0 : home 버튼
    //1 : search
    //2 : add
    //3 : trace
    //4 : me
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();

    }



    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }



    private void initDagger(){
        DaggerAppComponent.builder().application(this).build().inject(this);
    }


}
