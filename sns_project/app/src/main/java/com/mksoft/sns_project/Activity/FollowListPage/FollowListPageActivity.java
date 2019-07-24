package com.mksoft.sns_project.Activity.FollowListPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.FollowerData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.FolloweeViewModel;
import com.mksoft.sns_project.ViewModel.FollowerViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class FollowListPageActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    public static FollowListPageActivity followListPageActivity;
    private Integer fromPageState = 0;
    //0은 유저 버튼을 눌러서 오는 경우,
    // 1은 피드를 눌러서 자기 자신에게 오는경우
    //2 그외
    //새로고침 처리 필요
    private String masterID;
    EtcMethodClass etcMethodClass;
    Integer currentItemIdx = 0;//0팔로워, 1팔로이
    Intent intent;
    FollowListPageViewPagerAdapter followListPageViewPagerAdapter;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FolloweeViewModel followeeViewModel;
    private FollowerViewModel followerViewModel;
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
        configureExtras();


        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(App.userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateBottomLineUser(userData));


        followeeViewModel = ViewModelProviders.of(this, viewModelFactory).get(FolloweeViewModel.class);
        followeeViewModel.init(masterID);
        followeeViewModel.getUserLiveData().observe(this, followeeData -> updateFolloweeList(followeeData));

        followerViewModel = ViewModelProviders.of(this, viewModelFactory).get(FollowerViewModel.class);
        followerViewModel.init(masterID);
        followerViewModel.getUserLiveData().observe(this, followerData -> updateFollowerList(followerData));
        //팔로우 갱신
    }

    private void configureExtras(){
        masterID = getIntent().getExtras().getString("masterID");
        fromPageState = getIntent().getExtras().getInt("fromPageState");
        currentItemIdx = getIntent().getExtras().getInt("currentItemIdx");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_follow_list_page_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default :
                this.finish();
                return super.onOptionsItemSelected(item) ;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list_page_activity);
        this.configureDagger();
        this.configureViewModel();
        followListPageActivity = this;
        init();
        etcMethodClass = new EtcMethodClass(this, follow_list_page_background_relativeLayout);
        follow_list_page_viewpager.setCurrentItem(currentItemIdx);//팔로우에서 시작할지 팔로이에서 시작할지
        etcMethodClass.bottomLineButton.click_home_button(follow_list_page_home_button);
        etcMethodClass.bottomLineButton.click_add_feed_button(follow_list_page_add_feed_button);
        etcMethodClass.bottomLineButton.click_user_feed_button(follow_list_page_user_feed_button);
    }
    //탑
    RelativeLayout follow_list_page_background_relativeLayout;
    Toolbar follow_list_page_toolbar;
    TextView follow_list_page_toolbar_title_textView;

    //미드
    TabLayout follow_list_page_tabLayout;
    SwipeRefreshLayout follow_list_page_swipe_layout;
    ViewPager follow_list_page_viewpager;


    //바텀레이아웃
    ImageView follow_list_page_home_button;
    ImageView follow_list_page_search_button;
    ImageView follow_list_page_add_feed_button;
    ImageView follow_list_page_trace_button;
    ImageView follow_list_page_user_feed_button;


    private void init(){
        follow_list_page_background_relativeLayout = findViewById(R.id.follow_list_page_background_relativeLayout);
        follow_list_page_toolbar = findViewById(R.id.follow_list_page_toolbar);
        follow_list_page_toolbar_title_textView = findViewById(R.id.follow_list_page_toolbar_title_textView);
        follow_list_page_toolbar_title_textView.setText(masterID);
        setSupportActionBar(follow_list_page_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        getSupportActionBar().setTitle(null);



        follow_list_page_tabLayout = findViewById(R.id.follow_list_page_tabLayout);
        follow_list_page_swipe_layout = findViewById(R.id.follow_list_page_swipe_layout);
        follow_list_page_viewpager = findViewById(R.id.follow_list_page_viewpager);


        follow_list_page_tabLayout.addTab(follow_list_page_tabLayout.newTab().setText("팔로워"));
        follow_list_page_tabLayout.addTab(follow_list_page_tabLayout.newTab().setText("팔로잉"));
        follow_list_page_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        followListPageViewPagerAdapter =new FollowListPageViewPagerAdapter(this);
        follow_list_page_viewpager.setAdapter(followListPageViewPagerAdapter);
        viewPagerAddOnPageChangeListener();
        viewPagerAddOnTabListener();

        follow_list_page_home_button = findViewById(R.id.follow_list_page_home_button);
        follow_list_page_search_button = findViewById(R.id.follow_list_page_search_button);
        follow_list_page_add_feed_button = findViewById(R.id.follow_list_page_add_feed_button);
        follow_list_page_trace_button = findViewById(R.id.follow_list_page_trace_button);
        follow_list_page_user_feed_button = findViewById(R.id.follow_list_page_user_feed_button);
        if(fromPageState == 4){
            //유저 버튼을 통하여 타고 타고 들어노는 경우
            //봇텀라인 유저 사진에 검정 원 그려주자
        }else{
            follow_list_page_home_button.setImageResource(R.drawable.home_black);
        }

    }

    private void viewPagerAddOnTabListener(){
        follow_list_page_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(follow_list_page_tabLayout));
        //Set TabSelectedListener
        follow_list_page_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                follow_list_page_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void viewPagerAddOnPageChangeListener(){
        follow_list_page_viewpager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float v, int i1 ) {
            }

            @Override
            public void onPageSelected( int position ) {
            }

            @Override
            public void onPageScrollStateChanged( int state ) {
                follow_list_page_swipe_layout.setEnabled( state == ViewPager.SCROLL_STATE_IDLE );
            }
        } );
        //스와이프의 새로고침과 옆으로 스크롤이 겹쳐지는 현상을 막을 수 있다.




    }

    private void updateFolloweeList(List<FolloweeData> followeeDataList){
        List<UserData> userList = new ArrayList<>();
        for(int i =0; i<followeeDataList.size(); i++){
            userList.add(apiRepo.getUserData(followeeDataList.get(i).getFolloweeID()));

        }
        followListPageViewPagerAdapter.updataFolloweeList(userList);

    }
    private void updateFollowerList(List<FollowerData> followerDataList){
        List<UserData> userList = new ArrayList<>();

        for(int i =0; i<followerDataList.size(); i++) {
            userList.add(apiRepo.getUserData(followerDataList.get(i).getFollowerID()));

        }
        followListPageViewPagerAdapter.updateFollowerList(userList);
    }

    private void updateBottomLineUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(follow_list_page_user_feed_button);

            }else{
                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(follow_list_page_user_feed_button);
            }

        }
    }

}
