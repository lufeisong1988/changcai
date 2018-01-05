package com.changcai.buyer.business_logic.about_buy_beans.deposit_and_pay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/3/20.
 */
public class DepositAndPayFragment extends BaseFragment implements DepositAndPayContract.View {

    @BindView(R.id.tv_content)
    WebView tvContent;
    private DepositAndPayContract.Presenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_deposit_pay, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String htmlRes = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "            <title>支付说明</title>\n" +
                "            \n" +
                "            <style>\n" +
                "                body{\n" +
                "                    /*font-size: 62.5%;*/\n" +
                "                    margin: 40px 20px 20px;\n" +
                "                }\n" +
                "                #title-instructions h1{\n" +
                "                \n" +
                "                    font-family:PingFangSC-Regular,\"Microsoft YaHei\";\n" +
                "                    font-size: 17px;\n" +
                "                    color: #26272A;\n" +
                "                    letter-spacing: 0.5px;\n" +
                "                    line-height: 2px;\n" +
                "                }\n" +
                "                #content p{\n" +
                "                    font-family:PingFangSC-Regular;\n" +
                "                    font-size: 17px;\n" +
                "                    color: #26272A;\n" +
                "                    letter-spacing: 0.5px;\n" +
                "                    line-height: 20px;\n" +
                "                }\n" +
                "            </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        \n" +
                "        <div id=\"title-instructions\">\n" +
                "            <h1>\n" +
                "                买豆粕网快捷支付说明：\n" +
                "            </h1>\n" +
                "        </div>\n" +
                "        <div id=\"content\">\n" +
                "            <p>\n" +
                "            1、若同意使用快捷支付，则可跳过平安易宝向平台入金这一步骤，直接输入支付密码进行快速支付。<br><br>\n" +
                "            2、若不同意使用快捷支付，则在买豆粕网平台账户余额不足时，需要手动入金并支付。<br><br>\n" +
                "            3、快捷支付方式通过平安易宝交易账户中的可用余额支付，与平安易宝绑定的银行卡卡内资金无关。请放心使用。<br><br>\n" +
                "            4、如果在扣款后对费用有异议，您可拨打买豆粕网客服电话 021-54180258。我们将会第一时间为您处理。<br><br>\n" +
                "            5、快捷支付最终解释权归买豆粕网所有。<br><br>\n" +
                "            </p>\n" +
                "            \n" +
                "        </div>\n" +
                "        \n" +
                "    </body>\n" +
                "</html>";
        tvContent.loadDataWithBaseURL(null,htmlRes,"text/html","UTF-8",null);

    }




    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void setPresenter(DepositAndPayContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }
}
