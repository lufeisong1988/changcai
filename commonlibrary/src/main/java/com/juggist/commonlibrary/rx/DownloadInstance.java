package com.juggist.commonlibrary.rx;

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
