package com.changcai.buyer.util.asop;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZTFileUtil {

    private ZTFileUtil() {
    }

    public static Collection<File> listFiles(File dir) {
        return listFiles(dir, (FileFilter)null);
    }

    public static Collection<File> listFiles(File dir, FileFilter filter) {
        ArrayList accumulator = new ArrayList();
        if(dir.isFile()) {
            return accumulator;
        } else {
            if(filter == null) {
                filter = new FileFilter() {
                    public boolean accept(File pathname) {
                        return true;
                    }
                };
            }

            innerListFiles(dir, accumulator, filter);
            return accumulator;
        }
    }

    private static void innerListFiles(File dir, Collection<File> accumulator, FileFilter filter) {
        String[] filenames = dir.list();
        if(filenames != null) {
            for(int i = 0; i < filenames.length; ++i) {
                File file = new File(dir, filenames[i]);
                if(file.isDirectory()) {
                    innerListFiles(file, accumulator, filter);
                } else if(filter != null && filter.accept(file)) {
                    accumulator.add(file);
                }
            }
        }

    }
}
