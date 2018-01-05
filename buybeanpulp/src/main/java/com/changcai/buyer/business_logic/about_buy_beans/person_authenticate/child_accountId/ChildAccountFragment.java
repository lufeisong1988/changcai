package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.child_accountId;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/24.
 */

public class ChildAccountFragment extends BaseFragment implements ChildAccountContract.View {

    ChildAccountContract.Presenter presenter;

    Unbinder unbinder;
    @BindView(R.id.tv_relation)
    TextView tvRelation;
    @BindView(R.id.tv_relation_company)
    TextView tvRelationCompany;
    @BindView(R.id.buyer_power)
    TextView buyerPower;
    @BindView(R.id.salesman_power)
    TextView salesmanPower;
    @BindView(R.id.tv_service_phone)
    TextView tvServicePhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_id, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detach();
    }

    public ChildAccountFragment() {
    }

    public static ChildAccountFragment newInstance() {
        return new ChildAccountFragment();
    }

    @Override
    public void showAuthenticateInfo(String relation, String relationCompany, String buyerPermissions, String salesmanPermissions) {
        tvRelation.setText(relation);
        tvRelationCompany.setText(relationCompany);
        buyerPower.setText(buyerPermissions);
        salesmanPower.setText(salesmanPermissions);

        tvServicePhone.setText(String.format(getString(R.string.service_phone2), SPUtil.getString(Constants.KEY_CONTACT_PHONE)));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(ChildAccountContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }
}
