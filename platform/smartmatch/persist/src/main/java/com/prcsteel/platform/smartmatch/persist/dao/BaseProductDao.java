package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.dto.BaseProductDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.model.BaseProduct;

public interface BaseProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseProduct record);

    int insertSelective(BaseProduct record);

    BaseProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseProduct record);

    int updateByPrimaryKey(BaseProduct record);
    
    /**
     * 通过卖家名称、品名、材质、规格、产地、仓库、城市查询
     * @param baseProductQuery
     * @return
     */
    BaseProductDto selectBaseProductForSave(ResourceDto resourceDto);
}