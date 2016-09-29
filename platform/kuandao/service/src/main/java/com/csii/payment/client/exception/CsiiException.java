/*    */ package com.csii.payment.client.exception;
/*    */ 
/*    */ public class CsiiException extends Exception
/*    */ {
/*    */   private String _$1565;
/*    */   private String _$1570;
/*    */ 
/*    */   public CsiiException()
/*    */   {
/* 13 */     this._$1565 = "";
/*    */   }
/*    */ 
/*    */   public CsiiException(int i)
/*    */   {
/* 18 */     this(Integer.toString(i));
/*    */   }
/*    */ 
/*    */   public CsiiException(String s)
/*    */   {
/* 23 */     super(s);
/* 24 */     this._$1565 = "";
/* 25 */     this._$1565 = s;
/*    */   }
/*    */ 
/*    */   public String getErrorCode()
/*    */   {
/* 30 */     return this._$1565;
/*    */   }
/*    */ 
/*    */   public String getRelative()
/*    */   {
/* 35 */     return this._$1570;
/*    */   }
/*    */ 
/*    */   public void setRelative(String s)
/*    */   {
/* 40 */     this._$1570 = s;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.exception.CsiiException
 * JD-Core Version:    0.6.2
 */