package com.mksoft.sns_project.Activity.UserFeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mksoft.sns_project.Activity.AddFeed.AddNewsFeedActivity;
import com.mksoft.sns_project.Activity.NewsFeed.NewsFeedActivity;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.UserViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class OtherUserFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    public static OtherUserFeedActivity otherUserFeedActivity;
    private String otherUserID;
    Intent intent;
    Integer pageState = 0;//0은 로그인한 사용자 1은 외부 사람, 2는 팔로잉한 사람...

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;
    private UserViewModel otherUserViewModel;

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

        otherUserID = getIntent().getExtras().getString("otherUserID");

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(App.userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateUser(userData));

        otherUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        otherUserViewModel.init(otherUserID);
        otherUserViewModel.getUserLiveData().observe(this, userData -> updateOtherUser(userData));

    }


    //xml변수
    //전체 레이아웃
    RelativeLayout ohter_user_feed_layout;
    //툴바
    Toolbar ohter_user_feed_toolbar;
    TextView ohter_user_feed_toolbar_title_textView;
    //프로필레이아웃
    TextView ohter_user_feed_post_count_textView;
    TextView ohter_user_feed_follower_count_textView;
    TextView ohter_user_feed_following_count_textView;
    ImageView ohter_user_feed_profile_imageView;
    TextView ohter_user_feed_profile_name_textView;
    Button other_user_feed_profile_button;
    //바텀레이아웃
    ImageView ohter_user_feed_home_button;
    ImageView ohter_user_feed_add_feed_button;
    ImageView ohter_user_feed_user_feed_button;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_other_user_feed_action, menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.other_user_feed_setting_button :
                Toast.makeText(getApplicationContext(),"setting",Toast.LENGTH_LONG).show();
                return true;
            default :
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item) ;
        }
    }//툴바 메뉴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feed_activity);

        this.configureDagger();
        configureViewModel();

        otherUserFeedActivity = this;
        init();

        click_other_user_feed_add_feed_button();
    }
    private void init(){
        ohter_user_feed_layout = findViewById(R.id.other_user_feed_layout);

        ohter_user_feed_toolbar = findViewById(R.id.other_user_feed_toolbar);
        ohter_user_feed_toolbar_title_textView = findViewById(R.id.other_user_feed_toolbar_title_textView);
        ohter_user_feed_toolbar_title_textView.setText(App.userID);
        setSupportActionBar(ohter_user_feed_toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//홈버튼 활성화

        ohter_user_feed_post_count_textView = findViewById(R.id.other_user_feed_post_count_textView);
        ohter_user_feed_follower_count_textView = findViewById(R.id.other_user_feed_follower_count_textView);
        ohter_user_feed_following_count_textView = findViewById(R.id.other_user_feed_following_count_textView);
        ohter_user_feed_profile_imageView = findViewById(R.id.other_user_feed_profile_imageView);
        ohter_user_feed_profile_name_textView = findViewById(R.id.other_user_feed_profile_name_textView);
        other_user_feed_profile_button = findViewById(R.id.other_user_feed_profile_button);
        if(App.userID == otherUserID){
            other_user_feed_profile_button.setText("프로필 수정");
            pageState = 0;
        }else{
            other_user_feed_profile_button.setText("팔로우");
            pageState = 1;
        }//팔로우 상태인지 아닌지에 따라서 수정
        //이때 서버에 친구인지 요청하는 방법 OR 내부에 저장된 데이터로 매칭하는 방법
        ohter_user_feed_home_button = findViewById(R.id.other_user_feed_home_button);
        ohter_user_feed_add_feed_button = findViewById(R.id.other_user_feed_add_feed_button);
        ohter_user_feed_user_feed_button = findViewById(R.id.other_user_feed_user_feed_button);


    }

    private void click_other_user_feed_add_feed_button(){
        ohter_user_feed_add_feed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AddNewsFeedActivity.class);
                startActivity(intent);

            }
        });
    }
    private void updateOtherUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(ohter_user_feed_profile_imageView);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(ohter_user_feed_profile_imageView);
            }

        }//프로필 사진 초기화
        //초기화 과정이 필요
        ohter_user_feed_profile_name_textView.setText(String.valueOf(user.getUsername()));
        ohter_user_feed_follower_count_textView.setText(user.getFollowerCnt());
        ohter_user_feed_following_count_textView.setText(user.getFolloweeCnt());
    }
    private void updateUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(ohter_user_feed_user_feed_button);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(ohter_user_feed_user_feed_button);
            }

        }

    }//현제 사용자 밑에 라인 사진 업데이트...
}
