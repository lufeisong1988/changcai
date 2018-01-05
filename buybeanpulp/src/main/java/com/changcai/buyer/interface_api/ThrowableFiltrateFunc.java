package com.changcai.buyer.interface_api;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by liuxingwei on 2017/2/24.
 */

public class ThrowableFiltrateFunc<T>  implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        //ExceptionEngine为处理异常的驱动器
        return Observable.error( ExceptionDispatcher.handleException(throwable));
    }
}


