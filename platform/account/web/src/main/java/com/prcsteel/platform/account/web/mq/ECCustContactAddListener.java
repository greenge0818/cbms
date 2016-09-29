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
 * 超市（EC2.0）新增客户的联系人；
 * Created by chengui on 2016-6-29.
 *
 */
@Component
public class ECCustContactAddListener extends QueueListenerAbstract {
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
	ContactService contactService;


	@Override
	public boolean doProcess(String reqContent) {
	    
        //将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		try{
			ECCustContactDto contactDto = objMap.readValue(reqContent, ECCustContactDto.class);

			//判断超市id记录是否存在，存在则不做任何处理
			if(null != contactService.queryByEcUserId(contactDto.getId())){
				return true;
			}

			//判断mobile记录是否存在，存在则通过mobile更新ec_user_id
			if(null != contactService.queryByTel(contactDto.getMobile())){
				Contact contact = new Contact();
				contact.setEcUserId(contactDto.getId());
				contact.setTel(contactDto.getMobile());
				//source为PICK时，保存联系人名称、公司名称
				if(contactDto.getSource().toUpperCase().equals("PICK")){
					contact.setName(contactDto.getName());
					contact.setCompanyName(contactDto.getAccount());
				}
				contact.setSource(contactDto.getSource());
				contact.setLastUpdatedBy("超市系统");
				contact.setLastUpdated(new Date());
				contactService.updateEcUserIdByTel(contact);
				return true;
			}

			//不存在则新增联系人
			Contact contact = new Contact();
			contact.setEcUserId(contactDto.getId());
			contact.setTel(contactDto.getMobile());
			contact.setName(contactDto.getSource().toUpperCase().equals("PICK")?contactDto.getName():contactDto.getMobile());
			contact.setCompanyName(contactDto.getSource().toUpperCase().equals("PICK")?contactDto.getAccount():"");
			contact.setSource(contactDto.getSource());

			contact.setLastUpdatedBy("超市系统");
			contact.setLastUpdated(new Date());
			contact.setCreatedBy("超市系统");
			contact.setCreated(new Date());
			contactService.saveContact(contact);

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

