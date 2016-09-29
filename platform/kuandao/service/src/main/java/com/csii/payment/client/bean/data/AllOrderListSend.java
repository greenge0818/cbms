/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import java.sql.Date;
/*    */ import java.text.SimpleDateFormat;
/*    */ 
/*    */ public class AllOrderListSend
/*    */ {
/*    */   private Date _$618;
/*    */   private Date _$619;
/*    */ 
/*    */   public AllOrderListSend(Date begin, Date end)
/*    */   {
/* 19 */     this._$618 = begin;
/* 20 */     this._$619 = end;
/*    */   }
/*    */   public Date getBeginDate() {
/* 23 */     return this._$618; } 
/* 24 */   public void setBeginDate(Date newBeginDate) { this._$618 = newBeginDate; } 
/*    */   public Date getEngDate() {
/* 26 */     return this._$619; } 
/* 27 */   public void setEndDate(Date newEndDate) { this._$619 = newEndDate; }
/*    */ 
/*    */   public String getSendData() {
/* 30 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/* 31 */     return "beginDate=" + sdf.format(this._$618) + "|endDate=" + sdf.format(this._$619);
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.AllOrderListSend
 * JD-Core Version:    0.6.2
 */