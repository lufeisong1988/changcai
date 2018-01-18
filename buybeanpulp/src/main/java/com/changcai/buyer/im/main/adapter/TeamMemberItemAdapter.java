package com.changcai.buyer.im.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.im.DemoCache;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.RoundImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/16.
 */

public class TeamMemberItemAdapter extends BaseAdapter {
    private List<TeamMember> teamMembers;
    private Context context;
    private TeamMemberItemAdapterCallback callback;
    private Drawable defaultDrawable;
    private boolean showDeleteAble = false;
    private boolean managerAble = false;//是否是管理员

    private HashMap<String,String> onLineMap,offLineMap;


    public TeamMemberItemAdapter(List<TeamMember> teamMembers, Context context,TeamMemberItemAdapterCallback callback,HashMap<String,String> onLineMap,HashMap<String,String> offLineMap) {
        this.teamMembers = teamMembers;
        this.context = context;
        this.callback = callback;
        this.onLineMap = onLineMap;
        this.offLineMap = offLineMap;
        defaultDrawable = ContextCompat.getDrawable(context, R.drawable.icon_default_head);
        checkManager();
    }

    //增加，删除 2个按钮
    @Override
    public int getCount() {
        return managerAble ? teamMembers.size() + 2 : teamMembers.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < teamMembers.size()) {
            return teamMembers.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_teammember_usericon, null);
            vh.iv_userIcon = convertView.findViewById(R.id.iv_userIcon);
            vh.tv_name = convertView.findViewById(R.id.tv_name);
            vh.iv_delete = convertView.findViewById(R.id.iv_delete);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (position < teamMembers.size()) {
            final TeamMember teamMember = teamMembers.get(position);
            if(offLineMap.containsKey(teamMember.getAccount())){
                vh.iv_userIcon.setColorFilter(setColorMatrix(0));
            }else{
                vh.iv_userIcon.setColorFilter(null);
            }
            if (teamMember.getType().getValue() != TeamMemberType.Manager.getValue() && teamMember.getType().getValue() != TeamMemberType.Owner.getValue() && showDeleteAble) {
                vh.iv_delete.setVisibility(View.VISIBLE);
            } else {
                vh.iv_delete.setVisibility(View.GONE);
            }
            NimUserInfo nimUserInfo = NIMClient.getService(UserService.class).getUserInfo(teamMember.getAccount());
            if (nimUserInfo != null) {
                updateUI(teamMember, nimUserInfo, vh.iv_userIcon, vh.tv_name);
            } else {
                List<String> accounts = new ArrayList<>();
                accounts.add(teamMember.getAccount());
                final ViewHolder finalVh = vh;
                NIMClient.getService(UserService.class)
                        .fetchUserInfo(accounts)
                        .setCallback(new RequestCallback<List<NimUserInfo>>() {
                            @Override
                            public void onSuccess(List<NimUserInfo> userInfos) {
                                if (userInfos != null && userInfos.size() > 0) {
                                    updateUI(teamMember, userInfos.get(0), finalVh.iv_userIcon, finalVh.tv_name);
                                } else {
                                    updateUI(teamMember, null, finalVh.iv_userIcon, finalVh.tv_name);
                                }
                            }

                            @Override
                            public void onFailed(int i) {
                                updateUI(teamMember, null, finalVh.iv_userIcon, finalVh.tv_name);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                updateUI(teamMember, null, finalVh.iv_userIcon, finalVh.tv_name);
                            }
                        });
            }
            vh.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("NimIM","account = " + teamMember.getAccount() );
                    callback.showDeleteMember(teamMember.getAccount());
                }
            });
        } else if (position < teamMembers.size() + 1) {
            PicassoImageLoader.getInstance().displayNetImage((Activity) context, "i am not empty", vh.iv_userIcon,ContextCompat.getDrawable(context,R.drawable.icon_add_teammember));
            vh.tv_name.setText("添加成员");
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            vh.iv_delete.setVisibility(View.GONE);

        } else if (position < teamMembers.size() + 2) {
            PicassoImageLoader.getInstance().displayNetImage((Activity) context, "i am not empty", vh.iv_userIcon,ContextCompat.getDrawable(context,R.drawable.icon_delete_teammember));
            vh.tv_name.setText("删除成员");
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.color_F43531));
            vh.iv_delete.setVisibility(View.GONE);

        }

        return convertView;

    }

    private void updateUI(TeamMember teamMember, NimUserInfo nimUserInfo, RoundImageView iv_userIcon, TextView tv_name) {
        if (nimUserInfo != null) {
            PicassoImageLoader.getInstance().displayNetImage((Activity) context, StringUtils.isEmpty(nimUserInfo.getAvatar()) ? "i am not empty" : nimUserInfo.getAvatar(), iv_userIcon, defaultDrawable);
            tv_name.setText(StringUtils.isEmpty(teamMember.getTeamNick()) ? ((StringUtils.isEmpty(nimUserInfo.getName()) ? (StringUtils.isEmpty(nimUserInfo.getAccount()) ? (StringUtils.isEmpty(nimUserInfo.getMobile()) ? "" : nimUserInfo.getMobile()) : nimUserInfo.getAccount()) : nimUserInfo.getName())) : teamMember.getTeamNick());
        } else {
            PicassoImageLoader.getInstance().displayResourceImageNoResize((Activity) context, R.drawable.icon_default_head, iv_userIcon);
            tv_name.setText(StringUtils.isEmpty(teamMember.getTeamNick()) ? "" : teamMember.getTeamNick());
        }
        tv_name.setTextColor(context.getResources().getColor(R.color.storm_gray));
    }

    class ViewHolder {
        RoundImageView iv_userIcon;
        TextView tv_name;
        ImageView iv_delete;
    }
    public interface TeamMemberItemAdapterCallback{
        void showDeleteMember(String member);
    }

    public void customNotifyDataChange(List<TeamMember> teamMembers){
        this.teamMembers.clear();
        this.teamMembers.addAll(teamMembers);
        checkManager();
        notifyDataSetChanged();
    }
    private void checkManager(){
        for(TeamMember teamMember : teamMembers){//判断是否是管理员
            if(teamMember.getType().getValue() == TeamMemberType.Owner.getValue() || teamMember.getType().getValue() == TeamMemberType.Manager.getValue()){
                if(DemoCache.getAccount() != null && DemoCache.getAccount().equals(teamMember.getAccount())){
                    managerAble = true;
                    break;
                }
            }
        }
    }
    private ColorMatrixColorFilter setColorMatrix(float saturation){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(saturation);//饱和度 0灰色 100过度彩色，50正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }

    public void addMember(){
        if (showDeleteAble) {
            showDeleteAble = !showDeleteAble;
            notifyDataSetChanged();
        }
    }
    public void removeMember(){
        showDeleteAble = !showDeleteAble;
        notifyDataSetChanged();
    }
}
