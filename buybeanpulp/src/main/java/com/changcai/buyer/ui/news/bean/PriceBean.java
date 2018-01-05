package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.bean.GetQuotePriceBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class PriceBean implements Serializable, IKeepFromProguard {
    private List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeen;
    private int responseState ;

    public List<GetQuotePriceBean.QuotePriceBean.ResultBean> getResultBeen() {
        return resultBeen;
    }

    public void setResultBeen(List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeen) {
        this.resultBeen = resultBeen;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }
}
