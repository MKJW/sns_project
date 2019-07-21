package com.mksoft.sns_project.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.FolloweeData;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import javax.inject.Inject;

public class FolloweeViewModel extends ViewModel {
    private LiveData<List<FolloweeData>> livedataFollowee;
    private APIRepo apiRepo;
    @Inject
    public FolloweeViewModel(APIRepo apiRepo){
        this.apiRepo = apiRepo;
    }
    public void init(String userId) {
        if (this.livedataFollowee != null) {
            return;
        }
        livedataFollowee = apiRepo.getFolloweeLiveData(userId);

    }
    public LiveData<List<FolloweeData>> getUserLiveData() {
        return this.livedataFollowee;
    }
}
