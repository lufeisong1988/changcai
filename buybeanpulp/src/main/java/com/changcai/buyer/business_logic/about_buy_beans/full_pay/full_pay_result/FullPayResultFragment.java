package com.changcai.buyer.business_logic.about_buy_beans.full_pay.full_pay_result;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.MoneyUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;


/**
 * Created by liuxingwei on 2017/6/1.
 */

public class FullPayResultFragment extends BaseFragment implements FullPayResultContract.View {

    FullPayResultContract.Presenter presenter;
    @BindView(R.id.iv_full_pay_result)
    ImageView ivFullPayResult;
    @BindView(R.id.tv_full_pay_result)
    TextView tvFullPayResult;
    @BindView(R.id.ctv_pay_result_content)
    CustomFontTextView ctvPayResultContent;
    @BindView(R.id.tv_pay_action)
    TextView tvPayAction;
    Unbinder unbinder;
    @BindView(R.id.ctv_pay_action)
    CustomFontTextView ctvPayAction;

    BaseApiModel baseApiModel;


    private final static int DIRECT_PAY = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_pay_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
        baseApiModel = (BaseApiModel) getArguments().getSerializable("result");
        setResultModel(baseApiModel);

    }

    private void setResultModel(BaseApiModel b) {
        if (b.getErrorCode().contentEquals("0")) {
            showPayResultSuccess();
        } else {
            if (b.getErrorCode().contentEquals("FULL_PAY_WAIT_CONFIRM_ERROR")) {
                showPayResultWaitConfirm();
            } else if (b.getErrorCode().contentEquals("FULL_PAY_CONNECT_ERROR")) {
                showPayResultFail();
            } else if (b.getErrorCode().contentEquals("FULL_PAY_ERROR")) {
                showPayResultFail();
            } else if (b.getErrorCode().contentEquals("RECHARGE_OK_PAY_NG_ERROR")) {
                showRechargeOkPayFail();
            }
        }
    }

    /**
     * 充值成功支付失败
     */
    private void showRechargeOkPayFail() {
        tvFullPayResult.setText("付款失败！");
        ivFullPayResult.setImageLevel(0);
        ctvPayResultContent.setText(R.string.recharge_ok_delivery_pay_fail_for_full);
        ctvPayAction.setCompoundDrawables(null, null, null, null);
        SpannableString spannableString = new SpannableString("当前可用余额：".concat("¥").concat(StringUtil.formatForMoney(MoneyUtil.matchMoneyFromString(baseApiModel.getErrorDesc()))));
        ForegroundColorSpan foregroundColorSpanMColor = new ForegroundColorSpan(getResources().getColor(R.color.membership_color));
        ForegroundColorSpan foregroundColorSpanFlamingo = new ForegroundColorSpan(getResources().getColor(R.color.flamingo));
        spannableString.setSpan(foregroundColorSpanMColor, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpanFlamingo, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ctvPayAction.setText(spannableString);
        tvPayAction.setText("继续支付");
        RxView.clicks(tvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                getActivity().finish();
            }
        });
    }


    /**
     * 等待结果
     */
    private void showPayResultWaitConfirm() {
        tvFullPayResult.setText("等待返回结果！");
        ivFullPayResult.setImageLevel(50);
        ctvPayResultContent.setText(R.string.full_pay_result_confirm);
        ctvPayAction.setText("看看其它报价");
        RxView.clicks(ctvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                getActivity().finish();
            }
        });
        tvPayAction.setText("查看订单详情");
        RxView.clicks(tvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoActivity(OrderDetailActivity.class, getArguments());
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                getActivity().finish();
            }
        });
    }

    /**
     * 失败
     */
    private void showPayResultFail() {
        tvFullPayResult.setText("付款失败！");
        ivFullPayResult.setImageLevel(0);
        ctvPayResultContent.setText(R.string.full_pay_fail_info);
        ctvPayAction.setVisibility(View.GONE);
        tvPayAction.setText("查看订单详情");
        RxView.clicks(tvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoActivity(OrderDetailActivity.class, getArguments());
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                getActivity().finish();
            }
        });
    }

    /**
     * 直接付成功
     */
    @Override
    public void showImmediatePaySuccess() {
        tvFullPayResult.setText("直接付成功！");
        ivFullPayResult.setImageLevel(100);
        ctvPayResultContent.setText(R.string.direct_pay_success);
        ctvPayAction.setVisibility(View.VISIBLE);
        ctvPayAction.setText("看看其它报价");
        RxView.clicks(ctvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                getActivity().finish();
            }
        });
        tvPayAction.setText("查看订单详情");
        RxView.clicks(tvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoActivity(OrderDetailActivity.class, getArguments());
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                getActivity().finish();
            }
        });
    }

    //原全额付
    private void showPayResultSuccess() {
        if (getArguments().getBoolean("isFastPay")) {
            showImmediatePaySuccess();
            return;
        }
        tvFullPayResult.setText("付款成功！");
        ivFullPayResult.setImageLevel(100);
        ctvPayResultContent.setText(R.string.full_pay_result_success);
        ctvPayAction.setText("查看订单详情");
        RxView.clicks(ctvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class);
                gotoActivity(OrderDetailActivity.class, true, getArguments());
            }
        });
        tvPayAction.setText("直接付，全部打给卖家");
        RxView.clicks(tvPayAction).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (ivFullPayResult.getDrawable().getLevel() == 100) {
                    UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                    if (!userInfo.getType().contentEquals("admin")) {
                        showErrorDialog(getString(R.string.limit_enough), getString(R.string.limit_enough_contact));
                        return;
                    }
                    showErrorDialog(getString(R.string.direct_pay_to_seller), getString(R.string.direct_pay_to_seller_info), getString(R.string.balance_recharge_cancel), getString(R.string.direct_pay_to_seller), DIRECT_PAY);
                }
            }
        });
    }

    private void showErrorDialog(String title, String errorMessage) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), errorMessage, title);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }


    @Override
    public void setPresenter(FullPayResultContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message);
    }

    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        ConfirmDialog.createConfirmDialogCustomButtonString(getActivity(), title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {


                }

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case DIRECT_PAY:
                        presenter.directPayToSeller(getArguments().getString("orderId"));
                        break;
                }
            }
        }, leftButtonText, rightButtonText, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
