package com.changcai.buyer.ui.news.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.util.PicassoImageLoader;

import java.util.List;

/**
 * Created by lufeisong on 2017/11/14.
 */

public class DayFoucusAdapter extends BaseAdapter {
    private Activity context;
    private List<NewsEntity> newsEntities;
    Drawable defaultDrawable;
    public DayFoucusAdapter(Activity context, List<NewsEntity> newsEntities) {
        this.context = context;
        this.newsEntities = newsEntities;
        defaultDrawable = ContextCompat.getDrawable(context, R.mipmap.no_network_2);
    }

    @Override
    public int getCount() {
        return newsEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return newsEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_new_index_day_foucus,null);
            vh = new ViewHolder();
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_dayFocus_icon);
            vh.iv_classify = (ImageView) convertView.findViewById(R.id.iv_classify);
            vh.iv_membership = (ImageView) convertView.findViewById(R.id.iv_membership);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_dayFocus_title);
            vh.tv_text = (TextView) convertView.findViewById(R.id.tv_dayFocus_text);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        NewsEntity entity = newsEntities.get(position);
        PicassoImageLoader.getInstance().displayNetImage(context, entity.getTag(), vh.iv_classify, defaultDrawable);
        PicassoImageLoader.getInstance().displayNetImage(context, entity.getMinGradePic(), vh.iv_membership,defaultDrawable);
        vh.tv_title.setText(entity.getTitle());
        vh.tv_text.setText(entity.getSummary());
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_icon,iv_classify,iv_membership;
        private TextView tv_title,tv_text;

    }
}
