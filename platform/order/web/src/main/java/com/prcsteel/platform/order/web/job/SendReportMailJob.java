package com.prcsteel.platform.order.web.job;

import javax.annotation.Resource;

import com.prcsteel.platform.order.service.mail.SendMailService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * @description 通过邮件发送周报
 * @author tuxianming
 *
 */
@Component
public class SendReportMailJob extends CbmsJob {

	@Resource
	SendMailService sendMailService;
	@Value("${quartz.job.email.enabled}")
	private boolean emailEnabled;
	@Override
	public void execute() {
		if (isEnabled()) {
			sendMailService.sendReportMail();
		}
	}

	@Override
	public boolean isEnabled() {
		return emailEnabled;
	}

}
