package com.changcai.buyer.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.changcai.buyer.common.Constants;

/**
 * Created by Aoj on 15/6/15.
 */
public class BroadcastReceiverHelper extends BroadcastReceiver {

    private Context context;
    private Handler msgHandler;

    public BroadcastReceiverHelper(Context context, Handler msgHandler) {
        this.context = context;
        this.msgHandler = msgHandler;
    }
    public static final String PHONE_NUMBER = "13472404585";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            byte[] pdu = (byte[]) obj;
            SmsMessage sms = SmsMessage.createFromPdu(pdu);
            String message = sms.getMessageBody();
            Log.d("短信内容", "message：" + message);
            // 短息的手机号。。+86开头？
            String from = sms.getOriginatingAddress();
            Log.d("短信来源", "from ：" + from);
            if (!TextUtils.isEmpty(from)
                    && from.equals(PHONE_NUMBER)) {
                Message msg = new Message();
                msg.what = 1;
                msgHandler.sendMessage(msg);
            }
        }
    }
}
