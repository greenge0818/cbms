package com.prcsteel.platform.common.utils;

public class StringToReplace {

	/**
	 * 字符串去空格方法，如字符串为空则返回null
	 * 
	 * @param str
	 * @return character
	 */
	public static String toReplaceAll(String str) {

		if (str == null) {
			return null;
		}
		String character = str.replaceAll(" ", "");
		return character;
	}
}
