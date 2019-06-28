package com.mksoft.sns_project.Activity.UserFeed;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.mksoft.sns_project.Activity.NewsFeed.NewsFeedActivity;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.ViewModel.NewsFeedViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class UserFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    public static UserFeedActivity userFeedActivity;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;
    @Inject
    APIRepo apiRepo;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    private void configureViewModel(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        this.configureDagger();
        this.configureViewModel();
        userFeedActivity = this;

    }

}
