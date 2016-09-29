package com.prcsteel.platform.account.web.mq;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketUpdateUserSender extends QueueSender{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void withText(Long ecUserId, String mobile, String name) {
		try {
			String text = "{\"id\": "+ecUserId+", \"mobile\":\""+mobile+"\", \"name\":\""+name+"\"}";
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

