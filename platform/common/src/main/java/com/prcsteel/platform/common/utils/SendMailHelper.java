package com.prcsteel.platform.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

public class SendMailHelper{
	private Logger logger = Logger.getLogger(this.getClass());
	
	static String host;
	static String fromAddress;
	static Boolean isRun;
	static Boolean starttls;
	
	static String user;
	static String password;
	static Boolean isAuth;
	static int port;
	static String protocol="smtp";
	
	public SendMailHelper(){}
	
	public static boolean isMailActive(){
		if(!isRun){
			return false;
		}
		return true;
	}
	
	public static boolean send(String text, String subject, String toAddress){
		SendMailHelper helper = new SendMailHelper();
		MyAuthenticator authenticator=null;
		
		if(!isRun){
			return false;
		}
		
		if(!isAuth){
			authenticator=helper.getMyAuthenticator(user, password);
		}
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", isAuth);
	    props.put("mail.smtp.host", host); 
		
		Session session=Session.getDefaultInstance(props, authenticator);
		Message message=new MimeMessage(session);
		try {
			Address from=new InternetAddress(fromAddress);
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, getAddresses(toAddress));
			message.setSubject(subject);
			message.setHeader("X-Mailer", "JavaMail");
			message.setSentDate(new Date());
			message.setText(text);
			Transport transport = session.getTransport(protocol);
			transport.connect(host, port, user, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
//			 Transport.send(message);
			return true;
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean sendHTMLMail(String text, String subject, String toAddress){
		SendMailHelper helper = new SendMailHelper();
		
		if(!isRun){
			return false;
		}
		
		MyAuthenticator authenticator=null;
		if(isAuth){
			authenticator=helper.getMyAuthenticator(user, password);
		}
		
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", isAuth);
	    props.put("mail.smtp.host", host); 
		Session session=Session.getDefaultInstance(props, authenticator);
		try {
			Address from=new InternetAddress(fromAddress);
			
			Message message=new MimeMessage(session);
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, getAddresses(toAddress));
			message.setSentDate(new Date());
			
			message.setSubject(subject);
			Multipart mp = new MimeMultipart("related");
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setDataHandler(new DataHandler(text, "text/html;charset=utf-8"));
			mp.addBodyPart(bodyPart); 
			message.setContent(mp);
			Transport transport = session.getTransport(props.getProperty("protocol"));
			transport.connect(host, port, user, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
//			 Transport.send(message);
	        //Transport.send(message);
			return true;
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean sendAttatchment(String text, String subject, String toAddress, File attachment, String filename) {
		SendMailHelper helper = new SendMailHelper();
		
		if(!isRun){
			return false;
		}
		
		MyAuthenticator authenticator=null;
		if(isAuth){
			authenticator=helper.getMyAuthenticator(user, password);
		}
		
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", isAuth);
	    props.put("mail.smtp.host", host); 
		
		Session session=Session.getDefaultInstance(props,authenticator);
		try {
			Address from=new InternetAddress(fromAddress);
			
			Message message=new MimeMessage(session);
			message.setSubject(subject);
			
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, getAddresses(toAddress));
			message.setSentDate(new Date());
			
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(text, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(filename));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            
//	        Transport.send(message);
            Transport transport = session.getTransport(protocol);
			transport.connect(host, port, user, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public MyAuthenticator getMyAuthenticator(String userName,String password){
		return new MyAuthenticator(userName, password);
	}
	private class MyAuthenticator extends Authenticator{
		String userName=null;
		String password=null;
		
		public MyAuthenticator(){}
		
		public MyAuthenticator(String userName,String password){
			this.userName=userName;
			this.password=password;
		}
		protected PasswordAuthentication getPasswordAuthentication(){  
	        return new PasswordAuthentication(userName, password);  
	    }
	}
	
	private static Address[] getAddresses(String address){
		Address[] addresses= null;
		if(StringUtils.isNotEmpty(address)){
			String[] addressStr =  address.split(";");
			addresses = new Address[addressStr.length];
			for(int i=0; i<addressStr.length; i++){
				try {
					Address a = new InternetAddress(addressStr[i].trim());
					addresses[i] = a;
				} catch (AddressException e) {
					e.printStackTrace();
				}
			}
		}
		return addresses;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		SendMailHelper.host = host;
	}

	public static String getFromAddress() {
		return fromAddress;
	}

	public static void setFromAddress(String fromAddress) {
		SendMailHelper.fromAddress = fromAddress;
	}

	public static Boolean getIsRun() {
		return isRun;
	}

	public static void setIsRun(Boolean isRun) {
		SendMailHelper.isRun = isRun;
	}

	public static Boolean getStarttls() {
		return starttls;
	}

	public static void setStarttls(Boolean starttls) {
		SendMailHelper.starttls = starttls;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		SendMailHelper.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SendMailHelper.password = password;
	}

	public static Boolean getIsAuth() {
		return isAuth;
	}

	public static void setIsAuth(Boolean isAuth) {
		SendMailHelper.isAuth = isAuth;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		SendMailHelper.port = port;
	}

	public static String getProtocol() {
		return protocol;
	}

	public static void setProtocol(String protocol) {
		SendMailHelper.protocol = protocol;
	}

}