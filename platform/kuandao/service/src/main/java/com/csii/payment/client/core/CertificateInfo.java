/*    */ package com.csii.payment.client.core;
/*    */ 
/*    */ import java.security.cert.Certificate;
/*    */ import java.util.Date;
/*    */ 
/*    */ class CertificateInfo
/*    */ {
/*    */   private String _$2478;
/*    */   private Date _$2705;
/*    */   private Certificate _$2714;
/*    */   private Certificate[] _$2715;
/*    */ 
/*    */   String getAlias()
/*    */   {
/* 25 */     return this._$2478;
/*    */   }
/*    */ 
/*    */   Certificate getCertificate()
/*    */   {
/* 33 */     return this._$2714;
/*    */   }
/*    */ 
/*    */   Certificate[] getCertificateChain()
/*    */   {
/* 41 */     return this._$2715;
/*    */   }
/*    */ 
/*    */   Date getCreationDate()
/*    */   {
/* 49 */     return this._$2705;
/*    */   }
/*    */ 
/*    */   public void setAlias(String newAlias)
/*    */   {
/* 57 */     this._$2478 = newAlias;
/*    */   }
/*    */ 
/*    */   public void setCertificate(Certificate newCertificate)
/*    */   {
/* 65 */     this._$2714 = newCertificate;
/*    */   }
/*    */ 
/*    */   public void setCertificateChain(Certificate[] newCertificateChain)
/*    */   {
/* 73 */     this._$2715 = newCertificateChain;
/*    */   }
/*    */ 
/*    */   public void setCreationDate(Date newCreationDate)
/*    */   {
/* 81 */     this._$2705 = newCreationDate;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.core.CertificateInfo
 * JD-Core Version:    0.6.2
 */