package com.changcai.buyer.util.asop.transform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public abstract class StreamZipEntryTransformer implements ZipEntryTransformer{

    public StreamZipEntryTransformer() {
    }

    protected abstract void transform(ZipEntry var1, InputStream var2, OutputStream var3) throws IOException;

    public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(zipEntry.getName());
        entry.setTime(System.currentTimeMillis());
        out.putNextEntry(entry);
        this.transform((ZipEntry)zipEntry, (InputStream)in, (OutputStream)out);
        out.closeEntry();
    }
}
