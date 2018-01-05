package com.changcai.buyer.util.asop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipFileUtil {

    private static final String MISSING_METHOD_PLEASE_UPGRADE = "Your JRE doesn\'t support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).";
    private static final String CONSTRUCTOR_MESSAGE_FOR_ZIPFILE = "Using constructor ZipFile(File, Charset) has failed: ";
    private static final String CONSTRUCTOR_MESSAGE_FOR_OUTPUT = "Using constructor ZipOutputStream(OutputStream, Charset) has failed: ";
    private static final String CONSTRUCTOR_MESSAGE_FOR_INPUT = "Using constructor ZipInputStream(InputStream, Charset) has failed: ";

    private ZipFileUtil() {
    }

    static ZipInputStream createZipInputStream(InputStream inStream, Charset charset) {
        if(charset == null) {
            return new ZipInputStream(inStream);
        } else {
            try {
                Constructor e = ZipInputStream.class.getConstructor(new Class[]{InputStream.class, Charset.class});
                return (ZipInputStream)e.newInstance(new Object[]{inStream, charset});
            } catch (NoSuchMethodException var3) {
                throw new IllegalStateException("Your JRE doesn\'t support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", var3);
            } catch (InstantiationException var4) {
                throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + var4.getMessage(), var4);
            } catch (IllegalAccessException var5) {
                throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + var5.getMessage(), var5);
            } catch (IllegalArgumentException var6) {
                throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + var6.getMessage(), var6);
            } catch (InvocationTargetException var7) {
                throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + var7.getMessage(), var7);
            }
        }
    }

    static ZipOutputStream createZipOutputStream(BufferedOutputStream outStream, Charset charset) {
        if(charset == null) {
            return new ZipOutputStream(outStream);
        } else {
            try {
                Constructor e = ZipOutputStream.class.getConstructor(new Class[]{OutputStream.class, Charset.class});
                return (ZipOutputStream)e.newInstance(new Object[]{outStream, charset});
            } catch (NoSuchMethodException var3) {
                throw new IllegalStateException("Your JRE doesn\'t support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", var3);
            } catch (InstantiationException var4) {
                throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + var4.getMessage(), var4);
            } catch (IllegalAccessException var5) {
                throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + var5.getMessage(), var5);
            } catch (IllegalArgumentException var6) {
                throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + var6.getMessage(), var6);
            } catch (InvocationTargetException var7) {
                throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + var7.getMessage(), var7);
            }
        }
    }

    static ZipFile getZipFile(File src, Charset charset) throws IOException {
        if(charset == null) {
            return new ZipFile(src);
        } else {
            try {
                Constructor e = ZipFile.class.getConstructor(new Class[]{File.class, Charset.class});
                return (ZipFile)e.newInstance(new Object[]{src, charset});
            } catch (NoSuchMethodException var3) {
                throw new IllegalStateException("Your JRE doesn\'t support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", var3);
            } catch (InstantiationException var4) {
                throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + var4.getMessage(), var4);
            } catch (IllegalAccessException var5) {
                throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + var5.getMessage(), var5);
            } catch (IllegalArgumentException var6) {
                throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + var6.getMessage(), var6);
            } catch (InvocationTargetException var7) {
                throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + var7.getMessage(), var7);
            }
        }
    }

    static boolean isCharsetSupported() throws IOException {
        try {
            ZipFile.class.getConstructor(new Class[]{File.class, Charset.class});
            return true;
        } catch (NoSuchMethodException var1) {
            return false;
        }
    }
}
