package com.mksoft.sns_project.Activity.FollowListPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    public FollowListPageListViewAdapter(Context context, Integer pageState, APIRepo apiRepo){
        this.context = context;
        this.apiRepo = apiRepo;
        this.pageState = pageState;
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

        if(pageState == 0){
            apiRepo.checkFollowee(App.userID, items.get(position).getUserId(), myViewHolder.follow_list_page_follow_state_button, "팔로잉");

            //팔로워 페이지
        }else if(pageState == 1){
            myViewHolder.follow_list_page_follow_state_button.setText("팔로잉");
        }//팔로잉 페이지
        myViewHolder.follow_list_page_follow_state_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.follow_list_page_follow_state_button.getText() == "팔로우"){
                    apiRepo.addFollower(App.userID, items.get(position).getUserId(),myViewHolder.follow_list_page_follow_state_button);
                }else if(myViewHolder.follow_list_page_follow_state_button.getText() == "팔로잉"){
                    //팔로우 취소

                }
            }
        });

        if(items.get(position).getUserImageUrl() == null||String.valueOf(items.get(position).getUserImageUrl()).length() == 0){
            Glide.with(context).load(R.drawable.userbaseimg).apply(RequestOptions.circleCropTransform()).into(myViewHolder.follow_list_page_user_imageView);

        }else{

            Glide.with(context).load(App.BASE_URL+"/files/"+items.get(position).getUserImageUrl()).apply(RequestOptions.circleCropTransform()).into(myViewHolder.follow_list_page_user_imageView);
        }
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

        MyViewHolder(View view){
            super(view);
            follow_list_page_user_imageView = view.findViewById(R.id.follow_list_page_user_imageView);
            follow_list_page_user_id_textView = view.findViewById(R.id.follow_list_page_user_id_textView);
            follow_list_page_user_name_textView = view.findViewById(R.id.follow_list_page_user_name_textView);
            follow_list_page_follow_state_button = view.findViewById(R.id.follow_list_page_follow_state_button);
            follow_list_page_delete_button = view.findViewById(R.id.follow_list_page_delete_button);

        }
    }
}
