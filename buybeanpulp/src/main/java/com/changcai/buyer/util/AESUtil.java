package com.changcai.buyer.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * AES加密解密工具包
 *
 * @author Jun
 *
 */
@SuppressLint("NewApi")
public class AESUtil {
    public static String encrypt(String text, String key) {

        String result = "";
        if(TextUtils.isEmpty(text))
            return "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("utf-8");
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] results = cipher.doFinal(text.getBytes("utf-8"));
            if(results != null)
                result = Base64Util.encode(results);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String text, String key) {

        String result = "";
        if(TextUtils.isEmpty(text))
            return "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("utf-8");
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] results = cipher.doFinal(Base64Util.decode(text));
            if(results != null)
                result = new String(results, "utf-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}