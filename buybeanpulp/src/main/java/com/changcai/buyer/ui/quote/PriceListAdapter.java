package com.changcai.buyer.ui.quote;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.CustomFontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 报价列表页的Adapter
 * Created by huangjian299 on 16/6/7.
 */
public class PriceListAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<PriceInfo> priceInfos;

    private Drawable defaultDrawable;
    private Context mContext;

    private boolean isScreenInchLess4;

    public PriceListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        defaultDrawable = ContextCompat.getDrawable(context, R.drawable.cc_bg_defualt);
        isScreenInchLess4 = AndroidUtil.getScreenInch((Activity) mContext) < 4.2 ? true : false;
    }

    public void setData(List<PriceInfo> priceInfos) {
        this.priceInfos = priceInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (priceInfos == null) {
            return 0;
        } else {
            return priceInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return priceInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.price_fragment_adapter_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PriceInfo priceInfo = priceInfos.get(position);
        if (priceInfo != null) {
            viewHolder.name.setText(priceInfo.getRegion() + " " + priceInfo.getLocation());
            viewHolder.percent.setText(priceInfo.getFactoryBrand().concat(" / ").concat(priceInfo.getEggSpec()));
            if (priceInfo.getPublishTime() != null) {
                viewHolder.time.setText(priceInfo.getPublishTime().concat("发布"));
            }
            if (Constants.SPOT.equals(priceInfo.getProductType())) {
                viewHolder.price.setText("¥ ".concat(priceInfo.getPrice()));
//                viewHolder.time.setText(priceInfo.getDeliveryStartTime().concat("至").concat(priceInfo.getDeliveryEndTime()));
            } else {
                if (Integer.valueOf(priceInfo.getPrice()) > 0) {
                    viewHolder.price.setText(priceInfo.getBasisCode().concat("+ ¥ ").concat(priceInfo.getPrice()));
                } else if (Integer.valueOf(priceInfo.getPrice()) == 0) {
                    viewHolder.price.setText(priceInfo.getBasisCode().concat("+ ¥ ").concat("0"));
                } else {
                    viewHolder.price.setText(priceInfo.getBasisCode().concat("- ¥ ").concat(priceInfo.getPrice().substring(1, priceInfo.getPrice().length())));
                }
//                viewHolder.time.setText(priceInfo.getBasisMonth());

            }
            viewHolder.pickLocation.setText("提货：".concat(priceInfo.getRegion() == null ? "" : priceInfo.getRegion().concat(" ")).concat(priceInfo.getLocation() == null ? "" : priceInfo.getLocation()));
            String imageUrl = priceInfo.getEnterPic();
            PicassoImageLoader.getInstance().displayNetImage((Activity) mContext, imageUrl, viewHolder.icon, defaultDrawable);
            viewHolder.name.setText(priceInfo.getEnterName());

            if (priceInfo.getEnterTypeName() != null && priceInfo.getEnterTypeName().contentEquals("油厂")) {
                viewHolder.ivOilIcon.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivOilIcon.setVisibility(View.GONE);
            }
            //无效
            if (null != priceInfo.getPriceStatus() && priceInfo.getPriceStatus().contentEquals("INVALID")) {
                viewHolder.ivLoseEfficacy.setVisibility(View.VISIBLE);
                viewHolder.price.setTextColor(mContext.getResources().getColor(R.color.x_list_view_footer_color_light_gray));
                viewHolder.textViewUnit.setTextColor(mContext.getResources().getColor(R.color.x_list_view_footer_color_light_gray));
            } else {
                viewHolder.ivLoseEfficacy.setVisibility(View.GONE);
                viewHolder.price.setTextColor(mContext.getResources().getColor(R.color.flamingo));
                viewHolder.textViewUnit.setTextColor(mContext.getResources().getColor(R.color.flamingo));
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.percent)
        TextView percent;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.name)
        CustomFontTextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.iv_lose_efficacy)
        ImageView ivLoseEfficacy;
        @BindView(R.id.iv_oil_icon)
        ImageView ivOilIcon;
        @BindView(R.id.location)
        TextView pickLocation;
        @BindView(R.id.tv_unit)
        TextView textViewUnit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
