package com.changcai.buyer.ui.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.ConfirmDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryDetailActivity extends BaseActivity {

    @BindView(R.id.iv_order_status)
    ImageView ivOrderStatus;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.ll_delivery_status_top)
    LinearLayout llDeliveryStatusTop;
    @BindView(R.id.iv_icon_order_model)
    ImageView ivIconOrderModel;
    @BindView(R.id.tv_companyName)
    TextView tvCompanyName;
    @BindView(R.id.tv_delivery_no)
    TextView tvDeliveryNo;
    @BindView(R.id.tv_product_info_details)
    TextView tvProductInfoDetails;
    @BindView(R.id.tv_delivery_time_tip)
    TextView tvDeliveryTimeTip;
    @BindView(R.id.tv_delivery_time)
    TextView tvDeliveryTime;
    @BindView(R.id.tv_delivery_place)
    TextView tvDeliveryPlace;
    @BindView(R.id.tv_delivery_price)
    TextView tvDeliveryPrice;
    @BindView(R.id.tv_delivery_quantity)
    TextView tvDeliveryQuantity;
    @BindView(R.id.tv_delivery_amount_tip)
    TextView tvDeliveryAmountTip;
    @BindView(R.id.tv_delivery_amount)
    TextView tvDeliveryAmount;
    @BindView(R.id.tv_delivery_amount_deduction)
    TextView tvDeliveryAmountDeduction;
    @BindView(R.id.tv_transportation)
    TextView tvTransportation;
    @BindView(R.id.tv_phone_icon)
    ImageView tvPhoneIcon;
    @BindView(R.id.tv_check_my_order)
    TextView tvCheckMyOrder;
    @BindView(R.id.tv_status_btn)
    TextView tvStatusBtn;
    @BindView(R.id.tv_delivery_pay_way)
    TextView tvDeliveryPayWay;
    @BindView(R.id.rl_pay_way)
    RelativeLayout rlPayWay;
    @BindView(R.id.tv_standard)
    TextView tvStandard;
    @BindView(R.id.tv_no_transportation)
    TextView tvNoTransportation;
    @BindView(R.id.tv_delivery_already_pay)
    TextView tvDeliveryAlreadyPay;
    @BindView(R.id.iv_service_phone)
    ImageView ivServicePhone;
    private DeliveryInfo deliveryInfo;
    private String deliveryId;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("deliveryInfo")) {
                deliveryInfo = (DeliveryInfo) bundle.getSerializable("deliveryInfo");
            } else if (bundle.containsKey("deliveryId")) {
                deliveryId = bundle.getString("deliveryId");
            }
        }
        initTitle();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (deliveryInfo != null) {
            getOrderInfo(deliveryInfo.getId());
        } else {
            getOrderInfo(deliveryId);
        }
    }

    //
    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("提货单详情");
        titleView.setBackgroundColor(getResources().getColor(R.color.white));
        btnBack.setImageResource(R.drawable.icon_nav_back);
        tvTitle.setTextColor(getResources().getColor(R.color.black));

        ivServicePhone.setImageResource(R.drawable.icon_hotline_blue);
        ivServicePhone.setVisibility(View.VISIBLE);
        setOrderInfo(deliveryInfo);
    }

    //
    @SuppressWarnings("deprecation")
    private void setOrderInfo(DeliveryInfo info) {
        if (info != null) {
            tvOrderStatus.setText(info.getViewStatus());

            if (Constants.INVALID.equalsIgnoreCase(info.getStatus())) {
                ivOrderStatus.setImageLevel(0);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.global_text_gray));
            } else if (Constants.COMPLETE.equalsIgnoreCase(info.getStatus())) {
                ivOrderStatus.setImageLevel(10);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.pastel_green));
            } else if (Constants.SELLER_CONFIRMED.equalsIgnoreCase(info.getStatus())) {
                ivOrderStatus.setImageLevel(5);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (Constants.PAYED.equalsIgnoreCase(info.getStatus())) {
                ivOrderStatus.setImageLevel(5);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (Constants.UNPAY.equalsIgnoreCase(info.getStatus())) {
                ivOrderStatus.setImageLevel(5);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            tvCompanyName.setText(info.getSellerName() == null ? "" : info.getSellerName());
            tvDeliveryNo.setText(info.getDeliveryNo());
            tvProductInfoDetails.setText(info.getFactoryBrand().concat(" / ").concat(info.getEggSpec()).concat(" / ").concat(info.getPackType()));
            tvDeliveryTime.setText(info.getDeliveryTime());
            tvDeliveryPlace.setText(info.getAddress());
            tvDeliveryPrice.setText(info.getPrice());
            //应付金额
            tvDeliveryAmount.setText(info.getOriginalAmount());
            tvDeliveryAmountDeduction.setText("货款总额".concat(info.getAmount()).concat("-").concat("保证金抵扣金额").concat(info.getPayOffset()));


            tvDeliveryAlreadyPay.setText(info.getActualAmount());


            if (TextUtils.isEmpty(info.getMemo())) {
                RelativeLayout.LayoutParams relativeLayout = (RelativeLayout.LayoutParams) tvStandard.getLayoutParams();
                relativeLayout.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                tvStandard.setLayoutParams(relativeLayout);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvNoTransportation.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                tvNoTransportation.setLayoutParams(layoutParams);
                tvNoTransportation.setVisibility(View.VISIBLE);
                tvTransportation.setVisibility(View.GONE);
            } else {
                tvTransportation.setText(info.getMemo());
                tvTransportation.setVisibility(View.VISIBLE);
                tvNoTransportation.setVisibility(View.GONE);
            }
//派单
            if (Constants.UNPAY.equalsIgnoreCase(info.getStatus())) {
                tvStatusBtn.setVisibility(View.VISIBLE);
                tvStatusBtn.setText(R.string.delivery_pay6);
            }
            if (!TextUtils.isEmpty(info.getDeliveryMode()) && (info.getDeliveryMode().equalsIgnoreCase("SEND_ORDER"))) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dim81), getResources().getDimensionPixelOffset(R.dimen.dim57));
                ivIconOrderModel.setLayoutParams(layoutParams);
                ivIconOrderModel.setImageResource(R.drawable.icon_seller_initiated);

                rlPayWay.setVisibility(View.GONE);
                tvDeliveryQuantity.setText("申请:".concat(info.getAllQuantity()).concat("；").concat("实际:").concat(info.getQuantity()));

                tvDeliveryPayWay.setVisibility(View.GONE);
                tvDeliveryAmountDeduction.setText("货款总额".concat(info.getAmount()).concat("-").concat("定金抵扣金额").concat(info.getPayOffset()));


            } else {
                //订单
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dim29), getResources().getDimensionPixelOffset(R.dimen.dim25));
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                layoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.dim20), 0, 0, 0);
                ivIconOrderModel.setLayoutParams(layoutParams);
                ivIconOrderModel.setImageResource(R.drawable.icon_seller_order_list);
                rlPayWay.setVisibility(View.VISIBLE);
                tvDeliveryQuantity.setText(info.getQuantity());

                if (info.getPayType()!=null){
                    if (info.getPayType().equalsIgnoreCase("CASH_PAY")){
                        tvDeliveryPayWay.setTextColor(getResources().getColor(R.color.black));
                    }else if ( info.getPayType().equalsIgnoreCase("DELAY_PAY")){
                        tvDeliveryPayWay.setTextColor(getResources().getColor(R.color.flamingo));
                    }
                }else {
                    tvDeliveryPayWay.setTextColor(getResources().getColor(R.color.black));
                }
                tvDeliveryPayWay.setText(info.getPayTypeDesc());
                tvDeliveryPayWay.setVisibility(View.VISIBLE);

            }

        }

    }

    /**
     * 获取订单详情
     */

    private void getOrderInfo(final String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("deliveryId", "" + id);

        VolleyUtil.getInstance().httpPost(this, Urls.GET_DELIVERY_INFO, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                List<OrderInfo> tempList = null;
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    deliveryInfo = gson.fromJson(response.get(Constants.RESPONSE_CONTENT), DeliveryInfo.class);
                    setOrderInfo(deliveryInfo);
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(DeliveryDetailActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false);
    }


    @OnClick({R.id.iv_service_phone, R.id.tv_phone_icon, R.id.tv_check_my_order, R.id.tv_status_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_icon:
                if (deliveryInfo != null)
                    showChooseDialog("联系卖家：", deliveryInfo.getSellerMobile());
                break;
            case R.id.tv_check_my_order:
                Bundle bundleOrder = new Bundle();
                bundleOrder.putString("orderId", deliveryInfo.getOrderId());
                gotoActivity(OrderDetailActivity.class, bundleOrder);
                break;
            case R.id.tv_status_btn:
                if (!TextUtils.isEmpty(deliveryInfo.getDeliveryMode()) && (deliveryInfo.getDeliveryMode().equalsIgnoreCase("SEND_ORDER")) && Constants.UNPAY.equalsIgnoreCase(deliveryInfo.getStatus())) {
                    UserInfo u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                    if (u.getEnterType().equalsIgnoreCase("PERSONAL")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("deliveryId", deliveryInfo.getId());
                        bundle.putString("payType", "_goods_pay");
                        gotoActivity(PayActivity.class, bundle);
                    } else {
                        ConfirmDialog.createConfirmDialog(DeliveryDetailActivity.this, "尊敬的企业用户您好，因手机\n" +
                                "端无法使用Ukey支付，请在\n" +
                                "电脑端完成剩余操作。");
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "订单详情");
                    bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                    gotoActivity(ComputerOperationActivity.class, bundle);
                }

                break;
            case R.id.iv_service_phone:
                if (deliveryInfo != null)
                    showChooseDialog("拨打客服热线：", SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                break;
        }
    }

    private void showChooseDialog(String preString, @NonNull final String phone) {
        View view = getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(this, R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        if (preString != null) {
            tvPhone.setText(preString.concat(phone));
        } else {
            tvPhone.setText(phone);
        }
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                try {
                    startActivity(intent);
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


}
