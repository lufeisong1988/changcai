package com.juggist.commonlibrary.rx;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by liuxingwei on 2016/11/30.
 */

public class RefreshOrderEvent {

    /**
     * 静态、单例、不可实例化
     */
    private RefreshOrderEvent() {
    }

    /**
     * UserRefreshState
     */
    private static PublishSubject<Boolean> stateObservable = PublishSubject.create();

    /**
     * 获取UserRefreshState的全局Observable
     */
    public static Observable<Boolean> getObservable() {
        return stateObservable;
    }

    /**
     * 发布登录状态变化事件: true 刷新、
     */
    public static void publish(boolean bDone) {
        stateObservable.onNext(bDone);
    }
}
