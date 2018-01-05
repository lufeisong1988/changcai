package com.changcai.buyer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.changcai.buyer.R;

/**
 * Created by liuxingwei on 2017/7/28.
 */

public class PromptHeaderView extends LinearLayout {
    protected CustomFontTextView mCustomFontTextView;

    public PromptHeaderView(Context context) {
        super(context);
        initView();
    }

    public PromptHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mCustomFontTextView = (CustomFontTextView) layoutInflater.inflate(R.layout.prompt_header_view, this, false);
        addView(mCustomFontTextView);
    }

    public void setCustomFontText(String text){
        mCustomFontTextView.setText(text);
    }

}
