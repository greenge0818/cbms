/*    */ package com.csii.payment.client.util;
/*    */ 
/*    */ import com.csii.payment.client.bean.data.SettledOrderListReceive;
/*    */ import java.io.PrintStream;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.Properties;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class AutoUtil
/*    */ {
/*    */   public static String formatRequest(Object request)
/*    */   {
/* 21 */     Class _class = request.getClass();
/*    */ 
/* 23 */     Field[] fields = _class.getFields();
/*    */ 
/* 26 */     for (int i = 0; i < fields.length; i++) {
/* 27 */       System.err.println(fields[i]);
/*    */     }
/* 29 */     return null;
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) {
/* 33 */     formatRequest(new SettledOrderListReceive());
/*    */   }
/*    */ 
/*    */   public static Properties parseStringToProperties(String data, String token)
/*    */   {
/* 40 */     String PROPERTY_DELIMER = "=";
/* 41 */     boolean singleFlag = false;
/* 42 */     if (data == null)
/* 43 */       return null;
/* 44 */     if ((token == null) || (token.length() == 0))
/*    */     {
/* 46 */       singleFlag = true;
/*    */     }
/*    */ 
/* 49 */     StringTokenizer tokenizer = new StringTokenizer(data, token);
/* 50 */     Properties props = new Properties();
/*    */ 
/* 52 */     if (tokenizer.countTokens() == 0) {
/* 53 */       throw new NoSuchElementException("");
/*    */     }
/*    */ 
/* 56 */     while (tokenizer.hasMoreTokens()) {
/* 57 */       String element = tokenizer.nextToken();
/*    */ 
/* 59 */       if (element.indexOf(PROPERTY_DELIMER) != -1) {
/* 60 */         props.put(element.substring(0, element.indexOf(PROPERTY_DELIMER)), element.substring(element.indexOf(PROPERTY_DELIMER) + 1));
/*    */       }
/*    */     }
/*    */ 
/* 64 */     return props;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.AutoUtil
 * JD-Core Version:    0.6.2
 */