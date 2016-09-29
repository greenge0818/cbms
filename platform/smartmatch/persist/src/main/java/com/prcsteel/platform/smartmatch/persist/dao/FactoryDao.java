package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.model.Factory;

public interface FactoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(Factory record);

    int insertSelective(Factory record);

    Factory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Factory record);

    int updateByPrimaryKey(Factory record);
    
    List<Map<String,Object>> selectByFactoryNameAndCityAndBusiness(Map<String, Object> paramMap);
    
    int totalFactory(Map<String, Object> paramMap);

    List<Factory> queryByName(Factory factory);
    

    /**
     * 获取所有指定id的钢厂名
     * @param ids 钢厂ID集合，多个ID逗号分隔。
     * @return
     */
    List<String> queryFactoryNamesByIds(String ids);
    
    /**
	 * 获取所有钢厂
	 * @return
	 */
	List<Factory> getAllFactory();
	
	List<Factory> getRepeatAlias(Factory factory);
	
	/**
	 * 根据钢厂名称查询钢厂信息
	 * @param name
	 * @return
	 */
	Factory selectByName(@Param("name") String name);
}