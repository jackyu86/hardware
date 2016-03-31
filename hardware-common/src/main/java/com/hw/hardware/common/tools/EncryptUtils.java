package com.hw.hardware.common.tools;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hw.hardware.common.Constants;

/**
 * 加密与安全工具类
 * @author cfish
 * @since 2013-09-09
 */
public class EncryptUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);
	
	public static String encodeURI(String str){
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}
	
	public static String decodeURI(String str){
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}
	
	/**
	 * MD5加密算法(16位)
	 * @param str
	 * @return
	 */
	public static String md5Encode16(String str) {
		String temp = md5Encode(str);
		if (temp != null) {
			return temp.substring(8, 24);// 16位的加密
		}
		return temp;
	}

	/**
	 * MD5加密算法(32位)
	 * @param str
	 * @return
	 */
	public static String md5Encode(String str) {
		MessageDigest md = null;
		String dstr = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();  
			StringBuffer buf = new StringBuffer("");  
			for (int i : b) {  
				if (i < 0) i += 256;  
				if (i < 16)buf.append("0");  
				buf.append(Integer.toHexString(i));  
			}  
			dstr = buf.toString();
		} catch (Exception e) {
			LOGGER.warn("MD5加密失败.{}", e.getMessage());
		}
		return dstr;// 32位加密
	}
	
	/**
	 * 加密DES字符串
	 * @param strIn
	 * @return
	 */
	public static String desEncode(String strIn) {
		try {
			Cipher cipher = getDesCipher("encrypt");
			return byteArr2HexStr(cipher.doFinal(strIn.getBytes()));
		} catch (Exception e) {
			LOGGER.error("加密DES[" + strIn + "]失败", e);
			return null;
		}
	}

	/**
	 * 加密DES字符串
	 * @param strIn
	 * @return
	 */
	public static String desDecode(String strIn) {
		try {
			Cipher cipher = getDesCipher("decode");
			return new String(cipher.doFinal(hexStr2ByteArr(strIn)));
		} catch (Exception e) {
			LOGGER.error("解密DES[" + strIn + "]失败", e);
			return null;
		}
	}

	private static Cipher getDesCipher(String type) {
		Cipher cipher = null;
		try {
			// 实例化DES密钥
			DESKeySpec dks = new DESKeySpec(Constants.getSystemCfg("security.des.key", "^_^@@^_^").getBytes());
			// 实例化密钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 生成密钥
			SecretKey secretKey = keyFactory.generateSecret(dks);
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 加密/解密/工作模式/填充方式
			if ("encrypt".equals(type)) {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			}
		} catch (Exception e) {
			LOGGER.error("初始化DES-Cipher失败", e);
		}
		return cipher;
	}

	private static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16).toUpperCase());
		}
		return sb.toString();
	}

	private static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
}
