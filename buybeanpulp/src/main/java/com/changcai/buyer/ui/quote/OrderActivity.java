package com.changcai.buyer.ui.quote;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.OrderService;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.JsonFormat;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.EmptyView;
import com.changcai.buyer.view.MyProgressDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.juggist.commonlibrary.rx.RxBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 订单界面
 */
public class OrderActivity extends BaseActivity {


    private static final int PRODUCT_INFOMATION_CHANGED = 9212;
    @BindView(R.id.iv_btn_right)
    ImageView ivBtnRight;
    @BindView(R.id.tv_pick_up_location)
    CustomFontTextView tvPickUpLocation;
    @BindView(R.id.tv_pick_up_time)
    CustomFontTextView tvPickUpTime;
    @BindView(R.id.tv_price)
    CustomFontTextView tvPrice;
    @BindView(R.id.tv_ponds)
    CustomFontTextView tvPonds;
    @BindView(R.id.tv_insurance)
    CustomFontTextView tvInsurance;
    @BindView(R.id.tv_deposit_info)
    CustomFontTextView tvDepositInfo;
    @BindView(R.id.order)
    TextView order;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    @BindView(R.id.ll_order_layout)
    FrameLayout llOrderLayout;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    private PriceInfo priceInfo;
    private Subscription orderSubscription;
    private int tons;
    private static final int ORDER_PRICE_DATA_CHANGED = 9002;
    private static final int LIMIT = 9003;
    private static final int BASIS_NOT_SUPPORT = 9004;
    public static final int COMMON_ERROR_CODE = 9005;
    private static final int NOT_SUPPORT_BUY_SELF_CODE = 9006;
    private static final int NET_EXCEPTION = 9007;
    private String buyerDeposit;
    private String sellerDeposit;
    private String insuranceValue;
    private Observable<Boolean> clickObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            priceInfo = (PriceInfo) bundle.getSerializable("detail");
            tons = bundle.getInt("ton");
            buyerDeposit = bundle.getString("buyerDeposit");
            sellerDeposit = bundle.getString("sellerDeposit");
            insuranceValue = bundle.getString("insuranceValue");
        }
        initTitle();
        initView();

//        setViewGone();
//        showFailView();
    }


    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("确认订单");
        findViewById(R.id.view_bottom_line).setVisibility(View.VISIBLE);
        if (priceInfo == null) return;


        setPriceInfo(priceInfo);

        RxView.clicks(order).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                booking();
            }
        });
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        progress.showAnimation(true);
        order.setVisibility(View.GONE);
    }

    private void disMissProgress() {
        progress.refreshDone(true);
        progress.setVisibility(View.GONE);
        order.setVisibility(View.VISIBLE);
    }

    private void showFullProgressDialog() {
        setViewGone();
        MyProgressDialog myProgressDialog = new MyProgressDialog();
        myProgressDialog.show(getSupportFragmentManager(), "MyProgressDialog");
    }

    private void setViewGone() {
        scrollView.setVisibility(View.GONE);
        llOrderLayout.setVisibility(View.GONE);
    }

    private void setViewShow() {
        scrollView.setVisibility(View.VISIBLE);
        llOrderLayout.setVisibility(View.VISIBLE);
    }

    private void disMissFullProgressDialog() {
        MyProgressDialog progressDialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("MyProgressDialog");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void setPriceInfo(PriceInfo priceInfo) {
        if (priceInfo != null) {

            tvPickUpLocation.setText(priceInfo.getRegion().concat("  ").concat(priceInfo.getLocation()));
            tvPickUpTime.setText(priceInfo.getDeliveryStartTime().concat("至").concat(priceInfo.getDeliveryEndTime()));
            BigDecimal price = new BigDecimal(priceInfo.getPrice());
            if (priceInfo.getProductType().contentEquals(Constants.BASIS)) {
                price = new BigDecimal(Long.parseLong(priceInfo.getClosingPrice()) + Long.parseLong(priceInfo.getPrice()));
                String priceUnit;
                if (Integer.valueOf(priceInfo.getPrice()) > 0) {
                    priceUnit = priceInfo.getClosingPrice() + "+" + priceInfo.getPrice();
                } else {
                    priceUnit = priceInfo.getClosingPrice() + "-" + priceInfo.getPrice();
                }
                tvDepositInfo.setText(getString(R.string.buyer_deposit_money_base, priceUnit, tons + "", buyerDeposit, StringUtil.formatForMoney("" + insuranceValue)));
            } else {
                tvDepositInfo.setText(getString(R.string.buyer_deposit_money, price.toString(), "" + tons, buyerDeposit, StringUtil.formatForMoney(insuranceValue + "")));
            }
            tvPrice.setText(price.floatValue() + "".concat("元"));
            tvPonds.setText(String.valueOf(tons).concat("吨"));
            tvInsurance.setText(StringUtil.formatForMoney(insuranceValue + "").concat("元"));


        }
    }


    /**
     * 不满足条件 去电脑页面
     *
     * @param title
     * @param errorMessage
     */
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        ConfirmDialog.createConfirmDialogCustomButtonString(OrderActivity.this, title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (action == ORDER_PRICE_DATA_CHANGED) {
                    getProductDetail(priceInfo.getId());
                } else if (action == LIMIT) {

                } else if (action == NOT_SUPPORT_BUY_SELF_CODE) {
                    AppManager.getAppManager().finishActivity(OrderActivity.class, QuoteDetailActivity.class);
                } else {
                    gotoActivity(ComputerOperationActivity.class);
                }
            }
        }, leftButtonText, rightButtonText, false);
    }


    private void getProductDetail(String productDetailId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", productDetailId);
        if (UserDataUtil.isLogin())
            params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        showFullProgressDialog();
        VolleyUtil.getInstance().httpPost(this, Urls.GET_PRODUCT_DETAIL, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                disMissFullProgressDialog();
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    setViewShow();
                    Gson gson = new Gson();
                    priceInfo = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), PriceInfo.class);
                    setPriceInfo(priceInfo);
                } else {
                    showFailView();
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(OrderActivity.this, getString(R.string.net_error), errorCode);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                disMissFullProgressDialog();
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(OrderActivity.this, getString(R.string.net_error));

                showFailView();
            }

        }, false);
    }

    private void showFailView() {
//        final FailFragmentDialog failFragmentDialog = new FailFragmentDialog();
//        failFragmentDialog.show(getSupportFragmentManager());
        emptyView.setVisibility(View.VISIBLE);
        clickObservable = RxBus.get().register("errorRefresh", Boolean.class);
        clickObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                getProductDetail(priceInfo.getId());
            }
        });

//


    }

    /**
     * 签约
     */
    private void booking() {

        //业务员和交易员没有权限下单
//        if (UserDataUtil.isFinance() || UserDataUtil.isTransaction()) {
//            showErrorDialog(getString(R.string.limit_enough_contact), "", getString(R.string.balance_recharge_cancel), LIMIT);
//            return;
//        }
//
//        //不支持基差报价
//        if (priceInfo.getProductType().contentEquals("BASIS")) {
//            showErrorDialog(getString(R.string.mobile_limit), "", getString(R.string.label_confirm), BASIS_NOT_SUPPORT);
//            return;
//        }
        showProgress();
        final OrderService orderService = ApiServiceGenerator.createService(OrderService.class);
        Map<String, String> validParameter = new HashMap<>();
        validParameter.put("priceId", priceInfo.getId());
        validParameter.put("quantity", tons + "");
        Float f = Float.parseFloat(priceInfo.getPrice()) * 100;
        validParameter.put("price", String.valueOf(f.intValue()));
        if (priceInfo.getProductType().equalsIgnoreCase("BASIS")) {
            validParameter.put("basisCode", priceInfo.getBasisCode());
        }
        validParameter.put("buyerDepositRate", priceInfo.getBuyerDepositRate());
        validParameter.put("sellerDepositRate", priceInfo.getDepositRate());

        validParameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        validParameter.put("updateTime", priceInfo.getUpdateTime());

        RxUtil.remove(orderSubscription);
        orderSubscription = orderService.validCreateOrder(validParameter)
                .flatMap(new Func1<BaseApiModel<String>, Observable<BaseApiModel<Object>>>() {
                    @Override
                    public Observable<BaseApiModel<Object>> call(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().contentEquals("0")) {
                            Map<String, String> parameter = new HashMap<>();
                            parameter.put("priceId", priceInfo.getId());
                            parameter.put("enterId", priceInfo.getEnterId());
                            parameter.put("quantity", tons + "");
                            parameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
                            return orderService.createOrder(parameter);
                        } else {
                            return Observable.error(new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), stringBaseApiModel.getErrorDesc()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<Object>>() {
                    @Override
                    public void call(BaseApiModel<Object> stringBaseApiModel) {
                        disMissProgress();
                        if (stringBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                            JsonObject jsonObject = JsonFormat.String2Object(stringBaseApiModel.getResultObject().toString());
                            Bundle bundle = new Bundle();
                            bundle.putString("orderId", jsonObject.get("id").getAsString());
                            if (UserDataUtil.isAdmin()) {
                                gotoActivity(SignContractActivity.class, bundle);
                            } else if (UserDataUtil.isBusiness()) {
                                bundle.putString("financeContractPhone", jsonObject.get("financeContractPhone").getAsString());
                                gotoActivity(BusinessOrderActivity.class, bundle);
                            }
                        } else {
                            throw new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), stringBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        disMissProgress();
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_DATA_CHANGED") ) {
                                showErrorDialog(getString(R.string.price_data_changed), throwable.getMessage(), getString(R.string.label_confirm), ORDER_PRICE_DATA_CHANGED);
                            }else if (((ApiCodeErrorException) throwable).getState().contentEquals("PRODUCT_INFOMATION_CHANGED")){
                                showErrorDialog(throwable.getMessage(),"","刷新",PRODUCT_INFOMATION_CHANGED);
                            }
                            else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_BUYSELF_ERROR")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.balance_recharge_cancel), getString(R.string.look_other_price_info), NOT_SUPPORT_BUY_SELF_CODE);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_INVALID")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.balance_recharge_cancel), getString(R.string.look_other_price_info), NOT_SUPPORT_BUY_SELF_CODE);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("UPDATETIME_INVALID")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.label_confirm), COMMON_ERROR_CODE);
                            } else {
                                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(OrderActivity.this, getString(R.string.net_error));
                            }
                        } else {
                            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(OrderActivity.this, getString(R.string.net_error));
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        disMissProgress();
                    }
                });
        RxUtil.addSubscription(orderSubscription);
    }


    public void showErrorDialog(String title, String message, String buttonName, final int actionCode) {
        ConfirmDialog.createConfirmDialog(OrderActivity.this, message, title, buttonName, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (actionCode) {
                    case BASIS_NOT_SUPPORT:
                        gotoActivity(ComputerOperationActivity.class);
                        break;
                    case LIMIT:

                        break;
                    case PRODUCT_INFOMATION_CHANGED:
                        getProductDetail(priceInfo.getId());
                        break;

                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.remove(orderSubscription);
        RxBus.get().unregister("errorRefresh", clickObservable);
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
