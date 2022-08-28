package com.stars.lego.adapter.core;

/**
 * Created by asherchen(陈凌明) on 2018/12/5.
 * 游戏平台部
 */
public interface ContextDataSet {

    <T> T getContextData(String key);

}
