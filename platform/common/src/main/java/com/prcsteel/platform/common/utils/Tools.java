package com.prcsteel.platform.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.prcsteel.platform.common.constants.Constant;

/**
 * Created by kongbinheng on 2015/7/21.
 */
public class Tools {

	/**
     * 随机生成六位数验证码
     *
     * @return
     */
    public static int getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
    }


    /**
     * 检测字符串是否不为空(null,"","null")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测字符串是否为空(null,"","null")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

    /**
     * 字符串转换为字符串数组
     *
     * @param str        字符串
     * @param splitRegex 分隔符
     * @return
     */
    public static String[] strToStrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     *
     * @param str 字符串
     * @return
     */
    public static String[] strToStrArray(String str) {
        return strToStrArray(str, ",\\s*");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     *
     * @param date
     * @return
     */
    public static Date strToDate(String date) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }

    /**
     * 按照格式，字符串转日期(如yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @param format
     * @return
     */
    public static Date strToDate(String date, String format) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
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
     * 把时间根据时、分、秒转换为时间段
     *
     * @param StrDate
     */
    public static String getTimes(String StrDate) {
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date = df.parse(StrDate);
            long times = now.getTime() - date.getTime();
            long day = times / (24 * 60 * 60 * 1000);
            long hour = (times / (60 * 60 * 1000) - day * 24);
            long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            StringBuffer sb = new StringBuffer();
            // sb.append("发表于：");
            if (hour > 0) {
                sb.append(hour + "小时前");
            } else if (min > 0) {
                sb.append(min + "分钟前");
            } else {
                sb.append(sec + "秒前");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTimes;
    }

    /**
     * 将yyyy-MM-dd日期转化为中文类型yyyy年MM月dd日
     *
     * @param chineseCreateDate 如2015-07-31转换成2015年07月31日
     * @param splitChar         如-
     * @return
     */
    public static String getChineseDate(String chineseCreateDate,
                                        String splitChar) {
        String[] dateStrList = chineseCreateDate.split(splitChar);
        if (dateStrList.length == 3) {
            return chineseCreateDate = dateStrList[0] + "年" + dateStrList[1]
                    + "月" + dateStrList[2] + "日";
        }
        return "";
    }

    /**
     * 写txt里的单行内容
     *
     * @param fileP   文件路径
     * @param content 写入的内容
     */
    public static void writeFile(String fileP, String content) {
        String filePath = String.valueOf(Thread.currentThread()
                .getContextClassLoader().getResource(""))
                + "../../"; // 项目路径
        filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
        if (filePath.indexOf(":") != 1) {
            filePath = File.separator + filePath;
        }
        try {
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern
                    .compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 读取txt里的单行内容
     *
     * @param fileP 文件路径
     */
    public static String readTxtFile(String fileP) {
        try {

            String filePath = String.valueOf(Thread.currentThread()
                    .getContextClassLoader().getResource(""))
                    + "../../"; // 项目路径
            filePath = filePath.replaceAll("file:/", "");
            filePath = filePath.replaceAll("%20", " ");
            filePath = filePath.trim() + fileP.trim();
            if (filePath.indexOf(":") != 1) {
                filePath = File.separator + filePath;
            }
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding); // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    return lineTxt;
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return "";
    }

    /**
     * 左补0
     *
     * @param length
     * @param num
     * @return
     */
    public static String leftFillZero(Integer length, int num) {
        // 0 代表前面补充0
        // num 代表长度为num
        // d 代表参数为正数型
        String type = "%0" + num + "d";
        return String.format(type, length);
    }

    /**
     * @param b
     * @return
     */
    public static String formatBigDecimal(BigDecimal b) {
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

	/**
	 * BigDecimal格式化
	 * @param b 要格式化的数值
	 * @param len 保留小数点后N位
	 * @return
	 */
	public static String formatBigDecimal(BigDecimal b, int len) {
		return b.setScale(len, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 获取两个日历的月份之差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int monthsDiff(Date d1,Date d2) {
		Calendar c1=Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2=Calendar.getInstance();
		c2.setTime(d2);
		return (c2.get(Calendar.YEAR) - c1
				.get(Calendar.YEAR))
				* 12
				+ c2.get(Calendar.MONTH)
				- c1.get(Calendar.MONTH);
	}

    /**
     * 获取两个日历的月份之差
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int daysDiff(Date beginDate, Date endDate) {
        Calendar s = Calendar.getInstance();
        s.setTime(strToDate(dateToStr(beginDate, "yyyy-MM-dd"), "yyyy-MM-dd"));
        Calendar e = Calendar.getInstance();
        e.setTime(strToDate(dateToStr(endDate, "yyyy-MM-dd"), "yyyy-MM-dd"));
        int days = 0;

        if (e.after(s)) {
            while (e.after(s)) {
                days++;
                s.add(Calendar.DATE, 1);
            }
            return days;
        } else if (s.after(e)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 字符编码转换
     *
     * @param str
     * @return
     */
    public static String encodeStr(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 隐藏手机号码
     *
     * @param phone 手机号码
     * @return 隐藏处理以后的手机号码
     */
    public static String hidePhoneNumber(String phone) {
        if (StringUtils.isNotEmpty(phone) && phone.length() == 11) {
            String startPhone = phone.substring(0, 3);
            String endPhone = phone.substring(8, phone.length());
            phone = startPhone + "*****" + endPhone;
        }
        return phone;
    }
//	public static void main(String[] args) {
//		Date d1 = new Date();
//		Date d2 = new Date(System.currentTimeMillis()-6*24*3600*1000L);
//		System.out.println(getMonthsOfAge(d1,d2));
//	}

    /**
     * 计算百分比并保留两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 百分比
     */
    public static String calculatePercentage(Integer dividend, Integer divisor) {
        Double p3;
        int scale = 2;
        if (dividend == null || divisor == null || divisor == 0) {
            p3 = 0.0;
        } else {
            p3 = dividend * 1.0 / divisor;
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(scale);
        return nf.format(p3);
    }


    /**
     * Returns the string representation of the {@code BigDecimal} argument.
     *
     * @param bigDecimal a {@code BigDecimal}.
     * @return if the argument is {@code null}, then a string equal to
     * {@code ""}; otherwise, the value of
     * {@code bigDecimal.toString()} is returned.
     */
    public static String valueOfBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal == null ? "" : bigDecimal.toString();
    }

    public static String arrayToStr(List<String> list, String separator) {
        if (list == null) {
            return "";
        }
        String sp = separator == null ? "" : separator;
        Optional<String> res = list.stream().filter(a -> a != null).reduce((prev,curr) -> prev + sp + curr);
        return res.isPresent() ? res.get() : "";
    }


    public static String valueOfFormattedBigDecimal(BigDecimal bigDecimal,int scale) {
        if (bigDecimal == null) {
            bigDecimal = BigDecimal.ZERO;
        }
        return bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static String processTabNumber(int number) {
		return number > 0 ? "(" + number + ")" : "";
	}
/**
     *
     *
     * @param d 日期
     * @param day 日期之后的天数
     * @return 日期后面几天的日期，格式yyyy-MM-dd
     */
    public static String getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return dateToStr(now.getTime(),"yyyy-MM-dd");
    }
    /**
     *
     *
     * @param d 日期
     * @param day 日期之后的天数
     * @return 日期后面几天的日期，Date
     */
    public static Date getAfterDate(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     *
     *
     * @param d 日期
     * @param mon 日期之后的月数
     * @return 日期后面几月的日期，Date
     */
    public static Date getAfterMonth(Date d, int mon) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + mon);
        return now.getTime();
    }

    /**
     *
     *
     * @param d 日期
     * @param y 日期之后的年数
     * @return 日期后面几年的日期，Date
     */
    public static Date getAfterYear(Date d, int y) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + y);
        return now.getTime();
    }
	/**
	 * 判断字符是否为正数（正整数和小数）
	 */
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]+|[0-9]+.?[0-9]+"); 
	   Matcher isNum = pattern.matcher(str);
	   if(isNum.matches()){
		   if(Double.valueOf(str)>0){
			   return true; 
		   }
	   } 
	   return false; 
	}
	
	/**
	 * 生成二维码图片
	 * @param code	 	字符串
	 * @param format	图片格式 ：png/jpeg
	 * @param width		图片宽度 ：
	 * @param height	图片高度：
	 * @param stream	文件流：如果是response则是：response.getOutputStream()
	 * @return 返回outputStream
	 * @throws Exception 
	 */
	public static OutputStream getQRCode(String code, String format, int width, int height, OutputStream stream) throws Exception{
		
		/**
		 * 生成的二维码会有20px的边框
		 */
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		//hints.put(EncodeHintType.MARGIN, 1);
		
		BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, width+40, height+40);
		
		//去掉边框，原理：把生成的二维码部分放大
		/*
		BufferedImage bi =  MatrixToImageWriter.toBufferedImage(bitMatrix);

		BufferedImage newImage = new BufferedImage(width,height,bi.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(bi, 0, 0, width,height,null);
        g.dispose();
		
		ImageIO.write(newImage, format, stream); //生成二维码图片
		*/
		
		MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
		return stream;
	}
	
	/**
	 * 得到base64编码的二维码图片
	 * @param code 
	 * @param width
	 * @param height
	 * @param format png/jpeg
	 * @return
	 */
	public static String getQRCodeImgBase64(String code, int width, int height, String format){
		String imgDataHead = "data:image/"+format+";base64,";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			getQRCode(code, format, width, height, out);
			byte[] bs = out.toByteArray();
			byte[] encode = Base64.encodeBase64(bs);
			return imgDataHead+new String(encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到base64编码的二维码图片
	 * @param code 
	 * @param width
	 * @param height
	 * @param format png/jpeg
	 * @return
	 */
	public static String getQRCodeImgBase64(String code, int width, int height){
		return getQRCodeImgBase64(code, width, height, "jpeg");
	}
	 /**
     * 返回下月第一天
     * 格式:yyyy-MM-dd
     *
     * @return String
     */
	public static String getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateToStr(cal.getTime(), Constant.TIME_FORMAT_DAY);
    }
    //全角转换到半角
    public static String toDBC(String input) {
        if (null != input) {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if ('\u3000' == c[i]) {
                    c[i] = ' ';
                } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            String dbc = new String(c);
            return dbc;
        } else {
            return null;
        }
    }
	public static void main(String[] args) {
		System.out.println(getQRCodeImgBase64("12332kljdaslfiefj", 100, 100));
	}
	
}	


