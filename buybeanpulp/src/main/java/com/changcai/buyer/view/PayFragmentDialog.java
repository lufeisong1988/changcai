package com.changcai.buyer.view;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.deposit_and_pay.DepositAndPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.recharge.RechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword.ResetPayPasswordActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.StringUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.functions.Action1;


/**
 * Created by liuxingwei on 2017/3/6.
 */

public class PayFragmentDialog extends BaseBottomDialog {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_btn_right)
    ImageView ivBtnRight;
    @BindView(R.id.tv_pay_password)
    EditText tvPayPassword;
    @BindView(R.id.tv_agree_sign)
    TextView tvAgreeSign;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ctv_forget_password)
    CustomFontTextView ctvForgetPassword;
    Unbinder unbinder;
    @BindView(R.id.viewTop)
    View actionBar;

    @Override
    public int getLayoutRes() {
        return R.layout.full_pay_fragment_dialog;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        if (getArguments() != null) {
            if (getArguments().containsKey("payType")) {
                if (getArguments().getString("payType").contentEquals("_down_pay")) {
                    tvTitle.setText(R.string.down_pay_money);
                    tvAgreeSign.setText(R.string.confirm_down_pay_money);
                } else if (getArguments().getString("payType").contentEquals("_goods_pay")) {
                    tvTitle.setText(R.string.delivery_pay6);
                    tvAgreeSign.setText(R.string.confirm_delivery_pay);
                }
            }
        } else {
            tvTitle.setText(R.string.full_pay);
        }

        ivBtnRight.setVisibility(View.VISIBLE);
        actionBar.setBackgroundResource(R.color.white);
        RxTextView
                .textChanges(tvPayPassword)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0)
                            tvAgreeSign.setEnabled(true);
                        else
                            tvAgreeSign.setEnabled(false);
                    }
                });

        /**
         * 响应500ms 的第一次点击事件
         */
        RxView.clicks(tvAgreeSign).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (TextUtils.isEmpty(tvPayPassword.getText().toString())) {
                            showErrorDialog(getString(R.string.please_input_payment_password));
                            return;
                        }
                        if (!StringUtil.checkPassword(tvPayPassword.getText().toString())) {
//                    dismiss();
                            RxBus.get().post("forgetPayPassword", new Boolean(true));
                            return;
                        }
//                dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("payPassword", tvPayPassword.getText().toString());
                        bundle.putString("payType", "direct");
                        RxBus.get().post("payEvent", bundle);
                        RxBus.get().post("frontMoneyPayObservable", bundle);
                    }
                });
    }

    @Override
    public float getDimAmount() {
        return 1.0f;
    }

    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(getContext(), message);
    }


    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        ConfirmDialog.createConfirmDialogCustomButtonString(getActivity(), title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case Constants.PAY_PASSWARD_UNCRRECT:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isReset", false);//找回密码
                        gotoActivity(ResetPayPasswordActivity.class);
                        break;
                }

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case Constants.PAY_PASSWARD_UNCRRECT:
                        tvPayPassword.performClick();
                        tvPayPassword.requestFocus();
                        break;
                    case Constants.PAY_EBAO_INSUFFICIENT_ERROR:
                        gotoActivity(RechargeActivity.class);
                        break;

                }
            }
        }, leftButtonText, rightButtonText, false);
    }

    @OnClick({R.id.iv_btn_right, R.id.iv_back, R.id.ctv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.iv_btn_right:
                gotoActivity(DepositAndPayActivity.class);
                break;
            case R.id.ctv_forget_password:
//                dismiss();
                RxBus.get().post("resetPayPassword", new Boolean(true));
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
