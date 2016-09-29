package com.prcsteel.platform.smartmatch.service;


import java.util.List;
import java.util.Map;

import com.prcsteel.platform.smartmatch.model.dto.AttributeDataDto;

/**
 * Created by Rabbit on 2015-11-16 15:59:23.
 */
public interface CreatePurchaseOrderService {
	List<Map<String, Object>> getSortAndNsort();

	List<Map<String, Object>> getMaterial(String categoryUUID);

	List<AttributeDataDto> getAttribute(String categoryUUID);

	List<Map<String, Object>> getFactory(String categoryUUID);

	Map<String, Object> getSpec(String categoryUUID, String materialUUID);
}
