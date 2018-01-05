package com.changcai.buyer.view;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.changcai.buyer.R;

import static com.changcai.buyer.view.PopupWindowView.LOCATION.LOCATION_CUSTOM_DOWN_OFFSET;
import static com.changcai.buyer.view.PopupWindowView.LOCATION.LOCATION_CUSTOM_TOP_OFFSET;

/**
 * Created by liuxingwei on 16/9/6.
 */
public class PopupWindowView {


    private PopupWindow popupWindow;
    private int mPopupWindowWidth;
    private int mPopupWindowHeight;
    private LOCATION flag;
    View view;
    View _downView;
    private int offSetX;
    private int offSetY;


    public enum LOCATION {
        LOCATION_PARENT_CENTER,
        LOCATION_DROP_DOWN,
        LOCATION_DEFAULT,
        LOCATION_CUSTOM,
        LOCATION_CUSTOM_TOP_OFFSET,
        LOCATION_CUSTOM_DOWN_OFFSET;
    }


    public PopupWindowView(int w, int h, View view,
                           View downview, LOCATION flag) {
        super();
        this.mPopupWindowWidth = w;
        this.mPopupWindowHeight = h;
        this.view = view;
        this.flag = flag;
        this._downView = downview;
        init();
    }



    public PopupWindowView(int w, int h, View view,
                           View downview, LOCATION flag, int offSetX, int offSetY) {
        super();
        this.mPopupWindowWidth = w;
        this.mPopupWindowHeight = h;
        this.view = view;
        this.flag = flag;
        this._downView = downview;
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        init();
    }

    public PopupWindowView(int w, int h, View view,
                           View downview, LOCATION flag, ImageView iv) {
        super();

        this.mPopupWindowWidth = w;
        this.mPopupWindowHeight = h;
        this.view = view;
        this.flag = flag;
        this._downView = downview;
        init();
    }

    @SuppressWarnings("ResourceAsColor")
    public void init() {
        if (popupWindow != null) {
            return;
        }
        popupWindow = new PopupWindow(view, mPopupWindowWidth,
                mPopupWindowHeight);
        // view.setBackgroundResource(R.drawable.popupwindow);
        /*** 这2句很重要 ***/

        ColorDrawable cd = new ColorDrawable(-0000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);// 设置外部能点击
        if (flag == LOCATION.LOCATION_PARENT_CENTER) {
            popupWindow.showAtLocation(_downView, Gravity.CENTER, offSetX, offSetY);
        } else if (flag == LOCATION.LOCATION_DROP_DOWN) {
//			popupWindow.showAtLocation(downview,Gravity.BOTTOM, 0, 0);
//			popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
            popupWindow.showAsDropDown(_downView);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    _downView.setSelected(false);
                    popupWindow = null;
                }
            });
        } else if (flag == LOCATION.LOCATION_DEFAULT) {
            popupWindow.showAsDropDown(_downView);
        } else if (flag == LOCATION.LOCATION_CUSTOM) {
            popupWindow.showAtLocation(_downView, Gravity.LEFT, offSetX, offSetY);
        }else if (flag ==LOCATION_CUSTOM_TOP_OFFSET){
            popupWindow.showAtLocation(_downView, Gravity.BOTTOM, offSetX, offSetY);
        }else if (flag == LOCATION_CUSTOM_DOWN_OFFSET){
            popupWindow.showAsDropDown(_downView,offSetX,offSetY);
        }
        popupWindow.update();

        // setting popupWindow 点击消失
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

    }

    public PopupWindow getwindow() {
        return popupWindow;
    }

    /**
     * 点击消失方法
     */
    public void popupWindowDismiss() {
        popupWindow.dismiss();

    }

    public Boolean popupWindowisshowing() {
        if (popupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    // 下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
        popupWindow.showAsDropDown(parent, 0,
                // 保证尺寸是根据屏幕像素密度来的
                0);

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }
}
