package com.changcai.buyer.util.asop;

/**
 * Created by liuxingwei on 2017/1/24.
 */


import com.changcai.buyer.util.asop.commons.FileUtils;
import com.changcai.buyer.util.asop.commons.FilenameUtils;
import com.changcai.buyer.util.asop.commons.IOUtils;
import com.changcai.buyer.util.asop.transform.ZipEntryTransformer;
import com.changcai.buyer.util.asop.transform.ZipEntryTransformerEntry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtil {
    private static final String PATH_SEPARATOR = "/";
    public static final int DEFAULT_COMPRESSION_LEVEL = -1;

    private ZipUtil() {
    }

    public static boolean containsEntry(File zip, String name) {
        ZipFile zf = null;

        boolean var5;
        try {
            zf = new ZipFile(zip);
            var5 = zf.getEntry(name) != null;
        } catch (IOException var8) {
            throw ZipExceptionUtil.rethrow(var8);
        } finally {
            closeQuietly(zf);
        }

        return var5;
    }

    public static boolean containsAnyEntry(File zip, String[] names) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);

            for (int e = 0; e < names.length; ++e) {
                if (zf.getEntry(names[e]) != null) {
                    return true;
                }
            }
        } catch (IOException var7) {
            throw ZipExceptionUtil.rethrow(var7);
        } finally {
            closeQuietly(zf);
        }

        return false;
    }

    public static byte[] unpackEntry(File zip, String name) {
        ZipFile zf = null;

        byte[] var5;
        try {
            zf = new ZipFile(zip);
            var5 = doUnpackEntry(zf, name);
        } catch (IOException var8) {
            throw ZipExceptionUtil.rethrow(var8);
        } finally {
            closeQuietly(zf);
        }

        return var5;
    }

    public static byte[] unpackEntry(ZipFile zf, String name) {
        try {
            return doUnpackEntry(zf, name);
        } catch (IOException var3) {
            throw ZipExceptionUtil.rethrow(var3);
        }
    }

    private static byte[] doUnpackEntry(ZipFile zf, String name) throws IOException {
        ZipEntry ze = zf.getEntry(name);
        if (ze == null) {
            return null;
        } else {
            InputStream is = zf.getInputStream(ze);

            byte[] var5;
            try {
                var5 = IOUtils.toByteArray(is);
            } finally {
                IOUtils.closeQuietly(is);
            }

            return var5;
        }
    }

    public static byte[] unpackEntry(InputStream is, String name) {
        ZipUtil.ByteArrayUnpacker action = new ZipUtil.ByteArrayUnpacker( );
        return !handle((InputStream) is, name, action) ? null : action.getBytes();
    }

    public static boolean unpackEntry(File zip, String name, File file) {
        ZipFile zf = null;

        boolean var6;
        try {
            zf = new ZipFile(zip);
            var6 = doUnpackEntry(zf, name, file);
        } catch (IOException var9) {
            throw ZipExceptionUtil.rethrow(var9);
        } finally {
            closeQuietly(zf);
        }

        return var6;
    }

    public static boolean unpackEntry(ZipFile zf, String name, File file) {
        try {
            return doUnpackEntry(zf, name, file);
        } catch (IOException var4) {
            throw ZipExceptionUtil.rethrow(var4);
        }
    }

    private static boolean doUnpackEntry(ZipFile zf, String name, File file) throws IOException {
        ZipEntry ze = zf.getEntry(name);
        if (ze == null) {
            return false;
        } else {
            BufferedInputStream in = new BufferedInputStream(zf.getInputStream(ze));

            try {
                FileUtils.copy(in, file);
            } finally {
                IOUtils.closeQuietly(in);
            }

            return true;
        }
    }

    public static boolean unpackEntry(InputStream is, String name, File file) throws IOException {
        return handle((InputStream) is, name, new ZipUtil.FileUnpacker(file));
    }

    public static void iterate(File zip, ZipEntryCallback action) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);
            Enumeration e = zf.entries();

            while (e.hasMoreElements()) {
                ZipEntry e1 = (ZipEntry) e.nextElement();
                InputStream is = zf.getInputStream(e1);

                try {
                    action.process(is, e1);
                } catch (IOException var19) {
                    throw new ZipException("Failed to process zip entry \'" + e1.getName() + "\' with action " + action, var19);
                } catch (ZipBreakException var20) {
                    break;
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
        } catch (IOException var22) {
            throw ZipExceptionUtil.rethrow(var22);
        } finally {
            closeQuietly(zf);
        }

    }

    public static void iterate(File zip, String[] entryNames, ZipEntryCallback action) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);

            for (int e = 0; e < entryNames.length; ++e) {
                ZipEntry e1 = zf.getEntry(entryNames[e]);
                if (e1 != null) {
                    InputStream is = zf.getInputStream(e1);

                    try {
                        action.process(is, e1);
                    } catch (IOException var20) {
                        throw new ZipException("Failed to process zip entry \'" + e1.getName() + " with action " + action, var20);
                    } catch (ZipBreakException var21) {
                        break;
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                }
            }
        } catch (IOException var23) {
            throw ZipExceptionUtil.rethrow(var23);
        } finally {
            closeQuietly(zf);
        }

    }

    public static void iterate(File zip, ZipInfoCallback action) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);
            Enumeration e = zf.entries();

            while (e.hasMoreElements()) {
                ZipEntry e1 = (ZipEntry) e.nextElement();

                try {
                    action.process(e1);
                } catch (IOException var11) {
                    throw new ZipException("Failed to process zip entry \'" + e1.getName() + " with action " + action, var11);
                } catch (ZipBreakException var12) {
                    break;
                }
            }
        } catch (IOException var13) {
            throw ZipExceptionUtil.rethrow(var13);
        } finally {
            closeQuietly(zf);
        }

    }

    public static void iterate(File zip, String[] entryNames, ZipInfoCallback action) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);

            for (int e = 0; e < entryNames.length; ++e) {
                ZipEntry e1 = zf.getEntry(entryNames[e]);
                if (e1 != null) {
                    try {
                        action.process(e1);
                    } catch (IOException var12) {
                        throw new ZipException("Failed to process zip entry \'" + e1.getName() + " with action " + action, var12);
                    } catch (ZipBreakException var13) {
                        break;
                    }
                }
            }
        } catch (IOException var14) {
            throw ZipExceptionUtil.rethrow(var14);
        } finally {
            closeQuietly(zf);
        }

    }

    public static void iterate(InputStream is, ZipEntryCallback action, Charset charset) {
        try {
            ZipInputStream e = null;
            if (charset == null) {
                e = new ZipInputStream(new BufferedInputStream(is));
            } else {
                e = ZipFileUtil.createZipInputStream(is, charset);
            }

            ZipEntry entry;
            while ((entry = e.getNextEntry()) != null) {
                try {
                    action.process(e, entry);
                } catch (IOException var6) {
                    throw new ZipException("Failed to process zip entry \'" + entry.getName() + " with action " + action, var6);
                } catch (ZipBreakException var7) {
                    break;
                }
            }

        } catch (IOException var8) {
            throw ZipExceptionUtil.rethrow(var8);
        }
    }

    public static void iterate(InputStream is, ZipEntryCallback action) {
        iterate((InputStream) is, (ZipEntryCallback) action, (Charset) null);
    }

    public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action, Charset charset) {
        HashSet namesSet = new HashSet();

        for (int e = 0; e < entryNames.length; ++e) {
            namesSet.add(entryNames[e]);
        }

        try {
            ZipInputStream var11 = null;
            if (charset == null) {
                var11 = new ZipInputStream(new BufferedInputStream(is));
            } else {
                var11 = ZipFileUtil.createZipInputStream(is, charset);
            }

            ZipEntry entry;
            while ((entry = var11.getNextEntry()) != null) {
                if (namesSet.contains(entry.getName())) {
                    try {
                        action.process(var11, entry);
                    } catch (IOException var8) {
                        throw new ZipException("Failed to process zip entry \'" + entry.getName() + " with action " + action, var8);
                    } catch (ZipBreakException var9) {
                        break;
                    }
                }
            }

        } catch (IOException var10) {
            throw ZipExceptionUtil.rethrow(var10);
        }
    }

    public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action) {
        iterate(is, entryNames, action, (Charset) null);
    }

    public static boolean handle(File zip, String name, ZipEntryCallback action) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zip);
            ZipEntry e = zf.getEntry(name);
            if (e == null) {
                return false;
            }

            BufferedInputStream in = new BufferedInputStream(zf.getInputStream(e));

            try {
                action.process(in, e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        } catch (IOException var15) {
            throw ZipExceptionUtil.rethrow(var15);
        } finally {
            closeQuietly(zf);
        }

        return true;
    }

    public static boolean handle(InputStream is, String name, ZipEntryCallback action) {
        ZipUtil.SingleZipEntryCallback helper = new ZipUtil.SingleZipEntryCallback(name, action);
        iterate((InputStream) is, (ZipEntryCallback) helper);
        return helper.found();
    }

    public static void unpack(File zip, File outputDir) {
        unpack(zip, outputDir, IdentityNameMapper.INSTANCE);
    }

    public static void unpack(File zip, File outputDir, NameMapper mapper) {
        iterate((File) zip, (ZipEntryCallback) (new ZipUtil.Unpacker(outputDir, mapper)));
    }

    public static void unwrap(File zip, File outputDir) {
        unwrap(zip, outputDir, IdentityNameMapper.INSTANCE);
    }

    public static void unwrap(File zip, File outputDir, NameMapper mapper) {
        iterate((File) zip, (ZipEntryCallback) (new ZipUtil.Unwraper(outputDir, mapper)));
    }

    public static void unpack(InputStream is, File outputDir) {
        unpack(is, outputDir, IdentityNameMapper.INSTANCE);
    }

    public static void unpack(InputStream is, File outputDir, NameMapper mapper) {
        iterate((InputStream) is, (ZipEntryCallback) (new ZipUtil.Unpacker(outputDir, mapper)));
    }

    public static void unwrap(InputStream is, File outputDir) {
        unwrap(is, outputDir, IdentityNameMapper.INSTANCE);
    }

    public static void unwrap(InputStream is, File outputDir, NameMapper mapper) {
        iterate((InputStream) is, (ZipEntryCallback) (new ZipUtil.Unwraper(outputDir, mapper)));
    }

    public static void explode(File zip) {
        try {
            File e = FileUtils.getTempFileFor(zip);
            FileUtils.moveFile(zip, e);
            unpack(e, zip);
            if (!e.delete()) {
                throw new IOException("Unable to delete file: " + e);
            }
        } catch (IOException var2) {
            throw ZipExceptionUtil.rethrow(var2);
        }
    }

    public static byte[] packEntry(File file) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        try {
            ZipOutputStream e = new ZipOutputStream(result);
            ZipEntry entry = new ZipEntry(file.getName());
            entry.setTime(file.lastModified());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            try {
                ZipEntryUtil.addEntry(entry, in, e);
            } finally {
                IOUtils.closeQuietly(in);
            }

            e.close();
        } catch (IOException var9) {
            throw ZipExceptionUtil.rethrow(var9);
        }

        return result.toByteArray();
    }

    public static void pack(File rootDir, File zip) {
        pack(rootDir, zip, -1);
    }

    public static void pack(File rootDir, File zip, int compressionLevel) {
        pack(rootDir, zip, IdentityNameMapper.INSTANCE, compressionLevel);
    }

    public static void pack(File sourceDir, File targetZipFile, boolean preserveRoot) {
        if (preserveRoot) {
            final String parentName = sourceDir.getName();
            pack(sourceDir, targetZipFile, new NameMapper() {
                public String map(String name) {
                    return parentName + "/" + name;
                }
            });
        } else {
            pack(sourceDir, targetZipFile);
        }

    }

    public static void packEntry(File fileToPack, File destZipFile) {
        packEntry(fileToPack, destZipFile, IdentityNameMapper.INSTANCE);
    }

    public static void packEntry(File fileToPack, File destZipFile, final String fileName) {
        packEntry(fileToPack, destZipFile, new NameMapper() {
            public String map(String name) {
                return fileName;
            }
        });
    }

    public static void packEntry(File fileToPack, File destZipFile, NameMapper mapper) {
        packEntries(new File[]{fileToPack}, destZipFile, mapper);
    }

    public static void packEntries(File[] filesToPack, File destZipFile) {
        packEntries(filesToPack, destZipFile, IdentityNameMapper.INSTANCE);
    }

    public static void packEntries(File[] filesToPack, File destZipFile, NameMapper mapper) {
        ZipOutputStream out = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(destZipFile);
            out = new ZipOutputStream(new BufferedOutputStream(fos));

            for (int e = 0; e < filesToPack.length; ++e) {
                File fileToPack = filesToPack[e];
                ZipEntry zipEntry = new ZipEntry(mapper.map(fileToPack.getName()));
                zipEntry.setSize(fileToPack.length());
                zipEntry.setTime(fileToPack.lastModified());
                out.putNextEntry(zipEntry);
                FileUtils.copy(fileToPack, out);
                out.closeEntry();
            }
        } catch (IOException var11) {
            throw ZipExceptionUtil.rethrow(var11);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(fos);
        }

    }

    public static void pack(File sourceDir, File targetZip, NameMapper mapper) {
        pack(sourceDir, targetZip, mapper, -1);
    }

    public static void pack(File sourceDir, File targetZip, NameMapper mapper, int compressionLevel) {
        if (!sourceDir.exists()) {
            throw new ZipException("Given file \'" + sourceDir + "\' doesn\'t exist!");
        } else {
            ZipOutputStream out = null;

            try {
                out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetZip)));
                out.setLevel(compressionLevel);
                pack(sourceDir, out, mapper, "", true);
            } catch (IOException var9) {
                throw ZipExceptionUtil.rethrow(var9);
            } finally {
                IOUtils.closeQuietly(out);
            }

        }
    }

    private static void pack(File dir, ZipOutputStream out, NameMapper mapper, String pathPrefix, boolean mustHaveChildren) throws IOException {
        String[] filenames = dir.list();
        if (filenames == null) {
            if (!dir.exists()) {
                throw new ZipException("Given file \'" + dir + "\' doesn\'t exist!");
            } else {
                throw new IOException("Given file is not a directory \'" + dir + "\'");
            }
        } else if (mustHaveChildren && filenames.length == 0) {
            throw new ZipException("Given directory \'" + dir + "\' doesn\'t contain any files!");
        } else {
            for (int i = 0; i < filenames.length; ++i) {
                String filename = filenames[i];
                File file = new File(dir, filename);
                boolean isDir = file.isDirectory();
                String path = pathPrefix + file.getName();
                if (isDir) {
                    path = path + "/";
                }

                String name = mapper.map(path);
                if (name != null) {
                    ZipEntry zipEntry = new ZipEntry(name);
                    if (!isDir) {
                        zipEntry.setSize(file.length());
                        zipEntry.setTime(file.lastModified());
                    }

                    out.putNextEntry(zipEntry);
                    if (!isDir) {
                        FileUtils.copy(file, out);
                    }

                    out.closeEntry();
                }

                if (isDir) {
                    pack(file, out, mapper, path, false);
                }
            }

        }
    }

    public static void repack(File srcZip, File dstZip, int compressionLevel) {
        ZipUtil.RepackZipEntryCallback callback = new ZipUtil.RepackZipEntryCallback(dstZip, compressionLevel);

        try {
            iterate((File) srcZip, (ZipEntryCallback) callback);
        } finally {
            callback.closeStream();
        }

    }

    public static void repack(InputStream is, File dstZip, int compressionLevel) {
        ZipUtil.RepackZipEntryCallback callback = new ZipUtil.RepackZipEntryCallback(dstZip, compressionLevel);

        try {
            iterate((InputStream) is, (ZipEntryCallback) callback);
        } finally {
            callback.closeStream();
        }

    }

    public static void repack(File zip, int compressionLevel) {
        try {
            File e = FileUtils.getTempFileFor(zip);
            repack(zip, e, compressionLevel);
            if (!zip.delete()) {
                throw new IOException("Unable to delete the file: " + zip);
            } else {
                FileUtils.moveFile(e, zip);
            }
        } catch (IOException var3) {
            throw ZipExceptionUtil.rethrow(var3);
        }
    }

    public static void unexplode(File dir) {
        unexplode(dir, -1);
    }

    public static void unexplode(File dir, int compressionLevel) {
        try {
            File e = FileUtils.getTempFileFor(dir);
            pack(dir, e, compressionLevel);
            FileUtils.deleteDirectory(dir);
            FileUtils.moveFile(e, dir);
        } catch (IOException var3) {
            throw ZipExceptionUtil.rethrow(var3);
        }
    }

    public static void pack(ZipEntrySource[] entries, File zip) {
        ZipOutputStream out = null;

        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));

            for (int e = 0; e < entries.length; ++e) {
                addEntry(entries[e], out);
            }
        } catch (IOException var7) {
            throw ZipExceptionUtil.rethrow(var7);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public static void addEntry(File zip, String path, File file, File destZip) {
        addEntry(zip, (ZipEntrySource) (new FileSource(path, file)), (File) destZip);
    }

    public static void addEntry(final File zip, final String path, final File file) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.addEntry(zip, path, file, tmpFile);
                return true;
            }
        });
    }

    public static void addEntry(File zip, String path, byte[] bytes, File destZip) {
        addEntry(zip, (ZipEntrySource) (new ByteSource(path, bytes)), (File) destZip);
    }

    public static void addEntry(final File zip, final String path, final byte[] bytes) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.addEntry(zip, path, bytes, tmpFile);
                return true;
            }
        });
    }

    public static void addEntry(File zip, ZipEntrySource entry, File destZip) {
        addEntries(zip, new ZipEntrySource[]{entry}, destZip);
    }

    public static void addEntry(final File zip, final ZipEntrySource entry) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.addEntry(zip, entry, tmpFile);
                return true;
            }
        });
    }

    public static void addEntries(File zip, ZipEntrySource[] entries, File destZip) {
        ZipOutputStream out = null;

        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
            copyEntries(zip, out);

            for (int e = 0; e < entries.length; ++e) {
                addEntry(entries[e], out);
            }
        } catch (IOException var8) {
            ZipExceptionUtil.rethrow(var8);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public static void addEntries(final File zip, final ZipEntrySource[] entries) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.addEntries(zip, entries, tmpFile);
                return true;
            }
        });
    }

    public static void removeEntry(File zip, String path, File destZip) {
        removeEntries(zip, new String[]{path}, destZip);
    }

    public static void removeEntry(final File zip, final String path) {
        operateInPlace(zip, new ZipUtil.InPlaceAction( ) {
            public boolean act(File tmpFile) {
                ZipUtil.removeEntry(zip, path, tmpFile);
                return true;
            }
        });
    }

    public static void removeEntries(File zip, String[] paths, File destZip) {
        ZipOutputStream out = null;

        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
            copyEntries(zip, out, new HashSet(Arrays.asList(paths)));
        } catch (IOException var8) {
            throw ZipExceptionUtil.rethrow(var8);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public static void removeEntries(final File zip, final String[] paths) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.removeEntries(zip, paths, tmpFile);
                return true;
            }
        });
    }

    private static void copyEntries(File zip, final ZipOutputStream out) {
        final HashSet names = new HashSet();
        iterate(zip, new ZipEntryCallback() {
            public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                String entryName = zipEntry.getName();
                if (names.add(entryName)) {
                    ZipEntryUtil.copyEntry(zipEntry, in, out);
                }

            }
        });
    }

    private static void copyEntries(File zip, final ZipOutputStream out, final Set<String> ignoredEntries) {
        final HashSet names = new HashSet();
        final Set dirNames = filterDirEntries(zip, ignoredEntries);
        iterate(zip, new ZipEntryCallback() {
            public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                String entryName = zipEntry.getName();
                if (!ignoredEntries.contains(entryName)) {
                    Iterator var5 = dirNames.iterator();

                    while (var5.hasNext()) {
                        String dirName = (String) var5.next();
                        if (entryName.startsWith(dirName)) {
                            return;
                        }
                    }

                    if (names.add(entryName)) {
                        ZipEntryUtil.copyEntry(zipEntry, in, out);
                    }

                }
            }
        });
    }

    static Set<String> filterDirEntries(File zip, Collection<String> names) {
        HashSet dirs = new HashSet();
        if (zip == null) {
            return dirs;
        } else {
            ZipFile zf = null;

            try {
                zf = new ZipFile(zip);
                Iterator var5 = names.iterator();

                while (var5.hasNext()) {
                    String e = (String) var5.next();
                    ZipEntry entry = zf.getEntry(e);
                    if (entry.isDirectory()) {
                        dirs.add(entry.getName());
                    } else if (zf.getInputStream(entry) == null) {
                        dirs.add(entry.getName() + "/");
                    }
                }
            } catch (IOException var10) {
                ZipExceptionUtil.rethrow(var10);
            } finally {
                closeQuietly(zf);
            }

            return dirs;
        }
    }

    public static boolean replaceEntry(File zip, String path, File file, File destZip) {
        return replaceEntry(zip, (ZipEntrySource) (new FileSource(path, file)), (File) destZip);
    }

    public static boolean replaceEntry(final File zip, final String path, final File file) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                return ZipUtil.replaceEntry(zip, (ZipEntrySource) (new FileSource(path, file)), (File) tmpFile);
            }
        });
    }

    public static boolean replaceEntry(File zip, String path, byte[] bytes, File destZip) {
        return replaceEntry(zip, (ZipEntrySource) (new ByteSource(path, bytes)), (File) destZip);
    }

    public static boolean replaceEntry(final File zip, final String path, final byte[] bytes) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                return ZipUtil.replaceEntry(zip, (ZipEntrySource) (new ByteSource(path, bytes)), (File) tmpFile);
            }
        });
    }

    public static boolean replaceEntry(File zip, ZipEntrySource entry, File destZip) {
        return replaceEntries(zip, new ZipEntrySource[]{entry}, destZip);
    }

    public static boolean replaceEntry(final File zip, final ZipEntrySource entry) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                return ZipUtil.replaceEntry(zip, entry, tmpFile);
            }
        });
    }

    public static boolean replaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
        final Map entryByPath = entriesByPath(entries);
        int entryCount = entryByPath.size();

        try {
            final ZipOutputStream e = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));

            try {
                final HashSet names = new HashSet();
                iterate(zip, new ZipEntryCallback() {
                    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                        if (names.add(zipEntry.getName())) {
                            ZipEntrySource entry = (ZipEntrySource) entryByPath.remove(zipEntry.getName());
                            if (entry != null) {
                                ZipUtil.addEntry(entry, e);
                            } else {
                                ZipEntryUtil.copyEntry(zipEntry, in, e);
                            }
                        }

                    }
                });
            } finally {
                IOUtils.closeQuietly(e);
            }
        } catch (IOException var11) {
            ZipExceptionUtil.rethrow(var11);
        }

        return entryByPath.size() < entryCount;
    }

    public static boolean replaceEntries(final File zip, final ZipEntrySource[] entries) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction( ) {
            public boolean act(File tmpFile) {
                return ZipUtil.replaceEntries(zip, entries, tmpFile);
            }
        });
    }

    public static void addOrReplaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
        final Map entryByPath = entriesByPath(entries);

        try {
            final ZipOutputStream e = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));

            try {
                final HashSet names = new HashSet();
                iterate(zip, new ZipEntryCallback() {
                    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                        if (names.add(zipEntry.getName())) {
                            ZipEntrySource entry = (ZipEntrySource) entryByPath.remove(zipEntry.getName());
                            if (entry != null) {
                                ZipUtil.addEntry(entry, e);
                            } else {
                                ZipEntryUtil.copyEntry(zipEntry, in, e);
                            }
                        }

                    }
                });
                Iterator var7 = entryByPath.values().iterator();

                while (var7.hasNext()) {
                    ZipEntrySource zipEntrySource = (ZipEntrySource) var7.next();
                    addEntry(zipEntrySource, e);
                }
            } finally {
                IOUtils.closeQuietly(e);
            }
        } catch (IOException var12) {
            ZipExceptionUtil.rethrow(var12);
        }

    }

    public static void addOrReplaceEntries(final File zip, final ZipEntrySource[] entries) {
        operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                ZipUtil.addOrReplaceEntries(zip, entries, tmpFile);
                return true;
            }
        });
    }

    static Map<String, ZipEntrySource> entriesByPath(ZipEntrySource... entries) {
        HashMap result = new HashMap();

        for (int i = 0; i < entries.length; ++i) {
            ZipEntrySource source = entries[i];
            result.put(source.getPath(), source);
        }

        return result;
    }

    public static boolean transformEntry(File zip, String path, ZipEntryTransformer transformer, File destZip) {
        return transformEntry(zip, new ZipEntryTransformerEntry(path, transformer), destZip);
    }

    public static boolean transformEntry(final File zip, final String path, final ZipEntryTransformer transformer) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction() {
            public boolean act(File tmpFile) {
                return ZipUtil.transformEntry(zip, path, transformer, tmpFile);
            }
        });
    }

    public static boolean transformEntry(File zip, ZipEntryTransformerEntry entry, File destZip) {
        return transformEntries(zip, new ZipEntryTransformerEntry[]{entry}, destZip);
    }

    public static boolean transformEntry(final File zip, final ZipEntryTransformerEntry entry) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction( ) {
            public boolean act(File tmpFile) {
                return ZipUtil.transformEntry(zip, entry, tmpFile);
            }
        });
    }

    public static boolean transformEntries(File zip, ZipEntryTransformerEntry[] entries, File destZip) {
        try {
            ZipOutputStream e = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));

            boolean var6;
            try {
                ZipUtil.TransformerZipEntryCallback action = new ZipUtil.TransformerZipEntryCallback(Arrays.asList(entries), e);
                iterate((File) zip, (ZipEntryCallback) action);
                var6 = action.found();
            } finally {
                IOUtils.closeQuietly(e);
            }

            return var6;
        } catch (IOException var10) {
            throw ZipExceptionUtil.rethrow(var10);
        }
    }

    public static boolean transformEntries(final File zip, final ZipEntryTransformerEntry[] entries) {
        return operateInPlace(zip, new ZipUtil.InPlaceAction( ) {
            public boolean act(File tmpFile) {
                return ZipUtil.transformEntries(zip, entries, tmpFile);
            }
        });
    }

    public static boolean transformEntry(InputStream is, String path, ZipEntryTransformer transformer, OutputStream os) {
        return transformEntry(is, new ZipEntryTransformerEntry(path, transformer), os);
    }

    public static boolean transformEntry(InputStream is, ZipEntryTransformerEntry entry, OutputStream os) {
        return transformEntries(is, new ZipEntryTransformerEntry[]{entry}, os);
    }

    public static boolean transformEntries(InputStream is, ZipEntryTransformerEntry[] entries, OutputStream os) {
        try {
            ZipOutputStream e = new ZipOutputStream(os);
            ZipUtil.TransformerZipEntryCallback action = new ZipUtil.TransformerZipEntryCallback(Arrays.asList(entries), e);
            iterate((InputStream) is, (ZipEntryCallback) action);
            e.finish();
            return action.found();
        } catch (IOException var5) {
            throw ZipExceptionUtil.rethrow(var5);
        }
    }

    static Map<String, ZipEntryTransformer> transformersByPath(List<ZipEntryTransformerEntry> entries) {
        HashMap result = new HashMap();
        Iterator var3 = entries.iterator();

        while (var3.hasNext()) {
            ZipEntryTransformerEntry entry = (ZipEntryTransformerEntry) var3.next();
            result.put(entry.getPath(), entry.getTransformer());
        }

        return result;
    }

    private static void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
        out.putNextEntry(entry.getEntry());
        InputStream in = entry.getInputStream();
        if (in != null) {
            try {
                IOUtils.copy(in, out);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        out.closeEntry();
    }

    public static boolean archiveEquals(File f1, File f2) {
        try {
            if (FileUtils.contentEquals(f1, f2)) {
                return true;
            } else {
                long e = System.currentTimeMillis();
                boolean result = archiveEqualsInternal(f1, f2);
                long time = System.currentTimeMillis() - e;
                return result;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    private static boolean archiveEqualsInternal(File f1, File f2) throws IOException {
        ZipFile zf1 = null;
        ZipFile zf2 = null;

        try {
            zf1 = new ZipFile(f1);
            zf2 = new ZipFile(f2);
            Enumeration en = zf1.entries();

            while (en.hasMoreElements()) {
                ZipEntry e1 = (ZipEntry) en.nextElement();
                String path = e1.getName();
                ZipEntry e2 = zf2.getEntry(path);
                if (!metaDataEquals(path, e1, e2)) {
                    return false;
                }

                InputStream is1 = null;
                InputStream is2 = null;

                try {
                    is1 = zf1.getInputStream(e1);
                    is2 = zf2.getInputStream(e2);
                } finally {
                    IOUtils.closeQuietly(is1);
                    IOUtils.closeQuietly(is2);
                }
            }
        } finally {
            closeQuietly(zf1);
            closeQuietly(zf2);
        }

        return true;
    }

    private static boolean metaDataEquals(String path, ZipEntry e1, ZipEntry e2) throws IOException {
        if (e2 == null) {
            return false;
        } else if (e1.isDirectory()) {
            return e2.isDirectory();
        } else if (e2.isDirectory()) {
            return false;
        } else {
            long size1 = e1.getSize();
            long size2 = e2.getSize();
            if (size1 != -1L && size2 != -1L && size1 != size2) {
                return false;
            } else {
                long crc1 = e1.getCrc();
                long crc2 = e2.getCrc();
                return crc1 == -1L || crc2 == -1L || crc1 == crc2;
            }
        }
    }

    public static boolean entryEquals(File f1, File f2, String path) {
        return entryEquals(f1, f2, path, path);
    }

    public static boolean entryEquals(File f1, File f2, String path1, String path2) {
        ZipFile zf1 = null;
        ZipFile zf2 = null;

        boolean var8;
        try {
            zf1 = new ZipFile(f1);
            zf2 = new ZipFile(f2);
            var8 = doEntryEquals(zf1, zf2, path1, path2);
        } catch (IOException var11) {
            throw ZipExceptionUtil.rethrow(var11);
        } finally {
            closeQuietly(zf1);
            closeQuietly(zf2);
        }

        return var8;
    }

    public static boolean entryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) {
        try {
            return doEntryEquals(zf1, zf2, path1, path2);
        } catch (IOException var5) {
            throw ZipExceptionUtil.rethrow(var5);
        }
    }

    private static boolean doEntryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) throws IOException {
        InputStream is1 = null;
        InputStream is2 = null;

        try {
            ZipEntry e1 = zf1.getEntry(path1);
            ZipEntry e2 = zf2.getEntry(path2);
            if (e1 == null && e2 == null) {
                return true;
            }

            if (e1 != null && e2 != null) {
                is1 = zf1.getInputStream(e1);
                is2 = zf2.getInputStream(e2);
                if (is1 == null && is2 == null) {
                    return true;
                }

                if (is1 != null && is2 != null) {
                    boolean var9 = IOUtils.contentEquals(is1, is2);
                    return var9;
                }
            }
        } finally {
            IOUtils.closeQuietly(is1);
            IOUtils.closeQuietly(is2);
        }

        return false;
    }

    public static void closeQuietly(ZipFile zf) {
        try {
            if (zf != null) {
                zf.close();
            }
        } catch (IOException var2) {
            ;
        }

    }

    private static boolean operateInPlace(File src, ZipUtil.InPlaceAction action) {
        File tmp = null;

        boolean var5;
        try {
            tmp = File.createTempFile("zt-zip-tmp", ".zip");
            boolean e = action.act(tmp);
            if (e) {
                FileUtils.forceDelete(src);
                FileUtils.moveFile(tmp, src);
            }

            var5 = e;
        } catch (IOException var8) {
            throw ZipExceptionUtil.rethrow(var8);
        } finally {
            FileUtils.deleteQuietly(tmp);
        }

        return var5;
    }

    private static class ByteArrayUnpacker implements ZipEntryCallback {
        private byte[] bytes;

        private ByteArrayUnpacker() {
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            this.bytes = IOUtils.toByteArray(in);
        }

        public byte[] getBytes() {
            return this.bytes;
        }
    }

    private static class FileUnpacker implements ZipEntryCallback {
        private final File file;

        public FileUnpacker(File file) {
            this.file = file;
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            FileUtils.copy(in, this.file);
        }
    }

    private abstract static class InPlaceAction {
        private InPlaceAction() {
        }

        abstract boolean act(File var1);
    }

    private static final class RepackZipEntryCallback implements ZipEntryCallback {
        private ZipOutputStream out;

        private RepackZipEntryCallback(File dstZip, int compressionLevel) {
            try {
                this.out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dstZip)));
                this.out.setLevel(compressionLevel);
            } catch (IOException var4) {
                ZipExceptionUtil.rethrow(var4);
            }

        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            ZipEntryUtil.copyEntry(zipEntry, in, this.out);
        }

        private void closeStream() {
            IOUtils.closeQuietly(this.out);
        }
    }

    private static class SingleZipEntryCallback implements ZipEntryCallback {
        private final String name;
        private final ZipEntryCallback action;
        private boolean found;

        public SingleZipEntryCallback(String name, ZipEntryCallback action) {
            this.name = name;
            this.action = action;
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            if (this.name.equals(zipEntry.getName())) {
                this.found = true;
                this.action.process(in, zipEntry);
            }

        }

        public boolean found() {
            return this.found;
        }
    }

    private static class TransformerZipEntryCallback implements ZipEntryCallback {
        private final Map<String, ZipEntryTransformer> entryByPath;
        private final int entryCount;
        private final ZipOutputStream out;
        private final Set<String> names = new HashSet();

        public TransformerZipEntryCallback(List<ZipEntryTransformerEntry> entries, ZipOutputStream out) {
            this.entryByPath = ZipUtil.transformersByPath(entries);
            this.entryCount = this.entryByPath.size();
            this.out = out;
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            if (this.names.add(zipEntry.getName())) {
                ZipEntryTransformer entry = (ZipEntryTransformer) this.entryByPath.remove(zipEntry.getName());
                if (entry != null) {
                    entry.transform(in, zipEntry, this.out);
                } else {
                    ZipEntryUtil.copyEntry(zipEntry, in, this.out);
                }
            }

        }

        public boolean found() {
            return this.entryByPath.size() < this.entryCount;
        }
    }

    private static class Unpacker implements ZipEntryCallback {
        private final File outputDir;
        private final NameMapper mapper;

        public Unpacker(File outputDir, NameMapper mapper) {
            this.outputDir = outputDir;
            this.mapper = mapper;
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            String name = this.mapper.map(zipEntry.getName());
            if (name != null) {
                File file = new File(this.outputDir, name);
                if (zipEntry.isDirectory()) {
                    FileUtils.forceMkdir(file);
                } else {
                    FileUtils.forceMkdir(file.getParentFile());
                    FileUtils.copy(in, file);
                }
            }

        }
    }

    private static class Unwraper implements ZipEntryCallback {
        private final File outputDir;
        private final NameMapper mapper;
        private String rootDir;

        public Unwraper(File outputDir, NameMapper mapper) {
            this.outputDir = outputDir;
            this.mapper = mapper;
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            String root = this.getRootName(zipEntry.getName());
            if (this.rootDir == null) {
                this.rootDir = root;
            } else if (!this.rootDir.equals(root)) {
                throw new ZipException("Unwrapping with multiple roots is not supported, roots: " + this.rootDir + ", " + root);
            }

            String name = this.mapper.map(this.getUnrootedName(root, zipEntry.getName()));
            if (name != null) {
                File file = new File(this.outputDir, name);
                if (zipEntry.isDirectory()) {
                    FileUtils.forceMkdir(file);
                } else {
                    FileUtils.forceMkdir(file.getParentFile());
                    FileUtils.copy(in, file);
                }
            }

        }

        private String getUnrootedName(String root, String name) {
            return name.substring(root.length());
        }

        private String getRootName(String name) {
            String newName = name.substring(FilenameUtils.getPrefixLength(name));
            int idx = newName.indexOf("/");
            if (idx < 0) {
                throw new ZipException("Entry " + newName + " from the root of the zip is not supported");
            } else {
                return newName.substring(0, newName.indexOf("/"));
            }
        }
    }
}

