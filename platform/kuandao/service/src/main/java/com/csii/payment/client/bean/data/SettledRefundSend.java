/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ 
/*    */ public class SettledRefundSend
/*    */ {
/*    */   private String _$520;
/*    */   private String _$521;
/*    */   private String _$2419;
/*    */   private String _$523;
/*    */ 
/*    */   public SettledRefundSend(String newOrderId, String newSeqNo, String newAmount, String newMerchantDate)
/*    */   {
/* 21 */     this._$520 = newOrderId;
/* 22 */     this._$521 = newSeqNo;
/* 23 */     this._$2419 = newAmount;
/* 24 */     this._$523 = newMerchantDate;
/*    */   }
/*    */   public String getOrderId() {
/* 27 */     return this._$520; } 
/* 28 */   public void setOrderId(String newOrderId) { this._$520 = newOrderId; } 
/*    */   public String getSeqNo() {
/* 30 */     return this._$521; } 
/* 31 */   public void setSeqNo(String newSeqNo) { this._$521 = newSeqNo; } 
/*    */   public String getMerchantDate() {
/* 33 */     return this._$523; } 
/* 34 */   public void setMerchantDate(String newMerchantDate) { this._$523 = newMerchantDate; }
/*    */ 
/*    */   public String getSendData() {
/* 37 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/* 38 */     return "orderId=" + this._$520 + "|seqNo=" + this._$521 + "|amount=" + this._$2419 + "|merchantDate=" + this._$523;
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.SettledRefundSend
 * JD-Core Version:    0.6.2
 */