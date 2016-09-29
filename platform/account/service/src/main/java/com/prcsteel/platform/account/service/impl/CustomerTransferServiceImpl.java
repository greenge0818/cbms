package com.prcsteel.platform.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.dto.CustomerTransferDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAssignLogs;
import com.prcsteel.platform.account.model.model.AccountManager;
import com.prcsteel.platform.account.model.model.AccountManagerAssignLogs;
import com.prcsteel.platform.account.model.query.CustomerTransferQuery;
import com.prcsteel.platform.account.persist.dao.AccountAssignDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountManagerAssignLogsDao;
import com.prcsteel.platform.account.persist.dao.AccountManagerDao;
import com.prcsteel.platform.account.service.CustomerTransferService;

/**
 * Created by dq on 2016/01/11.
 * 
 * modify_date:2016-3-16
 * modify_author :zhoucai@prcsteel.com
 * descript:1：新增方法queryIsAdminCount函数 查询当前客户是否为管理员
 */
 
@Service("customerTransferService")
public class CustomerTransferServiceImpl implements CustomerTransferService {

	@Resource
	private AccountDao accountDao;
	
	@Resource
	private AccountAssignDao accountAssignDao;
	
	@Resource
	private AccountManagerDao accountManagerDao;
	
	@Resource
	private AccountManagerAssignLogsDao accountManagerAssignLogsDao;
	
	
	@Override
	public List<CustomerTransferDto> querCustomerTransferList(CustomerTransferQuery query) {
		return accountDao.querCustomerTransferList(query);
	}

	@Override
	public Integer querCustomerTransferCount(CustomerTransferQuery query) {
		return accountDao.querCustomerTransferCount(query);
	} 

	@Override
	public int queryIsAdminCount(String loginId) {
		return accountDao.queryIsAdminCount(loginId);
	} 
	@Transactional
	@Override
	//modify by zhoucai@prcsteel.com 注释交易员根性的部分程序
	public void customerTransferAction(CustomerTransferQuery query) {
		Long userId = query.getUser().getId();
		String userName = query.getUser().getName();
		Date date = new Date();

		AccountAssignLogs accountAssignLogs = new AccountAssignLogs();
		Account account = accountDao.selectByPrimaryKey(query.getAccountId());
		accountAssignLogs.setOrgExId(account.getOrgId());
		accountAssignLogs.setOrgExName(account.getOrgName());
		
		accountAssignLogs.setAccountId(query.getAccountId());
		accountAssignLogs.setRegTime(account.getCreated());
		accountAssignLogs.setOrgNextId(query.getOrgId());
		accountAssignLogs.setOrgNextName(query.getOrgName());
		
		accountAssignLogs.setAssignerId(userId);
		accountAssignLogs.setAssignerName(userName);
		accountAssignLogs.setLastUpdated(date);
		accountAssignLogs.setLastUpdatedBy(userName);
		accountAssignLogs.setCreated(date);
		accountAssignLogs.setCreatedBy(userName);
		accountAssignLogs.setModificationNumber(0);

		/*if("1".equals(query.getTransferType())) {
			query.setOrgId(null);
			query.setOrgName(null);
			accountAssignLogs.setOrgExId(null);
			accountAssignLogs.setOrgExName(null);
			accountAssignLogs.setOrgNextId(null);
			accountAssignLogs.setOrgNextName(null);
		}*/
		accountAssignDao.insertSelective(accountAssignLogs);
		
		account.setOrgId(query.getOrgId());
		account.setOrgName(query.getOrgName());
		account.setLastUpdated(date);
		account.setLastUpdatedBy(userName);
		accountDao.updateByPrimaryKeySelective(account);
		//int flag;
		/*
		if("1".equals(query.getTransferType())) {
			List<AccountManager> managers = accountManagerDao.queryManagerList(query.getAccountId());
			if(null != managers && managers.size() > 0) {
				flag = accountManagerDao.updateIsDeletedForIds(managers.stream().map(a -> a.getId()).collect(Collectors.toList()),1);
			}
			
			AccountManager manager = new AccountManager();
			manager.setAccountId(query.getAccountId());
			manager.setManagerId(query.getManagerId());
			manager.setManagerName(query.getManagerName());
			
			manager.setLastUpdated(date);
			manager.setLastUpdatedBy(userName);
			manager.setCreated(date);
			manager.setCreatedBy(userName);
			manager.setModificationNumber(0);
			accountManagerDao.insertSelective(manager);
			
			Long assignLogsId = accountAssignLogs.getId();
			List<AccountManagerAssignLogs> list = new ArrayList<AccountManagerAssignLogs>();
			managers.forEach(mg -> {
				AccountManagerAssignLogs log = new AccountManagerAssignLogs();
				log.setAssignLogs(assignLogsId);
				log.setAssignLogsType((byte) 0);
				log.setManagerId(mg.getManagerId());
				log.setManagerName(mg.getManagerName());
				
				log.setLastUpdated(date);
				log.setLastUpdatedBy(userName);
				log.setCreated(date);
				log.setCreatedBy(userName);
				log.setModificationNumber(0);
				list.add(log);
			});
			AccountManagerAssignLogs temp = new AccountManagerAssignLogs();
			temp.setAssignLogs(assignLogsId);
			temp.setAssignLogsType((byte) 1);
			temp.setManagerId(query.getManagerId());
			temp.setManagerName(query.getManagerName());
			
			temp.setLastUpdated(date);
			temp.setLastUpdatedBy(userName);
			temp.setCreated(date);
			temp.setCreatedBy(userName);
			temp.setModificationNumber(0);
			list.add(temp);
			
			flag = accountManagerAssignLogsDao.batchInsert(list);
		}*/
	}


}
