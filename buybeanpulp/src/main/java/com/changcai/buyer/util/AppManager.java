package com.changcai.buyer.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by liuxingwei on 2017/1/14.
 */

public class AppManager {
    // Activity栈
    private static Stack<Activity> activityStack;
    // 单例模式
    private static AppManager instance;

    private AppManager() {
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * overload
     *  for ConcurrentModificationException save need to finish activity then end which need to finish
     * @param classes
     */
    public void finishActivity(Class<?>... classes) {
        Stack<Activity> activities = new Stack<>();
        for (Activity activity : activityStack) {
            for (Class c : classes) {
                if (activity.getClass().equals(c)) {
                    activities.add(activity);
                }
            }
        }
        if (activities.isEmpty()) return;
        activityStack.removeAll(activities);
        for (Activity activity : activities) {
            activity.finish();
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            //调用Umeng保存统计数据
            MobclickAgent.onKillProcess(context);
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
