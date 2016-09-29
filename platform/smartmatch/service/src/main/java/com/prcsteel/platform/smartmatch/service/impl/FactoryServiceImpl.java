package com.prcsteel.platform.smartmatch.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.model.Factory;
import com.prcsteel.platform.smartmatch.persist.dao.CategoryWeightDao;
import com.prcsteel.platform.smartmatch.persist.dao.FactoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;

import com.prcsteel.platform.smartmatch.service.FactoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by Wangyx on 2015/11/12.
 */
@Transactional
@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {
	
	@Resource
	private FactoryDao factoryDao;
	@Resource
	private CategoryWeightDao categoryWeightDao;
	@Resource
	private ResourceDao resourceDao;
	
	@Override
	public List<Map<String, Object>> selectByFactoryNameAndCityAndBusiness(Map<String, Object> paramMap) {
		String factoryName = (String) paramMap.get("factoryName");
		if (StringUtils.isNotEmpty(factoryName)) {
			paramMap.put("factoryName", "%" + factoryName.replaceAll(" ", "")
					+ "%");
		}

		String factoryCity = (String) paramMap.get("factoryCity");
		if (StringUtils.isNotEmpty(factoryCity)) {
			paramMap.put("factoryCity", "%" + factoryCity.replaceAll(" ", "")
					+ "%");
		}
		
		String factoryBusiness = (String) paramMap.get("factoryBusiness");
		if (StringUtils.isNotEmpty(factoryBusiness)) {
			paramMap.put("factoryBusiness", "%" + factoryBusiness.replaceAll(" ", "")
					+ "%");
		}
		
		return factoryDao.selectByFactoryNameAndCityAndBusiness(paramMap);
	}

	@Override
	public int totalFactory(Map<String, Object> paramMap) {
		return factoryDao.totalFactory(paramMap);
	}

	@Override
	public void addFactory(Factory factory, User user) {
		if(factory.getAlias() != "" && factory.getAlias() != null){
			getRepeatAlias(factory);
		}
		factory.setCreated(new Date());
		factory.setLastUpdated(new Date());
		factory.setCreatedBy(user.getLoginId());
		factory.setLastUpdatedBy(user.getLoginId());
		
		if (factoryDao.queryByName(factory).size() > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "钢厂名称跟其他钢厂的名称或者别名重复，请重新输入！");
        }
        if(factoryDao.insertSelective(factory) != 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入钢厂信息数据失败");
        }
	}

	@Override
	public void updateFactory(Factory factory, User user) {
		if(factory.getAlias() != "" && factory.getAlias() != null){
			getRepeatAlias(factory);
		}
		
		factory.setLastUpdatedBy(user.getLoginId());

		if (factoryDao.queryByName(factory).size() > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "钢厂名称跟其他钢厂的名称或者别名重复，请重新输入！");
        }
		if(factoryDao.updateByPrimaryKeySelective(factory) != 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新钢厂数据失败");
        }
	}

	@Override
	public Factory selectByPrimaryKey(Long id) {
		return factoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public void deleteByPrimaryKey(Long id) {
		if (null != factoryDao.selectByPrimaryKey(id)){
			if(resourceDao.selectByFactoryId(id).size() > 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该钢厂存在相关资源，不能删除该钢厂！");
			}else if(categoryWeightDao.selectByFactoryId(id) > 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该钢厂存在对应的单件重量，不能删除该钢厂！");
			}else{
				if(factoryDao.deleteByPrimaryKey(id) != 1){
		            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除钢厂信息失败");
		        }
			}
			
		}else{
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到该钢厂信息");
		}
	}

	@Override
	public List<Factory> getAllFactory() {
		return factoryDao.getAllFactory();
	}
	
	/**
	 * 钢厂别名查重
	 * @param factory
	 */
	private void getRepeatAlias(Factory factory){
		List<Factory> repeatFactoryList = factoryDao.getRepeatAlias(factory);		
		if(repeatFactoryList.size() > 0){			
			List<String> aliasList = new ArrayList<String>(Arrays.asList(factory.getAlias().split(",")));
			for(Factory repeatFactory : repeatFactoryList){		
				if(repeatFactory.getAlias() != null && repeatFactory.getAlias() != ""){
					List<String> repeatAliasList = new ArrayList<String>(Arrays.asList(repeatFactory.getAlias().split(",")));
					repeatAliasList.retainAll(aliasList);
					if(repeatAliasList.size() > 0){
						String repeatAlias = String.join(",", (String[]) repeatAliasList.toArray(new String[repeatAliasList.size()]));
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "别名[" + repeatAlias + "]和钢厂[" + repeatFactory.getName() + "]别名相同，请重新输入！");
					}
				}
				List<String> repeatFactoryName = new ArrayList<String>(Arrays.asList(repeatFactory.getName()));
				aliasList.retainAll(repeatFactoryName);
				if(aliasList.size() > 0){
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "别名" + aliasList + "和钢厂[" + repeatFactory.getName() + "]名字相同，请重新输入！");
				}
				
			}
		}
		
	}
}
