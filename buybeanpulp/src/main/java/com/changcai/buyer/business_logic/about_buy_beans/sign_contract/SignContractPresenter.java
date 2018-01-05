package com.changcai.buyer.business_logic.about_buy_beans.sign_contract;

import android.text.TextUtils;
import android.view.View;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.OrderEnum;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.GetOrderInfo;
import com.changcai.buyer.interface_api.NetThrowableFiltrateFunc;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.PreviewContractService;
import com.changcai.buyer.interface_api.SignContractService;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.okhttp.NetErrorException;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.order.bean.PreviewContract;
import com.changcai.buyer.util.JsonFormat;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractFragment.ORDER_INFO_CHANGED;
import static com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractFragment.ORDER_STATUS_ERROR;

/**
 * Created by liuxingwei on 2017/1/9.
 */

public class SignContractPresenter implements SignContract.Presenter {

    SignContract.View view;

    private String TAG = SignContractPresenter.class.getSimpleName();
    private String orderId;
    private String orderPrice;

    private Subscription orderSubscription;

    private Subscription signSubscription;

    private Subscription previewSignContractSubscription;

    private String getOrderId() {
        return orderId;
    }

    private OrderEnum orderEnum;

    public SignContractPresenter(SignContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    public OrderEnum getOrderEnum() {
        return orderEnum;
    }

    public void setOrderEnum(OrderEnum orderEnum) {
        this.orderEnum = orderEnum;
    }

    @Override

    public void start() {
        getOrderInfo();
    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(orderSubscription);
        RxUtil.remove(signSubscription);
        RxUtil.remove(previewSignContractSubscription);
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    @Override
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public void getOrderInfo() {
        GetOrderInfo getOrderInfo = ApiServiceGenerator.createService(GetOrderInfo.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", getOrderId());
        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RxUtil.remove(orderSubscription);
        orderSubscription = getOrderInfo
                .getOrderInfo(parameters)
                .map(new NetworkResultFunc1<OrderInfo>())
                .onErrorResumeNext(new NetThrowableFiltrateFunc<OrderInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OrderInfo>() {
                    @Override
                    public void call(OrderInfo orderInfo) {
                        if (view.isActive())
                            view.showContractInfo(orderInfo);

                        for (OrderEnum orderEnum : OrderEnum.values()) {
                            if (orderEnum.getrderTypeCode().equalsIgnoreCase(orderInfo.getOrderModel())) {
                                setOrderEnum(orderEnum);
                            }
                        }
                        previewContract(orderInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(view.getActivityContext(), view.getActivityContext().getString(throwable instanceof NetErrorException ? R.string.network_unavailable : R.string.net_error));
                    }
                });
    }

    /**
     * 参数名称 类型 是否为空 说明
     * orderId	string	否	订单id
     * type string 否 固定：ORD_ORDER
     * signerType string 否 买家：A卖家：B
     */
    @Override
    public void signContract() {
        SignContractService signContractService = ApiServiceGenerator.createService(SignContractService.class);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("orderId", getOrderId());
        parameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        parameter.put("type", "ORD_ORDER");
        parameter.put("signerType", "A");
        parameter.put("myPrice", getOrderPrice());
        RxUtil.remove(signSubscription);
        signSubscription = signContractService
                .signContract(parameter)
                .map(new NetworkResultFunc1<>())
                .onErrorResumeNext(new NetThrowableFiltrateFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object orderInfoObject) {
                        view.dismissProgressDialog();
                        if (getOrderEnum() == null) {
                            view.gotoFullPay();
                        } else if (OrderEnum.CASH_ONHAND_ORDER.getrderTypeCode().equalsIgnoreCase(getOrderEnum().getrderTypeCode())) {
                            if (orderInfoObject != null && !TextUtils.isEmpty(orderInfoObject.toString())) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) orderInfoObject;
                                if (linkedTreeMap.containsKey("deliverys")) {
                                    ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) linkedTreeMap.get("deliverys");
                                    if (list.get(0).containsKey("id")) {
                                        view.gotoGoodsPay(list.get(0).get("id").toString());
                                    }
                                }
                            }
                        } else if (OrderEnum.RESERVE_DEPOSIT_ORDER.getrderTypeCode().equalsIgnoreCase(getOrderEnum().getrderTypeCode())) {
                            view.gotoDownPay();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.dismissProgressDialog();
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICECHANGE_ERROR")) {
                                view.showSuccessDialog(throwable.getMessage());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_STATUS_ERROR")) {
                                view.showSuccessDialog(throwable.getMessage(), ORDER_STATUS_ERROR);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ASSISTANT_SYSTEM_ERROE")) {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.net_error));
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_SYSTEM_ERROE")) {
                                view.showErrorDialog(throwable.getMessage());
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_INFO_CHANGED")) {
                                view.showErrorDialog(throwable.getMessage());
                            } else {
                                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(view.getActivityContext(), throwable.getMessage());
                            }
                        } else {

                            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(view.getActivityContext(), view.getActivityContext().getString(R.string.network_unavailable));
                        }
                    }
                });
    }


    @Override
    public void previewContract(OrderInfo orderInfo) {
        PreviewContractService previewContractService = ApiServiceGenerator.createService(PreviewContractService.class);
        UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        if (userInfo == null) return;
        Map<String, String> map = new HashMap<>();
        map.put("orderId", getOrderId());
        map.put("signerType", "A");
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("buyerEnterName", userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? userInfo.getName() : userInfo.getEnterName());
        map.put("buyerContractPhone", userInfo.getMobile());
        map.put("sellerEnterName", orderInfo.getProduct().getEnterName());
        map.put("sellerContractPhone", orderInfo.getProduct().getContactPhone());
        RxUtil.remove(previewSignContractSubscription);
        previewSignContractSubscription = previewContractService
                .signContract(map)
                .map(new NetworkResultFunc1<PreviewContract>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<PreviewContract>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PreviewContract>() {
                    @Override
                    public void call(PreviewContract previewContract) {
                        view.setSignEnAble(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                        view.setSignEnAble(false);
                    }
                });
    }
}
