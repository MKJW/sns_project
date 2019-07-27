package com.mksoft.sns_project.Activity.NewsFeed;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
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
import com.mksoft.sns_project.Activity.UserFeed.UserFeedActivity;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;
import com.mksoft.sns_project.ViewModel.NewsFeedViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class NewsFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    //내부에 필요한 변수
    Intent intent;
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
    }//주입
    private void configureViewModel(){
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.init(App.userID);
        userViewModel.getUserLiveData().observe(this, userData -> updateUser(userData));

        newsFeedViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsFeedViewModel.class);
        newsFeedViewModel.init(App.userID);
        newsFeedViewModel.getNewsFeedLiveData().observe(this, feedData -> updateNewsFeed(feedData));

    }//ViewModel초기화


    //xml변수
    //전체 레이아웃
    RelativeLayout news_feed_layout;
    //툴바 레이아웃
    Toolbar news_feed_toolbar;
    //피드 레이아웃
    SwipeRefreshLayout news_feed_swipe_layout;
    RecyclerView news_feed_feed_RecyclerView;
    RecyclerView.LayoutManager layoutManager;
    NewsFeedAdapter feedAdapter;
    //바텀 레이아웃
    ImageView news_feed_add_feed_button;
    ImageView news_feed_user_feed_button;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_news_feed_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news_feed_DM_button :
                // FIXME remove this test code
                uploadImageToServer();
                Toast.makeText(getApplicationContext(),"DM",Toast.LENGTH_LONG).show();
                return true;
            default :
                Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item) ;
        }
    }//툴바 메뉴


    private void uploadImageToServer() {
        // FIXME you need to create File object

        File file = null;
//        File file = new File(getCacheDir() + "/please.jpg");
//        try {
//            InputStream is = getAssets().open("please.jpg");
//            FileOutputStream fos = new FileOutputStream(file);
//
//            byte[] buffer = new byte[1024];
//            int length = 0;
//            while((length = is.read(buffer)) > 0) {
//                fos.write(buffer, 0, length);
//            }
//
//            fos.close();
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // FIXME change to get file Type from file object
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        // FIXME
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        apiRepo.saveImage(body);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.fromPageState = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        this.configureDagger();
        this.configureViewModel();
        //apiRepo.testLongList();//테스트트
        newsFeedActivity = this;
        init();
        etcMethodClass = new EtcMethodClass(this, news_feed_layout);
        etcMethodClass.hideKeyboard.clickHideKeyboard();
        etcMethodClass.bottomLineButton.click_add_feed_button(news_feed_add_feed_button);
        etcMethodClass.bottomLineButton.click_user_feed_button(news_feed_user_feed_button);
    }

    private void init(){



        news_feed_swipe_layout = findViewById(R.id.news_feed_swipe_layout);
        news_feed_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiRepo.getFeedListLiveData(App.userID);
                news_feed_swipe_layout.setRefreshing(false);
            }
        });

        news_feed_toolbar = findViewById(R.id.news_feed_toolbar);
        setSupportActionBar(news_feed_toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//홈버튼 활성화
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.camera);
        news_feed_add_feed_button = findViewById(R.id.news_feed_add_feed_button);
        news_feed_layout = findViewById(R.id.news_feed_layout);
        news_feed_user_feed_button = (ImageView)findViewById(R.id.news_feed_user_feed_button);
        news_feed_feed_RecyclerView = (RecyclerView)findViewById(R.id.news_feed_feed_RecyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        initListView();
    }//뷰에 관련된 변수 초기화

    private void initListView(){
        news_feed_feed_RecyclerView.setHasFixedSize(true);
        feedAdapter = new NewsFeedAdapter(getApplicationContext());
        news_feed_feed_RecyclerView.setAdapter(feedAdapter);
        news_feed_feed_RecyclerView.setLayoutManager(layoutManager);

    }//리스트뷰 초기화



    private void updateUser(UserData user){
        if (user != null){
            if(user.getUserImageUrl() == null||String.valueOf(user.getUserImageUrl()).length() == 0){
                Glide.with(this).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(news_feed_user_feed_button);

            }else{

                Glide.with(this).load(App.BASE_URL+"/files/"+user.getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(news_feed_user_feed_button);

            }

        }
    }//유저 버튼 이미지 갱신

    private void updateNewsFeed(List<FeedData> feedData){
        feedAdapter.updateNewsFeed(feedData);
    }//피드 갱신



}
