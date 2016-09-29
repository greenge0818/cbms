package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.core.model.dto.MaterialMgtDto;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.model.query.MaterialsMgtQuery;
import org.apache.ibatis.annotations.Param;

public interface MaterialsDao {
    int deleteByPrimaryKey(Long id);

    int insert(Materials record);

    int insertSelective(Materials record);

    Materials selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Materials record);

    int updateByPrimaryKey(Materials record);
    
    List<Materials> queryMaterials(String categoryUuid);
    
    
    /**
	 * 品规设置中材质管理列表总数
	 * @param rq
	 * @return
	 */
	Integer totalForMaterialMgt(MaterialsMgtQuery rq); // add by peanut on 2016-1-13
	
	/**
	 * 根据品名和材质名称搜索
	 * @param mmq
		   categoryName 品名名称
		   materialName 材质名称
		   isVague 是否模糊匹配
	 * @return
	 */
	List<MaterialMgtDto> selectByCategoryNameAndMaterialName(MaterialsMgtQuery mmq); // add by peanut on 2016-1-13
	
	/**
	 * 根据品名名称搜索
	 */
	Materials selectByName(@Param("materialName") String  materialName); // add by peanut on 2016-1-14
	
	List<Materials> selectByMaterialsName(@Param("materialName") String  materialName);
	
	Materials selectByMaterialUuid(@Param("materialUuid") String  materialUuid);
}