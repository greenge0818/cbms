package com.prcsteel.platform.kuandao.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title MD5SignVerifyUtil.java
 * @Package com.prcsteel.platform.kuandao.common.util
 * @Description MD5加签及验签类
 * @author zjshan
 *
 * @date 2016年7月21日 下午4:31:38
 */
public class MD5SignVerifyUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5SignVerifyUtil.class);
	/**
	 * 加签
	 * 剔除请求参数中key或者值为空（null或者''），并且按照key的值排序后加上secret，MD5加密，返回encode后的字符串
	 * @param signSource 需要加签的请求参数
	 * @param secret	 秘钥
	 * @param charset	字符集
	 * @return
	 */
	public static String sign(Map<String, String> signSource,String secret,String charset){
		String sign = null;
		StringBuilder signResult = new StringBuilder();
		Set<String> keyset = signSource.keySet();
		keyset.stream().sorted().forEach(key ->{
			String value = signSource.get(key);
			if(StringUtils.isNotEmpty(key)){
				signResult.append(key).append("=").append(value).append("&");
			}
		});
		if(signResult.length() > 1){
			signResult.append(secret);
			MessageDigest md5;
			try {
				md5 = MessageDigest.getInstance("MD5");
				byte[] bytes = md5.digest(signResult.toString().getBytes(charset));
				sign = toHexString(bytes).toLowerCase();
			} catch (NoSuchAlgorithmException e) {
				logger.error("MD5 do not supported",e);
			} catch (UnsupportedEncodingException e) {
				logger.error(String.format("%s do not supported", charset), e);
			}
		}
		return sign;
	}
	
	/**
	 * 验签
	 * 
	 * @param signSource 需要验签的请求参数
	 * @param secret	    秘钥
	 * @param signResult 签名
	 * @param charset	   字符集
	 * @return
	 */
	public static boolean verify(Map<String, String> signSource, String secret, String sign, String charset){
		return StringUtils.equals(sign(signSource,secret,charset), sign);
	}
	
	/**
	 * 将byte数组转换为16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String toHexString( byte[] bytes) {
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) { 
		    String hex = Integer.toHexString(bytes[i] & 0xFF); 
		    if (hex.length() == 1) { 
		      hex = '0' + hex; 
		    }
		    sb.append(hex);
	    } 
	    return sb.toString();
	}
	
}
