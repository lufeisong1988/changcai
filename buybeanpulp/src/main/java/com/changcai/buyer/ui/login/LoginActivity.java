package com.changcai.buyer.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.juggist.commonlibrary.rx.LoginState;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.NimSessionHelper;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.PlainEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author wlv
 * @version 1.0
 * @description 登录页面
 * @date 15-12-5 下午4:11
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText cet_phone_number;
    private PlainEditText cet_password;
    private TextView tv_login, tv_forget_password;
    private String phone;
    private Subscription loginSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_do_login_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone", "");
        }
        if (TextUtils.isEmpty(phone)) {
            phone = SPUtil.getString(Constants.KEY_LAST_USERNAME);
        }
        initTitle();
        initView();
        UserDataUtil.setPushBind(false);

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
        tvTitle.setText("登录");
        btnRight.setText("注册");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("mode", false);
                gotoActivity(RegisterAutoCodeActivity.class, bundle);
            }
        });
        showBackButton();
    }

    private void initView() {
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_login.setEnabled(false);
        cet_phone_number = (EditText) findViewById(R.id.cet_phone_number);
        cet_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (count > 0) {
                    cet_phone_number.setSelected(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cet_phone_number.getText())
                        && !TextUtils.isEmpty(cet_password.getText())) {
                    tv_login.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_login.setEnabled(true);
                } else {
                    tv_login.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_login.setEnabled(false);
                }
                if (s.length() > 0) {
                    cet_phone_number.setSelected(true);
                } else {
                    cet_phone_number.setSelected(false);
                }
            }
        });
        cet_password = (PlainEditText) findViewById(R.id.cet_password);
        cet_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (count > 0) {
                    cet_password.setSelected(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cet_phone_number.getText())
                        && !TextUtils.isEmpty(cet_password.getText())) {
                    tv_login.setBackgroundResource(R.drawable.login_blue_btn);
                    tv_login.setEnabled(true);
                } else {
                    tv_login.setBackgroundResource(R.drawable.login_disable_btn);
                    tv_login.setEnabled(false);
                }

                if (s.length() > 0) {
                    cet_password.setSelected(true);
                } else {
                    cet_password.setSelected(false);
                }
            }
        });
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        SpannableString spannableString = new SpannableString(getString(R.string.forget_password2));
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_forget_password.setText(spannableString);
        tv_forget_password.setOnClickListener(this);

        if (!TextUtils.isEmpty(phone)) {
            cet_phone_number.setText(phone);
            cet_password.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String nameStr = cet_phone_number.getText().toString();
                String passwordStr = cet_password.getText().toString();
                if (TextUtils.isEmpty(nameStr)) {
                    ConfirmDialog.createConfirmDialog(this, "账号不能为空");
                    if (TextUtils.isEmpty(passwordStr)) {
                        ConfirmDialog.createConfirmDialog(this, "密码不能为空");
                    }
                    return;
                }
                if (TextUtils.isEmpty(passwordStr)) {
                    ConfirmDialog.createConfirmDialog(this, "密码不能为空");
                    return;
                }

                if (!StringUtil.isMobile(nameStr)) {
                    ConfirmDialog.createConfirmDialog(this, "手机号或密码错误，请重新输入");
                    return;
                }
                if (!NetUtil.checkNet(this)) {
                    ConfirmDialog.createConfirmDialog(this, getString(R.string.no_signal_exception));
                    return;
                }
                loginTask(nameStr, passwordStr);
                break;
            case R.id.tv_forget_password:
                Bundle bundle = new Bundle();
                bundle.putBoolean("mode", true);
                gotoActivity(RegisterAutoCodeActivity.class, bundle);
                break;
            case R.id.btnLeft:
                LoginState.publish(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        LoginState.publish(false);
        super.onBackPressed();
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    private void loginTask(String name, final String password) {
        VolleyUtil.getInstance().showPd(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", name);
        params.put("password", password);
        params.put("registrationId", JPushInterface.getRegistrationID(this));
        VolleyUtil.getInstance().httpPost(this, Urls.USER_LOGIN, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    UserInfo user = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), UserInfo.class);
                    SPUtil.saveboolean(Constants.KEY_IS_LOGIN, true);
                    SPUtil.saveString(Constants.KEY_LAST_USERNAME, user.getMobile());
                    SPUtil.saveString(Constants.KEY_IS_AUTH, user.getEnterStatus());
                    SPUtil.saveboolean(Constants.KEY_IS_BUYER, "True".equalsIgnoreCase(user.getIsBuyer()));
                    SPUtil.saveboolean(Constants.KEY_IS_FACTORY, "factory".equalsIgnoreCase(user.getEnterType()));
                    SPUtil.saveString(Constants.KEY_TOKEN_ID, user.getTokenId());
                    SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, user);
                    LoginState.publish(true);
                    RxBus.get().post("inOrOutAction", new Boolean(true));
                    if (getIntent().getExtras() != null) {
                        if (getIntent().getExtras().containsKey("pushEvent")) {
                            RxBus.get().post(Constants.PUSH_MESSAGE, getIntent().getExtras());
                        }
                    }

                    //如果登录成功就登录网易云信
                    NimSessionHelper.getInstance().login();
                } else {
                 ServerErrorCodeDispatch.getInstance().checkErrorCode(LoginActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputKeyBoard(tv_login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 取消订阅
         */
        loginSubscription = RxUtil.unsubscribe(loginSubscription);
    }
}
