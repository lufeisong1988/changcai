package com.changcai.buyer.ui.message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.message.bean.MessageInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private List<MessageInfo> messageInfos;
    private Context context;

    public MessageListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<MessageInfo> orderInfos) {
        this.messageInfos = orderInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (messageInfos == null) {
            return 0;
        } else {
            return messageInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return messageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MessageInfo messageInfo = messageInfos.get(position);
        if (messageInfo != null) {
            viewHolder.tv_time.setText(messageInfo.getCreateTime());
            viewHolder.tv_content.setText(messageInfo.getContent());
            viewHolder.tv_title.setText(messageInfo.getTitle());
            if("Read".equals(messageInfo.getStatus())) {
                viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.global_text_gray));
            }
        }
        return convertView;
    }


    private class ViewHolder {
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_time;
    }

}
