package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.StraddleModel;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.ErrorCode;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.util.RxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class StraddlePresenter implements StraddleContract.Presenter {

    StraddleContract.View view;

    private Subscription subscription;

    public StraddlePresenter(StraddleContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getStraddleData(final int pageIndex) {
        if (pageIndex==0){
            view.showProgress();
        }
        StraddleService straddleService = ApiServiceGenerator.createService(StraddleService.class);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("currentPage", String.valueOf(pageIndex));
        subscription = straddleService
                .getArbitrageStrategy(paramsMap)
                .map(new NetworkResultFunc1<List<StraddleModel>>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<List<StraddleModel>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<StraddleModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiCodeErrorException && ((ApiCodeErrorException) e).getError() !=null) {
                            if (((ApiCodeErrorException) e).getError().getCode().equals(ErrorCode.NET_ERROR.getCode())) {
                                view.showErrorDialog(view.getViewContext().getString(R.string.net_work_exception));
                            }
                        } else {
                            view.showErrorDialog(view.getViewContext().getString(R.string.net_error));
                        }
                        if (pageIndex == 0) {
                            view.dismissProgress();
                            view.addDataList(null);
                        } else {
                            view.addDataListWithLoadMore(null);
                        }
                    }

                    @Override
                    public void onNext(List<StraddleModel> straddleModels) {
                        if (pageIndex == 0) {
                            view.dismissProgress();
                            view.addDataList(straddleModels);
                        } else {
                            view.addDataListWithLoadMore(straddleModels);
                        }
                    }
                });
    }

    @Override
    public void getStraddleDataOnRefresh() {
        StraddleService straddleService = ApiServiceGenerator.createService(StraddleService.class);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("currentPage", String.valueOf(0));
        subscription = straddleService
                .getArbitrageStrategy(paramsMap)
                .map(new NetworkResultFunc1<List<StraddleModel>>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<List<StraddleModel>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<StraddleModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) e).getError().getCode().equals(ErrorCode.NET_ERROR.getCode())) {
                                view.showErrorDialog(view.getViewContext().getString(R.string.net_work_exception));
                            }
                        } else {
                            view.showErrorDialog(view.getViewContext().getString(R.string.net_error));
                        }
                        view.addDataListWithOnRefresh(null);
                    }

                    @Override
                    public void onNext(List<StraddleModel> straddleModels) {
                        view.addDataListWithOnRefresh(straddleModels);
                    }
                });
    }

    @Override
    public void start() {
    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(subscription);
    }
}
