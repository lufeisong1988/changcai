package com.changcai.buyer.business_logic.about_buy_beans.person_introduce;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.view.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class PersonIntroduceFragment extends BaseFragment implements PersonIntroduceContract.View {


    PersonIntroduceContract.Presenter presenter;
    @BindView(R.id.et_buyer)
    EditText etBuyer;
    @BindView(R.id.tv_post_introduction)
    TextView tvPostIntroduction;


    private Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("PersonIntroduceFragment", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PersonIntroduceFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_introduce, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Log.d("PersonIntroduceFragment", "onCreateView");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("PersonIntroduceFragment", "onViewCreated");
        etBuyer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count>0)etBuyer.setSelected(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0)etBuyer.setSelected(true);
                else etBuyer.setSelected(false);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("PersonIntroduceFragment", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("PersonIntroduceFragment", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        Log.d("PersonIntroduceFragment", "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PersonIntroduceFragment", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
        unbinder.unbind();
        Log.d("PersonIntroduceFragment", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PersonIntroduceFragment", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("PersonIntroduceFragment", "onDetach");
    }


    public PersonIntroduceFragment() {
    }

    public static PersonIntroduceFragment newInstance() {
        return new PersonIntroduceFragment();
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void showErrorDialog(String errorMessage) {
        ConfirmDialog.createConfirmDialog(activity, errorMessage);
    }

    @Override
    public void showSuccessDialog(int message) {
        ConfirmDialog.createConfirmDialog(activity, getString(message), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @Override
    public void showSuccessDialogWithTitle(int message,int title) {
        ConfirmDialog.createConfirmDialogWithTitle(activity, getString(message), getString(title), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @Override
    public void showBuyerView(String buyerInformation) {
        etBuyer.setText(buyerInformation);
    }


    @Override
    public void setPresenter(PersonIntroduceContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @OnClick(R.id.tv_post_introduction)
    public void onClick() {
        presenter.checkInputIsLegal(etBuyer.getText().toString());
    }
}
