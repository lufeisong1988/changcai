package com.changcai.buyer.ui.strategy;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.ui.strategy.present.GetSalesAmontuPrensent;
import com.changcai.buyer.util.ArrayUtil;
import com.changcai.buyer.util.DensityUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.AlertDialog;
import com.changcai.buyer.view.GraphLineView;
import com.changcai.buyer.view.RotateDotsProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.changcai.buyer.ui.strategy.CashReportFragment.XList;
import static com.changcai.buyer.ui.strategy.CashReportFragment.YList;
import static com.changcai.buyer.ui.strategy.CashReportFragment.currentMonthPosition;
import static com.changcai.buyer.ui.strategy.CashReportFragment.dateStr;
import static com.changcai.buyer.ui.strategy.CashReportFragment.isExistBean;
import static com.changcai.buyer.ui.strategy.StrategyFragment.viewPagerCurrentItem;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class CashReportOneFragment extends BaseFragment implements CashReportOneViewModel {
    private final static String assetsFilePath = "file:///android_asset/MonthlyAnalysis/Analysis.html";


    GetSalesAmontuPrensent getSalesAmontuPrensent;
    @BindView(R.id.glView)
    GraphLineView glView;
    @BindView(R.id.hsv_cashreport_one)
    HorizontalScrollView hsvCashreportOne;
    @BindView(R.id.iv_nav_help_top)
    ImageView ivNavHelpTop;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.wb_cashreport_one)
    WebView wbCashreportOne;
    @BindView(R.id.iv_nav_help_bottom)
    ImageView ivNavHelpBottom;
    @BindView(R.id.dots_progress)
    RotateDotsProgressView dotsProgress;
    LinearLayout emptyview;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rl_parent_view)
    RelativeLayout rlParentView;

    private boolean isCreatedView = false;


    public static CashReportOneFragment getInstance() {
        CashReportOneFragment cashReportOneFragment = new CashReportOneFragment();
        return cashReportOneFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.d("CashReportOneFragment", "onCreate");
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isCreatedView) {
            isCreatedView = true;
        }
        View view = inflater.inflate(R.layout.fragment_cashreport_one, container, false);
        LogUtil.d("CashReportOneFragment", "onCreateView");
        baseUnbinder = ButterKnife.bind(this, view);
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        emptyview = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.view_emptydata, null);
        emptyview.setLayoutParams(lp);
        emptyview.setGravity(Gravity.CENTER);
        initData();
        initListener();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d("CashReportOneFragment", "hidden = " + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d("CashReportOneFragment", "isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSalesAmontuPrensent.onDestory();
        activity = null;
    }

    private void initData() {
        hsvCashreportOne.setHorizontalScrollBarEnabled(false);
        getSalesAmontuPrensent = new GetSalesAmontuPrensent(this, activity);
        getSalesAmontuPrensent.getSalesAmount();

    }

    private void initListener() {

        ivNavHelpTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(activity).builder()
                        .setTitle("帮助说明")
                        .setMsg(isExistBean.getSales())
                        .show();
            }
        });
        ivNavHelpBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(activity).builder()
                        .setTitle("帮助说明")
                        .setMsg(isExistBean.getLinks())
                        .show();
            }
        });
        hsvCashreportOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (XList.size() > 0 && YList.size() > 0) {
                            final int p = (int) (hsvCashreportOne.getScrollX() / glView.getSpaceXWidth()) + (hsvCashreportOne.getScrollX() % glView.getSpaceXWidth() > glView.getSpaceXWidth() / 2 ? 1 : 0);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    currentMonthPosition = p;
                                    rlBottom.removeView(emptyview);
                                    if (activity == null)
                                        return;
                                    hsvCashreportOne.smoothScrollTo((int) (glView.getSpaceXWidth() * currentMonthPosition), (int) hsvCashreportOne.getY());
                                    hsvCashreportOne.requestLayout();
                                    glView.smoothscrolltoposition(currentMonthPosition);
                                    if (!NetUtil.checkNet(activity)) {
                                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                                    } else {
                                        getSalesAmontuPrensent.getTopFiveData(currentMonthPosition);
                                    }
                                }
                            });
                        }
                        break;
                }
                return false;
            }
        });
    }

    WebViewClient getWebViewClient = new WebViewClient() {
        /**
         * 在开始加载网页时会回调
         */
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        /**
         * 在结束加载网页时会回调
         */
        public void onPageFinished(WebView view, String url) {

        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         */
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }

        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理
         */
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        }

    };

    protected void initWebSettings(WebView wb) {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(getWebViewClient);

        WebSettings webSettings = wb.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
    }

    @Override
    public void updateSalesAmountData(List<String> Xs, List<String> Ys, SalesAmountBean.DateStrBean dateStrBean, final int currnetPosition) {
        rlBottom.removeView(emptyview);
        rlParentView.removeView(emptyview);
//        rlBottom.removeView(autoEmptyView);
        XList = Xs;
        YList = Ys;
        LogUtil.d("ReportThree", "updateSalesAmountData");
        dateStr = dateStrBean;
        if (currnetPosition == -1) {//如果数据不存在就赋值第一个月份的
            currentMonthPosition = 0;
        }else{
            currentMonthPosition = currnetPosition;
        }
//        if (currentMonthPosition == -1) {//数据不存在
//            rlParentView.addView(emptyview);
//        }
        if (XList.size() > 0 && YList.size() > 0) {
            glView.setData(Ys, Xs, ArrayUtil.getMax(Ys));
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (activity == null)
                        return;
                    hsvCashreportOne.smoothScrollTo((int) (glView.getSpaceXWidth() * (currentMonthPosition)), (int) hsvCashreportOne.getY());
                    hsvCashreportOne.requestLayout();
                    glView.smoothscrolltoposition(currentMonthPosition);
                    if (!NetUtil.checkNet(activity)) {
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                    } else {
                        getSalesAmontuPrensent.getTopFiveData(currentMonthPosition);
                    }

                }
            });
        }

    }

    /**
     * 刷新H5
     *
     * @param str
     */
    @Override
    public void updateToFiveData(String str) {
        wbCashreportOne.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                view.loadUrl(url);
                return false;
            }

        });
        wbCashreportOne.addJavascriptInterface(new JavaScriptinterface(str),
                "MonthlyAnalysisModel");
        initWebSettings(wbCashreportOne);
        wbCashreportOne.loadUrl(assetsFilePath);
    }

    @Override
    public void showLoading() {
        dotsProgress.setVisibility(View.VISIBLE);
        dotsProgress.showAnimation(true);
    }

    @Override
    public void dismissLoading() {
        dotsProgress.setVisibility(View.GONE);
        dotsProgress.refreshDone(true);
    }

    @Override
    public void showNetErrorDialog() {
        if (viewPagerCurrentItem == 2) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    class JavaScriptinterface {
        String str;

        public JavaScriptinterface(String str) {
            this.str = str;
        }

        @JavascriptInterface
        public String jsCallOcAcquireHeight() {
            LogUtil.d("TAG", "webView.height:" + DensityUtil.px2dip(activity, wbCashreportOne.getHeight()) * 2.7);
            return String.valueOf(DensityUtil.px2dip(activity, wbCashreportOne.getHeight()) * 2.8) + "px";
        }

        @JavascriptInterface
        public String jsCallOcAcquireMonth() {
            return str;
        }

        @JavascriptInterface
        public void jsCallOcShowCurrentMonthData(String str) {
            LogUtil.d("TAG", "str = " + str);
            if (str.equals("请求成功")) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rlBottom.removeView(emptyview);
                        }
                    });
                }
            } else if (str.equals("暂无数据")) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rlBottom.removeView(emptyview);
                            rlBottom.addView(emptyview, 1);
                        }
                    });
                }
            } else if (str.equals("您的网络情况不稳定")) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rlBottom.removeView(emptyview);
                        }
                    });
                }
            }
        }

    }

    public void refresh() {
        LogUtil.d("CashReportOneFragment", "refresh");

        if (XList.size() > 0 && YList.size() > 0) {
            glView.setData(YList, XList, ArrayUtil.getMax(YList));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (activity == null)
                        return;
                    rlBottom.removeView(emptyview);
                    hsvCashreportOne.smoothScrollTo((int) (glView.getSpaceXWidth() * (currentMonthPosition)), (int) hsvCashreportOne.getY());
                    hsvCashreportOne.requestLayout();
                    glView.smoothscrolltoposition(currentMonthPosition);
                    if (!NetUtil.checkNet(activity)) {
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                    } else {
                        getSalesAmontuPrensent.getTopFiveData(currentMonthPosition);
                    }
                }
            }, 100);
        }


    }
}
