/*    */ package com.csii.payment.client.bean.data;
/*    */ 
/*    */ import com.csii.payment.client.exception.CsiiException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class AllOrderListReceive extends DataReceive
/*    */ {
/*    */   private int _$1116;
/*    */   private double _$1388;
/*    */   private ArrayList _$1389;
/*    */ 
/*    */   public int getCount()
/*    */   {
/* 21 */     return this._$1116; } 
/* 22 */   public double getSum() { return this._$1388; } 
/* 23 */   public ArrayList getOrders() { return this._$1389; }
/*    */ 
/*    */   public void setRecvData(String recv) throws CsiiException {
/* 26 */     if ((this.status == null) || (!this.status.equals("0"))) {
/* 27 */       return;
/*    */     }
/* 29 */     if (recv.length() < 22) {
/* 30 */       System.err.println("error in SettledOrderListReceive.setRecvData方法：");
/* 31 */       System.err.println("接受的数据长度小于22位（订单数量 + 订单总金额）");
/* 32 */       System.err.println(recv);
/*    */     }
/*    */ 
/* 35 */     StringTokenizer st = new StringTokenizer(recv, "~");
/*    */ 
/* 37 */     if (st.hasMoreTokens())
/* 38 */       this._$1116 = Integer.parseInt(st.nextToken());
/*    */     else {
/* 40 */       throw new CsiiException("取订单数量时，返回报文格式错");
/*    */     }
/* 42 */     if (st.hasMoreTokens())
/* 43 */       this._$1388 = Double.parseDouble(st.nextToken());
/*    */     else {
/* 45 */       throw new CsiiException("取订单总金额时，返回报文格式错");
/*    */     }
/* 47 */     this._$1389 = new ArrayList();
/*    */ 
/* 49 */     if (this._$1116 == 0) {
/* 50 */       return;
/*    */     }
/* 52 */     HashMap order = null;
/* 53 */     int cycle = 0;
/*    */ 
/* 55 */     while (st.hasMoreTokens()) {
/* 56 */       switch (cycle % 7) { case 0:
/* 57 */         order = new HashMap(); String oid = st.nextToken(); order.put("orderId", oid == null ? oid : oid.trim()); break;
/*    */       case 1:
/* 58 */         order.put("amount", st.nextElement()); break;
/*    */       case 2:
/* 59 */         order.put("merchantDate", st.nextElement()); break;
/*    */       case 3:
/* 60 */         order.put("payDate", st.nextElement()); break;
/*    */       case 4:
/* 61 */         order.put("commitDate", st.nextElement()); break;
/*    */       case 5:
/* 62 */         order.put("seqNo", st.nextElement()); break;
/*    */       case 6:
/* 63 */         order.put("commitFlag", st.nextElement()); this._$1389.add(order);
/*    */       }
/* 65 */       cycle++;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.bean.data.AllOrderListReceive
 * JD-Core Version:    0.6.2
 */