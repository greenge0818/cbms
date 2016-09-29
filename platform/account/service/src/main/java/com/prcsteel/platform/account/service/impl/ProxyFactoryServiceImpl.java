package com.prcsteel.platform.account.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.model.model.ProxyFactory;
import com.prcsteel.platform.account.persist.dao.ProxyFactoryDao;
import com.prcsteel.platform.account.service.ProxyFactoryService;
import com.prcsteel.platform.acl.model.model.User;

/**
 * 代理钢厂服务实现
 * @author tangwei
 *
 */
@Service("proxyFactoryService")
public class ProxyFactoryServiceImpl implements ProxyFactoryService { 

	@Resource
	private ProxyFactoryDao proxyFactoryDao;
	
	private String [] splits;
	
	@Override
	public List<ProxyFactory> findProxyFactoryByAccountId(long accountId) {
		List<ProxyFactory> result = proxyFactoryDao.queryProxyFactoryList(accountId);
		for(ProxyFactory factory:result){
			factory.setQuantity(convertDecimal(factory.getQuantity()));
			factory.setStock(convertDecimal(factory.getStock()));
		}
		return result;
	}
	
	@Override
	public int deleteProxyFactory(long id,User user) {
		ProxyFactory record = new ProxyFactory();
		record.setIsDeleted(1);
		record.setId(id);
		record.setLastUpdatedBy(user.getName());
		return proxyFactoryDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int addSaveProxyFactory(ProxyFactory record, User user) {
		record.setCreatedBy(user.getName());
		record.setLastUpdatedBy(user.getName());
		return proxyFactoryDao.insertSelective(record);
	}

	@Override
	public int editSaveProxyFactory(ProxyFactory record, User user) {
		record.setLastUpdatedBy(user.getName());
		return proxyFactoryDao.updateByPrimaryKeySelective(record);
	}
	
	private BigDecimal convertDecimal(BigDecimal number){
		splits = number.toString().split("\\.");
		if(splits.length > 1){
			if(Integer.parseInt(splits[1]) > 0){
				return number;
			}
			return new BigDecimal(splits[0]);
		}
		return number;
	}
}
