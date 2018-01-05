package com.changcai.buyer.permission;

/**
 * Created by liuxingwei on 2016/11/26.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/24 下午11:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterPermissionGranted {

    int value();

}
