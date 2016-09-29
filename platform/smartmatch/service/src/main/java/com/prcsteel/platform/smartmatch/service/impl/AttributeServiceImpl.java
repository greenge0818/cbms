package com.prcsteel.platform.smartmatch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.model.Attribute;
import com.prcsteel.platform.smartmatch.model.query.AttributeEditQuery;
import com.prcsteel.platform.smartmatch.persist.dao.AttributeDao;

import com.prcsteel.platform.smartmatch.service.AttributeService;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;

/**
 * Created by peanut on 2015/11/23.
 */
@Service
public class AttributeServiceImpl implements AttributeService {
	
	@Resource
	private AttributeDao attributeDao;
	
	@Override
	public Integer totalAttribute(AttributeEditQuery attributeEditQuery) {
		return attributeDao.totalAttribute(attributeEditQuery);
	}

	@Override
	public List<Attribute> selectByNameAndType(
			AttributeEditQuery attributeEditQuery) {
		return attributeDao.selectByNameAndType(attributeEditQuery);
	}

	@Override
	public Attribute findById(Long id) {
		if(id == null){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "id参数不能为空");
		}
		return attributeDao.selectByPrimaryKey(id);
	}

	@Override
	public void addAttributeEdit(Attribute attr) throws BusinessException  {
		Integer effect= attributeDao.insertSelective(attr);
		if(effect!=1){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "属性添加失败");
		}
	}

	@Override
	public void deleteByPrimaryKey(Long id) throws BusinessException{
		if(id == null){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "id参数不能为空");
		}
		Integer effect=  attributeDao.deleteByPrimaryKey(id);
		if(effect!=1){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "属性删除失败");
		}
	}

	@Override
	public void updateAttribute(Attribute attr) throws BusinessException {
		Integer effect= attributeDao.updateByPrimaryKeySelective(attr);
		if(effect!=1){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "属性更新失败");
		}
	}

	@Override
	public List<Attribute> getAllAttr() {
		return attributeDao.getAllAttr();
	}

	@Override
	public Attribute findByNameBesidesId(Long id, String name) {
		return attributeDao.findByNameBesidesId(id, name);
	}
	
}
