package com.changcai.buyer.util.asop;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipEntryOrInfoAdapter implements ZipEntryCallback, ZipInfoCallback{

    private final ZipEntryCallback entryCallback;
    private final ZipInfoCallback infoCallback;

    public ZipEntryOrInfoAdapter(ZipEntryCallback entryCallback, ZipInfoCallback infoCallback) {
        if((entryCallback == null || infoCallback == null) && (entryCallback != null || infoCallback != null)) {
            this.entryCallback = entryCallback;
            this.infoCallback = infoCallback;
        } else {
            throw new IllegalArgumentException("Only one of ZipEntryCallback and ZipInfoCallback must be specified together");
        }
    }

    public void process(ZipEntry zipEntry) throws IOException {
        this.infoCallback.process(zipEntry);
    }

    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
        if(this.entryCallback != null) {
            this.entryCallback.process(in, zipEntry);
        } else {
            this.process(zipEntry);
        }

    }
}
