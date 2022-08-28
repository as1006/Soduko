package com.stars.lego.adapter.bridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asherchen(陈凌明) on 2018/11/21.
 * 游戏平台部
 * Item之间的通信中心
 */
public class ItemBridge {

    private Map<String,List<BridgeEntity>> mItems = new HashMap<>();

    public void subscribe(String event,BridgeEntity bridgeEntity){
        List<BridgeEntity> items = mItems.get(event);
        if (items == null){
            items = new ArrayList<>();
            mItems.put(event,items);
        }
        if (!items.contains(bridgeEntity)){
            items.add(bridgeEntity);
        }
    }

    public void publish(BridgeEntity sender,String event,Object args){
        List<BridgeEntity> items = mItems.get(event);
        if (items != null){
            for (BridgeEntity item : items){
                item.onBridge(sender,event,args);
            }
        }
    }

}
