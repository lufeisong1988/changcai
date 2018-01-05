package com.changcai.buyer.util.asop.transform;


/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipEntryTransformerEntry {

    private final String path;
    private final ZipEntryTransformer transformer;

    public ZipEntryTransformerEntry(String path, ZipEntryTransformer transformer) {
        this.path = path;
        this.transformer = transformer;
    }

    public String getPath() {
        return this.path;
    }

    public ZipEntryTransformer getTransformer() {
        return this.transformer;
    }

    public String toString() {
        return this.path + "=" + this.transformer;
    }
}
