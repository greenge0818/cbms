package com.prcsteel.platform.kuandao.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prcsteel.platform.kuandao.model.dto.PaymentAccountDto;

@Service
public class KuandaoToPaymentAccountProcess {
	
	private static final Logger logger = LoggerFactory.getLogger(KuandaoToPaymentAccountProcess.class);
	
	@Resource
	private KuandaoMsgSender kuandaoMsgSender;
	
	@Value("${jms.kuandaoPaymentAccount}")
	private String queue;
	
	
	public boolean sendPaymentAccount(PaymentAccountDto paymentAccountDto){
		
		if(paymentAccountDto == null || paymentAccountDto.getAccountId() == null){
			logger.error("无效paymentAccountDto,发送MQ失败");
			return  false;
		}
		try{
			ObjectMapper objMap = new ObjectMapper();
	        String reqStr = objMap.writeValueAsString(paymentAccountDto);
	        logger.debug("客户资金账户信息发送MQ，报文如下：");
	        logger.debug(reqStr);
	        kuandaoMsgSender.send(reqStr, queue);
	        return true;
		}catch(Exception e){
			logger.error("客户资金账户信息发送MQ失败，客户编号：" + paymentAccountDto.getAccountId(),e);
			return false;
		}
	}
}
