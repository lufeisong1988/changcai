package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.bean.PlatformAmount;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.EbaoFullPayService;
import com.changcai.buyer.interface_api.GetAccountBalance;
import com.changcai.buyer.interface_api.GetDeliveryInfo;
import com.changcai.buyer.interface_api.GetOrderInfo;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by liuxingwei on 2017/3/31.
 */

public class PayPresenter implements PayContract.Presenter {

    private String orderId;
    private String deliveryId;
    private Subscription orderSubscription;
    private Subscription accountSubscription;
    private Subscription fullPaySubscription;


    private PayContract.View view;
    private EbaoFullPayService ebaoFullPayService;

    public PayPresenter(PayContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getOrderInfo() {
        RxUtil.remove(orderSubscription);
        GetOrderInfo getOrderInfo = ApiServiceGenerator.createService(GetOrderInfo.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", getOrderId());
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(orderSubscription);
        orderSubscription = getOrderInfo
                .getOrderInfo(parameters)
                .map(new NetworkResultFunc1<OrderInfo>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<OrderInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OrderInfo>() {
                    @Override
                    public void call(OrderInfo orderInfo) {
                        view.showFullPayInfo(orderInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });
    }

    @Override
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    @Override
    public String getDeliveryId() {
        return deliveryId;
    }

    /**
     * 定金支付
     * @param password
     * @param payType      payType	String	是
     *                     空（默认支付方式）：平台账户余额 》订单金额时，从平台账户扣钱支付
     *                     平台账户余额<订单金额时，返回失败
     *                     direct（快捷支付方式）：平台账户余额 》订单金额时，从平台账户扣钱支付
     *                     平台账户余额+易宝账户》订单金额时，从易宝账户充值到平台后直接支付
     */
    @Override
    public void frontMoneyPay(String password , final String payType) {
        if (ebaoFullPayService == null) {
            ebaoFullPayService = ApiServiceGenerator.createService(EbaoFullPayService.class);
        }
        final Map<String, String> parameter = new HashMap<>();
        parameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        parameter.put("password", password);
        RxUtil.remove(fullPaySubscription);
        fullPaySubscription = ebaoFullPayService
                .validatePayPass(parameter)
                .flatMap(new Func1<BaseApiModel<String>, Observable<BaseApiModel<String>>>() {
                    @Override
                    public Observable<BaseApiModel<String>> call(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().contentEquals("0")) {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
                            parameters.put("orderId", getOrderId());
                            if (!TextUtils.isEmpty(payType)) {
                                parameters.put("payType", payType);
                            }
                            return ebaoFullPayService.payFrontMoney(parameters);
                        } else {
                            return Observable.error(new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), TextUtils.isEmpty(stringBaseApiModel.getErrorDesc()) ? view.getMContext().getString(R.string.input_incrected_password) : stringBaseApiModel.getErrorDesc()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        view.dismissProgressDialog();
                        stringBaseApiModel.setResultObject("frontMoneyPay");
                        view.gotoResult(stringBaseApiModel);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.dismissProgressDialog();
                        //业务逻辑异常
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_PASS_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.pay_password_error), "", view.getMContext().getString(R.string.forget_password), view.getMContext().getString(R.string.retry), Constants.PAY_PASSWARD_UNCRRECT);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_EBAO_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.balance_not_enough), view.getMContext().getString(R.string.balance_not_enough_go_recharge), view.getMContext().getString(R.string.balance_recharge_cancel), view.getMContext().getString(R.string.go_to_recharge2), Constants.PAY_EBAO_INSUFFICIENT_ERROR);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_WAIT_CONFIRM_ERROR")) {
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("FULL_PAY_WAIT_CONFIRM_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel.setResultObject("frontMoneyPay");
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_CONNECT_ERROR")) {
                                view.showErrorDialog(throwable.getMessage());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_OK_PAY_NG_ERROR")) {
                                //入金成功 支付失败
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("RECHARGE_OK_PAY_NG_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel.setResultObject("frontMoneyPay");
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("TOTAL_BALANCE_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.balance_not_enough), throwable.getMessage(), view.getMContext().getString(R.string.balance_recharge_cancel), view.getMContext().getString(R.string.go_to_recharge), Constants.TOTAL_BALANCE_INSUFFICIENT_ERROR);
                            } else {

                                // 包含该错误     (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_NG_ERROR"))


                                //其他情况统一视作错误 ------支付失败
                                BaseApiModel<String> stringBaseApiModel2 = new BaseApiModel<>();
                                stringBaseApiModel2.setErrorCode("FULL_PAY_ERROR");
                                stringBaseApiModel2.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel2.setResultObject("frontMoneyPay");
                                view.gotoResult(stringBaseApiModel2);
                            }
                        } else {
                            view.showErrorDialog(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void getDelivery() {
        GetDeliveryInfo deliveryInfo = ApiServiceGenerator.createService(GetDeliveryInfo.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("deliveryId", getDeliveryId());
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(orderSubscription);
        orderSubscription = deliveryInfo
                .getDelivery(parameters)
                .map(new NetworkResultFunc1<DeliveryInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DeliveryInfo>() {
                    @Override
                    public void call(DeliveryInfo deliveryInfo) {
                        view.showDeliveryInfo(deliveryInfo);
                    }


                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getPingAnBalance() {
        GetAccountBalance getAccountBalance = ApiServiceGenerator.createService(GetAccountBalance.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(accountSubscription);
        accountSubscription = getAccountBalance
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

    @Override
    public void payOrdDelivery(String password, final String payType) {
        if (ebaoFullPayService == null) {
            ebaoFullPayService = ApiServiceGenerator.createService(EbaoFullPayService.class);
        }
        final Map<String, String> parameter = new HashMap<>();
        parameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        parameter.put("password", password);
        RxUtil.remove(fullPaySubscription);
        fullPaySubscription = ebaoFullPayService
                .validatePayPass(parameter)
                .flatMap(new Func1<BaseApiModel<String>, Observable<BaseApiModel<String>>>() {
                    @Override
                    public Observable<BaseApiModel<String>> call(BaseApiModel<String> stringBaseApiModel) {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
                        parameters.put("deliveryId", getDeliveryId());
                        if (!TextUtils.isEmpty(payType)) {
                            parameters.put("payType", payType);
                        }
                        return ebaoFullPayService.payOrdDelivery(parameters);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        view.dismissProgressDialog();
                        stringBaseApiModel.setResultObject("delivery");
                        view.gotoResult(stringBaseApiModel);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view == null )
                            return;
                        view.dismissProgressDialog();
                        //业务逻辑异常
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_PASS_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.pay_password_error), "", view.getMContext().getString(R.string.forget_password), view.getMContext().getString(R.string.retry), Constants.PAY_PASSWARD_UNCRRECT);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("PAY_EBAO_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.balance_not_enough), view.getMContext().getString(R.string.balance_not_enough_go_recharge), view.getMContext().getString(R.string.balance_recharge_cancel), view.getMContext().getString(R.string.go_to_recharge2), Constants.PAY_EBAO_INSUFFICIENT_ERROR);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_WAIT_CONFIRM_ERROR")) {
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("FULL_PAY_WAIT_CONFIRM_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel.setResultObject("delivery");
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("FULL_PAY_CONNECT_ERROR")) {
                                view.showErrorDialog(throwable.getMessage());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_OK_PAY_NG_ERROR")) {
                                //入金成功 支付失败
                                BaseApiModel<String> stringBaseApiModel = new BaseApiModel<>();
                                stringBaseApiModel.setErrorCode("RECHARGE_OK_PAY_NG_ERROR");
                                stringBaseApiModel.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel.setResultObject("delivery");
                                view.gotoResult(stringBaseApiModel);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("TOTAL_BALANCE_INSUFFICIENT_ERROR")) {
                                view.showErrorDialog(view.getMContext().getString(R.string.balance_not_enough), throwable.getMessage(), view.getMContext().getString(R.string.balance_recharge_cancel), view.getMContext().getString(R.string.go_to_recharge), Constants.TOTAL_BALANCE_INSUFFICIENT_ERROR);
                            } else {

                                // 包含该错误     (((ApiCodeErrorException) throwable).getState().contentEquals("RECHARGE_NG_ERROR"))


                                //其他情况统一视作错误 ------支付失败
                                BaseApiModel<String> stringBaseApiModel2 = new BaseApiModel<>();
                                stringBaseApiModel2.setErrorCode("FULL_PAY_ERROR");
                                stringBaseApiModel2.setErrorDesc(throwable.getMessage());
                                stringBaseApiModel2.setResultObject("delivery");
                                view.gotoResult(stringBaseApiModel2);
                            }
                        } else {
                            view.showErrorDialog(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void start() {
        //获取提货单信息
        if (!TextUtils.isEmpty(getDeliveryId())) {
            getDelivery();
        } else if (!TextUtils.isEmpty(getOrderId())) {
            getOrderInfo();
        }
        getPingAnBalance();
    }

    @Override
    public void detach() {
        RxUtil.remove(accountSubscription);
        RxUtil.remove(orderSubscription);
        RxUtil.remove(fullPaySubscription);
        view = null;
    }
}
