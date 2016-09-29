package com.prcsteel.platform.smartmatch.service;

import java.util.List;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.dto.MaterialMgtDto;
import com.prcsteel.platform.core.model.query.MaterialsMgtQuery;

/** 
 * 材质相关的业务
 * @author  peanut
 * @date 创建时间：2016年1月13日 下午3:18:27   
 */
public interface MaterialsSerivice {
	
	/**
	 * 品规设置中材质管理列表总数
	 * @param mmq
	 * @return
	 */
	Integer totalForMaterialMgt(MaterialsMgtQuery mmq);
	
	/**
	 * 根据品名和材质名称搜索
	 * @param mmq
		   categoryName 品名名称
		   materialName 材质名称
		   isVague 是否模糊匹配
	 * @return
	 */
	List<MaterialMgtDto> selectByCategoryNameAndMaterialName(MaterialsMgtQuery mmq); 

	/**
	 * 材质管理资源保存
	 * @param mmDto
	 * @param user
	 */
	void doSaveForMaterialMgt(MaterialMgtDto mmDto,User user);
	
	/**
	 * 材质管理资源删除
	 * @param materialId  材质记录表id
	 */
	void doDeleteMaterial(Long materialId);
	
}
