/*     */ package com.csii.payment.client.util;
/*     */ 
/*     */ import com.sun.net.ssl.HttpsURLConnection;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ public class HttpsPoster
/*     */ {
/*     */   private URL _$2574;
/*     */   private Properties _$5253;
/*     */   private SSLSocketFactory _$2568;
/*     */ 
/*     */   public HttpsPoster()
/*     */   {
/*     */   }
/*     */ 
/*     */   public HttpsPoster(String strUrl)
/*     */     throws MalformedURLException
/*     */   {
/*  40 */     this._$2574 = new URL(strUrl);
/*     */   }
/*     */ 
/*     */   public HttpsPoster(URL url)
/*     */   {
/*  46 */     this._$2574 = url;
/*     */   }
/*     */ 
/*     */   public HttpsPoster(String strUrl, Properties params)
/*     */     throws MalformedURLException
/*     */   {
/*  53 */     this._$2574 = new URL(strUrl);
/*  54 */     setParams(params);
/*     */   }
/*     */ 
/*     */   public HttpsPoster(URL Url, Properties params)
/*     */   {
/*  60 */     setUrl(this._$2574);
/*  61 */     setParams(params);
/*     */   }
/*     */ 
/*     */   public Properties getParams()
/*     */   {
/*  69 */     return this._$5253;
/*     */   }
/*     */ 
/*     */   public BufferedReader getReader()
/*     */     throws IOException
/*     */   {
/*  77 */     HttpsURLConnection conn = (HttpsURLConnection)this._$2574.openConnection();
/*  78 */     conn.setSSLSocketFactory(this._$2568);
/*     */     try {
/*  80 */       conn.setRequestMethod("POST");
/*     */     } catch (ProtocolException e) {
/*  82 */       e.printStackTrace();
/*     */     }
/*  84 */     conn.setDoOutput(true);
/*  85 */     conn.setDoInput(true);
/*  86 */     PrintWriter out = new PrintWriter(conn.getOutputStream());
/*     */ 
/*  88 */     out.print(_$5256());
/*  89 */     out.close();
/*  90 */     return new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */   }
/*     */ 
/*     */   public SSLSocketFactory getSslSocketFactory()
/*     */   {
/*  98 */     return this._$2568;
/*     */   }
/*     */ 
/*     */   public URL getUrl()
/*     */   {
/* 106 */     return this._$2574;
/*     */   }
/*     */ 
/*     */   public void setParams(Properties params)
/*     */   {
/* 114 */     this._$5253 = params;
/*     */   }
/*     */ 
/*     */   public void setSslSocketFactory(SSLSocketFactory sslSocketFactory)
/*     */   {
/* 122 */     this._$2568 = sslSocketFactory;
/*     */   }
/*     */ 
/*     */   public void setUrl(URL url)
/*     */   {
/* 130 */     this._$2574 = url;
/*     */   }
/*     */ 
/*     */   private String _$5256()
/*     */   {
/* 137 */     StringBuffer send = new StringBuffer();
/* 138 */     if (this._$5253 == null) {
/* 139 */       this._$5253 = new Properties();
/*     */     }
/* 141 */     Enumeration keys = this._$5253.keys();
/* 142 */     int i = 0;
/* 143 */     while (keys.hasMoreElements()) {
/* 144 */       String key = (String)keys.nextElement();
/* 145 */       String value = (String)this._$5253.get(key);
/* 146 */       if (i > 0) {
/* 147 */         send.append('&');
/*     */       }
/* 149 */       send.append(URLEncoder.encode(key)).append('=').append(URLEncoder.encode(value));
/*     */ 
/* 151 */       i++;
/*     */     }
/* 153 */     return send.toString();
/*     */   }
/*     */ }

/* Location:           D:\cert4real\spdbmerchant.jar
 * Qualified Name:     com.csii.payment.client.util.HttpsPoster
 * JD-Core Version:    0.6.2
 */