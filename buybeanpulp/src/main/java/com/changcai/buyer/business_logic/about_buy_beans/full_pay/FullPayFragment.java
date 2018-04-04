package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.direct_pay.DirectPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.full_pay_result.FullPayResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_result.PayResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no.PingAnEasyPayRechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.recharge.RechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword.ResetPayPasswordActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.CalendarViewFragment;
import com.changcai.buyer.view.ClearEditText;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.FontCache;
import com.changcai.buyer.view.PayFragmentDialog;
import com.changcai.buyer.view.ProgressDialogFragment;
import com.changcai.buyer.view.countdowntextview.CountDownTextView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class FullPayFragment extends BaseFragment implements FullPayContract.View {

    FullPayContract.Presenter presenter;
    @BindView(R.id.tv_count_down)
    CountDownTextView tvCountDown;
    @BindView(R.id.tv_pick_up_site)
    TextView tvPickUpSite;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_ton_number)
    TextView tvTonNumber;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_pick_up_time)
    public TextView tvPickUpTime;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_pay_password)
    ClearEditText tvPayPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_agree_sign)
    TextView tvAgreeSign;
    @BindView(R.id.viewRoot)
    LinearLayout viewRoot;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ctv_platform_balance)
    CustomFontTextView ctvPlatformBalance;
    @BindView(R.id.ctv_recharge)
    CustomFontTextView ctvRecharge;


    public String deliveryTime;
    private Bundle bundle;


    private boolean isFastPay;

    private Date currentDate;
    private String deliveryStartTime;
    private Observable<Bundle> payEventObservable;
    private Observable<Boolean> forgetPayPasswordEventObservable;
    private Observable<Boolean> resetPayPasswordEventObservable;
    long block;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_full_payment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payEventObservable = RxBus.get().register("payEvent", Bundle.class);
        forgetPayPasswordEventObservable = RxBus.get().register("forgetPayPassword", Boolean.class);
        resetPayPasswordEventObservable = RxBus.get().register("resetPayPassword", Boolean.class);

        payEventObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bundle>() {
                    @Override
                    public void call(Bundle payType) {
                        showProgressDialog();
                        if (payType.containsKey("payType"))
                            presenter.ebaoFullPay(payType.getString("payPassword"), deliveryTime, payType.getString("payType"));
                        else
                            presenter.ebaoFullPay(payType.getString("payPassword"), deliveryTime, null);
                    }
                });

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

        RxView.clicks(tvAgreeSign).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                showErrorDialog("因平安易宝异常，暂时无法线上支付，请联系卖家完成支付。有问题请联系：021-54180258");

                //选货时间
//                if (null == deliveryTime) {
//                    showErrorDialog(getString(R.string.please_chose_pick_up_time));
//                    return;
//                }
//                //校验密码
//                if (presenter.isSupport()) {
//                    showFullPayFragmentDialog();
//                } else {
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putString("title", "订单详情");
//                    bundle2.putString("tips", "这项操作暂时无法在app内完成，你可以：");
//                    gotoActivity(ComputerOperationActivity.class, bundle2);
//                }
            }
        });
        tvCountDown.setTypeface(FontCache.getTypeface("ping_fang_light.ttf", getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = getArguments();
        presenter.setOrderId(bundle.getString("orderId"));
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        tvPayPassword.clearFocus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
        RxBus.get().unregister("payEvent", payEventObservable);
        RxBus.get().unregister("forgetPayPassword", forgetPayPasswordEventObservable);
        RxBus.get().unregister("resetPayPassword", resetPayPasswordEventObservable);
    }

    @Override
    public int getPAY_EBAO_INSUFFICIENT_ERROR() {
        return Constants.PAY_EBAO_INSUFFICIENT_ERROR;
    }

    @Override
    public int getRechargeOkPayFailError() {
        return Constants.TOTAL_BALANCE_INSUFFICIENT_ERROR;
    }

    View view1;

    @Override
    public void gotoResult(BaseApiModel stringBaseApiModel) {
        bundle.putSerializable("result", stringBaseApiModel);
        bundle.putBoolean("isFastPay",isFastPay);
        gotoActivity(FullPayResultActivity.class,bundle);
//        if (isFastPay) {
//            //油厂直接进入到直接付结果 如果油厂不为success 进入payResult展示详情
//            if (!stringBaseApiModel.getErrorCode().contentEquals("0")) {
//                gotoActivity(PayResultActivity.class, bundle);
//            } else {
//                //油厂成功直接进入直接付成功
//                gotoActivity(DirectPayActivity.class, bundle);
//            }
//        } else {
//            gotoActivity(PayResultActivity.class, bundle);
//        }
    }

    public String endFixString = "235959";

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
    public void showBalance(String money) {
        ctvPlatformBalance.setText("¥".concat(StringUtil.formatForMoney(money)));
    }

    @Override
    public int getPayEbaoInsufficientError() {
        return Constants.PAY_EBAO_INSUFFICIENT_ERROR;
    }

    @Override
    public int getFullPayWaitConfirmError() {
        return Constants.FULL_PAY_WAIT_CONFIRM_ERROR;
    }

    @Override
    public int getFullPayConnectError() {
        return Constants.FULL_PAY_CONNECT_ERROR;
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFullPayInfo(OrderInfo orderInfo) {
        if (orderInfo.getPayModel().equalsIgnoreCase("FAST_PAY")) {
            isFastPay = true;
        } else {
            isFastPay = false;
        }
        tvPickUpSite.setText(orderInfo.getRegionAndLocation() == null ? "----" : orderInfo.getRegionAndLocation());
        tvCompanyName.setText(orderInfo.getProduct().getEnterName() == null ? "----" : orderInfo.getProduct().getEnterName());
        if (orderInfo.getPrice() != null) {
            tvUnit.setText(orderInfo.getPrice());
        } else {
            tvUnit.setText("-----");
        }
        tvTonNumber.setText(orderInfo.getQuantity() == null ? "----" : orderInfo.getQuantity());
        tvTotalAmount.setText(orderInfo.getOrderAmount() == null ? "----" : "¥".concat(StringUtil.formatForMoneyNoDot(orderInfo.getOrderAmount())));
        setCountDownTime(orderInfo);
        Date latelyPickUpDate = DateUtil.stringToDate("yyyy-MM-dd", orderInfo.getProduct().getDeliveryStartTime());
        currentDate = new Date(Long.parseLong(orderInfo.getCurrentTime()));
        if (currentDate == null) {
            currentDate = new Date();
        }
        if (DateUtil.compareDate(currentDate, latelyPickUpDate) >= 0) {
            tvPickUpTime.setText(DateUtil.dateToString("yyyy年MM月dd日", currentDate) + " " + DateUtil.getWeekOfDate(currentDate));
            deliveryTime = DateUtil.dateToString("yyyyMMdd", currentDate).concat(endFixString);
        } else if (DateUtil.compareDate(currentDate, latelyPickUpDate) < 0) {
            tvPickUpTime.setText(DateUtil.dateToString("yyyy年MM月dd日", latelyPickUpDate) + " " + DateUtil.getWeekOfDate(latelyPickUpDate));
            deliveryTime = DateUtil.dateToString("yyyyMMdd", latelyPickUpDate).concat(endFixString);
        }
        //提货区间
        block = DateUtil.countDays(DateUtil.stringToDate("yyyy-MM-dd", orderInfo.getProduct().getDeliveryStartTime()), DateUtil.stringToDate("yyyy-MM-dd", orderInfo.getProduct().getDeliveryEndTime()));

        deliveryStartTime = orderInfo.getProduct().getDeliveryStartTime();

    }

    @Override
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        scrollView.requestFocus();
        if (isActive())
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


    private void showCalendar() {
        CalendarViewFragment _calendarViewFragment = new CalendarViewFragment();
        Bundle _arguments = new Bundle();
        _arguments.putLong("block", block);
        _arguments.putString("time", deliveryStartTime);
        _arguments.putSerializable("date", currentDate);
        _calendarViewFragment.setArguments(_arguments);
        _calendarViewFragment.show(getChildFragmentManager(), "CalendarViewFragment");
    }

    private void setCountDownTime(OrderInfo info) {
        if (Constants.NEW_BUY_DEPOSITING.equalsIgnoreCase(info.getOrderStatus())
                || Constants.OPEN_SELL_DEPOSITING.equalsIgnoreCase(info.getOrderStatus())) {
            long currentTime = System.currentTimeMillis();
            try {
                long diff = SystemClock.elapsedRealtime() + (Long.parseLong(info.getUpdateTime()) * 1000) - currentTime;
                if (diff > 0) {
                    tvCountDown.setTimeInFuture(diff);
                    tvCountDown.setStartFix("请在");
                    tvCountDown.setEndFix("内全额付款");
                    tvCountDown.setTimeFormat(CountDownTextView.TIME_SHOW_H_M_S_CHINA);
                    tvCountDown.setAutoDisplayText(true);
                    tvCountDown.start();
                    tvCountDown.addCountDownCallback(new CountDownTextView.CountDownCallback() {
                        @Override
                        public void onTick(CountDownTextView countDownTextView, long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish(CountDownTextView countDownTextView) {
                            tvCountDown.setText(R.string.time_up_full_pay);
                            tvAgreeSign.setEnabled(false);
                        }
                    });

                    tvCountDown.setVisibility(View.VISIBLE);
                } else {
                    tvCountDown.setText(R.string.time_up_full_pay);
                    tvAgreeSign.setEnabled(false);
                }
            } catch (Exception e) {

            }
        } else {
            tvCountDown.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }


    @Override
    public void setPresenter(FullPayContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        if (isActive())
            ConfirmDialog.createConfirmDialog(getActivityContext(), message);
    }


    @OnClick({R.id.ctv_recharge,
            R.id.scrollView, R.id.viewRoot, R.id.tv_count_down, R.id.tv_pick_up_site, R.id.tv_company_name, R.id.tv_unit, R.id.tv_ton_number, R.id.tv_total_amount, R.id.tv_pick_up_time, R.id.iv_more, R.id.tv_pay_password, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctv_recharge:
                gotoActivity(RechargeActivity.class);
                break;
            case R.id.tv_count_down:
                break;
            case R.id.tv_pick_up_site:
                break;
            case R.id.tv_company_name:
                break;
            case R.id.tv_unit:
                break;
            case R.id.tv_ton_number:
                break;
            case R.id.tv_total_amount:
                break;
            case R.id.tv_pick_up_time:
                Toast.makeText(getActivityContext(), getString(R.string.chose_pick_up_time), Toast.LENGTH_SHORT).show();
                showCalendar();
                break;
            case R.id.iv_more:
                Toast.makeText(getActivityContext(), getString(R.string.chose_pick_up_time), Toast.LENGTH_SHORT).show();
                showCalendar();
                break;
            case R.id.tv_pay_password:
                break;
            case R.id.tv_forget_password:
                gotoActivity(ResetPayPasswordActivity.class);
                break;
        }
    }

    /**
     * 弹出全额付款密码支付对话框，为了省事
     * 密码支付不再新建MVP模块，采用fragmentDialog对话框，保持原来跳转逻辑不变。
     */
    public void showFullPayFragmentDialog() {
        if (getActivity().isFinishing()) return;
        PayFragmentDialog payFragmentDialog = new PayFragmentDialog();
        payFragmentDialog.show(getChildFragmentManager(), "PayDialog");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
