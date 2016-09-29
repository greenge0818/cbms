package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.core.model.model.CategoryNorms;


public interface CategoryNormsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryNorms record);

    int insertSelective(CategoryNorms record);

    CategoryNorms selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryNorms record);

    int updateByPrimaryKey(CategoryNorms record);
    
    List<CategoryNorms> getNormCombineByCategoryUuid(String categoryUuid);
}