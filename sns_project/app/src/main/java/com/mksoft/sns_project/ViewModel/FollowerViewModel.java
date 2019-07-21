package com.mksoft.sns_project.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.FollowerData;

import java.util.List;

import javax.inject.Inject;

public class FollowerViewModel extends ViewModel {
    private LiveData<List<FollowerData>> livedataFollower;
    private APIRepo apiRepo;
    @Inject
    public FollowerViewModel(APIRepo apiRepo){
        this.apiRepo = apiRepo;
    }
    public void init(String userId) {
        if (this.livedataFollower != null) {
            return;
        }
        livedataFollower = apiRepo.getFollowerLiveData(userId);

    }
    public LiveData<List<FollowerData>> getUserLiveData() {
        return this.livedataFollower;
    }
}
