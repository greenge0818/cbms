/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import java.sql.Date;
/*    */ 
/*    */ public class FullRefundSend
/*    */ {
/*    */   private String _$520;
/*    */   private String _$521;
/*    */   private Date _$523;
/*    */ 
/*    */   public FullRefundSend(String newOrderId, String newSeqNo, Date newMerchantDate)
/*    */   {
/* 19 */     this._$520 = newOrderId;
/* 20 */     this._$521 = newSeqNo;
/* 21 */     this._$523 = newMerchantDate;
/*    */   }
/*    */ 
/*    */   public String getOrderId() {
/* 25 */     return this._$520;
/*    */   }
/*    */   public void setOrderId(String newOrderId) {
/* 28 */     this._$520 = newOrderId;
/*    */   }
/*    */ 
/*    */   public Date getMerchantDate() {
/* 32 */     return this._$523;
/*    */   }
/*    */   public void setMerchantDate(Date newMerchantDate) {
/* 35 */     this._$523 = newMerchantDate;
/*    */   }
/*    */ 
/*    */   public String getSeqNo() {
/* 39 */     return this._$521;
/*    */   }
/*    */   public void setSeqNo(String newSeqNo) {
/* 42 */     this._$521 = newSeqNo;
/*    */   }
/*    */ 
/*    */   public String getSendData() {
/* 46 */     return "orderId=" + this._$520 + "|seqNo=" + this._$521 + "|merchantDate=" + this._$523;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.FullRefundSend
 * JD-Core Version:    0.6.2
 */