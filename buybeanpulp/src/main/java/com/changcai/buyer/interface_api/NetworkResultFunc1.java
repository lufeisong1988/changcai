package com.changcai.buyer.interface_api;


import com.changcai.buyer.common.Constants;

import rx.functions.Func1;

/**
 * Created by liuxingwei on 2016/11/28.
 */

public class NetworkResultFunc1<T> implements Func1<BaseApiModel<T>, T> {


    @Override
    public T call(BaseApiModel<T> tBaseApiModel) {
        if (tBaseApiModel.getErrorCode().contentEquals(Constants.REQUEST_SUCCESS_S)) {
            return tBaseApiModel.getResultObject();
        } else {
            throw new ApiCodeErrorException(tBaseApiModel.getErrorCode(), tBaseApiModel.getErrorDesc());
        }

    }
}
