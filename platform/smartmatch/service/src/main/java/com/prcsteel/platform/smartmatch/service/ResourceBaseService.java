package com.prcsteel.platform.smartmatch.service;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import com.prcsteel.platform.smartmatch.model.model.ResourceBase;
import com.prcsteel.platform.smartmatch.model.query.ResourceBaseQuery;

/**
 * Created by prcsteel on 2015/11/24.
 */
public interface ResourceBaseService {
	/**
	 * 基础资源查询
	 * 
	 * @param resourceQuery
	 * @return
	 */
	List<ResourceBaseDto> selectResourceBaseList(ResourceBaseQuery resourceBaseQuery);

	/**
	 * 新增记录
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(ResourceBase record, ResourceOperType operType);

}
