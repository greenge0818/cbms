package com.prcsteel.platform.smartmatch.service.impl;

import javax.annotation.Resource;

import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.service.CopyResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 复制资源数据
 * 
 * @author peanut  <p> 2016年2月17日 下午2:51:50</p>   
 */
@Service
public class CopyResourceServiceImpl implements CopyResourceService {
	@Resource
	private ResourceDao resourceDao;
	@Resource
	private ResourceNormsDao resourceNormsDao;
	
	/**
	 *复制资源数据
	 *
	 *<p>每次执行都将资源表(包括资源表cust_resource和资源规格表cust_resource_norms)中的所有资源数据复制至历史表中（以版本号和版本号日期加以区分）</p>
	 */
	@Override
	@Transactional
	public void doCopyResource() {
//		System.out.println("this is copy resource job test ！");
		//复制资源表
		resourceDao.doCopyResource();
		//复制资源规格表
		resourceNormsDao.doCopyResourceNorms();
	}
	
}
