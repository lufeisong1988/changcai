package com.changcai.buyer.ui.quote;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.quote.bean.ConditionValue;
import com.changcai.buyer.ui.quote.bean.LocationValue;
import com.changcai.buyer.view.CustomFontTextView;

import java.util.List;

/**
 * Created by huangjian299 on 16/6/8.
 */
public class PlaceClassifyMoreAdapter extends BaseAdapter {
    private Context context;
    private  List<LocationValue> textList;
    private int position = 0;
    Holder hold;

    public PlaceClassifyMoreAdapter(Context context, List<LocationValue> list) {
        this.context = context;
        this.textList = list;
    }
    public PlaceClassifyMoreAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<LocationValue> textList){
        this.textList = textList;
        notifyDataSetChanged();
    }


    public int getCount() {
        if (textList == null) {
            return 0;
        } else {
            return textList.size();
        }
    }

    public Object getItem(int position) {
        return textList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_classify_morelist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            hold = (Holder) view.getTag();
        }
        if(position < textList.size()) {
            LocationValue conditionValue = textList.get(position);
            if(conditionValue != null)
                hold.txt.setText(conditionValue.getName());
            hold.txt.setTextColor(0xFF26272A);
//            hold.txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,26);
            hold.layout.setBackgroundColor(0xFFFFFFFF);
            if (position == this.position) {
                hold.txt.setTextColor(0xFF408AFF);
                //hold.layout.setBackgroundColor(0xFFF5F5F5);
                hold.ivSelected.setVisibility(View.VISIBLE);
            }else {
                hold.ivSelected.setVisibility(View.GONE);
            }
        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    private static class Holder {
        LinearLayout layout;
        TextView txt;
        ImageView ivSelected;

        public Holder(View view) {
            layout = (LinearLayout) view.findViewById(R.id.moreitem_layout);
            txt = (CustomFontTextView) view.findViewById(R.id.moreitem_txt);
            ivSelected = (ImageView) view.findViewById(R.id.iv_selected);
        }
    }
}