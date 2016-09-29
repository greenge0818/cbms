/*     */ package com.csii.payment.client.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.KeyPair;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Security;
/*     */ import java.security.Signature;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ 
/*     */ public class SignAndVerify
/*     */ {
/*  11 */   public static String keyPath = "/paygate/";
/*  12 */   public static String signatureAlgorithm = "SHA/DSA";
/*     */ 
/*  15 */   public static Hashtable keyBuffer = new Hashtable();
/*     */ 
/*     */   public static String byteToHex(byte[] inbuf)
/*     */   {
/*  34 */     StringBuffer strBuf = new StringBuffer();
/*     */ 
/*  36 */     for (int i = 0; i < inbuf.length; i++)
/*     */     {
/*  38 */       String byteStr = Integer.toHexString(inbuf[i] & 0xFF);
/*  39 */       if (byteStr.length() != 2)
/*  40 */         strBuf.append('0').append(byteStr);
/*     */       else {
/*  42 */         strBuf.append(byteStr);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  47 */     return new String(strBuf);
/*     */   }
/*     */ 
/*     */   public static String encryptPassword(String userId, String password)
/*     */   {
/*  57 */     userId = userId.trim();
/*  58 */     password = password.trim();
/*  59 */     String signature = sign(userId + password, "/paygate/", "ibskey", "SHA/DSA");
/*     */ 
/*  61 */     return signature;
/*     */   }
/*     */ 
/*     */   public static byte[] hexToByte(String inbuf)
/*     */   {
/*  70 */     int len = inbuf.length() / 2;
/*  71 */     byte[] outbuf = new byte[len];
/*     */ 
/*  73 */     for (int i = 0; i < len; i++) {
/*  74 */       String tmpbuf = inbuf.substring(i * 2, i * 2 + 2);
/*  75 */       outbuf[i] = ((byte)Integer.parseInt(tmpbuf, 16));
/*     */     }
/*  77 */     return outbuf;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  85 */     String shadow = encryptPassword("1", "df");
/*     */ 
/*  89 */     boolean ok = verifyPassword("1", "df", shadow);
/*     */   }
/*     */ 
/*     */   public static String packetSign(String inbuf, String keyName)
/*     */   {
/* 101 */     String signature = sign(inbuf, keyName);
/*     */ 
/* 103 */     return URLEncoder.encode(inbuf) + "@" + signature;
/*     */   }
/*     */ 
/*     */   public static String packetVerify(String inbuf, String keyName)
/*     */   {
/* 115 */     int offset = inbuf.indexOf('@');
/* 116 */     String data = inbuf.substring(0, offset);
/*     */     try {
/* 118 */       data = URLDecoder.decode(data);
/*     */     } catch (Exception e) {
/*     */     }
/* 121 */     String sign = inbuf.substring(offset + 1);
/*     */ 
/* 123 */     if (verify(data, sign, keyName)) {
/* 124 */       return data;
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */   public static String sign(String inbuf, String keyName) {
/* 129 */     return sign(inbuf, keyPath, keyName, signatureAlgorithm);
/*     */   }
/*     */ 
/*     */   public static String sign(String inbuf, String keyPath, String keyName, String signatureAlgorithm)
/*     */   {
/* 137 */     String sigStr = null;
/*     */     try {
/* 139 */       KeyPair pair = (KeyPair)keyBuffer.get("S" + keyPath + keyName);
/* 140 */       if (pair == null) {
/* 141 */         pair = KeyManager.getKeyPair(keyPath, keyName);
/* 142 */         if (pair == null)
/* 143 */           return null;
/* 144 */         keyBuffer.put("S" + keyPath + keyName, pair);
/*     */       }
/* 146 */       Signature dsa = Signature.getInstance(signatureAlgorithm);
/*     */ 
/* 148 */       PrivateKey priv = pair.getPrivate();
/*     */ 
/* 150 */       dsa.initSign(priv);
/*     */ 
/* 152 */       dsa.update(inbuf.getBytes());
/*     */ 
/* 154 */       byte[] sig = dsa.sign();
/*     */ 
/* 157 */       sigStr = byteToHex(sig);
/*     */     }
/*     */     catch (Exception e) {
/* 160 */       System.err.println("Caught exception " + e.toString());
/*     */     }
/*     */ 
/* 163 */     return sigStr;
/*     */   }
/*     */   public static boolean verify(String inbuf, String sign, String keyName) {
/* 166 */     return verify(inbuf, sign, keyPath, keyName, signatureAlgorithm);
/*     */   }
/*     */ 
/*     */   public static boolean verify(String inbuf, String sign, String keyPath, String keyName, String signatureAlgorithm)
/*     */   {
/*     */     try
/*     */     {
/* 176 */       PublicKey pub = (PublicKey)keyBuffer.get("V" + keyPath + keyName);
/* 177 */       if (pub == null) {
/* 178 */         pub = KeyManager.getPublicKey(keyPath, keyName);
/* 179 */         if (pub == null)
/* 180 */           return false;
/* 181 */         keyBuffer.put("V" + keyPath + keyName, pub);
/*     */       }
/*     */ 
/* 184 */       Signature dsa = Signature.getInstance(signatureAlgorithm);
/* 185 */       dsa.initVerify(pub);
/*     */ 
/* 187 */       dsa.update(inbuf.getBytes());
/* 188 */       byte[] sig = hexToByte(sign);
/* 189 */       return dsa.verify(sig);
/*     */     } catch (Exception e) {
/* 191 */       System.err.println("Caught exception " + e);
/* 192 */     }return false;
/*     */   }
/*     */ 
/*     */   public static boolean verifyPassword(String userId, String password, String cryptograph)
/*     */   {
/* 205 */     if (verify(userId + password, cryptograph, "/paygate/", "ibskey", "SHA/DSA"))
/*     */     {
/* 210 */       return true;
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean verifySignature(String inbuf, String sign, PublicKey pub)
/*     */   {
/* 226 */     Signature dsa = null;
/*     */     RSAPublicKey rsaKey;
/* 229 */     if ((pub instanceof RSAPublicKey)) {
/* 230 */       rsaKey = (RSAPublicKey)pub;
/*     */     } else {
/* 232 */       System.err.println("invalid key");
/* 233 */       rsaKey = (RSAPublicKey)pub;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 240 */       dsa = Signature.getInstance("MD5withRSA", "ABA");
/* 241 */       dsa.initVerify(pub);
/* 242 */       dsa.update(inbuf.getBytes());
/* 243 */       byte[] sig = hexToByte(sign);
/* 244 */       return dsa.verify(sig);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 248 */       System.err.println("Caught exception " + e.toString());
/* 249 */     }return false;
/*     */   }
/*     */ 
/*     */   public static boolean verifySignature(byte[] orig, String sign, PublicKey pub)
/*     */   {
/* 266 */     Signature dsa = null;
/*     */     RSAPublicKey rsaKey;
/* 269 */     if ((pub instanceof RSAPublicKey)) {
/* 270 */       rsaKey = (RSAPublicKey)pub;
/*     */     } else {
/* 272 */       System.err.println("invalid key");
/* 273 */       rsaKey = (RSAPublicKey)pub;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 280 */       dsa = Signature.getInstance("MD5withRSA", "ABA");
/* 281 */       dsa.initVerify(pub);
/* 282 */       dsa.update(orig);
/* 283 */       byte[] sig = hexToByte(sign);
/* 284 */       return dsa.verify(sig);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 288 */       System.err.println("Caught exception " + e.toString());
/* 289 */     }return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  17 */       Security.addProvider(new BouncyCastleProvider());
/*     */     }
/*     */     catch (Exception e) {
/*  20 */       System.err.println("load ABA provider failed");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.SignAndVerify
 * JD-Core Version:    0.6.2
 */