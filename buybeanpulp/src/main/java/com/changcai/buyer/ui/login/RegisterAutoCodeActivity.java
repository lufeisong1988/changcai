package com.changcai.buyer.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.rx.LoginState;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscription;
import rx.functions.Action1;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 15-12-6 下午4:19
 */
public class RegisterAutoCodeActivity extends BaseActivity implements View.OnClickListener {

    private EditText cet_phone, cet_verify_code;
    private TextView tv_get_authcode, tv_register;

    private Timer mTimer;
    private static final int COUNT_DOWN_TIME = 60;//倒计时
    private int time = COUNT_DOWN_TIME;
    private Subscription loginSubscription;

    private boolean resetPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_do_authcode_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resetPassword = bundle.getBoolean("mode");
        }
        initTitle();
        initView();
        initEditTextWatcher();
        loginSubscription = RxUtil.unsubscribe(loginSubscription);
        loginSubscription = LoginState.getObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean bDone) {
                /**
                 * 登录成功则销毁自己、返回至登录前页面
                 */
                if (bDone && UserDataUtil.isLogin()) {
                    finish();
                }
            }
        });
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
        cet_phone = (EditText) findViewById(R.id.cet_phone_number);
        cet_verify_code = (EditText) findViewById(R.id.cet_verify_code);
        tv_get_authcode = (TextView) findViewById(R.id.tv_get_otp);
        tv_get_authcode.setOnClickListener(this);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        tv_register.setEnabled(false);
        tv_register.setText(R.string.next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_otp:
                String phone = cet_phone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ConfirmDialog.createConfirmDialog(this, "请输入正确的手机号");
                    return;
                }
                if (!StringUtil.isMobile(phone)) {
                    ConfirmDialog.createConfirmDialog(this, "请输入正确的手机号");
                    return;
                }
                if (!NetUtil.checkNet(this)) {
                    ConfirmDialog.createConfirmDialog(this, getString(R.string.no_signal_exception));
                    return;
                }
                getAuthCode(phone);
                break;
            case R.id.tv_register:
                String phoneStr = cet_phone.getText().toString();
                if (TextUtils.isEmpty(phoneStr)) {
                    ConfirmDialog.createConfirmDialog(this, "请输入正确的手机号");
                    return;
                }
                if (!StringUtil.isMobile(phoneStr)) {
                    ConfirmDialog.createConfirmDialog(this, "请输入正确的手机号");
                    return;
                }
                String codeStr = cet_verify_code.getText().toString();
                if (TextUtils.isEmpty(codeStr)) {
                    ConfirmDialog.createConfirmDialog(this, "动态码不能为空");
                    return;
                }

                if (!NetUtil.checkNet(this)) {
                    ConfirmDialog.createConfirmDialog(this, getString(R.string.no_signal_exception));
                    return;
                }
                closeTimer();
                checkAuthCode(phoneStr, codeStr);
                break;
            default:
                break;
        }
    }

    /**
     * Edittext输入框监听
     */
    private void initEditTextWatcher() {
        cet_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (time < COUNT_DOWN_TIME && s != null && s.length() < 11) {//fix bug tricky
                    closeTimer();
                }
                if (!TextUtils.isEmpty(cet_phone.getText())
                        && !TextUtils.isEmpty(cet_verify_code.getText())) {
                    tv_register.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_register.setEnabled(true);
                } else {
                    tv_register.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_register.setEnabled(false);
                }
            }
        });
        cet_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cet_phone.getText())
                        && !TextUtils.isEmpty(cet_verify_code.getText())) {
                    tv_register.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_register.setEnabled(true);
                } else {
                    tv_register.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_register.setEnabled(false);
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getAuthCode(String phone) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", phone);
        String url = Urls.SEND_VERIFY_CODE;
        if (resetPassword)
            url = Urls.SEND_FORGET_PWD;
        VolleyUtil.getInstance().httpPost(this, url, params, new HttpListener() {

            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    startTimerTask();
                    cet_verify_code.requestFocus();
                    ToastUtil.showShort(RegisterAutoCodeActivity.this, "动态码发送成功，请留意查看短信");
                } else {
                    closeTimer();
                    time = COUNT_DOWN_TIME;
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(RegisterAutoCodeActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                closeTimer();
            }
        }, false);
    }

    /**
     * 检查验证码
     *
     * @param phone
     * @param authCode
     */
    private void checkAuthCode(final String phone, final String authCode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", phone);
        params.put("verifyCode", authCode);
        String url = Urls.CHECK_VERIFY_CODE;
        if (resetPassword)
            url = Urls.CHECK_FORGET_PWD;
        VolleyUtil.getInstance().httpPost(this, url, params, new HttpListener() {

            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get("errorCode").getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Bundle b = new Bundle();
                    b.putString("phone", phone);
                    b.putString("verifyCode", authCode);
                    b.putBoolean("mode", resetPassword);
                    gotoActivity(RegisterSetPasswordActivity.class, false, b);
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(RegisterAutoCodeActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                closeTimer();
            }
        }, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeTimer();
        /**
         * 取消订阅
         */
        loginSubscription = RxUtil.unsubscribe(loginSubscription);
    }

    /**
     * 关闭定时器；
     */
    private void closeTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        tv_get_authcode.setClickable(true);
        time = COUNT_DOWN_TIME;
        if (tv_get_authcode != null) {
            tv_get_authcode.setText("重发动态码");
            tv_get_authcode.setTextColor(getResources().getColor(R.color.white));
            tv_get_authcode.setBackgroundResource(R.drawable.login_get_auth_background);
        }
    }

    /**
     * 启动定时器
     */
    private void startTimerTask() {
        tv_get_authcode.setClickable(false);
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

    /**
     * do some action
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    time--;
                    tv_get_authcode.setBackgroundResource(R.drawable.login_disable_auth_background);
                    tv_get_authcode.setTextColor(getResources().getColor(R.color.global_text_gray));
                    tv_get_authcode.setText("重新发送" + "(" + time + ")");
                    if (time == 0) {
                        closeTimer();
                        return;
                    }
                    break;
            }
        }
    };

}

