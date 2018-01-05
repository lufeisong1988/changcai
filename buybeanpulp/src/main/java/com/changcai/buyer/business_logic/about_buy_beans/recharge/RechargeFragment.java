package com.changcai.buyer.business_logic.about_buy_beans.recharge;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no.PingAnEasyPayRechargeActivity;
import com.changcai.buyer.business_logic.about_buy_beans.recharge_result.RechargeResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword.ResetPayPasswordActivity;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.listener.KeyboardChangeListener;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.NumUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ClearEditText;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.LoadingProgressDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public class RechargeFragment extends BaseFragment implements RechargeContract.View {

    RechargeContract.Presenter presenter;
    @BindView(R.id.tv_ping_an_balance)
    TextView tvPingAnBalance;
    @BindView(R.id.tv_ping_an_balance_money)
    TextView tvPingAnBalanceMoney;
    @BindView(R.id.tv_ping_an_recharge_money)
    TextView tvPingAnRechargeMoney;
    @BindView(R.id.iv_more_ping_an)
    ImageView ivMorePingAn;
    @BindView(R.id.et_recharge_money)
    ClearEditText etRechargeMoney;
    @BindView(R.id.et_recharge_money_password)
    ClearEditText etRechargeMoneyPassword;
    @BindView(R.id.tv_post_introduction)
    TextView tvPostIntroduction;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private KeyboardChangeListener keyboardChangeListener;
    private EbaoBalanceInfo ebaoBalanceInfo;

    private Dialog progressDialog;

    private static final int ACTION1 = 952;//充值
    public static final int ACTION2 = 953;//忘记密码
    private static final int ACTION3 = 954;//无网络

    /**
     * 输入框小数的位数
     */
    private static final int DECIMAL_DIGITS = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recharge, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etRechargeMoney.setFilters(new InputFilter[]{lengthfilter});
        buttonHighLightObservable();
        etRechargeMoneyPassword.clearFocus();
        etRechargeMoneyPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                } else {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }
        });

        keyboardChangeListener = new KeyboardChangeListener(getActivity());
        keyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, final int keyboardHeight) {

                if (isShow){
                    Observable.timer(500, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {

                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                    scrollView.scrollTo(0,keyboardHeight);
                                }
                            });
                }
            }
        });

        RxView.clicks(tvPostIntroduction).throttleFirst(500,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (verify())
                    presenter.rechargePay(etRechargeMoneyPassword.getText().toString(), etRechargeMoney.getText().toString());
            }
        });
    }

    private void buttonHighLightObservable() {
        Observable<Boolean> moneyNotNull = RxTextView.textChangeEvents(etRechargeMoney).map(new Func1<TextViewTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                return !TextUtils.isEmpty(textViewTextChangeEvent.text());
            }
        });

        Observable<Boolean> passwordNotNull = RxTextView.textChangeEvents(etRechargeMoneyPassword).map(new Func1<TextViewTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                return !TextUtils.isEmpty(textViewTextChangeEvent.text());
            }
        });

        Observable.combineLatest(moneyNotNull, passwordNotNull, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        }).observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        tvPostIntroduction.setEnabled(aBoolean);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
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
      switch (action){
          case ACTION2:
              etRechargeMoneyPassword.clearFocus();
              break;
      }

        ConfirmDialog.createConfirmDialogCustomButtonString(getActivity(), title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case ACTION1:

                        break;
                    case ACTION2:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isReset",false);//找回密码
                        gotoActivity(ResetPayPasswordActivity.class);
                        break;
                    case ACTION3:
                        getActivity().finish();
                        break;
                }

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case ACTION1:
                        goPingAnEasyPayRechargeActivity();
                        break;
                    case ACTION2:
                        etRechargeMoneyPassword.requestFocus();
                        break;

                    case ACTION3:

                        break;
                }
            }
        }, leftButtonText, rightButtonText, false);
    }

    @Override
    public void setPresenter(RechargeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message);
    }


    @Override
    public void showSuccessDialog(int message) {

    }

    @Override
    public void setEbaoBalance(EbaoBalanceInfo balanceMoney) {
        this.ebaoBalanceInfo = balanceMoney;
        setBalanceText();
    }

    @Override
    public void goToResult(BaseApiModel baseApiModel) {
        Bundle bundle = new Bundle();
        StringBuffer stringBuffer = new StringBuffer();
        baseApiModel.setResultObject(stringBuffer.append("充值金额").append(NumUtil.moneyWithComma(Double.parseDouble(etRechargeMoney.getText().toString()))).append("元"));
        bundle.putSerializable("recharge_result", baseApiModel);
        gotoActivity(RechargeResultActivity.class, bundle);
        getActivity().finish();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = LoadingProgressDialog.rechargeLoadingDialog(getActivityContext());
        }
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private void setBalanceText() {
        if (ebaoBalanceInfo != null)
            tvPingAnBalanceMoney.setText((NumUtil.moneyWithComma(Double.parseDouble(ebaoBalanceInfo.getBankAcountBalance())) + "元"));
    }

    @OnClick({R.id.scrollView,R.id.tv_forget_password, R.id.tv_ping_an_balance, R.id.tv_ping_an_balance_money, R.id.tv_ping_an_recharge_money, R.id.iv_more_ping_an, R.id.et_recharge_money, R.id.et_recharge_money_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ping_an_balance:
                break;
            case R.id.tv_ping_an_balance_money:
                break;
            case R.id.tv_ping_an_recharge_money:
                goPingAnEasyPayRechargeActivity();
                break;
            case R.id.iv_more_ping_an:
                break;
            case R.id.et_recharge_money:
                break;
            case R.id.et_recharge_money_password:
                break;
            case R.id.tv_forget_password:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isReset",false);
                gotoActivity(ResetPayPasswordActivity.class,bundle);
                break;
        }
    }


    private void goPingAnEasyPayRechargeActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("EbaoInfo", ebaoBalanceInfo);
        gotoActivity(PingAnEasyPayRechargeActivity.class, bundle);
    }

    /**
     * 设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if (source.equals(".") && dest.toString().length() == 0) {
                return "0.";
            }
            if (dest.toString().contains(".")) {
                int index = dest.toString().indexOf(".");
                int mlength = dest.toString().substring(index).length();
                if (mlength == DECIMAL_DIGITS) {
                    return "";
                }
            }
            return null;
        }
    };


    private boolean verify() {

        if (!NetUtil.checkNet(getActivityContext())) {
            showErrorDialog(
                    ""
                    , getString(R.string.net_work_exception)
                    , getString(R.string.go_back)
                    , getString(R.string.retry)
                    , ACTION3);
            return false;
        }
        if (null == ebaoBalanceInfo) {
            showErrorDialog(getString(R.string.get_balance_failed));
            return false;
        }
        if (!StringUtil.checkPassword(etRechargeMoneyPassword.getText().toString())) {
            showErrorDialog(
                    getString(R.string.pay_password_error)
                    , ""
                    , getString(R.string.forget_password)
                    , getString(R.string.retry)
                    , ACTION2);
            return false;
        }

        Double balanceMoney = Double.valueOf(ebaoBalanceInfo.getBankAcountBalance());
        Double rechargeMoney = Double.valueOf(etRechargeMoney.getText().toString());
        if (Double.valueOf(rechargeMoney).compareTo(0.00) == 0) {
            showErrorDialog(getString(R.string.please_input_recharge_money_error));
            return false;
        }
        if (Double.compare(rechargeMoney, balanceMoney) > 0) {
            showErrorDialog(
                    getString(R.string.balance_not_enough)
                    , getString(R.string.balance_not_enough_go_recharge)
                    , getString(R.string.balance_recharge_cancel)
                    , getString(R.string.go_to_recharge)
                    , ACTION1);
            return false;
        }
        return true;
    }



}
