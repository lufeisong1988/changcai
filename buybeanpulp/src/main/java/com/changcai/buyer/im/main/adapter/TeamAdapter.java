package com.changcai.buyer.im.main.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GroupDetailModel;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.UserDataUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/8.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private Activity context;
    List<GroupDetailModel> list = new ArrayList<>();
    Drawable defaultDrawable;
    GroupAdapterListener listener;

    public TeamAdapter(Activity context, List<GroupDetailModel> list, GroupAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        defaultDrawable = context.getResources().getDrawable(R.drawable.appicon_186);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_group, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GroupDetailModel groupDetailModel = list.get(position);
        String name = groupDetailModel.gettName();
        int onLine = groupDetailModel.getOnLineMap().size();
        int offLine = groupDetailModel.getOffLineMap().size();
        int msgUnreadCount = groupDetailModel.getMsgUnreadCount();
        String msg = groupDetailModel.getMsg();
        boolean myTeam = groupDetailModel.isMyTeam();
        holder.tv_name.setText(name);
        PicassoImageLoader.getInstance().displayNetImage(context, TextUtils.isEmpty(groupDetailModel.getIcon()) ? "http://" : groupDetailModel.getIcon(), holder.iv_icon, defaultDrawable);
        if (UserDataUtil.isLogin()) {
            if(myTeam){
                holder.tv_time.setText(onLine + "/" + (onLine + offLine) + "在线");
                holder.iv_dot.setVisibility(msgUnreadCount > 0 ? View.VISIBLE : View.INVISIBLE);
            }else{
                holder.tv_time.setText(TimeUtil.getTimeShowString(System.currentTimeMillis(), true));
                holder.iv_dot.setVisibility( View.INVISIBLE);
            }

            holder.tv_content.setText(msg);
        } else {
            holder.tv_time.setText(TimeUtil.getTimeShowString(System.currentTimeMillis(), true));
            holder.iv_dot.setVisibility(View.INVISIBLE);
            holder.tv_content.setText("登录后查看会话消息");
        }


        holder.cl_consultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cl_consultant;
        ImageView iv_icon, iv_dot, iv_gurad;
        TextView tv_name, tv_content, tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            cl_consultant = itemView.findViewById(R.id.cl_consultant);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_dot = itemView.findViewById(R.id.iv_dot);
            iv_gurad = itemView.findViewById(R.id.iv_gurad);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    public interface GroupAdapterListener {
        void onClickListener(int position);

    }
}
