package com.changcai.buyer.ui.news.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.CustomFontTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/28.
 */

public class NewIndexAdapter2 extends BaseAdapter {
    private final Drawable defaultDrawable;
    private Activity activity;
    private  List<NewsEntity> articles = new ArrayList<>();
    private NewIndexAdapterCallback callback;
    public NewIndexAdapter2(Activity activity, List<NewsEntity> articles,NewIndexAdapterCallback callback) {
        this.callback = callback;
        this.activity = activity;
        this.articles = articles;
        defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
    }
    @Override
    public int getCount() {

        return articles.size();
    }

    @Override
    public Object getItem(int position) {

        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_new_index,null);
            vh.tv_new_index_child_title = (TextView) convertView.findViewById(R.id.tv_new_index_child_title);
            vh.tv_new_index_child_text = (TextView) convertView.findViewById(R.id.tv_new_index_child_text);
            vh.iv_new_index_child_icon = (ImageView) convertView.findViewById(R.id.iv_new_index_child_icon);
            vh.iv_new_index_child_classify = (ImageView) convertView.findViewById(R.id.iv_new_index_child_classify);
            vh.iv_new_index_child_membership = (ImageView) convertView.findViewById(R.id.iv_new_index_child_membership);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.view_bottom_child = (LinearLayout) convertView.findViewById(R.id.view_bottom_child);
            vh.ll_child = (LinearLayout) convertView.findViewById(R.id.ll_child);
            vh.view_bottom = (LinearLayout) convertView.findViewById(R.id.view_bottom);
            vh.tv_new_index_group_title = (TextView) convertView.findViewById(R.id.tv_new_index_group_title);
            vh.tv_new_index_group_more = (CustomFontTextView) convertView.findViewById(R.id.tv_new_index_group_more);
            vh.ll_group = (LinearLayout) convertView.findViewById(R.id.ll_group);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        if(articles.get(position).getFlag()== null || articles.get(position).getFlag().isEmpty()){
            if((articles.size() - 1) == position){
                vh.view_bottom_child.setVisibility(View.VISIBLE);
            }else{
                vh.view_bottom_child.setVisibility(View.GONE);
            }
            vh.ll_child.setVisibility(View.VISIBLE);
            vh.ll_group.setVisibility(View.GONE);
            NewsEntity entity = articles.get(position);
            vh.tv_new_index_child_title.setText(entity.getTitle());
            vh.tv_new_index_child_text.setText(entity.getSummary());
            PicassoImageLoader.getInstance().displayNetImage(activity, entity.getTag(), vh.iv_new_index_child_classify, defaultDrawable);

            if(!TextUtils.isEmpty(entity.getMinGradePic())){
                vh.iv_new_index_child_membership.setVisibility(View.VISIBLE);
                PicassoImageLoader.getInstance().displayNetImage(activity, entity.getMinGradePic(), vh.iv_new_index_child_membership, defaultDrawable);
            }else{
                vh.iv_new_index_child_membership.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(entity.getPicUrl())){
                PicassoImageLoader.getInstance().displayNetImage(activity, entity.getPicUrl(), vh.iv_new_index_child_icon, defaultDrawable);
            }
            vh.tv_time.setText(entity.getTimeDesc());
            vh.ll_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.childCallback(v,position);
                }
            });
        }else{
            if(0 == position){
                vh.view_bottom.setVisibility(View.GONE);
            }else{
                vh.view_bottom.setVisibility(View.VISIBLE);
            }
            vh.ll_child.setVisibility(View.GONE);
            vh.ll_group.setVisibility(View.VISIBLE);
            vh.tv_new_index_group_title.setText(articles.get(position).getFlag());
            vh.ll_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.groupCallback(articles.get(position).getFlag());
                }
            });
        }

        return convertView;
    }
    class ViewHolder{
        TextView tv_new_index_group_title;
        CustomFontTextView tv_new_index_group_more;
        ImageView iv_new_index_child_icon,iv_new_index_child_classify,iv_new_index_child_membership;
        TextView tv_new_index_child_title,tv_new_index_child_text,tv_time;
        LinearLayout view_bottom,view_bottom_child;
        LinearLayout ll_group,ll_child;
    }
    public void notify(List<NewsEntity> articles){
        this.articles.clear();
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }
    public interface NewIndexAdapterCallback{
        void groupCallback(String flag);
        void childCallback(View view,int position);
    }
}
