package com.changcai.buyer.util;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/3.
 */

public class ArrayUtil {
    public static int getMax(List<String> arr) {
        int max = Integer.valueOf(arr.get(0));
        for (int i = 1; i < arr.size(); i++) {
            if (Integer.valueOf(arr.get(i)) > max) {
                max =Integer.valueOf( arr.get(i));
            }
        }
        return max;
    }
}
