package com.mksoft.sns_project.Activity.AddFeed;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.etcMethod.EtcMethodClass;

import java.io.File;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewsFeedActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    Intent intent;
    EtcMethodClass etcMethodClass;

    private static final int PICK_FROM_ALBUM = 1;
    Uri photoUri;
    File tempFile;
    boolean imageState = false;



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
    //xml레이아웃
    //전체레이아웃
    RelativeLayout add_news_feed_layout;
    //툴바레이아웃
    Toolbar add_news_feed_toolbar;
    //게시물작성 레이아웃
    EditText add_news_feed_feed_contents_editText;
    ImageView add_news_feed_add_imageView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_add_news_feed_action, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_news_feed_send_to_server_button :
                FeedData feedData = new FeedData();
                feedData.setContent(add_news_feed_feed_contents_editText.getText().toString());
                feedData.setNumOfLikes(0);
                feedData.setWriter(App.userID);
                feedData.setFeedImgUrl(tempFile.getName());
                apiRepo.postNewsFeed(feedData);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), tempFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", tempFile.getName(), requestFile);
                apiRepo.saveImage(body);
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
        etcMethodClass.permissionMethod.tedPermission();
        etcMethodClass.hideKeyboard.clickHideKeyboard();
        clickAddImageView();
    }
    void init(){

        intent = new Intent(Intent.ACTION_PICK);

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
                if(etcMethodClass.permissionMethod.getPermission()){
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

        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


}
