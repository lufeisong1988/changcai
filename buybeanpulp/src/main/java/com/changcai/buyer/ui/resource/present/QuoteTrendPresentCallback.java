package com.changcai.buyer.ui.resource.present;

import com.changcai.buyer.BasePresnetCallback;
import com.changcai.buyer.ui.resource.bean.QuoteBean;

/**
 * Created by lufeisong on 2017/8/31.
 */

public interface QuoteTrendPresentCallback<T>  extends BasePresnetCallback<T>{
    void onGetQuoteNext(QuoteBean quoteBean);
    void onGetQuoteError(Throwable e);
}
