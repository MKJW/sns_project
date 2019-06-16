package com.mksoft.sns_project.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mksoft.sns_project.Repository.APIRepo;
import com.mksoft.sns_project.Repository.DataType.UserData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Philippe on 02/03/2018.
 */

public class UserViewModel extends ViewModel {

    private LiveData<com.mksoft.sns_project.Repository.DataType.UserData> user;
    private APIRepo apiRepo;

    @Inject
    public UserViewModel(APIRepo apiRepo) {

        this.apiRepo = apiRepo;
    }

    // ----

    public void init(String userId) {
        if (this.user != null) {
            return;
        }
        user = apiRepo.getUserLiveData(userId);

    }

    public LiveData<UserData> getUserLiveData() {
        return this.user;
    }
}
