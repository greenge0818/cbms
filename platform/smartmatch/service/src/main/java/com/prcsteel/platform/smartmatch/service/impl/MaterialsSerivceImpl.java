package com.prcsteel.platform.smartmatch.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.persist.dao.MaterialsDao;
import com.prcsteel.platform.smartmatch.service.MaterialsSerivice;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.dto.MaterialMgtDto;
import com.prcsteel.platform.order.model.model.CategoryMaterials;
import com.prcsteel.platform.core.model.query.MaterialsMgtQuery;
import com.prcsteel.platform.order.persist.dao.CategoryMaterialsDao;

/** 
 * @author  peanut
 * @date 创建时间：2016年1月13日 下午3:51:33   
 */
@Service
public class MaterialsSerivceImpl implements MaterialsSerivice {
	
	@Resource
	private MaterialsDao materialsDao;
	@Resource
	private CategoryMaterialsDao categoryMaterialsDao;

	/**
	 * 品规设置中材质管理列表总数
	 * @param mmq
	 * @return
	 */
	@Override
	public Integer totalForMaterialMgt(MaterialsMgtQuery mmq) {
		return materialsDao.totalForMaterialMgt(mmq);
	}

	/**
	 * 根据品名和材质名称搜索
	 * @param mmq
		   categoryName 品名名称
		   materialName 材质名称
		   isVague 是否模糊匹配
	 * @return
	 */
	@Override
	public List<MaterialMgtDto> selectByCategoryNameAndMaterialName(MaterialsMgtQuery mmq) {
		return materialsDao.selectByCategoryNameAndMaterialName(mmq);
	}
	
	/**
	 * 材质管理资源保存
	 *  
	 * @param mmDto   材质管理列表数据对象
	 * @param user
	 */
	@Override
	@Transactional
	public void doSaveForMaterialMgt(MaterialMgtDto mmDto,User user) {
		if(mmDto==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "无可保存的材质");
		}
		String materialName=mmDto.getMaterialName();
		if(StringUtils.isBlank(materialName)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "材质为空!");
		}
		String categoryName=mmDto.getCategoryName();
		String categoryUuid=mmDto.getCategoryUuid();
		if(StringUtils.isBlank(categoryName) || StringUtils.isBlank(categoryUuid)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "品名为空!");
		}
		
		Materials m=materialsDao.selectByName(materialName);
		String materialUuid="";
		//要操作的材质是否存在
		if(m==null){
			//不存在，新增
			Materials material=new Materials();
			materialUuid=UUID.randomUUID().toString();
			material.setUuid(materialUuid);
			material.setName(materialName);
			material.setCreated(new Date());
			material.setCreatedBy(user.getLoginId());
			material.setLastUpdated(new Date());
			material.setLastUpdatedBy(user.getLoginId());
			
			materialsDao.insertSelective(material);
		}else{
			materialUuid=m.getUuid();
		}
		//查询要更新在关系是否已经存在
		CategoryMaterials cm=categoryMaterialsDao.selectByMaterialUuidAndCategoryUuid(categoryUuid,materialUuid);
		if(mmDto.getCategoryMaterialId()==null){
			//新增关联关系
			if(cm!=null){//要新增的关系已经存在了，报错
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名对应的材质已存在!");
			}else{
				//不存在,新增
				cm=new CategoryMaterials();
				cm.setCategoryUuid(categoryUuid);
				cm.setMaterialsUuid(materialUuid);
				cm.setCreated(new Date());
				cm.setCreatedBy(user.getLoginId());
				cm.setLastUpdated(new Date());
				cm.setLastUpdatedBy(user.getLoginId());
				cm.setRemark(mmDto.getRemark());
				categoryMaterialsDao.insertSelective(cm);
			}
			
		}else{
			//找到原来的关联关系
			CategoryMaterials old = categoryMaterialsDao.selectByPrimaryKey(mmDto.getCategoryMaterialId());
			//做更新
			old.setMaterialsUuid(materialUuid);
			old.setRemark(mmDto.getRemark());
			
			categoryMaterialsDao.updateByPrimaryKey(old);
		}
	}
	
	/**
	 * 材质管理资源删除
	 * <p> 删除材质表记录和品名材质关联表记录</p>
	 * 
	 * @param materialId  材质记录表id
	 */
	@Override
	public void doDeleteMaterial(Long materialId) {
		if(materialId==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "材质记录不存在!");
		}
		String materialUuid=materialsDao.selectByPrimaryKey(materialId).getUuid();
		
		categoryMaterialsDao.deleteByMaterialUuid(materialUuid);
		materialsDao.deleteByPrimaryKey(materialId);
	}
}
