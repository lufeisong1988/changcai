package com.changcai.buyer.util.asop.transform;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public interface ZipEntryTransformer {

    void transform(InputStream var1, ZipEntry var2, ZipOutputStream var3) throws IOException;

}
