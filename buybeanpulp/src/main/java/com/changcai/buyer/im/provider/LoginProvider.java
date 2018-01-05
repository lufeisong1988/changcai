package com.changcai.buyer.im.provider;

import java.util.ArrayList;

/**
 * Created by lufeisong on 2017/12/26.
 */

public class LoginProvider {
    private static LoginProvider instance;
    private ArrayList<LoginCallback> list = new ArrayList<>();
    public interface LoginCallback{
        void nimLoginSucceed();
        void nimLoginFail(String failStr);
    }
    public LoginProvider() {
    }

    public static LoginProvider getInstance() {
        if(instance == null){
            instance = new LoginProvider();
        }
        return instance;
    }
    public void addLoginCallback(LoginCallback callback){
        if(!list.contains(callback)){
            list.add(callback);
        }
    }
    public void deleteLoginCallback(LoginCallback callback){
        if(list.contains(callback)){
            list.remove(callback);
        }
    }
    public void updateLoginCallbackSucceed(){
        for (int i = 0;i < list.size();i++){
            list.get(i).nimLoginSucceed();
        }
    }
    public void updateLoginCallbackFail(String failStr){
        for (int i = 0;i < list.size();i++){
            list.get(i).nimLoginFail(failStr);
        }
    }

}
