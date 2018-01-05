package com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.DetailsIdent;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.set_paypassword.SetPayPasswordActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.util.IDCardUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ConfirmDialog;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public class ResetPayPasswordFragment extends BaseFragment implements ResetPayPasswordContract.View {


    Unbinder unbinder;
    ResetPayPasswordContract.Presenter presenter;
    DetailsIdent details;
    protected UserInfo userInfo;
    protected Timer mTimer;
    protected static final int COUNT_DOWN_TIME = 60;//倒计时
    protected int time = COUNT_DOWN_TIME;
    protected uiHandler mHandler;
    @BindView(R.id.tv_register_mobile_number)
    TextView tvRegisterMobileNumber;
    @BindView(R.id.tv_send_dynamic_code)
    TextView tvSendDynamicCode;
    @BindView(R.id.et_received_dynamic_code)
    EditText etReceivedDynamicCode;
    @BindView(R.id.et_company_code)
    EditText etCompanyCode;
    @BindView(R.id.tv_commit_update)
    TextView tvCommitUpdate;
    @BindView(R.id.tv_safe_info)
    TextView tvSafeInfo;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_modify_pay_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isActive()) {
            userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
            presenter.start();
            mHandler = new uiHandler(this);
        }
//        ivPasswordVisibility.setSelected(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VolleyUtil.getInstance().showPd(getActivityContext());
        presenter.loadIdentityDetails(userInfo.getTokenId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }


    public static ResetPayPasswordFragment getInstance() {
        return new ResetPayPasswordFragment();
    }

    @Override
    public void setPresenter(ResetPayPasswordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
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

    @Override
    public void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void closeTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        tvSendDynamicCode.setClickable(true);
        time = COUNT_DOWN_TIME;
        if (tvSendDynamicCode != null) {
            tvSendDynamicCode.setText("重发动态码");
            tvSendDynamicCode.setTextColor(getResources().getColor(R.color.white));
            tvSendDynamicCode.setBackgroundResource(R.drawable.login_get_auth_background);
        }
    }

    @Override
    public void startTimerTask() {
        tvSendDynamicCode.setClickable(false);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }, 100, 1000);
    }

    @Override
    public void reSetTime() {
        time = COUNT_DOWN_TIME;
    }

    @Override
    public void setEnterType(DetailsIdent ident) {

        this.details = ident;
        tvRegisterMobileNumber.setText(userInfo.getMobile());
        if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL")) {
//            tvPersonOrCompany.setText(R.string.people_id_card);
            etCompanyCode.setHint(R.string.please_input_legal_people_id_card3);
            tvSafeInfo.setText(getString(R.string.reset_password_info, getString(R.string.please_input_legal_people_id_card3)));
        } else {
            if (null != details) {

                if (!TextUtils.isEmpty(details.getZzjgdm())) {
//                    tvPersonOrCompany.setText(getString(R.string.organization_code));
                    etCompanyCode.setHint(getString(R.string.organization_code2));
                    tvSafeInfo.setText(getString(R.string.reset_password_info, getString(R.string.organization_code2)));

                } else if (!TextUtils.isEmpty(details.getUniformSocialCreditCode())) {
//                    tvPersonOrCompany.setText(getString(R.string.uniform_standard_credit_code));
                    etCompanyCode.setHint(getString(R.string.please_uniform_standard_credit_code));
                    tvSafeInfo.setText(getString(R.string.reset_password_info, getString(R.string.please_uniform_standard_credit_code)));
                } else {
                    etCompanyCode.setHint(R.string.company_type_code);
                    tvSafeInfo.setText(getString(R.string.reset_password_info, getString(R.string.company_type_code)));

                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeMessages(1);
    }

    @OnClick({R.id.tv_register_mobile_number, R.id.tv_send_dynamic_code, R.id.et_received_dynamic_code, R.id.et_company_code, R.id.tv_commit_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_dynamic_code:
                presenter.sendDynamicCode(userInfo.getTokenId());
                break;
            case R.id.tv_commit_update:
                if (TextUtils.isEmpty(etReceivedDynamicCode.getText().toString())) {
                    showToast(getString(R.string.please_input_code));
                    return;
                }

                if (!StringUtil.checkSMSCode(etReceivedDynamicCode.getText().toString())) {
                    showErrorDialog(getString(R.string.sms_limit));
                    return;
                }


                if (details != null) {
                    if (TextUtils.isEmpty(etCompanyCode.getText().toString())) {
                        if (!TextUtils.isEmpty(details.getZzjgdm())) {
                            showErrorDialog(getString(R.string.organization_code2));
                            return;
                        } else if (!TextUtils.isEmpty(details.getUniformSocialCreditCode())) {
                            showErrorDialog(getString(R.string.please_uniform_standard_credit_code));
                            return;
                        } else if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL")) {
                            showErrorDialog(getString(R.string.please_input_no2));
                            return;
                        }
                    }

                    if (!TextUtils.isEmpty(etCompanyCode.getText().toString())) {
                        if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL")) {
                            if (!IDCardUtil.isIDCard(etCompanyCode.getText().toString())) {
                                showErrorDialog(getString(R.string.please_input_legal_right_people_id_card6));
                                return;
                            }
                        } else {
                            if (!TextUtils.isEmpty(details.getZzjgdm())) {
                                if (!StringUtil.isValidEntpCode(etCompanyCode.getText().toString())) {
                                    showErrorDialog(getString(R.string.organization_code3));
                                    return;
                                }
                            }

                            if (!TextUtils.isEmpty(details.getUniformSocialCreditCode())) {
                                if (!StringUtil.uniformCode(etCompanyCode.getText().toString())) {
                                    showErrorDialog(getString(R.string.please_uniform_standard_credit_code));
                                    return;
                                }
                            }

                        }
                    }
                }


//                if (!StringUtil.checkPassword(etNewPaymentPassword.getText().toString())) {
//                    showErrorDialog(getString(R.string.payment_limited_info2));
//                    return;
//                }
//
//                if (!etNewPaymentPassword.getText().toString().equalsIgnoreCase(etSureNewPaymentPassword.getText().toString())) {
//                    showErrorDialog(getString(R.string.password_not_equals));
//                    return;
//                }


//                presenter.commitUpdate(userInfo.getTokenId(), etNewPaymentPassword.getText().toString(), etReceivedDynamicCode.getText().toString(), etCompanyCode.getText().toString());


                Bundle bundle = new Bundle();
                bundle.putString("dynamicCode", etReceivedDynamicCode.getText().toString());
                bundle.putString("companyCode", etCompanyCode.getText().toString());
                bundle.putBoolean("isReset", getArguments().getBoolean("isReset"));
                gotoActivity(SetPayPasswordActivity.class, bundle);
                getActivity().finish();
                break;
            case R.id.tv_register_mobile_number:
                break;

            case R.id.iv_password_visibility:
            case R.id.ll_iv_password_parent:

//                if (ivPasswordVisibility.isSelected()) {
//                    etNewPaymentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    etSureNewPaymentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    etNewPaymentPassword.setSelection(etNewPaymentPassword.getText().length());
//                    etSureNewPaymentPassword.setSelection(etSureNewPaymentPassword.getText().length());
//                    ivPasswordVisibility.setSelected(false);
//                } else {
//                    etNewPaymentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    etSureNewPaymentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    etNewPaymentPassword.setSelection(etNewPaymentPassword.getText().length());
//                    etSureNewPaymentPassword.setSelection(etSureNewPaymentPassword.getText().length());
//                    ivPasswordVisibility.setSelected(true);
//                }
                break;
        }
    }


    private static class uiHandler extends Handler {
        private WeakReference<ResetPayPasswordFragment> activityWeakReference;

        public uiHandler(ResetPayPasswordFragment activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ResetPayPasswordFragment activity = activityWeakReference.get();
            if (null != activity) {
                int msgId = msg.what;
                switch (msgId) {
                    case 1:
                        if (activity.isAdded() && activity.isActive()) {
                            activity.time--;
                            activity.tvSendDynamicCode.setBackgroundResource(R.drawable.login_disable_auth_background);
                            activity.tvSendDynamicCode.setTextColor(activity.getResources().getColor(R.color.global_text_gray));
                            activity.tvSendDynamicCode.setText("重新发送" + "(" + activity.time + ")");
                            if (activity.time == 0) {
                                activity.closeTimer();
                                return;
                            }
                        }
                        break;
                }
            }
        }
    }
}
