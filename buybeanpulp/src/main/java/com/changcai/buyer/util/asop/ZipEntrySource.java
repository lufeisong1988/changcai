package com.changcai.buyer.util.asop;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public interface ZipEntrySource {
    String getPath();

    ZipEntry getEntry();

    InputStream getInputStream() throws IOException;
}
