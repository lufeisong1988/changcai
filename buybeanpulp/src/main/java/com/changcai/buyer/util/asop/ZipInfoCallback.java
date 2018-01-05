package com.changcai.buyer.util.asop;

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public interface ZipInfoCallback {
    void process(ZipEntry var1) throws IOException;

}
