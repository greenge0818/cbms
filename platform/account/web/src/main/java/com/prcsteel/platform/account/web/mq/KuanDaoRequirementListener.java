package com.prcsteel.platform.account.web.mq;


import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听器
 * 款道2.0会员体系会修改客户的公司全称和组织机构代码；
 *  支付体系会修改会员的账户余额。
 * @author wangxiao
 *
 */
@Component
public class KuanDaoRequirementListener extends QueueListenerAbstract {
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
	AccountService accountService;

    
	@Override
	protected boolean doProcess(String reqContent) {
	    
        //将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		boolean processFlag = true;
		try{
			Account account = objMap.readValue(reqContent, Account.class);
			accountService.updateAccount(account);
		} catch (Exception e) {
			processFlag = false;
            logger.info("{}", e);
        } 
        return processFlag;
	}

	@Override
	protected boolean doProcess(Object reqObj) {
		return false;
	}

}

