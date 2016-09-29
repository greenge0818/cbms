package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.model.ResourceBase;
import com.prcsteel.platform.smartmatch.model.query.ResourceBaseQuery;

public interface ResourceBaseDao {

	int insert(ResourceBase record);

	/**
	 * 基础资源新增
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(ResourceBase record);

	/**
	 * 基础资源查询
	 * 
	 * @param resourceQuery
	 * @return
	 */
	List<ResourceBaseDto> selectResourceBaseList(ResourceBaseQuery resourceBaseQuery);
	
	
	void batchInsertResourceBase (@Param("baseList")List<ResourceBase> baseList);
}