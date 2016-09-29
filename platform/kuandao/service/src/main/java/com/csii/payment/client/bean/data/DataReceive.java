/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import com.csii.payment.client.exception.CsiiException;
/*    */ 
/*    */ public abstract class DataReceive
/*    */ {
/*    */   String status;
/*    */   String errDesc;
/*    */ 
/*    */   public String getStatus()
/*    */   {
/* 17 */     return this.status; } 
/* 18 */   public String getErrDesc() { return this.errDesc; } 
/* 19 */   public void setStatus(String newStatus) { this.status = newStatus; } 
/* 20 */   public void setErrDesc(String newErrDesc) { this.errDesc = newErrDesc; }
/*    */ 
/*    */ 
/*    */   protected abstract void setRecvData(String paramString)
/*    */     throws CsiiException;
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.DataReceive
 * JD-Core Version:    0.6.2
 */