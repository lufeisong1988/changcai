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

public abstract class StringZipEntryTransformer implements ZipEntryTransformer{

    private final String encoding;

    public StringZipEntryTransformer() {
        this((String)null);
    }

    public StringZipEntryTransformer(String encoding) {
        this.encoding = encoding;
    }

    protected abstract String transform(ZipEntry var1, String var2) throws IOException;

    public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
        String data = IOUtils.toString(in, this.encoding);
        data = this.transform(zipEntry, data);
        byte[] bytes = this.encoding == null?data.getBytes():data.getBytes(this.encoding);
        ByteSource source = new ByteSource(zipEntry.getName(), bytes);
       ZipEntrySourceZipEntryTransformer.addEntry(source, out);
    }
}
