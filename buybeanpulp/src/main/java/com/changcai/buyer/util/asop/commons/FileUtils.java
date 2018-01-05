package com.changcai.buyer.util.asop.commons;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class FileUtils {

    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = 1048576L;
    public static final long ONE_GB = 1073741824L;
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    public FileUtils() {
    }

    public static void copy(File file, OutputStream out) throws IOException {
        FileInputStream in = new FileInputStream(file);

        try {
             IOUtils.copy(new BufferedInputStream(in), out);
        } finally {
             IOUtils.closeQuietly(in);
        }

    }

    public static void copy(InputStream in, File file) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

        try {
             IOUtils.copy(in, out);
        } finally {
             IOUtils.closeQuietly(out);
        }

    }

    public static File getTempFileFor(File file) {
        File parent = file.getParentFile();
        String name = file.getName();
        int index = 0;

        File result;
        do {
            result = new File(parent, name + "_" + index++);
        } while(result.exists());

        return result;
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if(file.exists()) {
            if(file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            } else if(!file.canRead()) {
                throw new IOException("File \'" + file + "\' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File \'" + file + "\' does not exist");
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if(file.exists()) {
            if(file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            }

            if(!file.canWrite()) {
                throw new IOException("File \'" + file + "\' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if(parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("File \'" + file + "\' could not be created");
            }
        }

        return new FileOutputStream(file);
    }

    public static boolean contentEquals(File file1, File file2) throws IOException {
        boolean file1Exists = file1.exists();
        if(file1Exists != file2.exists()) {
            return false;
        } else if(!file1Exists) {
            return true;
        } else if(!file1.isDirectory() && !file2.isDirectory()) {
            if(file1.length() != file2.length()) {
                return false;
            } else if(file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
                return true;
            } else {
                FileInputStream input1 = null;
                FileInputStream input2 = null;

                boolean var6;
                try {
                    input1 = new FileInputStream(file1);
                    input2 = new FileInputStream(file2);
                    var6 =  IOUtils.contentEquals(input1, input2);
                } finally {
                     IOUtils.closeQuietly(input1);
                     IOUtils.closeQuietly(input2);
                }

                return var6;
            }
        } else {
            throw new IOException("Can\'t compare directories, only files");
        }
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if(destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if(destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination \'" + destDir + "\' is not a directory");
        } else {
            copyFile(srcFile, new File(destDir, srcFile.getName()), preserveFileDate);
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if(srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if(destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if(!srcFile.exists()) {
            throw new FileNotFoundException("Source \'" + srcFile + "\' does not exist");
        } else if(srcFile.isDirectory()) {
            throw new IOException("Source \'" + srcFile + "\' exists but is a directory");
        } else if(srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source \'" + srcFile + "\' and destination \'" + destFile + "\' are the same");
        } else if(destFile.getParentFile() != null && !destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
            throw new IOException("Destination \'" + destFile + "\' directory cannot be created");
        } else if(destFile.exists() && !destFile.canWrite()) {
            throw new IOException("Destination \'" + destFile + "\' exists but is read-only");
        } else {
            doCopyFile(srcFile, destFile, preserveFileDate);
        }
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if(destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination \'" + destFile + "\' exists but is a directory");
        } else {
            FileInputStream input = new FileInputStream(srcFile);

            try {
                FileOutputStream output = new FileOutputStream(destFile);

                try {
                     IOUtils.copy(input, output);
                } finally {
                     IOUtils.closeQuietly(output);
                }
            } finally {
                 IOUtils.closeQuietly(input);
            }

            if(srcFile.length() != destFile.length()) {
                throw new IOException("Failed to copy full contents from \'" + srcFile + "\' to \'" + destFile + "\'");
            } else {
                if(preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }

            }
        }
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, (FileFilter)null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        if(srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if(destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if(!srcDir.exists()) {
            throw new FileNotFoundException("Source \'" + srcDir + "\' does not exist");
        } else if(!srcDir.isDirectory()) {
            throw new IOException("Source \'" + srcDir + "\' exists but is not a directory");
        } else if(srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source \'" + srcDir + "\' and destination \'" + destDir + "\' are the same");
        } else {
            ArrayList exclusionList = null;
            if(destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                File[] srcFiles = filter == null?srcDir.listFiles():srcDir.listFiles(filter);
                if(srcFiles != null && srcFiles.length > 0) {
                    exclusionList = new ArrayList(srcFiles.length);

                    for(int i = 0; i < srcFiles.length; ++i) {
                        File copiedFile = new File(destDir, srcFiles[i].getName());
                        exclusionList.add(copiedFile.getCanonicalPath());
                    }
                }
            }

            doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
        }
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
        if(destDir.exists()) {
            if(!destDir.isDirectory()) {
                throw new IOException("Destination \'" + destDir + "\' exists but is not a directory");
            }
        } else {
            if(!destDir.mkdirs()) {
                throw new IOException("Destination \'" + destDir + "\' directory cannot be created");
            }

            if(preserveFileDate) {
                destDir.setLastModified(srcDir.lastModified());
            }
        }

        if(!destDir.canWrite()) {
            throw new IOException("Destination \'" + destDir + "\' cannot be written to");
        } else {
            File[] files = filter == null?srcDir.listFiles():srcDir.listFiles(filter);
            if(files == null) {
                throw new IOException("Failed to list contents of " + srcDir);
            } else {
                for(int i = 0; i < files.length; ++i) {
                    File copiedFile = new File(destDir, files[i].getName());
                    if(exclusionList == null || !exclusionList.contains(files[i].getCanonicalPath())) {
                        if(files[i].isDirectory()) {
                            doCopyDirectory(files[i], copiedFile, filter, preserveFileDate, exclusionList);
                        } else {
                            doCopyFile(files[i], copiedFile, preserveFileDate);
                        }
                    }
                }

            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if(directory.exists()) {
            cleanDirectory(directory);
            if(!directory.delete()) {
                String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }
    }

    public static boolean deleteQuietly(File file) {
        if(file == null) {
            return false;
        } else {
            try {
                if(file.isDirectory()) {
                    cleanDirectory(file);
                }
            } catch (Exception var3) {
                ;
            }

            try {
                return file.delete();
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        String var7;
        if(!directory.exists()) {
            var7 = directory + " does not exist";
            throw new IllegalArgumentException(var7);
        } else if(!directory.isDirectory()) {
            var7 = directory + " is not a directory";
            throw new IllegalArgumentException(var7);
        } else {
            File[] files = directory.listFiles();
            if(files == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException exception = null;

                for(int i = 0; i < files.length; ++i) {
                    File file = files[i];

                    try {
                        forceDelete(file);
                    } catch (IOException var6) {
                        exception = var6;
                    }
                }

                if(exception != null) {
                    throw exception;
                }
            }
        }
    }

    public static String readFileToString(File file, String encoding) throws IOException {
        FileInputStream in = null;

        String var4;
        try {
            in = openInputStream(file);
            var4 =  IOUtils.toString(in, encoding);
        } finally {
             IOUtils.closeQuietly(in);
        }

        return var4;
    }

    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, (String)null);
    }

    public static void forceDelete(File file) throws IOException {
        if(file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if(!file.delete()) {
                if(!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }

                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }

    }

    public static void forceDeleteOnExit(File file) throws IOException {
        if(file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }

    }

    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if(directory.exists()) {
            cleanDirectoryOnExit(directory);
            directory.deleteOnExit();
        }
    }

    private static void cleanDirectoryOnExit(File directory) throws IOException {
        String var7;
        if(!directory.exists()) {
            var7 = directory + " does not exist";
            throw new IllegalArgumentException(var7);
        } else if(!directory.isDirectory()) {
            var7 = directory + " is not a directory";
            throw new IllegalArgumentException(var7);
        } else {
            File[] files = directory.listFiles();
            if(files == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException exception = null;

                for(int i = 0; i < files.length; ++i) {
                    File file = files[i];

                    try {
                        forceDeleteOnExit(file);
                    } catch (IOException var6) {
                        exception = var6;
                    }
                }

                if(exception != null) {
                    throw exception;
                }
            }
        }
    }

    public static void forceMkdir(File directory) throws IOException {
        String message;
        if(directory.exists()) {
            if(directory.isFile()) {
                message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else if(!directory.mkdirs()) {
            message = "Unable to create directory " + directory;
            throw new IOException(message);
        }

    }

    public static long sizeOfDirectory(File directory) {
        String var6;
        if(!directory.exists()) {
            var6 = directory + " does not exist";
            throw new IllegalArgumentException(var6);
        } else if(!directory.isDirectory()) {
            var6 = directory + " is not a directory";
            throw new IllegalArgumentException(var6);
        } else {
            long size = 0L;
            File[] files = directory.listFiles();
            if(files == null) {
                return 0L;
            } else {
                for(int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if(file.isDirectory()) {
                        size += sizeOfDirectory(file);
                    } else {
                        size += file.length();
                    }
                }

                return size;
            }
        }
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if(srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if(destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if(!srcDir.exists()) {
            throw new FileNotFoundException("Source \'" + srcDir + "\' does not exist");
        } else if(!srcDir.isDirectory()) {
            throw new IOException("Source \'" + srcDir + "\' is not a directory");
        } else if(destDir.exists()) {
            throw new IOException("Destination \'" + destDir + "\' already exists");
        } else {
            boolean rename = srcDir.renameTo(destDir);
            if(!rename) {
                copyDirectory(srcDir, destDir);
                deleteDirectory(srcDir);
                if(srcDir.exists()) {
                    throw new IOException("Failed to delete original directory \'" + srcDir + "\' after copy to \'" + destDir + "\'");
                }
            }

        }
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if(srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if(destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if(!srcFile.exists()) {
            throw new FileNotFoundException("Source \'" + srcFile + "\' does not exist");
        } else if(srcFile.isDirectory()) {
            throw new IOException("Source \'" + srcFile + "\' is a directory");
        } else if(destFile.exists()) {
            throw new IOException("Destination \'" + destFile + "\' already exists");
        } else if(destFile.isDirectory()) {
            throw new IOException("Destination \'" + destFile + "\' is a directory");
        } else {
            boolean rename = srcFile.renameTo(destFile);
            if(!rename) {
                copyFile(srcFile, destFile);
                if(!srcFile.delete()) {
                    deleteQuietly(destFile);
                    throw new IOException("Failed to delete original file \'" + srcFile + "\' after copy to \'" + destFile + "\'");
                }
            }

        }
    }
}
