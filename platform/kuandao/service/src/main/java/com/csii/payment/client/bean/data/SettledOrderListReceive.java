/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class SettledOrderListReceive extends DataReceive
/*    */ {
/*    */   private int _$1116;
/*    */   private double _$1388;
/*    */   private ArrayList _$1389;
/*    */ 
/*    */   public int getCount()
/*    */   {
/* 18 */     return this._$1116; } 
/* 19 */   public double getSum() { return this._$1388; } 
/* 20 */   public ArrayList getOrders() { return this._$1389; }
/*    */ 
/*    */   public void setRecvData(String recv) {
/* 23 */     if ((this.status == null) || (!this.status.equals("0"))) {
/* 24 */       return;
/*    */     }
/* 26 */     if (recv.length() < 22) {
/* 27 */       System.err.println("error in SettledOrderListReceive.setRecvData方法：");
/* 28 */       System.err.println("接受的数据长度小于22位（订单数量 + 订单总金额）");
/* 29 */       System.err.println(recv);
/*    */     }
/*    */ 
/* 32 */     StringTokenizer st = new StringTokenizer(recv, "~");
/*    */ 
/* 34 */     if (st.hasMoreTokens()) {
/* 35 */       this._$1116 = Integer.parseInt(st.nextToken());
/*    */     }
/* 37 */     if (st.hasMoreTokens()) {
/* 38 */       this._$1388 = Double.parseDouble(st.nextToken());
/*    */     }
/* 40 */     this._$1389 = new ArrayList();
/*    */ 
/* 42 */     if (this._$1116 == 0) {
/* 43 */       return;
/*    */     }
/* 45 */     HashMap order = null;
/* 46 */     int cycle = 0;
/*    */ 
/* 48 */     while (st.hasMoreTokens()) {
/* 49 */       switch (cycle % 6) { case 0:
/* 50 */         order = new HashMap(); String oid = st.nextToken(); order.put("orderId", oid == null ? oid : oid.trim()); break;
/*    */       case 1:
/* 51 */         order.put("amount", st.nextElement()); break;
/*    */       case 2:
/* 52 */         order.put("payDate", st.nextElement()); break;
/*    */       case 3:
/* 53 */         order.put("merchantDate", st.nextElement()); break;
/*    */       case 4:
/* 54 */         order.put("seqNo", st.nextElement()); break;
/*    */       case 5:
/* 55 */         order.put("commitDate", st.nextElement()); this._$1389.add(order);
/*    */       }
/* 57 */       cycle++;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.SettledOrderListReceive
 * JD-Core Version:    0.6.2
 */