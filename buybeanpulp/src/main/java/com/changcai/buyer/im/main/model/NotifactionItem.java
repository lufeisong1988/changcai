package com.changcai.buyer.im.main.model;

import com.changcai.buyer.R;

/**
 * Created by lufeisong on 2017/12/20.
 */

public enum NotifactionItem {
//    CUSTOMER_SERVICE(0,R.drawable.icon_new_customer,"客服"),
    VIP_SERVICE(0,R.drawable.icon_new_adviser,"会员顾问"),
//    GROUP_SERVICE(2,R.drawable.icon_new_union,"产业联盟"),
    NOTIFACTION_SERVICE(1,R.drawable.icon_new_answer,"顾问答复");
    public final int id;
    public final int icondId;
    public final String title;


    NotifactionItem(int id,int icondId, String title) {
        this.id = id;
        this.icondId = icondId;
        this.title = title;

    }
}
