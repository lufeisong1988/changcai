package com.changcai.buyer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.util.StringUtil;

/**
 * 倒计时控件
 * Created by huangjian299 on 16/6/12.
 */
public class TimeTextView extends TextView implements Runnable {
    private long[] times;
    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run = true;
    private String prefix = "还剩";
    private int type = 0;
    public static final int TYPE_2 = 1; //00:00:00类型

    public interface ITimerViewCallBack {
        void onTimerOut();
    }

    public ITimerViewCallBack iTimerViewCallBack;

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle();
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle();
    }

    public TimeTextView(Context context) {
        super(context);
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
    }

    public long getTime() {
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        return mday * nd + mhour * nh + mmin * nm + msecond * ns;
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mday--;
                    if (mday < 0) {
                        run = false;
                        if (iTimerViewCallBack != null) {
                            iTimerViewCallBack.onTimerOut();
                        }
                    }
                }
            }
        }
    }

    public boolean getRunFlag() {
        return run;
    }

    @Override
    public void run() {
        if (run) {
            ComputeTime();
        }
        if (run) {
            String strTime = prefix;
            if (type == TYPE_2) {
                if (mday != 0) {
                    strTime += mday + "天";
                }
                strTime += StringUtil.longToString(mhour) + ":"
                        + StringUtil.longToString(mmin) + ":"
                        + StringUtil.longToString(msecond);
                Log.d("TimeTextView1", strTime);
            } else {
                if (mday != 0) {
                    strTime += mday + "天";
                }
                strTime += StringUtil.longToString(mhour) + "时"
                        + StringUtil.longToString(mmin) + "分" + StringUtil.longToString(msecond)
                        + "秒";
            }
            Log.d("TimeTextView1", strTime);
            this.setText(Html.fromHtml(strTime));
            postDelayed(this, 1000);
        }

    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void setCallBack(ITimerViewCallBack iTimerViewCallBack) {
        this.iTimerViewCallBack = iTimerViewCallBack;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}