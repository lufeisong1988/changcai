package com.changcai.buyer.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.common.Constants;

import okhttp3.ResponseBody;

/**
 * 文件处理工具类
 *
 * @author Zhoujun
 */
public class FileUtil {

    private static String TAG = FileUtil.class.getSimpleName();

    /**
     * 读取assets下的文本数据
     *
     * @param fileName
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文本流文件
     *
     * @param is
     * @return
     */
    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 获得文件大小
     *
     * @param f
     * @throws IOException
     * @returns
     */
    public static long getFileSize(File f) throws IOException {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        }
        return s;
    }

    /**
     * 格式化文件大小
     *
     * @param f
     * @return
     * @throws IOException
     */
    public static String formatFileSize(File f) {// 转换文件大小
        String fileSizeString = "";
        try {
            long fileS = getFileSize(f);
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "b";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "kb";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "mb";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "gb";
            }
        } catch (Exception e) {
        }
        return fileSizeString;
    }

    /**
     * 拷贝资源文件到sd卡
     *
     * @param context
     * @param resId
     * @param databaseFilename 如数据库文件拷贝到sd卡中
     */
    public static void copyResToSdcard(Context context, int resId, String databaseFilename) {// name为sd卡下制定的路径
        try {
            // 不存在得到数据库输入流对象
            InputStream is = context.getResources().openRawResource(resId);
            // 创建输出流
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            // 将数据输出
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            // 关闭资源
            fos.close();
            is.close();
        } catch (Exception e) {
        }
    }

    /**
     * 保存到SD卡
     *
     * @param b
     */
    public static boolean savePic(Bitmap b) {
        FileOutputStream fos = null;
        File TEMP_DIR = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIR_NAME + "/");
        File tempFile = new File(TEMP_DIR, ".nomedia");
        if (!TEMP_DIR.exists()) {
            // 创建照片的存储目录
            TEMP_DIR.mkdirs();
        }
        try {
            if (!tempFile.exists())
                tempFile.createNewFile();
            File file = new File(TEMP_DIR, StringUtil.createImageName(Constants.SCREEN_CAPTURE));
            fos = new FileOutputStream(file, false);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.flush();
                fos.close();
            }
            if (b != null && !b.isRecycled()) {
                b.recycle();
                b = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取截屏路径
     *
     * @return
     */
    public static String getScreenCapturePath() {
        return Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIR_NAME + "/"
                + StringUtil.createImageName(Constants.SCREEN_CAPTURE);
    }


    /**
     * 计算图片压缩比
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,

                                        int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength,

                maxNumOfPixels);

        int roundedSize;

        if (initialSize <= 8) {

            roundedSize = 1;

            while (roundedSize < initialSize) {

                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap, int quality) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 保存文件
     *
     * @param content
     */
    public static void saveStringToFile(String content, String fileName) {
        String file = Constants.FILE_DIR + fileName;
        File fileDir = new File(Constants.FILE_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        FileOutputStream foutput = null;
        try {
            foutput = new FileOutputStream(file);
            foutput.write(content.getBytes("UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                foutput.flush();
                foutput.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存bitmap到文件中
     *
     * @param b
     * @param fileName
     * @return
     */
    public static boolean saveBitmap2File(Bitmap b, String fileName) {
        FileOutputStream fos = null;
        File imageDir = new File(Constants.IMAGE_DIR);
        File tempFile = new File(imageDir, ".nomedia");
        if (!imageDir.exists()) {
            // 创建照片的存储目录
            imageDir.mkdirs();
        }
        try {
            if (!tempFile.exists())
                tempFile.createNewFile();
            fos = new FileOutputStream(Constants.IMAGE_DIR + fileName, false);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

        String TAG = "delete_file";
        LogUtil.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    if (files.length != 0) {
                        for (int i = 0; i < files.length; i++) {
                            deleteFile(files[i]);
                        }
                    }
                }
            }
            file.delete();
        } else {
            LogUtil.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }


    public static void unzip(String zipFile, String location) throws IOException {
        try {
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(path, false);
                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }


}
