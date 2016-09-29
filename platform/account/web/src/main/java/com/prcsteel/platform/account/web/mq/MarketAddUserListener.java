package com.prcsteel.platform.account.web.mq;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.ContactService;

/**
 * CBMS新增用户，通知超市 ，然后超市接收后，返回给CBMS超市用户id
 * 这里处理的是： 超市接收后，返回给CBMS超市用户id， 将得到的超市用户id通过手机号，更新到cbms中。
 * @author tuxianming
 * @date 20160701
 */
public class MarketAddUserListener extends QueueListenerAbstract{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	ContactService contactService;
	
	@Override
	protected boolean doProcess(String reqJson) {
	
		 //将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		try{
			JsonNode node = objMap.readTree(reqJson);
			Integer id = node.path("id").asInt();
			String mobile = node.path("mobile").asText();
			
			Contact contact = contactService.queryByTel(mobile);
			Contact updateObj = new Contact();
			updateObj.setId(contact.getId());
			updateObj.setEcUserId(id);
			contactService.updateById(updateObj);
			
		} catch (Exception e) {
            logger.info(e.getMessage(), e);
            return false;
        } 
		
		return true;
	}

	@Override
	protected boolean doProcess(Object reqObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
