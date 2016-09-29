package com.prcsteel.platform.smartmatch.web.mq;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @description 找货系统发送资源信息给行情中心，EC2.0
 * @author zhoucai
 * @date 2016-6-12
 * @version ：1.0
 *
 */

public class ResourceRequirementSender implements SendMsg {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private JmsTemplate jmsTemplate;
	private String queue;

	@Override
	public void send(Serializable dto) {
		jmsTemplate.send(queue, new MessageCreator() {
			public Message createMessage(Session s) throws JMSException {
				ObjectMessage msg = s.createObjectMessage(dto);
				return msg;
			}
		});
	}

	@Override
	public void send(String str) {
		jmsTemplate.send(queue, new MessageCreator() {
			public Message createMessage(Session s) throws JMSException {
				TextMessage msg = s.createTextMessage(str);
				return msg;
			}
		});
	}

	@Override
	public void sendWithTrack(String str,String busiCode,int busiId) {
        try {
            send(str);
        } catch (Exception e) {
            logger.info("{}",e);

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
