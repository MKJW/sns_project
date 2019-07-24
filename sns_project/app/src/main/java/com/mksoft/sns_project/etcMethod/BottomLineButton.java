package com.mksoft.sns_project.etcMethod;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.mksoft.sns_project.Activity.AddFeed.AddNewsFeedActivity;
import com.mksoft.sns_project.Activity.NewsFeed.NewsFeedActivity;
import com.mksoft.sns_project.Activity.UserFeed.UserFeedActivity;
import com.mksoft.sns_project.App;

public class BottomLineButton {
    Context context;
    public BottomLineButton() {
        this.context = App.context;
    }

    public void click_home_button(ImageView homeButton){
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsFeedActivity.class);
                App.fromPageState = 0;
                context.startActivity(intent);
            }
        });
    }
    public void click_add_feed_button(ImageView addFeedButton){
        addFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewsFeedActivity.class);
                App.fromPageState = 2;
                context.startActivity(intent);

            }
        });
    }
    public void click_user_feed_button(ImageView userFeedButton){
        userFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserFeedActivity.class);
                App.fromPageState = 4;//유저 버튼을 통하여 넘어가는 경우
                intent.putExtra("masterID", App.userID);
                intent.putExtra("fromPageState", App.fromPageState);
                context.startActivity(intent);
            }
        });
    }//유저정보로 넘어가는 버튼

}
