package com.changcai.buyer.business_logic.about_buy_beans.recharge;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.EbaoRechargeService;
import com.changcai.buyer.interface_api.GetAccountBalance;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.changcai.buyer.business_logic.about_buy_beans.recharge.RechargeFragment.ACTION2;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public class RechargePresenter implements RechargeContract.Presenter {

    RechargeContract.View view;

    private String TAG = RechargePresenter.class.getSimpleName();
    private Subscription accountSubscription;

    public RechargePresenter(RechargeContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        getAccountBalance();
    }

    @Override
    public void detach() {
        RxUtil.remove(accountSubscription);
    }

    @Override
    public void getAccountBalance() {
        GetAccountBalance getAccountBalance = ApiServiceGenerator.createService(GetAccountBalance.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(accountSubscription);
        accountSubscription = getAccountBalance
                .getAccountInfo(parameters)
                .map(new NetworkResultFunc1<EbaoBalanceInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EbaoBalanceInfo>() {
                    @Override
                    public void call(EbaoBalanceInfo ebaoBalanceInfo) {
                        if (view.isActive())
                            view.setEbaoBalance(ebaoBalanceInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view.isActive()) {
                            view.showErrorDialog(view.getActivityContext().getString(R.string.get_balance_failed));
                        }
                    }
                });

    }


    @Override
    public void rechargePay(String password, final String tranAmount) {
        view.showProgressDialog();
        final EbaoRechargeService ebaoRechargeService = ApiServiceGenerator.createService(EbaoRechargeService.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", UserDataUtil.getTokenId());
        parameters.put("password", password);
        ebaoRechargeService
                .validatePayPass(parameters)
                .flatMap(new Func1<BaseApiModel<String>, Observable<BaseApiModel<String>>>() {
                    @Override
                    public Observable<BaseApiModel<String>> call(BaseApiModel<String> stringBaseApiModel) {

                        if (stringBaseApiModel.getErrorCode().contentEquals(Constants.REQUEST_SUCCESS_S)) {
                            Map<String, String> map = new HashMap<>();
                            map.put("tokenId", UserDataUtil.getTokenId());
                            map.put("tranAmount", tranAmount);
                            return ebaoRechargeService.recharge(map);
                        } else {
                            return Observable.error(new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), stringBaseApiModel.getErrorDesc()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        view.dismissProgressDialog();
                        if (stringBaseApiModel.getErrorCode().contentEquals("-1")) {
                            view.showErrorDialog(view.getActivityContext().getString(R.string.recharge_failed));
                        } else {
                            view.goToResult(stringBaseApiModel);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.d(TAG, throwable.getMessage());
                        MobclickAgent.reportError(view.getActivityContext(), throwable);
                        if (view.isActive()) {
                            view.dismissProgressDialog();
                            if (throwable instanceof ApiCodeErrorException) {
                                if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_PASS_ERROR")) {
                                    view.showErrorDialog(
                                            view.getActivityContext().getString(R.string.pay_password_error)
                                            , ""
                                            , view.getActivityContext().getString(R.string.forget_password)
                                            , view.getActivityContext().getString(R.string.retry)
                                            , ACTION2);
                                } else {
                                    view.showErrorDialog(((ApiCodeErrorException) throwable).getMsg());
                                }
                            } else {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.recharge_failed));
                            }
                        }
                    }
                });

    }
}
