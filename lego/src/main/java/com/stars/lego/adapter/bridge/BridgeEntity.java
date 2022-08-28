package com.stars.lego.adapter.bridge;

/**
 * Created by asherchen(陈凌明) on 2018/11/21.
 * 游戏平台部
 * Item通信实体
 */
public interface BridgeEntity {
    void onBridge(Object sender,String event,Object args);
}
