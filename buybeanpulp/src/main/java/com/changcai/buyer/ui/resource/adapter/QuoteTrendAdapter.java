package com.changcai.buyer.ui.resource.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.resource.bean.Item;
import com.changcai.buyer.ui.resource.bean.QuoteBean;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.DensityConst;
import com.changcai.buyer.util.DensityUtil;
import com.changcai.buyer.util.LogUtil;
import com.jingchen.pulltorefresh.PinnedSectionListView;

import java.util.List;


/**
 * Created by lufeisong on 2017/8/31.
 */

public class QuoteTrendAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context context;
    private List<QuoteBean.AllQuoteBean.ResultBean> items;
    private final static String assetsFilePath = "file:///android_asset/resourceApp/ResourcesOffer/resourcesOffer.html";
    Typeface typeFace;
    AdviceListener adviceListener;
    public QuoteTrendAdapter(Context context, List<QuoteBean.AllQuoteBean.ResultBean> items,AdviceListener adviceListener) {
        this.context = context;
        this.items = items;
        this.adviceListener  = adviceListener;
        typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/ping_fang_light.ttf");
        DensityConst.initDensity((Activity) context);
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return items.get(viewType).type != Item.GROUP;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder vh = null;
        if(view == null){
            LogUtil.d("TAG","position = " + i + " is null");
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_resource,null);
            vh.wb = (WebView) view.findViewById(R.id.wb_resoure);
            vh.ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
            vh.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            vh.iv_line = (ImageView) view.findViewById(R.id.iv_line);
            vh.iv_line_top = (ImageView) view.findViewById(R.id.iv_line_top);
            view.setTag(vh);
        }else{
            LogUtil.d("TAG","position = " + i + " not null");
            vh = (ViewHolder) view.getTag();
        }
        QuoteBean.AllQuoteBean.ResultBean item = items.get(i);
        if(i == 0){
            vh.wb.setVisibility(View.VISIBLE);
            vh.ll_group.setVisibility(View.GONE);
            vh.ll_item.setVisibility(View.GONE);
            vh.iv_line_top.setVisibility(View.VISIBLE);
            vh.iv_line.setVisibility(View.GONE);
            vh.wb.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    view.loadUrl(url);
                    return false;
                }

            });
            vh.wb.addJavascriptInterface(new JavaScriptinterface(item.headStr,item.domainsName,String.valueOf(DensityUtil.px2dip(context,vh.wb.getHeight()) * 2.5)),
                    "ResourcesOfferModel");
            initWebSettings(vh.wb);
            vh.wb.loadUrl(assetsFilePath);
        }else{
            if(item.type == Item.GROUP){
                vh.wb.setVisibility(View.GONE);
                vh.ll_group.setVisibility(View.VISIBLE);
                vh.ll_item.setVisibility(View.GONE);
                vh.iv_line_top.setVisibility(View.GONE);
                vh.iv_line.setVisibility(View.GONE);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(0)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(1)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(2)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(3)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(4)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(5)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_group.getChildAt(0)).getChildAt(6)).setTypeface(typeFace);
            }else if(item.type == Item.CHILD){
                ((TextView)vh.ll_item.getChildAt(0)).setTypeface(typeFace);
                ((TextView)vh.ll_item.getChildAt(2)).setTypeface(typeFace);
                ((TextView)vh.ll_item.getChildAt(4)).setTypeface(typeFace);
                ((TextView)vh.ll_item.getChildAt(6)).setTypeface(typeFace);
                ((TextView)vh.ll_item.getChildAt(8)).setTypeface(typeFace);
                ((TextView)vh.ll_item.getChildAt(10)).setTypeface(typeFace);
                ((TextView)((LinearLayout)vh.ll_item.getChildAt(12)).getChildAt(1)).setTypeface(typeFace);
                if(i % 2 == 0){
                    vh.ll_item.getChildAt(0).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(2).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(4).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(6).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(8).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(10).setBackgroundColor(context.getResources().getColor(R.color.white));
                    vh.ll_item.getChildAt(12).setBackgroundColor(context.getResources().getColor(R.color.white));
                }else{
                    vh.ll_item.getChildAt(0).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(2).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(4).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(6).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(8).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(10).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                    vh.ll_item.getChildAt(12).setBackgroundColor(context.getResources().getColor(R.color.resource_item_bg));
                }
                vh.wb.setVisibility(View.GONE);
                vh.ll_group.setVisibility(View.GONE);
                vh.ll_item.setVisibility(View.VISIBLE);
                vh.iv_line_top.setVisibility(View.GONE);
                vh.iv_line.setVisibility(View.VISIBLE);
                ((TextView)vh.ll_item.getChildAt(0)).setText(item.getFactoryName());
                ((TextView)vh.ll_item.getChildAt(2)).setText(item.getSupplierName() );
                ((TextView)vh.ll_item.getChildAt(4)).setText(item.getDeliveryLocation());
                ((TextView)vh.ll_item.getChildAt(6)).setText(item.getShowPrice());
                if(item.getPrice().equals("") || item.getPrice().equals("持平")) {
                    ((TextView)vh.ll_item.getChildAt(8)).setCompoundDrawables(null,null,null,null);
                    ((TextView)vh.ll_item.getChildAt(6)).setTextColor(Color.parseColor("#26272A"));
                    ((TextView)vh.ll_item.getChildAt(8)).setTextColor(Color.parseColor("#26272A"));
                    ((TextView)vh.ll_item.getChildAt(8)).setText(item.getPrice());
                }else if(item.getPrice().substring(0,1).equals("-")){
                    Drawable mRouteOnDraw = context.getResources().getDrawable(
                            R.drawable.icon_godown);
//                    mRouteOnDraw.setBounds(30,0,42,15);
                    mRouteOnDraw.setBounds(DensityConst.getPx(10),0,DensityConst.getPx(16),DensityConst.getPx(8));
                    ((TextView)vh.ll_item.getChildAt(8)).setCompoundDrawables(mRouteOnDraw,null,null,null);
                    ((TextView)vh.ll_item.getChildAt(6)).setTextColor(context.getResources().getColor(R.color.resource_item_green));
                    ((TextView)vh.ll_item.getChildAt(8)).setTextColor(context.getResources().getColor(R.color.resource_item_green));
                    ((TextView)vh.ll_item.getChildAt(8)).setText(item.getPrice().substring(1,item.getPrice().length()));
                }else{
                    Drawable mRouteOnDraw = context.getResources().getDrawable(
                            R.drawable.icon_goup);
                    mRouteOnDraw.setBounds(DensityConst.getPx(10),0,DensityConst.getPx(16),DensityConst.getPx(8));
                    ((TextView)vh.ll_item.getChildAt(8)).setCompoundDrawables(mRouteOnDraw,null,null,null);
                    ((TextView)vh.ll_item.getChildAt(6)).setTextColor(context.getResources().getColor(R.color.resource_item_red));
                    ((TextView)vh.ll_item.getChildAt(8)).setTextColor(context.getResources().getColor(R.color.resource_item_red));
                    ((TextView)vh.ll_item.getChildAt(8)).setText(item.getPrice());
                }

                ((TextView)vh.ll_item.getChildAt(10)).setText(item.getDeliveryDate());
                ((TextView)((LinearLayout)vh.ll_item.getChildAt(12)).getChildAt(1)).setText(DateUtil.parserTimeToMMddHHmm(String.valueOf(item.getQuoteDate())) + "更新");
                ((Button)((LinearLayout)vh.ll_item.getChildAt(12)).getChildAt(0)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adviceListener.showAdviceDialog(i);

                    }
                });
            }
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    class ViewHolder{
        LinearLayout ll_group;
        LinearLayout ll_item;
        WebView wb;
        ImageView iv_line;
        ImageView iv_line_top;
    }


    WebViewClient getWebViewClient = new WebViewClient(){
        /**
         * 在开始加载网页时会回调
         */
        public void onPageStarted(WebView view, String url, Bitmap favicon){

        }
        /**
         * 在结束加载网页时会回调
         */
        public void onPageFinished(WebView view, String url){

        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         */
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl){

        }
        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理
         */
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){

        }

    };

    protected void initWebSettings(WebView wb){
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(getWebViewClient);

        WebSettings webSettings=wb.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
//        webSettings.setDatabasePath(BaseActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
    }
    class JavaScriptinterface{
        String str;
        String domainsName;
        String height;
        public JavaScriptinterface(String str,String domainsName,String height) {
            this.str = str;
            this.domainsName = domainsName;
            this.height = height;
        }
        @JavascriptInterface
        public String jsCallOcAcquireHeight(){
            return height+ "px";
        }

        @JavascriptInterface
        public String jsCallObjcAcquireSpotGoodsNumber(){
            return str;
        }

        @JavascriptInterface
        public String jsCallObjcAcquireCurrentRegion(){
            return domainsName;
        }
    }
    public interface AdviceListener{
        void showAdviceDialog(int position);
    }
}
