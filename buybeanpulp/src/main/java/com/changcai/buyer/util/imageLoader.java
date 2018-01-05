package com.changcai.buyer.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/11/7.
 */

public interface imageLoader extends Serializable {

    void displayImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height);

    void clearMemoryCache();

    void displayNetImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height);

    void displayResourceImage(Activity activity, int resourceId, ImageView imageView, Drawable defaultDrawable, int width, int height);

    void displayNetImage(Activity activity, String path, ImageView imageView, Drawable defaultDrawable);

    void displayResourceImageNoResize(Activity activity, int resourceId, ImageView imageView);


    void displayImage(Activity activity, String path, ImageView imageView);

    void displayImageDimen(Activity activity, String path, ImageView imageView, Drawable defaultDrawable, int width, int height);

     void displayNetImage(Activity activity, String path, ImageView imageView);
}
