package com.changcai.buyer.ui.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.message.bean.MessageSettingInfo;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

public class MessageSettingAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private List<MessageSettingInfo> messageSettingInfos;
    private Context context;

    public MessageSettingAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<MessageSettingInfo> orderInfos) {
        this.messageSettingInfos = orderInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (messageSettingInfos == null) {
            return 0;
        } else {
            return messageSettingInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return messageSettingInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_setting_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            viewHolder.tg_button = (ToggleButton) convertView.findViewById(R.id.tg_button);
            viewHolder.v_bottom_view = (View) convertView.findViewById(R.id.v_bottom_view);
            viewHolder.v_top = (View) convertView.findViewById(R.id.v_top);
            viewHolder.v_bottom = (View) convertView.findViewById(R.id.v_bottom);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MessageSettingInfo messageSettingInfo = messageSettingInfos.get(position);
        if (messageSettingInfo != null) {
            viewHolder.tv_name.setText(messageSettingInfo.getName());
            if(TextUtils.isEmpty(messageSettingInfo.getInfo())) {
                viewHolder.tv_info.setVisibility(View.GONE);
            } else {
                viewHolder.tv_info.setVisibility(View.VISIBLE);
                viewHolder.tv_info.setText(messageSettingInfo.getInfo());
            }
            viewHolder.tg_button.setChecked("1".equals(messageSettingInfo.getStatus()));
            viewHolder.tg_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMyExt(messageSettingInfo.getKey(), viewHolder.tg_button.isChecked());
                }
            });

            if( position == 0 ) {
                viewHolder.v_bottom_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.v_bottom_view.setVisibility(View.GONE);
            }

            if( position == 0 | position ==  messageSettingInfos.size() - 1) {
                viewHolder.v_bottom.setVisibility(View.VISIBLE);
            } else {
                viewHolder.v_bottom.setVisibility(View.GONE);
            }
        }
        return convertView;
    }


    private class ViewHolder {
        public TextView tv_name;
        public TextView tv_info;
        public ToggleButton tg_button;
        public View v_bottom_view;
        public View v_top;
        public View v_bottom;

    }


    /**
     * 设置消息
     */
    private void setMyExt(String key, boolean isChecked) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put(key, isChecked?"1":"0");
        VolleyUtil.getInstance().httpPost(context, Urls.SET_MY_EXT, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode .equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(context,errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, true);
    }
}
