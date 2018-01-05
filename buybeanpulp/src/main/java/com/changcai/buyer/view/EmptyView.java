package com.changcai.buyer.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.rx.RxBus;

/**
 * Created by liuxingwei on 2017/6/13.
 */

public class EmptyView extends LinearLayout{

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

   private void init(Context context){
       View view  = LayoutInflater.from(context).inflate(R.layout.empty_error,this,false);
       this.addView(view);
       findViewById(R.id.tv_refresh).setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               RxBus.get().post("errorRefresh",new Boolean(true));
               setVisibility(GONE);
           }
       });
    }
}
