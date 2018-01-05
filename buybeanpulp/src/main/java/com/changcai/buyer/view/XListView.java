/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.changcai.buyer.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.changcai.buyer.R;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;


public class
XListView extends ListView implements OnScrollListener {

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    private boolean updateHight = true;

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    // -- header view
    private XListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private TextView mHeaderTimeViewPrefix;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private XListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private float mOffsetRadio = 1.8f; // support iOS like pull
    // feature.

    //当正在下拉刷新的时候，用户再次继续下拉默认不回调IXListViewListener的onRefresh方法

    private boolean mEnableOnRefreshWhileRefreshing = false;


    private static final int AUTO_REFRESH = 1001;

    private AutoRefreshHandler mAutoRefreshHandler;

    //    private SimpleDateFormat mSDF = new SimpleDateFormat(
//            "yyyy-MM-dd HH:mm");
    private SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    private SimpleDateFormat dyaSDF = new SimpleDateFormat("yyyy-MM-dd");


    private Date mDate = new Date();

    private Context mContext;


    public boolean ismPullRefreshing() {
        return mPullRefreshing;
    }

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        mContext = context;
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);
        WeakReference<XListView> reference = new WeakReference<>(this);
        mAutoRefreshHandler = new AutoRefreshHandler(reference);
        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView
                .findViewById(R.id.xlistview_header_time);
        mHeaderTimeViewPrefix = (TextView) mHeaderView
                .findViewById(R.id.xlistview_header_time_prefix);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            //// TODO: 2017/6/14  切记这里是 显示和隐藏 footer的关键
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
            //make sure "pull up" don't show a line in bottom when listview with one page
//            mFooterView.setState(XListViewFooter.STATE_NO_MORE);
            setFooterDividersEnabled(false);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            //make sure "pull up" don't show a line in bottom when listview with one page
            setFooterDividersEnabled(true);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    public void setPullLoadEnable(boolean enable, int noMoreString, boolean isLoadMoreFail) {
        mFooterView.show();
        mEnablePullLoad = enable;
        mPullLoading = false;
        setFooterDividersEnabled(true);
        if (isLoadMoreFail) {
            mFooterView.setState(XListViewFooter.STATE_LOAD_MORE_FAIL, 0);
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        } else {
            mFooterView.setState(XListViewFooter.STATE_NO_MORE, noMoreString);
            mFooterView.setOnClickListener(null);
        }
    }

    public void stopRefresh(String text) {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            mHeaderView.setRefreshComplete(text);
            Observable.empty().delay(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            }, new Action0() {
                @Override
                public void call() {
                    resetHeaderHeight();
                    //400 ms之后执行
//                     mHeaderView.resetRefreshViewVisible();
                    Observable.empty().delay(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {
                            mHeaderView.resetRefreshViewVisible();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {

                        }
                    });

                }
            });
        }
        mDate.setTime(System.currentTimeMillis());

        String time = mSDF.format(mDate);
        setRefreshTime(time);
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            mHeaderView.setRefreshComplete(null);
            Observable.empty().delay(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            }, new Action0() {
                @Override
                public void call() {
                    resetHeaderHeight();
                    //400 ms之后执行
//                     mHeaderView.resetRefreshViewVisible();
                    Observable.empty().delay(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {
                            mHeaderView.resetRefreshViewVisible();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {

                        }
                    });

                }
            });
        }
        mDate.setTime(System.currentTimeMillis());

        String time = mSDF.format(mDate);
        setRefreshTime(time);
    }


    /**
     * stop refresh, reset header view depend on refresh weather success.
     */
    public void stopRefresh(long refreshTime) {
//		Log.d("HomeFragment", "XListView stopRefresh: " + refreshTime);
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }

        if (refreshTime > 0) {
            mHeaderTimeViewPrefix.setVisibility(VISIBLE);
            mHeaderTimeView.setVisibility(VISIBLE);
            mDate.setTime(refreshTime);
            String time = mSDF.format(mDate);

            setRefreshTime(time);
        } else {
            mHeaderTimeViewPrefix.setVisibility(INVISIBLE);
            mHeaderTimeView.setVisibility(INVISIBLE);
        }

    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }


    /**
     * 开始自动下拉刷新，有弹性下拉效果
     */
    public void startAutoRefresh() {
        mLastY = -1; // reset
        // 判断是否在第一行
        if (getFirstVisiblePosition() != 0) {
            smoothScrollToPosition(0);
        }
        //增加延时，不然不能自动下拉滑动
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh();
            }
        }, 500);

    }

    private static class AutoRefreshHandler extends Handler {

        private int mRecord;

        private WeakReference<XListView> mReference;

        public AutoRefreshHandler(WeakReference<XListView> reference) {
            this.mReference = reference;
        }

        @Override
        public void handleMessage(Message msg) {
            //need check context state ,if finishing no need handle
            if (mReference == null) {
                mRecord = 0;
                removeMessages(AUTO_REFRESH);
                return;
            }
            XListView xListView = mReference.get();
            if (xListView == null || xListView.mContext == null ||
                    (xListView.mContext instanceof Activity && (((Activity) (xListView.mContext)).isFinishing()))) {
                mRecord = 0;
                removeMessages(AUTO_REFRESH);
                return;
            }
            if (xListView.mHeaderView.getVisiableHeight() > xListView.mHeaderViewHeight) {
                if (xListView.mListViewListener != null) {
                    xListView.mListViewListener.onPullRefresh();
                }
                mRecord = 0;
                xListView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING, xListView.visiblePercent(xListView.mHeaderView.getVisiableHeight(), xListView.mHeaderViewHeight));
                xListView.resetHeaderHeight();
                removeMessages(AUTO_REFRESH);
            } else {
                xListView.updateHeaderHeight(mRecord);
                mRecord = mRecord + 10;
                sendEmptyMessageDelayed(AUTO_REFRESH, 40);
            }
        }
    }

    private void autoRefresh() {
        if (mEnablePullRefresh && !mPullRefreshing) {
            mPullRefreshing = true;
            mAutoRefreshHandler.sendEmptyMessage(AUTO_REFRESH);
        }
    }

    /**
     * 当正处于下拉刷新的时候，如果用于再次下拉，设置是否回调onRefresh方法
     *
     * @param enable
     */
    public void setEnableOnRefreshWhileRefreshing(boolean enable) {
        this.mEnableOnRefreshWhileRefreshing = enable;
    }

    /**
     * 获取上次刷新时间
     *
     * @return
     */
    public Date getRefreshTime() {
        try {
            return mSDF.parse(mHeaderTimeView.getText().toString());

        } catch (Exception ex) {

        }

        return null;
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        if (TextUtils.isEmpty(time)) {
            mHeaderView.setRefreshTimeViewVisibility(false);
        } else {
            mHeaderView.setRefreshTimeViewVisibility(true);
            mHeaderTimeView.setText(time);
        }
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY, visiblePercent(mHeaderView.getVisiableHeight(), mHeaderViewHeight));
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL, visiblePercent(mHeaderView.getVisiableHeight(), mHeaderViewHeight));
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
                // more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onPullLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    //第一个item可见并且头部部分显示或者下拉操作,则表示处于下拉刷新状态
                    updateHeaderHeight(deltaY / mOffsetRadio);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    //最后一个item可见并且footer被上拉显示或者上拉操作,则表示处于上拉刷新状态
                    if (updateHight) {
                        updateFooterHeight(-deltaY / mOffsetRadio);
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    boolean flag = false;
                    if (!mEnableOnRefreshWhileRefreshing) {
                        flag = mHeaderView.isRefreshing();
                    }
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight && !flag) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING, visiblePercent(mHeaderView.getVisiableHeight(), mHeaderViewHeight));
                        if (mListViewListener != null) {
                            mListViewListener.onPullRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnablePullLoad
                            && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
                            && !mPullLoading) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        void onPullRefresh();

        void onPullLoadMore();
    }

    public void startAutoLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onPullLoadMore();
        }
        resetFooterHeight();
    }

    /**
     * 设置【下拉刷新、正在加载、上次更新、时间】字体颜色
     *
     * @param color
     */
    public void setHeaderTextColor(int color) {
        mHeaderView.setHeaderTextColor(color);
        mHeaderTimeView.setTextColor(color);
        mHeaderTimeViewPrefix.setTextColor(color);
    }

    public void setHeaderBgNewStyle() {
        mHeaderView.setHeaderBgNewStyle();
        setHeaderTextColor(getResources().getColor(R.color.black));
    }

    public void setHeaderBgDefaultStyle() {
        mHeaderView.setHeaderBgDefaultStyle();
        setHeaderTextColor(getResources().getColor(R.color.black));
    }

    public void setHeaderMargin(int left, int right, int top, int bottom) {
        mHeaderView.setHeaderMargin(left, top, right, bottom);
    }

    public void setUpdateHight(boolean updateHight) {
        this.updateHight = updateHight;
    }

    /**
     * 设置下拉刷新显示文字
     *
     * @param text
     */
    public void setHeadReadyStateText(String text) {
        mHeaderView.setReadyStateText(text);
    }

    /**
     * 设置下拉刷新敏感度
     *
     * @param radio
     */
    public void setOffsetRadio(float radio) {
        mOffsetRadio = radio;
    }


    private float visiblePercent(int visibleHeight, int mHeaderViewHeight) {
        if (mHeaderViewHeight == 0) {
            return 0.1f;
        }

        BigDecimal bigDecimal = new BigDecimal(visibleHeight);
        BigDecimal bigDecimal1 = new BigDecimal(mHeaderViewHeight);

        return bigDecimal.divide(bigDecimal1, 2, BigDecimal.ROUND_HALF_UP).floatValue();

    }

}
