package com.changcai.buyer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @description base64字符串转文件，或者（文件转base64字符串）
 * @author zhoujun
 * @date 2014年10月5日 下午4:31:03
 * @version 1.0
 */
public class Base64FileUtil {
	/**
	 * 图片转化成base64字符串
	 * 
	 * @param imgFile
	 *            文件路径
	 * @return
	 */
	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * base64字符串转化成图片
	 * 
	 * @param imgStr
	 *            base64字符串
	 * @param imgFilePath
	 *            图片路径
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			//防止服务器端png格式不标准，少了个字节
			if(b[1] == 'P' && b[2] == 'N' && b[3] == 'G'){
				if(b[b.length - 1] == (byte)0x42){
					b = Arrays.copyOf(b, b.length + 2);
					b[b.length - 2] = (byte)0x60;
					b[b.length - 1] = (byte)0x82;
				}
				if(b[b.length - 1] != (byte)0x82){
					b = Arrays.copyOf(b, b.length + 1);
					b[b.length - 1] = (byte)0x82;
				}
			}
			File file = new File(imgFilePath);
			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 将base64转为bitmap对象
	 * 
	 * @param imgStr
	 * @return
	 */
	public static Bitmap GenerateBitmap(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			//防止服务器端png格式不标准，少了个字节
			if(b[1] == 'P' && b[2] == 'N' && b[3] == 'G'){
				if(b[b.length - 1] == (byte)0x42){
					b = Arrays.copyOf(b, b.length + 2);
					b[b.length - 2] = (byte)0x60;
					b[b.length - 1] = (byte)0x82;
				}
				if(b[b.length - 1] != (byte)0x82){
					b = Arrays.copyOf(b, b.length + 1);
					b[b.length - 1] = (byte)0x82;
				}
			}
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} catch (Exception e) {
			return null;
		}
	}

}
