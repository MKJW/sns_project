package com.mksoft.sns_project.DI;


import com.mksoft.sns_project.Activity.AddFeed.AddNewsFeedActivity;
import com.mksoft.sns_project.Activity.FollowListPage.FollowListPageActivity;
import com.mksoft.sns_project.Activity.NewsFeed.NewsFeedActivity;
import com.mksoft.sns_project.Activity.UserFeed.UserFeedActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by Philippe on 02/03/2018.
 */


@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector//그 액티비티의 연결된 플레그먼트의 모듈 자신보다 아래에 있는 친구
    abstract NewsFeedActivity contributeNewsFeedActivity();//여러개의 액티비티중에 그중에 MainAcrivity를 주입

    @ContributesAndroidInjector
    abstract AddNewsFeedActivity cotributeAddNewsFeedActivity();

    @ContributesAndroidInjector
    abstract UserFeedActivity contributeUserFeedActivity();

    @ContributesAndroidInjector
    abstract FollowListPageActivity contributeFollowListPageActivity();

}
