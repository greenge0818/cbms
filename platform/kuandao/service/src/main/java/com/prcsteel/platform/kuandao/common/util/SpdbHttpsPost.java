package com.prcsteel.platform.kuandao.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csii.payment.client.core.MerchantSignVerify;
import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

/**
 * 
 * @Title SpdbHttpsPost.java
 * @Package com.prcsteel.platform.kuandao.common.util
 * @Description 浦发交易请求专用类<p>以https方式提交请求<br/>解析xml报文<br/>封装返回结果</p>
 * @author zjshan
 *
 * @date 2016年5月27日 下午2:02:39
 */
public class SpdbHttpsPost {
	
	private final String httpsURL;

	private final String password;
	
	private final String keyStorePath;
	
	private final String trustStorePath;
	
	private static final int CONNECTTIMEOUT = 5 * 1000;

	private static final int READTIMEOUT = 60 * 1000;
	
	private static final String CHARSET = "GBK";
	
	private static final  Logger logger = LoggerFactory.getLogger(SpdbHttpsPost.class);

	
	public SpdbHttpsPost(String httpsURL,String password, String keyStorePath, String trustStorePath){
		this.httpsURL = httpsURL;
		this.password = password;
		this.keyStorePath = keyStorePath;
		this.trustStorePath = trustStorePath;
	}
	
	/**
	 * 获得KeyStore.
	 * @param keyStorePath 密钥库路径
	 * @param password 密码
	 * @return 密钥库
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 */
	private KeyStore getKeyStore(String password, String keyStorePath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance("JKS");
		// 获得密钥库文件流
		FileInputStream is = new FileInputStream(keyStorePath);
		// 加载密钥库
		ks.load(is, password.toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}

	/**
	 * 获得SSLSocketFactory.
	 * @param password 密码
	 * @param keyStorePath 密钥库路径
	 * @param trustStorePath 信任库路径
	 * @return SSLSocketFactory
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 * @throws Exception
	 */
	private SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException  {
		// 实例化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		// 获得密钥库
		KeyStore keyStore = getKeyStore(password, keyStorePath);
		// 初始化密钥工厂
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 实例化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		// 获得信任库
		KeyStore trustStore = getKeyStore(password, trustStorePath);
		// 初始化信任库
		trustManagerFactory.init(trustStore);
		
		
		TrustManager[] trustAllCerts = new TrustManager[] { 
			new X509TrustManager() {
				
				@Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            	return new java.security.cert.X509Certificate[] {};
	            }
	            
	            @Override
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            	// Do nothing
	            }
	            @Override
	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            	// Do nothing
	            }
			}
        };
		
		
		// 实例化SSL上下文
		SSLContext ctx = SSLContext.getInstance("TLS");
		
		ctx.init(null, trustAllCerts, new java.security.SecureRandom());
		
		// 获得SSLSocketFactory
		return ctx;
	}
	
	/**
	 * 初始化HttpsURLConnection.
	 * @param password 密码
	 * @param keyStorePath 密钥库路径
	 * @param trustStorePath 信任库路径
	 * @throws IOException 
	 */
	private void initHttpsURLConnection() throws IOException  {
		// 声明SSL上下文
		SSLContext sslContext = null;
		// 实例化主机名验证接口
		HostnameVerifier hnv = new MyHostnameVerifier();
		try {
			sslContext = getSSLContext(password, keyStorePath, trustStorePath);
		} catch (GeneralSecurityException e) {
			logger.error("initHttpsURLConnection failed", e);;
		}
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}

	/**
	 * 
	 * @param spdbRequestParam
	 * @return SPDBResponseResult 
	 * if get response failed return null
	 */
	public SPDBResponseResult post(SPDBRequstParam spdbRequestParam) {
		logger.debug(spdbRequestParam.getPlain());
		logger.debug(spdbRequestParam.getSignature());
		SPDBResponseResult result = null;
		HttpsURLConnection urlCon = null;
		BufferedReader in = null;
		String xmlStr = getSendXml(spdbRequestParam);
		int retryNum = 5;
		try {
			boolean flag = true; //是否发送请求
			for(int i = 0; i <= retryNum && flag; i++){
				try{
					
					urlCon = (HttpsURLConnection) (new URL(httpsURL)).openConnection();
					String sContentLength = String.valueOf(xmlStr.getBytes().length);
					configURLConnection(urlCon, sContentLength);
				}catch(Exception e){
					if(urlCon != null)
						urlCon.disconnect();
					logger.warn(String.format("connect %s failed", httpsURL),e);
					flag = true;
					continue;
				}
				
				// 设置为gbk可以解决服务器接收时读取的数据中文乱码问题
				OutputStream out = urlCon.getOutputStream();
				out.write(xmlStr.getBytes());
				out.flush();
				out.close();
				
				if(HttpURLConnection.HTTP_OK != urlCon.getResponseCode()){ //响应吗不是200，进行重试
					logger.warn(String.format("ResponseCode:%s,retry %s times", urlCon.getResponseCode(),i));
					urlCon.disconnect();
					flag = true;
					continue;
				}else{
					flag = false;
					in =  new BufferedReader(new InputStreamReader(urlCon.getInputStream(),CHARSET));
					result = getResult(in);
				}
			}
			return result;
		} catch (MalformedURLException e) {
			logger.error("bad url", e);
			return result;
		} catch (IOException e) {
			logger.error("", e);
			return result;
		} catch (Exception e) {
			logger.error("post failed", e);
			return result;
		}finally{
			if(in != null)
				IOUtils.closeQuietly(in);
			if(urlCon != null)
				urlCon.disconnect();
		}
		
	}

	private void configURLConnection(HttpsURLConnection urlCon, String sContentLength) throws ProtocolException {
		urlCon.setConnectTimeout(CONNECTTIMEOUT);
		urlCon.setReadTimeout(READTIMEOUT);
		urlCon.setDoInput(true);
		urlCon.setDoOutput(true);
		urlCon.setRequestMethod("POST");
		urlCon.setRequestProperty("Content-Type", "text/xml;charset=GBK");
		urlCon.setRequestProperty("Content-Length",sContentLength);
		urlCon.setUseCaches(false);
	}
	
	
	/**
     * 发送数据XML
     * @param plain
     * @param signature
     * @return
     */
	public String getSendXml(SPDBRequstParam spdbTransParam) {
		
       
		String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
        strXml += "<packet>";
        strXml += "<transName>" + spdbTransParam.getTransName() + "</transName>";
        strXml += "<Plain>" +spdbTransParam.getPlain() + "</Plain>";
        strXml += "<Signature>" + spdbTransParam.getSignature() + "</Signature>";
        strXml += "</packet>";
        return strXml;
    }
	
	public SPDBResponseResult getResult(BufferedReader in){
		SAXReader saxReader = new SAXReader();
		Document document;
		SPDBResponseResult result = new SPDBResponseResult();
		try {
			document = saxReader.read(in);
			Element root = document.getRootElement();
			Element errCodeElement = root.element("ErrorCode");
			if(errCodeElement != null){
				String errMsg = root.elementText("ErrorMsg");
				result.setSuccess(false);
				result.setErrCode(errCodeElement.getText());
				result.setErrMsg(errMsg);
			}else{
				String transName = root.elementText("transName");
				String plain = root.elementText("Plain");
				String sign = root.elementText("Signature");
				if(SPDBTransNameConstant.MCLS.equals(transName)){
					plain = plain + "\r\n";
				}else if(SPDBTransNameConstant.LSXZ.equals(transName)){
					plain = plain.replaceAll("\n", "\r\n");
					plain = plain + "\r\n";
				}
				if(MerchantSignVerify.merchantVerifyPayGate_ABA(sign,plain)){
					result.setSuccess(true);
					result.setTransName(transName);
					plain = URLDecoder.decode(plain,CHARSET);
					result.setPlain(plain);
				}else{
					result.setSuccess(false);
					result.setErrCode("-1");
					result.setErrMsg("浦发返回报文验签失败");
				}
			}
		} catch (DocumentException e) {
			result.setSuccess(false);
			result.setErrCode("-2");
			result.setErrMsg("浦发返回报文解析失败");
			logger.error("parse xml failed",e);
		} catch (UnsupportedEncodingException e) {
			result.setSuccess(false);
			result.setErrCode("-3");
			result.setErrMsg("浦发返回报文解码失败");
			logger.error("decode spdb plain text failed",e);
		}
		return result;
	}
	
	
}
