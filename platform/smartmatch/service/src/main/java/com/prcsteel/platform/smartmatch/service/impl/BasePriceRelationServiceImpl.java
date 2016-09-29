package com.prcsteel.platform.smartmatch.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.dto.CategoryDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.model.query.AreaQuery;

import com.prcsteel.platform.core.persist.dao.MaterialsDao;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelationDetail;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
import com.prcsteel.platform.smartmatch.persist.dao.BasePriceRelationDao;
import com.prcsteel.platform.smartmatch.persist.dao.BasePriceRelationDetailDao;
import com.prcsteel.platform.smartmatch.service.AreaService;
import com.prcsteel.platform.smartmatch.service.BasePriceRelationService;
import com.prcsteel.platform.smartmatch.service.QuotationService;
import com.prcsteel.platform.smartmatch.service.ResourceService;

/**
 * 基价关联设置服务实现类
 * @author caosulin
 *
 */
@Service("basePriceRelationService")
public class BasePriceRelationServiceImpl implements BasePriceRelationService{
	//增加日志
	private Logger logger = LoggerFactory.getLogger(BasePriceRelationServiceImpl.class);
	
	@Resource
	private MaterialsDao materialsDao = null;
	@Resource
	private ResourceService resource = null;
	@Resource
	private BasePriceRelationDao basePriceRelationDao = null;
	@Resource
	private BasePriceRelationDetailDao detailDao = null;
	@Resource
	private QuotationService quotationService = null;
	@Resource
	private AreaService areaService;
	@Resource
	private CategoryDao categoryDao;
	
	@Override
	public Map<String, Object> getCommonData() {
		Map<String, Object> resultMap = resource.getCommonData();
		//所有基价
		List<CustBasePriceDto> basePriceList = queryBasePriceByCityId(null);
		resultMap.put("basePriceList", basePriceList);
		//所有品名
		List<CategoryDto> categoryList = categoryDao.getAllCategory();
		resultMap.put("categoryList", categoryList);
		return resultMap;
	}

	/**
	 * 根据ID获取基价关联设置
	 * @param id
	 * @return
	 */
	public CustBasePriceRelationDto queryBasePriceRelationById(Long id){
		CustBasePriceRelation relation = basePriceRelationDao.selectByPrimaryKey(id);
		//获取主表
		CustBasePriceRelationDto dto = new CustBasePriceRelationDto();
		try {
			BeanUtils.copyProperties(dto, relation);
		} catch (Exception e) {
			logger.error("转换主表数据失败",e);
		} 
		//获取明细表
		List<CustBasePriceRelationDetailDto> list = new ArrayList<CustBasePriceRelationDetailDto>();
		List<CustBasePriceRelationDetail> details = detailDao.selectDetailsByRelationId(id);
		if(details != null && !details.isEmpty()){
			for(CustBasePriceRelationDetail detail : details){
				CustBasePriceRelationDetailDto detailDto = new CustBasePriceRelationDetailDto();
				try {
					BeanUtils.copyProperties(detailDto, detail);
				} catch (Exception e) {
					logger.error("转换子表数据失败",e);
				} 
				createSpecByDto(detailDto);
				list.add(detailDto);
			}
		}
		if(!list.isEmpty()){
			dto.setDetails(list);
		}
		
		return dto;
	}
	@Override
	public List<AreaCityDto> getAllCity() {
		//地区集合
		AreaQuery query = new AreaQuery();
		query.setIsEnable("1");
		query.setStart(0);
		query.setLength(100);
		List<AreaCityDto> cityList = areaService.query(query);
		return cityList;
	}
	/**
	 * 根据品名获取所有材质
	 * @param categoryUuid
	 * @return
	 */
	public List<Materials> queryMaterials(String categoryUuid){
		return materialsDao.queryMaterials(categoryUuid);
	}

	@Override
	public City selectCity(Long cityId) {
		return quotationService.selectCity(cityId);
	}

	@Override
	public List<CustBasePriceDto> queryBasePriceByCityId(Long cityId) {
		CustBasePrice queryParams = new CustBasePrice();
		queryParams.setCityId(cityId);
		return quotationService.selectBasePrice(queryParams);
	}
	/**
	 * 删除基价关联设置
	 * @param id
	 */
	@Transactional
	public void deleteBasePriceRelation(Long id) {
		//删除主表
		basePriceRelationDao.deleteByPrimaryKey(id);
		//删除明细表
		detailDao.deleteDetailsByRelationId(id);
	}
	
	@Transactional
	public void saveBasePriceRelation(CustBasePriceRelationDto dto, User user) {
		//设置更新日期和操作人
		dto.setLastUpdated(new Date());
		dto.setLastUpdatedBy(user.getLoginId());
		
		if(dto.getId() == null){//新增
			//判断当前卖家，基价，地区，是否已经存在
			CustBasePriceRelation result = basePriceRelationDao.getBasePriceRelationByParams(dto);
			if(result != null){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家与基价不能重复设置！");
			}
			dto.setCreated(new Date());
			dto.setCreatedBy(user.getLoginId());
			//新增主表
			basePriceRelationDao.insertSelective(dto);
			//保存明细表
			saveBasePriceItems(dto);
		}else{//修改
			//修改主表
			basePriceRelationDao.updateByPrimaryKeySelective(dto);
			//修改明细表，逻辑先删除后增加
			detailDao.deleteDetailsByRelationId(dto.getId());
			saveBasePriceItems(dto);
		}
		
	}
	
	/**
	 * 保存明细表
	 * @param dto
	 * @param record
	 */
	private void saveBasePriceItems(CustBasePriceRelationDto dto) {
		//新增明细表
		List<CustBasePriceRelationDetailDto> details = dto.getDetails();
		if(details != null){
			for(CustBasePriceRelationDetailDto detail : details){
				
				detail.setBasePriceRelationId(dto.getId());
				detail.setCreated(dto.getCreated());
				detail.setCreatedBy(dto.getCreatedBy());
				detail.setLastUpdated(dto.getLastUpdated());
				detail.setLastUpdatedBy(dto.getLastUpdatedBy());
				//获取规格
				getSpecFromDetailDto(detail);
				//保存明细表
				detailDao.insertSelective(detail);
			}
		}
	}
	
	/**
	 * 把spec1，spec2，spec3生成规格spec
	 * @param detial
	 * @return
	 */
	private void createSpecByDto(CustBasePriceRelationDetailDto detial){
		String spec = null;
		if(detial.getSpec1() != null && detial.getSpec2() != null && detial.getSpec3() != null){
			spec = detial.getSpec1() +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR
					+detial.getSpec2() +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR
					+detial.getSpec3();
		}else if(detial.getSpec1() != null && detial.getSpec2() != null && detial.getSpec3() == null){
			spec = detial.getSpec1() +Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR
					+detial.getSpec2();
					
		}else if(detial.getSpec1() != null && detial.getSpec2() == null && detial.getSpec3() == null){
			spec = detial.getSpec1();
		}
		detial.setSpec(spec);
	}
	/**
	 * 从前台的spec获取规格，分割成spec1，spec2，spec3
	 * @param detail
	 * @param item
	 */
	private void getSpecFromDetailDto(CustBasePriceRelationDetailDto detail) {
		//转换规格
		String spec = detail.getSpec();
		if(StringUtils.isBlank(spec)){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "请输入规格!!");
		}else{
			String [] specs = spec.split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
			if(specs.length == 1){
				detail.setSpec1(specs[0]);
			}else if(specs.length == 2){
				detail.setSpec1(specs[0]);
				detail.setSpec2(specs[1]);
			}else if(specs.length == 3){
				detail.setSpec1(specs[0]);
				detail.setSpec2(specs[1]);
				detail.setSpec3(specs[2]);
			}
		}
	}

	@Override
	public Boolean checkBasePrice(Long accountId, Long cityId, Long basePriceId) {
		//判断当前卖家，基价，地区，是否已经存在
		CustBasePriceRelationDto dto = new CustBasePriceRelationDto();
		dto.setCityId(cityId);
		dto.setAccountId(accountId);
		dto.setBasePriceId(basePriceId);
		CustBasePriceRelation result = basePriceRelationDao.getBasePriceRelationByParams(dto);
		if(result != null){
			return true;
		}
		return false;
	}

	@Override
	public Integer getBasePriceRelationListTotal(CustBasePriceRelationQuery query) {
		return basePriceRelationDao.getBasePriceRelationListTotal(query);
	}

	@Override
	public List<CustBasePriceRelation> getBasePriceRelationList(CustBasePriceRelationQuery query) {
		return basePriceRelationDao.getBasePriceRelationList(query);
	}
}
