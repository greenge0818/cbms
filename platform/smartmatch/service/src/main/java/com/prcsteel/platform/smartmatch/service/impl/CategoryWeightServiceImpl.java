package com.prcsteel.platform.smartmatch.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.service.NormsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.CategoryWeightDto;
import com.prcsteel.platform.smartmatch.model.model.CategoryWeight;
import com.prcsteel.platform.smartmatch.model.query.CatagoryWeightQuery;
import com.prcsteel.platform.smartmatch.model.query.SingleWeightQuery;
import com.prcsteel.platform.smartmatch.persist.dao.CategoryWeightDao;
import com.prcsteel.platform.smartmatch.service.CategoryWeightService;

@Service("categoryWeightService")
public class CategoryWeightServiceImpl implements CategoryWeightService {

	@Resource
	private CategoryWeightDao categoryWeightDao;

	@Resource
	private NormsService normsService;
	@Override
	public List<Map<String, Object>> selectByNameAndBusinessAndMaterialAndNorms(CatagoryWeightQuery catagoryWeightQuery) {
		String factory = catagoryWeightQuery.getFactory();
		if (StringUtils.isNotEmpty(factory)) {
			catagoryWeightQuery.setFactory("%" + factory.replaceAll(" ", "") + "%");
		}

		String category = catagoryWeightQuery.getCategory();
		if (StringUtils.isNotEmpty(category)) {
			catagoryWeightQuery.setCategory("%" + category.replaceAll(" ", "") + "%");
		}

		String material = catagoryWeightQuery.getMaterial();
		if (StringUtils.isNotEmpty(material)) {
			catagoryWeightQuery.setMaterial("%" + material.replaceAll(" ", "") + "%");
		}

		String norms = catagoryWeightQuery.getNorms();
		if (StringUtils.isNotEmpty(norms)) {
			catagoryWeightQuery.setNorms("%" + norms.replaceAll(" ", "") + "%");
		}

		return categoryWeightDao.selectByNameAndBusinessAndMaterialAndNorms(catagoryWeightQuery);
	}

	@Override
	public int totalCategoryWeight(CatagoryWeightQuery catagoryWeightQuery) {
		return categoryWeightDao.totalCategoryWeight(catagoryWeightQuery);
	}

	@Override
	public CategoryWeightDto selectByPrimaryKey(Long id) {
		return modelToDTO(categoryWeightDao.selectByPrimaryKey(id));
	}

	private CategoryWeightDto modelToDTO(CategoryWeight model) {
		if (model == null)
			return null;
		CategoryWeightDto dto = new CategoryWeightDto();
		dto.setCategoryWeight(model);
		dto.setCategoryName(categoryWeightDao.selectCategoryName(model.getId()));
		dto.setFactoryName(categoryWeightDao.selectFactoryName(model.getId()));
		dto.setMaterialName(categoryWeightDao.selectMaterialName(model.getId()));

		return dto;
	}

	@Override
	public List<Map<String, Object>> selectById(Long id) {
		return categoryWeightDao.selectById(id);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		if (null != categoryWeightDao.selectByPrimaryKey(id)) {
			if (categoryWeightDao.deleteByPrimaryKey(id) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除物资单件重量信息失败");
			}
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到该物资单件重量信息");
		}
	}

	@Override
	public void addCategoryWeight(CategoryWeight categoryWeight,User user) {
		String normsUuidCombin = normsService.getUuidCombineByCategoryUuid(categoryWeight.getCategoryUuid());
		categoryWeight.setCreatedBy(user.getLoginId());
		categoryWeight.setLastUpdatedBy(user.getLoginId());
		String regex = "^[0-9*.]+$";
		if(!categoryWeight.getNormsName().matches(regex)){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格输入不正确，必须为数字，如1.2*2*3");
		}else if(categoryWeight.getNormsName().split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length != normsUuidCombin.split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格填写不正确(规格长度应为"+normsUuidCombin.split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length+"), 如：规格1*2*3 ，长度为3");
		}else if(categoryWeightDao.selectByParameter(categoryWeight) > 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该钢厂已经存在相同品名，规格的单件重量！");
		}else{
			categoryWeight.setNormsUuid(normsUuidCombin);
			categoryWeight.setCreated(new Date());
			if (categoryWeightDao.insertSelective(categoryWeight) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入钢厂信息数据失败");
			}
		}
	}

	@Override
	public void updateCategoryWeight(CategoryWeight categoryWeight,User user) {		
		String normsUuidCombin = normsService.getUuidCombineByCategoryUuid(categoryWeight.getCategoryUuid());
		categoryWeight.setLastUpdatedBy(user.getLoginId());
		String regex = "^[0-9*.]+$";
		if(!categoryWeight.getNormsName().matches(regex)){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格输入不正确，必须为数字，如1.2*2*3");
		}else if(categoryWeight.getNormsName().split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length != normsUuidCombin.split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格填写不正确(规格长度应为"+normsUuidCombin.split("\\" +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length+"), 如：规格1*2*3 ，长度为3");
		}else if(categoryWeightDao.selectByParameter(categoryWeight) > 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该钢厂已经存在相同品名,规格的单件重量！");
		}else{
			if(categoryWeightDao.updateByPrimaryKeySelective(categoryWeight) != 1){
	            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新钢厂数据失败");
	        }
		}
		
	}

	@Override
	public List<Map<String, Object>> getAllmaterial() {
		return categoryWeightDao.queryAllMaterials();
	}


	@Override
	public BigDecimal selectSingleWeightByParamIds(SingleWeightQuery query) {
		return categoryWeightDao.selectSingleWeightByParamIds(query);
	}
}
