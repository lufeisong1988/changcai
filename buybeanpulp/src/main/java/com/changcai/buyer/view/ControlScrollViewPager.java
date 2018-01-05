package com.changcai.buyer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @description 可控制是否滚动的viewpager
 * @author zhoujun
 * @date 2014年10月22日 下午11:33:54
 * @version 1.0
 */
public class ControlScrollViewPager extends ViewPager {
	/**
	 * 是否可滚动
	 */
	private boolean isScrollable = true;

	public ControlScrollViewPager(Context context) {
		super(context);
	}

	public ControlScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollable(boolean enable) {
		isScrollable = enable;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}
}
