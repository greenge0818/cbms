package com.prcsteel.platform.kuandao.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;

@Service
public class KuandaoToAccountProcess {
	
	private static final Logger logger = LoggerFactory.getLogger(KuandaoToAccountProcess.class);
	
	@Resource
	private KuandaoMsgSender kuandaoMsgSender;
	
	
	public boolean sendAccount(CustAccountDto custAccountDto){
		
		if(custAccountDto == null || custAccountDto.getId() == null){
			logger.error("无效custAccountDto,发送MQ失败");
			return  false;
		}
		try{
			ObjectMapper objMap = new ObjectMapper();
	        String reqStr = objMap.writeValueAsString(custAccountDto);
	        logger.info("客户信息发送MQ，报文如下：");
	        logger.info(reqStr);
	        kuandaoMsgSender.send(reqStr);
	        return true;
		}catch(Exception e){
			logger.error("客户信息发送MQ失败，客户编号："+custAccountDto.getId(),e);
			return false;
		}
	}
}
