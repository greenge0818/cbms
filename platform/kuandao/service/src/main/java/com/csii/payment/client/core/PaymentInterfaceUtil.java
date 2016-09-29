/*     */ package com.csii.payment.client.core;
/*     */ 
/*     */ import com.csii.payment.client.bean.data.AllOrderListReceive;
/*     */ import com.csii.payment.client.bean.data.AllOrderListSend;
/*     */ import com.csii.payment.client.bean.data.DataReceive;
/*     */ import com.csii.payment.client.bean.data.FullRefundReceive;
/*     */ import com.csii.payment.client.bean.data.FullRefundSend;
/*     */ import com.csii.payment.client.bean.data.SettledOrderListReceive;
/*     */ import com.csii.payment.client.bean.data.SettledOrderListSend;
/*     */ import com.csii.payment.client.bean.data.SettledRefundReceive;
/*     */ import com.csii.payment.client.bean.data.SettledRefundSend;
/*     */ import com.csii.payment.client.bean.data.UnsettledOrderListReceive;
/*     */ import com.csii.payment.client.bean.data.UnsettledOrderListSend;
/*     */ import com.csii.payment.client.bean.data.UnsettledRefundReceive;
/*     */ import com.csii.payment.client.bean.data.UnsettledRefundSend;
/*     */ import com.csii.payment.client.exception.CsiiConnectionException;
/*     */ import com.csii.payment.client.exception.CsiiException;
/*     */ import com.csii.payment.client.exception.CsiiHostException;
/*     */ import com.csii.payment.client.util.HttpsPoster;
/*     */ import com.csii.payment.client.util.InputCheckTool;
/*     */ import com.sun.net.ssl.HttpsURLConnection;
/*     */ import com.sun.net.ssl.KeyManagerFactory;
/*     */ import com.sun.net.ssl.SSLContext;
/*     */ import com.sun.net.ssl.TrustManagerFactory;
/*     */ import com.sun.net.ssl.internal.ssl.Provider;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ConnectException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Security;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ public class PaymentInterfaceUtil
/*     */ {
/*  52 */   private static ResourceBundle _$2530 = ResourceBundle.getBundle("merchant");
/*     */   private String _$2566;
/*     */   private String _$4292;
/*     */   private static SSLSocketFactory _$2568;
/*  49 */   private static boolean _$4293 = false;
/*     */   private static String _$4294;
/*     */ 
/*     */   public PaymentInterfaceUtil()
/*     */     throws CsiiException
/*     */   {
/*  56 */     if (_$4293)
/*  57 */       throw new CsiiException(_$4294);
/*     */   }
/*     */ 
/*     */   public void login(String userName, String password)
/*     */     throws CsiiException
/*     */   {
/*     */     try
/*     */     {
/*  68 */       String mode = _$2530.getString("development");
/*     */ 
/*  70 */       if ((mode != null) && (mode.equals("yes")));
/* 103 */       Security.addProvider(new Provider());
/* 104 */       System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
/*     */ 
/* 108 */       URL url = new URL(_$4310("firstUrl"));
/* 109 */       HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
/* 110 */       conn.setSSLSocketFactory(_$4311());
/* 111 */       conn.setRequestMethod("POST");
/* 112 */       conn.connect();
/* 113 */       String cookie = conn.getHeaderField("Set-Cookie");
/* 114 */       System.err.println(cookie);
/* 115 */       this._$4292 = (cookie == null ? null : cookie.substring(0, cookie.indexOf(";")));
/* 116 */       conn.disconnect();
/*     */ 
/* 119 */       URL url2 = new URL(_$4310("loginUrl") + "&LoginId=" + userName + "&Password=" + password);
/* 120 */       HttpsURLConnection conn2 = (HttpsURLConnection)url2.openConnection();
/* 121 */       conn2.setSSLSocketFactory(_$4311());
/* 122 */       conn2.setRequestProperty("Cookie", this._$4292);
/* 123 */       conn2.setRequestProperty("LoginId", userName);
/* 124 */       conn2.setRequestProperty("Password", password);
/* 125 */       conn2.setRequestProperty("transName", "EntLogin");
/* 126 */       conn2.setRequestMethod("GET");
/* 127 */       conn2.connect();
/*     */ 
/* 130 */       BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
/* 131 */       String aLine2 = null;
/*     */ 
/* 134 */       String temp = reader2.readLine();
/* 135 */       String orig = null;
/* 136 */       String sign = null;
/* 137 */       if (temp.length() != 0)
/* 138 */         sign = temp.substring(temp.indexOf("=") + 1);
/* 139 */       temp = reader2.readLine();
/* 140 */       orig = temp.substring(temp.indexOf("=") + 1);
/* 141 */       Properties fromProps = InputCheckTool.parseStringToProperties(orig, "|");
/* 142 */       String status = ((String)fromProps.get("status")).trim();
/* 143 */       String errDesc = ((String)fromProps.get("errdesc")).trim();
/* 144 */       if (!status.equals("0"))
/* 145 */         throw new CsiiException(errDesc);
/* 146 */       System.out.println(" login ok ");
/*     */     }
/*     */     catch (ConnectException e)
/*     */     {
/* 151 */       e.printStackTrace();
/* 152 */       throw new CsiiException("连接支付网关失败");
/*     */     }
/*     */     catch (MalformedURLException e) {
/* 155 */       e.printStackTrace();
/* 156 */       throw new CsiiException("连接支付网关地址格式错");
/*     */     }
/*     */     catch (IOException e) {
/* 159 */       e.printStackTrace();
/* 160 */       throw new CsiiException("io读写错");
/*     */     }
/*     */ 
/* 163 */     _$4314(userName);
/*     */   }
/*     */ 
/*     */   private void _$4314(String name)
/*     */   {
/* 168 */     this._$2566 = name;
/*     */   }
/*     */ 
/*     */   public SettledOrderListReceive getSettledList(SettledOrderListSend request)
/*     */     throws CsiiException
/*     */   {
/* 177 */     if (this._$2566 == null) {
/* 178 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 180 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 182 */     SettledOrderListReceive recv = new SettledOrderListReceive();
/* 183 */     String recvData = _$4317(_$2530.getString("settledListUrl"), sendData, recv);
/*     */ 
/* 185 */     recv.setRecvData(recvData);
/* 186 */     return recv;
/*     */   }
/*     */ 
/*     */   public AllOrderListReceive getAllOrderList(AllOrderListSend request)
/*     */     throws CsiiException
/*     */   {
/* 194 */     if (this._$2566 == null) {
/* 195 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 197 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 199 */     AllOrderListReceive recv = new AllOrderListReceive();
/* 200 */     String recvData = _$4317(_$2530.getString("allOrderListUrl"), sendData, recv);
/*     */ 
/* 202 */     recv.setRecvData(recvData);
/* 203 */     return recv;
/*     */   }
/*     */ 
/*     */   public UnsettledOrderListReceive getUnsettledList(UnsettledOrderListSend request)
/*     */     throws CsiiException
/*     */   {
/* 212 */     if (this._$2566 == null) {
/* 213 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 215 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 217 */     UnsettledOrderListReceive recv = new UnsettledOrderListReceive();
/* 218 */     String recvData = _$4317(_$2530.getString("unsettledListUrl"), sendData, recv);
/*     */ 
/* 220 */     recv.setRecvData(recvData);
/* 221 */     return recv;
/*     */   }
/*     */ 
/*     */   public UnsettledRefundReceive unsettledRefund(UnsettledRefundSend request)
/*     */     throws CsiiException
/*     */   {
/* 230 */     if (this._$2566 == null) {
/* 231 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 233 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 235 */     UnsettledRefundReceive recv = new UnsettledRefundReceive();
/* 236 */     String recvData = _$4317(_$2530.getString("unsettledRefundUrl"), sendData, recv);
/*     */ 
/* 238 */     recv.setRecvData(recvData);
/* 239 */     return recv;
/*     */   }
/*     */ 
/*     */   public SettledRefundReceive settledRefund(SettledRefundSend request)
/*     */     throws CsiiException
/*     */   {
/* 249 */     if (this._$2566 == null) {
/* 250 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 252 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 254 */     SettledRefundReceive recv = new SettledRefundReceive();
/* 255 */     String recvData = _$4317(_$2530.getString("settledRefundUrl"), sendData, recv);
/*     */ 
/* 257 */     recv.setRecvData(recvData);
/* 258 */     return recv;
/*     */   }
/*     */ 
/*     */   public FullRefundReceive fullRefund(FullRefundSend request)
/*     */     throws CsiiException
/*     */   {
/* 269 */     if (this._$2566 == null) {
/* 270 */       throw new CsiiException("需要先运行login(String user, String password)方法进行登陆");
/*     */     }
/* 272 */     String sendData = request.getSendData() + "|userId=" + this._$2566;
/*     */ 
/* 274 */     FullRefundReceive recv = new FullRefundReceive();
/* 275 */     String recvData = _$4317(_$2530.getString("fullRefundUrl"), sendData, recv);
/*     */ 
/* 277 */     recv.setRecvData(recvData);
/* 278 */     return recv;
/*     */   }
/*     */ 
/*     */   private String _$4317(String targetUrl, String toGate, DataReceive recv)
/*     */     throws CsiiException
/*     */   {
/* 286 */     HttpsURLConnection conn = null;
/* 287 */     String fromGate = null;
/*     */ 
/* 289 */     String toSign = MerchantSignVerify.merchantSignData_ABA(toGate);
/*     */ 
/* 291 */     String fromOrig = null;
/* 292 */     String fromSign = null;
/* 293 */     String payFlag = null;
/*     */     try
/*     */     {
/* 296 */       URL url = new URL(targetUrl);
/*     */ 
/* 298 */       conn = (HttpsURLConnection)url.openConnection();
/* 299 */       conn.setDoOutput(true);
/* 300 */       conn.setDoInput(true);
/* 301 */       conn.setSSLSocketFactory(_$4311());
/* 302 */       conn.setRequestProperty("userid", this._$2566);
/* 303 */       conn.setRequestProperty("origdata", toGate);
/* 304 */       conn.setRequestProperty("sign", toSign);
/* 305 */       conn.setRequestProperty("cookie", this._$4292);
/*     */ 
/* 307 */       conn.setRequestMethod("POST");
/* 308 */       conn.connect();
/*     */ 
/* 310 */       BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GB2312"));
/*     */ 
/* 317 */       String temp = reader2.readLine();
/*     */ 
/* 319 */       if (temp.length() != 0) {
/* 320 */         fromOrig = temp.substring(temp.indexOf("=") + 1);
/*     */       }
/* 322 */       temp = reader2.readLine();
/*     */ 
/* 324 */       if (temp.length() != 0) {
/* 325 */         fromSign = temp.substring(temp.indexOf("=") + 1);
/*     */       }
/* 327 */       payFlag = reader2.readLine();
/*     */     }
/*     */     catch (ConnectException e)
/*     */     {
/* 331 */       e.printStackTrace();
/* 332 */       throw new CsiiException("连接支付网关失败");
/*     */     }
/*     */     catch (MalformedURLException e) {
/* 335 */       throw new CsiiException("连接支付网关URL格式异常");
/*     */     }
/*     */     catch (IOException e) {
/* 338 */       e.printStackTrace();
/* 339 */       throw new CsiiConnectionException("连接支付网关异常");
/*     */     }
/*     */ 
/* 343 */     if ((payFlag == null) || (!payFlag.equals("SDBPAYGATE=SDBPAYGATE"))) {
/* 344 */       throw new CsiiHostException("没有收到支付标志，支付网关返回异常");
/*     */     }
/*     */ 
/* 348 */     boolean verify = MerchantSignVerify.merchantVerifyPayGate_ABA(fromSign, fromOrig);
/* 349 */     if (!verify) {
/* 350 */       throw new CsiiException("客户端数据校验失败：fromSign=(" + fromSign + "), fromOrig=" + fromOrig);
/*     */     }
/*     */ 
/* 353 */     Properties fromProps = InputCheckTool.parseStringToProperties(fromOrig, "|");
/*     */ 
/* 356 */     String status = (String)fromProps.get("status");
/* 357 */     String errDesc = (String)fromProps.get("errdesc");
/*     */ 
/* 359 */     recv.setStatus(status);
/* 360 */     recv.setErrDesc(errDesc);
/*     */ 
/* 362 */     if ((status == null) || (!status.equals("0"))) {
/* 363 */       throw new CsiiException(errDesc);
/*     */     }
/*     */ 
/* 366 */     return (String)fromProps.get("data");
/*     */   }
/*     */ 
/*     */   private SSLSocketFactory _$4311()
/*     */   {
/*     */     try
/*     */     {
/* 376 */       SecureRandom secureRandom = new SecureRandom();
/* 377 */       secureRandom.nextInt();
/*     */ 
/* 379 */       KeyStore merchantKeyStore = KeyStore.getInstance("JKS");
/* 380 */       String keyStoreFile = _$2530.getString("cafile");
/* 381 */       merchantKeyStore.load(new FileInputStream(keyStoreFile), _$2530.getString("store_password").toCharArray());
/*     */ 
/* 383 */       TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
/* 384 */       tmf.init(merchantKeyStore);
/*     */ 
/* 386 */       KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
/* 387 */       kmf.init(merchantKeyStore, _$2530.getString("key_password").toCharArray());
/*     */ 
/* 389 */       SSLContext sslContext = SSLContext.getInstance("TLS");
/* 390 */       sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
/* 391 */       _$2568 = sslContext.getSocketFactory();
/*     */     }
/*     */     catch (KeyStoreException e) {
/* 394 */       _$4293 = true;
/* 395 */       _$4294 = "证书私钥容器异常";
/* 396 */       e.printStackTrace();
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 399 */       _$4293 = true;
/* 400 */       _$4294 = "证书私钥容器不存在";
/* 401 */       e.printStackTrace();
/*     */     }
/*     */     catch (CertificateException e) {
/* 404 */       _$4293 = true;
/* 405 */       _$4294 = "证书私钥容器密码错误";
/* 406 */       e.printStackTrace();
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/* 409 */       _$4293 = true;
/* 410 */       _$4294 = "无此算法";
/* 411 */       e.printStackTrace();
/*     */     }
/*     */     catch (UnrecoverableKeyException e) {
/* 414 */       _$4293 = true;
/* 415 */       _$4294 = "不可恢复私钥或私钥密码错";
/* 416 */       e.printStackTrace();
/*     */     }
/*     */     catch (Exception e) {
/* 419 */       _$4293 = true;
/* 420 */       _$4294 = "无法生成SSL连接";
/* 421 */       e.printStackTrace();
/*     */     }
/* 423 */     return _$2568;
/*     */   }
/*     */ 
/*     */   private String _$4310(String key) throws CsiiException {
/* 427 */     String url = _$2530.getString(key);
/* 428 */     if (url == null)
/* 429 */       throw new CsiiException("请在 merchant.properties文件设置" + key);
/* 430 */     System.err.println(key + " = " + url);
/* 431 */     return url.trim();
/*     */   }
/*     */   public static void main(String[] args) throws Exception {
/* 434 */     Security.addProvider(new Provider());
/* 435 */     System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
/*     */ 
/* 437 */     HttpsPoster poster = new HttpsPoster();
/* 438 */     Properties p = new Properties();
/* 439 */     p.put("transName", "EntLogin");
/* 440 */     p.put("userName", "cao");
/* 441 */     String strUrl = "https://paygate.sdb.com.cn:443/personal/servlet/com.csii.ebank.core.MainServlet";
/* 442 */     URL url = new URL(strUrl);
/* 443 */     poster.setUrl(url);
/* 444 */     poster.setParams(p);
/* 445 */     poster.setSslSocketFactory(new PaymentInterfaceUtil()._$4311());
/* 446 */     BufferedReader reader = poster.getReader();
/* 447 */     String line = null;
/*     */     while (true) {
/* 449 */       line = reader.readLine();
/* 450 */       if (line == null) {
/*     */         break;
/*     */       }
/* 453 */       System.out.println(line);
/*     */     }
/* 455 */     reader.close();
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.core.PaymentInterfaceUtil
 * JD-Core Version:    0.6.2
 */