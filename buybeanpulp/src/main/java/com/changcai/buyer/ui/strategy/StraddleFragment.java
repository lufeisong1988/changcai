package com.changcai.buyer.ui.strategy;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.SpotFolderListBean;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.LogBlockUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.view.AutoEmptyView;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.MyAlertDialog;
import com.changcai.buyer.view.RotateDotsProgressView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/7/26.
 * 套利图景
 */

public class StraddleFragment extends BaseFragment implements PromptGoodsContract.View {
    private final static String assetsFilePath = "file:///android_asset/strategy/index.html";

    @BindView(R.id.wb_strategy_data)
    WebView wbStrategyData;
    @BindView(R.id.tv_strategy_content)
    TextView tvStrategyContent;
    @BindView(R.id.iv_watermark)
    ImageView ivWatermark;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    @BindView(R.id.tv_declare)
    CustomFontTextView tvDeclare;
    @BindView(R.id.levelJudgementView)
    LevelJudgementView levelJudgementView;
    @BindView(R.id.autoEmptyView)
    AutoEmptyView autoEmptyView;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.paperAutoEmptyView)
    AutoEmptyView paperAutoEmptyView;



    private Bundle authenticationBundle;
    private SpotFolderListBean currentSpotFolderListBean;
    private boolean isAuth;
    private PromptGoodsContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.straddle_layout, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        autoEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(Constants.STRATEGY_REFRESH,null);
            }
        });
        paperAutoEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionFromServer(isAuth);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(authenticationBundle != null){
            requestPermissionFromServer(isAuth);
        }
        if(savedInstanceState != null){
            authenticationBundle = savedInstanceState.getBundle("authenticationBundle");
            isAuth = savedInstanceState.getBoolean("isAuth");
            requestPermissionFromServer(isAuth);
        }
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden && authenticationBundle != null){
            requestPermissionFromServer(isAuth);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        baseUnbinder.unbind();
        presenter.detach();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("authenticationBundle",authenticationBundle);
        outState.putBoolean("isAuth",isAuth);
    }

    public Bundle getAuthenticationBundle() {
        return authenticationBundle;
    }

    /**
     * 传递数据
     *
     * @param bundle
     */
    public void setAuthenticationBundle(Bundle bundle) {
        authenticationBundle = bundle;
    }

    /**
     * 是否授权访问
     *
     * @param isAuth
     */
    public void requestPermissionFromServer(boolean isAuth) {
        this.isAuth = isAuth;
        if (isAuth) {
            showNormalView();
            initWebView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.getStrategyTarget("", "");
                }
            }, 1000);
        } else {
            showUnPermissionView();
        }
    }

    private void showNormalView() {
        llContent.setVisibility(View.VISIBLE);
        tvDeclare.setVisibility(View.VISIBLE);
        paperAutoEmptyView.setVisibility(View.INVISIBLE);
        autoEmptyView.setVisibility(View.INVISIBLE);
        levelJudgementView.setVisibility(View.INVISIBLE);
    }
    private void showUnPermissionView() {
        llContent.setVisibility(View.INVISIBLE);
        tvDeclare.setVisibility(View.INVISIBLE);
        autoEmptyView.setVisibility(View.INVISIBLE);
        levelJudgementView.setVisibility(View.VISIBLE);
        levelJudgementView.showPermission(authenticationBundle, "spot");
    }
    private void showEmptyView() {
        llContent.setVisibility(View.INVISIBLE);
        tvDeclare.setVisibility(View.INVISIBLE);
        autoEmptyView.setVisibility(View.VISIBLE);
        autoEmptyView.setEmptyStatus();
        levelJudgementView.setVisibility(View.INVISIBLE);
    }
    private void showPaperView(String content) {
        llContent.setVisibility(View.VISIBLE);
        tvDeclare.setVisibility(View.VISIBLE);
        ivWatermark.setVisibility(View.VISIBLE);
        tvStrategyContent.setVisibility(View.VISIBLE);
        paperAutoEmptyView.setVisibility(View.INVISIBLE);
        autoEmptyView.setVisibility(View.INVISIBLE);
        levelJudgementView.setVisibility(View.INVISIBLE);
        tvStrategyContent.setText(content);
    }


    private void showNetError(){
        llContent.setVisibility(View.INVISIBLE);
        tvDeclare.setVisibility(View.INVISIBLE);
        autoEmptyView.setVisibility(View.VISIBLE);
        autoEmptyView.setErrorStatus();
        levelJudgementView.setVisibility(View.INVISIBLE);
    }
    private void showSpotNetError(){
        llContent.setVisibility(View.VISIBLE);
        tvDeclare.setVisibility(View.VISIBLE);
        ivWatermark.setVisibility(View.INVISIBLE);
        paperAutoEmptyView.setVisibility(View.VISIBLE);
        paperAutoEmptyView.setErrorStatus();
        autoEmptyView.setVisibility(View.INVISIBLE);
        levelJudgementView.setVisibility(View.INVISIBLE);
    }
    /**
     * 解析bundle
     *
     * @return
     */
    private StrategyInitModel parseStrategyInitModel() {
        if (authenticationBundle != null && authenticationBundle.containsKey("StrategyInitModel")) {
            StrategyInitModel strategyInitModel = (StrategyInitModel) authenticationBundle.getSerializable("StrategyInitModel");
            return strategyInitModel;
        } else {
            return null;
        }
    }


    /**
     * 初始化webview
     */
    void initWebView() {
        wbStrategyData.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });
        wbStrategyData.addJavascriptInterface(new JavaScriptinterface(), "StrategyModel");
        initWebSettings(wbStrategyData);
        wbStrategyData.loadUrl(assetsFilePath);

    }

    public void showLoading() {
//        progress.setVisibility(View.VISIBLE);
//        progress.showAnimation(true);
    }

    @Override
    public void dismissLoading() {
//        progress.setVisibility(View.GONE);
//        progress.refreshDone(true);
    }


    String[] targetId = new String[0];
    String[] dataUrlParam = new String[0];
    String[] name = new String[0];
    String[] code = new String[0];
    @Override
    public void initStrategyTarget(final String[] targetId,final String[] dataUrlParam,final String[] name,final String[] code) {
        this.targetId = targetId;
        this.dataUrlParam = dataUrlParam;
        this.name = name;
        this.code = code;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String tmpName = Arrays.toString(name);
                tmpName = tmpName.substring(1,tmpName.length() - 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    wbStrategyData.evaluateJavascript("javascript:updateSpotTarget('" + tmpName + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                } else {
                    wbStrategyData.loadUrl("javascript:updateSpotTarget('" + tmpName +"')");
                }
            }
        });
    }

    @Override
    public void initStrategyTargetFail() {
        showSpotNetError();
        showErrorDialog(getString(R.string.net_error));
    }

    @Override
    public void initStrategyTargetEmpty() {
    }

    @Override
    public void initStrategyTargetData(String[] date, String[] price, String[] point, int minY, int maxY,String currentDate) {
        updateWebview(date, price, point, minY, maxY,currentDate);
    }

    @Override
    public void initStrategyTargetDataFail() {
        showSpotNetError();
        showErrorDialog(getString(R.string.net_error));
    }

    @Override
    public void initStrategyTargetDataEmpty() {
        ToastUtil.showLong(getActivity(),"当前数据为空");
    }


    @Override
    public void initPaper(String content) {
        showPaperView(content);
    }

    @Override
    public void initPaperEmpty() {
        ToastUtil.showLong(getActivity(),"不存在文章");
    }

    @Override
    public void initPaperFail() {
        tvStrategyContent.setText("");
        showSpotNetError();
        showErrorDialog(getString(R.string.net_error));
    }

    @Override
    public void setPresenter(PromptGoodsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        MyAlertDialog alertDialog = new MyAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", message);
        bundle.putInt("icon", R.drawable.icon_alt_fail);
        alertDialog.setArguments(bundle);
        alertDialog.show(getChildFragmentManager(), "alert");
    }


    /**
     * 刷新webview页面
     *
     * @param date
     * @param price
     * @param point
     */
    private void updateWebview(final String[] date, final String[] price, final String[] point, final int minY, final int maxY,final String currentDate) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String tmpDate = Arrays.toString(date);
                String tmpPrice = Arrays.toString(price);
                String tmpPoint = Arrays.toString(point);
                tmpDate = tmpDate.substring(1, tmpDate.length() - 1);
                tmpPrice = tmpPrice.substring(1, tmpPrice.length() - 1);
                tmpPoint = tmpPoint.substring(1, tmpPoint.length() - 1);

                LogBlockUtil.i("策略","date = " + tmpDate + " price = " + tmpPrice + " point = " + tmpPoint + " minY = " + minY + " maxY = " + maxY + " currentDate = " +  currentDate);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    wbStrategyData.evaluateJavascript("javascript:updateSpotDate('" + tmpDate + "','" + tmpPrice + "','" + tmpPoint + "'," + minY + "," + maxY + ",'"+ currentDate+ "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                } else {
                    wbStrategyData.loadUrl("javascript:updateSpotDate('" + tmpDate + "','" + tmpPrice + "','" + tmpPoint + "'," + minY + "," + maxY + ",'"+ currentDate+ "')");
                }
            }
        });

    }
    public void requestNetError(){
        showNetError();
    }

    class JavaScriptinterface {
        String date;
        @JavascriptInterface
        public void spotTargetUnexistAndPaperExist(final String date){
            LogUtil.i("策略","spotTargetUnexistAndPaperExist date = " + date);
            updatePaper(date);
        }
        @JavascriptInterface
        public void spotTargetExistUnChooseAndPaperExist(final String date){
            LogUtil.i("策略","spotTargetExistUnChooseAndPaperExist date = " + date);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(date != null && !TextUtils.isEmpty(date)){
                        presenter.getStrategyTarget(date,"");
                    }

                }
            });
        }
        @JavascriptInterface
        public void spotTargetChooseAndPaperExist(final String date){
            LogUtil.i("策略","spotTargetChooseAndPaperExist date = " + date);
            updatePaper(date);

        }
        @JavascriptInterface
        public void spotPaperEmpty(){
            LogUtil.i("策略","spotPaperEmpty");
            ToastUtil.showLong(getActivity(), getActivity().getResources().getString(R.string.strategy_paper_empty));
        }
        @JavascriptInterface
        public void updateSpotTargetData(final String tmpIndex){
            LogUtil.i("策略","updateSpotTargetData");
            final int index = Integer.parseInt(tmpIndex);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(index >= 0 && targetId.length > index && code.length > index &&  dataUrlParam.length > index && !TextUtils.isEmpty(date)){
                        presenter.getStrategyTargetData(targetId[index],"",code[index],dataUrlParam[index],date);
                    }
                }
            });

        }

        void updatePaper(final String date){
            this.date = date;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(date != null && !TextUtils.isEmpty(date)){
                        presenter.getSpotPaper("", date);
                    }
                }
            });
        }

    }

}
