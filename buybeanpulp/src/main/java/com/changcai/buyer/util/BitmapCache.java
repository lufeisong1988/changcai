package com.changcai.buyer.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
/**
 * bitmap缓存
 * @author zhoujun
 *
 */
public class BitmapCache extends Activity {

	public Handler handler = new Handler();
	public final String TAG = getClass().getSimpleName();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}

	public void displayBmp(final ImageView iv, final String key, final String base64Str) {
		if (TextUtils.isEmpty(base64Str)) {
			Log.e(TAG, "no paths pass in");
			return;
		}

		if (imageCache.containsKey(key)) {
			SoftReference<Bitmap> reference = imageCache.get(key);
			Bitmap bmp = reference.get();
			if (bmp != null) {
				iv.setImageBitmap(bmp);
				Log.d(TAG, "hit cache");
				return;
			}
		}
		iv.setImageBitmap(null);

		new Thread() {
			Bitmap thumb;

			public void run() {
				try {
					thumb = Base64FileUtil.GenerateBitmap(base64Str);
				} catch (Exception e) {
					Log.d(TAG, e.getMessage());
				}
				put(key, thumb);
				handler.post(new Runnable() {

					@Override
					public void run() {
						iv.setImageBitmap(thumb);
					}
				});

			}
		}.start();

	}

	public Bitmap revitionImageSize(String base64Str) {
		byte[] bytes = Base64.decode(base64Str, Base64.DEFAULT);
		InputStream input = new ByteArrayInputStream(bytes);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, options);
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256) && (options.outHeight >> i <= 256)) {
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(input, null, options);
				break;
			}
			i += 1;
		}
		try {
			input.close();
		} catch (IOException e) {
			
		}
		return bitmap;
	}

	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
	}
}
