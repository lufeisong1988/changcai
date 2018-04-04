package com.changcai.buyer.ui.quote;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.authenticate.AuthenticateActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.NetThrowableFiltrateFunc;
import com.changcai.buyer.interface_api.OrderService;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.ui.base.BaseTouchBackActivity;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.TimeTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.juggist.commonlibrary.rx.LoginState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 报价详情页
 */
public class QuoteDetailActivity extends BaseTouchBackActivity implements View.OnClickListener, TimeTextView.ITimerViewCallBack, RuntimePermission.PermissionCallbacks {
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnLeft)
    TextView btnLeft;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnRight)
    TextView btnRight;
    @BindView(R.id.iv_service_phone)
    ImageView ivServicePhone;
    @BindView(R.id.iv_btn_right)
    ImageView ivBtnRight;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.price_text)
    TextView priceText;
    @BindView(R.id.price)
    TextView tvPrice;
    @BindView(R.id.tv_storage_and_start_less)
    TextView tvStorageAndStartLess;
    @BindView(R.id.iv_reduce)
    ImageView ivReduce;
    @BindView(R.id.et_buy_pond)
    EditText etBuyPond;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_cash_deposit)
    TextView tvCashDeposit;
    @BindView(R.id.place)
    TextView place;
    @BindView(R.id.deliveryTime)
    TextView deliveryTime;
    @BindView(R.id.tv_product_info_details)
    TextView tvProductInfoDetails;
    @BindView(R.id.tv_contact_way)
    TextView tvContactWay;
    @BindView(R.id.tv_seller_company_info)
    TextView tvSellerCompanyInfo;
    @BindView(R.id.order)
    TextView order;
    @BindView(R.id.tv_insurance_deposit)
    TextView tvInsuranceDeposit;
    @BindView(R.id.nested_scrollview)
    NestedScrollView nestedScrollview;
    @BindView(R.id.iv_invalid)
    ImageView ivInvalid;
    @BindView(R.id.tv_company_name)
    CustomFontTextView tvCompanyName;
    @BindView(R.id.ll_contract_seller)
    LinearLayout llContractSeller;
    @BindView(R.id.tv_deposit_info)
    TextView tvBuyerDepositInfo;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    @BindView(R.id.ll_order_layout)
    LinearLayout llOrderLayout;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    @BindView(R.id.tv_empty_action)
    TextView tvEmptyAction;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;
    @BindView(R.id.ctv_publish_time)
    CustomFontTextView ctvPublishTime;
    @BindView(R.id.orderProgress)
    RotateDotsProgressView orderProgress;
    //productDetailId == priceId
    private String productDetailId;
    private PriceInfo priceInfo;
    private Subscription mSubscription;
    //    private int insuranceValue;
    private BigDecimal insuranceValue;
    private int maxStorage;
    private int minBuyNum;
    private long minSpace = 10;
    private BigDecimal bigDecimalBuyer;
    private BigDecimal bigDecimalSeller;
    private ViewTreeObserver.OnGlobalLayoutListener myViewTreeObserver;
    View rootView;
    private static final int ORDER_PRICE_DATA_CHANGED = 9002;
    private static final int LIMIT = 9003;
    private static final int BASIS_NOT_SUPPORT = 9004;
    public static final int COMMON_ERROR_CODE = 9005;
    private static final int NOT_SUPPORT_BUY_SELF_CODE = 9006;
    private static final int AUTHENTICATE = 9008;
    private static final int PRODUTE_INFOMATION_CHANGED = 9007;
    public static final int NOT_SUPPORT_ALL_PAY = 9009;
    private static final int ORDER_PRICE_QUANTITY_NOT_ENOUGH = 9010;

    private Subscription validSubscription;
    private boolean clickButNotShowChooseDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);
        ButterKnife.bind(this);

        nestedScrollview.setFillViewport(true);
        nestedScrollview.setNestedScrollingEnabled(true);

        setListenerToRootView();
        etBuyPond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()))
                    countBuyerDeposit(Integer.valueOf(s.toString()));
            }
        });
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            productDetailId = bundle.getString("detailId");
        } else {
            productDetailId = SPUtil.getObjectFromShare(Constants.KEY_JPUSH_QUOTATIONID);
            SPUtil.clearObjectFromShare(Constants.KEY_JPUSH_QUOTATIONID);
        }
        initTitle();
        initView();

        getProductDetail(productDetailId);
        mSubscription = RxUtil.unsubscribe(mSubscription);
        mSubscription = LoginState.getObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean bDone) {
                if (bDone && UserDataUtil.isLogin()) {
                    setPriceInfo(priceInfo);
                    if (clickButNotShowChooseDialog) {
                        showChooseDialog("拨打电话：" + priceInfo.getContactPhone());
                    }
                }
            }
        });
        // showTimeOutView();

        // 500 MILLISECONDS one first action
        RxView.clicks(order).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserDataUtil.isLogin()) {
                    UserInfo u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                    if (!("SUCCESS".equalsIgnoreCase(u.getEnterStatus()) && "SUCCESS".equalsIgnoreCase(u.getBankSignStatus()) && Boolean.parseBoolean(u.getPayPassword()) && Boolean.parseBoolean(u.getIsContracted()) && (!TextUtils.isEmpty(u.getBuyerInformation()) || !TextUtils.isEmpty(u.getSellerInformation())))) {
                        showErrorDialog(getString(R.string.notice), getString(R.string.pleanse_input_id_certify), getString(R.string.cancel_string), getString(R.string.affirm), AUTHENTICATE);
                        return;
                    }
                    if (UserDataUtil.isFinance() || UserDataUtil.isTransaction()) {
                        showErrorDialog(getString(R.string.limit_enough_contact), "", getString(R.string.balance_recharge_cancel), LIMIT);
                        return;
                    }
                    //不支持基差报价
                    if (priceInfo.getProductType().contentEquals("BASIS")) {
                        showErrorDialog(getString(R.string.mobile_limit), "", getString(R.string.label_confirm), BASIS_NOT_SUPPORT);
                        return;
                    }

                    if (UserDataUtil.isAuth() || "success".equalsIgnoreCase(priceInfo.getUserEnterStatus())) {
                        if (UserDataUtil.isBuyer() || "true".equalsIgnoreCase(priceInfo.getUserIsBuyer())) {
                            validCreateOrder();
                        } else {
                            ConfirmDialog.createConfirmDialog(QuoteDetailActivity.this, "无权限操作，请联系管理员或买豆粕网021-54180251", "取消");
                        }
                    } else if (UserDataUtil.isProcessAuth() || "process".equalsIgnoreCase(priceInfo.getUserEnterStatus())) {
                        ConfirmDialog.createConfirmDialog(QuoteDetailActivity.this, "买家认证正在审核中，审核通过后才可下单和购买", "取消");
                    } else {
                        gotoActivity(ComputerOperationActivity.class);
                    }
                } else {
                    gotoActivity(LoginActivity.class);
                }
            }
        });
    }

    private void setListenerToRootView() {
        rootView = getWindow().getDecorView();
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        myViewTreeObserver = new ViewTreeObserver.OnGlobalLayoutListener() {

            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                // Retrieve visible rectangle inside window.
                rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                // Decide whether keyboard is visible from changing decor view height.
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
//                            listener.onKeyboardShown(currentKeyboardHeight);
                        verifyInputNumber(etBuyPond.getText().toString());
                        etBuyPond.requestFocus();
                        etBuyPond.setCursorVisible(true);

                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        // Notify listener about keyboard being hidden.
                        etBuyPond.setCursorVisible(false);
                        verifyInputNumber(etBuyPond.getText().toString());
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }


        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(myViewTreeObserver);
    }


    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivReduce.setEnabled(false);
        findViewById(R.id.view_bottom_line).setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void setPriceInfo(final PriceInfo priceInfo) {
        if (priceInfo == null)
            return;
        Float floatMaxStorage = new Float(priceInfo.getInventory());
        maxStorage = floatMaxStorage.intValue();
        minBuyNum = Integer.parseInt(priceInfo.getMinPurchaseNum());
        etBuyPond.setText(priceInfo.getMinPurchaseNum());
        etBuyPond.setSelection(priceInfo.getMinPurchaseNum().length());
        etBuyPond.setCursorVisible(false);
        verifyInputNumber(etBuyPond.getText().toString());
        etBuyPond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    if (s.toString().startsWith("0")) {
                        s.delete(0, 1);
                    } else {
                        int inputBuyNum = Integer.parseInt(s.toString());
                        if (inputBuyNum >= maxStorage) {
                            ivReduce.setEnabled(true);
                            ivAdd.setEnabled(false);
                            etBuyPond.removeTextChangedListener(this);
                            String inputString = maxStorage + "";
                            etBuyPond.setText(inputString);
                            etBuyPond.setSelection(inputString.length());
                            etBuyPond.addTextChangedListener(this);
                        }
                    }
                }
            }
        });

        //单价区分 报价类型 - 一口价／基差
        if (Constants.SPOT.equals(priceInfo.getProductType())) {
            tvPrice.setText("¥ ".concat(priceInfo.getPrice()));
        } else {
            //基差
            if (Integer.valueOf(priceInfo.getPrice()) > 0) {
                tvPrice.setText(priceInfo.getBasisCode() + "+ ¥ " + priceInfo.getPrice());
            } else if (Integer.valueOf(priceInfo.getPrice()) == 0) {
                tvPrice.setText(priceInfo.getBasisCode().concat("+ ¥ ").concat("0"));
            } else {
                tvPrice.setText(priceInfo.getBasisCode() + "- ¥ " + priceInfo.getPrice().substring(1, priceInfo.getPrice().length()));
            }
        }

        //库存
        tvStorageAndStartLess.setText(getString(R.string.order_buyer_rule, priceInfo.getInventory(), priceInfo.getMinPurchaseNum()));
        PicassoImageLoader.getInstance().displayNetImage(QuoteDetailActivity.this, priceInfo.getEnterPic(), icon, ContextCompat.getDrawable(QuoteDetailActivity.this, R.mipmap.no_network_2));
        setDepositRate(priceInfo);
        setCompanyInfo(priceInfo);
        if ("INVALID".equalsIgnoreCase(priceInfo.getPriceStatus())) {
            showTimeOutView();
        }

        //mock
//        order.performClick();
//        order.performClick();


    }

    /**
     * 企业信息
     *
     * @param priceInfo
     */
    @SuppressWarnings("deprecation")
    private void setCompanyInfo(PriceInfo priceInfo) {
        tvCompanyName.setText(priceInfo.getEnterName());
        tvContactWay.setText(UserDataUtil.isLogin() ? priceInfo.getContactPhone() : "登录后显示联系方式");
        tvSellerCompanyInfo.setText(priceInfo.getEnterInformation());
        tvProductInfoDetails.setText(priceInfo.getFactoryBrand().concat("/").concat(priceInfo.getEggSpec()).concat("/").concat(priceInfo.getPackType()));
        deliveryTime.setText(priceInfo.getDeliveryStartTime().concat("至").concat(priceInfo.getDeliveryEndTime()));
        place.setText(priceInfo.getRegion().concat(" ").concat(priceInfo.getLocation()).concat(getString(R.string.buyer_pick_by_self)));
        ctvPublishTime.setText(priceInfo.getPublishTime().concat(" 发布"));
    }

    @SuppressWarnings("deprecation")
    private void setDepositRate(PriceInfo priceInfo) {
        Double dBuyer = 10000.0;
        Double rateBuyer = Double.parseDouble(priceInfo.getBuyerDepositRate());

        if (rateBuyer.compareTo(0.0) == 0) {
            bigDecimalBuyer = new BigDecimal(0);
        } else {
            bigDecimalBuyer = new BigDecimal(100).divide(new BigDecimal(dBuyer / rateBuyer));
        }

//        tvBuyerDeposit.setText(spannableStringBuyerDeposit);
        //卖家保证金比例
        Double d = 10000.0;
        Double rate = Double.parseDouble(priceInfo.getDepositRate());
        if (rate.compareTo(0.0) == 0) {
            bigDecimalSeller = new BigDecimal(0);
        } else {
            bigDecimalSeller = new BigDecimal(100).divide(new BigDecimal(d / rate));
//            bigDecimalSeller = new BigDecimal(100).divide(new BigDecimal(d / rate), 0, BigDecimal.ROUND_HALF_UP);
        }
        ForegroundColorSpan foregroundColorSpanBuyerDepositGray = new ForegroundColorSpan(getResources().getColor(R.color.storm_gray));
        ForegroundColorSpan foregroundColorSpanBuyerDepositGreen = new ForegroundColorSpan(getResources().getColor(R.color.dark_pastel_green));
        SpannableString spannableStringAll = new SpannableString(getString(R.string.all_deposit, bigDecimalBuyer.toString().concat("%"), bigDecimalSeller.toString().concat("%")));

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.dark_pastel_green));
        spannableStringAll.setSpan(foregroundColorSpanBuyerDepositGray, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringAll.setSpan(foregroundColorSpanBuyerDepositGreen, 8, String.valueOf(bigDecimalBuyer.intValue()).length() > 1 ? 11 : 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringAll.setSpan(foregroundColorSpanBuyerDepositGray, String.valueOf(bigDecimalBuyer.intValue()).length() > 1 ? 11 : 10,
                String.valueOf(bigDecimalBuyer.intValue()).length() > 1 ? 11 : 10 + 6,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringAll.setSpan(foregroundColorSpan, String.valueOf(bigDecimalSeller.intValue()).length() > 1 ? spannableStringAll.length() - 4 : spannableStringAll.length() - 3, spannableStringAll.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringAll.setSpan(foregroundColorSpanBuyerDepositGray, spannableStringAll.length() - 1, spannableStringAll.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCashDeposit.setText(spannableStringAll);
    }


    private void verifyInputNumber(String inputNumber) {
        if (TextUtils.isEmpty(inputNumber)) {
            ivReduce.setEnabled(false);
            ivAdd.setEnabled(true);
            etBuyPond.setText(minBuyNum + "");
            etBuyPond.setSelection(String.valueOf(minBuyNum).length());
            countBuyerDeposit(minBuyNum);
            return;
        }
        int inputBuyNum = Integer.parseInt(inputNumber.toString());
        if (inputBuyNum > minBuyNum && inputBuyNum < maxStorage) {
            ivReduce.setEnabled(true);
            ivAdd.setEnabled(true);
            etBuyPond.setText(inputBuyNum + "");
        } else if (inputBuyNum >= maxStorage) {
            ivReduce.setEnabled(true);
            ivAdd.setEnabled(false);
            etBuyPond.setText(maxStorage + "");
        } else if (inputBuyNum <= minBuyNum) {
            ivReduce.setEnabled(false);
            ivAdd.setEnabled(true);
            etBuyPond.setText(minBuyNum + "");
        }
        etBuyPond.setSelection(etBuyPond.getText().toString().length());

        countBuyerDeposit(Integer.parseInt(etBuyPond.getText().toString()));
    }

    private void countBuyerDeposit(int ponds) {
        if (priceInfo == null) return;
        BigDecimal price = new BigDecimal(priceInfo.getPrice());
        if (Constants.BASIS.equals(priceInfo.getProductType())) {
            price = new BigDecimal(Long.parseLong(priceInfo.getClosingPrice()) + Long.parseLong(priceInfo.getPrice()));
        }
        long totalValue = price.multiply(new BigDecimal(ponds)).longValue();
//        Double d = 10000.0;
        Double rate = Double.parseDouble(priceInfo.getBuyerDepositRate());

        if (rate.compareTo(0.0) == 0) {
            bigDecimalBuyer = new BigDecimal(0);
        } else {
//            bigDecimalBuyer = new BigDecimal(100).divide(new BigDecimal(d / rate), 0, BigDecimal.ROUND_HALF_UP);
            bigDecimalBuyer = new BigDecimal(100).divide(new BigDecimal("10000.0").divide(new BigDecimal(priceInfo.getBuyerDepositRate()), 2, BigDecimal.ROUND_HALF_UP), 2, BigDecimal.ROUND_HALF_UP);
        }

        if (bigDecimalBuyer.compareTo(new BigDecimal(0)) == 0) {
            insuranceValue = new BigDecimal("0.00");
        } else {
            insuranceValue = new BigDecimal(totalValue).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).multiply(bigDecimalBuyer);
        }
        String priceUnit;
        if (Constants.BASIS.equals(priceInfo.getProductType())) {
            if (Integer.valueOf(priceInfo.getPrice()) > 0) {
                priceUnit = priceInfo.getClosingPrice() + "+" + priceInfo.getPrice();
            } else {
                priceUnit = priceInfo.getClosingPrice() + "-" + priceInfo.getPrice();
            }
            tvBuyerDepositInfo.setText(getString(R.string.buyer_deposit_money_base2, priceUnit, "" + ponds, StringUtil.formatForMoneyNoDot(bigDecimalBuyer.toString()).concat("%"), StringUtil.formatForMoney(insuranceValue + "")));
        } else {
            tvBuyerDepositInfo.setText(getString(R.string.buyer_deposit_money2, price.toString(), "" + ponds, StringUtil.formatForMoneyNoDot(bigDecimalBuyer.toString()).concat("%"), StringUtil.formatForMoney(insuranceValue + "")));
        }
        tvInsuranceDeposit.setText("¥" + StringUtil.formatForMoney(insuranceValue + ""));
    }


    /**
     * 显示过时后的View
     */
    private void showTimeOutView() {
        ivAdd.setEnabled(false);
        ivReduce.setEnabled(false);
        order.setEnabled(false);
        order.setText(R.string.invalid);
        ivInvalid.setVisibility(View.VISIBLE);
        llOrderLayout.setBackgroundResource(R.drawable.disable_order);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription = RxUtil.unsubscribe(mSubscription);
        rootView.getViewTreeObserver().removeGlobalOnLayoutListener(myViewTreeObserver);
        RxUtil.remove(validSubscription);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 显示拨打电话的dialog
     */
    private void showChooseDialog(final String phone) {
        View view = getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(this, R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText(phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = phone.length() > 13 ? phone.substring(5, phone.length()) : phone;
                clickButNotShowChooseDialog = false;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + s));
                try {
                    QuoteDetailActivity.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onTimerOut() {  //倒计时完成的回调
        showTimeOutView();
    }

    /**
     * 获取产品报价详情页信息
     */
    private void getProductDetail(String productDetailId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", productDetailId);
        showProgress();
        if (UserDataUtil.isLogin())
            params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));


        VolleyUtil.getInstance().httpPost(this, Urls.GET_PRODUCT_DETAIL, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                disMissProgress();
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {

                    Gson gson = new Gson();
                    priceInfo = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), PriceInfo.class);
                    setPriceInfo(priceInfo);
                    long currentTime = System.currentTimeMillis();
                    if (!TextUtils.isEmpty(priceInfo.getCurrentTimeMillis()))
                        currentTime = System.currentTimeMillis();
                    long diff = SystemClock.elapsedRealtime() + (DateUtil.stringToDate(null, priceInfo.getPriceEndTime()).getTime()) - currentTime;
                    if (diff > 0) {

                    } else {
                        showTimeOutView();
                    }
                } else {
                    showRequestFail();
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(QuoteDetailActivity.this, getString(R.string.net_work_exception), errorCode);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                disMissProgress();
                showRequestFail();
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(QuoteDetailActivity.this, getString(R.string.network_unavailable));
            }

        }, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RuntimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(List<String> perms) {
        showChooseDialog("拨打电话：" + priceInfo.getContactPhone());
    }

    public void showErrorDialog(String title, String message, String buttonName, final int actionCode) {
        ConfirmDialog.createConfirmDialog(QuoteDetailActivity.this, message, title, buttonName, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                switch (actionCode) {
                    case PRODUTE_INFOMATION_CHANGED:
                    case ORDER_PRICE_QUANTITY_NOT_ENOUGH:
                        getProductDetail(productDetailId);
                        break;
                    case NOT_SUPPORT_ALL_PAY:
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        Toast.makeText(this, R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }

    /**
     * 不满足条件 去电脑页面
     *
     * @param title
     * @param errorMessage
     */
    public void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, final int action) {
        ConfirmDialog.createConfirmDialogCustomButtonString(QuoteDetailActivity.this, title, errorMessage, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (action == ORDER_PRICE_DATA_CHANGED) {
                    getProductDetail(priceInfo.getId());
                } else if (action == LIMIT) {

                } else if (action == NOT_SUPPORT_BUY_SELF_CODE) {
                    QuoteDetailActivity.this.finish();
                } else if (action == AUTHENTICATE) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putBoolean("isPersonal", UserDataUtil.userEnterType().equalsIgnoreCase("PERSONAL") ? true : false);
                    bundle1.putString("identificationState", UserDataUtil.userEnterStatus());
                    gotoActivity(AuthenticateActivity.class, bundle1);
                } else {
                    gotoActivity(ComputerOperationActivity.class);
                }
            }
        }, leftButtonText, rightButtonText, false);
    }

    private void showOrderProgress() {
        order.setVisibility(View.GONE);
        orderProgress.setVisibility(View.VISIBLE);
        orderProgress.showAnimation(true);
    }

    private void disMissOrderProgress() {
        order.setVisibility(View.VISIBLE);
        orderProgress.setVisibility(View.GONE);
        orderProgress.refreshDone(true);
    }

    private void validCreateOrder() {
        showOrderProgress();
        final OrderService orderService = ApiServiceGenerator.createService(OrderService.class);
        Map<String, String> validParameter = new HashMap<>();
        validParameter.put("priceId", priceInfo.getId());
        validParameter.put("quantity", etBuyPond.getText().toString());
        Float f = Float.parseFloat(priceInfo.getPrice()) * 100;
        validParameter.put("price", String.valueOf(f.intValue()));
        if (priceInfo.getProductType().equalsIgnoreCase("BASIS")) {
            validParameter.put("basisCode", priceInfo.getBasisCode());
        }
        validParameter.put("buyerDepositRate", priceInfo.getBuyerDepositRate());
        validParameter.put("sellerDepositRate", priceInfo.getDepositRate());

        validParameter.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        validParameter.put("updateTime", priceInfo.getUpdateTime());

        RxUtil.remove(validSubscription);
        validSubscription = orderService.validCreateOrder(validParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new NetThrowableFiltrateFunc<BaseApiModel<String>>())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        disMissOrderProgress();
                        if (stringBaseApiModel.getErrorCode().contentEquals("0")) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("detail", priceInfo);
                            bundle.putInt("ton", Integer.parseInt(etBuyPond.getText().toString()));
                            bundle.putString("insuranceValue", insuranceValue.toString());
                            bundle.putString("sellerDeposit", bigDecimalSeller.toString().concat("%"));
                            bundle.putString("buyerDeposit", bigDecimalBuyer.toString().concat("%"));
                            gotoActivity(OrderActivity.class, bundle);
                        } else {
                            throw new ApiCodeErrorException(stringBaseApiModel.getErrorCode(), stringBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {


                    @Override
                    public void call(Throwable throwable) {
                        /**
                         * 如果有定义的业务错误就提示
                         * 否则全部处理为网络错误
                         */

                        disMissOrderProgress();
                        if (throwable instanceof ApiCodeErrorException) {
                            if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_DATA_CHANGED")) {
                                showErrorDialog(getString(R.string.price_data_changed), throwable.getMessage(), getString(R.string.label_confirm), ORDER_PRICE_DATA_CHANGED);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_BUYSELF_ERROR")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.balance_recharge_cancel), getString(R.string.look_other_price_info), NOT_SUPPORT_BUY_SELF_CODE);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_INVALID")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.balance_recharge_cancel), getString(R.string.look_other_price_info), NOT_SUPPORT_BUY_SELF_CODE);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("UPDATETIME_INVALID")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.label_confirm), COMMON_ERROR_CODE);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("PRODUCT_INFOMATION_CHANGED")) {
                                showErrorDialog(throwable.getMessage(), "", "刷新", PRODUTE_INFOMATION_CHANGED);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("NOT_SUPPORT_ALL_PAY")) {
                                showErrorDialog("支付账户不匹配", throwable.getMessage(), getString(R.string.label_confirm), NOT_SUPPORT_ALL_PAY);
                            } else if (((ApiCodeErrorException) throwable).getState().contentEquals("ORDER_PRICE_QUANTITY_NOT_ENOUGH")) {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.label_confirm), ORDER_PRICE_QUANTITY_NOT_ENOUGH);
                            } else {
                                showErrorDialog(throwable.getMessage(), "", getString(R.string.label_confirm), COMMON_ERROR_CODE);
                            }
                        } else {
                            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(QuoteDetailActivity.this, getString(R.string.net_error));
                        }
                    }
                });
    }

    @OnClick({R.id.ll_contract_seller, R.id.btnBack, R.id.tvTitle, R.id.btnRight, R.id.iv_service_phone, R.id.iv_btn_right, R.id.icon, R.id.price_text, R.id.price, R.id.tv_storage_and_start_less, R.id.iv_reduce, R.id.et_buy_pond, R.id.iv_add, R.id.tv_cash_deposit, R.id.place, R.id.deliveryTime, R.id.tv_product_info_details, R.id.tv_contact_way, R.id.tv_seller_company_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_contract_seller:
                if (UserDataUtil.isLogin()) {
                    String[] permission = new String[]{Manifest.permission.CALL_PHONE};
                    if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
                        showChooseDialog("拨打电话：" + priceInfo.getContactPhone());
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permission, Constants.REQUEST_PERMISSION_CALL_PHONE);
                    }
                } else {
                    clickButNotShowChooseDialog = true;
                    gotoActivity(LoginActivity.class);
                }
                break;
            case R.id.btnBack:
                break;
            case R.id.tvTitle:
                break;
            case R.id.btnRight:
                break;
            case R.id.iv_service_phone:
                break;
            case R.id.iv_btn_right:
                break;
            case R.id.icon:
                break;
            case R.id.price_text:
                break;
            case R.id.price:
                break;
            case R.id.tv_storage_and_start_less:
                break;
            case R.id.iv_reduce:
                if (!TextUtils.isEmpty(etBuyPond.getText().toString()))
                    verifyInputNumber(Integer.parseInt(etBuyPond.getText().toString()) - minSpace + "");
                break;
            case R.id.et_buy_pond:
                break;
            case R.id.iv_add:
                if (!TextUtils.isEmpty(etBuyPond.getText().toString()))
                    verifyInputNumber(Integer.parseInt(etBuyPond.getText().toString()) + minSpace + "");
                break;
            case R.id.tv_cash_deposit:
                break;
            case R.id.tv_buyer_deposit:
                break;
            case R.id.tv_seller_deposit:
                break;
            case R.id.place:
                break;
            case R.id.deliveryTime:
                break;
            case R.id.tv_product_info_details:
                break;
            case R.id.tv_contact_way:
                break;
            case R.id.tv_seller_company_info:
                break;
        }
    }


    private void showProgress() {
        if (progress.getVisibility() == View.GONE) {
            progress.setVisibility(View.VISIBLE);
        }
        llOrderLayout.setVisibility(View.INVISIBLE);
        nestedScrollview.setVisibility(View.INVISIBLE);
        tvTitle.setText("加载中");
        progress.showAnimation(true);
    }

    private void disMissProgress() {
        progress.refreshDone(true);
        progress.setVisibility(View.GONE);
        llOrderLayout.setVisibility(View.VISIBLE);
        tvTitle.setText("报价详情");
        nestedScrollview.setVisibility(View.VISIBLE);
    }


    @SuppressWarnings("deprecation")
    private void showRequestFail() {
        llOrderLayout.setVisibility(View.INVISIBLE);
        nestedScrollview.setVisibility(View.INVISIBLE);
        llEmptyView.setVisibility(View.VISIBLE);
        tvTitle.setText("报价详情");
        if (llEmptyView.findViewWithTag("rightNowRefresh") == null) {
            tvEmptyAction.setText("加载失败，请点击刷新");
            emptyView.setImageResource(R.drawable.default_img_404);
            TextView textView = new TextView(this);
            textView.setTag("rightNowRefresh");
            textView.setBackgroundResource(R.drawable.common_tv_bg_2radius);
            textView.setTextColor(this.getResources().getColor(R.color.global_blue));
            textView.setText("立即刷新");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dim26));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.dim40);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llEmptyView.setVisibility(View.GONE);
                    getProductDetail(productDetailId);
                }
            });
            llEmptyView.addView(textView, layoutParams);
        }

    }


}
