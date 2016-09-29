/*     */ package com.csii.payment.client.util;
/*     */ 
/*     */ import com.csii.payment.client.exception.CsiiException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Date;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class InputCheckTool
/*     */ {
/*     */   public static boolean isInteger(String input)
/*     */   {
/*  25 */     if ((input == null) || (input.equals(""))) {
/*  26 */       return false;
/*     */     }
/*  28 */     for (int i = 0; i < input.length(); i++) {
/*  29 */       if (!Character.isDigit(input.charAt(i))) {
/*  30 */         return false;
/*     */       }
/*     */     }
/*  33 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isDouble(String input) {
/*  37 */     if ((input == null) || (input.equals("")))
/*  38 */       return false;
/*     */     try
/*     */     {
/*  41 */       Double.parseDouble(input);
/*     */     } catch (Exception e) {
/*  43 */       return false;
/*     */     }
/*     */ 
/*  46 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isDate(String input) {
/*  50 */     if ((input == null) || (input.equals("")) || (input.length() != 8)) {
/*  51 */       return false;
/*     */     }
/*  53 */     if (!isInteger(input)) {
/*  54 */       return false;
/*     */     }
/*  56 */     int ayear = Integer.parseInt(input.substring(0, 4));
/*  57 */     int amonth = Integer.parseInt(input.substring(4, 6));
/*  58 */     int aday = Integer.parseInt(input.substring(6, 8));
/*     */ 
/*  60 */     Date newdate = new Date(ayear, amonth - 1, aday);
/*     */ 
/*  62 */     int newyear = newdate.getYear();
/*  63 */     int newmonth = newdate.getMonth() + 1;
/*  64 */     int newday = newdate.getDate();
/*     */ 
/*  66 */     if ((ayear != newyear) || (amonth != newmonth) || (aday != newday)) {
/*  67 */       return false;
/*     */     }
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isMoney(String input) {
/*  73 */     boolean hasDot = false;
/*     */ 
/*  75 */     if ((input == null) || (input.equals(""))) {
/*  76 */       return false;
/*     */     }
/*  78 */     StringBuffer target = new StringBuffer();
/*  79 */     int begin = 0;
/*  80 */     int end = 0;
/*  81 */     while ((end = input.indexOf(",", begin)) != -1) {
/*  82 */       target.append(input.substring(begin, end));
/*  83 */       begin = end + 1;
/*     */     }
/*     */ 
/*  86 */     input = input.substring(begin);
/*     */ 
/*  88 */     for (int i = 0; i < input.length(); i++) {
/*  89 */       if (!Character.isDigit(target.charAt(i))) {
/*  90 */         if (target.charAt(i) == '.') {
/*  91 */           if (hasDot) {
/*  92 */             return false;
/*     */           }
/*  94 */           hasDot = true;
/*     */         }
/*     */         else
/*     */         {
/*  98 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   public static double parseMoney(String input) throws CsiiException {
/* 106 */     if ((input == null) || (input.equals(""))) {
/* 107 */       return 0.0D;
/*     */     }
/* 109 */     if (!isMoney(input)) {
/* 110 */       throw new CsiiException("com.bocbj.bean.bank.InputCheckTool.parseMoney error: 金额格式错误");
/*     */     }
/* 112 */     if (input.indexOf(",") == -1) {
/* 113 */       return Double.parseDouble(input);
/*     */     }
/*     */ 
/* 116 */     StringBuffer target = new StringBuffer();
/* 117 */     int begin = 0;
/* 118 */     int end = 0;
/* 119 */     while ((end = input.indexOf(",", begin)) != -1) {
/* 120 */       target.append(input.substring(begin, end));
/* 121 */       begin = end + 1;
/*     */     }
/*     */ 
/* 124 */     input = input.substring(begin);
/*     */ 
/* 126 */     return Double.parseDouble(input);
/*     */   }
/*     */ 
/*     */   public static String normalFormatDouble(Double input) {
/* 130 */     return normalFormatDouble(input.doubleValue());
/*     */   }
/*     */ 
/*     */   public static String normalFormatDouble(double input) {
/* 134 */     DecimalFormat formater = new DecimalFormat("##.##");
/*     */ 
/* 136 */     return formater.format(input);
/*     */   }
/*     */ 
/*     */   public static void checkNotNull(String[] values, String descs)
/*     */     throws CsiiException
/*     */   {
/* 144 */     StringTokenizer descToken = new StringTokenizer(descs, "|");
/* 145 */     int i = 0;
/*     */ 
/* 147 */     if (descToken.countTokens() != values.length) {
/* 148 */       throw new CsiiException("内部错误：参数名与参数数量不一致");
/*     */     }
/* 150 */     while (descToken.hasMoreElements()) {
/* 151 */       String para = values[i];
/* 152 */       if ((para == null) || (para.equals(""))) {
/* 153 */         throw new CsiiException(descToken.nextElement() + "不能为空");
/*     */       }
/* 155 */       descToken.nextElement();
/*     */ 
/* 157 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isEmail(String email) {
/* 162 */     if ((email == null) || (email.length() == 0)) {
/* 163 */       return false;
/*     */     }
/* 165 */     if (email.indexOf("@") == -1)
/* 166 */       return false;
/* 167 */     if (email.indexOf("@") != email.indexOf("@"))
/* 168 */       return false;
/* 169 */     if (email.indexOf(".", email.indexOf("@")) == -1) {
/* 170 */       return false;
/*     */     }
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   public static Properties parseStringToProperties(String data, String token)
/*     */   {
/* 178 */     String PROPERTY_DELIMER = "=";
/* 179 */     boolean singleFlag = false;
/* 180 */     if (data == null)
/* 181 */       return null;
/* 182 */     if ((token == null) || (token.length() == 0))
/*     */     {
/* 184 */       singleFlag = true;
/*     */     }
/*     */ 
/* 187 */     StringTokenizer tokenizer = new StringTokenizer(data, token);
/* 188 */     Properties props = new Properties();
/*     */ 
/* 190 */     if (tokenizer.countTokens() == 0) {
/* 191 */       throw new NoSuchElementException("");
/*     */     }
/*     */ 
/* 194 */     while (tokenizer.hasMoreTokens()) {
/* 195 */       String element = tokenizer.nextToken();
/*     */ 
/* 197 */       if (element.indexOf(PROPERTY_DELIMER) != -1) {
/* 198 */         props.put(element.substring(0, element.indexOf(PROPERTY_DELIMER)), element.substring(element.indexOf(PROPERTY_DELIMER) + 1));
/*     */       }
/*     */     }
/*     */ 
/* 202 */     return props;
/*     */   }
/*     */ 
/*     */   public static String toFixedLength(String source, int length) {
/* 206 */     if ((source == null) || (source.length() == 0)) {
/* 207 */       return new String(new byte[length]);
/*     */     }
/*     */ 
/* 210 */     int byteLength = length;
/* 211 */     byte[] sourceByte = source.getBytes();
/*     */ 
/* 213 */     byte[] target = new byte[byteLength];
/*     */ 
/* 215 */     for (int i = 0; i < byteLength - sourceByte.length; i++) {
/* 216 */       target[i] = 32;
/*     */     }
/*     */ 
/* 219 */     for (int i = byteLength - sourceByte.length; i < byteLength; i++) {
/* 220 */       target[i] = sourceByte[(sourceByte.length - byteLength + i)];
/*     */     }
/*     */ 
/* 223 */     return new String(target);
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.InputCheckTool
 * JD-Core Version:    0.6.2
 */