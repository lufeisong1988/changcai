package com.changcai.buyer.util.asop.transform;


import com.changcai.buyer.util.asop.ByteSource;
import com.changcai.buyer.util.asop.commons.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public abstract class ByteArrayZipEntryTransformer implements ZipEntryTransformer {

    public ByteArrayZipEntryTransformer() {
    }

    protected abstract byte[] transform(ZipEntry var1, byte[] var2) throws IOException;

    public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
        byte[] bytes = IOUtils.toByteArray(in);
        bytes = this.transform(zipEntry, bytes);
        ByteSource source;
        if(this.preserveTimestamps()) {
            source = new ByteSource(zipEntry.getName(), bytes, zipEntry.getTime());
        } else {
            source = new ByteSource(zipEntry.getName(), bytes);
        }

        ZipEntrySourceZipEntryTransformer.addEntry(source, out);
    }

    protected boolean preserveTimestamps() {
        return false;
    }
}
