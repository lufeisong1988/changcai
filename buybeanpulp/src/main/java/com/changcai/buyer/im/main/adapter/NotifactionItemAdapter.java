package com.changcai.buyer.im.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.NotifactionItemModel;
import com.changcai.buyer.im.main.model.NotifactionItem;
import com.changcai.buyer.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by lufeisong on 2017/12/20.
 */

public class NotifactionItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotifactionItemModel> items;
    public NotifactionItemAdapter(Context context,ArrayList<NotifactionItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_notifaction_item,null);
            vh.iv_nt_icon = convertView.findViewById(R.id.iv_nt_icon);
            vh.iv_dot = convertView.findViewById(R.id.iv_dot);
            vh.iv_gurad = convertView.findViewById(R.id.iv_gurad);
            vh.tv_nt_name = convertView.findViewById(R.id.tv_nt_name);
            vh.tv_content = convertView.findViewById(R.id.tv_content);
            vh.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        NotifactionItemModel model = items.get(position);
        NotifactionItem item = model.getNotifactionItem();
        vh.iv_nt_icon.setBackgroundResource(item.icondId);
        vh.tv_nt_name.setText(item.title);
        vh.tv_content.setText(model.getContent());
        if(model.isShowDot()){
            vh.iv_dot.setVisibility(View.VISIBLE);
        }else{
            vh.iv_dot.setVisibility(View.GONE);
        }
//        if(item != NotifactionItem.VIP_SERVICE){
            vh.tv_time.setText(DateUtil.getStringDate("MM-dd HH:mm:ss"));
            vh.iv_gurad.setBackgroundResource(0);
            vh.iv_gurad.setVisibility(View.GONE);
//        }else{
//            vh.iv_gurad.setBackgroundResource(R.drawable.icon_xzchyys);
//        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv_nt_icon,iv_dot,iv_gurad;
        TextView tv_nt_name,tv_content,tv_time;
    }

}
