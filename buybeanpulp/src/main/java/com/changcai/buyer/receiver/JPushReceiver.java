package com.changcai.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.CommonWebViewActivity;
import com.changcai.buyer.ui.SplashActivity;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.ui.message.bean.ExtrasInfo;
import com.changcai.buyer.ui.order.DeliveryDetailActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtil.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtil.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d(TAG, "[MyReceiver] 用户点击打开了通知");
//			onNotificationClick(context, bundle.getString(JPushInterface.EXTRA_ALERT));
            if (bundle.getString(JPushInterface.EXTRA_EXTRA).contentEquals("{}")) {
                onNotificationClick(context, null);
            } else {
                onNotificationClick(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtil.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtil.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtil.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        if (bundle == null)
            return "bundle is null";
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key == null) {
                sb.append("\nkey:" + key);
            } else if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity

    /**
     * 透传
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            ExtrasInfo extrasInfo = (ExtrasInfo) bundle.getSerializable("extrasInfo");
            if (!TextUtils.isEmpty(extrasInfo.getArticle())) {
                Bundle b = new Bundle();
                b.putString("url", extrasInfo.getArticle());
                b.putString("title", "资讯详情");
                AndroidUtil.startBrowser(context, b, false);
                return;
            }

            if (!TextUtils.isEmpty(extrasInfo.getOrder())) {
                Bundle b = new Bundle();
                b.putString("orderId", extrasInfo.getOrder());
                intent = new Intent(context, OrderDetailActivity.class);
                context.getApplicationContext().startActivity(intent);
                return;
            }

            if (!TextUtils.isEmpty(extrasInfo.getDelivery())) {
                Bundle b = new Bundle();
                b.putString("deliveryId", extrasInfo.getDelivery());
                intent = new Intent(context, DeliveryDetailActivity.class);
                context.getApplicationContext().startActivity(intent);
                return;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.getApplicationContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.getApplicationContext().startActivity(intent);
        }
    }

    /**
     * 打开通知
     *
     * @param context
     * @param customContentString
     */
    private void onNotificationClick(final Context context, final String customContentString) {
        LogUtil.d("JpushReceiveCustomContentString", customContentString);
        if (customContentString == null) {
            LogUtil.d("JpushReceiveCustomContentString", "JpushReceiveCustomContentString is null android launchApp");
            if (!AndroidUtil.isForegroundApp(context)) {
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.changcai.buyer");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            }
            return;
        }
        Observable.just(customContentString)
                .map(new Func1<String, JsonObject>() {
                    @Override
                    public JsonObject call(String s) {
                        JsonElement jsonElement = new JsonParser().parse(customContentString);
                        return jsonElement.getAsJsonObject();
                    }
                })
                .map(new Func1<JsonObject, Bundle>() {
                    @Override
                    public Bundle call(JsonObject jsonObject) {
                        Bundle bundle = null;
                        if (null != jsonObject) {
                            bundle = new Bundle();
                            bundle.putString("j_push_content", jsonObject.get("content").getAsString());
                            bundle.putString("j_push_type", jsonObject.get("type").getAsString());
                        }
                        return bundle;
                    }
                })
                .subscribe(new Action1<Bundle>() {
                    @Override
                    public void call(final Bundle bundle) {
                        final Bundle bundle1 = new Bundle();
                        if (bundle.getString("j_push_type").equalsIgnoreCase("cms") || bundle.getString("j_push_type").equalsIgnoreCase("action")) {
                            String[] content = bundle.getString("j_push_content").split(",");

                            bundle1.putString("j_push_type", bundle.getString("j_push_type"));
                            bundle1.putString("url", content.length > 0 ? content[0].split("=")[1] : "www.maidoupo.com");
                            bundle1.putString("info", content.length > 1 ? content[1].split("=")[1] : context.getString(R.string.newest_cms_default));
                            bundle1.putString("title", content.length > 2 ? content[2].split("=")[1] : context.getString(R.string.cms_default));
                            if (bundle.getString("j_push_type").equalsIgnoreCase("action")) {
                                bundle1.putString("minGrade", "0");
                            } else {
                                bundle1.putString("minGrade", content.length > 3 ? content[3].split("=")[1] : "0");
                            }
                            if (AndroidUtil.isForegroundApp(context)) {
                                LogUtil.d("TAG","在前台");
                                final int minGrade = Integer.valueOf(bundle1.getString("minGrade"));
                                if (AppManager.getAppManager().currentActivity() instanceof SplashActivity) {
                                    Observable.empty().delay(5, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                                        @Override
                                        public void onCompleted() {
                                            if (minGrade >= 0) {
                                                if (UserDataUtil.isLogin()) {
                                                    browseDetails(context, bundle1);
                                                } else {
                                                    Login(context, bundle1);
                                                }
                                            } else if (minGrade == -1) {
                                                // CommonWebViewActivity   android:launchMode="singleTop"
                                                browseDetails(context, bundle1);
                                            }
                                            LogUtil.d("pushReceiver", "click by SplashActivity");
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Object o) {

                                        }
                                    });
                                } else {
                                    if (minGrade >= 0) {
                                        if (UserDataUtil.isLogin()) {
                                            browseDetails(context, bundle1);
                                        } else {
                                            Login(context, bundle1);
                                        }
                                    } else if (minGrade == -1) {
                                        // CommonWebViewActivity   android:launchMode="singleTop"
                                        browseDetails(context, bundle1);
                                    }
                                }

                            } else {
                                LogUtil.d("TAG","在后台");
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.changcai.buyer");
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                launchIntent.putExtras(bundle1);
                                context.startActivity(launchIntent);
                            }
                        } else if (bundle.getString("j_push_type").equalsIgnoreCase("quotation")) {

                            String[] content = bundle.getString("j_push_content").split("=");
                            if (AndroidUtil.isForegroundApp(context)) {
                                LogUtil.d("TAG","在前台 quotation");
                                Intent intent = new Intent(context, QuoteDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                SPUtil.saveObjectToShare(Constants.KEY_JPUSH_QUOTATIONID, content[1]);
                                context.startActivity(intent);
                            } else {
                                LogUtil.d("TAG","在后台 quotation");
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.changcai.buyer");
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                SPUtil.saveObjectToShare(Constants.KEY_JPUSH_QUOTATIONID, content[1]);
                                context.startActivity(launchIntent);
                            }

                        } else if (bundle.getString("j_push_type").equalsIgnoreCase("spotGoods")) {
                            String[] content = bundle.getString("j_push_content").split(",");
                            bundle1.putString("j_push_type", bundle.getString("j_push_type"));
                            bundle1.putString("tabIndex", content.length > 0 ? content[0].split("=")[1] : "0");
                            bundle1.putString("folder", content.length > 1 ? content[1].split("=")[1] : "0");
                            if (AndroidUtil.isForegroundApp(context)) {

                                if (AppManager.getAppManager().currentActivity() instanceof SplashActivity) {
                                    LogUtil.d("TAG","SplashActivity spotGoods");
                                    Observable.empty().delay(5, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                                        @Override
                                        public void onCompleted() {
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.putExtras(bundle1);
                                            context.startActivity(intent);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Object o) {

                                        }
                                    });
                                } else {
                                    LogUtil.d("TAG","MainActivity spotGoods");
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtras(bundle1);
                                    context.startActivity(intent);
                                }
                            } else {
                                LogUtil.d("TAG","launchIntent by notification");
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.changcai.buyer");
                                launchIntent.putExtras(bundle1);
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                context.startActivity(launchIntent);
                            }
                        } else {
                            LogUtil.d("TAG","else other");
                            if (!AndroidUtil.isForegroundApp(context)) {
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.changcai.buyer");
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                context.startActivity(launchIntent);
                            }
                        }

                    }
                });


    }

    private void Login(final Context context, final Bundle bundle) {
        LogUtil.d("activity", AppManager.getAppManager().currentActivity().getLocalClassName());
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putBoolean("pushEvent", true);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void browseDetails(Context context, Bundle bundle1) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtras(bundle1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
