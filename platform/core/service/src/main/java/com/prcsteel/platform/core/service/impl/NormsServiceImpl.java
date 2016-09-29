package com.prcsteel.platform.core.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.core.model.model.CategoryNorms;
import com.prcsteel.platform.core.persist.dao.CategoryNormsDao;
import com.prcsteel.platform.core.service.NormsService;

/**
 * Created by Green.Ge on 2015/11/18.
 */
@Service("normsService")
public class NormsServiceImpl implements NormsService {
	@Resource
	private CategoryNormsDao categoryNormsDao;
	
	@Override
	public String getUuidCombineByCategoryUuid(String categoryUuid) {
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(categoryUuid);
		List<String> uuidList=list.stream().map(a->a.getNormsUuid()).collect(Collectors.toList());
		return StringUtils.join(uuidList,"*");
		
	}
	
}