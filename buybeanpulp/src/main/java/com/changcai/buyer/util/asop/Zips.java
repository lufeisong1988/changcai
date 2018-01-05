package com.changcai.buyer.util.asop;



import com.changcai.buyer.util.asop.commons.FileUtils;
import com.changcai.buyer.util.asop.commons.IOUtils;
import com.changcai.buyer.util.asop.transform.ZipEntryTransformer;
import com.changcai.buyer.util.asop.transform.ZipEntryTransformerEntry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class Zips {

    private final File src;
    private File dest;
    private Charset charset;
    private boolean preserveTimestamps;
    private List<ZipEntrySource> changedEntries = new ArrayList();
    private Set<String> removedEntries = new HashSet();
    private List<ZipEntryTransformerEntry> transformers = new ArrayList();
    private NameMapper nameMapper;
    private boolean unpackedResult;

    private Zips(File src) {
        this.src = src;
    }

    public static Zips get(File src) {
        return new  Zips(src);
    }

    public static Zips create() {
        return new Zips((File)null);
    }

    public Zips addEntry(ZipEntrySource entry) {
        this.changedEntries.add(entry);
        return this;
    }

    public Zips addEntries(ZipEntrySource[] entries) {
        this.changedEntries.addAll(Arrays.asList(entries));
        return this;
    }

    public Zips addFile(File file) {
        return this.addFile(file, false, (FileFilter)null);
    }

    public Zips addFile(File file, boolean preserveRoot) {
        return this.addFile(file, preserveRoot, (FileFilter)null);
    }

    public Zips addFile(File file, FileFilter filter) {
        return this.addFile(file, false, filter);
    }

    public Zips addFile(File file, boolean preserveRoot, FileFilter filter) {
        if(!file.isDirectory()) {
            this.changedEntries.add(new FileSource(file.getName(), file));
            return this;
        } else {
            Collection files = ZTFileUtil.listFiles(file);
            Iterator var6 = files.iterator();

            while(true) {
                File entryFile;
                do {
                    if(!var6.hasNext()) {
                        return this;
                    }

                    entryFile = (File)var6.next();
                } while(filter != null && !filter.accept(entryFile));

                String entryPath = this.getRelativePath(file, entryFile);
                if(File.separator.equals("\\")) {
                    entryPath = entryPath.replace('\\', '/');
                }

                if(preserveRoot) {
                    entryPath = file.getName() + entryPath;
                }

                if(entryPath.startsWith("/")) {
                    entryPath = entryPath.substring(1);
                }

                this.changedEntries.add(new FileSource(entryPath, entryFile));
            }
        }
    }

    private String getRelativePath(File parent, File file) {
        String parentPath = parent.getPath();
        String filePath = file.getPath();
        if(!filePath.startsWith(parentPath)) {
            throw new IllegalArgumentException("File " + file + " is not a child of " + parent);
        } else {
            return filePath.substring(parentPath.length());
        }
    }

    public Zips removeEntry(String entry) {
        this.removedEntries.add(entry);
        return this;
    }

    public Zips removeEntries(String[] entries) {
        this.removedEntries.addAll(Arrays.asList(entries));
        return this;
    }

    public Zips preserveTimestamps() {
        this.preserveTimestamps = true;
        return this;
    }

    public Zips setPreserveTimestamps(boolean preserve) {
        this.preserveTimestamps = preserve;
        return this;
    }

    public Zips charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public Zips destination(File destination) {
        this.dest = destination;
        return this;
    }

    public Zips nameMapper(NameMapper nameMapper) {
        this.nameMapper = nameMapper;
        return this;
    }

    public Zips unpack() {
        this.unpackedResult = true;
        return this;
    }

    private boolean isInPlace() {
        return this.dest == null;
    }

    private boolean isUnpack() {
        return this.unpackedResult || this.dest != null && this.dest.isDirectory();
    }

    public Zips addTransformer(String path, ZipEntryTransformer transformer) {
        this.transformers.add(new ZipEntryTransformerEntry(path, transformer));
        return this;
    }

    public void process() {
        if(this.src == null && this.dest == null) {
            throw new IllegalArgumentException("Source and destination shouldn\'t be null together");
        } else {
            File destinationFile = null;

            try {
                destinationFile = this.getDestinationFile();
                ZipOutputStream e = null;
               ZipEntryOrInfoAdapter zipEntryAdapter = null;
                if(destinationFile.isFile()) {
                    e = ZipFileUtil.createZipOutputStream(new BufferedOutputStream(new FileOutputStream(destinationFile)), this.charset);
                    zipEntryAdapter = new ZipEntryOrInfoAdapter(new Zips.CopyingCallback(this.transformers, e, this.preserveTimestamps), ( ZipInfoCallback)null);
                } else {
                    zipEntryAdapter = new ZipEntryOrInfoAdapter(new Zips.UnpackingCallback(this.transformers, destinationFile), ( ZipInfoCallback)null);
                }

                try {
                    this.processAllEntries(zipEntryAdapter);
                } finally {
                    IOUtils.closeQuietly(e);
                }

                this.handleInPlaceActions(destinationFile);
            } catch (IOException var13) {
                ZipExceptionUtil.rethrow(var13);
            } finally {
                if(this.isInPlace()) {
                    FileUtils.deleteQuietly(destinationFile);
                }

            }

        }
    }

    private void processAllEntries(ZipEntryOrInfoAdapter zipEntryAdapter) {
        this.iterateChangedAndAdded(zipEntryAdapter);
        this.iterateExistingExceptRemoved(zipEntryAdapter);
    }

    private File getDestinationFile() throws IOException {
        if(this.isUnpack()) {
            File result;
            if(this.isInPlace()) {
                result = File.createTempFile("zips", (String)null);
                FileUtils.deleteQuietly(result);
                result.mkdirs();
                return result;
            } else if(!this.dest.isDirectory()) {
                FileUtils.deleteQuietly(this.dest);
                result = new File(this.dest.getAbsolutePath());
                result.mkdirs();
                return result;
            } else {
                return this.dest;
            }
        } else if(this.isInPlace()) {
            return File.createTempFile("zips", ".zip");
        } else if(this.dest.isDirectory()) {
            FileUtils.deleteQuietly(this.dest);
            return new File(this.dest.getAbsolutePath());
        } else {
            return this.dest;
        }
    }

    public void iterate(ZipEntryCallback zipEntryCallback) {
        ZipEntryOrInfoAdapter zipEntryAdapter = new  ZipEntryOrInfoAdapter(zipEntryCallback, ( ZipInfoCallback)null);
        this.processAllEntries(zipEntryAdapter);
    }

    public void iterate(ZipInfoCallback callback) {
         ZipEntryOrInfoAdapter zipEntryAdapter = new  ZipEntryOrInfoAdapter(( ZipEntryCallback)null, callback);
        this.processAllEntries(zipEntryAdapter);
    }

    public byte[] getEntry(String name) {
        if(this.src == null) {
            throw new IllegalStateException("Source is not given");
        } else {
            return  ZipUtil.unpackEntry(this.src, name);
        }
    }

    public boolean containsEntry(String name) {
        if(this.src == null) {
            throw new IllegalStateException("Source is not given");
        } else {
            return  ZipUtil.containsEntry(this.src, name);
        }
    }

    private void iterateExistingExceptRemoved( ZipEntryOrInfoAdapter zipEntryCallback) {
        if(this.src != null) {
            Set removedDirs =  ZipUtil.filterDirEntries(this.src, this.removedEntries);
            ZipFile zf = null;

            try {
                zf = this.getZipFile();
                Enumeration e = zf.entries();

                while(true) {
                    ZipEntry entry;
                    while(true) {
                        String entryName;
                        do {
                            do {
                                if(!e.hasMoreElements()) {
                                    return;
                                }

                                entry = (ZipEntry)e.nextElement();
                                entryName = entry.getName();
                            } while(this.removedEntries.contains(entryName));
                        } while(this.isEntryInDir(removedDirs, entryName));

                        if(this.nameMapper == null) {
                            break;
                        }

                        String is = this.nameMapper.map(entry.getName());
                        if(is != null) {
                            if(!is.equals(entry.getName())) {
                                entry =  ZipEntryUtil.copy(entry, is);
                            }
                            break;
                        }
                    }

                    InputStream is1 = zf.getInputStream(entry);

                    try {
                        zipEntryCallback.process(is1, entry);
                    } catch ( ZipBreakException var19) {
                        break;
                    } finally {
                        IOUtils.closeQuietly(is1);
                    }
                }
            } catch (IOException var21) {
                 ZipExceptionUtil.rethrow(var21);
            } finally {
                 ZipUtil.closeQuietly(zf);
            }

        }
    }

    private void iterateChangedAndAdded( ZipEntryOrInfoAdapter zipEntryCallback) {
        Iterator var3 = this.changedEntries.iterator();

        while(var3.hasNext()) {
             ZipEntrySource entrySource = (ZipEntrySource)var3.next();

            try {
                ZipEntry e = entrySource.getEntry();
                if(this.nameMapper != null) {
                    String mappedName = this.nameMapper.map(e.getName());
                    if(mappedName == null) {
                        continue;
                    }

                    if(!mappedName.equals(e.getName())) {
                        e =  ZipEntryUtil.copy(e, mappedName);
                    }
                }

                zipEntryCallback.process(entrySource.getInputStream(), e);
            } catch (ZipBreakException var6) {
                break;
            } catch (IOException var7) {
                 ZipExceptionUtil.rethrow(var7);
            }
        }

    }

    private void handleInPlaceActions(File result) throws IOException {
        if(this.isInPlace()) {
            FileUtils.forceDelete(this.src);
            if(result.isFile()) {
                FileUtils.moveFile(result, this.src);
            } else {
                FileUtils.moveDirectory(result, this.src);
            }
        }

    }

    private boolean isEntryInDir(Set<String> dirNames, String entryName) {
        Iterator var4 = dirNames.iterator();

        while(var4.hasNext()) {
            String dirName = (String)var4.next();
            if(entryName.startsWith(dirName)) {
                return true;
            }
        }

        return false;
    }

    private ZipFile getZipFile() throws IOException {
        return  ZipFileUtil.getZipFile(this.src, this.charset);
    }

    private static class CopyingCallback implements  ZipEntryCallback {
        private final Map<String, ZipEntryTransformer> entryByPath;
        private final ZipOutputStream out;
        private final Set<String> visitedNames;
        private final boolean preserveTimestapms;

        private CopyingCallback(List<ZipEntryTransformerEntry> transformerEntries, ZipOutputStream out, boolean preserveTimestapms) {
            this.out = out;
            this.preserveTimestapms = preserveTimestapms;
            this.entryByPath =  ZipUtil.transformersByPath(transformerEntries);
            this.visitedNames = new HashSet();
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            String entryName = zipEntry.getName();
            if(!this.visitedNames.contains(entryName)) {
                this.visitedNames.add(entryName);
                ZipEntryTransformer transformer = (ZipEntryTransformer)this.entryByPath.remove(entryName);
                if(transformer == null) {
                     ZipEntryUtil.copyEntry(zipEntry, in, this.out, this.preserveTimestapms);
                } else {
                    transformer.transform(in, zipEntry, this.out);
                }

            }
        }
    }

    private static class UnpackingCallback implements ZipEntryCallback {
        private final Map<String, ZipEntryTransformer> entryByPath;
        private final Set<String> visitedNames;
        private final File destination;

        private UnpackingCallback(List<ZipEntryTransformerEntry> entries, File destination) {
            this.destination = destination;
            this.entryByPath = ZipUtil.transformersByPath(entries);
            this.visitedNames = new HashSet();
        }

        public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            String entryName = zipEntry.getName();
            if(!this.visitedNames.contains(entryName)) {
                this.visitedNames.add(entryName);
                File file = new File(this.destination, entryName);
                if(zipEntry.isDirectory()) {
                    FileUtils.forceMkdir(file);
                } else {
                    FileUtils.forceMkdir(file.getParentFile());
                    file.createNewFile();
                    ZipEntryTransformer transformer = (ZipEntryTransformer)this.entryByPath.remove(entryName);
                    if(transformer == null) {
                        FileUtils.copy(in, file);
                    } else {
                        this.transformIntoFile(transformer, in, zipEntry, file);
                    }

                }
            }
        }

        private void transformIntoFile(final ZipEntryTransformer transformer, final InputStream entryIn, final ZipEntry zipEntry, File destination) throws IOException {
            PipedInputStream pipedIn = new PipedInputStream();
            PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);
            final ZipOutputStream zipOut = new ZipOutputStream(pipedOut);
            ZipInputStream zipIn = new ZipInputStream(pipedIn);
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);

            try {
                newFixedThreadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            transformer.transform(entryIn, zipEntry, zipOut);
                        } catch (IOException var2) {
                            ZipExceptionUtil.rethrow(var2);
                        }

                    }
                });
                zipIn.getNextEntry();
                FileUtils.copy(zipIn, destination);
            } finally {
                try {
                    zipIn.closeEntry();
                } catch (IOException var15) {
                    ;
                }

                newFixedThreadPool.shutdown();
                IOUtils.closeQuietly(pipedIn);
                IOUtils.closeQuietly(zipIn);
                IOUtils.closeQuietly(pipedOut);
                IOUtils.closeQuietly(zipOut);
            }

        }
    }
}
