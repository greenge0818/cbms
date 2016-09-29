package com.prcsteel.platform.common.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**   
 * @Title: NumberTool.java 
 * @Package com.prcsteel.cbms.web.utils 
 * @Description: 数字格式化工具 
 * @author Green.Ge   
 * @date 2015年8月5日 上午9:41:51 
 * @version V1.0   
 */
public class NumberTool extends org.apache.velocity.tools.generic.NumberTool{
	public static String toThousandth(Object number){
		if(number==null||"".equals(number)) return "";
		return NumberFormat.getCurrencyInstance(Locale.CHINA).format(number).replace("￥", "");
	}

	public static String number2CNMontray(Object number){
		if(number==null||"".equals(number)) return "";
		return NumberToCNUtils.number2CNMontrayUnit((BigDecimal)number);
	}

	public static String dateToStr(Date date, String format){
		return Tools.dateToStr(date, format);
	}
	
	public static Object abs(Object number){
		double ins = 0;
		if(number instanceof Number)
			ins = ((Number)number).doubleValue();
		else
			return number;
		
		return Math.abs(ins);
	}
	
	/**
	 * Double转化成百分数
	 * @param val 数值
	 * @param scale 小数点位数
	 * @return
	 */
	public static String getDoubleToPercent(BigDecimal val, int scale) {
		if (val==null || val.equals(BigDecimal.ZERO)) {
			return "0";
		}

		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(scale); //最大小数位数
		percentFormat.setMinimumFractionDigits(scale); //最小小数位数

		return percentFormat.format(val);
	}
}
 