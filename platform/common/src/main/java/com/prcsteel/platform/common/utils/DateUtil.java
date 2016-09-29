package com.prcsteel.platform.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class DateUtil {

	/**
	 * 将一个日期转换成结束日期
	 * 处理方法： 日增加一天，时分秒设置为0s
	 * @param endDate
	 * @return
	 */
	public static Date toEndDate(Date endDate){
		if(endDate == null){
			return null;
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(endDate);
		ca.add(Calendar.DATE, 1);
		ca.set(Calendar.HOUR, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
	
	/** 
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示 
     *  
     * @author wxy 
     * @param ctime 
     *            时间 
     * @param format 
     *            格式 格式描述:例如:yyyy-MM-dd yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String showTime(Date ctime, String format) {  
        //System.out.println("当前时间是："+new Timestamp(System.currentTimeMillis()));  
          
  
        //System.out.println("发布时间是："+df.format(ctime).toString());  
        String r = "";  
        if(ctime==null)return r;  
        if(format==null)format="MM-dd HH:mm";  
  
        long nowtimelong = System.currentTimeMillis();  
  
        long ctimelong = ctime.getTime();  
        long result = Math.abs(nowtimelong - ctimelong);  
  
        if(result < 60000){// 一分钟内  
            long seconds = result / 1000;  
            if(seconds == 0){  
                r = "刚刚";  
            }else{  
                r = seconds + "秒前";  
            }  
        }else if (result >= 60000 && result < 3600000){// 一小时内  
            long seconds = result / 60000;  
            r = seconds + "分钟前";  
        }else if (result >= 3600000 && result < 86400000){// 一天内  
            long seconds = result / 3600000;  
            r = seconds + "小时前";  
        }else if (result >= 86400000 && result < 1702967296){// 三十天内  
            long seconds = result / 86400000;  
            r = seconds + "天前";  
        }else{// 日期格式  
            format="MM-dd HH:mm";  
            SimpleDateFormat df = new SimpleDateFormat(format);  
            r = df.format(ctime).toString();  
        }  
        return r;  
    }  
    
    public static Boolean isSameDay(Date date){
    	if(date==null){
    		return false;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(new Date()).equals(sdf.format(date));
    }
    
    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 时间字符串转Date对象
     * @param date
     * @return
     */
    public static Date strToDate(String date, String format){
        if(StringUtils.isBlank(date)){
            return null;
        }else{
            try{
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期
                sdf.setLenient(false);
                return sdf.parse(date);
            }catch (ParseException e){
                return null;
            }
        }
    }
}
