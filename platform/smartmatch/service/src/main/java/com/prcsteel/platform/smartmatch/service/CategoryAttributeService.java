package com.prcsteel.platform.smartmatch.service;


import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.BaseCategoryAttributeDto;
import com.prcsteel.platform.core.model.dto.CategoryAttributeDto;
import com.prcsteel.platform.core.model.model.CategoryAttribute;
import com.prcsteel.platform.smartmatch.model.query.CategoryAttributeQuery;

/**
 *  属性设置
 * create by peanut on 2015-11-24
 */
public interface CategoryAttributeService {
	/**
	 * 添加多个CategoryAttribute记录
	 * @param categoryUuid       品规uuid
	 * @param attributeUuid      属性uuid数组
	 * @param loginId            用户id
	 * @throws Exception
	 */
	void addMultCategoryAttributes(String categoryUuid, List<String> attributeUuid, String loginId);
	
	/**
	 * 根据大类名称或者品名名称 查找数据
	 */
	List<CategoryAttributeDto> searchByCategoryNameOrGroupName(CategoryAttributeQuery caq);
	
	/**
	 * 根据大类名称或者品名名称 查找总数
	 */
	Integer searchTotalByCategoryNameOrGroupName(CategoryAttributeQuery caq);
	
	/**
	 * 根据大类Uuid 查找所有扩展属性
	 */
	List<CategoryAttribute> searchByCategoryUuid(String categoryUuid);
	
	/**
	 * 根据品名uuid删除其对应的一至多条属性记录
	 * @param categoryUuid
	 */
	void delCategoryAttributeByUuid(String categoryUuid);
	
	/**
	 * 根据uuid查询CategoryAttribute
	 * @param categoryUuid       品名uuid
	 * @return
	 */
	List<CategoryAttribute> findByUuid(String categoryUuid);
	
	/**
	 * 更新属性uuid  (先做删除再重新添加)
	 * @param list    记录集
	 * @param attributeUuid   新的属性集
	 * @param loginId   
	 */
	void updateMultAttributUuid(List<CategoryAttribute> list,
			List<String> attributeUuid, String loginId);

	/**
	 * @description:根据品名id查询对应的属性集合
	 * @param categoryUuid
	 * @author :zhoucai
	 * @date 2016-6-23
	 * @return
	 */
	List<BaseCategoryAttributeDto> searchAttributeByCategoryUuid(String categoryUuid);
	
}
