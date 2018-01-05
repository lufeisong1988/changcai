package com.changcai.buyer;

/**
 * Created by lufeisong on 2017/8/31.
 */

public interface BasePresnetCallback <T>{
    void onError(Throwable e);
    void onNext(T t);

}
