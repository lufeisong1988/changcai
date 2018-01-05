package com.changcai.buyer.util.asop.transform;


import com.changcai.buyer.util.asop.FileSource;
import com.changcai.buyer.util.asop.commons.FileUtils;
import com.changcai.buyer.util.asop.commons.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public abstract class FileZipEntryTransformer implements ZipEntryTransformer{

    public FileZipEntryTransformer() {
    }

    protected abstract void transform(ZipEntry var1, File var2, File var3) throws IOException;

    public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
        File inFile = null;
        File outFile = null;

        try {
            inFile = File.createTempFile("zip", (String)null);
            outFile = File.createTempFile("zip", (String)null);
            copy(in, inFile);
            this.transform(zipEntry, inFile, outFile);
            FileSource source = new FileSource(zipEntry.getName(), outFile);
           ZipEntrySourceZipEntryTransformer.addEntry(source, out);
        } finally {
            FileUtils.deleteQuietly(inFile);
            FileUtils.deleteQuietly(outFile);
        }

    }

    private static void copy(InputStream in, File file) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }
}
