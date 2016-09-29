/*     */ package com.csii.payment.client.core;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.Key;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Signature;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ class KeyStoreUtil
/*     */ {
/*  15 */   private Hashtable _$2463 = new Hashtable();
/*     */ 
/*  17 */   private Hashtable _$2464 = new Hashtable();
/*     */   private KeyStore _$2466;
/*     */   private InputStream _$2467;
/*     */ 
/*     */   public KeyStoreUtil(String keyName, String storePassword)
/*     */     throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException
/*     */   {
/*  26 */     if (keyName == null) {
/*  27 */       keyName = System.getProperty("JAVA_HOME") + "/jre/lib/security/cacerts";
/*     */     }
/*     */     try
/*     */     {
/*  31 */       this._$2467 = new FileInputStream(keyName);
/*     */     } catch (FileNotFoundException e) {
/*  33 */       System.err.println("File Not Found");
/*  34 */       e.printStackTrace();
/*  35 */       throw e;
/*     */     }
/*     */ 
/*  38 */     char[] pass = storePassword == null ? null : storePassword.toCharArray();
/*     */     try
/*     */     {
/*  43 */       this._$2466 = KeyStore.getInstance("JKS");
/*  44 */       this._$2466.load(this._$2467, pass);
/*     */     }
/*     */     catch (KeyStoreException e1) {
/*  47 */       e1.printStackTrace();
/*  48 */       throw e1;
/*     */     } catch (NoSuchAlgorithmException e2) {
/*  50 */       e2.printStackTrace();
/*  51 */       throw e2;
/*     */     }
/*     */ 
/*  54 */     Enumeration enu = this._$2466.aliases();
/*     */ 
/*  56 */     while (enu.hasMoreElements()) {
/*  57 */       String alias = (String)enu.nextElement();
/*     */ 
/*  59 */       if (this._$2466.isCertificateEntry(alias)) {
/*  60 */         CertificateInfo ci = new CertificateInfo();
/*  61 */         ci.setAlias(alias);
/*  62 */         ci.setCertificate(this._$2466.getCertificate(alias));
/*  63 */         ci.setCertificateChain(this._$2466.getCertificateChain(alias));
/*  64 */         ci.setCreationDate(this._$2466.getCreationDate(alias));
/*  65 */         this._$2464.put(alias, ci);
/*     */       }
/*  69 */       else if (this._$2466.isKeyEntry(alias)) {
/*  70 */         KeyInfo ki = new KeyInfo();
/*  71 */         ki.setAlias(alias);
/*  72 */         ki.setCreationDate(this._$2466.getCreationDate(alias));
/*  73 */         this._$2463.put(alias, ki);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public KeyStoreUtil(KeyStore keyStore)
/*     */     throws KeyStoreException
/*     */   {
/*  86 */     this._$2466 = keyStore;
/*     */ 
/*  88 */     Enumeration enu = keyStore.aliases();
/*     */ 
/*  90 */     while (enu.hasMoreElements()) {
/*  91 */       String alias = (String)enu.nextElement();
/*     */ 
/*  93 */       if (keyStore.isCertificateEntry(alias)) {
/*  94 */         CertificateInfo ci = new CertificateInfo();
/*  95 */         ci.setAlias(alias);
/*  96 */         ci.setCertificate(keyStore.getCertificate(alias));
/*  97 */         ci.setCertificateChain(keyStore.getCertificateChain(alias));
/*  98 */         ci.setCreationDate(keyStore.getCreationDate(alias));
/*  99 */         this._$2464.put(alias, ci);
/*     */       }
/* 103 */       else if (keyStore.isKeyEntry(alias)) {
/* 104 */         KeyInfo ki = new KeyInfo();
/* 105 */         ki.setAlias(alias);
/* 106 */         ki.setCreationDate(keyStore.getCreationDate(alias));
/* 107 */         this._$2463.put(alias, ki);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String byteToHex(byte[] inbuf)
/*     */   {
/* 124 */     StringBuffer strBuf = new StringBuffer();
/*     */ 
/* 126 */     for (int i = 0; i < inbuf.length; i++)
/*     */     {
/* 128 */       String byteStr = Integer.toHexString(inbuf[i] & 0xFF);
/* 129 */       if (byteStr.length() != 2)
/*     */       {
/* 131 */         strBuf.append('0').append(byteStr);
/*     */       }
/*     */       else
/*     */       {
/* 135 */         strBuf.append(byteStr);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     return new String(strBuf);
/*     */   }
/*     */ 
/*     */   public void creatCertificateRequest()
/*     */     throws Exception
/*     */   {
/* 149 */     KeyPairGenerator keyGen = KeyPairGenerator.getInstance("MD5withRSA");
/* 150 */     keyGen.initialize(1024, new SecureRandom());
/* 151 */     KeyPair pair = keyGen.generateKeyPair();
/*     */   }
/*     */ 
/*     */   public void creatKeyStore()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertificateInfo getCertificateEntry(String alias)
/*     */   {
/* 166 */     return (CertificateInfo)this._$2464.get(alias);
/*     */   }
/*     */ 
/*     */   public int getCertificateEntrySize()
/*     */   {
/* 174 */     return this._$2464.size();
/*     */   }
/*     */ 
/*     */   public KeyInfo getKeyEntry(String alias)
/*     */   {
/* 183 */     return (KeyInfo)this._$2463.get(alias);
/*     */   }
/*     */ 
/*     */   public int getKeyEntrySize()
/*     */   {
/* 191 */     return this._$2463.size();
/*     */   }
/*     */ 
/*     */   public KeyStore getKeyStore()
/*     */   {
/* 199 */     return this._$2466;
/*     */   }
/*     */ 
/*     */   private PrivateKey _$2511(String alias, String password)
/*     */   {
/* 210 */     if ((alias == null) || (password == null))
/* 211 */       return null;
/*     */     try
/*     */     {
/* 214 */       return (PrivateKey)this._$2466.getKey(alias, password.toCharArray());
/*     */     } catch (Exception e) {
/* 216 */       e.printStackTrace();
/* 217 */     }return null;
/*     */   }
/*     */ 
/*     */   public static byte[] hexToByte(String inbuf)
/*     */   {
/* 230 */     int len = inbuf.length() / 2;
/* 231 */     byte[] outbuf = new byte[len];
/*     */ 
/* 233 */     for (int i = 0; i < len; i++)
/*     */     {
/* 235 */       String tmpbuf = inbuf.substring(i * 2, i * 2 + 2);
/*     */ 
/* 237 */       outbuf[i] = ((byte)Integer.parseInt(tmpbuf, 16));
/*     */     }
/*     */ 
/* 241 */     return outbuf;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 250 */     KeyStoreUtil ks = null;
/*     */     try {
/* 252 */       ks = new KeyStoreUtil("c:\\Documents and Settings\\Administrator.XDTAO\\.keystore", null);
/*     */     } catch (Exception e) {
/* 254 */       e.printStackTrace();
/* 255 */       return;
/*     */     }
/*     */ 
/* 261 */     Key key = ks._$2511("t1", "123456");
/* 262 */     System.err.println("Key :" + key.getClass().getName());
/*     */ 
/* 264 */     if ((key instanceof RSAPrivateKey)) {
/* 265 */       System.err.println("java.security.interfaces.RSAPrivateKey");
/*     */     }
/*     */ 
/* 268 */     String bb = "1";
/* 269 */     String aa = ks.signDataRSA("t2", "123456", bb);
/* 270 */     System.err.println("org  data is:\n" + bb);
/* 271 */     System.err.println("sign data is:\n" + aa);
/*     */ 
/* 273 */     if (ks.verifyDataRSA("t2", aa, bb))
/* 274 */       System.err.println("verify ok");
/*     */     else
/* 276 */       System.err.println("verify fail");
/*     */   }
/*     */ 
/*     */   public String payGateSignData(String data)
/*     */   {
/* 286 */     ResourceBundle rb = ResourceBundle.getBundle("paygate");
/* 287 */     String alias = rb.getString("alias");
/* 288 */     String password = rb.getString("password");
/*     */ 
/* 290 */     return signDataRSA(alias, password, data);
/*     */   }
/*     */ 
/*     */   public boolean payGateVerifyData(String mer_id, String signData, String orgData)
/*     */   {
/* 303 */     return verifyDataRSA(mer_id, signData, orgData);
/*     */   }
/*     */ 
/*     */   private String _$3066(PrivateKey privateKey, String data, String alg)
/*     */   {
/*     */     try
/*     */     {
/* 315 */       Signature dsa = Signature.getInstance(alg);
/*     */ 
/* 317 */       dsa.initSign(privateKey);
/*     */ 
/* 319 */       dsa.update(data.getBytes("GBK"));
/*     */ 
/* 321 */       byte[] sig = dsa.sign();
/*     */ 
/* 323 */       return byteToHex(sig);
/*     */     }
/*     */     catch (Exception e) {
/* 326 */       e.printStackTrace();
/* 327 */     }return null;
/*     */   }
/*     */ 
/*     */   public String signDataRSA(String alias, String password, String data)
/*     */   {
/* 339 */     if ((alias == null) || (password == null) || (data == null)) {
/* 340 */       return null;
/*     */     }
/* 342 */     PrivateKey privateKey = _$2511(alias, password);
/*     */ 
/* 344 */     if (privateKey == null) {
/* 345 */       System.err.println("The alias or password is wrong");
/* 346 */       return null;
/*     */     }
/*     */ 
/* 349 */     if (!(privateKey instanceof RSAPrivateKey))
/*     */     {
/* 351 */       System.err.println("The key is not RSAPrivateKey");
/* 352 */       System.err.println("The class should Run under JDK1.3");
/* 353 */       return null;
/*     */     }
/* 355 */     return _$3066(privateKey, data, "MD5withRSA");
/*     */   }
/*     */ 
/*     */   private boolean _$3082(PublicKey publicKey, String signData, String orgData, String alg)
/*     */   {
/*     */     try
/*     */     {
/* 369 */       if ((publicKey == null) || (signData == null) || (orgData == null) || (alg == null)) {
/* 370 */         System.err.println("Error:in KeyStoreUtil.verifyData() . publicKey or signData or orgData or alg is null");
/* 371 */         return false;
/*     */       }
/*     */ 
/* 374 */       Signature dsa = Signature.getInstance(alg);
/*     */ 
/* 376 */       dsa.initVerify(publicKey);
/*     */ 
/* 378 */       dsa.update(orgData.getBytes("GBK"));
/*     */ 
/* 380 */       byte[] sig = hexToByte(signData);
/* 381 */       return dsa.verify(sig);
/*     */     }
/*     */     catch (Exception e) {
/* 384 */       e.printStackTrace();
/* 385 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean verifyDataRSA(String alias, String signData, String orgData)
/*     */   {
/*     */     try
/*     */     {
/* 400 */       if ((alias == null) || (signData == null) || (orgData == null)) {
/* 401 */         System.err.println("Error:in KeyStoreUtil.verifyDataRSA() . alias or signData or orgData is null");
/* 402 */         return false;
/*     */       }
/*     */ 
/* 405 */       PublicKey publicKey = this._$2466.getCertificate(alias).getPublicKey();
/*     */ 
/* 407 */       return _$3082(publicKey, signData, orgData, "MD5withRSA");
/*     */     }
/*     */     catch (Exception e) {
/* 410 */       e.printStackTrace();
/* 411 */     }return false;
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.core.KeyStoreUtil
 * JD-Core Version:    0.6.2
 */