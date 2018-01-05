package com.changcai.buyer.util.asop;

import com.changcai.buyer.util.asop.commons.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipEntryUtil {

    private ZipEntryUtil() {
    }

    static ZipEntry copy(ZipEntry original) {
        return copy(original, (String)null);
    }

    static ZipEntry copy(ZipEntry original, String newName) {
        ZipEntry copy = new ZipEntry(newName == null?original.getName():newName);
        if(original.getCrc() != -1L) {
            copy.setCrc(original.getCrc());
        }

        if(original.getMethod() != -1) {
            copy.setMethod(original.getMethod());
        }

        if(original.getSize() >= 0L) {
            copy.setSize(original.getSize());
        }

        if(original.getExtra() != null) {
            copy.setExtra(original.getExtra());
        }

        copy.setComment(original.getComment());
        copy.setTime(original.getTime());
        return copy;
    }

    static void copyEntry(ZipEntry zipEntry, InputStream in, ZipOutputStream out) throws IOException {
        copyEntry(zipEntry, in, out, true);
    }

    static void copyEntry(ZipEntry zipEntry, InputStream in, ZipOutputStream out, boolean preserveTimestamps) throws IOException {
        ZipEntry copy = copy(zipEntry);
        copy.setTime(preserveTimestamps?zipEntry.getTime():System.currentTimeMillis());
        addEntry(copy, new BufferedInputStream(in), out);
    }

    static void addEntry(ZipEntry zipEntry, InputStream in, ZipOutputStream out) throws IOException {
        out.putNextEntry(zipEntry);
        if(in != null) {
            IOUtils.copy(in, out);
        }

        out.closeEntry();
    }
}
