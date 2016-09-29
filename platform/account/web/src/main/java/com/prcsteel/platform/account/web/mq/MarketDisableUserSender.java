package com.prcsteel.platform.account.web.mq;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketDisableUserSender extends QueueSender{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void withText(Integer ecUserId, String status) {
		try {
			String text = "{\"id\": "+ecUserId+", \"status\":\""+status+"\"}";
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
