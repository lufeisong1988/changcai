package com.changcai.buyer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;

/**
 * Created by huangjian299 on 16/6/7.
 */
public class ImageText extends LinearLayout {
    private Context mContext = null;
    private ImageView ivImage = null;
    private TextView mTextView = null;
    private TextView tvIndex = null;

    public ImageText(Context context) {
        super(context);
        mContext = context;
    }

    public ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.image_text_layout, this, true);
        ivImage = (ImageView) findViewById(R.id.image_iamge_text);
        mTextView = (TextView) findViewById(R.id.text_iamge_text);
        tvIndex = (TextView) findViewById(R.id.unread_find_number);
        tvIndex.setVisibility(View.GONE);
    }

    public void setImage(int id) {
        if (ivImage != null) {
            ivImage.setImageResource(id);
        }
    }

    public void setText(String s) {
        if (mTextView != null) {
            mTextView.setText(s);
        }
    }

    public void showText(boolean isShow) {
        if (mTextView != null) {
            if (isShow) {
                mTextView.setVisibility(View.VISIBLE);
            } else {
                mTextView.setVisibility(View.GONE);
            }
        }
    }

    public  void showUnreadNumberText(){
        if(tvIndex != null){
            tvIndex.setVisibility(View.VISIBLE);
        }
    }

    public  void hideUnreadNumberText(){
        if(tvIndex != null){
            tvIndex.setVisibility(View.GONE);
        }
    }

    public void showUnreadNumberText(String number){
        if(tvIndex != null){
            tvIndex.setVisibility(View.VISIBLE);
            tvIndex.setText(number);
        }
    }


    private void setImageSize(int w, int h) {
        if (ivImage != null) {
            ViewGroup.LayoutParams params = ivImage.getLayoutParams();
            params.width = w;
            params.height = h;
            ivImage.setLayoutParams(params);
        }
    }


}
