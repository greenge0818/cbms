package com.prcsteel.platform.order.web.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MarketAddUserSender extends QueueSender{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void withText(String mobile, String name) {
		try {
			String text = "{\"mobile\": \""+mobile+"\", \"name\":\""+name+"\"}";
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
