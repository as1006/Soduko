package com.stars.base.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class StarApplication extends Application implements ViewModelStoreOwner {

    private ViewModelStore mApplicationViewModelStore = new ViewModelStore();

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mApplicationViewModelStore;
    }


    private ViewModelProvider mApplicationProvider;

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory());
        }
        return mApplicationProvider.get(modelClass);
    }
}
