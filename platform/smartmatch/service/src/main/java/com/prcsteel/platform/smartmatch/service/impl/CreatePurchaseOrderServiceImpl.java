package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.core.model.dto.BaseDataDto;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.dto.CategoryInfoDto;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.smartmatch.model.dto.AttributeDataDto;
import com.prcsteel.platform.smartmatch.persist.dao.CreatePruchaseOrderDao;

import com.prcsteel.platform.smartmatch.service.CreatePurchaseOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Rabbit on 2015-11-16 16:03:31.
 */
@Service("materialService")
public class CreatePurchaseOrderServiceImpl implements CreatePurchaseOrderService {
	@Autowired
	private CategoryDao categoryDao;
	@Resource
	private CategoryGroupDao categoryGroupDao;
	@Resource
	private CreatePruchaseOrderDao createPruchaseOrderDao;
	/**
	 * 根据所有小类和大类uuid
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSortAndNsort() {
		List<Map<String, Object>> data = new LinkedList<>();
		/**
		 *  Map<String, Object>
		 statusCode 0
		 Massage 请求成功
		 total count()
		 isLogin false
		 data        List<Map<String, Object>>
		             sortID
		             sortName
		             classInfo     List<Map<String, Object>>
		                           classID
		                           className
		                           nsort         List<Map<String, Object>>
		                                         nsortID
		                                         nsortName
		                                         spec
		                                                1,2,3,4,5
		 */
		List<CategoryGroupDto> parentCategoryGroupList = categoryGroupDao.queryAllParentCategoryGroupOuter();
		List<CategoryGroupDto> categoryGroupList = categoryGroupDao.queryAllCategoryGroupOuter();
		List<CategoryInfoDto> categoryList = categoryDao.queryAllCategoryAndGroup();
		if (parentCategoryGroupList.size() >= 1 && categoryList.size() >= 1) {
			Map<String, List<CategoryGroupDto>> categoryGroup = categoryGroupList.stream().collect(Collectors.groupingBy(CategoryGroupDto::getParentUuid));
			Map<String, List<CategoryInfoDto>> category = categoryList.stream().collect(Collectors.groupingBy(CategoryInfoDto::getGroupUuid));
			for (CategoryGroupDto dto : parentCategoryGroupList) {
				Map<String, Object> tempData = new HashMap<>();
				List<Map<String, Object>> classInfo = new LinkedList<>();
				tempData.put("sortID", dto.getCategoryGroupUuid());
				tempData.put("sortName", dto.getCategoryGroupName());
				tempData.put("classInfo", classInfo);
				data.add(tempData);
				List<CategoryGroupDto> categoryGroupListGroupingByParentUuid = categoryGroup.get(dto.getCategoryGroupUuid());
				for(CategoryGroupDto group : categoryGroupListGroupingByParentUuid) {
					Map<String, Object> classInfoMap = new HashMap<>();
					classInfoMap.put("classID", group.getCategoryGroupUuid());
					classInfoMap.put("className", group.getCategoryGroupName());
					classInfo.add(classInfoMap);
					List<Map<String, Object>> nsortList = new LinkedList<>();
					classInfoMap.put("nsort", nsortList);
					List<CategoryInfoDto> categoryListGroupingByGroupUuid = category.get(group.getCategoryGroupUuid());
					for (CategoryInfoDto categoryInfoDto : categoryListGroupingByGroupUuid) {
						Map<String, Object> nsort = new HashMap<>();
						nsort.put("nsortID", categoryInfoDto.getUuid());
						nsort.put("nsortName", categoryInfoDto.getName());
						nsort.put("nsortIsEcShow", categoryInfoDto.getIsEcShow());
						Map<String, Object> spec = new LinkedHashMap<>();
						List<BaseDataDto> specList = createPruchaseOrderDao.selectNormsByCategoryUUID(categoryInfoDto.getUuid());
						for(int i = 0; i < specList.size(); i++){
							spec.put("spec" + (i+1), specList.get(i).getName());
						}
						nsort.put("specName", spec);
						nsortList.add(nsort);
					}
				}
			}
		}
		return data;
	}

	@Override
	public List<AttributeDataDto> getAttribute(String categoryUUID){
		return createPruchaseOrderDao.selectAttributeByCategoryUUID(categoryUUID);
	}

	@Override
	public List<Map<String, Object>> getMaterial(String categoryUUID){
		List<Map<String, Object>> data = new LinkedList<>();
		for(BaseDataDto material : createPruchaseOrderDao.selectMaterialByCategoryUUID(categoryUUID)){
			Map<String, Object> map = new HashMap<>();
			map.put("material", material);
			data.add(map);
		}
		return data;
	}

	@Override
	public List<Map<String, Object>> getFactory(String categoryUUID){
		List<Map<String, Object>> data = new LinkedList<>();
		for(BaseDataDto factory: createPruchaseOrderDao.selectFactoryByCategoryUUID(categoryUUID)){
			Map<String, Object> map = new HashMap<>();
			map.put("factory", factory);
			data.add(map);
		}
		return data;
	}

	@Override
	public Map<String, Object> getSpec(String categoryUUID, String materialUUID){
		List<BaseDataDto> specList = createPruchaseOrderDao.selectNormsByCategoryUUID(categoryUUID);
		Map<String, Object> spec = new LinkedHashMap<>();
		for(int i = 0; i < specList.size(); i++){
			List<String> normsList = createPruchaseOrderDao.selectNormsByCategoryUUIDAndMaterialNorms(categoryUUID, materialUUID, specList.get(i).getUuid());
			List<Map<String, Object>> normsMap = new LinkedList<>();
			for(String norm : normsList) {
				Map<String, Object> norms = new LinkedHashMap<>();
				norms.put("spec", norm);
				normsMap.add(norms);
			}
			spec.put("spec" + (i + 1) + "List", normsMap);
		}
		return spec;
	}
}