package com.prcsteel.platform.account.service;

import java.util.Date;

import javax.annotation.Resource;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAssignLogs;
import com.prcsteel.platform.account.model.query.CustomerTransferQuery;
import com.prcsteel.platform.account.persist.dao.AccountAssignDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;

/**
 * Created by dq on 2016/2/15.
 */
public class CustomerTransferServiceTest extends BaseTest {

	@Resource
	private CustomerTransferService customerTransferService;
	
	private AccountDao accountDao;
	
	private AccountAssignDao accountAssignDao;
	
	@Before
    public void before() {
		accountDao = EasyMock.createMock(AccountDao.class);
		accountAssignDao = EasyMock.createMock(AccountAssignDao.class);
		
		ReflectionTestUtils.setField(customerTransferService, "accountDao", accountDao);
		ReflectionTestUtils.setField(customerTransferService, "accountAssignDao", accountAssignDao);
		
	}
	
	@After
    public void after() {
        accountDao = null;
        accountAssignDao = null;
    }
	
	@Test
	public void testCustomerTransferAction() {
		CustomerTransferQuery query = new CustomerTransferQuery();
		query.setAccountId(1l);
		query.setOrgId(1l);
		query.setOrgName("长沙服务中心");
		User user = new User();
		user.setId(1l);
		user.setName("admin");
		query.setUser(user);
		Account account = new Account();
		account.setOrgId(1l);
		account.setOrgName("长沙服务中心");
		account.setCreated(new Date());
		
		EasyMock.expect(accountDao.selectByPrimaryKey(query.getAccountId())).andReturn(account);
		EasyMock.expect(accountDao.updateByPrimaryKeySelective(account)).andReturn(1);
        EasyMock.replay(accountDao);//执行测试
        
        EasyMock.expect(accountAssignDao.insertSelective(EasyMock.anyObject())).andReturn(1);
        EasyMock.replay(accountAssignDao);//执行测试
        
        //调用需测试的方法
        try {
        	customerTransferService.customerTransferAction(query);
        } catch (BusinessException e) {
            Assert.assertEquals("客户划转失败", e.getMsg());
        }
        EasyMock.verify(accountDao);//验证结果
        EasyMock.verify(accountAssignDao);//验证结果
	}
	
}
