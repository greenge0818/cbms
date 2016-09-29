package com.prcsteel.platform.kuandao.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.service.SendMsg;

@Service
public class KuandaoMsgSender implements SendMsg{

	@Resource
	private JmsTemplate jmsTemplate;
	@Value("${jms.kuandaoAccount}")
	private String queue;
	
	@Override
	public void send(Serializable dto) {
		send(dto,queue);
	}
	
	@Override
	public void send(String str) {
		send(str,queue);
	}
    
	
	@Override
	public void send(Serializable dto,String queue) {
		jmsTemplate.send(queue, new MessageCreator() {
			public Message createMessage(Session s) throws JMSException {
				return s.createObjectMessage(dto);
			}
		});
	}
	
	@Override
	public void send(String str,String queue) {
		jmsTemplate.send(queue, new MessageCreator() {
			public Message createMessage(Session s) throws JMSException {
				return s.createTextMessage(str);
			}
		});
	}
    
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

	
}
