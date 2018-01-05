package com.changcai.buyer.ui.order;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.listener.ForegroundListener;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliverListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private List<DeliveryInfo> deliveryInfos;
    private Context context;
    private CustomListener customListener;


    private ForegroundColorSpan foregroundColorSpanBlack;
    private ForegroundColorSpan foregroundColorSpanFlamingo;

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    public DeliverListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        foregroundColorSpanBlack = new ForegroundColorSpan(context.getResources().getColor(R.color.black));
        foregroundColorSpanFlamingo = new ForegroundColorSpan(context.getResources().getColor(R.color.flamingo));
    }

    public void setData(List<DeliveryInfo> orderInfos) {
        this.deliveryInfos = orderInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (deliveryInfos == null) {
            return 0;
        } else {
            return deliveryInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (deliveryInfos == null)
            return null;
        return deliveryInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.deliver_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DeliveryInfo deliveryInfo = deliveryInfos.get(position);
        if (deliveryInfo != null) {
            // TODO: 2017/4/20
            StringBuffer info  = new StringBuffer();
            if (!TextUtils.isEmpty(deliveryInfo.getFactoryBrand())){
                info.append(deliveryInfo.getFactoryBrand().concat(" / "));
            }
            if (!TextUtils.isEmpty(deliveryInfo.getEggSpec())){
                info.append(deliveryInfo.getEggSpec().concat(" / "));
            }
            if (!TextUtils.isEmpty(deliveryInfo.getPackType())){
                info.append(deliveryInfo.getPackType().concat(" / "));
            }
//            viewHolder._tvPriceInfo.setText(deliveryInfo.getFactoryBrand().concat(" / ").concat(deliveryInfo.getEggSpec().concat(" / ").concat(deliveryInfo.getPackType())));
            viewHolder._tvPriceInfo.setText(info);
            if (deliveryInfo.getDeliveryTime() != null) {
                viewHolder._tvTimePickCount.setText(deliveryInfo.getDeliveryTime().concat("提货").concat(deliveryInfo.getQuantity().concat("吨")));
            }

            if (deliveryInfo.getDeliveryMode() != null && deliveryInfo.getDeliveryMode().equalsIgnoreCase("SEND_ORDER")) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dim81), context.getResources().getDimensionPixelSize(R.dimen.dim57));
                layoutParams.topMargin = 0;
                viewHolder._imageView.setLayoutParams(layoutParams);
                viewHolder._imageView.setImageResource(R.drawable.icon_seller_initiated);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dim29), context.getResources().getDimensionPixelOffset(R.dimen.dim25));
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                layoutParams.setMargins(23, 0, 0, 0);
                viewHolder._imageView.setLayoutParams(layoutParams);
                viewHolder._imageView.setImageResource(R.drawable.icon_seller_order_list);
            }

            if (!TextUtils.isEmpty(deliveryInfo.getActualAmount())) {
                SpannableString spannableString = new SpannableString("应付金额：".concat(StringUtil.formatForMoney(deliveryInfo.getOriginalAmount())).concat("元"));
                viewHolder._tvShouldPay.setText(spannableString);
                SpannableString spannableStringAlready = new SpannableString("已付金额：".concat(StringUtil.formatForMoney(deliveryInfo.getActualAmount())).concat("元"));
                spannableStringAlready.setSpan(foregroundColorSpanBlack, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringAlready.setSpan(foregroundColorSpanFlamingo, 5, spannableStringAlready.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringAlready.setSpan(foregroundColorSpanBlack, spannableStringAlready.length() - 1, spannableStringAlready.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder._tvAlreadyPay.setText(spannableStringAlready);
            } else {
                SpannableString spannableString = new SpannableString("应付金额：".concat(StringUtil.formatForMoney(deliveryInfo.getOriginalAmount())).concat("元"));
                spannableString.setSpan(foregroundColorSpanBlack, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(foregroundColorSpanFlamingo, 5, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(foregroundColorSpanBlack, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder._tvShouldPay.setText(spannableString);
            }

            viewHolder._tvEnterName.setText(deliveryInfo.getSellerName());
            viewHolder._tvStatusInfo.setText(deliveryInfo.getViewStatus());
            if (deliveryInfo.getButtons() != null && deliveryInfo.getButtons().size() > 0) {
                viewHolder._rlButtonsStatus.setVisibility(View.VISIBLE);
                viewHolder._viewBottomLine.setVisibility(View.VISIBLE);
                viewHolder._tvStatusBtn.setText(deliveryInfo.getButtons().get(0).getMessage());
            } else {
                viewHolder._rlButtonsStatus.setVisibility(View.GONE);
                viewHolder._viewBottomLine.setVisibility(View.GONE);
            }
        }
        viewHolder._parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });
        viewHolder._tvStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.imageView)
        ImageView _imageView;
        @BindView(R.id.tv_enter_name)
        TextView _tvEnterName;
        @BindView(R.id.tv_status_info)
        TextView _tvStatusInfo;
        @BindView(R.id.tv_price_info)
        TextView _tvPriceInfo;
        @BindView(R.id.tv_time_pick_count)
        TextView _tvTimePickCount;
        @BindView(R.id.tv_should_pay)
        TextView _tvShouldPay;
        @BindView(R.id.tv_already_pay)
        TextView _tvAlreadyPay;
        @BindView(R.id.view_bottom_line)
        View _viewBottomLine;
        @BindView(R.id.tv_status_btn)
        TextView _tvStatusBtn;
        @BindView(R.id.rl_buttons_status)
        RelativeLayout _rlButtonsStatus;

        @BindView(R.id.ll_item_parent)
        LinearLayout _parent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
