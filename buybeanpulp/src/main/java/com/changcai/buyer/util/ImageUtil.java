package com.changcai.buyer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片处理工具类
 * 
 * @author Zhoujun
 * 
 */
public class ImageUtil {
	private static final String TAG = "ImageUtil.Util";

	/**
	 * drawable 转换成 bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}

	/**
	 * 图片加上圆角效果
	 * 
	 * @param drawable
	 *            需要处理的图片
	 * @param percent
	 *            圆角比例大小
	 * @return
	 */
	public static Bitmap getRoundCornerBitmapWithPic(Drawable drawable, float percent) {
		Bitmap bitmap = drawableToBitmap(drawable);
		return getRoundedCornerBitmapWithPic(bitmap, percent);
	}

	/**
	 * 图片加上圆角效果
	 * 
	 * @param bitmap
	 *            要处理的位图
	 * @param percent
	 *            圆角大小
	 * @return 返回处理后的位图
	 */
	public static Bitmap getRoundedCornerBitmapWithPic(Bitmap bitmap, float percent) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, bitmap.getWidth() * percent, bitmap.getHeight() * percent, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得某个图片资源的BitMap对象
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap getBitMapByRes(Context context, int drawableId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), drawableId, opts);
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, opts);
		return bitmap;
	}

	/**
	 * 从文件中获取bitmap对象
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByFile(Context context, String filePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
		;
		return bitmap;
	}

	/******************************** 微信调用 *******************************************/
	/**
	 * 将图片转转为字节流 微信调用，缩略图大小不能超过32K
	 * 
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] weiXinBmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);
		int options = 100;
		while (output.toByteArray().length / 1024 >= 32) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			output.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			bmp.compress(CompressFormat.JPEG, options, output);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 压缩图片
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path, int sizeWidth, int sizeHeight)
			throws IOException {
		// 取得图片
		InputStream temp = new FileInputStream(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= sizeWidth) && (options.outHeight >> i <= sizeHeight)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new FileInputStream(path);

				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(4.0D, i);

				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	/**
	 * 头像文件名
	 * 
	 * @param sid
	 * @return
	 */
	public static String createAvatarFileName(String sid) {
		return "avatar_" + sid + ".jpg";
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		return dateFormat.format(date) + ".jpg";
	}

	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

	/**
	 * 提取缩略图
	 * 
	 * @param path
	 * @param height
	 * @param width
	 * @param crop
	 * @return
	 */
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x"
					+ options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1,
						(bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}

	/**
	 * 对bitmap进行压缩图片
	 * 
	 * @param image
	 * @return
	 *
	 * height
	 */
	public static Bitmap compressImage(Bitmap image, int width, int height) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);
		int i = 0;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= width) && (options.outHeight >> i <= height)) {
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeByteArray(b, 0, b.length, options);
			}
			i += 1;
		}
	}

	/**
	 * 压缩背景图片，便于毛玻璃
	 * 
	 * @return
	 */
	public static Bitmap compressBitmap(String path) {
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bgBitmap = BitmapFactory.decodeFile(path, options);
		int realWidth = options.outWidth;
		int realHeight = options.outHeight;
		int i = 0;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= 600) && (options.outHeight >> i <= 600)) {
				if (i == 0) {
					i = 1;
				}
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;
				bgBitmap = BitmapFactory.decodeFile(path, options);
				Bitmap newBitmap = Bitmap.createScaledBitmap(bgBitmap, realWidth, realHeight, true);
				if(bgBitmap != null && !bgBitmap.isRecycled()){
					bgBitmap.recycle();
					bgBitmap = null;
				}
				return newBitmap;
			}
			i += 1;
		}
	}

	/**
	 * 压缩图片（分享图片）
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap compressBitmapShare(String path) {
		//bitmap 通过compress方法，可以压缩字节，但是再重新存到文件里，还是没变化
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Bitmap bitmap = BitmapFactory.decodeFile(path, calculateOptions(path));
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//		int length = out.toByteArray().length/1024;
//		out.reset();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
//		int length2 = out.toByteArray().length/1024;
//		try {
//			FileOutputStream fileout = new FileOutputStream(Constants.IMAGE_DIR + "temp.jpg");
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileout);
//		} catch (FileNotFoundException e) {
//		}
		return BitmapFactory.decodeFile(path, calculateOptions(path));
	}
	/**
	 * 压缩图片传给微博
	 * @param path
	 * @return
	 */
	public static byte[] compressBitmapShareToByte(String path){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bitmap bitmap = BitmapFactory.decodeFile(path, calculateOptions(path));
		bitmap.compress(CompressFormat.JPEG, 70, out);
		return out.toByteArray();
	}
	private static int MAX_WIDTH = 768;
	private static int MAX_HEIGHT = 1024;
	/**
	 * 计算压缩比例
	 * @param path(最大宽度为768 最大高度为1024)
	 * @return
	 */
	public static BitmapFactory.Options calculateOptions(String path) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, localOptions);
		int i1 = (int) Math.ceil(localOptions.outWidth / MAX_WIDTH);
		int i2 = (int) Math.ceil(localOptions.outHeight / MAX_HEIGHT);

		if ((i2 > 1) && (i1 > 1)) {
			if (i2 > i1) {
				localOptions.inSampleSize = i2;
			} else {
				localOptions.inSampleSize = i1;
			}
		} else if (i2 > 2) {
			localOptions.inSampleSize = i2;
		} else if (i1 > 2) {
			localOptions.inSampleSize = i1;
		}

		localOptions.inJustDecodeBounds = false;
		Log.d("", "### 图片缩放比例 : " + localOptions.inSampleSize);
		return localOptions;
	}
	/**
	 * 矩形生成圆角图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap makeRoundCorner(Bitmap bitmap)
	  {
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int left = 0, top = 0, right = width, bottom = height;
	    float roundPx = height/2;
	    if (width > height) {
	      left = (width - height)/2;
	      top = 0;
	      right = left + height;
	      bottom = height;
	    } else if (height > width) {
	      left = 0;
	      top = (height - width)/2;
	      right = width;
	      bottom = top + width;
	      roundPx = width/2;
	    }

	    Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    int color = 0xff424242;
	    Paint paint = new Paint();
	    Rect rect = new Rect(left, top, right, bottom);
	    RectF rectF = new RectF(rect);

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    return output;
	  }



	/**
	 * 压缩图片，并对图片角度旋转
	 * @param filePath
	 * @param outFilePath
	 * @return
	 */
	public static String compressImage(String filePath,String outFilePath){
		return compressImage(filePath,outFilePath,480,800);
	}

//	/**
//	 * 压缩图片，并对图片角度旋转
//	 * @param filePath
//	 * @param outFilePath
//	 * @return
//	 */
//	public static String compressImage(String filePath,String outFilePath ,int width,int height){
//		return compressImage(filePath,outFilePath,width,height);
//	}

	/**
	 * 压缩图片
	 * @param filePath
	 * @param outFilePath
	 * @return
	 */
	public static String compressImage(String filePath,String outFilePath,int width,int height)  {
		if(TextUtils.isEmpty(filePath)){
			return "";
		}
		Bitmap bm = getSmallBitmap(filePath,width,height);

		int degree = readPictureDegree(filePath);

		if(degree!=0){//旋转照片角度
			bm=rotateBitmap(bm,degree);
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			//压缩到60K
			bm.compress(CompressFormat.JPEG, compressBitmap(bm, 1024), out);
		}catch (Exception e){
			try {
				if(out != null)
					out.close();
			} catch (IOException e1) {

			}
			return "";
		}
		bm.recycle();
		try {
			if(out != null)
				out.close();
		} catch (IOException e1) {

		}
		return outFilePath;
	}
	/**
	 * 计算压缩尺寸比例
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 获取小图
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);

	}
	/**
	 *读取照片角度
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转照片
	 * @param bitmap
	 * @param degress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	/**
	 * 图片质量压缩
	 * @param bitmap
	 * @param size
	 * @return
	 */
	public static int  compressBitmap(Bitmap bitmap,float size){
		int quality=100;
		if(bitmap==null||getSizeOfBitmap(bitmap)<=size){
			return quality;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
		while (baos.toByteArray().length / 1024f>size) {
			quality=quality-4;// 每次都减少4
			baos.reset();// 重置baos即清空baos
			if(quality<=0){
				break;
			}
			bitmap.compress(CompressFormat.JPEG, quality, baos);
		}
		try {
			baos.close();
		} catch (IOException e) {

		}
		return quality;
	}

	/**
	 * 获取bitmap大小，单位kb
	 * @param bitmap
	 */
	public static long getSizeOfBitmap(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);//这里100的话表示不压缩质量
		int length = baos.toByteArray().length/1024;//读出图片的kb大小
		try {
			baos.close();
		} catch (IOException e) {

		}
		return length;
	}




	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
}
