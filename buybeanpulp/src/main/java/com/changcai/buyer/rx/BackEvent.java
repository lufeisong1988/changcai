package com.changcai.buyer.rx;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by liuxingwei on 2016/12/1.
 */

public class BackEvent {

    /**
     * 静态、单例、不可实例化
     */
    private BackEvent() {
    }

    /**
     * LoginState的全局Observable
     */
    private static PublishSubject<Boolean> stateObservable = PublishSubject.create();

    /**
     * 获取LoginState的全局Observable
     */
    public static Observable<Boolean> getObservable() {
        return stateObservable;
    }

    /**
     * 发布登录状态变化事件: true 表示登入或登出成功、false 表示取消回退至登录前页面
     */
    public static void publish(boolean bDone) {
        stateObservable.onNext(bDone);
    }
}
