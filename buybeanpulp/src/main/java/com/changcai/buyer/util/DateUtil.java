package com.changcai.buyer.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 *
 * @author Zhoujun
 *         说明：对日期格式的格式化与转换操作等一系列操作
 */
public class DateUtil {
    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static String getStringDateYYYYmm() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static String getStringDate(String form) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(form);
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static String getStringDate(long time,String form) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(form);
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 时间戳转化为 yyyy-MM-dd
     * 服务器传回的时间戳末尾被截断3位，
     * 自动补零3位
     *
     * @param time
     * @return
     */
    public static String parserTimeToMMddHHmm(String time) {
        if(time == null || time.isEmpty()){
            return "0";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

        String sd = sdf.format(new Date(Long.parseLong(time)));
        return sd;
    }


    /**
     * 字符串转换成日期  yyyy-MM-dd HH:mm:ss
     *
     * @param strFormat 格式定义 如：yyyy-MM-dd HH:mm:ss
     * @param dateValue 日期对象
     * @return
     */
    public static Date stringToDate(String strFormat, String dateValue) {
        if (dateValue == null)
            return null;
        if (strFormat == null)
            strFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date newDate = null;
        try {
            newDate = dateFormat.parse(dateValue.trim());
        } catch (ParseException pe) {
            newDate = null;
        }
        return newDate;
    }


    /**
     * 字符串转换成日期  yyyy-MM-dd HH:mm:ss
     *
     * @param dateValue 日期对象
     * @return
     */
    public static Date stringToDate(String dateValue) {
        if (dateValue == null)
            return null;
        if (dateValue.length() < 13) {
            dateValue = dateValue + "000";
        }
        Date newDate = null;
        try {
            newDate = new Date(dateValue.trim());
        } catch (Exception pe) {
            newDate = null;
        }
        return newDate;
    }

    /**
     * 获取检测时间
     *
     * @param time
     * @return
     */
    public static String getFormatCheckTime(String time) {
        if (getFormatTimeMillis("yyyy-MM-dd", time).equals(dateToString("yyyy-MM-dd", new Date()))) {
            return "今天  " + getFormatTimeMillis("HH:mm:ss", time);
        } else if (getFormatTimeMillis("yyyy", time).equals(dateToString("yyyy", new Date()))) {
            return getFormatTimeMillis("MM-dd HH:mm:ss", time);
        } else {
            return getFormatTimeMillis("yyyy-MM-dd HH:mm:ss", time);
        }
    }

    /**
     * 将时间秒数或者毫秒数格式
     *
     * @param strFormat
     * @param time      时间秒数或者毫秒数
     * @return
     */
    public static String getFormatTimeMillis(String strFormat, String time) {
        if (TextUtils.isEmpty(time) || time.equals("0")) {
            return "";
        }
        if (time.length() < 13) {

            time = time + "000";
        }
        String defaultTime;
        try {
            long endDate = Long.parseLong(time);
            Date date = new Date(endDate);
            defaultTime = dateToString(strFormat, date);
        } catch (NumberFormatException e) {
            return "";
        }
        return defaultTime;
    }

    /**
     * 日期转成字符串
     *
     * @param strFormat 格式定义 如：yyyy-MM-dd HH:mm:ss
     * @param date      日期字符串
     * @return
     */
    public static String dateToString(String strFormat, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        return dateFormat.format(date);
    }


    /**
     * 计算两个日期间隔天数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int countDays(Date begin, Date end) {
        int days = 0;
        Calendar c_b = Calendar.getInstance();
        Calendar c_e = Calendar.getInstance();
        c_b.setTime(begin);
        c_e.setTime(end);
        while (c_b.before(c_e)) {
            days++;
            c_b.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    public static int compareDate(Date date1, Date date2) {
        Calendar c_b = Calendar.getInstance();
        Calendar c_e = Calendar.getInstance();
        c_b.setTime(date1);
        c_e.setTime(date2);
        return c_b.compareTo(c_e);
    }

    //获得两个时间的毫秒时间差异
    public static long[] diffDate(long diff, String format) {
        long[] timer = new long[4];
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
        StringBuffer diffString = new StringBuffer();
        if (day > 0) {
            diffString.append(day + "天");
        }
        if (hour > 0) {
            diffString.append(hour + "时");
        }
        if (min > 0) {
            diffString.append(min + "分");
        }
        if (sec > 0) {
            diffString.append(sec + "秒");
        }
        timer[0] = day;
        timer[1] = hour;
        timer[2] = min;
        timer[3] = sec;
        return timer;
    }

    public static String dateDiff(String startTime, String endTime, String format) {
//按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
//获得两个时间的毫秒时间差异
        long diff = 0;
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            if (diff < 0)
                diff = 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
        StringBuffer diffString = new StringBuffer();
        if (day > 0) {
            diffString.append(day + "天");
        }
        if (hour > 0) {
            diffString.append(hour + "小时");
        }
        if (min > 0) {
            diffString.append(min + "分钟");
        }
        if (sec > 0) {
            diffString.append(sec + "秒");
        }
        return diffString.toString();
    }

    /**
     * 获取最近的时间
     *
     * @param str1
     * @return
     */
    public static String getNearTime(String str1) {
        String result = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = new Date(str1);
            df.format(one);
            two = new Date();
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println(day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
            long m = day * 24 * 60 + hour * 60 + min;

            if (m <= 1) {
                result = "刚刚";
            } else if (m > 1 && m <= 5) {
                result = "刚刚";
            } else if (m > 5 && m <= 30) {
                result = "半小时前";
            } else if (m > 30 && m <= 60) {
                result = "一小时前";
            } else if (m > 60 && m <= 60 * 2) {
                result = "两小时前";
            } else if (m > 60 * 2 && m <= 60 * 24) {
                result = "一天前";
            } else if (m > 60 * 24 && m <= 60 * 24 * 2) {
                result = "两天前";
            } else if (m > 60 * 24 * 2 && m <= 60 * 24 * 7) {
                result = "一星期前";
            } else if (m > 60 * 24 * 7 && m <= 60 * 24 * 30) {
                result = "一个月前";
            } else if (m > 60 * 24 * 30 && m <= 60 * 24 * 30 * 6) {
                result = "六个月前";
            } else if (m > 60 * 24 * 30 * 6) {
                result = one.toString();
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("hj", "ParseException");
        }
        return result;
    }

    /**
     * 获取最近的时间
     *
     * @param str1  2017-03-11
     * @return
     */
    public static String getNearTime2(String str1) {
        String result = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = new Date(str1);
            df.format(one);
            two = new Date();
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println(day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
            long m = day * 24 * 60 + hour * 60 + min;

            if (m <= 1) {
                result = "刚刚";
            } else if (m > 1 && m <= 5) {
                result = "刚刚";
            } else if (m > 5 && m <= 30) {
                result = "半小时前";
            } else if (m > 30 && m <= 60) {
                result = "一小时前";
            } else if (m > 60 && m <= 60 * 2) {
                result = "两小时前";
            } else if (m > 60 * 2 && m <= 60 * 24) {
                result = "一天前";
            } else if (m > 60 * 24 && m <= 60 * 24 * 2) {
                result = "两天前";
            } else if (m > 60 * 24 * 2 && m <= 60 * 24 * 7) {
                result = "一星期前";
            } else if (m > 60 * 24 * 7 && m <= 60 * 24 * 30) {
                result = "一个月前";
            } else if (m > 60 * 24 * 30 && m <= 60 * 24 * 30 * 6) {
                result = "六个月前";
            } else if (m > 60 * 24 * 30 * 6) {
                result = one.toString();
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("hj", "ParseException");
        }
        return result;
    }
    /**
     * 日期格式化
     *
     * @param time
     * @return
     */
    public static String formateDate2Week(String time) {
        final String[] number = new String[]{"第一周", "第二周", "第三周", "第四周", "第五周", "第六周", "第七周"};
        String str1 = getFormatTimeMillis("yyyy年MM月 ", time);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(time));
        int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
        Log.d("weekOfMonth", weekOfMonth + "");
        return str1 + number[weekOfMonth - 1];
    }

    /**
     * 是否是今年的周
     *
     * @param index
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static boolean isWeekCurrentYear(int index) {
        Calendar cal = Calendar.getInstance();
        //获取当前是一年的哪一天
        cal.add(Calendar.WEEK_OF_YEAR, index);
        return cal.get(Calendar.WEEK_OF_YEAR) == 1;
    }

    /**
     * 获取统计的是哪个月
     *
     * @param index
     * @return
     */
    public static String getTongjiMonth(int index) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, index);
        return dateToString("yyyy年MM月", cal.getTime());
    }

    /**
     * 当前月份是否是本年的开始
     *
     * @param index
     * @return
     */
    @SuppressWarnings("WrongConstant")

    public static boolean isMonthCurrentYear(int index) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, index);
        return cal.get(Calendar.MONTH) == Calendar.JANUARY;
    }

    /**
     * 获取统计月份的起始日期
     *
     * @param index
     * @return
     */
    public static String getTongjiMonthStartEnd(int index) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, index);
        return dateToString("yyyy年MM月", cal.getTime()) + "1日~" + cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "日";
    }

    /**
     * 获取统计的哪年
     *
     * @param index
     * @return
     */
    public static String getTongjiYear(int index) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, index);
        int year = cal.get(Calendar.YEAR);
        return (year - 1) + "年~" + year + "年";
    }


    /**
     * unix时间戳转指定格式日期
     *
     * @param timestampString
     * @return
     */
    public static final String unixTime2Date(String timestampString) {
        if (TextUtils.isEmpty(timestampString)) {
            return "";
        }
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat("yyyy年MM月dd日").format(new java.util.Date(timestamp));
        return date;
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getDateAndWeekOfDate(String strFormat, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return dateFormat.format(date) + "  " + weekDays[w];
    }

    /**
     * 判断是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 201701转1
     * @param yyyyMM 201701
     * @return 1
     */
    public static String parseYYYYmmToMM(String yyyyMM){
        String mm = "01";
        if(yyyyMM.length() >=5 ){
            mm = yyyyMM.substring(4,yyyyMM.length());
        }else{
            return yyyyMM;
        }
        return String.valueOf(Integer.parseInt(mm));
    }
}
