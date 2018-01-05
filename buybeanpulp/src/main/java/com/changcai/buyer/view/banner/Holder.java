package com.changcai.buyer.view.banner;

/**
 * Created by Sai on 15/12/14.
 * @param <T> 任何你指定的对象
 *
 * imported by GONGGUODONG377@pingan.com.cn
 */

import android.content.Context;
import android.view.View;

public interface Holder<T>{
    View createView(Context context);
    void UpdateUI(Context context, int position, T data);
}