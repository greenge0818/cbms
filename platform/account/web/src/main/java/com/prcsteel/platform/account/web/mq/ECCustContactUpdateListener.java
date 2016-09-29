package com.prcsteel.platform.account.web.mq;


import com.prcsteel.platform.account.model.dto.ECCustContactDto;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.ContactService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 监听器
 * 超市（EC2.0）修改客户的联系人；
 * Created by chengui on 2016-6-29.
 *
 */
@Component
public class ECCustContactUpdateListener extends QueueListenerAbstract {
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
	ContactService contactService;

	@Override
	public boolean doProcess(String reqContent) {

		//将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		try{
			ECCustContactDto contactDto = objMap.readValue(reqContent, ECCustContactDto.class);
			Contact contact = new Contact();
			contact.setEcUserId(contactDto.getId());
			contact.setTel(contactDto.getMobile());
			contact.setLastUpdatedBy("超市系统");
			contact.setLastUpdated(new Date());
			contactService.updateTelByEcUserId(contact);
			return true;
		} catch (Exception e) {
			logger.info("{}", e);
			return false;
		}
	}

	@Override
	protected boolean doProcess(Object reqObj) {
		return false;
	}

}

