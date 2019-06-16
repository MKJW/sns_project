package com.mksoft.sns_project.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FeedData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import javax.inject.Inject;

public class NewsFeedViewModel extends ViewModel {
    private LiveData<List<FeedData>> newsFeed;
    private APIRepo apiRepo;

    @Inject
    public NewsFeedViewModel(APIRepo apiRepo) {

        this.apiRepo = apiRepo;
    }

    // ----

    public void init(String userId) {
        if (this.newsFeed != null) {
            return;
        }
        newsFeed = apiRepo.getFeedListLiveData(userId);

    }

    public LiveData<List<FeedData>> getNewsFeedLiveData() {
        return this.newsFeed;
    }
}
