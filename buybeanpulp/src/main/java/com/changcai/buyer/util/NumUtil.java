package com.changcai.buyer.util;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * @description 数字格式化
 * @author zhoujun 
 * @date 2014年8月19日 下午2:53:39
 * @version 1.1
 */
public class NumUtil {
	private static final String defaultFormat = "#####0.0";

	private static  final  String moneyWithComma = "#,##0.00";


	public static String moneyWithComma(double money){

		// DecimalFormat的用法 #,###是三位分割一下
		// .00是小数点后保留两位 四舍五入
		DecimalFormat df = new DecimalFormat(moneyWithComma);
		String m = df.format(money);
		System.out.println(m);
		return m;
	}
	/**
	 * 格式化数字
	 * @param num
	 * @return
	 */
	public static String formatNum(String num){
		return formatNum(num, null);
	}

	/**
	 * 格式化数字
	 * @param num
	 * @return
	 */
	public static String formatNum(double num){
		return formatNum(num, null);
	}

	/**
	 * 格式化数字
	 * @param num
	 * @param formate
	 * @return
	 */
	public static String formatNum(double num, String formate){
		DecimalFormat df;
		if(TextUtils.isEmpty(formate)){
			df = new DecimalFormat(defaultFormat);
		}else{
			df = new DecimalFormat(formate);
		}
		try {
			String parseNum = df.format(num);
			return parseNum;
		} catch (NumberFormatException e) {
			return "0";
		}
	}

	/**
	 * 格式化数字
	 * @param num
	 * @param formate
	 * @return
	 */
	public static String formatNum(String num, String formate){
		DecimalFormat df;
		if(TextUtils.isEmpty(formate)){
			df = new DecimalFormat(defaultFormat);
		}else{
			df = new DecimalFormat(formate);
		}
		if(TextUtils.isEmpty(num)){
			return "0";
		}
		try {
			num = df.format(Float.parseFloat(num));
		} catch (NumberFormatException e) {
			return "0";
		}
		return num;
	}

	/**
	 * 银行卡号 加空格
	 * @param res
	 * @return
     */
	public static String bankFormat(String res){
		return  res.replaceAll("\\d{4}(?!$)", "$0 ");
	}
	/**
	 * 格式化字符串，保留一位小数（通过String的方法）
	 * @param num
	 * @return
	 */
	public static  String formatNumString(double num){
		return String.format("%.2f",num);
	}
}
