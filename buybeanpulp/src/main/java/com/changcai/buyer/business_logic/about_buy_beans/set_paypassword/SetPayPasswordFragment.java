package com.changcai.buyer.business_logic.about_buy_beans.set_paypassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.view.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public class SetPayPasswordFragment extends BaseFragment implements SetPayPasswordContract.View {

    SetPayPasswordContract.Presenter presenter;
    Unbinder binder;
    @BindView(R.id.cet_payment_password)
    EditText cetPaymentPassword;
    @BindView(R.id.cet_payment_password_confirm)
    EditText cetPaymentPasswordConfirm;
    @BindView(R.id.tv_set_payment)
    TextView tvSetPayment;
    @BindView(R.id.iv_password_visibility)
    ImageView ivPasswordVisibility;
    @BindView(R.id.ll_iv_password_parent)
    LinearLayout llIvPasswordParent;
    @BindView(R.id.tv_is_reset)
    TextView tvIsReset;


    private TextView tvTitle;

    public SetPayPasswordFragment() {
    }

    public static SetPayPasswordFragment getInstance() {
        return new SetPayPasswordFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
        binder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_set_payment_password, container, false);
        binder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(SetPayPasswordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
    }

    @OnClick()
    public void onClick() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        if (getArguments() != null) {
            if (getArguments().containsKey("isReset")) {
                if (getArguments().getBoolean("isReset")) {
                    tvTitle.setText(getString(R.string.reset_password));
                    tvIsReset.setText(getString(R.string.set_pay_password3));
                } else {
                    tvTitle.setText(R.string.retrieve_password);
                    tvIsReset.setText(R.string.set_pay_password3);
                }
                cetPaymentPassword.setHint(R.string.please_input_new_payment_password);
            }
        } else {
            tvTitle.setText(getString(R.string.set_pay_password2));
            tvIsReset.setText(getString(R.string.set_pay_password2));
        }

    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void showSuccessDialog() {

        ConfirmDialog.createConfirmDialog(activity, getString(R.string.set_success), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @OnClick({R.id.iv_password_visibility, R.id.ll_iv_password_parent, R.id.tv_set_payment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_password_visibility:
            case R.id.ll_iv_password_parent:
                if (ivPasswordVisibility.isSelected()) {
                    cetPaymentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cetPaymentPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cetPaymentPassword.setSelection(cetPaymentPassword.getText().length());
                    cetPaymentPasswordConfirm.setSelection(cetPaymentPasswordConfirm.getText().length());
                    ivPasswordVisibility.setSelected(false);
                } else {
                    cetPaymentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cetPaymentPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cetPaymentPassword.setSelection(cetPaymentPassword.getText().length());
                    cetPaymentPasswordConfirm.setSelection(cetPaymentPasswordConfirm.getText().length());
                    ivPasswordVisibility.setSelected(true);
                }
                break;
            case R.id.tv_set_payment:
                presenter.checkout(cetPaymentPassword.getText().toString(), cetPaymentPasswordConfirm.getText().toString(),getArguments());
                break;
        }
    }
}
