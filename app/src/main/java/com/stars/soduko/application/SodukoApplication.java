package com.stars.soduko.application;

import com.blankj.utilcode.util.Utils;
import com.stars.base.ui.StarApplication;
import com.stars.soduko.viewmodel.SodukoPuzzleViewModel;

public class SodukoApplication extends StarApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        SodukoPuzzleViewModel viewModel = getApplicationScopeViewModel(SodukoPuzzleViewModel.class);
        viewModel.init();
    }




}
