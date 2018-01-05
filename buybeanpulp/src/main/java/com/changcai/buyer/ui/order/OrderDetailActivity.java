package com.changcai.buyer.ui.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.business_logic.about_buy_beans.direct_pay.DirectPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_record.PayRecordListActivity;
import com.changcai.buyer.business_logic.about_buy_beans.point_price.PointPriceListActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.DirectPayService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.order.bean.Buttons;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.order.bean.Payment;
import com.changcai.buyer.ui.order.bean.PricingInfo;
import com.changcai.buyer.ui.order.bean.Trader;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.MoneyUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.countdowntextview.CountDownTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAY = 9527;
    private static final int LIMIT = 9528;
    @BindView(R.id.iv_order_status)
    ImageView ivOrderStatus;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.time)
    CountDownTextView time;
    @BindView(R.id.iv_icon_order_model)
    ImageView ivIconOrderModel;
    @BindView(R.id.tv_companyName)
    TextView tvCompanyName;
    @BindView(R.id.tv_price_info)
    TextView tvPriceInfo;
    @BindView(R.id.tv_deliveryTime)
    TextView tvDeliveryTime;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_seller_deposit)
    TextView tvSellerDeposit;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_buyer_deposit)
    TextView tvBuyerDeposit;
    @BindView(R.id.tv_insurance)
    TextView tvInsurance;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_direct_pay)
    TextView tvDirectPay;
    @BindView(R.id.ll_pricing_all)
    LinearLayout llPricingAll;
    @BindView(R.id.ll_delivery_all)
    LinearLayout llDeliveryAll;
    @BindView(R.id.ll_payment_all)
    LinearLayout llPaymentAll;

    @BindView(R.id.tv_status_btn)
    TextView tvStatusBtn;
    @BindView(R.id.tv_phone_icon)
    ImageView tvPhoneIcon;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_recently_point_price)
    TextView tvRecentlyPointPrice;
    @BindView(R.id.tv_pick_up_recently)
    TextView tvPickUpRecently;
    @BindView(R.id.ll_order_info)
    LinearLayout llOrderInfo;
    @BindView(R.id.ll_pay_way)
    RelativeLayout llPayWay;
    @BindView(R.id.ll_order_action)
    LinearLayout llOrderAction;
    @BindView(R.id.iv_service_phone)
    ImageView ivServicePhone;
    @BindView(R.id.ll_seller_deposit)
    LinearLayout llSellerDeposit;
    @BindView(R.id.ll_buyer_deposit)
    LinearLayout llBuyerDeposit;
    @BindView(R.id.tv_pay_way_title)
    TextView tvPayWayTitle;
    @BindView(R.id.viewDeliveryAllTopLine)
    View viewDeliveryAllTopLine;
    @BindView(R.id.viewDeliveryAllBottomLine)
    View viewDeliveryAllBottomLine;
    @BindView(R.id.viewPayTopLine)
    View viewPayTopLine;
    @BindView(R.id.viewPriceBottomLine)
    View viewPriceBottomLine;

    //订单信息

    private OrderInfo orderInfo;
    private String priceId;

    private Subscription directPayToSellerSubscription;
    private Buttons buttons;

    private Observable directPayObservable;
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台

    private String saveOrderId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("orderInfo")) {
                orderInfo = (OrderInfo) bundle.getSerializable("orderInfo");
            }
            if (bundle.containsKey("orderId")) {
                priceId = bundle.getString("orderId");
            }
        }
        initTitle();
        initView();
        directPayObservable = RxBus.get().register("OrderDetailPay", Boolean.class);
        directPayObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean.booleanValue()) {
                            getOrderInfo(orderInfo.getId());
                        }
                    }
                });

        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (saveOrderId == null) {
                    saveOrderId = savedInstanceState.getString("orderId");
                    if (saveOrderId == null) {
                        OrderDetailActivity.this.finish();
                    } else {
                        time.cancel();
                        getOrderInfo(saveOrderId);
                    }
                } else {
                    time.cancel();
                    getOrderInfo(saveOrderId);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderInfo != null) {
            getOrderInfo(orderInfo.getId());
        } else {
            if (!TextUtils.isEmpty(priceId))
                getOrderInfo(priceId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("OrderDetailPay", directPayObservable);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
        time.cancel();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        saveOrderId = savedInstanceState.getString("orderId");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (saveOrderId != null) {
            outState.putString("orderId", saveOrderId);
        }
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("订单详情");
        titleView.setBackgroundColor(getResources().getColor(R.color.white));

        btnBack.setImageResource(R.drawable.icon_nav_back);
        tvTitle.setTextColor(getResources().getColor(R.color.black));


        tvPayWay = (TextView) findViewById(R.id.tv_pay_way);
        tvDirectPay = (TextView) findViewById(R.id.tv_direct_pay);
        ivServicePhone.setVisibility(View.VISIBLE);
        ivServicePhone.setImageResource(R.drawable.icon_hotline_blue);
    }


    /**
     * 设置订单状态
     *
     * @param orderInfo
     */
    @SuppressWarnings("deprecation")
    private void setIvOrderStatusWithIcon(OrderInfo orderInfo) {
        tvOrderStatus.setText(orderInfo.getOrderViewStatus());
        //取消或者违约
        if (orderInfo.getOrderStatus().equalsIgnoreCase("CANCEL") || orderInfo.getOrderStatus().equalsIgnoreCase("CANCEL_BUY_VIOLATE") || orderInfo.getOrderStatus().equalsIgnoreCase("CANCEL_SELL_VIOLATE")) {
            ivOrderStatus.setImageLevel(0);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.global_text_gray));
        } else if (orderInfo.getOrderStatus().equalsIgnoreCase("COMPLATE")) {//完成
            ivOrderStatus.setImageLevel(10);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.pastel_green));
        } else {
            ivOrderStatus.setImageLevel(5);
            tvOrderStatus.setTextColor(getResources().getColor(R.color.global_blue));
            if (Constants.NEW_BUY_CONFIRMING.equalsIgnoreCase(orderInfo.getOrderStatus()) ||
                    Constants.NEW_BUY_DEPOSITING.equalsIgnoreCase(orderInfo.getOrderStatus())
                    || Constants.NEW_BUY_FRONT_MONEY.equalsIgnoreCase(orderInfo.getOrderStatus())
                    || Constants.OPEN_SELL_UNDER.equalsIgnoreCase(orderInfo.getOrderStatus())) {
                long currentTime = System.currentTimeMillis();
                try {
                    long diff = SystemClock.elapsedRealtime() + (Long.parseLong(orderInfo.getUpdateTime()) * 1000) - currentTime;

                    if (diff > 0) {
                        time.setTimeInFuture(diff);
                        time.setStartFix("请在");
                        time.setEndFix("内完成操作");
                        time.setAutoDisplayText(true);
                        time.setTimeFormat(CountDownTextView.TIME_SHOW_H_M_S_CHINA);
                        time.start();
                        time.addCountDownCallback(new CountDownTextView.CountDownCallback() {
                            @Override
                            public void onTick(CountDownTextView countDownTextView, long millisUntilFinished) {
                                LogUtil.d(OrderDetailActivity.class.getName(), String.valueOf(millisUntilFinished));
                            }

                            @Override
                            public void onFinish(CountDownTextView countDownTextView) {
                                LogUtil.d(OrderDetailActivity.class.getName(),
                                        "finish- time s up");

                                time.setVisibility(View.GONE);
                            }
                        });

                        time.setVisibility(View.VISIBLE);
                    } else {
                        time.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            } else {
                time.setVisibility(View.GONE);
            }
        }
    }


    private void setOrderModelIconWithCompanyName(OrderInfo orderInfo) {

        buttons = getButtons();

        if (buttons != null) {
            tvStatusBtn.setText(buttons.getMessage());
            tvStatusBtn.setOnClickListener(this);
            tvStatusBtn.setVisibility(View.VISIBLE);
        } else {
            tvStatusBtn.setVisibility(View.GONE);
        }
        //派单
        if (!TextUtils.isEmpty(orderInfo.getOrderModel()) && (orderInfo.getOrderModel().equalsIgnoreCase("CASH_ONHAND_ORDER") || orderInfo.getOrderModel().equalsIgnoreCase("RESERVE_DEPOSIT_ORDER"))) {

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dim81), getResources().getDimensionPixelOffset(R.dimen.dim57));
            ivIconOrderModel.setLayoutParams(layoutParams);
            ivIconOrderModel.setImageResource(R.drawable.icon_seller_initiated);
            llPayWay.setVisibility(View.GONE);

            llBuyerDeposit.setVisibility(View.GONE);
            llSellerDeposit.setVisibility(View.GONE);
            tvPayWayTitle.setText("定金:");
        } else {
            //订单
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dim29), getResources().getDimensionPixelOffset(R.dimen.dim25));
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            layoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.dim20), 0, 0, 0);
            ivIconOrderModel.setLayoutParams(layoutParams);
            ivIconOrderModel.setImageResource(R.drawable.icon_seller_order_list);

            if (orderInfo.getPayModel().equalsIgnoreCase("GUARANTEE_PAY")) {
                tvPayWay.setText(R.string.guarantee);
            } else if (orderInfo.getPayModel().equalsIgnoreCase("FAST_PAY")) {
                if (orderInfo.getBuyerFastPayStatus().equalsIgnoreCase("true")) {
                    tvPayWay.setText(getString(R.string.fast_pay).concat(getString(R.string.agree_fast_pay)));
                } else {
                    tvPayWay.setText(R.string.fast_pay);
                }
            }
            if (isFastPay()) {
                tvDirectPay.setVisibility(View.VISIBLE);
                tvDirectPay.setOnClickListener(this);
            } else {
                tvDirectPay.setVisibility(View.GONE);
            }

        }
        if (orderInfo.getProduct() != null) {
            tvCompanyName.setText(orderInfo.getProduct().getEnterName() == null ? "" : orderInfo.getProduct().getEnterName());
        }


    }

    /**
     * 产品信息
     *
     * @param priceInfo
     */
    private void setProductInformation(@NonNull PriceInfo priceInfo) {
        tvPriceInfo.setText(priceInfo.getFactoryBrand().concat(" / ").concat(priceInfo.getEggSpec()).concat(" / ").concat(priceInfo.getPackType()));
        tvDeliveryTime.setText(priceInfo.getDeliveryStartTime().concat("至").concat(priceInfo.getDeliveryEndTime()));
        tvPlace.setText(priceInfo.getRegion().concat(priceInfo.getLocation()).concat("(买方自提)"));
        if (priceInfo.getDepositRate() != null) {
            tvSellerDeposit.setText(MoneyUtil.ratio(priceInfo.getDepositRate()) + "".concat("%"));
        } else {
            tvSellerDeposit.setText(MoneyUtil.ratio("0") + "".concat("%"));
        }
        if (priceInfo.getBuyerDepositRate() != null) {
            tvBuyerDeposit.setText(MoneyUtil.ratio(priceInfo.getBuyerDepositRate()) + "".concat("%"));
        } else {
            tvBuyerDeposit.setText(MoneyUtil.ratio("0") + "".concat("%"));
        }
    }

    /**
     * 设置订单信息
     *
     * @param orderInformation
     */
    private void setOrderInformation(final OrderInfo orderInformation) {
        if (orderInformation.getPrice().contains("-")) {
            tvPrice.setText(orderInformation.getPrice().replace("¥", "").replace("-", "").replace("+", "-").concat("元"));
        } else {
            tvPrice.setText(orderInformation.getPrice().replace("¥", "").concat("元"));
        }
        tvQuantity.setText("".concat(orderInformation.getQuantity()).concat("吨"));
        if (Constants.SPOT.equalsIgnoreCase(orderInformation.getOrderType())) {
            tvTotalAmount.setText("总价：".concat(orderInformation.getOrderAmount()).concat("元"));
            tvTotalAmount.setVisibility(View.VISIBLE);
        }else if (Constants.BASIS.equalsIgnoreCase(orderInformation.getOrderType())){
            tvTotalAmount.getLayoutParams().height = 20;
            tvTotalAmount.setVisibility(View.INVISIBLE);
        }
        tvInsurance.setText(orderInformation.getPayOffset().concat("元"));
        llOrderInfo.removeAllViews();
        if (orderInformation.getTrades() != null && orderInformation.getTrades().size() > 0) {
            if (orderInformation.getTrades().size() > 1) {
                addTradeDetails(orderInformation, 1);
                final View expandMoreLinearLayout = getLayoutInflater().inflate(R.layout.expand_more, null, false);
                final View retractLinearLayout = getLayoutInflater().inflate(R.layout.retract, null, false);
                llOrderInfo.addView(expandMoreLinearLayout);
                expandMoreLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llOrderInfo.removeAllViews();
                        addTradeDetails(orderInformation, orderInformation.getTrades().size());
                        llOrderInfo.addView(retractLinearLayout);
                    }
                });

                retractLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llOrderInfo.removeAllViews();
                        addTradeDetails(orderInformation, 1);
                        llOrderInfo.addView(expandMoreLinearLayout);
                    }
                });

            } else {
                addTradeDetails(orderInformation, orderInformation.getTrades().size());
            }
        } else {
            llOrderAction.setVisibility(View.VISIBLE);
            addTradeDetails(orderInformation, 0);
        }
    }

    /**
     * 动态添加交易明细
     *
     * @param orderInformation
     * @param size
     */
    private void addTradeDetails(OrderInfo orderInformation, int size) {
        TextView textView = new TextView(this);
        textView.setText("订单号：".concat(orderInformation.getOrderNo()));
        if (size == 0) {
            textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dim20), getResources().getDimensionPixelOffset(R.dimen.dim20), 0, getResources().getDimensionPixelOffset(R.dimen.dim20));
        } else {
            textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dim20), getResources().getDimensionPixelOffset(R.dimen.dim20), 0, 0);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dim24));
        llOrderInfo.addView(textView);
        for (int i = 0; i < size; i++) {
            Trader trader = orderInformation.getTrades().get(i);
            TextView textView1 = new TextView(this);
            textView1.setText(trader.getCreateTime().concat(" ").concat(trader.getTradeObjectZh().concat(trader.getAfterStatusZh())));
            textView1.setPadding(getResources().getDimensionPixelOffset(R.dimen.dim20), getResources().getDimensionPixelOffset(R.dimen.dim10), 0, orderInformation.getTrades().size() == 1 ? getResources().getDimensionPixelOffset(R.dimen.dim20) : 0);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dim24));
            llOrderInfo.addView(textView1);
        }
    }

    /**
     * 获取订单详情
     */
    private void getOrderInfo(final String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("orderId", "" + id);
        saveOrderId = id;

        VolleyUtil.getInstance().httpPost(this, Urls.GET_ORDER_INFO, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    orderInfo = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), OrderInfo.class);
                    //设置订单状态的图片
                    if(time.isRunning()){
                        time.cancel();
                    }
                    setIvOrderStatusWithIcon(orderInfo);

                    setOrderInformation(orderInfo);
                    //设置订单类型 企业名称

                    setOrderModelIconWithCompanyName(orderInfo);
                    //设置产品信息
                    setProductInformation(orderInfo.getProduct());

                    //点价信息
                    pointPriceInformation(orderInfo.getPricings());

                    //提货信息
                    pickUpInformation(orderInfo.getDeliverys());

                    //支付信息
                    paymentInformation(orderInfo.getPayments());


                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(OrderDetailActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false);
    }

    private void paymentInformation(@Nullable List<Payment> paymentList) {
        if (paymentList != null && paymentList.size() > 0) {

            if (llPricingAll.getVisibility() == View.GONE && llDeliveryAll.getVisibility() == View.GONE) {
                LinearLayout.LayoutParams llPaymentAllLayoutParams = (LinearLayout.LayoutParams) llPaymentAll.getLayoutParams();

                llPaymentAllLayoutParams.topMargin = 20;
                LinearLayout.LayoutParams viewPayTopLineParams = (LinearLayout.LayoutParams) viewPayTopLine.getLayoutParams();
                viewPayTopLineParams.leftMargin = 0;

                llPaymentAll.setLayoutParams(llPaymentAllLayoutParams);
                viewPayTopLine.setLayoutParams(viewPayTopLineParams);

            }
            llPaymentAll.setVisibility(View.VISIBLE);
            viewDeliveryAllBottomLine.setVisibility(View.GONE);
        } else {
            llPaymentAll.setVisibility(View.GONE);
            viewDeliveryAllBottomLine.setVisibility(View.VISIBLE);
        }
    }

    private void pickUpInformation(@Nullable List<DeliveryInfo> deliveryInfoList) {
        if (deliveryInfoList != null && deliveryInfoList.size() > 0) {
            if (llPricingAll.getVisibility() == View.GONE) {
                LinearLayout.LayoutParams llDeliveryAllLayoutParams = (LinearLayout.LayoutParams) llDeliveryAll.getLayoutParams();

                llDeliveryAllLayoutParams.topMargin = 20;
                LinearLayout.LayoutParams viewDeliveryAllTopLineParams = (LinearLayout.LayoutParams) viewDeliveryAllTopLine.getLayoutParams();
                viewDeliveryAllTopLineParams.leftMargin = 0;

                llDeliveryAll.setLayoutParams(llDeliveryAllLayoutParams);
                viewDeliveryAllTopLine.setLayoutParams(viewDeliveryAllTopLineParams);

            }


            llDeliveryAll.setVisibility(View.VISIBLE);

            tvPickUpRecently.setText("已提:".concat(orderInfo.getHaveDeliveryQuantity()).concat("吨").concat("，").concat("未提:").concat(orderInfo.getUnDeliveryQuantity()).concat("吨"));
        } else {
            llDeliveryAll.setVisibility(View.GONE);
        }
    }

    private void pointPriceInformation(@Nullable List<PricingInfo> pricingInfoList) {
        if (pricingInfoList != null && pricingInfoList.size() > 0) {
            if ((orderInfo.getDeliverys() == null || orderInfo.getDeliverys().size() == 0) && (orderInfo.getPayments() == null || orderInfo.getPayments().size() == 0)) {
                viewDeliveryAllTopLine.setVisibility(View.VISIBLE);

            }


            llPricingAll.setVisibility(View.VISIBLE);
            tvRecentlyPointPrice.setText("已点:".concat(orderInfo.getHavePricingQuantityHand().concat("手")).concat("，").concat("未点:").concat(orderInfo.getUnPricingQuantityHand()).concat("手"));
        } else {
            llPaymentAll.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_status_btn:
                Bundle bundle = new Bundle();
                if (orderInfo.getButtons() != null && !orderInfo.getButtons().isEmpty()) {
                    if (orderInfo.getOrderType().contentEquals("BASIS")) {
                        bundle.putString("title", "订单详情");
                        bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                        gotoActivity(ComputerOperationActivity.class, bundle);
                    } else {
                        if (buttons.getType().equalsIgnoreCase("CONTRACT")) {
                            bundle.putString("orderId", orderInfo.getId());
                            gotoActivity(SignContractActivity.class, bundle);
                        } else if (buttons.getType().equalsIgnoreCase("ALL_PAY")) {
                            bundle.putString("orderId", orderInfo.getId());
                            gotoActivity(FullPayActivity.class, bundle);
                        } else if (buttons.getType().contentEquals("PAY_FRONT_MONEY")) {
                            bundle.putString("orderId", orderInfo.getId());
                            bundle.putString("payType", "_down_pay");
                            gotoActivity(PayActivity.class, bundle);
                        } else if (buttons.getType().contentEquals("PAY_DELIVERY")) {
                            bundle.putString("deliveryId", orderInfo.getDeliverys().get(0).getId());
                            bundle.putString("payType", "_goods_pay");
                            gotoActivity(PayActivity.class, bundle);
                        } else {
                            bundle.putString("title", "订单详情");
                            bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                            gotoActivity(ComputerOperationActivity.class, bundle);
                        }
                    }

                }

                break;


            case R.id.tv_direct_pay:
                showErrorDialog(getString(R.string.pay_alert), getString(R.string.pay_alert_message), getString(R.string.direct1), getString(R.string.keep_guarantee), PAY);
                break;
        }
    }

    private void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(this, message);
    }

    /**
     * 不满足条件 去电脑页面
     *
     * @param title
     * @param errorMessage
     */
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        ConfirmDialog.createConfirmDialogCustomButtonString(OrderDetailActivity.this, title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

                switch (action) {
                    case PAY:
                        if (UserDataUtil.isAdmin()) {
                            directPayToSeller(orderInfo.getId());
                        } else {
                            ConfirmDialog.createConfirmDialog(OrderDetailActivity.this, getString(R.string.limit_enough_contact), getString(R.string.label_confirm));
                        }
                        break;
                }
            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        }, leftButtonText, rightButtonText, false);
    }

    private Buttons getButtons() {
        for (Buttons b : orderInfo.getButtons()) {
            if (!b.getType().equalsIgnoreCase("CONFIRM_FAST_PAY")) {
                return b;
            }
        }
        return null;
    }

    private boolean isFastPay() {
        for (Buttons b : orderInfo.getButtons()) {
            if (b.getType().equalsIgnoreCase("CONFIRM_FAST_PAY")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 显示拨打电话的dialog
     */
    private void showChooseDialog(final String phone) {
        View view = getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(this, R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText(phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                try {
                    OrderDetailActivity.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public void directPayToSeller(String orderId) {

        DirectPayService directPayService = ApiServiceGenerator.createService(DirectPayService.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RxUtil.remove(directPayToSellerSubscription);
        directPayToSellerSubscription = directPayService
                .directPay(parameters)
                .map(new NetworkResultFunc1<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        goToDirectPayResult();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showErrorDialog(throwable.getMessage());
                    }
                });

    }

    private void goToDirectPayResult() {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderInfo.getId());
        gotoActivity(DirectPayActivity.class, bundle);
    }


    @OnClick({R.id.iv_service_phone, R.id.tv_recently_point_price, R.id.tv_pick_up_recently, R.id.iv_order_status, R.id.tv_order_status, R.id.time, R.id.iv_icon_order_model, R.id.tv_companyName, R.id.tv_price_info, R.id.tv_deliveryTime, R.id.tv_place, R.id.tv_seller_deposit, R.id.tv_price, R.id.tv_quantity, R.id.tv_buyer_deposit, R.id.tv_insurance, R.id.tv_pay_way, R.id.tv_direct_pay, R.id.ll_pricing_all, R.id.ll_delivery_all, R.id.ll_payment_all, R.id.tv_status_btn, R.id.tv_phone_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_status:
                break;
            case R.id.tv_order_status:
                break;
            case R.id.time:
                break;
            case R.id.iv_icon_order_model:
                break;
            case R.id.tv_companyName:
                break;
            case R.id.tv_price_info:
                break;
            case R.id.tv_deliveryTime:
                break;
            case R.id.tv_place:
                break;
            case R.id.tv_seller_deposit:
                break;
            case R.id.tv_price:
                break;
            case R.id.tv_quantity:
                break;
            case R.id.tv_buyer_deposit:
                break;
            case R.id.tv_insurance:
                break;
            case R.id.tv_pay_way:
                break;
            case R.id.tv_direct_pay:
                break;
            case R.id.ll_pricing_all:
            case R.id.tv_recently_point_price:
                Bundle bundlePrice = new Bundle();
                bundlePrice.putSerializable("priceList", (Serializable) orderInfo.getPricings());
                gotoActivity(PointPriceListActivity.class, bundlePrice);
                break;
            case R.id.ll_delivery_all:
            case R.id.tv_pick_up_recently:
                Bundle bundleDeliveryOneOrder = new Bundle();
                bundleDeliveryOneOrder.putSerializable("delivery", (Serializable) orderInfo.getDeliverys());
                gotoActivity(DeliveryListActivity.class, bundleDeliveryOneOrder);
                break;
            case R.id.ll_payment_all:
                Bundle bundlePay = new Bundle();
                bundlePay.putSerializable("paymentList", (Serializable) orderInfo.getPayments());
                gotoActivity(PayRecordListActivity.class, bundlePay);
                break;
            case R.id.tv_status_btn:
                break;
            case R.id.tv_phone_icon:
                if (orderInfo.getProduct() != null) {
                    showChooseDialog(orderInfo.getProduct().getContactPhone());
                }
                break;


            case R.id.iv_service_phone:
                showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                break;
        }
    }


}