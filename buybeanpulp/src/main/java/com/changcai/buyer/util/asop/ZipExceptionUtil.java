package com.changcai.buyer.util.asop;

import java.io.IOException;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipExceptionUtil {

    ZipExceptionUtil() {
    }

    static ZipException rethrow(IOException e) {
        throw new ZipException(e);
    }

}
