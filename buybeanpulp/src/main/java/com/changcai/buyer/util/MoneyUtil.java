/**
 * ChangCai.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.changcai.buyer.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金钱工具类
 *
 * @author LiuYi
 * @version $Id: PriceUtil.java, v 0.1 2016年3月22日 上午11:21:13 LiuYi Exp $
 */
public class MoneyUtil {

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    public static final String rechargeAmount = "10000.00";

    /**
     * 将元为单位的转换为分
     * 替换小数点
     *
     * @param amount
     * @return
     */
    public static Long convertYuanToFen(String amount) {
        Long amLong = 0l;
        if (TextUtils.isEmpty(amount)) {
            amount = "";
        }
        int index = amount.indexOf(".");
        int length = amount.length();

        if (index == -1) {
            amLong = Long.valueOf(amount + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((amount.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((amount.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((amount.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong;
    }

    /**
     * 分转换为元.  用来显示的逗号隔开的
     *
     * @param fen 分
     * @return 元
     */
    public static String convertFenToYuan(final String fen) {
        String yuan = "0.00";
        StringBuffer result = new StringBuffer();
        String transFen = fen;
        boolean isMinus = fen.startsWith("-");
        if (isMinus) {
            transFen = fen.substring(1, fen.length());
        }
        final int MULTIPLIER = 100;
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
        Matcher matcher = pattern.matcher(transFen);
        if (matcher.matches()) {
            yuan = new BigDecimal(transFen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();

            String intString = yuan.substring(0, yuan.length() - 3);
            for (int i = 1; i <= intString.length(); i++) {
                if ((i - 1) % 3 == 0 && i != 1) {
                    result.append(",");
                }
                result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
            }
            result.reverse().append(".").append(yuan.substring(yuan.length() - 2));
            yuan = result.toString();

            if (isMinus) {
                yuan = "-" + yuan;
            }
        } else {
            yuan = "0";
        }
        //元不要带小数点
        yuan = StringUtil.replace(yuan, ".00", "");
        return yuan;
    }

    /**
     * 分转换为元.  没有逗号隔开，用于计算
     *
     * @param fen 分
     * @return 元
     */
    public static String convertToYuan(final String fen) {
        String yuan = "0.00";
        StringBuffer result = new StringBuffer();
        String transFen = fen;
        boolean isMinus = fen.startsWith("-");
        if (isMinus) {
            transFen = fen.substring(1, fen.length());
        }
        final int MULTIPLIER = 100;
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
        Matcher matcher = pattern.matcher(transFen);
        if (matcher.matches()) {
            yuan = new BigDecimal(transFen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
//            result.reverse().append(".").append(yuan.substring(yuan.length() - 2));
//            yuan = result.toString();

            if (isMinus) {
                yuan = "-" + yuan;
            }
        } else {
            yuan = "0";
        }
        //元不要带小数点
//        yuan = StringUtil.replace(yuan, ".00", "");
        return yuan;
    }

    /**
     * 分向上取整,返回值没有角和分的值
     * 例如：210 返回 300
     *
     * @param amount
     * @return
     */
    public static Long fenCeil(String amount) {
        String yuan = BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
        Double yuanDouble = Double.valueOf(yuan);
        yuanDouble = NumberUtil.ceil(yuanDouble);
        return convertYuanToFen(String.valueOf(yuanDouble));
    }

    /**
     * 字符型的金额转换成BigDecimal类型的金额类型.
     *
     * @param amount
     * @return
     */
    public static BigDecimal toBigDecimalByString(String amount) {
        BigDecimal bd = new BigDecimal(amount);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    /**
     * ratio = 500
     * ratio =1000
     * ratio = 800
     *
     * @param ratio
     * @return
     */
    public static int ratio(@NonNull String ratio) {
        Double rate = Double.parseDouble(ratio);

        BigDecimal bigDecimalBuyer;
        if (rate.compareTo(0.0) == 0) {
            bigDecimalBuyer = new BigDecimal(0);
        } else {
//            bigDecimalBuyer = new BigDecimal(100).divide(new BigDecimal("10000.0").divide(new BigDecimal(ratio), 2, BigDecimal.ROUND_HALF_UP), 2, BigDecimal.ROUND_HALF_UP);
            bigDecimalBuyer = new BigDecimal(ratio).multiply(new BigDecimal("0.0001")).multiply(new BigDecimal("100"));
        }
        return bigDecimalBuyer.intValue();
    }

    public static BigDecimal insuranceValue(long totalValue, BigDecimal bigDecimalRatio) {
        BigDecimal insuranceValue;
        if (bigDecimalRatio.compareTo(new BigDecimal(0)) == 0) {
            insuranceValue = new BigDecimal("0.00");
        } else {
            insuranceValue = new BigDecimal(totalValue).divide(bigDecimalRatio, 2, BigDecimal.ROUND_HALF_UP);
        }

        return insuranceValue;
    }

    public static String matchMoneyFromString(String errorDesc) {
        char[] b = errorDesc.toCharArray();
        String result = "";
        for (int i = 0; i < b.length; i++) {
            if (("0123456789.").indexOf(b[i] + "") != -1) {
                result += b[i];
            }
        }
        return result;
    }
}
