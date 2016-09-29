package com.prcsteel.platform.core.service;

import java.util.List;

import com.prcsteel.platform.core.model.dto.CategoryGroupRecordDto;

/**
 * Created by chenchen on 2015/8/6.
 */
public interface GroupForCategoryService {
	
	/**
	 * 获取所有品类
	 * @return
	 */
	public List<CategoryGroupRecordDto> getAllCategory();
	
	
	
	
}
