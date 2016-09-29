package com.prcsteel.platform.order.persist.dao;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.CategoryMaterials;

/** 
 * created by peanut on ：2016年1月14日 下午12:30:35   
 */

public interface CategoryMaterialsDao {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryMaterials record);

    int insertSelective(CategoryMaterials record);

    CategoryMaterials selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryMaterials record);

    int updateByPrimaryKey(CategoryMaterials record);
    
    /**
     * 根据材质uuid删除品名材质关联表
     * @param materialUuid   材质uuid
     */
	void deleteByMaterialUuid(String materialUuid);
	
	/**
	 * 根据材质uuid和品名uuid查询品名材质表
	 * @param categoryUuid    品名uuid
	 * @param materialUuuid   材质uuid
	 * @return
	 */
	CategoryMaterials selectByMaterialUuidAndCategoryUuid(@Param("categoryUuid")String categoryUuid,
			@Param("materialUuuid")	String materialUuuid);
}