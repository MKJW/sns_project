package com.mksoft.sns_project.DI;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.mksoft.sns_project.ViewModel.FactoryViewModel;
import com.mksoft.sns_project.ViewModel.FolloweeViewModel;
import com.mksoft.sns_project.ViewModel.FollowerViewModel;
import com.mksoft.sns_project.ViewModel.NewsFeedViewModel;
import com.mksoft.sns_project.ViewModel.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Philippe on 02/03/2018.
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(NewsFeedViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FolloweeViewModel.class)
    abstract ViewModel bindNewsFolloweeViewModel(FolloweeViewModel repoViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(FollowerViewModel.class)
    abstract ViewModel bindNewsFollowerViewModel(FollowerViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
