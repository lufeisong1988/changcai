package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PlatformAmount;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.EbaoFullPayService;
import com.changcai.buyer.interface_api.GetAccountBalance;
import com.changcai.buyer.interface_api.GetOrderInfo;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.okhttp.NetErrorException;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by liuxingwei on 2017/1/11.
 */

public class FullPayPresenter implements FullPayContract.Presenter {

    private Subscription orderSubscription;

    private Subscription fullPaySubscription;

    private Subscription accountingSubscription;
    FullPayContract.View view;

    private String orderId;

    private boolean isSupport;

    public void setSupport(boolean support) {
        isSupport = support;
    }

    public FullPayPresenter(FullPayContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        getOrderInfo();
        accountingProperty();
    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(orderSubscription);
        RxUtil.remove(fullPaySubscription);
    }

    @Override
    public void getOrderInfo() {
        RxUtil.remove(orderSubscription);
        GetOrderInfo getOrderInfo = ApiServiceGenerator.createService(GetOrderInfo.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", getOrderId());
        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RxUtil.remove(orderSubscription);
        orderSubscription = getOrderInfo
                .getOrderInfo(parameters)
                .map(new NetworkResultFunc1<OrderInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OrderInfo>() {
                    @Override
                    public void call(OrderInfo orderInfo) {
                        view.showFullPayInfo(orderInfo);

                        //支持全额付
//                        if (orderInfo.getOrderModel().equalsIgnoreCase("ALL_PAY_ORDER")){
//                            setSupport(true);
//                        }

                        if ("ALL_PAY_ORDER".equalsIgnoreCase(orderInfo.getOrderModel())){
                            setSupport(true);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(view.getActivityContext(),view.getActivityContext().getString(throwable instanceof NetErrorException ?R.string.network_unavailable:R.string.net_error));

                    }
                });
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    @Override
    public void ebaoFullPay(@NonNull String password, @NonNull final String deliveryTime, @Nullable final String payType) {
        RxUtil.remove(fullPaySubscription);
        final EbaoFullPayService ebaoFullPayService = ApiServiceGenerator.createService(EbaoFullPayService.class);
        final Map<String, String> parameter = new HashMap<>();
        parameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        parameter.put("password", password);

        fullPaySubscription = ebaoFullPayService
                .validatePayPass(parameter)
                .flatMap(new Func1<BaseApiModel<String>, Observable<BaseApiModel<String>>>() {
                    @Override
                    public Observable<BaseApiModel<String>> call(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().contentEquals("0")) {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
                            parameters.put("orderId", getOrderId());
                            parameters.put("deliveryTime", deliveryTime);
                            if (!TextUtils.isEmpty(payType)) {
                                parameters.put("payType", payType);
                            }
                            return ebaoFullPayService.ebaoFullPay(parameters);
                        } else {
                            return Observable.error(new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), TextUtils.isEmpty(stringBaseApiModel.getErrorDesc()) ? view.getActivityContext().getString(R.string.input_incrected_password) : stringBaseApiModel.getErrorDesc()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        view.dismissProgressDialog();
                        view.gotoResult(stringBaseApiModel);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.dismissProgressDialog();
                        //业务逻辑异常
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_PASS_ERROR")) {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.pay_password_error), "", view.getActivityContext().getString(R.string.forget_password), view.getActivityContext().getString(R.string.retry), Constants.PAY_PASSWARD_UNCRRECT);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_EBAO_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.balance_not_enough), view.getActivityContext().getString(R.string.balance_not_enough_go_recharge), view.getActivityContext().getString(R.string.balance_recharge_cancel), view.getActivityContext().getString(R.string.go_to_recharge2), view.getPayEbaoInsufficientError());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_WAIT_CONFIRM_ERROR")) {
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("FULL_PAY_WAIT_CONFIRM_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_CONNECT_ERROR")) {
                                view.showErrorDialog(throwable.getMessage());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_OK_PAY_NG_ERROR")) {
                                //入金成功 支付失败
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("RECHARGE_OK_PAY_NG_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("TOTAL_BALANCE_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.balance_not_enough), throwable.getMessage(), view.getActivityContext().getString(R.string.balance_recharge_cancel), view.getActivityContext().getString(R.string.go_to_recharge), view.getRechargeOkPayFailError());
                            } else {

                                // 包含该错误     (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_NG_ERROR"))


                                //其他情况统一视作错误 ------支付失败
                                BaseApiModel<String> stringBaseApiModel2 = new BaseApiModel<>();
                                stringBaseApiModel2.setErrorCode("FULL_PAY_ERROR");
                                stringBaseApiModel2.setErrorDesc(throwable.getMessage());
                                view.gotoResult(stringBaseApiModel2);
                            }
                        } else {
                            view.showErrorDialog(throwable.getMessage());
                            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(view.getActivityContext(),view.getActivityContext().getString(R.string.net_error));
                        }
                    }
                });
    }

    @Override
    public boolean isSupport() {
        return isSupport;
    }

    @Override
    public void accountingProperty() {
        GetAccountBalance getAccountBalance = ApiServiceGenerator.createService(GetAccountBalance.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(accountingSubscription);
        accountingSubscription = getAccountBalance
                .getUserPlatformTotalAmount(parameters)
                .map(new NetworkResultFunc1<List<PlatformAmount>>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<List<PlatformAmount>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PlatformAmount>>() {
                    @Override
                    public void call(List<PlatformAmount> platformAmountList) {
                        for (PlatformAmount platformAmount : platformAmountList) {
                            if (platformAmount.getAccountType().equalsIgnoreCase("RECHARGE")) {
                                view.showBalance(platformAmount.getAmountYuan());
                                break;
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });

    }
}
