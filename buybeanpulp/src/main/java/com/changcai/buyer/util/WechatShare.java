package com.changcai.buyer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.common.ShareConstants;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendMessageToWX;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * 微信分享
 * Created by songlei on 16/1/1.
 */
public class WechatShare {
//    private IWXAPI iwxapi;
    private Context mContext;

    /**
     * 微信分享初始化
     *
     * @param context 上下文
     */
    public WechatShare(Context context) {
//        iwxapi = WXAPIFactory.createWXAPI(context, ShareConstants.WECHAT_APPID);
//        iwxapi.registerApp(ShareConstants.WECHAT_APPID);
        mContext = context;
    }
//
    /**
     * 分享图文链接
     *
     */
    public void share(final  String url ,final String title , final String description) {
//        if (!iwxapi.isWXAppInstalled()) {
//            try {
//                Toast.makeText(mContext, ShareConstants.WECHAT_NOT_INSTALL, Toast.LENGTH_SHORT).show();
//            } catch (Throwable e) {
//
//            }
//        } else {
//            shareWechat(url,  title,  description, null);
//        }
    }

    private void shareWechat(String url, String title, String description, Bitmap bitmap) {
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//        final WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = description;
//
//        if (bitmap != null) {
//            msg.setThumbImage(bitmap);
//        } else {
//            msg.setThumbImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
//        }
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = String.valueOf(System.currentTimeMillis());
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        iwxapi.sendReq(req);
    }
}
