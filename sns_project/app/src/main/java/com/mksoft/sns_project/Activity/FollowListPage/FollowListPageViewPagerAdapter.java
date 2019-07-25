package com.mksoft.sns_project.Activity.FollowListPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.mksoft.sns_project.R;
import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowListPageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<FollowListPageListViewAdapter> followListAdapter = new ArrayList<>();
    private List<UserData> followerList = Collections.emptyList();
    private List<UserData> followeeList = Collections.emptyList();
    FollowListPageViewPagerAdapter(Context context, APIRepo apiRepo) {
        this.context = context;
        FollowListPageListViewAdapter followeeListAdapter =new FollowListPageListViewAdapter(context, 1, apiRepo);
        FollowListPageListViewAdapter followerListAdapter =new FollowListPageListViewAdapter(context, 0, apiRepo);
        followListAdapter.add(followerListAdapter);
        followListAdapter.add(followeeListAdapter);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null ;
        if (context != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.follow_list_page_viewpager_item, container, false);
            RecyclerView follow_list_page_recyclerView = view.findViewById(R.id.follow_list_page_recyclerView);
            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(context);
            follow_list_page_recyclerView.setHasFixedSize(true);
            follow_list_page_recyclerView.setAdapter(followListAdapter.get(position));
            follow_list_page_recyclerView.setLayoutManager(layoutManager);
        }
        container.addView(view) ;
        return view ;
    }

    public void updateFollowerList(List<UserData> items){
         followerList = items;
        followListAdapter.get(0).updateFollowList(followerList);
        notifyDataSetChanged();
    }
    public void updataFolloweeList(List<UserData> items){
        followeeList = items;
        followListAdapter.get(1).updateFollowList(followeeList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  (view == (View)object);
    }


}
