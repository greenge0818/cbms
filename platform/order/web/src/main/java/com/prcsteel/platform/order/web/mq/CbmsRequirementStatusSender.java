package com.prcsteel.platform.order.web.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CbmsRequirementStatusSender extends QueueSender {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void withText(String code, String requirementCode, String statusTo, String closeReason) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sf.format((new Date()).getTime());

			String text = "{\"code\":\""+requirementCode+"\", \"statusTo\":\""+statusTo+"\", \"operateCode\":\""+code+
					"\", \"closeReason\":\""+closeReason+"\", \"operated\":\""+date+"\"}";
			super.withText(text);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void withObject(Serializable dto) {
		super.withObject(dto);
	}

}
