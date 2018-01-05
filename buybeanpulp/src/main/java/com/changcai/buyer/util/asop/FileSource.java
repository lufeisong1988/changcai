package com.changcai.buyer.util.asop;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class FileSource implements ZipEntrySource{

    private final String path;
    private final File file;

    public FileSource(String path, File file) {
        this.path = path;
        this.file = file;
    }

    public String getPath() {
        return this.path;
    }

    public ZipEntry getEntry() {
        ZipEntry entry = new ZipEntry(this.path);
        if(!this.file.isDirectory()) {
            entry.setSize(this.file.length());
        }

        entry.setTime(this.file.lastModified());
        return entry;
    }

    public InputStream getInputStream() throws IOException {
        return this.file.isDirectory()?null:new BufferedInputStream(new FileInputStream(this.file));
    }

    public String toString() {
        return "FileSource[" + this.path + ", " + this.file + "]";
    }

    public static FileSource[] pair(File[] files, String[] names) {
        if(files.length > names.length) {
            throw new IllegalArgumentException("names array must contain at least the same amount of items as files array or more");
        } else {
            FileSource[] result = new FileSource[files.length];

            for(int i = 0; i < files.length; ++i) {
                result[i] = new FileSource(names[i], files[i]);
            }

            return result;
        }
    }
}
