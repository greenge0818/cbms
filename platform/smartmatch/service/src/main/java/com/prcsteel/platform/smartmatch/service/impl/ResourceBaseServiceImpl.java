package com.prcsteel.platform.smartmatch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import com.prcsteel.platform.smartmatch.model.model.ResourceBase;
import com.prcsteel.platform.smartmatch.model.query.ResourceBaseQuery;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceBaseDao;
import com.prcsteel.platform.smartmatch.service.ResourceBaseService;

@Service("resourceBaseService")
public class ResourceBaseServiceImpl implements ResourceBaseService {
	@Resource
	private ResourceBaseDao resourcebaseDao;

	/**
	 * 基础资源查询
	 * 
	 * @param resourceQuery
	 * @return
	 */
	@Override
	public List<ResourceBaseDto> selectResourceBaseList(ResourceBaseQuery resourceBaseQuery) {
		return resourcebaseDao.selectResourceBaseList(resourceBaseQuery);
	}

	/**
	 * 基础资源新增
	 * 
	 */
	@Override
	public int insertSelective(ResourceBase record, ResourceOperType operType) {
		if (record == null) {
			return 0;
		}
		record.setOperType(operType.getCode());

		return resourcebaseDao.insertSelective(record);
	}

}
