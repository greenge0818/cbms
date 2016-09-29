package com.prcsteel.platform.core.service.impl;

import com.prcsteel.platform.core.model.dto.CategoryGroupRecordDto;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.GroupForCategoryDao;
import com.prcsteel.platform.core.service.GroupForCategoryService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenchen on 2015/8/6.
 */
@Service("groupForCategoryService")
public class GroupForCategoryServiceImpl implements GroupForCategoryService {
	private static final Logger logger = Logger.getLogger(GroupForCategoryServiceImpl.class);
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private GroupForCategoryDao groupForCategoryDao;
	@Autowired
	private CategoryGroupDao categoryGroupDao;
	
	@Override
	public List<CategoryGroupRecordDto> getAllCategory() {
		List<CategoryGroupRecordDto> list = groupForCategoryDao.queryAllReordBySite("inner_site");
		return list;
	}
	
	

}
