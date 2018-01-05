package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.direct_pay.DirectPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_result.PayResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_result.PayResultContract;
import com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no.PingAnEasyPayRechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.recharge.RechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword.ResetPayPasswordActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.order.bean.PreviewContract;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.LoadingProgressDialog;
import com.changcai.buyer.view.PayFragmentDialog;
import com.changcai.buyer.view.ProgressDialogFragment;
import com.changcai.buyer.view.countdowntextview.CountDownTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2017/3/31.
 * 定金支付
 */

public class PayFragment extends BaseFragment implements PayContract.View {


    @Nullable
    @BindView(R.id._tv_please)
    TextView _TvPlease;
    @Nullable
    @BindView(R.id._tv_count_down)
    CountDownTextView _TvCountDown;
    @Nullable
    @BindView(R.id._tv)
    TextView _Tv;
    @Nullable
    @BindView(R.id._ll_count_down)
    LinearLayout _LlCountDown;
    @Nullable
    @BindView(R.id._tv_seller)
    TextView _TvSeller;
    @Nullable
    @BindView(R.id._tv_price)
    TextView _TvPrice;
    @Nullable
    @BindView(R.id._tv_quantity)
    TextView _TvQuantity;
    @Nullable
    @BindView(R.id._tv_deposit)
    TextView _TvDeposit;
    @Nullable
    @BindView(R.id._tv_ping_an_pay)
    TextView _TvPingAnPay;
    @Nullable
    @BindView(R.id.tv_agree_sign)
    TextView _tvAgreeSign;
    @Nullable
    @BindView(R.id.tv_recharge)
    TextView _TvRecharge;
    @Nullable
    @BindView(R.id._tv_deliver_time)
    TextView _TvDeliverTime;
    @Nullable
    @BindView(R.id._tv_pick_up_location)
    TextView _TvPickUpLocation;
    @Nullable
    @BindView(R.id._tv_actual_pick_up)
    TextView _TvActualPickUp;
    @Nullable
    @BindView(R.id._tv_money_deduction)
    TextView _TvMoneyDeduction;
    @Nullable
    @BindView(R.id._tv_waiting_pay)
    TextView _TvWaitingPay;
    private View view;


    private PayContract.Presenter payPresenter;


    private Observable<Bundle> payEventObservable;
    private Observable<Boolean> forgetPayPasswordEventObservable;
    private Observable<Boolean> resetPayPasswordEventObservable;
    private Observable<Bundle> frontMoneyPayObservable;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments().getString("payType").contentEquals("_down_pay")) {
            view = inflater.inflate(R.layout.down_pay, container, false);
        } else if (getArguments().getString("payType").contentEquals("_goods_pay")) {
            view = inflater.inflate(R.layout.goods_pay, container, false);
        }
        baseUnbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _TvRecharge.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        _TvRecharge.setText(R.string.recharge);
        forgetPayPasswordEventObservable = RxBus.get().register("forgetPayPassword", Boolean.class);
        resetPayPasswordEventObservable = RxBus.get().register("resetPayPassword", Boolean.class);

        if (getArguments().getString("payType").contentEquals("_down_pay")) {
            frontMoneyPayObservable = RxBus.get().register("frontMoneyPayObservable", Bundle.class);
            frontMoneyPayObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bundle>() {
                        @Override
                        public void call(Bundle bundle) {

                            try {
                                showProgressDialog();
                            } catch (NullPointerException e) {
                                LogUtil.d(PayFragment.class.getSimpleName(), e.getMessage());
                                MobclickAgent.reportError(getContext(), e);
                            } finally {

                            }

                            if (bundle.containsKey("payType"))
                                payPresenter.frontMoneyPay(bundle.getString("payPassword"), bundle.getString("payType"));
                            else
                                payPresenter.frontMoneyPay(bundle.getString("payPassword"), null);
                        }
                    });
        } else if (getArguments().getString("payType").contentEquals("_goods_pay")) {
            payEventObservable = RxBus.get().register("payEvent", Bundle.class);
            payEventObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bundle>() {
                        @Override
                        public void call(Bundle payType) {
                            if (isAdded()) {
                                showProgressDialog();
                            }
                            if (payType.containsKey("payType"))
                                payPresenter.payOrdDelivery(payType.getString("payPassword"), payType.getString("payType"));
                            else
                                payPresenter.payOrdDelivery(payType.getString("payPassword"), null);
                        }
                    });
        }
        forgetPayPasswordEventObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean.booleanValue())
                            showErrorDialog(getString(R.string.pay_password_error), "", getString(R.string.forget_password), getString(R.string.retry), Constants.PAY_PASSWARD_UNCRRECT);
                    }
                });

        resetPayPasswordEventObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean.booleanValue()) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isReset", false);//找回密码
                            gotoActivity(ResetPayPasswordActivity.class, bundle);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            if (getArguments().containsKey("deliveryId")) {
                payPresenter.setDeliveryId(getArguments().getString("deliveryId"));
            } else if (getArguments().containsKey("orderId")) {
                payPresenter.setOrderId(getArguments().getString("orderId"));
            }
        }
        payPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        payPresenter.detach();
        if (getArguments().getString("payType").contentEquals("_down_pay"))
            _TvCountDown.cancel();
        super.onDestroy();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void showProgressDialog() {
        if (getActivity().isFinishing()) return;
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.show(getChildFragmentManager(), "Progress");
    }

    @Override
    public void dismissProgressDialog() {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if (progressDialogFragment != null)
            progressDialogFragment.dismiss();
    }

    @Override
    public Context getMContext() {
        return getContext();
    }

    /**
     * String errorCode; 支付成功/错误码
     * String errorDesc; 错误描述
     * T resultObject; 此处用来区分订单类型。提货单，定金支付两种类型
     *
     * @param baseApiModel
     */
    @Override
    public void gotoResult(BaseApiModel baseApiModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", baseApiModel);
        if (getArguments().containsKey("orderId")) {
            bundle.putString("orderId", getArguments().getString("orderId"));
        }
        if (getArguments().containsKey("deliveryId")) {
            bundle.putString("deliveryId", getArguments().getString("deliveryId"));
        }
        gotoActivity(PayResultActivity.class, bundle);
    }


    @Override
    public void showFullPayInfo(OrderInfo orderInfo) {
        //支付定金
        if (getArguments().getString("payType").contentEquals("_down_pay")) {
            setCountDownTime(orderInfo);
            _TvSeller.setText(orderInfo.getProduct().getEnterName());
            if (orderInfo.getPrice().contains("¥"))
                _TvPrice.setText(orderInfo.getPrice().replace("¥", "").concat("元"));
            _TvQuantity.setText(orderInfo.getQuantity().concat("吨"));
            _TvDeposit.setText(StringUtil.formatForMoney(orderInfo.getPayOffset()).concat("元"));
        }

    }

    @Override
    public void showDeliveryInfo(DeliveryInfo deliveryInfo) {
        //支付货款
        if (getArguments().getString("payType").contentEquals("_goods_pay")) {
            _TvSeller.setText(deliveryInfo.getSellerName() == null ? "null" : deliveryInfo.getSellerName());
            _TvDeliverTime.setText(deliveryInfo.getDeliveryTime());
            _TvPickUpLocation.setText(deliveryInfo.getAddress());
            _TvActualPickUp.setText(deliveryInfo.getQuantity().concat("吨"));
            _TvMoneyDeduction.setText(deliveryInfo.getPayOffset().concat("元"));
            _TvWaitingPay.setText(deliveryInfo.getOriginalAmount() == null ? "null元" : deliveryInfo.getOriginalAmount().concat("元"));
        }
    }

    private void setCountDownTime(OrderInfo info) {
        long currentTime = System.currentTimeMillis();
        try {
            long diff = SystemClock.elapsedRealtime() + (Long.parseLong(info.getUpdateTime()) * 1000) - currentTime;
            if (diff > 0) {
                _TvCountDown
                        .setTimeInFuture(diff);
                _TvCountDown.setTimeFormat(CountDownTextView.TIME_SHOW_H_M_S_CHINA);
                _TvCountDown.setAutoDisplayText(true);
                _TvCountDown.start();
                _TvCountDown.addCountDownCallback(new CountDownTextView.CountDownCallback() {
                    @Override
                    public void onTick(CountDownTextView countDownTextView, long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish(CountDownTextView countDownTextView) {
                        _TvPlease.setVisibility(View.GONE);
                        _Tv.setVisibility(View.GONE);
                        _TvCountDown.setText(R.string.time_up_down_pay);
                        _tvAgreeSign.setEnabled(false);
                    }
                });

                _TvCountDown.setVisibility(View.VISIBLE);
            } else {
                _TvPlease.setVisibility(View.GONE);
                _Tv.setVisibility(View.GONE);
                _TvCountDown.setText(R.string.time_up_down_pay);
                _tvAgreeSign.setEnabled(false);
            }
        } catch (Exception e) {
            _TvPlease.setVisibility(View.GONE);
            _TvCountDown.setText(R.string.time_up_down_pay);
            _tvAgreeSign.setEnabled(false);
        }
    }

    @Override
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        if (isAdded())
            ConfirmDialog.createConfirmDialogCustomButtonString(getActivity(), title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
                @Override
                public void onConfirmListener() {
                    switch (action) {
                        //忘记密码
                        case Constants.PAY_PASSWARD_UNCRRECT:
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isReset", false);//找回密码
                            gotoActivity(ResetPayPasswordActivity.class, bundle);
                            break;
                    }

                }
            }, new ConfirmDialog.OnBtnConfirmListener() {
                @Override
                public void onConfirmListener() {
                    switch (action) {
                        //重试
                        case Constants.PAY_PASSWARD_UNCRRECT:
//                        ((FullPayActivity) getActivity()).showFullPayFragmentDialog();
                            break;
                        case Constants.TOTAL_BALANCE_INSUFFICIENT_ERROR:
                            gotoActivity(PingAnEasyPayRechargeActivity.class);
                            break;
                        case Constants.PAY_EBAO_INSUFFICIENT_ERROR:
                            gotoActivity(RechargeActivity.class);
                            break;

                    }
                }
            }, leftButtonText, rightButtonText, false);
    }

    @Override
    public void showBalance(String money) {
//        _TvPingAnPay.setText(getString(R.string.platform_balance).concat("：").concat(money));
        _TvPingAnPay.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(PayContract.Presenter presenter) {
        this.payPresenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        if (isAdded())
            ConfirmDialog.createConfirmDialog(getContext(), message);
    }


    public void showFullPayFragmentDialog() {
        if (getActivity().isFinishing()) return;
        PayFragmentDialog payFragmentDialog = new PayFragmentDialog();
        payFragmentDialog.setArguments(getArguments());
        payFragmentDialog.show(getChildFragmentManager(), "PayDialog");
    }


    @OnClick({R.id.tv_recharge, R.id.tv_agree_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agree_sign:
//                showFullPayFragmentDialog();
                showErrorDialog("因平安易宝异常，暂时无法线上支付，请联系卖家完成支付。有问题请联系：021-54180258");

                break;
            case R.id.tv_recharge:
                gotoActivity(RechargeActivity.class);
                break;
        }
    }
}
