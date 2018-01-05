package com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.util.NumUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class PingAnEasyPayRechargeFragment extends BaseFragment implements PingAnEasyPayRechargeContract.View {

    Unbinder unbinder;

    PingAnEasyPayRechargeContract.Presenter presenter;
    @BindView(R.id.tv_bank_card)
    TextView tvBankCard;
    @BindView(R.id.tv_copy_plate)
    TextView tvCopyPlate;
    @BindView(R.id.tv_ping_an_beans_contract)
    TextView tvPingAnBeansContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ping_an_easy_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContractInfoStyle();
        setEbaoPayCardInfo();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setEbaoPayCardInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("EbaoInfo") != null) {
                EbaoBalanceInfo ebaoBalanceInfo = (EbaoBalanceInfo) bundle.getSerializable("EbaoInfo");
                tvBankCard.setText(NumUtil.bankFormat(ebaoBalanceInfo.getBankAccNo()));
            } else {
                presenter.getAccountBalance();
            }
        } else {
            presenter.getAccountBalance();
        }
    }

    @SuppressWarnings("deprecation")
    private void setContractInfoStyle() {
        SpannableString spannableString = new SpannableString(getString(R.string.bank_introduce));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.sticky_header_text_color));
        spannableString.setSpan(foregroundColorSpan, 25, 35, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvPingAnBeansContract.setText(spannableString);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void setBankInfo(EbaoBalanceInfo ebaoBalanceInfo) {
        tvBankCard.setText(NumUtil.bankFormat(ebaoBalanceInfo.getBankAccNo()));
    }

    @Override
    public void setPresenter(PingAnEasyPayRechargeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }

    @OnClick(R.id.tv_copy_plate)
    public void onClick() {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        ClipData clipData = ClipData.newPlainText("text", tvBankCard.getText().toString());
        cm.setPrimaryClip(clipData);
        Toast.makeText(getActivityContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
    }
}
