package com.changcai.buyer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.changcai.buyer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/12/27.
 */

public class ActionSheetListDialog extends Dialog {
    private Context context;
    private Dialog dialog;
    private Display display;

    private ClickListener listener;
    private ItemAdapter adapter;

    private TextView btn_left,btn_right;
    private ListView lv_userLevel;

    private List<String> items = new ArrayList<>();
    private int currentPosition = 0;

    public ActionSheetListDialog(Context context) {
        super(context);
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }
    public ActionSheetListDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_actionsheetlist, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        lv_userLevel = view.findViewById(R.id.lv_userLevel);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ensureClick(currentPosition);
            }
        });
        lv_userLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                adapter.notifyDataSetChanged();

            }
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        adapter = new ItemAdapter();
        lv_userLevel.setAdapter(adapter);
        return this;
    }

    public ActionSheetListDialog setAdapter(List<String> items,int position){
        this.currentPosition = position;
        this.items = items;
        adapter.notifyDataSetChanged();
        return this;
    }
    public ActionSheetListDialog setClickListener(ClickListener listener){
        this.listener = listener;
        return this;
    }
    public void show(){
        try{
            dialog.show();
        }catch (Exception e){
        }
    }
    public void dismiss(){
        try{
            dialog.dismiss();
        }catch (Exception e){
        }
    }
    public interface ClickListener{
        void cancleClick();
        void ensureClick(int position);
    }
    private class ItemAdapter extends BaseAdapter{

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
                convertView = LayoutInflater.from(context).inflate(R.layout.dialog_actionsheetlist_item,null);
                vh = new ViewHolder();
                vh.iv_select = convertView.findViewById(R.id.iv_select);
                vh.tv_level = convertView.findViewById(R.id.tv_level);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            String itemStr = items.get(position);
            vh.tv_level.setText(itemStr);
            if(currentPosition == position){
                vh.tv_level.setTextColor(context.getResources().getColor(R.color.graph_bottom_click_dot));
                vh.iv_select.setVisibility(View.VISIBLE);
            }else{
                vh.tv_level.setTextColor(context.getResources().getColor(R.color.font_black));
                vh.iv_select.setVisibility(View.GONE);
            }
            return convertView;
        }
        class ViewHolder{
            TextView tv_level;
            ImageView iv_select;
        }
    }
}
