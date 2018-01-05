package com.changcai.buyer.ui.news.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
 * Created by lufeisong on 2017/11/15.
 */

public class NewIndexAdapter extends BaseExpandableListAdapter {
    private final Drawable defaultDrawable;
    private Activity activity;
    private List<String> groups = new ArrayList<>();
    private List<List<NewsEntity>> childs = new ArrayList<>();

    public NewIndexAdapter(Activity activity, List<String> groups, List<List<NewsEntity>> childs) {
        this.activity = activity;
        this.groups = groups;
        this.childs = childs;
        defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder vh = null;
        if(convertView == null){
            vh = new GroupViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_new_index_group,null);
            vh.tv_new_index_group_title = (TextView) convertView.findViewById(R.id.tv_new_index_group_title);
            vh.tv_new_index_group_more = (CustomFontTextView) convertView.findViewById(R.id.tv_new_index_group_more);
            convertView.setTag(vh);
        }else{
            vh = (GroupViewHolder) convertView.getTag();
        }
        vh.tv_new_index_group_title.setText(groups.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder vh = null;
        if(convertView == null){
            vh = new ChildViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_new_index_child,null);
            vh.tv_new_index_child_title = (TextView) convertView.findViewById(R.id.tv_new_index_child_title);
            vh.tv_new_index_child_text = (TextView) convertView.findViewById(R.id.tv_new_index_child_text);
            vh.iv_new_index_child_icon = (ImageView) convertView.findViewById(R.id.iv_new_index_child_icon);
            vh.iv_new_index_child_classify = (ImageView) convertView.findViewById(R.id.iv_new_index_child_classify);
            vh.iv_new_index_child_membership = (ImageView) convertView.findViewById(R.id.iv_new_index_child_membership);
            vh.view_bottom = (LinearLayout) convertView.findViewById(R.id.view_bottom);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);
        }else{
            vh = (ChildViewHolder) convertView.getTag();
        }
        if(childPosition == (childs.get(groupPosition).size() - 1)){
            vh.view_bottom.setVisibility(View.VISIBLE);
        }else{
            vh.view_bottom.setVisibility(View.GONE);
        }
        NewsEntity entity = childs.get(groupPosition).get(childPosition);
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
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupViewHolder{
        TextView tv_new_index_group_title;
        CustomFontTextView tv_new_index_group_more;
    }
    class ChildViewHolder{
        ImageView iv_new_index_child_icon,iv_new_index_child_classify,iv_new_index_child_membership;
        TextView tv_new_index_child_title,tv_new_index_child_text,tv_time;
        LinearLayout view_bottom;
    }
    public void update(List<String> groups, List<List<NewsEntity>> childs){
        this.groups.clear();
        this.childs.clear();
        this.groups.addAll(groups);
        this.childs.addAll(childs);
        notifyDataSetChanged();
    }
}
