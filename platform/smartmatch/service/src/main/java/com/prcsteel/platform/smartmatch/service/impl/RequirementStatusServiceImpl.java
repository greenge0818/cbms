package com.prcsteel.platform.smartmatch.service.impl;

import com.google.gson.Gson;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.service.RequirementStatusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * 找货需求单号状态更新
 *
 * @author peanut
 * @date 2016/6/22 9:30
 */
@Service("requirementStatusService")
public class RequirementStatusServiceImpl implements RequirementStatusService {

    //日志
    private final Logger logger = LoggerFactory.getLogger(RequirementStatusServiceImpl.class);

    private JmsTemplate jmsTemplate;

    private String queue;

    @Override
    public void send(Object o) {
    	
    	if(logger.isDebugEnabled()){
    		StackTraceElement[] temp = Thread.currentThread().getStackTrace();
    		StackTraceElement a = (StackTraceElement)temp[2];
    		logger.debug("修改超市状态，开始调用MQ的发送接口-from--"+a.getMethodName()+"--method----------to sendMQ--------");
    	}
    	
    	try{
	        jmsTemplate.send(queue, s -> {
	            final TextMessage message = s.createTextMessage();
	            message.setText(new Gson().toJson(o));
	            return message;
	        });
    	} catch (Exception e){
    		logger.error("---------调用发送接口失败----",e);
    		throw new RuntimeException("调用发送MQ接口失败", e);
    	}
    	
    	if(logger.isDebugEnabled()){
    		logger.debug("修改超市状态，发送MQ调用完毕-----------------");
    	}
    }


    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
