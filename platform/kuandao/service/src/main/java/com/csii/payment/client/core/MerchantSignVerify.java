/*     */ package com.csii.payment.client.core;
/*     */ 
/*     */ import com.sun.net.ssl.internal.ssl.Provider;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.Security;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class MerchantSignVerify
/*     */ {
/*     */   private static KeyStore _$2466;
/*     */   private static ResourceBundle _$2530;
/*     */ 
/*     */   public static String merchantSignData(String data)
/*     */   {
/*  60 */     String password = _$2530.getString("key_password");
/*  61 */     String keyAlias = _$2530.getString("key_alias");
/*     */ 
/*  63 */     KeyStoreUtil ks = null;
/*     */     try {
/*  65 */       ks = new KeyStoreUtil(_$2466);
/*     */     } catch (Exception e) {
/*  67 */       return null;
/*     */     }
/*     */ 
/*  70 */     System.err.println("alias_key is !!!!!!!!!!!!!!" + keyAlias);
/*     */ 
/*  72 */     System.err.println("password is!!!!!!!!!!!!!!!!" + password);
/*     */ 
/*  74 */     return ks.signDataRSA(keyAlias, password, data);
/*     */   }
/*     */ 
/*     */   public static String merchantSignData_ABA(String data)
/*     */   {
/*  84 */     String password = _$2530.getString("key_password");
/*  85 */     String keyAlias = _$2530.getString("key_alias");
/*     */ 
/*  87 */     KeyStoreUtil_ABA ks = null;
/*     */     try {
/*  89 */       ks = new KeyStoreUtil_ABA(_$2466);
/*     */     } catch (Exception e) {
/*  91 */       return null;
/*     */     }
/*     */ 
/*  97 */     return ks.signDataRSA(keyAlias, password, data);
/*     */   }
/*     */ 
/*     */   public static boolean merchantVerifyPayGate(String signData, String orgData)
/*     */   {
/* 112 */     KeyStoreUtil ks = null;
/*     */     try {
/* 114 */       ks = new KeyStoreUtil(_$2466);
/*     */     } catch (Exception e) {
/* 116 */       return false;
/*     */     }
/*     */ 
/* 119 */     return ks.verifyDataRSA("paygate_cert", signData, orgData);
/*     */   }
/*     */ 
/*     */   public static boolean merchantVerifyPayGate_ABA(String signData, String orgData)
/*     */   {
/* 134 */     KeyStoreUtil_ABA ks = null;
/*     */     try {
/* 136 */       ks = new KeyStoreUtil_ABA(_$2466);
/*     */     } catch (Exception e) {
/* 138 */       return false;
/*     */     }
/*     */ 
/* 141 */     return ks.verifyDataRSA("paygate_cert", signData, orgData);
/*     */   }
/*     */ 
/*     */   public static boolean merchantVerifySelf(String signData, String orgData)
/*     */   {
/* 152 */     String alias = _$2530.getString("key_alias");
/*     */ 
/* 154 */     KeyStoreUtil ks = null;
/*     */     try {
/* 156 */       ks = new KeyStoreUtil(_$2466);
/*     */     } catch (Exception e) {
/* 158 */       return false;
/*     */     }
/*     */ 
/* 161 */     return ks.verifyDataRSA(alias, signData, orgData);
/*     */   }
/*     */ 
/*     */   public static boolean merchantVerifySelf_ABA(String signData, String orgData)
/*     */   {
/* 174 */     String keyAlias = _$2530.getString("key_alias");
/* 175 */     KeyStoreUtil_ABA ks = null;
/*     */     try {
/* 177 */       ks = new KeyStoreUtil_ABA(_$2466);
/*     */     } catch (Exception e) {
/* 179 */       return false;
/*     */     }
/*     */ 
/* 182 */     return ks.verifyDataRSA(keyAlias, signData, orgData);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 197 */     String ori = "orderId=000760|seqNo=10825000073|merchantDate=2003-08-25|userId=yyb01";
/*     */ 
/* 199 */     String sign = merchantSignData_ABA(ori);
/* 200 */     boolean correct = merchantVerifySelf_ABA(sign, ori);
/* 201 */     System.out.println("ori=" + ori);
/* 202 */     System.out.println("sign=" + sign);
/* 203 */     System.out.println("correct=" + correct);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  16 */     System.err.println("Debug==> begin");
/*  17 */     Security.addProvider(new Provider());
/*     */ 
/*  19 */     _$2530 = ResourceBundle.getBundle("spdb_merchant");
/*  20 */     String cafile = _$2530.getString("cafile");
/*  21 */     String store_password = _$2530.getString("store_password");
/*  22 */     System.err.println("Debug==> cafile=" + cafile);
/*  23 */     InputStream inputStream = null;
/*     */     try {
/*  25 */       inputStream = new FileInputStream(cafile);
/*     */     } catch (FileNotFoundException e) {
/*  27 */       System.err.println("Error in MerSignVerify CA File Not Found");
/*  28 */       e.printStackTrace();
/*  29 */       System.err.println("Debug==> file not found exception");
/*     */     }
/*  31 */     System.err.println("Debug==> end");
/*     */ 
/*  33 */     char[] pass = store_password == null ? null : store_password.toCharArray();
/*     */     try
/*     */     {
/*  37 */       _$2466 = KeyStore.getInstance("JKS");
/*  38 */       _$2466.load(inputStream, pass);
/*     */     }
/*     */     catch (Exception e) {
/*  41 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.core.MerchantSignVerify
 * JD-Core Version:    0.6.2
 */