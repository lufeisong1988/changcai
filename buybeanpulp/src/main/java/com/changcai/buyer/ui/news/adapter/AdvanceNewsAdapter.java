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
import com.changcai.buyer.bean.NewsReader;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.CustomFontTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxingwei on 2016/11/4.
 * 2.0版资讯升级改版
 */

public class AdvanceNewsAdapter extends BaseAdapter {


    private CustomListener customListener;
    Drawable defaultDrawable;

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    private static final int SECTION = 0;
    private static final int ITEM = 1;
    private List<NewsEntity> newsList;
    private LayoutInflater inflater;
    private Activity activity;

    public AdvanceNewsAdapter(Activity activity, List<NewsEntity> newsEntities) {
        inflater = LayoutInflater.from(activity);
        newsList = newsEntities;
        this.activity = activity;
        defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
    }

    private View createViewBySection(ViewGroup parent, int position) {
        switch (getItemViewType(position)) {
            case SECTION:
                return inflater.inflate(R.layout.cms_list_calendar, parent, false);
            case ITEM:
                return inflater.inflate(R.layout.activity_cms_list_item, parent, false);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {

        NewsEntity newsEntity = newsList.get(position);


        if (null == newsEntity.getSection()) {
            return ITEM;
        } else {
            return SECTION;
        }


    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setNewsList(ArrayList<NewsEntity> newsList) {
        if (newsList != null) {
            this.newsList = (ArrayList<NewsEntity>) newsList.clone();
            notifyDataSetChanged();
        }
    }

    public void clearDataList() {
        if (this.newsList != null) {
            this.newsList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NewsEntity entity = newsList.get(position);
        NewsEntity prevEntity = null;
        if (position - 1 > 0) {
            prevEntity = newsList.get(position - 1);
        }
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = createViewBySection(parent, position);
            //如果分组为空 则表明为item内容
            if (null == entity.getSection()) {

                viewHolder.tvNewsInfo = (TextView) convertView.findViewById(R.id.tv_news_item_info);
                viewHolder.ivClassify = (ImageView) convertView.findViewById(R.id.iv_classify);
                viewHolder.tvTimeDesc = (CustomFontTextView) convertView.findViewById(R.id.tv_time_desc);
                viewHolder.ivMemberShip = (ImageView) convertView.findViewById(R.id.iv_membership);
                viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.cms_list_item_root_view);
                viewHolder.tvDesc = (CustomFontTextView) convertView.findViewById(R.id.tv_news_item_desc);

            } else {
                //viewHolder.tvSection = (TextView) convertView.findViewById(R.id.tv_cms_list_section_item);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
                viewHolder.tvEvent = (TextView) convertView.findViewById(R.id.tv_event);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == ITEM) {
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != customListener) {
                        customListener.onCustomerListener(v, position);
                    }
                }
            });

            viewHolder.tvTimeDesc.setText(entity.getTimeDesc());
//            viewHolder.tvClassifyTitle.setText(entity.getTag());
            LinearLayout.LayoutParams layoutParams;
            if (prevEntity != null && prevEntity.getSection() != null) {
                layoutParams = (LinearLayout.LayoutParams) viewHolder.tvNewsInfo.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams = (LinearLayout.LayoutParams) viewHolder.tvNewsInfo.getLayoutParams();
                layoutParams.setMargins(0, 24, 0, 0);
            }
            viewHolder.tvNewsInfo.setLayoutParams(layoutParams);
            if (TextUtils.isEmpty(entity.getSummary())) {
                viewHolder.tvDesc.setVisibility(View.GONE);
            } else {
                viewHolder.tvDesc.setVisibility(View.VISIBLE);
                viewHolder.tvDesc.setText(entity.getSummary());
            }
            if (NewsReader.getSingleton().isReadNews(entity.getArticleId())) {
                viewHolder.tvNewsInfo.setSelected(true);
            } else {
                viewHolder.tvNewsInfo.setSelected(false);
            }
            viewHolder.tvNewsInfo.setText(entity.getTitle());

//            PicassoImageLoader.getInstance().displayNetImage(activity, entity.getPicUrl(), viewHolder.ivIcon, defaultDrawable, AndroidUtil.dip2px(activity,R.dimen.dim180),AndroidUtil.dip2px(activity,R.dimen.dim120));
            PicassoImageLoader.getInstance().displayNetImage(activity, entity.getTag(), viewHolder.ivClassify, defaultDrawable);

            if(!TextUtils.isEmpty(entity.getMinGradePic())){
                viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
                PicassoImageLoader.getInstance().displayNetImage(activity, entity.getMinGradePic(), viewHolder.ivMemberShip,defaultDrawable);
            }else{
                viewHolder.ivMemberShip.setVisibility(View.GONE);
            }

//            if (null != entity.getMinGrade()) {
//                switch (entity.getMinGrade()) {
//                    case "-1":
//                        viewHolder.ivMemberShip.setVisibility(View.INVISIBLE);
//                        viewHolder.ivMemberShip.setImageDrawable(null);
//                        break;
//                    case "0":
//                        viewHolder.ivMemberShip.setVisibility(View.INVISIBLE);
//                        viewHolder.ivMemberShip.setImageDrawable(null);
//                        break;
//                    case "100":
//                        viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
//                        viewHolder.ivMemberShip.setImageResource(R.drawable.vip2_by_app);
//                        break;
//                    case "150":
//                        viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
//                        viewHolder.ivMemberShip.setImageResource(R.drawable.vip_by_plus_app);
//                        break;
//                    case "200":
//                        viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
//                        viewHolder.ivMemberShip.setImageResource(R.drawable.vip3_hj_app);
//                        break;
//                    case "300":
//                        viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
//                        viewHolder.ivMemberShip.setImageResource(R.drawable.vip4_zs_app);
//                        break;
//                    case "400":
//                        viewHolder.ivMemberShip.setVisibility(View.VISIBLE);
//                        viewHolder.ivMemberShip.setImageResource(R.drawable.vip5_vip_app);
//                        break;
//                    default:
//                        viewHolder.ivMemberShip.setVisibility(View.INVISIBLE);
//                        viewHolder.ivMemberShip.setImageDrawable(null);
//                        break;
//                }
//            } else {
//                viewHolder.ivMemberShip.setVisibility(View.INVISIBLE);
//                viewHolder.ivMemberShip.setImageDrawable(null);
//            }
        } else {
            viewHolder.tvTime.setText(entity.getSection());
            viewHolder.tvWeek.setText(DateUtil.getWeekOfDate(new Date(Long.parseLong(entity.getCreateTime()))));
            viewHolder.tvEvent.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItemViewType(position) == SECTION) {
            return false;
        }
        return super.isEnabled(position);
    }

    protected class ViewHolder {
        TextView tvSection;
        TextView tvNewsInfo;
        ImageView ivClassify;
        CustomFontTextView tvTimeDesc;
        ImageView ivMemberShip;
        LinearLayout rootView;
        CustomFontTextView tvDesc;

        TextView tvTime;
        TextView tvWeek;
        TextView tvEvent;
    }


}
