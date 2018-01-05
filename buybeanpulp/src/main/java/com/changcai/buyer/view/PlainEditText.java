package com.changcai.buyer.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.view.View;

import com.changcai.buyer.R;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.AndroidUtil;

/**
 * Created by liuxingwei on 2017/2/22.
 */

public class PlainEditText extends EditText implements TextWatcher {
    //切换drawable的引用
    private Drawable visibilityDrawable;

    private boolean visibililty = false;

    public PlainEditText(Context context) {
        this(context, null);
    }

    public PlainEditText(Context context, AttributeSet attrs) {
        //指定了默认的style属性
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PlainEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //获得该EditText的left ,top ,right,bottom四个方向的drawable
        Drawable[] compoundDrawables = getCompoundDrawables();
        visibilityDrawable = compoundDrawables[2];
        if (visibilityDrawable == null) {
            visibilityDrawable = getResources().getDrawable(R.drawable.icon_password_hide);
        }
        setEyesIconVisible();
        addTextChangedListener(this);
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param
     */
    protected void setEyesIconVisible() {
        Drawable right = visibilityDrawable ;
//        int tempWidth = right.getIntrinsicWidth();
//        int tempHeight = right.getIntrinsicHeight();
        int tempWidth = (int) getContext().getResources().getDimension(R.dimen.dim44);
        int tempHeight = (int) getContext().getResources().getDimension(R.dimen.dim25);
        right.setBounds(0, 0, tempWidth,
                tempHeight);
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    /**
     * 用按下的位置来模拟点击事件
     * 当按下的点的位置 在  EditText的宽度 - (图标到控件右边的间距 + 图标的宽度)  和
     * EditText的宽度 - 图标到控件右边的间距 之间就模拟点击事件，
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            if (getCompoundDrawables()[2] != null) {
                boolean xFlag = false;
                boolean yFlag = false;
                //得到用户的点击位置，模拟点击事件
                xFlag = event.getX() > getWidth() - (visibilityDrawable.getIntrinsicWidth() + getCompoundPaddingRight
                        ()) &&
                        event.getX() < getWidth() - (getTotalPaddingRight() - getCompoundPaddingRight());

                if (xFlag) {
                    visibililty = !visibililty;
                    if (visibililty) {
                        visibilityDrawable = getResources().getDrawable(R.drawable.icon_password_show);
                    /*this.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/

                        this.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //隐藏密码
                        visibilityDrawable = getResources().getDrawable(R.drawable.icon_password_hide);
                        //this.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                        this.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }

                    //将光标定位到指定的位置
                    CharSequence text = this.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    //调用setCompoundDrawables方法时，必须要为drawable指定大小，不然不会显示在界面上
                    setEyesIconVisible();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count>0){
            this.setActivated(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}
