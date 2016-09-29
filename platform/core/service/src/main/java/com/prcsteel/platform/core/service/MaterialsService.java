package com.prcsteel.platform.core.service;

import java.util.List;

import com.prcsteel.platform.core.model.model.Materials;


public interface MaterialsService {
	public List<Materials> queryMaterials(String categoryUuid);

}
