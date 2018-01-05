package com.changcai.buyer.util.asop;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ByteSource implements ZipEntrySource {

    private final String path;
    private final byte[] bytes;
    private final long time;

    public ByteSource(String path, byte[] bytes) {
        this(path, bytes, System.currentTimeMillis());
    }

    public ByteSource(String path, byte[] bytes, long time) {
        this.path = path;
        this.bytes = (byte[])bytes.clone();
        this.time = time;
    }

    public String getPath() {
        return this.path;
    }

    public ZipEntry getEntry() {
        ZipEntry entry = new ZipEntry(this.path);
        if(this.bytes != null) {
            entry.setSize((long)this.bytes.length);
        }

        entry.setTime(this.time);
        return entry;
    }

    public InputStream getInputStream() throws IOException {
        return this.bytes == null?null:new ByteArrayInputStream(this.bytes);
    }

    public String toString() {
        return "ByteSource[" + this.path + "]";
    }
}
