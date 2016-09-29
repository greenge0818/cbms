/*    */ package com.csii.payment.client.util;
/*    */ 
/*    */ import com.csii.payment.client.bean.data.AllOrderListReceive;
/*    */ import com.csii.payment.client.bean.data.AllOrderListSend;
/*    */ import com.csii.payment.client.bean.data.SettledOrderListReceive;
/*    */ import com.csii.payment.client.bean.data.SettledOrderListSend;
/*    */ import com.csii.payment.client.bean.data.UnsettledOrderListReceive;
/*    */ import com.csii.payment.client.bean.data.UnsettledOrderListSend;
/*    */ import com.csii.payment.client.core.PaymentInterfaceUtil;
/*    */ import com.csii.payment.client.exception.CsiiException;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.Date;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class DemoMerchant
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws CsiiException
/*    */   {
/* 23 */     PaymentInterfaceUtil util = new PaymentInterfaceUtil();
/* 24 */     util.login("tian01", "111111");
/*    */ 
/* 26 */     UnsettledOrderListSend send2 = new UnsettledOrderListSend(Date.valueOf("2003-09-01"), Date.valueOf("2003-09-03"));
/*    */ 
/* 30 */     UnsettledOrderListReceive rec2 = util.getUnsettledList(send2);
/* 31 */     System.out.println("unsettled order list=" + rec2.getCount());
/*    */ 
/* 33 */     SettledOrderListSend send3 = new SettledOrderListSend(Date.valueOf("2003-09-01"), Date.valueOf("2003-09-03"));
/*    */ 
/* 37 */     SettledOrderListReceive rec3 = util.getSettledList(send3);
/* 38 */     System.out.println("settled order list=" + rec3.getCount());
/*    */ 
/* 40 */     AllOrderListSend send4 = new AllOrderListSend(Date.valueOf("2003-09-01"), Date.valueOf("2003-09-03"));
/*    */ 
/* 44 */     AllOrderListReceive rec4 = util.getAllOrderList(send4);
/* 45 */     System.out.println("all order list count=" + rec4.getCount());
/* 46 */     ArrayList allOrders = rec4.getOrders();
/* 47 */     for (int i = 0; i < allOrders.size(); i++) {
/* 48 */       System.out.println((HashMap)allOrders.get(i));
/*    */     }
/*    */ 
/* 56 */     System.out.println("end of demo");
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.DemoMerchant
 * JD-Core Version:    0.6.2
 */