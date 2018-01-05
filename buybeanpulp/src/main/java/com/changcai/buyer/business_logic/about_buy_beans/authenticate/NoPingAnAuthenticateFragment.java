package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.company_authenticate.CompanyAuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.PersonAuthenticateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuxingwei on 2017/6/16.
 */

public class NoPingAnAuthenticateFragment extends BaseFragment implements NoPingAnAuthenticateContract.View {

    NoPingAnAuthenticateContract.Presenter presenter;
    @BindView(R.id.iv_authenticate_status)
    ImageView ivAuthenticateStatus;
    @BindView(R.id.tv_authenticate_status)
    TextView tvAuthenticateStatus;
    @BindView(R.id.tv_ongoing_colorful)
    TextView tvOngoingColorful;
    @BindView(R.id.tv_check_company)
    TextView tvCheckCompany;
    @BindView(R.id.ll_status_ongoing)
    RelativeLayout llStatusOngoing;
    @BindView(R.id.tv_company_title)
    TextView tvCompanyTitle;
    @BindView(R.id.tv_person_title)
    TextView tvPersonTitle;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.ll_company)
    LinearLayout llCompany;
    @BindView(R.id.ll_person)
    LinearLayout llPerson;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ping_an, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("identificationState")) {
            setViewByType(getArguments().getString("identificationState"));
        }
//        if (getArguments().containsKey("isPersonal")) {
//            tvCheckCompany.setText(getArguments().getBoolean("isPersonal") ? "查看个人资料" : "查看企业资料");
//            tvOngoingColorful.setText(getString(R.string.no_ping_an_ongoing_text, getArguments().getBoolean("isPersonal") ? "个人身份认证" : "企业身份认证"));
//        }

        if (getArguments().containsKey("isPersonal")) {
            setAuthenticateType(getArguments().getBoolean("isPersonal"));
        }
    }


    public void setAuthenticateType(boolean authenticateType) {
        tvCheckCompany.setText(authenticateType ? "查看个人资料" : "查看企业资料");
        tvOngoingColorful.setText(getString(R.string.no_ping_an_ongoing_text, getArguments().getBoolean("isPersonal") ? "个人身份认证" : "企业身份认证"));
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
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public void setViewByType(@NonNull String type) {
        setAuthenticateStatus(type);
    }

    private void setAuthenticateStatus(String type) {
        switch (type) {
            //未认证
            case "INIT":
                ivAuthenticateStatus.setImageLevel(0);
                tvAuthenticateStatus.setText("认证身份：未认证");
//                tvAuthenticateStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
                tvAuthenticateStatus.setTextColor(getResources().getColor(R.color.black));
                llStatusOngoing.setVisibility(View.GONE);
                llCompany.setVisibility(View.VISIBLE);
                llPerson.setVisibility(View.VISIBLE);
                tvCompany.setVisibility(View.VISIBLE);
                tvPerson.setVisibility(View.VISIBLE);
                break;
            //认证中
            case "PROCESS":
                ivAuthenticateStatus.setImageLevel(2);
                tvAuthenticateStatus.setText("认证身份：审核中");
                tvAuthenticateStatus.setTextColor(getResources().getColor(R.color.black));
                llStatusOngoing.setVisibility(View.VISIBLE);
                llCompany.setVisibility(View.GONE);
                llPerson.setVisibility(View.GONE);
                tvCompany.setVisibility(View.GONE);
                tvPerson.setVisibility(View.GONE);
                break;
            //认证失败
            case "FAIL":
                ivAuthenticateStatus.setImageLevel(4);
                tvAuthenticateStatus.setText("身份认证：未通过，请按要求重新提交");
                tvAuthenticateStatus.setTextColor(getResources().getColor(R.color.red_orange));
                llStatusOngoing.setVisibility(View.GONE);
                llCompany.setVisibility(View.VISIBLE);
                llPerson.setVisibility(View.VISIBLE);
                tvCompany.setVisibility(View.VISIBLE);
                tvPerson.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void setPresenter(NoPingAnAuthenticateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }


    @OnClick({R.id.iv_authenticate_status, R.id.tv_authenticate_status, R.id.tv_ongoing_colorful, R.id.tv_check_company, R.id.ll_status_ongoing, R.id.tv_company_title, R.id.tv_person_title, R.id.tv_company, R.id.tv_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_authenticate_status:
                break;
            case R.id.tv_authenticate_status:
                break;
            case R.id.tv_ongoing_colorful:
                break;
            case R.id.tv_check_company:
                if (getArguments().containsKey("isPersonal")) {
                    gotoActivity(getArguments().getBoolean("isPersonal") ? PersonAuthenticateActivity.class : CompanyAuthenticateActivity.class);
                }
                break;
            case R.id.ll_status_ongoing:
                break;
            case R.id.tv_company_title:
                break;
            case R.id.tv_person_title:
                break;
            case R.id.tv_company:
                gotoActivity(CompanyAuthenticateActivity.class);
                break;
            case R.id.tv_person:
                gotoActivity(PersonAuthenticateActivity.class);
                break;
        }
    }

}
