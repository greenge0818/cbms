package com.prcsteel.platform.order.web.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * @author chengui
 * @date 20160704
 */
public class QueueSender {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private JmsTemplate jmsTemplate;
	private String queue;

	/**
	 * 使用文本的方式发送MQ
	 * @param text
	 */
	public void withText(String text) {
		try {
			jmsTemplate.send(queue, (session)->{
				TextMessage msg = session.createTextMessage(text);
				return msg;
			});
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
	}

	/**
	 * 使用序列化对象的方式发送MQ
	 * @param dto
	 */
	public void withObject(Serializable dto) {
		try {
			jmsTemplate.send(queue, (session)->{
				ObjectMessage msg = session.createObjectMessage(dto);
				return msg;
			});
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
