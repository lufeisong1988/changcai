package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.child_accountId;

import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.SPUtil;

/**
 * Created by liuxingwei on 2016/11/24.
 */
public class ChildAccountIdPresenter implements ChildAccountContract.Presenter {

    ChildAccountContract.View view;
    protected UserInfo userInfo;

    public ChildAccountIdPresenter(ChildAccountContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    /**
     * finance 财务员
     * business 业务员
     * transaction 交易员
     */
    @Override
    public void start() {
        userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);

        String childRole = userInfo.getType();
        String relationChildRole = "";
        String relationCompany = userInfo.getEnterName();
        String buyerPermissions = "";
        String salesmanPermissions = "";
        if ("finance".equalsIgnoreCase(childRole)) {
            relationChildRole = "已关联：财务员";
            buyerPermissions = "买家：签署合同，支付保证金/支付货款/支付补充打款";
            salesmanPermissions = "卖家：签署合同，支付保证金/支付补充打款";
        } else if ("business".equalsIgnoreCase(childRole)) {
            relationChildRole = "已关联：业务员";
            buyerPermissions = "买家：查看报价/下订单，发起提货/确认收货/发起补充打款，发起点价";
            salesmanPermissions = "卖家：发布报价/确认订单，确认提货/发起补充打款";
        } else if ("transaction".equalsIgnoreCase(childRole)) {
            relationChildRole = "已关联：交易员";
            buyerPermissions = "交易员：挂单，输入点价结果";
        }

        view.showAuthenticateInfo(relationChildRole, relationCompany, buyerPermissions, salesmanPermissions);
    }

    @Override
    public void detach() {
        view = null;
    }
}
