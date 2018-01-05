package com.changcai.buyer.ui.strategy.model;

import android.content.Context;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.ui.strategy.present.GetSalesAmountPresentCallback;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class GetSalesAmonutModel implements GetSalesAmonutModelInterface{
    private GetSalesAmountPresentCallback getSalesAmountPresentCallback;
    private Context context;

    public GetSalesAmonutModel(GetSalesAmountPresentCallback getSalesAmountPresentCallback,Context context) {
        this.getSalesAmountPresentCallback = getSalesAmountPresentCallback;
        this.context = context;
    }

    @Override
    public void getSalesAmount() {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));

        VolleyUtil.getInstance().httpPost(context, Urls.BASE_URL + Urls.GET_SALES_AMOUNT, map, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                SalesAmountBean salesAmountBean = new SalesAmountBean();
                JsonObject obj = response;
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    String errorCode = jsonObject.getString("errorCode");
                    if(errorCode.equals("0")){
                        //1
                        JSONObject resultObject = jsonObject.getJSONObject("resultObject");
                        JSONObject dateStr = resultObject.getJSONObject("dateStr");
                        Iterator dateStrKeys = dateStr.keys();
                        List<SalesAmountBean.DateStrBean.MonthsBean> monthsBeenList = new ArrayList<SalesAmountBean.DateStrBean.MonthsBean>();
                        SalesAmountBean.DateStrBean dateStrBean = new SalesAmountBean.DateStrBean();
                        while(dateStrKeys.hasNext()){
                            String key = (String) dateStrKeys.next();
                            ArrayList<String> values = new ArrayList<String>();
                            JSONArray valueArray = dateStr.getJSONArray(key);
                            for(int i = 0;i < valueArray.length();i++){
                                values.add((String) valueArray.get(i));
                            }
                            SalesAmountBean.DateStrBean.MonthsBean monthsBean = new SalesAmountBean.DateStrBean.MonthsBean();
                            monthsBean.setYear(key);
                            monthsBean.setMonths(values);
                            monthsBeenList.add(monthsBean);
                        }
                        Collections.sort(monthsBeenList, new Comparator<SalesAmountBean.DateStrBean.MonthsBean>() {
                            @Override
                            public int compare(SalesAmountBean.DateStrBean.MonthsBean o1, SalesAmountBean.DateStrBean.MonthsBean o2) {
                                Collator collator = Collator.getInstance(Locale.CHINA);
                                return collator.compare(o1.getYear(),o2.getYear());
                            }
                        });
                        dateStrBean.setMonthsBeenList(monthsBeenList);
                        salesAmountBean.setDateStr(dateStrBean);

                        //2
                        JSONObject salesByMonth = resultObject.getJSONObject("salesByMonth");
                        List<String> xs = new ArrayList<String>();
                        List<String> ys = new ArrayList<String>();
                        JSONArray x = salesByMonth.getJSONArray("x");
                        JSONArray y = salesByMonth.getJSONArray("y");
                        for(int i = 0;i < x.length();i++){
                            xs.add((String) x.get(i));
                        }
                        for(int i = 0;i < y.length();i++){
                            ys.add((String) y.get(i));
                        }

                        SalesAmountBean.SalesByMonthBean salesByMonthBean = new SalesAmountBean.SalesByMonthBean();
                        salesByMonthBean.setX(xs);
                        salesByMonthBean.setY(ys);

                        salesAmountBean.setSalesByMonth(salesByMonthBean);
                        //3
                        String nowDate = resultObject.getString("nowDate");
                        salesAmountBean.setNowDate(nowDate);
                        getSalesAmountPresentCallback.onGetSalesAmountNext(salesAmountBean);
                    }else{
                        getSalesAmountPresentCallback.onGetSalesAmountError(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getSalesAmountPresentCallback.onGetSalesAmountError(null);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                getSalesAmountPresentCallback.onGetSalesAmountError(null);

            }
        }, false, true);




//        StrategyService strategyService = ApiServiceGenerator.createService(StrategyService.class);
//
//        strategyService.getSalesAmount(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseApiModel<SalesAmountBean>>() {
//                    @Override
//                    public void call(BaseApiModel<SalesAmountBean> salesAmountBeanBaseApiModel) {
//                        if(getSalesAmountPresentCallback != null){
//                            getSalesAmountPresentCallback.onGetSalesAmountNext(salesAmountBeanBaseApiModel.getResultObject());
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        if(getSalesAmountPresentCallback != null){
//                            getSalesAmountPresentCallback.onGetSalesAmountError(throwable);
//                        }
//                    }
//                });
//
//
//
    }
}
interface GetSalesAmonutModelInterface{
    void getSalesAmount();
}