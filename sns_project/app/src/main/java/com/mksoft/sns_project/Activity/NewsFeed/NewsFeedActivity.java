package com.mksoft.sns_project.Activity.NewsFeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mksoft.sns_project.Activity.AddFeed.AddNewsFeedActivity;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.NewsFeedViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class NewsFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    Intent intent;
    String userID;
    EtcMethodClass etcMethodClass;
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
    ImageView userImag;
    RelativeLayout newsfeedLayout;
    ImageView feedAddButton;
    Toolbar toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_DM :
                Toast.makeText(getApplicationContext(),"DM",Toast.LENGTH_LONG).show();
                return true;
            default :
                Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item) ;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        this.configureDagger();
        this.configureViewModel();
        newsFeedActivity = this;
        init();
        etcMethodClass = new EtcMethodClass(this, newsfeedLayout);
        etcMethodClass.clickHideKeyboard();
        clickFeedAddButton();
        clickUserIMGButton();
    }
    void receiveID(){
        userID = "mkjw";//로그인 페이지에서 받아오기
    }
    private void init(){



        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiRepo.getFeedListLiveData(userID);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//홈버튼 활성화
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.camera);
        feedAddButton = findViewById(R.id.feedAddButton);
        newsfeedLayout = findViewById(R.id.newsfeedLayout);
        userImag = (ImageView)findViewById(R.id.userIMG);
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
    private void clickFeedAddButton(){
        feedAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AddNewsFeedActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }//추가페이지로 넘어가는 버튼
    private void clickUserIMGButton(){
        userImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
            }
        });
    }//유저정보로 넘어가는 버튼
    private void updateUser(UserData user){
        if (user != null){

            if(user.getUserImgUrl() == null||user.getUserImgUrl().length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(userImag);
            }else{

                Glide.with(this).load(user.getUserImgUrl()).apply(RequestOptions.circleCropTransform()).into(userImag);
            }

        }
    }
    private void updateNewsFeed(List<FeedData> feedData){
        feedAdapter.updateNewsFeed(feedData);
    }










}
