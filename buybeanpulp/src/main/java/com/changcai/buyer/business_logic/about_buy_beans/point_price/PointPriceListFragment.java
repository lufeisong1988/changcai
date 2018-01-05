package com.changcai.buyer.business_logic.about_buy_beans.point_price;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changcai.buyer.BaseListFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.order.bean.PricingInfo;
import com.changcai.buyer.view.ConfirmDialog;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PointPriceListFragment extends BaseListFragment implements PointPriceListContract.View{


    PointPriceListContract.Presenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment,container,false);
        baseUnbinder = ButterKnife.bind(this,view);
        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<PricingInfo> pricingInfoList= (List<PricingInfo>) getArguments().getSerializable("priceList");
        getListView().setAdapter(new PointPriceListAdapter(getContext(),pricingInfoList));
        getListView().setHeaderDividersEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void setPresenter(PointPriceListContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(getContext(),message);
    }
}
