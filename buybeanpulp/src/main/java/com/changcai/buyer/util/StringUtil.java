package com.changcai.buyer.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * 字符串工具类
 *
 * @author Zhoujun
 *         说明：处理一下字符串的常用操作，字符串校验等
 */
public class StringUtil {


    /**
     * 验证企业代码是否正确
     * @param code 企业组织机构代码
     * @return
     */
    public static final boolean isValidEntpCode(String code) {
        int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String regex = "^([0-9A-Z]){8}-[0-9|X]$";

        if (!code.matches(regex)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
        }

        int c9 = 11 - (sum % 11);

        String sc9 = String.valueOf(c9);
        if (11 == c9) {
            sc9 = "0";
        } else if (10 == c9) {
            sc9 = "X";
        }
        return sc9.equals(String.valueOf(code.charAt(9)));
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        // String strPattern =
        // "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 验证是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern.compile("^[1][34578][0-9]{9}$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户名必须为6-20位的小写字母和数字
     *
     * @param nameStr
     * @return
     */
    public static boolean checkName(String nameStr) {
        String check = "[a-z0-9A-Z]{6,20}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(nameStr);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 统一谁会信用代码
     * @param code
     * @return
     */
    public static boolean uniformCode(String code){
        String check = "[0-9A-Z]{18}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(code);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
    /**
     * 检验密码不能为纯数字
     *
     * @param password
     * @return
     */
    public static boolean checkPasswordNum(String password) {
        String check = "[0-9]{6,16}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(password);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 密码是6-16位英文字母和数字
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        String check = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(password);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 检测车架号(现在车架号只判断17位)
     *
     * @param vin
     * @return
     */
    public static boolean checkVin(String vin) {
        if (TextUtils.isEmpty(vin)) {
            return false;
        }
        if (vin.length() >= 17) {
            return true;
        } else {
            return false;
        }
//		String check = "L[a-z0-9A-Z]{16}";
//		Pattern regex = Pattern.compile(check);
//		Matcher matcher = regex.matcher(vin);
//		boolean isMatched = matcher.matches();
//		return isMatched;
    }

    /**
     * 检查发动机号
     *
     * @param engineNum
     * @return
     */
    public static boolean checkEngineNum(String engineNum) {
        String check = "[0-9]{9}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(engineNum);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    public static boolean checkSMSCode(String codeNum){
        String check = "[0-9]{6}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(codeNum);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
    /**
     * 检测车牌号
     *
     * @param remark
     * @return
     */
    public static boolean checkRemark(String remark) {
        String check = "[京|津|沪|渝|冀|豫|云|辽|黑|湘|皖|鲁|新|苏|浙|赣|鄂|桂|甘|晋|蒙 |陕|吉|闽|贵|粤|青|藏|川|宁|琼][A-Z]{1}[a-z0-9A-Z]{5}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(remark);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 检测16终端设备号(现在只判断30位)
     *
     * @param tuSerialNumber
     * @return
     */
    public static boolean checkTuSerialNumber(String tuSerialNumber) {
        if (TextUtils.isEmpty(tuSerialNumber)) {
            return true;
        }
        if (tuSerialNumber.length() > 30) {
            return false;
        } else {
            return true;
        }
//		String check = "[a-z0-9A-Z]{16}|[a-z0-9A-Z]{18}";
//		Pattern regex = Pattern.compile(check);
//		Matcher matcher = regex.matcher(tuSerialNumber);
//		boolean isMatched = matcher.matches();
//		return isMatched;
    }

    /**
     * 检测设备终端服务号码
     *
     * @param serviceNum
     * @return
     */
    public static boolean checkServiceNum(String serviceNum) {
        String check = "106[0-9]{10}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(serviceNum);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * MD5加密
     *
     * @param secret_key
     * @return
     */
    public static String MD5(String secret_key) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(secret_key.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 字符全角化
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * 将网络图片路径md5加密作为文件名
     *
     * @param imageUrl
     * @return
     */
    public static String createImageName(String imageUrl) {
        return MD5(imageUrl) + ".jpg";
    }

    /**
     * 将网络图片路径md5加密作为文件名,可以设置图片类型
     *
     * @param imageUrl
     * @param imgSuffix
     * @return
     */
    public static String createImageName(String imageUrl, String imgSuffix) {
        return MD5(imageUrl) + imgSuffix;
    }

    /**
     * 判断乱码为"�"这个特殊字符的情况
     *
     * @param str
     * @return
     */
    public static boolean isSpecialCharacter(String str) {
        // 是"�"这个特殊字符的乱码情况
        if (str.contains("ï¿½")) {
            return true;
        }
        return false;
    }

    /**
     * 将一位数前加上0
     *
     * @param month
     * @return
     */
    public static String getMonthLables(long month) {
        month = month + 1;
        if (month < 10) {
            return "0" + month;
        } else {
            return String.valueOf(month);
        }
    }

    /**
     * @param time
     * @return time format with 00:00:00
     */
    public static String longToString(long time) {
        if (time < 10) {
            return "0" + time;
        }
        return String.valueOf(time);
    }

    /**
     * 获取胎压
     *
     * @param pressure
     * @return
     */
    public static String getPressure(String pressure) {
        if (TextUtils.isEmpty(pressure) || pressure.equals("-1") || pressure.equals("-1.0")) {
            return "--";
        }
        return pressure;
    }

    /**
     * 格式化金额显示，小数点前面数字每隔3位添加“,”号
     */
    public static String formatForMoney(String value) {
        if (value == null) {
            return "0";
        }
        String str = "0";
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            value = value.replaceAll(",", "");
            BigDecimal bigDecimal = new BigDecimal(value);
            str = decimalFormat.format(bigDecimal);
        } catch (Exception e) {
            str = "0.00";
        }

        return str;
    }


    /**
     * 校验名字
     *
     * @param name
     * @return
     */
    public static boolean ChineseName(String name) {
        if (!name.matches("[\u4e00-\u9fa5]{2,4}")) {
            return false;
        } else return true;
    }

    /**
     * 替换指定的子串，替换所有出现的子串。
     *
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>，则返回原字符串。
     * <pre>
     * StringUtil.replace(null, *, *)        = null
     * StringUtil.replace("", *, *)          = ""
     * StringUtil.replace("aba", null, null) = "aba"
     * StringUtil.replace("aba", null, null) = "aba"
     * StringUtil.replace("aba", "a", null)  = "aba"
     * StringUtil.replace("aba", "a", "")    = "b"
     * StringUtil.replace("aba", "a", "z")   = "zbz"
     * </pre>
     * </p>
     *
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     *
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, String repl, String with, int max) {
        if ((text == null) || (repl == null) || (with == null) || (repl.length() == 0)
                || (max == 0)) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        int end = 0;

        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }

        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String formatForMoneyNoDot(String value) {
        if (value == null) {
            return "0";
        }
        String str = "0";
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
            value = value.replaceAll(",", "");
            BigDecimal bigDecimal = new BigDecimal(value);
            str = decimalFormat.format(bigDecimal);
        } catch (Exception e) {
            str = "0.00";
        }

        return str;
    }
}
