package com.prcsteel.platform.smartmatch.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.CategoryWeightDto;
import com.prcsteel.platform.smartmatch.model.model.CategoryWeight;
import com.prcsteel.platform.smartmatch.model.query.CatagoryWeightQuery;
import com.prcsteel.platform.smartmatch.model.query.SingleWeightQuery;

public interface CategoryWeightService {
	List<Map<String, Object>> selectByNameAndBusinessAndMaterialAndNorms(CatagoryWeightQuery catagoryWeightQuery);

	int totalCategoryWeight(CatagoryWeightQuery catagoryWeightQuery);
	
	CategoryWeightDto selectByPrimaryKey(Long id);

	List<Map<String, Object>> selectById(Long id);
	
	void deleteByPrimaryKey(Long id);
	
	
	public void addCategoryWeight(CategoryWeight categoryWeigh,User user);

	public void updateCategoryWeight(CategoryWeight categoryWeight,User user);
	
	public List<Map<String, Object>> getAllmaterial();

	BigDecimal selectSingleWeightByParamIds(SingleWeightQuery query);
}
