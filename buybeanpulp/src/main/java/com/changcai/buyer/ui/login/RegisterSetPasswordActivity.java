package com.changcai.buyer.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.juggist.commonlibrary.rx.LoginState;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 15-12-6 下午4:19
 */
public class RegisterSetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText cet_password;
    private EditText cet_password_confirm;
    private TextView tv_setPassword;
    private String verifyCode;
    private String phone;
    private boolean resetPassword;

    private ImageView ivPasswordVisibility;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_do_password_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            verifyCode = bundle.getString("verifyCode");
            phone = bundle.getString("phone");
            resetPassword = bundle.getBoolean("mode");
        }
        initTitle();
        initView();
        initEditTextWatcher();
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        bar.statusBarDarkFont(true, 0.0f).statusBarColor(R.color.white).fitsSystemWindows(false).init();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        if (resetPassword) {
            tvTitle.setText(R.string.label_find_password);
        } else {
            tvTitle.setText(R.string.label_register);
        }
        showBackButton();
    }

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_iv_password_parent);
        ivPasswordVisibility = (ImageView) findViewById(R.id.iv_password_visibility);
        cet_password = (EditText) findViewById(R.id.cet_password);
        cet_password_confirm = (EditText) findViewById(R.id.cet_password_confirm);
        tv_setPassword = (TextView) findViewById(R.id.tv_setPassword);
        tv_setPassword.setOnClickListener(this);
        tv_setPassword.setEnabled(false);
//        if (resetPassword) {
//            tv_setPassword.setText(R.string.label_confirm);
//        }
    }

    private void initEditTextWatcher() {
        cet_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cet_password.getText())
                        && !TextUtils.isEmpty(cet_password_confirm.getText())) {
                    tv_setPassword.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_setPassword.setEnabled(true);
                } else {
                    tv_setPassword.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_setPassword.setEnabled(false);
                }
            }
        });
        cet_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cet_password.getText())
                        && !TextUtils.isEmpty(cet_password_confirm.getText())) {
                    tv_setPassword.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_setPassword.setEnabled(true);
                } else {
                    tv_setPassword.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_setPassword.setEnabled(false);
                }
            }
        });

        ivPasswordVisibility.setOnClickListener(this);
        ivPasswordVisibility.setSelected(false);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setPassword:
                String password = cet_password.getText().toString();
                if (!StringUtil.checkPassword(password)) {
                    ConfirmDialog.createConfirmDialog(this, "密码6-16位，包含字母和数字，请重新输入");
                    return;
                }
                if (!(password.equals(cet_password_confirm.getText().toString()))) {
                    ConfirmDialog.createConfirmDialog(this, "两次密码输入不一致，请重新输入");
                    return;
                }

                if (!NetUtil.checkNet(this)) {
                    ToastUtil.showShort(RegisterSetPasswordActivity.this, R.string.no_signal_exception);
                    return;
                }
                setPassword(phone, password, verifyCode);
                break;

            case R.id.ll_iv_password_parent:
            case R.id.iv_password_visibility:
                if (ivPasswordVisibility.isSelected()) {
                    cet_password_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cet_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cet_password.setSelection(cet_password.getText().length());
                    cet_password_confirm.setSelection(cet_password_confirm.getText().length());
                    ivPasswordVisibility.setSelected(false);
                } else {
                    cet_password_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cet_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cet_password.setSelection(cet_password.getText().length());
                    cet_password_confirm.setSelection(cet_password_confirm.getText().length());
                    ivPasswordVisibility.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置密码
     *
     * @param phone
     * @param authCode
     */
    private void setPassword(final String phone, String password, String authCode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", phone);
        params.put("password", password);
        params.put("verifyCode", authCode);
        String url = Urls.REGISTER;
        if (resetPassword)
            url = Urls.UPDATE_PWD_BY_FORGET;
        VolleyUtil.getInstance().httpPost(this, url, params, new HttpListener() {

            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get("errorCode").getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    if (resetPassword) {
                        ToastUtil.showShort(RegisterSetPasswordActivity.this, "密码更新成功");
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phone);
                        gotoActivity(LoginActivity.class, bundle);
                    } else {
                        ToastUtil.showShort(RegisterSetPasswordActivity.this, "注册成功");
                        Gson gson = new Gson();
                        UserInfo user = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), UserInfo.class);
                        SPUtil.saveboolean(Constants.KEY_IS_LOGIN, true);
                        SPUtil.saveString(Constants.KEY_LAST_USERNAME, user.getMobile());
                        SPUtil.saveboolean(Constants.KEY_IS_BUYER, "True".equalsIgnoreCase(user.getIsBuyer()));
                        SPUtil.saveboolean(Constants.KEY_IS_FACTORY, "factory".equalsIgnoreCase(user.getEnterType()));
                        SPUtil.saveString(Constants.KEY_IS_AUTH, user.getEnterStatus());
                        SPUtil.saveString(Constants.KEY_TOKEN_ID, user.getTokenId());
                        SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, user);
                        LoginState.publish(true);
                        //如果登录成功就登录网易云信
//                        NimSessionHelper.getInstance().login();
                        finish();
                        return;
                    }
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(RegisterSetPasswordActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        }, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

