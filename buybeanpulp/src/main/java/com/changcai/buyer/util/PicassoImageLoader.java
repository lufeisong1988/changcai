package com.changcai.buyer.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.changcai.buyer.R;
import com.squareup.picasso.Cache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by liuxingwei on 2016/11/7.
 * RGB_565_内存优化 -
 */
public class PicassoImageLoader implements imageLoader{
    private static PicassoImageLoader ourInstance = new PicassoImageLoader();

    public static PicassoImageLoader getInstance() {
        return ourInstance;
    }

    private Context context;
    private PicassoImageLoader() {
        this._mConfig = Bitmap.Config.RGB_565;
    }

    private Bitmap.Config _mConfig;


    private void setBitmapConfig(Bitmap.Config _mConfig){
        this._mConfig = _mConfig;
    }

    /**
     * @param activity
     * @param path
     * @param imageView
     * @param defaultDrawable
     * @param width
     * @param height
     */
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height) {


            Picasso.with(activity)
                    .load(new File(path))
                    .placeholder(defaultDrawable)
                    .error(defaultDrawable)
                    .config(_mConfig)
                    .resize(width, height)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);


    }

    /**
     * @param activity
     * @param path
     * @param imageView
     * @param defaultDrawable
     * @param width
     * @param height
     */
    @Override
    public void displayNetImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load( path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(_mConfig)
                .resize(width, height)
                .centerInside()
                .into(imageView);
    }

    /**
     * @param activity
     * @param resourceId
     * @param imageView
     * @param defaultDrawable
     * @param width
     * @param height
     */
    @Override
    public void displayResourceImage(Activity activity, int resourceId, ImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(resourceId)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(_mConfig)
                .resize(width, height)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);
    }


    @Override
    public void displayNetImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable) {
        Picasso.with(activity)
                .load(path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(_mConfig)
                .priority(Picasso.Priority.NORMAL)
                .into(imageView);
    }

    @Override
    public void displayNetImage(Activity activity, String path, ImageView imageView) {
        Picasso.with(activity)
                .load(path)
                .config(_mConfig)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .priority(Picasso.Priority.NORMAL)
                .into(imageView);
    }
    @Override
    public void displayResourceImageNoResize(Activity activity, int resourceId, ImageView imageView) {
        Picasso.with(activity)
                .load(resourceId).tag(resourceId)
                .config(_mConfig)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView) {
        Picasso.with(activity)
                .load(new File(path)).tag(path)
                .config(_mConfig)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageView);

    }

    @Override
    public void displayImageDimen(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .resizeDimen(width, height)
                .centerInside()
                .tag(path)
                .into(imageView);
    }


    @Override
    public void clearMemoryCache() {
    }
}
