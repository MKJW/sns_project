package com.mksoft.sns_project.Activity.UserFeed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mksoft.sns_project.Activity.AddFeed.AddNewsFeedActivity;
import com.mksoft.sns_project.Activity.FollowListPage.FollowListPageActivity;
import com.mksoft.sns_project.Activity.NewsFeed.NewsFeedActivity;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.FolloweeViewModel;
import com.mksoft.sns_project.ViewModel.FollowerViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
//새로고침에대한 처리 필요
public class UserFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    public static UserFeedActivity userFeedActivity;
    private Integer fromPageState = 0;
    //0은 유저 버튼을 눌러서 오는 경우,
    //1은 피드를 눌러서 자기 자신에게 오는경우
    //2 그외
    private String masterID;
    EtcMethodClass etcMethodClass;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;
    private UserViewModel masterViewModel;
    private FolloweeViewModel followeeViewModel;
    private FollowerViewModel followerViewModel;

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


        masterViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        masterViewModel.init(masterID);
        masterViewModel.getUserLiveData().observe(this, userData -> updateProfileUser(userData));
        //현제 개인 피드 주인의 사진 갱신
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(App.userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateBottomLineUser(userData));
        //바텀라인 이미지 갱신
        followeeViewModel = ViewModelProviders.of(this, viewModelFactory).get(FolloweeViewModel.class);
        followeeViewModel.init(masterID);
        followeeViewModel.getUserLiveData().observe(this, followeeData -> updateFolloweeCnt(followeeData.size()));

        followerViewModel = ViewModelProviders.of(this, viewModelFactory).get(FollowerViewModel.class);
        followerViewModel.init(masterID);
        followerViewModel.getUserLiveData().observe(this, followerData -> updateFollowerCnt(followerData.size()));
        //팔로우 갱신
    }

    private void configureExtras(){
        masterID = getIntent().getExtras().getString("masterID");
        fromPageState = getIntent().getExtras().getInt("fromPageState");
    }



    //xml변수
    //전체 레이아웃
    RelativeLayout user_feed_layout;
    //툴바
    Toolbar user_feed_toolbar;
    TextView user_feed_toolbar_title_textView;
    //프로필레이아웃
    TextView user_feed_post_count_textView;
    TextView user_feed_follower_count_textView;
    TextView user_feed_following_count_textView;
    ImageView user_feed_profile_imageView;
    TextView user_feed_profile_name_textView;
    Button user_feed_state_button;
    LinearLayout user_feed_follower_count_layout;
    LinearLayout user_feed_following_count_layout;
    //바텀레이아웃
    ImageView user_feed_home_button;
    ImageView user_feed_add_feed_button;
    ImageView user_feed_user_feed_button;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_user_feed_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_feed_setting_button :
                Toast.makeText(getApplicationContext(),"setting",Toast.LENGTH_LONG).show();
                return true;
            default :
                if(fromPageState != 4){
                    //userButton으로 온 친구 아닌경우
                    this.finish();//뒤로가기
                }
                return super.onOptionsItemSelected(item) ;
        }
    }//툴바 메뉴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feed_activity);
        this.configureDagger();
        this.configureViewModel();
        userFeedActivity = this;

        init();
        etcMethodClass = new EtcMethodClass(this, user_feed_layout);
        etcMethodClass.bottomLineButton.click_home_button(user_feed_home_button);
        etcMethodClass.bottomLineButton.click_add_feed_button(user_feed_add_feed_button);
        clickFollower();
        clickFollowee();
    }
    private void init(){
        user_feed_layout = findViewById(R.id.user_feed_layout);

        user_feed_toolbar = findViewById(R.id.user_feed_toolbar);
        user_feed_toolbar_title_textView = findViewById(R.id.user_feed_toolbar_title_textView);
        user_feed_toolbar_title_textView.setText(masterID);
        setSupportActionBar(user_feed_toolbar);
        if(fromPageState != 4)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        getSupportActionBar().setTitle(null);

        user_feed_post_count_textView = findViewById(R.id.user_feed_post_count_textView);
        user_feed_follower_count_textView = findViewById(R.id.user_feed_follower_count_textView);
        user_feed_following_count_textView = findViewById(R.id.user_feed_following_count_textView);
        user_feed_profile_imageView = findViewById(R.id.user_feed_profile_imageView);
        user_feed_profile_name_textView = findViewById(R.id.user_feed_profile_name_textView);
        user_feed_state_button = findViewById(R.id.user_feed_state_button);
        Log.d("test0721", masterID + App.userID);
        if(masterID.equals( App.userID)){
            user_feed_state_button.setText("프로필 수정");
        }else{
            apiRepo.checkFollowee(App.userID, masterID, user_feed_state_button, "메세지");

        }
        user_feed_follower_count_layout = findViewById(R.id.user_feed_follower_count_layout);
        user_feed_following_count_layout = findViewById(R.id.user_feed_following_count_layout);



        user_feed_home_button = findViewById(R.id.user_feed_home_button);
        user_feed_add_feed_button = findViewById(R.id.user_feed_add_feed_button);
        user_feed_user_feed_button = findViewById(R.id.user_feed_user_feed_button);

        if(fromPageState == 4){
            //유저 버튼을 통하여 타고 타고 들어노는 경우
            //봇텀라인 유저 사진에 검정 원 그려주자
        }else{
            user_feed_home_button.setImageResource(R.drawable.home_black);
        }

    }
    private void clickFollower(){
        user_feed_follower_count_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(App.context, FollowListPageActivity.class);
                intent.putExtra("masterID", masterID);
                intent.putExtra("fromPageState", App.fromPageState);
                intent.putExtra("currentItemIdx", 0);
                App.context.startActivity(intent);
            }
        });
    }
    private void clickFollowee(){
        user_feed_following_count_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context, FollowListPageActivity.class);
                intent.putExtra("masterID", masterID);
                intent.putExtra("fromPageState", App.fromPageState);
                intent.putExtra("currentItemIdx", 1);
                App.context.startActivity(intent);
            }
        });
    }
    private void updateProfileUser(UserData user){

        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(user_feed_profile_imageView);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(user_feed_profile_imageView);

            }

        }//프로필 사진 초기화
        //초기화 과정이 필요
        user_feed_profile_name_textView.setText(String.valueOf(user.getUsername()));

    }
    private void updateFolloweeCnt(Integer followeeCnt){
        user_feed_following_count_textView.setText(String.valueOf(followeeCnt));
    }
    private void updateFollowerCnt(Integer followerCnt){
        user_feed_follower_count_textView.setText(String.valueOf(followerCnt));
    }
    private void updateBottomLineUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(user_feed_user_feed_button);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(user_feed_user_feed_button);
            }

        }
    }
}
