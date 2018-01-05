package com.changcai.buyer.business_logic.about_buy_beans.sign_contract;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.ImageUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.FontCache;
import com.changcai.buyer.view.LoadingProgressDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.countdowntextview.CountDownTextView;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/1/9.
 */

public class SignContractFragment extends BaseFragment implements SignContract.View {

    SignContract.Presenter presenter;

    Unbinder unbinder;
    @BindView(R.id.tv_count_down)
    CountDownTextView tvCountDown;
    @BindView(R.id.tv_pick_up_time)
    CustomFontTextView tvPickUpTime;
    @BindView(R.id.tv_unit)
    CustomFontTextView tvUnit;
    @BindView(R.id.tv_ton_number)
    CustomFontTextView tvTonNumber;
    @BindView(R.id.tv_agree_sign)
    TextView tvAgreeSign;
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;
    @BindView(R.id.iv_contract_template)
    ImageView ivContractTemplate;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;


    private Bundle bundle;

    private UserInfo userInfo;

    public static final int ORDER_STATUS_ERROR = 1;
    public static final int ORDER_INFO_CHANGED = 2;


    /**
     * 定义target为类的成员变量，picasso 对target object 持有弱引用若直接在内部new target() GC回收object，自然不能回调到onBitmapLoaded方法
     * ImageUtil.compressImage（）属于io操作 Rx切换线程
     * In other words, you are not allowed to do Picasso.with(this).load("url").into(new Target() { ... }).
     */
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            if (bitmap != null) {
                Observable.just(bitmap)
                        .map(new Func1<Bitmap, Bitmap>() {
                            @Override
                            public Bitmap call(Bitmap bitmap) {
                                if (bitmap.getHeight() > AndroidUtil.getMaxHeight()) {
                                    return ImageUtil.compressImage(bitmap, bitmap.getWidth(), AndroidUtil.getMaxHeight());
                                } else {
                                    return bitmap;
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                ivContractTemplate.setImageBitmap(bitmap);
                            }
                        });
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            showErrorDialog("图片加载错误");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            LogUtil.d(SignContractFragment.class.getSimpleName(), "onprepareload");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_sign_contract, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCountDown.setTypeface(FontCache.getTypeface("ping_fang_light.ttf", getActivity()));
        RxView.clicks(tvAgreeSign).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showProgressDialog();

                if (userInfo == null) {
                    userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                }
                if (userInfo.getType().contentEquals("admin") || userInfo.getType().contentEquals("finance")) {
                    presenter.signContract();
                } else {
                    showErrorDialog(getString(R.string.limit_enough), getString(R.string.limit_enough_contact));
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = getArguments();
        if (bundle != null) {
            presenter.setOrderId(bundle.getString("orderId"));
        }
        presenter.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, int action) {

    }

    public void showErrorDialog(String title, String errorMessage) {
        ConfirmDialog.createConfirmDialogWithTitle(getActivityContext(), errorMessage, title);
    }

    @Override
    public void showSuccessDialog(String message) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                presenter.getOrderInfo();
            }
        });
    }

    @Override
    public void showSuccessDialog(String message, final int action) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case ORDER_STATUS_ERROR:
                        gotoActivity(OrderDetailActivity.class, bundle);
                        break;
                }
            }
        });
    }

    @Override
    public void showErrorDialog(String message, final int action) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (action) {
                    case ORDER_INFO_CHANGED:
                        break;
                }
            }
        });
    }
    @Override
    public void showContractInfo(OrderInfo orderInfo) {
        StringBuffer stringBuffer = new StringBuffer(orderInfo.getProduct().getDeliveryStartTime());
        stringBuffer.append(" 至 ");
        stringBuffer.append(orderInfo.getProduct().getDeliveryEndTime());
        tvPickUpTime.setText(stringBuffer);
        if (orderInfo.getPrice().contains("¥")) {
            tvUnit.setText(orderInfo.getPrice().replace("¥", ""));
        } else {
            tvUnit.setText(orderInfo.getPrice());
        }
        tvTonNumber.setText(orderInfo.getQuantity());
        setCountDownTime(orderInfo);
        presenter.setOrderPrice(orderInfo.getProduct().getPrice());
        Drawable defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
        Picasso.with(getActivity())
                .load(orderInfo.getContractTempURI())
                .placeholder(defaultDrawable)
                .into(target);
    }

    @Override
    public void gotoFullPay() {
        gotoActivity(FullPayActivity.class, bundle);
    }

    @Override
    public void setSignEnAble(boolean isAble) {
        tvAgreeSign.setEnabled(isAble);
    }

    @Override
    public void setPresenter(SignContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = LoadingProgressDialog.rechargeLoadingDialog(getActivityContext());
//        }
//        progressDialog.show();
        tvAgreeSign.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.showAnimation(true);

    }

    @Override
    public void dismissProgressDialog() {
//        if (progressDialog != null) {
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();
//        }
        progress.setVisibility(View.INVISIBLE);
        tvAgreeSign.setVisibility(View.VISIBLE);
        progress.refreshDone(true);
    }

    @Override
    public void gotoGoodsPay(String deliveryId) {
        bundle.putString("deliveryId", deliveryId);
        bundle.putString("payType", "_goods_pay");
        gotoActivity(PayActivity.class, bundle);
    }

    @Override
    public void gotoDownPay() {
        bundle.putString("payType", "_down_pay");
        gotoActivity(PayActivity.class, bundle);
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(getActivityContext(), message);
    }


    private void setCountDownTime(OrderInfo info) {
        if (Constants.NEW_BUY_CONFIRMING.equalsIgnoreCase(info.getOrderStatus()) ||
                Constants.OPEN_SELL_CONFIRMING.equalsIgnoreCase(info.getOrderStatus())) {
            long currentTime = System.currentTimeMillis();
            try {
                long diff = SystemClock.elapsedRealtime() + (Long.parseLong(info.getUpdateTime()) * 1000) - currentTime;
                if (diff > 0) {
                    tvCountDown.setTimeInFuture(diff);
                    tvCountDown.setStartFix("请在");
                    tvCountDown.setEndFix("内签署合同");
                    tvCountDown.setTimeFormat(CountDownTextView.TIME_SHOW_H_M_S_CHINA);
                    tvCountDown.setAutoDisplayText(true);
                    tvCountDown.start();

                    tvCountDown.addCountDownCallback(new CountDownTextView.CountDownCallback() {
                        @Override
                        public void onTick(CountDownTextView countDownTextView, long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish(CountDownTextView countDownTextView) {
                            tvCountDown.setText(R.string.time_up_sign_contract);
                            tvAgreeSign.setEnabled(false);
                        }
                    });

                    tvCountDown.setVisibility(View.VISIBLE);
                } else {
                    tvCountDown.setText(R.string.time_up_sign_contract);
                    tvAgreeSign.setEnabled(false);
                }
            } catch (Exception e) {

            }
        } else {
            tvCountDown.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
