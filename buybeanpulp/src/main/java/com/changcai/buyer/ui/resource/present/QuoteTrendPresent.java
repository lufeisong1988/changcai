package com.changcai.buyer.ui.resource.present;

import android.view.View;
import android.widget.ListView;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.resource.QuoteTrendViewModel;
import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;
import com.changcai.buyer.ui.resource.bean.QuoteBean;
import com.changcai.buyer.ui.resource.model.QuoteTrendModel;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lufeisong on 2017/8/30.
 */

public class QuoteTrendPresent implements QuoteTrendInterface,QuoteTrendPresentCallback<DomainsAndTypesBean>{
    private int currentPage = 0;//当前页面
    private String currentProductType = "";//定价方式
    private String currentDomainId = "";//区域
    private String currentDomainName = "";//区域
//    private int totalItem = 0;//总共的数据
//    private int currentItem = 0; //当前总共的数据
    private int currentProductTypePosition = -1;//当前位置
    private int currentDomainIdPosition = -1;//当前位置
    private final int MAX_ITEM = 10;//每次返回的最大页数


    private List<DomainsAndTypesBean.DomainsBean> domains = new ArrayList<>();
    private List<DomainsAndTypesBean.ProductTypeBean> productType = new ArrayList<>();

    private QuoteTrendViewModel quoteTrendViewModel;
    private QuoteTrendModel quoteTrendModel;

    public QuoteTrendPresent(QuoteTrendViewModel quoteTrendViewModel) {
        this.quoteTrendViewModel = quoteTrendViewModel;
        quoteTrendModel = new QuoteTrendModel(this);
    }

    /**
     * 没有数据，初始化数据
     */
    @Override
    public void init() {
        if(quoteTrendViewModel != null){
            if (UserDataUtil.isLogin()) {
                quoteTrendViewModel.dismissLoginView();
                quoteTrendViewModel.loadMoreEnable(true);
            }else{
                quoteTrendViewModel.showLoginView();
                quoteTrendViewModel.loadMoreEnable(false);
            }
        }
        getDomainsAndType();
    }

    @Override
    public void clickPriceList(ListView listview) {
        if (quoteTrendViewModel != null && listview != null) {
            dismissAreaList();
            if (listview.getVisibility() == View.GONE) {
                quoteTrendViewModel.showLvQuotertrendPrice();
                quoteTrendViewModel.showRlQuotertrendBg();
                quoteTrendViewModel.quoterendPriceArrowUp();
            } else if (listview.getVisibility() == View.VISIBLE) {
                quoteTrendViewModel.dismissLvQuotertrendPrice();
                quoteTrendViewModel.dismissRlQuotertrendBg();
                quoteTrendViewModel.quoterendPriceArrowDown();
            }
        }

    }

    @Override
    public void clickAreaList(ListView listview) {
        if (quoteTrendViewModel != null && listview != null) {
            dismissPriceList();
            if (listview.getVisibility() == View.GONE) {
                quoteTrendViewModel.showLvQuotertrendArea();
                quoteTrendViewModel.showRlQuotertrendBg();
                quoteTrendViewModel.quoterendAraeArrowUp();
            } else if (listview.getVisibility() == View.VISIBLE) {
                quoteTrendViewModel.dismissLvQuotertrendArea();
                quoteTrendViewModel.dismissRlQuotertrendBg();
                quoteTrendViewModel.quoterendAreaArrowDown();
            }

        }

    }

    @Override
    public void clickListBg() {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissRlQuotertrendBg();
            quoteTrendViewModel.quoterendPriceArrowDown();
            quoteTrendViewModel.quoterendAreaArrowDown();
        }
    }

    /**
     * 获取tab
     */
    @Override
    public void  getDomainsAndType() {
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.showLoading();
        }
        quoteTrendModel.getDomainsAndType();
    }

    /**
     * 修改页卡后初始化数据
     */
    @Override
    public void initQuoteData( int productTypePosition,int domainIdPosition) {
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.dismissRlQuotertrendBg();
            quoteTrendViewModel.quoterendPriceArrowDown();
            quoteTrendViewModel.quoterendAreaArrowDown();
        }
        currentPage = 0;
        if(productTypePosition != -1){
            this.currentProductTypePosition = productTypePosition;
            currentProductType = productType.get(productTypePosition).getId();
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.updatePriceName(productType.get(productTypePosition).getName());
            }
        }
        if(domainIdPosition != -1){
            this.currentDomainIdPosition = domainIdPosition;
            currentDomainName = domains.get(domainIdPosition).getName();
            currentDomainId = String.valueOf(domains.get(domainIdPosition).getId());
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.updateDomainName(domains.get(domainIdPosition).getName());
            }
        }
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.showLoading();

        }
        quoteTrendModel.getAllQuote(currentProductType,currentDomainId,String.valueOf(currentPage));
    }


    /**
     * 刷新当前页卡的数据
     */
    @Override
    public void refreshQuoteData() {
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.showLoading();
        }
        currentPage = 0;
        quoteTrendModel.getAllQuote(currentProductType,currentDomainId,String.valueOf(currentPage));
    }

    /**
     * 加载当前页卡的更多数据
     */
    @Override
    public void loadMoreQuoteData() {
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.showLoading();
        }
        currentPage++;
        quoteTrendModel.getAllQuote(currentProductType,currentDomainId,String.valueOf(currentPage));
    }

    @Override
    public void onDestory() {
        quoteTrendViewModel = null;
    }

    /**
     * 刷新当前数据
     */
    @Override
    public void initQuoteData() {
        if(currentProductTypePosition == -1 || currentDomainIdPosition == -1){//数据不存在重新刷新
            init();
        }else{
            initQuoteData(currentProductTypePosition,currentDomainIdPosition);//reload当前数据
        }
    }

    private void dismissPriceList() {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissLvQuotertrendPrice();
            quoteTrendViewModel.dismissRlQuotertrendBg();
            quoteTrendViewModel.quoterendPriceArrowDown();
        }
    }

    private void dismissAreaList() {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissLvQuotertrendArea();
            quoteTrendViewModel.dismissRlQuotertrendBg();
            quoteTrendViewModel.quoterendAreaArrowDown();
        }
    }



    /**
     * 获取tab成功
     * @param domainsAndTypesBean
     */
    @Override
    public void onNext(DomainsAndTypesBean domainsAndTypesBean) {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissLoaidng();
            quoteTrendViewModel.updateAreaData(domainsAndTypesBean.getDomains());
            quoteTrendViewModel.updatePriceData(domainsAndTypesBean.getProductType());
        }
        this.domains = domainsAndTypesBean.getDomains();
        this.productType = domainsAndTypesBean.getProductType();
        initQuoteData(0,0);
    }
    @Override
    public void onError(Throwable e) {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissLoaidng();
            quoteTrendViewModel.showNetErrorDialog();
        }
    }

    /**
     * 获取tab下数据成功
     * @param quoteBean
     */
    @Override
    public void onGetQuoteNext(QuoteBean quoteBean) {
        if(quoteTrendViewModel != null){
            quoteTrendViewModel.dismissLoaidng();
            if(currentPage == 0){
                quoteTrendViewModel.refreshFinish();
            }else{
                quoteTrendViewModel.loadMoreFinish();
            }
        }
        List<QuoteBean.AllQuoteBean.ResultBean> list = new ArrayList<>();
//        totalItem = quoteBean.getAllQuote().getTotal();

        if(currentPage == 0){//初始化||刷新返回的数据
            QuoteBean.AllQuoteBean.ResultBean resultBean = new QuoteBean.AllQuoteBean.ResultBean();
            resultBean.type = QuoteBean.AllQuoteBean.ResultBean.CHILD;
            resultBean.headStr = toJsStr(currentDomainId);
            resultBean.domainsName = currentDomainName;
            QuoteBean.AllQuoteBean.ResultBean resultBean2 = new QuoteBean.AllQuoteBean.ResultBean();
            resultBean2.type = QuoteBean.AllQuoteBean.ResultBean.GROUP;
            list.add(resultBean);
            list.add(resultBean2);
            list.addAll(quoteBean.getAllQuote().getResult());
//            currentItem = quoteBean.getAllQuote().getResult().size();
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.refreshQuoteData(list);
            }
        }else{//加载更多返回的数据，要判断是否是最后一页
            list.addAll(quoteBean.getAllQuote().getResult());
//            currentItem = currentItem + quoteBean.getAllQuote().getResult().size();
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.loadMoreQuoteData(list);
            }

        }
//        LogUtil.d("TAG","currentItem = " + currentItem + ";totalItem = " + totalItem);
        if(MAX_ITEM > quoteBean.getAllQuote().getResult().size()){//数据已经加载到底
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.loadMoreEnable(false);
            }
        }else{//数据未加载到底
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.loadMoreEnable(true);
            }


        }
    }

    @Override
    public void onGetQuoteError(Throwable e) {
        if (quoteTrendViewModel != null) {
            quoteTrendViewModel.dismissLoaidng();
            quoteTrendViewModel.showNetErrorDialog();
        }
        if(currentPage > 0){
            currentPage--;
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.loadMoreFinish();
            }
        }else{
            if(quoteTrendViewModel != null){
                quoteTrendViewModel.refreshFinish();
            }
        }
    }
    String toJsStr(String domainId){
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("domainId", domainId);
        Gson gson = new Gson();
        return DesUtil.encrypt(gson.toJson(map), "abcdefgh");

    }
}


interface QuoteTrendInterface {

    void init();

    void clickPriceList(ListView listview);

    void clickAreaList(ListView listview);

    void clickListBg();

    void getDomainsAndType();


    void initQuoteData(int productTypePosition,int domainIdPosition);


    void refreshQuoteData();

    void loadMoreQuoteData();

    void onDestory();

    void initQuoteData();
}
