package com.mksoft.sns_project.Activity.AddFeed;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class AddNewsFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    String userID;

    EtcMethodClass etcMethodClass;

    private static final int PICK_FROM_ALBUM = 1;
    Uri photoUri;
    File tempFile;



    public static AddNewsFeedActivity addNewsFeedActivity;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    APIRepo apiRepo;
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    RelativeLayout add_news_feed_layout;
    EditText add_news_feed_feed_contents_editText;
    ImageView add_news_feed_add_imageView;
    boolean imageState = false;
    Toolbar add_news_feed_toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_add_page_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share :
                FeedData feedData = new FeedData();
                feedData.setContent(add_news_feed_feed_contents_editText.getText().toString());
                feedData.setNumOfLikes(0);
                feedData.setWriter(userID);
                apiRepo.postNewsFeed(feedData);
                return true;
            default :
                this.finish();
                return super.onOptionsItemSelected(item) ;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news_feed_activity);

        this.configureDagger();
        addNewsFeedActivity = this;
        init();
        etcMethodClass = new EtcMethodClass(this, add_news_feed_layout);
        etcMethodClass.tedPermission();
        etcMethodClass.clickHideKeyboard();
        clickAddImageView();
    }
    void init(){


        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");//사용자 정보

        add_news_feed_toolbar = findViewById(R.id.add_news_feed_toolbar);
        setSupportActionBar(add_news_feed_toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//홈버튼 활성화

        add_news_feed_layout = findViewById(R.id.add_news_feed_layout);
        add_news_feed_feed_contents_editText = findViewById(R.id.add_news_feed_feed_contents_editText);
        add_news_feed_add_imageView = findViewById(R.id.add_news_feed_add_imageView);

    }

    private void clickAddImageView(){
        add_news_feed_add_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etcMethodClass.getPermission()){
                    goToAlbum();
                }else{
                    Toast.makeText(getApplicationContext(), "권한을 먼저 승인해 주세여.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {

                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if(data == null)
                return;
            photoUri = data.getData();
            Glide.with(this).load(photoUri).into(add_news_feed_add_imageView);
            Cursor cursor = null;
            imageState = true;
            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }


        }
    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


}
