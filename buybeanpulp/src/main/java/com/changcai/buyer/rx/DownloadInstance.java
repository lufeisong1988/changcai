package com.changcai.buyer.rx;

import android.content.Context;

import rx.Observable;
import rx.subjects.PublishSubject;
import zlc.season.rxdownload.RxDownload;

/**
 * Created by liuxingwei on 2016/12/26.
 */

public class DownloadInstance {

    public static RxDownload rxDownload = RxDownload.getInstance();

    private DownloadInstance() {
    }

    public static RxDownload getInstance() {
        return rxDownload;
    }


}
