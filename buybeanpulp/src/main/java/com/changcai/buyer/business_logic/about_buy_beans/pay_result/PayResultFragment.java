package com.changcai.buyer.business_logic.about_buy_beans.pay_result;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.direct_pay.DirectPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.order.DeliveryDetailActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class PayResultFragment extends BaseFragment implements PayResultContract.View {
    private static final int DIRECT_PAY = 1;
    PayResultContract.Presenter presenter;
    @BindView(R.id.iv_pay_result)
    ImageView ivPayResult;
    @BindView(R.id.tv_pay_result)
    TextView tvPayResult;
    @BindView(R.id.tv_pay_result_info)
    TextView tvPayResultInfo;
    @BindView(R.id.tv_agree_sign)
    TextView tvAgreeSign;
    @BindView(R.id.tv_check_my_order)
    TextView tvCheckMyOrder;
    @BindView(R.id.tv_button_style_check_order)
    TextView tvButtonStyleCheckOrder;
    BaseApiModel baseApiModel;
    @BindView(R.id.tv_button_style_check_delivery)
    TextView tvButtonStyleCheckDelivery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pay_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseApiModel = (BaseApiModel) getArguments().getSerializable("result");
        if (baseApiModel.getErrorCode().contentEquals("0")) {
            if (baseApiModel.getResultObject() == null) {
                //原全额付
                showPayResultSuccess();
            } else if (baseApiModel.getResultObject().toString().contentEquals("frontMoneyPay")) {
                //预定订单支付成功
                showFrontMoneyPaySuccess();
                //标题下载成功
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_success));

            } else if (baseApiModel.getResultObject().toString().contentEquals("delivery")) {
                //提货单支付成功
                showDeliveryPaySuccess();
                //标题下载成功
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_success));

            }


        } else {
            if (baseApiModel.getErrorCode().contentEquals("FULL_PAY_WAIT_CONFIRM_ERROR")) {
                if (baseApiModel.getResultObject() == null) {
                    showPayResultConfirm();
                } else if (baseApiModel.getResultObject().toString().contentEquals("frontMoneyPay")) {
                    showFrontMoneyResultConfirm();
                } else if (baseApiModel.getResultObject().toString().contentEquals("delivery")) {
                    showDeliveryResultConfirm();
                }
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_waiting));
            } else if (baseApiModel.getErrorCode().contentEquals("FULL_PAY_CONNECT_ERROR")) {

                if (baseApiModel.getResultObject() == null) {
                    showPayResultFail();
                } else if (baseApiModel.getResultObject().toString().contentEquals("frontMoneyPay")) {
                    showFrontMoneyResultFail();
                } else if (baseApiModel.getResultObject().toString().contentEquals("delivery")) {
                    showDeliveryResultFail();
                }
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_fail_title));
            } else if (baseApiModel.getErrorCode().contentEquals("FULL_PAY_ERROR")) {
                if (baseApiModel.getResultObject() == null) {
                    showPayResultFail();
                } else if (baseApiModel.getResultObject().toString().contentEquals("frontMoneyPay")) {
                    showFrontMoneyResultFail();
                } else if (baseApiModel.getResultObject().toString().contentEquals("delivery")) {
                    showDeliveryResultFail();
                }
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_fail_title));
            } else if (baseApiModel.getErrorCode().contentEquals("RECHARGE_OK_PAY_NG_ERROR")) {
                ((PayResultActivity) getActivity()).setTitleText(getString(R.string.pay_fail_title));


                if (baseApiModel.getResultObject() == null) {
                    showRechargeOkPayFail();
                } else if (baseApiModel.getResultObject().toString().contentEquals("delivery")) {
                    showRechargeOkDeliveryPayFail();
                }else if (baseApiModel.getResultObject().toString().contentEquals("frontMoneyPay")){
                    showRechargeOkFrontPayFail();
                }

            }
        }

        RxView.clicks(tvAgreeSign).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (ivPayResult.getDrawable().getLevel() == 100) {
                    UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                    if (!userInfo.getType().contentEquals("admin")) {
                        showErrorDialog(getString(R.string.limit_enough), getString(R.string.limit_enough_contact));
                        return;
                    }
                    showErrorDialog(getString(R.string.direct_pay_to_seller), getString(R.string.direct_pay_to_seller_info), getString(R.string.balance_recharge_cancel), getString(R.string.direct_pay_to_seller), DIRECT_PAY);
                } else if (ivPayResult.getDrawable().getLevel() == 0) {
                    if (baseApiModel.getErrorCode().contentEquals("RECHARGE_OK_PAY_NG_ERROR"))
                        PayResultFragment.this.getActivity().finish();
                }
            }
        });

    }

    private void showRechargeOkFrontPayFail() {
        tvPayResultInfo.setText(baseApiModel.getErrorDesc());
        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setVisibility(View.GONE);
        tvCheckMyOrder.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvButtonStyleCheckOrder.getLayoutParams();
        layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckOrder.setLayoutParams(layoutParams);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tvButtonStyleCheckDelivery.getLayoutParams();
        layoutParams1.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckDelivery.setLayoutParams(layoutParams1);
        tvButtonStyleCheckDelivery.setVisibility(View.VISIBLE);
    }



    /**
     * 定金付款失败
     */
    private void showFrontMoneyResultFail() {

        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setVisibility(View.GONE);
        SpannableString spannableString = new SpannableString(getString(R.string.pay_fail_info));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray));
        spannableString.setSpan(foregroundColorSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray3));
        spannableString.setSpan(foregroundColorSpan1, 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPayResultInfo.setText(spannableString);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckDelivery.setVisibility(View.GONE);
    }

    /**
     * 充值成功-支付失败
     */
    private void showRechargeOkDeliveryPayFail() {
        tvPayResultInfo.setText(baseApiModel.getErrorDesc());
        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setVisibility(View.GONE);
        tvCheckMyOrder.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvButtonStyleCheckOrder.getLayoutParams();
        layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckOrder.setLayoutParams(layoutParams);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tvButtonStyleCheckDelivery.getLayoutParams();
        layoutParams1.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckDelivery.setLayoutParams(layoutParams1);
        tvButtonStyleCheckDelivery.setVisibility(View.VISIBLE);
    }

    private void showDeliveryResultFail() {
        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setVisibility(View.GONE);
        SpannableString spannableString = new SpannableString(getString(R.string.pay_fail_info));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray));
        spannableString.setSpan(foregroundColorSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray3));
        spannableString.setSpan(foregroundColorSpan1, 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPayResultInfo.setText(spannableString);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvCheckMyOrder.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvButtonStyleCheckOrder.getLayoutParams();
        layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckOrder.setLayoutParams(layoutParams);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tvButtonStyleCheckDelivery.getLayoutParams();
        layoutParams1.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckDelivery.setLayoutParams(layoutParams1);
        tvButtonStyleCheckDelivery.setVisibility(View.VISIBLE);
    }


    /**
     * 提货单等待
     */
    private void showDeliveryResultConfirm() {
        ivPayResult.setImageLevel(50);
        tvPayResult.setText(R.string.waiting_ping_an_easy_pay_confirm);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvAgreeSign.setVisibility(View.GONE);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        tvPayResultInfo.setText(R.string.waiting_info);
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckDelivery.setVisibility(View.VISIBLE);
    }

    /**
     * 定金等待
     */
    private void showFrontMoneyResultConfirm() {
        ivPayResult.setImageLevel(50);
        tvPayResult.setText(R.string.waiting_ping_an_easy_pay_confirm);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvAgreeSign.setVisibility(View.GONE);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        tvPayResultInfo.setText(R.string.waiting_info);
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckDelivery.setVisibility(View.GONE);
    }

    /**
     * 定金支付成功
     */
    private void showFrontMoneyPaySuccess() {
        ivPayResult.setImageLevel(100);
        tvPayResult.setText(R.string.front_money_pay_success);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvPayResult.setTextColor(getResources().getColor(R.color.black));
        tvAgreeSign.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvPayResultInfo.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        tvPayResultInfo.setLayoutParams(layoutParams);
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setText(R.string.front_money_pay_success_info);
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckDelivery.setVisibility(View.GONE);
    }

    /**
     * 提货单支付成功
     */
    private void showDeliveryPaySuccess() {
        ivPayResult.setImageLevel(100);
        tvPayResult.setText(R.string.delivery_pay_success);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvPayResult.setTextColor(getResources().getColor(R.color.black));
        tvAgreeSign.setVisibility(View.GONE);
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        LinearLayout.LayoutParams layoutParamsResult = (LinearLayout.LayoutParams) tvPayResultInfo.getLayoutParams();
        layoutParamsResult.gravity = Gravity.CENTER_HORIZONTAL;
        tvPayResultInfo.setLayoutParams(layoutParamsResult);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setText(R.string.delivery_pay_success_info);
        tvCheckMyOrder.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCheckMyOrder.setText(R.string.order_detail_btn_text);
        tvCheckMyOrder.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvButtonStyleCheckOrder.getLayoutParams();
        layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckOrder.setLayoutParams(layoutParams);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tvButtonStyleCheckDelivery.getLayoutParams();
        layoutParams1.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dim30);
        tvButtonStyleCheckDelivery.setLayoutParams(layoutParams1);
        tvButtonStyleCheckDelivery.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
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
    public void showSuccessDialog(int message) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void showPayResultFail() {
        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setVisibility(View.GONE);
        SpannableString spannableString = new SpannableString(getString(R.string.pay_fail_info));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray));
        spannableString.setSpan(foregroundColorSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.global_text_gray3));
        spannableString.setSpan(foregroundColorSpan1, 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPayResultInfo.setText(spannableString);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void showPayResultSuccess() {
        ivPayResult.setImageLevel(100);
        tvPayResult.setText(R.string.pay_success_and_waiting_seller_confirm);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvPayResult.setTextColor(getResources().getColor(R.color.black));
        tvAgreeSign.setVisibility(View.VISIBLE);
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setText(R.string.pay_trust);
        tvCheckMyOrder.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCheckMyOrder.setText(R.string.order_detail_btn_text);
        tvCheckMyOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckOrder.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showPayResultConfirm() {
        ivPayResult.setImageLevel(50);
        tvPayResult.setText(R.string.waiting_ping_an_easy_pay_confirm);
        tvPayResult.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt42)));
        tvAgreeSign.setVisibility(View.GONE);
        tvPayResultInfo.setTextSize(AndroidUtil.px2sp(getActivityContext(), getResources().getDimension(R.dimen.txt30)));
        tvPayResultInfo.setTextColor(getResources().getColor(R.color.global_text_gray));
        tvPayResultInfo.setText(R.string.waiting_info);
        tvCheckMyOrder.setVisibility(View.GONE);
        tvButtonStyleCheckOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToDirectPayResult() {
        gotoActivity(DirectPayActivity.class, getArguments());
    }

    @Override
    public void showRechargeOkPayFail() {
        tvPayResultInfo.setText(baseApiModel.getErrorDesc());
        ivPayResult.setImageLevel(0);
        tvPayResult.setText(R.string.pay_fail);
        tvAgreeSign.setText(R.string.go_on_to_pay);
        tvCheckMyOrder.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCheckMyOrder.setText(R.string.order_detail_btn_text);
        tvCheckMyOrder.setVisibility(View.VISIBLE);
        tvButtonStyleCheckOrder.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(PayResultContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }

    @OnClick({R.id.tv_button_style_check_delivery, R.id.tv_button_style_check_order, R.id.iv_pay_result, R.id.tv_pay_result, R.id.tv_pay_result_info, R.id.tv_check_my_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_button_style_check_order:
                gotoActivity(OrderDetailActivity.class, getArguments());
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, PayActivity.class);
                getActivity().finish();
                break;
            case R.id.iv_pay_result:
                break;
            case R.id.tv_pay_result:
                break;
            case R.id.tv_pay_result_info:
                break;

            case R.id.tv_check_my_order:
                gotoActivity(OrderDetailActivity.class, getArguments());
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, PayActivity.class);
                getActivity().finish();
                break;
            case R.id.tv_button_style_check_delivery:
                gotoActivity(DeliveryDetailActivity.class, getArguments());

                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, PayActivity.class);
                getActivity().finish();
                break;
        }
    }


    private void showErrorDialog(String title, String errorMessage) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), errorMessage, title);
    }


}
