/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ 
/*    */ public class UnsettledRefundSend
/*    */ {
/*    */   private String _$520;
/*    */   private String _$521;
/*    */   private String _$523;
/*    */ 
/*    */   public UnsettledRefundSend(String newOrderId, String newSeqNo, String newMerchantDate)
/*    */   {
/* 20 */     this._$520 = newOrderId;
/* 21 */     this._$521 = newSeqNo;
/* 22 */     this._$523 = newMerchantDate;
/*    */   }
/*    */   public String getOrderId() {
/* 25 */     return this._$520; } 
/* 26 */   public void setOrderId(String newOrderId) { this._$520 = newOrderId; } 
/*    */   public String getMerchantDate() {
/* 28 */     return this._$523; } 
/* 29 */   public void setMerchantDate(String newMerchantDate) { this._$523 = newMerchantDate; } 
/*    */   public String getSeqNo() {
/* 31 */     return this._$521; } 
/* 32 */   public void setSeqNo(String newSeqNo) { this._$521 = newSeqNo; }
/*    */ 
/*    */   public String getSendData() {
/* 35 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/* 36 */     return "orderId=" + this._$520 + "|seqNo=" + this._$521 + "|merchantDate=" + this._$523;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.UnsettledRefundSend
 * JD-Core Version:    0.6.2
 */