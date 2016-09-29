/*     */ package com.csii.payment.client.util;
/*     */ 
/*     */ import com.sun.net.ssl.internal.ssl.Provider;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.Security;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class PFX2JKS
/*     */ {
/*     */   public static void main(String[] args)
/*     */     throws Throwable
/*     */   {
/*  16 */     if (args.length < 3) {
/*  17 */       _$5480();
/*     */     }
/*     */ 
/*  20 */     Properties rb = new Properties();
/*  21 */     rb.load(new FileInputStream(args[0]));
/*     */ 
/*  23 */     Security.addProvider(new Provider());
/*     */ 
/*  26 */     String sourceType = "pkcs12";
/*  27 */     String source = args[1].trim();
/*  28 */     String sourceKeyPass = rb.getProperty("key_password");
/*     */ 
/*  30 */     String destinationType = "jks";
/*  31 */     String destination = rb.getProperty("cafile");
/*     */ 
/*  33 */     String destinationpass = rb.getProperty("store_password");
/*  34 */     String sourcepass = args[2];
/*     */ 
/*  36 */     String keyAlias = rb.getProperty("key_alias");
/*     */ 
/*  38 */     FileInputStream in = null;
/*     */ 
/*  40 */     KeyStore ksin = KeyStore.getInstance(sourceType, new Provider().getName());
/*     */ 
/*  48 */     char[] pwin = null;
/*     */     try
/*     */     {
/*  52 */       in = new FileInputStream(source);
/*  53 */       pwin = sourcepass.toCharArray();
/*  54 */       if (pwin.length == 0)
/*  55 */         pwin = null;
/*  56 */       ksin.load(in, pwin); } catch (FileNotFoundException e) { System.err.println(source + " 文件不存在");
/*     */       return;
/*     */     }
/*     */     catch (Exception e) {
/*  61 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/*  64 */         if (in != null)
/*  65 */           in.close();
/*     */       }
/*     */       catch (Exception e) {
/*     */       }
/*     */     }
/*  70 */     KeyStore ksout = KeyStore.getInstance(destinationType);
/*     */ 
/*  72 */     char[] pwout = null;
/*     */     try {
/*  74 */       in = new FileInputStream(destination);
/*  75 */       pwout = destinationpass.toCharArray();
/*  76 */       if (pwout.length == 0)
/*  77 */         pwout = null;
/*  78 */       ksout.load(in, pwout); } catch (FileNotFoundException e) { System.err.println(destination + " not found");
/*     */       return;
/*     */     }
/*     */     catch (Exception e) {
/*  83 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/*  86 */         if (in != null)
/*  87 */           in.close();
/*     */       }
/*     */       catch (Exception e) {
/*     */       }
/*     */     }
/*  92 */     Enumeration en = ksin.aliases();
/*  93 */     while (en.hasMoreElements()) {
/*  94 */       String alias = (String)en.nextElement();
/*  95 */       if (ksout.containsAlias(keyAlias)) {
/*  96 */         System.out.println(destination + " already contains " + keyAlias);
/*     */ 
/*  98 */         break;
/*     */       }
/*     */ 
/* 101 */       if (ksin.isCertificateEntry(alias)) {
/* 102 */         System.out.println("importing certificate " + alias);
/* 103 */         ksout.setCertificateEntry(alias, ksin.getCertificate(alias));
/*     */       }
/*     */ 
/* 106 */       if (ksin.isKeyEntry(alias)) {
/* 107 */         System.out.println("importing key " + alias + " as " + keyAlias);
/*     */ 
/* 110 */         char[] keyPass = sourceKeyPass.toCharArray();
/* 111 */         if (keyPass.length == 0)
/* 112 */           keyPass = null;
/* 113 */         System.out.println("the provier key is: " + ksin.getKey(alias, pwin).getClass().getName());
/* 114 */         ksout.setKeyEntry(keyAlias, ksin.getKey(alias, pwin), keyPass, ksin.getCertificateChain(alias));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 122 */     FileOutputStream out = new FileOutputStream(destination);
/* 123 */     ksout.store(out, pwout);
/*     */ 
/* 125 */     Enumeration enn = ksout.aliases();
/* 126 */     while (enn.hasMoreElements()) {
/* 127 */       String tmp = (String)enn.nextElement();
/* 128 */       System.out.println(tmp);
/* 129 */       System.out.println(ksout.getCertificate(tmp));
/*     */     }
/*     */ 
/* 132 */     out.close();
/*     */ 
/* 134 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   private static void _$5480() {
/* 138 */     System.err.println("Usage: PFX2JKS <properties file > <pfx file name> <pfx file password>");
/*     */ 
/* 140 */     System.err.println(" Copyright to CSII U,S.A  1999 - 2003   All right reserved");
/*     */ 
/* 142 */     System.exit(0);
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.PFX2JKS
 * JD-Core Version:    0.6.2
 */