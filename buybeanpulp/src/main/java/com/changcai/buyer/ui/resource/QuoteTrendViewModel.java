package com.changcai.buyer.ui.resource;

import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;
import com.changcai.buyer.ui.resource.bean.QuoteBean;

import java.util.List;

/**
 * Created by lufeisong on 2017/8/30.
 */
public interface QuoteTrendViewModel {
    void showLoginView();

    void dismissLoginView();

    void showRlQuotertrendBg();

    void dismissRlQuotertrendBg();

    void showLvQuotertrendPrice();

    void dismissLvQuotertrendPrice();

    void showLvQuotertrendArea();

    void dismissLvQuotertrendArea();

    void quoterendPriceArrowUp();

    void quoterendPriceArrowDown();

    void quoterendAraeArrowUp();

    void quoterendAreaArrowDown();

    void showLoading();

    void dismissLoaidng();

    void updatePriceData(List<DomainsAndTypesBean.ProductTypeBean> productTypeBeen);

    void updateAreaData(List<DomainsAndTypesBean.DomainsBean> domainsBeen);

    void showNetErrorDialog();

    void refreshQuoteData(List<QuoteBean.AllQuoteBean.ResultBean> resultBeens);

    void loadMoreQuoteData(List<QuoteBean.AllQuoteBean.ResultBean> resultBeens);

    void refreshFinish();

    void loadMoreFinish();


    void refreshEnable(boolean refreshEnable);

    void loadMoreEnable(boolean loadMoreEnable);

    void loadWebview(String parmarsStr);

    void updateDomainName(String name);

    void updatePriceName(String name);
}
