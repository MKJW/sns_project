package com.mksoft.sns_project.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.NewsFeedViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;
import com.mksoft.sns_project.etcMethod.BackPressCloseHandler;
import com.mksoft.sns_project.etcMethod.HideKeyboard;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class NewsFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    String userID;
    HideKeyboard hideKeyboard;
    BackPressCloseHandler backPressCloseHandler;
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public static NewsFeedActivity newsFeedActivity;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;
    private NewsFeedViewModel newsFeedViewModel;
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
        receiveID();
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateUser(userData));

        newsFeedViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsFeedViewModel.class);
        newsFeedViewModel.init(userID);
        newsFeedViewModel.getNewsFeedLiveData().observe(this, feedData -> updateNewsFeed(feedData));

    }

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NewsFeedAdapter feedAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton fab;
    ImageView userImag;
    TextView userName;

    String userNameS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        this.configureDagger();
        this.configureViewModel();
        newsFeedActivity = this;
        init();
    }
    void receiveID(){
        userID = "mkjw";//로그인 페이지에서 받아오기
    }
    private void init(){
        hideKeyboard = new HideKeyboard(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiRepo.getFeedListLiveData(userID);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        fab = findViewById(R.id.fab_main);
        userImag = (ImageView)findViewById(R.id.userIMG);
        userName = (TextView)findViewById(R.id.userInfoTextView);
        recyclerView = (RecyclerView)findViewById(R.id.feedRecyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        initListView();
    }
    private void initListView(){
        recyclerView.setHasFixedSize(true);
        feedAdapter = new NewsFeedAdapter(getApplicationContext());
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void updateUser(UserData user){
        if (user != null){

            if(user.getUserImgUrl() == null||user.getUserImgUrl().length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(userImag);
            }else{

                Glide.with(this).load(user.getUserImgUrl()).apply(RequestOptions.circleCropTransform()).into(userImag);
            }
            this.userName.setText(user.getName());
            userNameS = user.getName();

        }
    }
    private void updateNewsFeed(List<FeedData> feedData){
        feedAdapter.updateNewsFeed(feedData);
    }


    //뒤로가기 버튼
    public interface onKeyBackPressedListener{
        void onBackKey();
    }
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }
    @Override
    public void onBackPressed() {
        if(mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        }else{
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                backPressCloseHandler.onBackPressed();
            }
            else{
                super.onBackPressed();
            }
        }
    }


}
