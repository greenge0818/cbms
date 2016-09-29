package com.prcsteel.platform.smartmatch.service;


import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.model.Factory;

/**
 * Created by Wangyx on 2015/11/12.
 */

public interface FactoryService {
	
	List<Map<String,Object>> selectByFactoryNameAndCityAndBusiness(Map<String, Object> paramMap);
	
	int totalFactory(Map<String, Object> paramMap);
	
	public void addFactory(Factory factory, User user);
	
	public void updateFactory(Factory factory, User user);

	Factory selectByPrimaryKey(Long id);

	void deleteByPrimaryKey(Long id);
	
	/**
	 * 获取所有钢厂
	 * @return
	 */
	List<Factory> getAllFactory();

}
