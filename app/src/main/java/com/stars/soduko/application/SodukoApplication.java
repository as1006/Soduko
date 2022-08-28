package com.stars.soduko.application;

import com.blankj.utilcode.util.Utils;
import com.stars.base.ui.StarApplication;
import com.stars.kit.lego.layoutcenter.LayoutCenter;
import com.stars.soduko.model.SodukoPuzzle;
import com.stars.soduko.ui.level.LevelActivity;
import com.stars.soduko.viewmodel.SodukoPuzzleViewModel;

public class SodukoApplication extends StarApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        SodukoPuzzleViewModel viewModel = getApplicationScopeViewModel(SodukoPuzzleViewModel.class);
        viewModel.init();

        LayoutCenter.getInstance().registerItemBuilder(SodukoPuzzle.class, LevelActivity.PuzzleItem::new);
    }




}
