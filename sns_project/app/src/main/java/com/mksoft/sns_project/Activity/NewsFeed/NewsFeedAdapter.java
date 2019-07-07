package com.mksoft.sns_project.Activity.NewsFeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mksoft.sns_project.Activity.UserFeed.OtherUserFeedActivity;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.DataType.FeedData;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //xml 변수
        TextView news_feed_item_user_name_textView;
        ImageView news_feed_item_feed_imageView;
        ImageView news_feed_item_register_imageView;
        TextView news_feed_item_contents_textView;
        TextView news_feed_item_like_textView;

        MyViewHolder(View view){
            super(view);
            news_feed_item_user_name_textView = view.findViewById(R.id.news_feed_item_user_name_textView);
            news_feed_item_feed_imageView = view.findViewById(R.id.news_feed_item_feed_imageView);
            news_feed_item_contents_textView = view.findViewById(R.id.news_feed_item_contents_textView);
            news_feed_item_like_textView = view.findViewById(R.id.news_feed_item_like_textView);
            news_feed_item_register_imageView = view.findViewById(R.id.news_feed_item_register_imageView);

        }
    }

    List<FeedData> items =  Collections.emptyList();
    Context context;
    MyViewHolder myViewHolder;
    Intent intent;
    public NewsFeedAdapter(Context context){
        this.context = context;
        intent = new Intent(context, OtherUserFeedActivity.class);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_item, parent, false);

        return new MyViewHolder(v);
    }

    //Bundle bundle;
    //FeedItemViewFragment FeedItemViewFragment;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        myViewHolder = (MyViewHolder) holder;
        myViewHolder.news_feed_item_user_name_textView.setText(items.get(position).getWriter());
        Glide.with(context).load((App.BASE_URL+"/files/"+items.get(position))).into(myViewHolder.news_feed_item_feed_imageView);
        myViewHolder.news_feed_item_contents_textView.setText(items.get(position).getContent());
        myViewHolder.news_feed_item_like_textView.setText("좋아요 "+(items.get(position).getNumOfLikes())+"개");
        if(items.get(position).getUserImgUrl() == null||items.get(position).getUserImgUrl().length() == 0){
            Glide.with(context).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(myViewHolder.news_feed_item_register_imageView);
        }else{

            Glide.with(context).load(App.BASE_URL+items.get(position).getUserImgUrl()).apply(RequestOptions.circleCropTransform()).into(myViewHolder.news_feed_item_register_imageView);
        }




        myViewHolder.news_feed_item_user_name_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("otherUserID", myViewHolder.news_feed_item_user_name_textView.getText().toString());
                context.startActivity(intent);
            }
        });
    }//itemView초기화

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }//피드 안보이게 하기


    public void updateNewsFeed(List<FeedData> items){
        this.items = items;
        notifyDataSetChanged();
    }
    public FeedData getItem(int idx){
        return items.get(idx);
    }
    public List<FeedData> getAllItem(){return items;}
}
