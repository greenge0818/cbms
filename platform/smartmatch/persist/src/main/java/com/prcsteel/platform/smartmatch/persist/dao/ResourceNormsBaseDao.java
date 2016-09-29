package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.model.ResourceNormsBase;

public interface ResourceNormsBaseDao {
	int deleteByPrimaryKey(Long id);

	int insert(ResourceNormsBase record);

	int insertSelective(ResourceNormsBase record);

	ResourceNormsBase selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ResourceNormsBase record);

	int updateByPrimaryKey(ResourceNormsBase record);
	
}