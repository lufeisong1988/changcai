package com.changcai.buyer.ui.order;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.ui.order.bean.Buttons;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.StringUtil;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private List<OrderInfo> orderInfos;
    private Context context;
    private CustomListener customListener;

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    @SuppressWarnings("deprecation")
    public OrderListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (orderInfos == null) {
            return 0;
        } else {
            return orderInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return orderInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
            viewHolder.tv_regionAndLocation = (TextView) convertView.findViewById(R.id.tv_regionAndLocation);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_status_btn = (TextView) convertView.findViewById(R.id.tv_status_btn);
            viewHolder.tv_status_info = (TextView) convertView.findViewById(R.id.tv_status_info);
//            viewHolder.v_divide = convertView.findViewById(R.id.v_divide);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_enter_name);
            viewHolder._viewBottomLine = convertView.findViewById(R.id.view_bottom_line);
            viewHolder._tvPriceInfo = (TextView) convertView.findViewById(R.id.tv_price_info);
            viewHolder._rlButtonsStatus = (RelativeLayout) convertView.findViewById(R.id.rl_buttons_status);
            viewHolder._tvEarnest = (TextView) convertView.findViewById(R.id.tv_earnest);
            viewHolder._ivIcon = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderInfo orderInfo = orderInfos.get(position);
        if (orderInfo != null) {


            if (orderInfo.getPrice().contains("-")) {
                viewHolder.tv_price.setText(orderInfo.getPrice().replace("¥", "").replace("-", "").replace("+", "-").concat("元/吨"));
            } else {
                viewHolder.tv_price.setText(orderInfo.getPrice().replace("¥", "").concat("元/吨"));
            }
            viewHolder.tv_quantity.setText("".concat(orderInfo.getQuantity()).concat("吨"));
            viewHolder.tv_regionAndLocation.setText(orderInfo.getRegionAndLocation());
            viewHolder.tv_time.setText(orderInfo.getDeliveryStartTime() + "至" + orderInfo.getDeliveryEndTime());

            if (!TextUtils.isEmpty(orderInfo.getOrderModel()) && ((orderInfo.getOrderModel().contentEquals("CASH_ONHAND_ORDER") || orderInfo.getOrderModel().contentEquals("RESERVE_DEPOSIT_ORDER")))) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dim81), context.getResources().getDimensionPixelSize(R.dimen.dim57));
                layoutParams.topMargin = 0;
                viewHolder._ivIcon.setLayoutParams(layoutParams);
                viewHolder._ivIcon.setImageResource(R.drawable.icon_seller_initiated);
                if (orderInfo.getOrderModel().contentEquals("RESERVE_DEPOSIT_ORDER")) {
                    viewHolder._tvEarnest.setVisibility(View.VISIBLE);
                    viewHolder._tvEarnest.setText(StringUtil.formatForMoney(orderInfo.getPayOffset()).concat("元"));
                } else {
                    viewHolder._tvEarnest.setVisibility(View.GONE);
                }
            } else {
                //正常单子
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dim29), context.getResources().getDimensionPixelOffset(R.dimen.dim25));
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                layoutParams.setMargins(20, 0, 0, 0);
                viewHolder._ivIcon.setLayoutParams(layoutParams);
                viewHolder._ivIcon.setImageResource(R.drawable.icon_seller_order_list);
                viewHolder._tvEarnest.setVisibility(View.GONE);
            }
            PriceInfo priceInfo = orderInfo.getProduct();

            if (priceInfo != null) {
//
                if (!TextUtils.isEmpty(priceInfo.getFactoryBrand()) && !TextUtils.isEmpty(priceInfo.getEggSpec()) && !TextUtils.isEmpty(priceInfo.getPackType())) {
                    viewHolder._tvPriceInfo.setText(priceInfo.getFactoryBrand().concat(" / ").concat(priceInfo.getEggSpec()).concat(" / ").concat(priceInfo.getPackType()));
                } else {
                    viewHolder._tvPriceInfo.setVisibility(View.INVISIBLE);
                }
                viewHolder.tv_name.setText(TextUtils.isEmpty(priceInfo.getEnterName()) ? "" : priceInfo.getEnterName());
            }

            if (orderInfo.getButtons() == null || orderInfo.getButtons().isEmpty()) {
                if (Constants.OPEN_BUY_PICK.equalsIgnoreCase(orderInfo.getOrderStatus()) || Constants.OPEN_BUY_PICKING.equalsIgnoreCase(orderInfo.getOrderStatus())) {
                    viewHolder._tvEarnest.setText("提货请联系卖家");
                    viewHolder._tvEarnest.setVisibility(View.VISIBLE);
                    viewHolder._viewBottomLine.setVisibility(View.VISIBLE);
                    viewHolder._rlButtonsStatus.setVisibility(View.VISIBLE);

                } else {
                    viewHolder._rlButtonsStatus.setVisibility(View.GONE);
                    viewHolder.tv_status_btn.setVisibility(View.GONE);
                    viewHolder._viewBottomLine.setVisibility(View.GONE);
                }
                viewHolder.tv_status_btn.setVisibility(View.GONE);

            } else {

                List<Buttons> buttonses = orderInfo.getButtons();
                final Buttons button = getButtons(buttonses);
                if (button != null) {
                    viewHolder._viewBottomLine.setVisibility(View.VISIBLE);
                    viewHolder._rlButtonsStatus.setVisibility(View.VISIBLE);
                    viewHolder.tv_status_btn.setVisibility(View.VISIBLE);
                    viewHolder.tv_status_btn.setText(button.getMessage());
                    viewHolder.tv_status_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (customListener != null) {
                                customListener.onCustomerListener(v, position);
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(orderInfo.getOrderModel()) && orderInfo.getOrderModel().contentEquals("RESERVE_DEPOSIT_ORDER") && button.getType().contentEquals("PAY_FRONT_MONEY")) {
                        viewHolder._tvEarnest.setText("定金".concat(StringUtil.formatForMoney(orderInfo.getPayOffset())).concat("元"));
                        viewHolder._tvEarnest.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder._tvEarnest.setVisibility(View.GONE);
                    }

                } else {
                    viewHolder.tv_status_btn.setVisibility(View.GONE);
                    viewHolder._viewBottomLine.setVisibility(View.GONE);
                    viewHolder._tvEarnest.setVisibility(View.GONE);
                }


            }

            viewHolder.tv_status_info.setText(orderInfo.getOrderViewStatus());

        }
        return convertView;
    }

    private Buttons getButtons(List<Buttons> buttonses) {
        for (Buttons b : buttonses) {
            if (!b.getType().equalsIgnoreCase("CONFIRM_FAST_PAY")) {
                return b;
            }
        }
        return null;
    }

    private class ViewHolder {
        public TextView tv_price;
        public TextView tv_quantity;
        public TextView tv_regionAndLocation;
        public TextView tv_time;
        public TextView tv_status_info;
        public TextView tv_status_btn;
        public TextView tv_name;
        public View _viewBottomLine;
        public TextView _tvPriceInfo;
        public RelativeLayout _rlButtonsStatus;
        public TextView _tvEarnest;
        public ImageView _ivIcon;
    }

}
