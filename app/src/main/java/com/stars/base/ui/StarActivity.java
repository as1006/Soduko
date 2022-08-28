package com.stars.base.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stars.soduko.application.SodukoApplication;

public class StarActivity extends AppCompatActivity {
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory());
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((StarApplication)getApplication(), new ViewModelProvider.NewInstanceFactory());
        }
        return mApplicationProvider.get(modelClass);
    }
}
