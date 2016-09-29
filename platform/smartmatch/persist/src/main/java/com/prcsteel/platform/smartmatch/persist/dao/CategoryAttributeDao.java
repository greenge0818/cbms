package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.BaseCategoryAttributeDto;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.core.model.dto.CategoryAttributeDto;
import com.prcsteel.platform.core.model.model.CategoryAttribute;
import com.prcsteel.platform.smartmatch.model.query.CategoryAttributeQuery;

public interface CategoryAttributeDao {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryAttribute record);

    int insertSelective(CategoryAttribute record);

    CategoryAttribute selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryAttribute record);

    int updateByPrimaryKey(CategoryAttribute record);
    
	/**
	 * 根据查询query对 查找总数
	 */
	Integer searchTotalCategoryAttribute(CategoryAttributeQuery caq);
	/**
	 * 根据查询query对象 查找数据
	 */
	List<CategoryAttributeDto> searchCategoryAttribute(CategoryAttributeQuery caq);
	/**
	 * 根据品名uuid删除品名属性记录
	 * @param categoryUuid
	 * @return
	 */
	Integer delByUuid(String categoryUuid);
	
	/**
	 * 根据uuid查询CategoryAttribute
	 * @param categoryUuid       品名uuid
	 * @return
	 */
	List<CategoryAttribute> findByUuid(@Param("categoryUuid") String categoryUuid);

	/**
	 * @description:根据品名id查询对应的属性集合
	 * @param categoryUuid
	 * @author :zhoucai
	 * @date 2016-6-23
	 * @return
	 */
	List<BaseCategoryAttributeDto> searchAttributeByCategoryUuid(String categoryUuid);
}