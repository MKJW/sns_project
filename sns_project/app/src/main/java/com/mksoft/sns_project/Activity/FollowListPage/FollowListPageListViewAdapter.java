package com.mksoft.sns_project.Activity.FollowListPage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mksoft.sns_project.Activity.UserFeed.UserFeedActivity;
import com.mksoft.sns_project.App;
import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.Collections;
import java.util.List;

public class FollowListPageListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    List<UserData> items =  Collections.emptyList();//userData로
    Context context;
    MyViewHolder myViewHolder;
    APIRepo apiRepo;
    Integer pageState;
    Intent intent;
    public FollowListPageListViewAdapter(Context context, Integer pageState, APIRepo apiRepo){
        this.context = context;
        this.apiRepo = apiRepo;
        this.pageState = pageState;

        intent = new Intent(context, UserFeedActivity.class);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_page_listview_item, parent, false);
        return new MyViewHolder(v);
    }

    //Bundle bundle;
    //FeedItemViewFragment FeedItemViewFragment;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        myViewHolder = (MyViewHolder) holder;
        myViewHolder.follow_list_page_user_id_textView.setText(items.get(position).getUserId());
        myViewHolder.follow_list_page_user_name_textView.setText(items.get(position).getUsername());
        setStateButton(myViewHolder.follow_list_page_follow_state_button,position);

        if(items.get(position).getUserImageUrl() == null||String.valueOf(items.get(position).getUserImageUrl()).length() == 0){
            Glide.with(context).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(myViewHolder.follow_list_page_user_imageView);

        }else{

            Glide.with(context).load(App.BASE_URL+"/files/"+items.get(position).getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(myViewHolder.follow_list_page_user_imageView);
        }
        myViewHolder.follow_list_page_listview_item_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("masterID", items.get(position).getUserId());
                intent.putExtra("fromPageState", 5);//뒤로가기 버튼을 위하여
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


    public void updateFollowList(List<UserData> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //xml 변수
        ImageView follow_list_page_user_imageView;
        TextView follow_list_page_user_id_textView;
        TextView follow_list_page_user_name_textView;
        Button follow_list_page_follow_state_button;
        ImageView follow_list_page_delete_button;
        RelativeLayout follow_list_page_listview_item_relativeLayout;
        MyViewHolder(View view){
            super(view);
            follow_list_page_user_imageView = view.findViewById(R.id.follow_list_page_user_imageView);
            follow_list_page_user_id_textView = view.findViewById(R.id.follow_list_page_user_id_textView);
            follow_list_page_user_name_textView = view.findViewById(R.id.follow_list_page_user_name_textView);
            follow_list_page_follow_state_button = view.findViewById(R.id.follow_list_page_follow_state_button);
            follow_list_page_delete_button = view.findViewById(R.id.follow_list_page_delete_button);
            follow_list_page_listview_item_relativeLayout = view.findViewById(R.id.follow_list_page_listview_item_relativeLayout);

        }
    }
    private void setStateButton(Button followStateButton, int position){
        if(items.get(position).isFollowWhithLoginUser()){

            followStateButton.setText("팔로잉");
            followStateButton.setBackgroundResource(R.drawable.custom_follow_state_button);
            followStateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiRepo.subFollower(App.userID, items.get(position).getUserId());
                    items.get(position).setFollowWhithLoginUser(false);
                    setStateButton(followStateButton, position);
                }
            });
        }else{
            followStateButton.setText("팔로우");
            followStateButton.setBackgroundResource(R.drawable.custom_follow_state_button_blue);
            followStateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiRepo.addFollower(App.userID, items.get(position).getUserId());
                    items.get(position).setFollowWhithLoginUser(true);
                    setStateButton(followStateButton, position);
                }
            });
        }
    }
}
