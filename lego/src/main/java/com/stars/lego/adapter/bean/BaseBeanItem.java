package com.stars.lego.adapter.bean;

import android.content.Context;

import androidx.annotation.NonNull;

import com.stars.lego.adapter.core.BaseItem;
import com.stars.lego.adapter.core.BaseViewHolder;

/**
 * Created by asherchen(陈凌明) on 2018/11/15.
 * 游戏平台部
 */
public abstract class BaseBeanItem<T> extends BaseItem {

    protected T bean;

    public BaseBeanItem(Context context,T bean) {
        super(context);
        this.bean = bean;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {

    }




}
