package com.changcai.buyer.util.asop.transform;



import com.changcai.buyer.util.asop.ZipEntrySource;
import com.changcai.buyer.util.asop.commons.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipEntrySourceZipEntryTransformer implements ZipEntryTransformer{

    private final ZipEntrySource source;

    public ZipEntrySourceZipEntryTransformer(ZipEntrySource source) {
        this.source = source;
    }

    public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
        addEntry(this.source, out);
    }

    static void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
        out.putNextEntry(entry.getEntry());
        InputStream in = entry.getInputStream();
        if(in != null) {
            try {
                IOUtils.copy(in, out);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        out.closeEntry();
    }
}
