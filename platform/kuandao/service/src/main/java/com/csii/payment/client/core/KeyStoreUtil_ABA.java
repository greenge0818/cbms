/*     */ package com.csii.payment.client.core;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Security;
/*     */ import java.security.Signature;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.CipherOutputStream;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ 
/*     */ class KeyStoreUtil_ABA
/*     */ {
/*  17 */   private Hashtable _$2463 = new Hashtable();
/*     */ 
/*  19 */   private Hashtable _$2464 = new Hashtable();
/*     */   private KeyStore _$2466;
/*     */   private InputStream _$2467;
/*     */ 
/*     */   public KeyStoreUtil_ABA(String keyName, String storePassword)
/*     */     throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException
/*     */   {
/*  34 */     if (keyName == null) {
/*  35 */       keyName = System.getProperty("JAVA_HOME") + "/jre/lib/security/cacerts";
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  40 */       this._$2467 = new FileInputStream(keyName);
/*     */     } catch (FileNotFoundException e) {
/*  42 */       System.err.println("File Not Found");
/*  43 */       e.printStackTrace();
/*  44 */       throw e;
/*     */     }
/*     */ 
/*  47 */     char[] pass = storePassword == null ? null : storePassword.toCharArray();
/*     */     try
/*     */     {
/*  52 */       this._$2466 = KeyStore.getInstance("JKS");
/*  53 */       this._$2466.load(this._$2467, pass);
/*     */     }
/*     */     catch (KeyStoreException e1) {
/*  56 */       e1.printStackTrace();
/*  57 */       throw e1;
/*     */     } catch (NoSuchAlgorithmException e2) {
/*  59 */       e2.printStackTrace();
/*  60 */       throw e2;
/*     */     }
/*     */ 
/*  63 */     Enumeration enu = this._$2466.aliases();
/*     */ 
/*  65 */     while (enu.hasMoreElements()) {
/*  66 */       String alias = (String)enu.nextElement();
/*     */ 
/*  68 */       if (this._$2466.isCertificateEntry(alias)) {
/*  69 */         CertificateInfo ci = new CertificateInfo();
/*  70 */         ci.setAlias(alias);
/*  71 */         ci.setCertificate(this._$2466.getCertificate(alias));
/*  72 */         ci.setCertificateChain(this._$2466.getCertificateChain(alias));
/*  73 */         ci.setCreationDate(this._$2466.getCreationDate(alias));
/*  74 */         this._$2464.put(alias, ci);
/*     */       }
/*     */ 
/*  78 */       if (this._$2466.isKeyEntry(alias)) {
/*  79 */         KeyInfo ki = new KeyInfo();
/*  80 */         ki.setAlias(alias);
/*  81 */         ki.setCreationDate(this._$2466.getCreationDate(alias));
/*  82 */         this._$2463.put(alias, ki);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public KeyStoreUtil_ABA(KeyStore keyStore)
/*     */     throws KeyStoreException
/*     */   {
/*  95 */     this._$2466 = keyStore;
/*     */ 
/*  97 */     Enumeration enu = keyStore.aliases();
/*     */ 
/*  99 */     while (enu.hasMoreElements()) {
/* 100 */       String alias = (String)enu.nextElement();
/*     */ 
/* 102 */       if (keyStore.isCertificateEntry(alias)) {
/* 103 */         CertificateInfo ci = new CertificateInfo();
/* 104 */         ci.setAlias(alias);
/* 105 */         ci.setCertificate(keyStore.getCertificate(alias));
/* 106 */         ci.setCertificateChain(keyStore.getCertificateChain(alias));
/* 107 */         ci.setCreationDate(keyStore.getCreationDate(alias));
/* 108 */         this._$2464.put(alias, ci);
/*     */       }
/*     */ 
/* 112 */       if (keyStore.isKeyEntry(alias)) {
/* 113 */         KeyInfo ki = new KeyInfo();
/* 114 */         ki.setAlias(alias);
/* 115 */         ki.setCreationDate(keyStore.getCreationDate(alias));
/* 116 */         this._$2463.put(alias, ki);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String byteToHex(byte[] inbuf)
/*     */   {
/* 133 */     StringBuffer strBuf = new StringBuffer();
/*     */ 
/* 135 */     for (int i = 0; i < inbuf.length; i++) {
/* 136 */       String byteStr = Integer.toHexString(inbuf[i] & 0xFF);
/* 137 */       if (byteStr.length() != 2)
/* 138 */         strBuf.append('0').append(byteStr);
/*     */       else {
/* 140 */         strBuf.append(byteStr);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 145 */     return new String(strBuf);
/*     */   }
/*     */ 
/*     */   public void creatCertificateRequest()
/*     */     throws Exception
/*     */   {
/* 153 */     KeyPairGenerator keyGen = KeyPairGenerator.getInstance("MD5withRSA");
/* 154 */     keyGen.initialize(1024, new SecureRandom());
/* 155 */     KeyPair pair = keyGen.generateKeyPair();
/*     */   }
/*     */ 
/*     */   public void creatKeyStore()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertificateInfo getCertificateEntry(String alias)
/*     */   {
/* 171 */     return (CertificateInfo)this._$2464.get(alias);
/*     */   }
/*     */ 
/*     */   public int getCertificateEntrySize()
/*     */   {
/* 179 */     return this._$2464.size();
/*     */   }
/*     */ 
/*     */   public KeyInfo getKeyEntry(String alias)
/*     */   {
/* 188 */     return (KeyInfo)this._$2463.get(alias);
/*     */   }
/*     */ 
/*     */   public int getKeyEntrySize()
/*     */   {
/* 196 */     return this._$2463.size();
/*     */   }
/*     */ 
/*     */   public KeyStore getKeyStore()
/*     */   {
/* 204 */     return this._$2466;
/*     */   }
/*     */ 
/*     */   private PrivateKey _$2511(String alias, String password)
/*     */   {
/* 215 */     if ((alias == null) || (password == null))
/* 216 */       return null;
/*     */     try
/*     */     {
/* 219 */       return (PrivateKey)this._$2466.getKey(alias, password.toCharArray());
/*     */     } catch (Exception e) {
/* 221 */       e.printStackTrace();
/* 222 */     }return null;
/*     */   }
/*     */ 
/*     */   public static byte[] hexToByte(String inbuf)
/*     */   {
/* 235 */     int len = inbuf.length() / 2;
/* 236 */     byte[] outbuf = new byte[len];
/*     */ 
/* 238 */     for (int i = 0; i < len; i++) {
/* 239 */       String tmpbuf = inbuf.substring(i * 2, i * 2 + 2);
/*     */ 
/* 241 */       outbuf[i] = ((byte)Integer.parseInt(tmpbuf, 16));
/*     */     }
/*     */ 
/* 245 */     return outbuf;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 254 */     KeyStoreUtil_ABA ks = null;
/*     */     try {
/* 256 */       ks = new KeyStoreUtil_ABA("c:/merchant_test.jks", "111111");
/*     */     } catch (Exception e) {
/* 258 */       e.printStackTrace();
/* 259 */       return;
/*     */     }
/*     */ 
/* 265 */     Key key = ks._$2511("merchant_key", "111111");
/* 266 */     System.err.println("Key :" + key.getClass().getName());
/*     */ 
/* 268 */     if ((key instanceof RSAPrivateKey))
/* 269 */       System.err.println("java.security.interfaces.RSAPrivateKey");
/*     */   }
/*     */ 
/*     */   public String payGateSignData(String data)
/*     */   {
/* 280 */     ResourceBundle rb = ResourceBundle.getBundle("paygate");
/* 281 */     String alias = rb.getString("alias");
/* 282 */     String password = rb.getString("password");
/*     */ 
/* 284 */     return signDataRSA(alias, password, data);
/*     */   }
/*     */ 
/*     */   public boolean payGateVerifyData(String mer_id, String signData, String orgData)
/*     */   {
/* 299 */     return verifyDataRSA(mer_id, signData, orgData);
/*     */   }
/*     */ 
/*     */   private String _$3066(PrivateKey privateKey, String data, String alg)
/*     */   {
/*     */     try
/*     */     {
/* 311 */       Signature dsa = Signature.getInstance(alg);
/*     */ 
/* 313 */       dsa.initSign(privateKey);
/*     */ 
/* 315 */       dsa.update(data.getBytes("GBK"));
/*     */ 
/* 317 */       byte[] sig = dsa.sign();
/*     */ 
/* 319 */       return byteToHex(sig);
/*     */     }
/*     */     catch (Exception e) {
/* 322 */       e.printStackTrace();
/* 323 */     }return null;
/*     */   }
/*     */ 
/*     */   private String _$3071(PrivateKey privateKey, String data, String alg)
/*     */   {
/* 345 */     Security.addProvider(new BouncyCastleProvider());
/*     */ 
/* 347 */     SecureRandom rand = new SecureRandom();
/* 348 */     long cost = System.currentTimeMillis();
/*     */     Cipher out;
/*     */     try
/*     */     {
/* 351 */       KeyFactory fact = KeyFactory.getInstance("RSA", "ABA");
/* 352 */       Cipher in = Cipher.getInstance("RSA/ECB/PKCS1Padding", "ABA");
/* 353 */       out = Cipher.getInstance("RSA/ECB/PKCS1Padding", "ABA");
/* 354 */       out.init(1, privateKey);
/*     */     } catch (Exception e) {
/* 356 */       System.err.println(e.getMessage());
/* 357 */       e.printStackTrace(System.err);
/* 358 */       System.exit(2);
/* 359 */       return null;
/*     */     }
/*     */ 
/* 367 */     ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/*     */ 
/* 369 */     CipherOutputStream cOut = new CipherOutputStream(bOut, out);
/*     */ 
/* 371 */     byte[] bytes = new byte[data.length()];
/*     */ 
/* 373 */     for (int i = 0; i != bytes.length; i++) {
/* 374 */       bytes[i] = ((byte)data.charAt(i));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 381 */       cOut.write(bytes);
/* 382 */       cOut.close();
/*     */     } catch (IOException e) {
/* 384 */       System.err.println(e.getMessage());
/*     */ 
/* 386 */       return null;
/*     */     }
/* 388 */     bytes = bOut.toByteArray();
/*     */ 
/* 390 */     System.out.println("enc:  " + byteToHex(bytes));
/*     */ 
/* 392 */     System.out.println("Cost time " + (System.currentTimeMillis() - cost));
/*     */ 
/* 394 */     return byteToHex(bytes);
/*     */   }
/*     */ 
/*     */   public String signDataRSA(String alias, String password, String data)
/*     */   {
/* 404 */     if ((alias == null) || (password == null) || (data == null)) {
/* 405 */       return null;
/*     */     }
/* 407 */     PrivateKey privateKey = _$2511(alias, password);
/*     */ 
/* 409 */     if (privateKey == null) {
/* 410 */       System.err.println("The alias or password is wrong");
/* 411 */       return null;
/*     */     }
/*     */ 
/* 414 */     if (!(privateKey instanceof RSAPrivateKey))
/*     */     {
/* 416 */       System.err.println("The key is not RSAPrivateKey");
/* 417 */       System.err.println("The class should Run under ABA");
/*     */     }
/*     */ 
/* 421 */     return _$3066(privateKey, data, "MD5withRSA");
/*     */   }
/*     */ 
/*     */   private boolean _$3082(PublicKey publicKey, String signData, String orgData, String alg)
/*     */   {
/*     */     try
/*     */     {
/* 440 */       if ((publicKey == null) || (signData == null) || (orgData == null) || (alg == null))
/*     */       {
/* 444 */         System.err.println("Error:in KeyStoreUtil.verifyData() . publicKey or signData or orgData or alg is null");
/*     */ 
/* 446 */         return false;
/*     */       }
/*     */ 
/* 449 */       Signature dsa = Signature.getInstance(alg);
/*     */ 
/* 451 */       dsa.initVerify(publicKey);
/*     */ 
/* 453 */       dsa.update(orgData.getBytes("GBK"));
/*     */ 
/* 455 */       byte[] sig = hexToByte(signData);
/* 456 */       return dsa.verify(sig);
/*     */     }
/*     */     catch (Exception e) {
/* 459 */       e.printStackTrace();
/* 460 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean verifyDataRSA(String alias, String signData, String orgData)
/*     */   {
/*     */     try
/*     */     {
/* 478 */       if ((alias == null) || (signData == null) || (orgData == null)) {
/* 479 */         System.err.println("Error:in KeyStoreUtil.verifyDataRSA() . alias or signData or orgData is null");
/*     */ 
/* 481 */         return false;
/*     */       }
/*     */ 
/* 484 */       System.err.println(signData);
/* 485 */       System.err.println(orgData);
/*     */ 
/* 487 */       PublicKey publicKey = this._$2466.getCertificate(alias).getPublicKey();
/*     */ 
/* 489 */       return _$3082(publicKey, signData, orgData, "MD5withRSA");
/*     */     }
/*     */     catch (Exception e) {
/* 492 */       e.printStackTrace();
/* 493 */     }return false;
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.core.KeyStoreUtil_ABA
 * JD-Core Version:    0.6.2
 */