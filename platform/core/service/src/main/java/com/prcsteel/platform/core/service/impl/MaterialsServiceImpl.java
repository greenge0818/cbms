package com.prcsteel.platform.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.persist.dao.MaterialsDao;
import com.prcsteel.platform.core.service.MaterialsService;

@Service("materialsService")
public class MaterialsServiceImpl implements MaterialsService {

	@Resource
	private MaterialsDao materialsDao;
	
	@Override
	public List<Materials> queryMaterials(String categoryUuid) {
		return materialsDao.queryMaterials(categoryUuid);
	}

}
