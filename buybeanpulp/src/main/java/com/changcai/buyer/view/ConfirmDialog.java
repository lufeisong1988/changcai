package com.changcai.buyer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;

/**
 * @author zhoujun
 * @version 1.0
 * @description 加载动画dialog
 * @date 2014年9月5日 上午11:08:34
 */
public class ConfirmDialog {

    /**
     * 显示一个button
     *
     * @param context
     * @param info
     */
    public static void createOneConfirmDialog(Context context, String info) {
        createConfirmDialog(context, info, null, true);
    }

    public static void createConfirmDialog(Context context, String info) {
        createConfirmDialog(context, info, null, false);
    }
    public static void createConfirmDialog(Context context, String info,String title,String leftText,String rightText,OnBtnConfirmListener listener1,OnBtnConfirmListener listener2) {
        showAlertView(context,0,info,title,leftText,new String[]{rightText},listener1,listener2);
    }
    public static void createConfirmDialog(Context context, String info,String leftText,String rightText,OnBtnConfirmListener listener1,OnBtnConfirmListener listener2) {
        showAlertView(context,0,info,"",leftText,new String[]{rightText},listener1,listener2);
    }

    public static void createConfirmDialog(Context context, String info,String title,String buttonName,final OnBtnConfirmListener listener) {
        showAlertView(context,0,title,info,buttonName,null,listener,null);
    }
    public static void createConfirmDialog(Context context, String info, final OnBtnConfirmListener listener) {
        createConfirmDialog(context, info, listener, false);
    }

    public static void createConfirmDialog(Context context, String info, final OnBtnConfirmListener listener1, final OnBtnConfirmListener listener2) {
        createConfirmDialog(context, info, listener1, listener2, false);
    }

    public static void createConfirmDialog(Context context, String info, String buttonName) {
        showAlertView(context, 0, info, null, buttonName, null, null, null);
    }

    /**
     * 创建通用的确定对话框
     *
     * @param context
     * @param info
     * @param listener 确定按钮监听
     */
    public static void createConfirmDialog(Context context,int titleIcon, String info, final OnBtnConfirmListener listener) {
        showAlertView(context, titleIcon, info, null, "确定", null, listener, null);
    }
    public static void createConfirmDialog(Context context, String info, final OnBtnConfirmListener listener, boolean isOneButton) {
        showAlertView(context, 0, info, null, "确定", null, listener, null);
    }

    public static void createConfirmDialogWithTitle(Context context, String info, String title, final OnBtnConfirmListener listener, boolean isOneButton) {
        showAlertView(context, 0, title,info, "确定", null, listener, null);
    }

    public static void createConfirmDialogWithTitle(Context context,String info ,String title){
        createConfirmDialogWithTitle(context,  info,  title,   null, false) ;
        }

    public static void createConfirmDialogWithTitle(Context context,String info ,String title,final OnBtnConfirmListener listener){
        createConfirmDialogWithTitle(context,  info,  title,   listener, false) ;
    }
    /**
     * 创建通用的确定对话框
     *
     * @param context
     * @param info
     * @param listener1 确定按钮监听
     * @param listener2 取消按钮监听
     */
    public static void createConfirmDialog(Context context, String info, final OnBtnConfirmListener listener1
            , final OnBtnConfirmListener listener2, boolean isOneButton) {
        showAlertView(context, 0, info, null, "确定", new String[]{"取消"}, listener1, listener2);
    }

    public static void createConfirmDialogCustomButtonString(Context context, String info, final OnBtnConfirmListener listener1
            , final OnBtnConfirmListener listener2, boolean isOneButton) {
        showAlertView(context, 0, info, null, "放弃", new String[]{"继续填写"}, listener1, listener2);
    }
    public static void createConfirmDialogCustomButtonString(Context context, String title,String info, final OnBtnConfirmListener listener1
            , final OnBtnConfirmListener listener2,String cancelString ,String positiveString, boolean isOneButton) {
        showAlertView(context, 0, title, info, cancelString, new String[]{positiveString}, listener1, listener2);
    }
    public static void createConfirmDialog(Context context, String info, String title,final OnBtnConfirmListener listener1
            , final OnBtnConfirmListener listener2, boolean isOneButton) {
        showAlertView(context, 0, info, title, "确定", new String[]{"取消"}, listener1, listener2);
    }


    /**
     * 按钮确认监听
     */
    public interface OnBtnConfirmListener {
        void onConfirmListener();
    }


    /**
     * 实现一个按钮，2个按钮
     *
     * @param mContext       上下文
     * @param titleIcon      头部图标
     * @param title          头部文字
     * @param message        内容
     * @param positiveButton 一个默认按钮
     * @param otherButtons   其他按钮
     * @return
     */
    public static Dialog showAlertView(Context mContext, int titleIcon, String title,
                                       String message, String positiveButton,
                                       final String[] otherButtons,
                                       final OnBtnConfirmListener listener1,
                                       final OnBtnConfirmListener listener2) {

        final Dialog dialog = new Dialog(mContext, R.style.alertView);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view =  inflater.inflate(R.layout.alertview, null);
        // 标题和内容L
        LinearLayout titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        ImageView icon = (ImageView) view.findViewById(R.id.titileicon);
        if (0 != titleIcon) {
            icon.setBackgroundResource(titleIcon);
        } else {
            icon.setVisibility(View.GONE);
        }
        if (null != title && !title.equalsIgnoreCase("")) {
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.GONE);
        }
        if (null != message && !message.equalsIgnoreCase("")) {
            messageTextView.setText(message);
        } else {
            messageTextView.setVisibility(View.GONE);
        }
        Button pButton = new Button(mContext);
        pButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 44), 1.0f));
        pButton.setTextColor(mContext.getResources().getColor(R.color.global_blue));
        pButton.setTextSize(18);
        // 动态添加按钮
        LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        if (null == otherButtons || otherButtons.length == 0) {
            // 一个按钮
            buttonLayout.setOrientation(LinearLayout.VERTICAL);
            pButton.setBackgroundResource(R.drawable.alert_bottom_button);
        } else if (null != otherButtons && otherButtons.length == 1) {
            // 两个按钮
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            pButton.setBackgroundResource(R.drawable.alert_left_button);
        } else {
            // 三个或三个以上按钮
            buttonLayout.setOrientation(LinearLayout.VERTICAL);
            pButton.setBackgroundResource(R.drawable.alert_middle_button);
        }

        pButton.setText(positiveButton);
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener1) {
                    listener1.onConfirmListener();
                }
                dialog.dismiss();
            }
        });
        buttonLayout.addView(pButton);
        if (null != otherButtons && otherButtons.length > 0) {
            for (int i = 0; i < otherButtons.length; i++) {
                final int index = i;
                Button otherButton = new Button(mContext);
                otherButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 44), 1.0f));
                otherButton.setText(otherButtons[i]);
                otherButton.setTextSize(18);
                otherButton.setTextColor(mContext.getResources().getColor(R.color.global_blue));
                if (1 == otherButtons.length) {
                    otherButton
                            .setBackgroundResource(R.drawable.alert_right_button);
                } else if (i < (otherButtons.length - 1)) {
                    otherButton
                            .setBackgroundResource(R.drawable.alert_middle_button);
                } else {
                    otherButton
                            .setBackgroundResource(R.drawable.alert_bottom_button);
                }
                otherButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener2) {
                            listener2.onConfirmListener();
                        }
                        dialog.dismiss();
                    }
                });
                buttonLayout.addView(otherButton);
            }
        }
        final int viewWidth = dip2px(mContext, 250);
        view.setMinimumWidth(viewWidth);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

    /**
     * 获取屏幕分辨率宽计算dialog的宽度
     */
    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
