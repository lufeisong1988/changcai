package com.changcai.buyer.business_logic.about_buy_beans.about_us;

import com.changcai.buyer.bean.InitModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.InitService;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/23.
 */
public class AboutBuyBeansPresenter implements AboutBuyBeansContract.Presenter {

    AboutBuyBeansContract.View view;

    protected Subscription initSubscription;
    public AboutBuyBeansPresenter(AboutBuyBeansContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(initSubscription);
    }

    @Override
    public void checkNewVersion() {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        InitService initService = ApiServiceGenerator.createService(InitService.class);
        initSubscription = initService
                .appInit(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiModel<InitModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseApiModel<InitModel> initModelBaseApiModel) {

                        if (initModelBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)){
                            InitModel initModel = initModelBaseApiModel.getResultObject();
                            if (Boolean.parseBoolean(initModel.getNeedUpdate())){

                                view.showNewUpdateVersion(initModel.getUpdateLog());
                            }else{
                                view.showCheckResultToast(false);
                            }
                        }else{
                            view.showErrorDialog(initModelBaseApiModel.getErrorCode());
                        }
                    }
                });
        RxUtil.addSubscription(initSubscription);


    }
}
