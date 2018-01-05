package com.changcai.buyer.util;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxUtil {
    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private RxUtil() {

    }

    public static Subscription unsubscribe(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }

        return null;
    }

    public static void remove(Subscription subscription) {
        if (subscription != null) {
            mCompositeSubscription.remove(subscription);
        }

    }

    /**
     * clear all
     */
    public static void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }

    public static void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }
}
