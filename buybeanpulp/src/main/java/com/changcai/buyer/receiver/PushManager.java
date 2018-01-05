package com.changcai.buyer.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.CommonWebViewActivity;
import com.changcai.buyer.ui.SplashActivity;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.main.MainActivity;
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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lufeisong on 2017/9/20.
 */

public class PushManager {
    private static PushManager instance;

    public PushManager() {
    }

    public static PushManager getInstance() {
        if(instance == null){
            instance = new PushManager();
        }
        return instance;
    }
    /**
     * 打开通知
     *
     * @param context
     * @param customContentString
     */
    public void onNotificationClick(final Context context, final String customContentString) {
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
                                Intent intent = new Intent(context, QuoteDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                SPUtil.saveObjectToShare(Constants.KEY_JPUSH_QUOTATIONID, content[1]);
                                context.startActivity(intent);
                            } else {
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
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtras(bundle1);
                                    context.startActivity(intent);
                                }
                            } else {
                                LogUtil.d("launchIntent", "launchIntent by notification");
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.changcai.buyer");
                                launchIntent.putExtras(bundle1);
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                context.startActivity(launchIntent);
                            }
                        } else {
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
