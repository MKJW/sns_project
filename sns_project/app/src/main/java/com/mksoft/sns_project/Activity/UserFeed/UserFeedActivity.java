package com.mksoft.sns_project.Activity.UserFeed;

import android.content.Intent;
import android.os.Bundle;
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

public class UserFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    public static UserFeedActivity userFeedActivity;
    Intent intent;
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

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(App.userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateUser(userData));
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
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
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

        click_user_feed_home_button();
        click_user_feed_add_feed_button();
    }
    private void init(){
        user_feed_layout = findViewById(R.id.user_feed_layout);

        user_feed_toolbar = findViewById(R.id.user_feed_toolbar);
        user_feed_toolbar_title_textView = findViewById(R.id.user_feed_toolbar_title_textView);
        user_feed_toolbar_title_textView.setText(App.userID);
        setSupportActionBar(user_feed_toolbar);
        getSupportActionBar().setTitle(null);

        user_feed_post_count_textView = findViewById(R.id.user_feed_post_count_textView);
        user_feed_follower_count_textView = findViewById(R.id.user_feed_follower_count_textView);
        user_feed_following_count_textView = findViewById(R.id.user_feed_following_count_textView);
        user_feed_profile_imageView = findViewById(R.id.user_feed_profile_imageView);
        user_feed_profile_name_textView = findViewById(R.id.user_feed_profile_name_textView);

        user_feed_home_button = findViewById(R.id.user_feed_home_button);
        user_feed_add_feed_button = findViewById(R.id.user_feed_add_feed_button);
        user_feed_user_feed_button = findViewById(R.id.user_feed_user_feed_button);


    }
    private void click_user_feed_home_button(){
        user_feed_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
                startActivity(intent);
            }
        });
    }
    private void click_user_feed_add_feed_button(){
        user_feed_add_feed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AddNewsFeedActivity.class);
                startActivity(intent);

            }
        });
    }
    private void updateUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(user_feed_profile_imageView);
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(user_feed_user_feed_button);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(user_feed_profile_imageView);
                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(user_feed_user_feed_button);
            }

        }//프로필 사진 초기화
        //초기화 과정이 필요
        user_feed_profile_name_textView.setText(String.valueOf(user.getUsername()));
        user_feed_follower_count_textView.setText(String.valueOf(user.getFollowerCnt()));
        user_feed_following_count_textView.setText(String.valueOf(user.getFolloweeCnt()));
    }
}
