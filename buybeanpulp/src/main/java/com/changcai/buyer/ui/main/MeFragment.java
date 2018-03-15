package com.changcai.buyer.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.SpaceStatistic;
import com.changcai.buyer.bean.TempInfoInputForIdCertify;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.about_us.AboutBuyBeansActivity;
import com.changcai.buyer.business_logic.about_buy_beans.authenticate.AuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.company_authenticate.CompanyAuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.PersonAuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.child_accountId.ChildAccountIdActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.im.provider.LoginProvider;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.RefreshUserInfoService;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.rx.LoginState;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.ui.consultant.ConsultantSettingActivity;
import com.changcai.buyer.ui.introduction.MemberShipIntroductionActivity;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.message.MessageListActivity;
import com.changcai.buyer.ui.order.DeliveryListActivity;
import com.changcai.buyer.ui.order.OrderListActivity;
import com.changcai.buyer.util.NimSessionHelper;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.MyScrollView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 15-12-9 上午11:51
 */
public class MeFragment extends BaseFragment implements View.OnClickListener ,LoginProvider.LoginCallback{
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.username)
    CustomFontTextView ctvUserInfo;
    @BindView(R.id.icon)
    View icon;
    @BindView(R.id.iv_membership)
    ImageView ivMembership;
    @BindView(R.id.tv_member_name)
    CustomFontTextView tvMemberName;
    @BindView(R.id.tv_validity)
    CustomFontTextView tvValidity;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.my_info_layout)
    RelativeLayout myInfoLayout;
    @BindView(R.id.cvt_my_order)
    CustomFontTextView cvtMyOrder;
    @BindView(R.id.tab_divider)
    View tabDivider;
    @BindView(R.id.my_order_signing)
    CustomFontTextView myOrderSigning;
    @BindView(R.id.deposit)
    CustomFontTextView deposit;
    @BindView(R.id.pickingUpGoods)
    CustomFontTextView pickingUpGoods;
    @BindView(R.id.cvt_my_pick)
    CustomFontTextView cvtMyPick;
    @BindView(R.id.tab_divider2)
    View tabDivider2;
    @BindView(R.id.it_waiting_pay)
    CustomFontTextView itWaitingPay;
    @BindView(R.id.it_confirm_landing)
    CustomFontTextView itConfirmLanding;
    @BindView(R.id.message_layout)
    RelativeLayout messageLayout;
    @BindView(R.id.tv_about_buy_beans)
    CustomFontTextView tvAboutBuyBeans;
    @BindView(R.id.view_new_version_mark)
    View viewNewVersionMark;
    @BindView(R.id.about_app_layout)
    RelativeLayout aboutAppLayout;
    @BindView(R.id.more_item_icon)
    ImageView moreItemIcon;
    @BindView(R.id.id_authenticate_info)
    TextView idAuthenticateInfo;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.id_authenticate_layout)
    RelativeLayout idAuthenticateLayout;
    @BindView(R.id.membership_level_layout)
    RelativeLayout membershipLevelLayout;
    @BindView(R.id.login_out)
    CustomFontTextView loginOut;
    @BindView(R.id.ll_login_out)
    LinearLayout llLoginOut;
    @BindView(R.id.scroll_view)
    MyScrollView nestedScrollView;
    Unbinder unbinder;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.view_bottom_line)
    View viewBottomLine;
    @BindView(R.id.titleView)
    RelativeLayout titleView;
    @BindView(R.id.ctv_free)
    CustomFontTextView ctvFree;
    @BindView(R.id.ll_member)
    LinearLayout llmember;
    @BindView(R.id.consultant_layout)
    RelativeLayout consultantLayout;

    private LinearLayout btnLoginOut;   //退出
    private Subscription mSubscription;
    private UserInfo userInfo;


//    private TextView tvIdAuthenticateInfo;

    private View newVersionView;

    private Subscription subscription;

    private String TAG = MeFragment.class.getSimpleName();

    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台

    ForegroundColorSpan foregroundColorSpan;
    ForegroundColorSpan foregroundColorSpan1;
    private Drawable defaultDrawable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginProvider.getInstance().addLoginCallback(this);
        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (UserDataUtil.isLogin()) {
                    refreshUserInfo();
                    getMySpaceStatistic();
                }
            }
        });
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.membership_color));
        foregroundColorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.burnt_orange));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_home_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
        initTitle();
        initView(view);
        tvTitle.setText("我的");
        cvtMyOrder.setOnClickListener(this);
        cvtMyPick.setOnClickListener(this);
        ctvUserInfo.setOnClickListener(this);
        tvValidity.setOnClickListener(this);
        ctvFree.setOnClickListener(this);
        llmember.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        tvMemberName.setOnClickListener(this);
        ivMembership.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        myOrderSigning.setOnClickListener(this);
        deposit.setOnClickListener(this);
        itWaitingPay.setOnClickListener(this);
        pickingUpGoods.setOnClickListener(this);
        itConfirmLanding.setOnClickListener(this);
        consultantLayout.setOnClickListener(this);
        nestedScrollView.setScrollViewSlideListener(new MyScrollView.ScrollViewSlideListener() {
            @Override
            public void scrollChanged(int l, int t, int oldl, int oldt) {
                float percentY = (float) t / 150f;
                if (percentY > 1.0f) {
                    if (titleView.getAlpha() <= 1.0f) {
                        titleView.setAlpha(1.0f);
                    }
                } else if (percentY < 1.0f) {
                    titleView.setAlpha(percentY);
                }
            }
        });

    }

    private void callNumber() {
        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        if (getActivity().checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
            ((MainActivity) getActivity()).showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().requestPermissions(permission, Constants.REQUEST_PERMISSION_CALL_PHONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (UserDataUtil.isLogin())
                refreshUserInfo();
        }
    }


    @Override
    public void onDestroy() {
        mSubscription = RxUtil.unsubscribe(mSubscription);
        RxUtil.remove(subscription);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
        LoginProvider.getInstance().removeLoginCallback(this);
        super.onDestroy();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        View line = getActivity().findViewById(R.id.line);
        if (line != null)
            line.setVisibility(View.GONE);
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        newVersionView = view.findViewById(R.id.view_new_version_mark);
        //全部订单

        RelativeLayout rlMessage = (RelativeLayout) view.findViewById(R.id.message_layout);//消息列表
        rlMessage.setOnClickListener(this);
        RelativeLayout relativeLayoutMemberShipIntroduction = (RelativeLayout) view.findViewById(R.id.membership_level_layout);
        relativeLayoutMemberShipIntroduction.setOnClickListener(this);
        RelativeLayout relativeLayoutAboutBuyBeans = (RelativeLayout) view.findViewById(R.id.about_app_layout);
        relativeLayoutAboutBuyBeans.setOnClickListener(this);
        RelativeLayout relativeLayoutMyId = (RelativeLayout) view.findViewById(R.id.id_authenticate_layout);
        relativeLayoutMyId.setOnClickListener(this);
        btnLoginOut = (LinearLayout) view.findViewById(R.id.ll_login_out);
        btnLoginOut.setOnClickListener(this);
        CustomFontTextView btnLoginOut = (CustomFontTextView) view.findViewById(R.id.login_out);
        btnLoginOut.setOnClickListener(this);
        newVersionView.setVisibility(SPUtil.getBoolean(Constants.KEY_NEED_UPDATE) ? View.VISIBLE : View.INVISIBLE);
        refreshUI();


        mSubscription = RxUtil.unsubscribe(mSubscription);
        mSubscription = LoginState.getObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean bDone) {
                if (bDone && UserDataUtil.isLogin()) {
                    refreshUI();
                    getMySpaceStatistic();
                }
            }
        });

    }


    @SuppressWarnings("deprecation")
    private void certifyId() {
        if (null != userInfo) {
            if (userInfo.getType().equalsIgnoreCase("admin")) {
                ctvUserInfo.setVisibility(View.VISIBLE);
                if (userInfo.getEnterStatus().equalsIgnoreCase("SUCCESS")) {
                    ctvUserInfo.setText(String.format(getString(R.string.admin_success), userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? getString(R.string.person1) : getString(R.string.company2)));
                } else if (userInfo.getEnterStatus().equalsIgnoreCase("PROCESS")) {
                    ctvUserInfo.setText(getString(R.string.admin_process));
                } else if (userInfo.getEnterStatus().equalsIgnoreCase("INIT")) {
                    ctvUserInfo.setText(getString(R.string.admin_init));
                } else if (userInfo.getEnterStatus().equalsIgnoreCase("FAIL")) {
                    ctvUserInfo.setText(getString(R.string.admin_fail));
                }
            } else {
                ctvUserInfo.setVisibility(View.VISIBLE);
                ctvUserInfo.setText(String.format(getString(R.string.child_success), userInfo.getTypeName()));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserDataUtil.isLogin())
            refreshUserInfo();
    }

    @SuppressWarnings("deprecation")
    private void refreshUI() {
        if (UserDataUtil.isLogin()) {
            userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
            btnLoginOut.setVisibility(View.VISIBLE);
            ctvFree.setVisibility(View.GONE);
            if (userInfo == null) {
                return;
            }
            if (!TextUtils.isEmpty(userInfo.getGradeEndTime())) {
                tvValidity.setGravity(Gravity.LEFT);
                tvValidity.setText(new String("").concat(userInfo.getGradeEndTime()));
            }

            getMySpaceStatistic();
            tvUserName.setTextColor(getResources().getColor(R.color.membership_color));
            if ("SUCCESS".equalsIgnoreCase(userInfo.getEnterStatus())) {
                if ("PERSONAL".equalsIgnoreCase(userInfo.getEnterType())) {
                    tvUserName.setText(TextUtils.isEmpty(userInfo.getName()) ? userInfo.getMobile() : userInfo.getName());
                } else {
                    tvUserName.setText(TextUtils.isEmpty(userInfo.getEnterName()) ? userInfo.getMobile() : userInfo.getEnterName());
                }
                ctvUserInfo.setText(String.format(getString(R.string.admin_success), userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? getString(R.string.person1) : getString(R.string.company2)));
            } else {
                tvUserName.setText(userInfo.getMobile());
                ctvUserInfo.setText("认证身份并绑卡后，就可买豆粕");
            }

            if (null != userInfo.getGradeName()) {
                tvMemberName.setText(userInfo.getGradeName());
            }
            if (!TextUtils.isEmpty(userInfo.getGradePic())) {
                ivMembership.setVisibility(View.VISIBLE);
                PicassoImageLoader.getInstance().displayNetImage(activity, userInfo.getGradePic(), ivMembership, defaultDrawable);
            } else {
                ivMembership.setVisibility(View.INVISIBLE);
            }
            if(userInfo.getServiceLevel() == null && userInfo.getServiceStatus() == null && userInfo.getCounselorStatus() == null){
                consultantLayout.setVisibility(View.GONE);
            }else{
                consultantLayout.setVisibility(View.VISIBLE);
            }
            certifyId();

        } else {
            ivMembership.setVisibility(View.GONE);
            tvUserName.setText(R.string.my_not_login_username_text);
            tvUserName.setTextColor(getResources().getColor(R.color.gray_chateau));
            btnLoginOut.setVisibility(View.INVISIBLE);
            ctvUserInfo.setText("点击登录/注册");
            myOrderSigning.setText("待签署合同");
            deposit.setText("待支付");
            pickingUpGoods.setText("待提货");
            itWaitingPay.setText("待支付货款");
            itConfirmLanding.setText("待确认收货");
            tvMemberName.setText(null);
            tvValidity.setText("了解会员权益");
            tvValidity.setGravity(Gravity.RIGHT);
            ctvFree.setVisibility(View.VISIBLE);
            consultantLayout.setVisibility(View.GONE);

        }
    }

    public void refreshUserInfo() {
        Map<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(SPUtil.getString(Constants.KEY_TOKEN_ID))) {
            UserDataUtil.clearUserData();
            openActivity(LoginActivity.class);
            return;
        }
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RefreshUserInfoService refreshUserInfoService = ApiServiceGenerator.createService(RefreshUserInfoService.class);
        subscription = refreshUserInfoService
                .refreshUserInfo(map)
                .map(new NetworkResultFunc1<UserInfo>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<UserInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        if (SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, userInfo)) {
                            refreshUI();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiCodeErrorException) {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(getContext(), ((ApiCodeErrorException) throwable).getState(), throwable.getMessage());
                        } else {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(getContext(), "", throwable.getMessage());
                        }
                    }
                });
        RxUtil.addSubscription(subscription);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.membership_level_layout) {
            callNumber();
            return;
        }

        if (v.getId() == R.id.tv_validity || v.getId() == R.id.ctv_free || v.getId() == R.id.iv_membership || v.getId() == R.id.iv_more || v.getId() == R.id.tv_member_name || v.getId() == R.id.ll_member) {
            openActivity(MemberShipIntroductionActivity.class);
            return;
        }
        if (v.getId() == R.id.about_app_layout) {
            openActivity(AboutBuyBeansActivity.class);
            return;
        }
        if (!UserDataUtil.isLogin() && v.getId() == R.id.tv_user_name) {
            openActivity(LoginActivity.class);
            return;
        }
        if (!UserDataUtil.isLogin() && v.getId() != R.id.message_layout) {
            openActivity(LoginActivity.class);
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.my_info_layout:
//                openActivity(MemberShipIntroductionActivity.class);
                callNumber();
                break;
            case R.id.my_order_signing:
                bundle.putString("orderStatus", Constants.NEW_BUY_CONFIRMING);
                openActivity(OrderListActivity.class, bundle);
                break;
            case R.id.deposit:
                bundle.putString("orderStatus", Constants.NEW_BUY_DEPOSITING);
                openActivity(OrderListActivity.class, bundle);
                break;
            case R.id.pickingUpGoods:
                bundle.putString("orderStatus", Constants.OPEN_BUY_PICK);
                openActivity(OrderListActivity.class, bundle);
                break;

            case R.id.it_waiting_pay:
                //等待支付货款订单
                bundle.putString("deliveryStatus", Constants.UNPAY);
                openActivity(DeliveryListActivity.class, bundle);
                break;
            case R.id.it_confirm_landing:
                bundle.putString("deliveryStatus", Constants.SELLER_CONFIRMED);
                openActivity(DeliveryListActivity.class, bundle);
                break;
            case R.id.cvt_my_pick:

                openActivity(DeliveryListActivity.class, bundle);
                break;
            case R.id.message_layout:
                openActivity(MessageListActivity.class, bundle);
                break;
//            case R.id.remind_layout:
//                openActivity(MessageSettingActivity.class, bundle);
//                break;
            case R.id.cvt_my_order:
                bundle.putString("orderViewStatus", "");
                openActivity(OrderListActivity.class, bundle);
                break;
            case R.id.login_out:
            case R.id.ll_login_out:
                ConfirmDialog.createConfirmDialog(getActivity(), "是否退出登录？", getString(R.string.exit), getString(R.string.balance_recharge_cancel), new ConfirmDialog.OnBtnConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        UserDataUtil.clearUserData();
                        RxBus.get().post(Constants.REPORT_CLEAR, new Boolean(true));
                        RxBus.get().post("inOrOutAction", new Boolean(true));
                        refreshUI();
                        SPUtil.clearObjectFromShare(Constants.KEY_USER_INFO);
                        SPUtil.saveObjectToShare(Constants.KEY_LOCAL_INFO, new TempInfoInputForIdCertify());
                        SPUtil.saveObjectToShare(Constants.KEY_PERSON_CERTIFY, new TempInfoInputForIdCertify());
                        //退出网易云信
                        NimSessionHelper.getInstance().loginOut();
                    }
                }, new ConfirmDialog.OnBtnConfirmListener() {
                    @Override
                    public void onConfirmListener() {

                    }
                });
                break;

            case R.id.username:
                if (!UserDataUtil.isLogin()) {
                    openActivity(LoginActivity.class);
                } else {
                    if (userInfo.getType().equalsIgnoreCase("admin")) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putBoolean("isPersonal", userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? true : false);
                        switch (userInfo.getEnterStatus()) {
                            case "SUCCESS":
                                openActivity(userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? PersonAuthenticateActivity.class : CompanyAuthenticateActivity.class);
                                return;
                            case "INIT":
                            case "PROCESS":
                            case "FAIL":
                                bundle1.putString("identificationState", userInfo.getEnterStatus());
                                break;
                        }
                        openActivity(AuthenticateActivity.class, bundle1);
                    } else {
                        openActivity(ChildAccountIdActivity.class);
                    }
                }
                break;
            case R.id.consultant_layout:
                openActivity(ConsultantSettingActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 获取个人中心概要统计信息
     * //    "buyDeliveryPayed": "3",待确认收货
     * //    "buyDeliveryUnpay": "0",未支付
     * //    "newBuyConfirming": "0",待签署合同
     * //    "newBuyDepositing": "0",待支付保证金
     * //    "openBuyPick": "0",待提货
     * //    "openBuyPicking": "0"提货中
     */
    public void getMySpaceStatistic() {
        HashMap<String, String> params = new HashMap<String, String>();
        if (TextUtils.isEmpty(SPUtil.getString(Constants.KEY_TOKEN_ID))) {
            UserDataUtil.clearUserData();
            openActivity(LoginActivity.class);
            return;
        }
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_MY_SPACE_STATISTIC, params, new HttpListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    SpaceStatistic spaceStatistic = new Gson().fromJson(response.get(Constants.RESPONSE_CONTENT).getAsJsonObject(), new TypeToken<SpaceStatistic>() {
                    }.getType());
                    if (spaceStatistic == null) return;
                    if (checkShowMessage(spaceStatistic.getNewBuyConfirming())) {
                        SpannableString spannableString = new SpannableString("待签署合同".concat(spaceStatistic.getNewBuyConfirming()));
                        spannableString.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan1, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        myOrderSigning.setText(spannableString);
                    } else {
                        myOrderSigning.setText("待签署合同");

                    }
                    if (checkShowMessage(spaceStatistic.getNewBuyDepositing())) {
                        SpannableString spannableString = new SpannableString("待支付".concat(spaceStatistic.getNewBuyDepositing()));
                        spannableString.setSpan(foregroundColorSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan1, 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        deposit.setText(spannableString);
                    } else {
                        deposit.setText("待支付");
                    }
                    if (checkShowMessage(spaceStatistic.getOpenBuyPick())) {
                        SpannableString spannableString = new SpannableString("待提货".concat(spaceStatistic.getOpenBuyPick()));
                        spannableString.setSpan(foregroundColorSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan1, 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    } else {
                        pickingUpGoods.setText("待提货");
                    }

                    if (checkShowMessage(spaceStatistic.getBuyDeliveryUnpay())) {
                        SpannableString spannableString = new SpannableString("待支付货款".concat(spaceStatistic.getBuyDeliveryUnpay()));
                        spannableString.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan1, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        itWaitingPay.setText(spannableString);
                    } else {
                        itWaitingPay.setText("待支付货款");
                    }

                    if (checkShowMessage(spaceStatistic.getBuyDeliveryPayed())) {
                        SpannableString spannableString = new SpannableString("待确认收货".concat(spaceStatistic.getBuyDeliveryPayed()));
                        spannableString.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan1, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        itConfirmLanding.setText(spannableString);
                    } else {
                        itConfirmLanding.setText("待确认收货");
                    }
                    ((MainActivity) getActivity()).setMeTabHostRedPot(spaceStatistic, false);
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(getContext(), errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false);
    }

    private boolean checkShowMessage(String message) {
        return !TextUtils.isEmpty(message) && !"0".equals(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void nimLoginSucceed() {

    }

    @Override
    public void nimLoginFail(String failStr) {

    }

    @Override
    public void nimKicked() {
        refreshUI();
    }
}