package com.prcsteel.platform.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;

public class StringUtil {
	public static String build(String origin, String charsetName) {
		if (origin == null)
			return null;

		StringBuilder sb = new StringBuilder();
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		// 生成一组length=16的byte数组
		byte[] bs = digest.digest(origin.getBytes(Charset.forName(charsetName)));

		for (int i = 0; i < bs.length; i++) {
			int c = bs[i] & 0xFF; // byte转int为了不丢失符号位， 所以&0xFF
			if (c < 16) { // 如果c小于16，就说明，可以只用1位16进制来表示， 那么在前面补一个0
				sb.append("0");
			}
			sb.append(Integer.toHexString(c));
		}
		return sb.toString();
	}

	public static String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h",

			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",

			"u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",

			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",

			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",

			"U", "V", "W", "X", "Y", "Z" };

	public static String build(String url) {
		if (url == null) {
			return null;
		}
		// 先得到url的32个字符的md5码
		String md5 = build(url, "UTF-8");
		StringBuilder sb = new StringBuilder();
		// 将32个字符的md5码分成4段处理，每段8个字符
		for (int i = 0; i < 4; i++) {
			int offset = i * 8;
			String sub = md5.substring(offset, offset + 8);
			long sub16 = Long.parseLong(sub, 16); // 将sub当作一个16进制的数，转成long
			// & 0X3FFFFFFF，去掉最前面的2位，只留下30位
			sub16 &= 0X3FFFFFFF;
			// 将剩下的30位分6段处理，每段5位
			for (int j = 0; j < 6; j++) {
				// 得到一个 <= 61的数字
				long t = sub16 & 0x0000003D;
				sb.append(chars[(int) t]);
				sub16 >>= 5; // 将sub16右移5位
			}
		}
		return sb.toString();
	}
	/**
	 * 签名生成算法
	 * @param params 请求参数集，所有参数必须已转换为字符串类型
	 * @param secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(Map<String,String> params, String secret) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<>(params);
		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> param : entrys) {
			sb.append(param.getKey()).append("=").append(param.getValue());
		}
		sb.append(secret);

		// 使用MD5对待签名串求签
		byte[] bytes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(sb.toString().getBytes("UTF-8"));
		} catch (GeneralSecurityException ex) {
			throw new IOException(ex);
		}

		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}
	
	
	public static boolean verifySignature(HashMap<String,String> params, String token) {
		try {
			String myToken = StringUtil.getSignature(params, Constant.SECRET);

			if (token.equals(myToken)) {
				return true;
			}

		} catch (IOException e) {
			new BusinessException("500", "Get signature failed");
		}

		return false;
	}
}
