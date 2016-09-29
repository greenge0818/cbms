package com.prcsteel.platform.smartmatch.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.BaseCategoryAttributeDto;
import com.prcsteel.platform.core.model.dto.CategoryAttributeDto;
import com.prcsteel.platform.core.model.model.CategoryAttribute;
import com.prcsteel.platform.smartmatch.model.query.CategoryAttributeQuery;
import com.prcsteel.platform.smartmatch.persist.dao.CategoryAttributeDao;

import com.prcsteel.platform.smartmatch.service.CategoryAttributeService;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.common.constants.Constant;

/**
 *  属性设置
 * create by peanut on 2015-11-24
 */

@Service
public class CategoryAttributeServiceImpl implements CategoryAttributeService {
	@Resource 
	private CategoryAttributeDao categoryAttributeDao;
	
	@Transactional
	@Override
	public void addMultCategoryAttributes(String categoryUuid,
			List<String> attributeUuid, String loginId) {
		
		if(StringUtils.isEmpty(categoryUuid)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "categoryUuid参数不能为空");
		}
		if(attributeUuid!=null && attributeUuid.size()>0){
			for(int i=0;i<attributeUuid.size();i++){
				CategoryAttribute ca=generateCateAttribute(categoryUuid,attributeUuid.get(i),loginId);
				categoryAttributeDao.insertSelective(ca);
			}
		}else{ // 插入无属性的记录
			CategoryAttribute ca=generateCateAttribute(categoryUuid,null,loginId);
			categoryAttributeDao.insertSelective(ca);
		}
	}
	@Transactional
	@Override
	public void updateMultAttributUuid(List<CategoryAttribute> list,
			List<String> attributeUuid, String loginId){
		
		if(attributeUuid ==null || attributeUuid.size()<=0){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "所选属性参数不能为空!");
		}
		if(list ==null ||list.isEmpty()){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "未选择品名!");
		}
		String categoryUuid=list.get(0).getCategoryUuid();
		//先删除记录
		delCategoryAttributeByUuid(categoryUuid);
		//再重新添加
		addMultCategoryAttributes(categoryUuid,attributeUuid,loginId);
	}
	
	@Override
	public List<CategoryAttributeDto> searchByCategoryNameOrGroupName(CategoryAttributeQuery caq) {
		
		return categoryAttributeDao.searchCategoryAttribute(caq);
	}
	
	@Override
	public Integer searchTotalByCategoryNameOrGroupName(
			CategoryAttributeQuery caq) {
		return categoryAttributeDao.searchTotalCategoryAttribute(caq);
	}
	
	@Override
	public List<CategoryAttribute> searchByCategoryUuid(String categoryUuid) {
		return categoryAttributeDao.findByUuid(categoryUuid);
	}
	
	@Override
	public void delCategoryAttributeByUuid(String categoryUuid) {
		if(StringUtils.isEmpty(String.valueOf(categoryUuid))){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "品名uuid参数不能为空");
		}
		Integer effect=categoryAttributeDao.delByUuid(categoryUuid);
		if(effect<=0){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "属性记录删除失败");
		}
	}
	@Override
	public List<CategoryAttribute> findByUuid(String categoryUuid) {
		return categoryAttributeDao.findByUuid(categoryUuid);
	}
	/**
	 * 生成CategoryAttribute对象 
	 * @param categoryUuid      品规uuid
	 * @param attributeUuid     属性uuid
	 * @param loginId           操作人id
	 * @return
	 */
	private CategoryAttribute generateCateAttribute(String categoryUuid, String attributeUuid,String loginId){
		CategoryAttribute categoryAttribute=new CategoryAttribute();
		categoryAttribute.setAttributeUuid(attributeUuid);
		categoryAttribute.setCategoryUuid(categoryUuid);
		categoryAttribute.setLastUpdatedBy(loginId);
		categoryAttribute.setCreatedBy(loginId);
		categoryAttribute.setCreated(new Date());
		categoryAttribute.setLastUpdated(new Date());
		return categoryAttribute;
	}
	/**
	 * @description:根据品名id查询对应的属性集合
	 * @param categoryUuid
	 * @author :zhoucai
	 * @date 2016-6-23
	 * @return
	 */
	@Override
	public List<BaseCategoryAttributeDto> searchAttributeByCategoryUuid(String categoryUuid) {
		return categoryAttributeDao.searchAttributeByCategoryUuid(categoryUuid);
	}
}
