package com.prcsteel.platform.smartmatch.persist.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.smartmatch.model.model.CategoryWeight;
import com.prcsteel.platform.smartmatch.model.query.CatagoryWeightQuery;
import com.prcsteel.platform.smartmatch.model.query.SingleWeightQuery;

public interface CategoryWeightDao {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryWeight record);

    int insertSelective(CategoryWeight record);

    CategoryWeight selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryWeight record);

    int updateByPrimaryKey(CategoryWeight record);
    
    List<Map<String,Object>> selectByNameAndBusinessAndMaterialAndNorms(CatagoryWeightQuery catagoryWeightQuery);
    
    List<Map<String,Object>> selectById(Long id);
    
    int totalCategoryWeight(CatagoryWeightQuery catagoryWeightQuery);
    
    String selectFactoryName(Long id);
    
    String selectCategoryName(Long id);
    
    String selectMaterialName(Long id);
    
    List<Map<String,Object>> queryAllMaterials();

    BigDecimal selectSingleWeightByParamIds(SingleWeightQuery query);
    
    int selectByFactoryId(Long factoryId);
    
    int selectByParameter(CategoryWeight categoryWeight);
}