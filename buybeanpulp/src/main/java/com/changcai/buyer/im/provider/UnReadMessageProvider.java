package com.changcai.buyer.im.provider;

import java.util.ArrayList;

/**
 * Created by lufeisong on 2017/12/23.
 */

public class UnReadMessageProvider {
    private ArrayList<UnreadMessageCallback> callbacks = new ArrayList<>();
    public interface UnreadMessageCallback{
        void updateUnReadMessageCount(int unReadMessageCount);
    }
    private static UnReadMessageProvider provider;

    public UnReadMessageProvider() {

    }

    public static UnReadMessageProvider getProvider() {
        if(provider == null){
            provider = new UnReadMessageProvider();
        }
        return provider;
    }

    public void addCallbacks(UnreadMessageCallback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }
    public void deleteCallback(UnreadMessageCallback callback){
        if(callbacks.contains(callback)){
            callbacks.remove(callback);
        }
    }

    public void updateUnReadMessage(int unReadMessageCount){
        for(int i = 0;i < callbacks.size();i++){
            callbacks.get(i).updateUnReadMessageCount(unReadMessageCount);
        }
    }
}
