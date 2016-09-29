package com.prcsteel.platform.kuandao.common.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtils {
	
	private DateUtils(){
		
	}
	
	public static String getPlainDate(){
		return getPlainDate(new Date());
	}
	
	public static String getDashDate(){
		return getDashDate(new Date());
	}
	
	public static String getDashDate(Date date){
		return getPlain(date,"yyyy-MM-dd");
	}
	
	public static String getPlainDate(Date date){
		return getPlain(date,"yyyyMMdd");
	}
	
	public static String getPlainDateTime(){
		return getPlainDateTime(new Date());
	}
	
	public static String getPlainDateTime(Date date){
		return getPlain(date,"yyyyMMddhhmmss");
	}
	
	private static String getPlain(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
}
