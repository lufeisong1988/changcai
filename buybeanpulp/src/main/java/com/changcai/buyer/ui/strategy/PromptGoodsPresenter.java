package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PromptGoodsModel;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.ErrorCode;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class PromptGoodsPresenter implements PromptGoodsContract.Presenter {

    PromptGoodsContract.View view;

    Subscription subscription;

    public PromptGoodsPresenter(PromptGoodsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(subscription);
    }


    @Override
    public void getSpotStrategy(final int pageIndex, String folderId) {
        if (pageIndex==0){
            view.showLoadProgress();
        }
        Map<String, String> params = new HashMap<>(2);
        params.put("currentPage", String.valueOf(pageIndex));
        params.put("folderId", folderId);
        SpotStrategy strategyInitService = ApiServiceGenerator.createService(SpotStrategy.class);
        subscription = strategyInitService
                .getArbitrageStrategy(params)
                .map(new NetworkResultFunc1<PromptGoodsModel>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<PromptGoodsModel>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PromptGoodsModel>() {
                    @Override
                    public void onCompleted() {
                        view.setCustomFontTextViewDeclareVisible();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiCodeErrorException && ((ApiCodeErrorException) e).getError() !=null) {
                            if (((ApiCodeErrorException) e).getError().getCode().equals(ErrorCode.NET_ERROR.getCode())) {
                                if(view != null)
                                    view.showErrorDialog(view.getViewContext().getString(R.string.net_work_exception));
                            }
                        } else {
                            if(view != null)
                                view.showErrorDialog(view.getViewContext().getString(R.string.net_error));
                        }
                        if (pageIndex == 0) {
                            if(view != null){
                                view.dismissLoadProgress();
                                view.addDataNormal(null);
                            }
                        } else {
                            if(view != null)
                                view.addDataLoadMore(null);
                        }
                    }

                    @Override
                    public void onNext(PromptGoodsModel strategyInitModel) {
                        if (pageIndex == 0) {
                            if(view != null){
                                view.dismissLoadProgress();
                                view.addDataNormal(strategyInitModel.getArticles());
                            }
                        } else {
                            if(view != null)
                                view.addDataLoadMore(strategyInitModel.getArticles());
                        }
//                        if(view != null)
//                            view.setHeaderText(strategyInitModel.getLastUpdateTime());
                    }
                });

    }

    @Override
    public void getSpotStrategyOnRefresh(String folderId) {
        Map<String, String> params = new HashMap<>(2);
        params.put("currentPage", String.valueOf(0));
        params.put("folderId", String.valueOf(folderId));
        SpotStrategy strategyInitService = ApiServiceGenerator.createService(SpotStrategy.class);
        subscription = strategyInitService
                .getArbitrageStrategy(params)
                .map(new NetworkResultFunc1<PromptGoodsModel>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<PromptGoodsModel>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PromptGoodsModel>() {
                    @Override
                    public void onCompleted() {
                        view.setCustomFontTextViewDeclareVisible();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("netOnError",e.toString());
                        if (e instanceof ApiCodeErrorException && ((ApiCodeErrorException) e).getError() !=null) {
                            if (((ApiCodeErrorException) e).getError().getCode().equals(ErrorCode.NET_ERROR.getCode())) {
                                if(view != null)
                                    view.showErrorDialog(view.getViewContext().getString(R.string.net_work_exception));
                            }
                        } else {
                            if(view != null)
                                view.showErrorDialog(view.getViewContext().getString(R.string.net_error));
                        }
                        if(view != null)
                            view.addDataWithOnRefresh(null);
                    }

                    @Override
                    public void onNext(PromptGoodsModel strategyInitModel) {
                        if(view != null){
                            view.addDataWithOnRefresh(strategyInitModel.getArticles());
//                            view.setHeaderText(strategyInitModel.getLastUpdateTime());
                        }
                    }
                });

    }
}
