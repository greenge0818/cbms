package com.prcsteel.platform.order.service.mail;

/**
 * 用来发送邮件
 * @author tuxianming
 *
 */
public interface SendMailService {
	/**
	 * 用来周报与月报邮件
	 */
	void sendReportMail();
}
