package com.changcai.buyer.util.asop;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public interface ZipEntryCallback {
    void process(InputStream var1, ZipEntry var2) throws IOException;
}
