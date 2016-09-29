/*    */ package com.csii.payment.client.util;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.security.KeyPair;
/*    */ import java.security.KeyPairGenerator;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.PublicKey;
/*    */ import java.security.SecureRandom;
/*    */ 
/*    */ public class KeyManager
/*    */ {
/*  8 */   static String keyAlgorithm = "DSA";
/*    */ 
/*    */   public static void createKeyPair(String keyAlgorithm, String keyPath, String keyName)
/*    */   {
/*    */     try
/*    */     {
/* 20 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
/*    */ 
/* 22 */       keyGen.initialize(1024, new SecureRandom());
/* 23 */       KeyPair pair = keyGen.generateKeyPair();
/*    */ 
/* 25 */       PrivateKey priv = pair.getPrivate();
/* 26 */       PublicKey pub = pair.getPublic();
/*    */ 
/* 28 */       FileOutputStream ostream = new FileOutputStream(keyPath + keyName + ".private");
/*    */ 
/* 30 */       ObjectOutputStream p = new ObjectOutputStream(ostream);
/* 31 */       p.writeObject(priv);
/* 32 */       p.close();
/* 33 */       ostream = new FileOutputStream(keyPath + keyName + ".public");
/* 34 */       p = new ObjectOutputStream(ostream);
/* 35 */       p.writeObject(pub);
/* 36 */       p.close();
/*    */ 
/* 39 */       byte[] privBytes = priv.getEncoded();
/*    */ 
/* 43 */       byte[] pubBytes = pub.getEncoded();
/* 44 */       ostream = new FileOutputStream(keyPath + keyName + ".priraw");
/* 45 */       ostream.write(privBytes);
/* 46 */       ostream.close();
/* 47 */       ostream = new FileOutputStream(keyPath + keyName + ".pubraw");
/* 48 */       ostream.write(pubBytes);
/* 49 */       ostream.close();
/*    */     } catch (Exception e) {
/* 51 */       System.err.println("Caught exception " + e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static KeyPair getKeyPair(String keyPath, String keyName)
/*    */   {
/*    */     try
/*    */     {
/* 59 */       FileInputStream istream = new FileInputStream(keyPath + keyName + ".private");
/*    */ 
/* 61 */       ObjectInputStream p = new ObjectInputStream(istream);
/* 62 */       PrivateKey priv = (PrivateKey)p.readObject();
/* 63 */       p.close();
/* 64 */       istream = new FileInputStream(keyPath + keyName + ".public");
/* 65 */       p = new ObjectInputStream(istream);
/* 66 */       PublicKey pub = (PublicKey)p.readObject();
/* 67 */       p.close();
/* 68 */       return new KeyPair(pub, priv);
/*    */     }
/*    */     catch (Exception e) {
/* 71 */       System.err.println("Caught exception " + e);
/* 72 */     }return null;
/*    */   }
/*    */ 
/*    */   public static PrivateKey getPrivateKey(String keyPath, String keyName) {
/*    */     try {
/* 77 */       FileInputStream istream = new FileInputStream(keyPath + keyName + ".private");
/*    */ 
/* 79 */       ObjectInputStream p = new ObjectInputStream(istream);
/* 80 */       PrivateKey priv = (PrivateKey)p.readObject();
/* 81 */       p.close();
/* 82 */       return priv;
/*    */     } catch (Exception e) {
/* 84 */       System.err.println("Caught exception ");
/* 85 */     }return null;
/*    */   }
/*    */ 
/*    */   public static PublicKey getPublicKey(String keyPath, String keyName) {
/*    */     try {
/* 90 */       FileInputStream istream = new FileInputStream(keyPath + keyName + ".public");
/*    */ 
/* 92 */       ObjectInputStream p = new ObjectInputStream(istream);
/* 93 */       PublicKey pub = (PublicKey)p.readObject();
/* 94 */       p.close();
/* 95 */       return pub;
/*    */     } catch (Exception e) {
/* 97 */       System.err.println("Caught exception " + e);
/* 98 */     }return null;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.KeyManager
 * JD-Core Version:    0.6.2
 */