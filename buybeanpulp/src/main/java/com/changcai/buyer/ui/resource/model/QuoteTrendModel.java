package com.changcai.buyer.ui.resource.model;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.resource.api.GetALLQuoteService;
import com.changcai.buyer.ui.resource.api.GetDomainsAndTypeService;
import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;
import com.changcai.buyer.ui.resource.bean.QuoteBean;
import com.changcai.buyer.ui.resource.present.QuoteTrendPresentCallback;
import com.changcai.buyer.util.SPUtil;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/8/30.
 * 报价趋势
 * 数据原型
 */

public class QuoteTrendModel implements QuoteTrendModelInterface{

    private QuoteTrendPresentCallback<DomainsAndTypesBean> callback;
    public QuoteTrendModel(QuoteTrendPresentCallback<DomainsAndTypesBean> callback) {
        this.callback = callback;
    }

    @Override
    public void getDomainsAndType() {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetDomainsAndTypeService getDomainsAndTypeService = ApiServiceGenerator.createService(GetDomainsAndTypeService.class);
        getDomainsAndTypeService.getDomainsAndType(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<DomainsAndTypesBean>>() {
                    @Override
                    public void call(BaseApiModel<DomainsAndTypesBean> domainsAndTypesBeanBaseApiModel) {
                        callback.onNext(domainsAndTypesBeanBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onError(throwable);
                    }
                });
    }

    /**
     *
     * @param domainId      区域Id
     * @param productType   定价方式
     * @param currentPage   页码
     */
    @Override
    public void getAllQuote(String productType, String domainId, String currentPage) {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("domainId", domainId);
        map.put("productType", productType);
        map.put("currentPage", currentPage);
        GetALLQuoteService getALLQuoteService = ApiServiceGenerator.createService(GetALLQuoteService.class);
        getALLQuoteService.getDomainsAndType(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<QuoteBean>>() {

                    @Override
                    public void call(BaseApiModel<QuoteBean> quoteBeanBaseApiModel) {
                        callback.onGetQuoteNext(quoteBeanBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onGetQuoteError(throwable);
                    }
                });

    }
}
interface QuoteTrendModelInterface{
    void getDomainsAndType();

    void getAllQuote(String domainId,String productType,String currentPage);
}