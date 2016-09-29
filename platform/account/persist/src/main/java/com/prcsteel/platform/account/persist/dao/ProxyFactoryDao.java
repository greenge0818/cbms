package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import com.prcsteel.platform.account.model.model.ProxyFactory;

public interface ProxyFactoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(ProxyFactory record);

    int insertSelective(ProxyFactory record);

    ProxyFactory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProxyFactory record);

    int updateByPrimaryKey(ProxyFactory record);
    
	public List<ProxyFactory> queryProxyFactoryList(long accountId);
	
}