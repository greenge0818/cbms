package com.prcsteel.platform.smartmatch.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
/**
 *  基价关联设置服务
 * @author caosulin
 *
 */
public interface BasePriceRelationService {
	/**
	 * 根据ID获取基价关联设置
	 * @param id
	 * @return
	 */
	public CustBasePriceRelationDto queryBasePriceRelationById(Long id);

	/**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * 
	 * @return
	 */
	public Map<String, Object> getCommonData();

	/**
	 * 获取所有的城市
	 * @return
	 */
	public List<AreaCityDto> getAllCity();
	/**
	 * 根据品名获取所有材质
	 * @param categoryUuid
	 * @return
	 */
	public List<Materials> queryMaterials(String categoryUuid);
	/**
	 * 根据城市的ID获取城市
	 * @param cityId
	 * @return
	 */
	public City selectCity(Long cityId);
	/**
	 * 根据城市ID获取所有的基价
	 * @param cityId
	 * @author afeng
	 * @return
	 */
	public List<CustBasePriceDto> queryBasePriceByCityId(Long cityId);
	/**
	 * 删除基价关联设置
	 * @param id
	 */
	public void deleteBasePriceRelation(Long id);
	/**
	 * 保存基价
	 * @param dto 保存的DTO
	 * @param user 操作人
	 */
	public void saveBasePriceRelation(CustBasePriceRelationDto dto, User user);

	/**
	 * 判断当前基价和卖家已经关联
	 * @param basePriceId 
	 * @param cityId 
	 * @param accountId 
	 * @return
	 */
	public Boolean checkBasePrice(Long accountId, Long cityId, Long basePriceId);
   /**
	 * 基价关联设置统计页查询
	 * @param query
	 * @return
	 */
	public Integer getBasePriceRelationListTotal(CustBasePriceRelationQuery query);
	
	/**
	 * 基价关联设置分页数据查询
	 * @param query
	 * @return
	 */
	public List<CustBasePriceRelation> getBasePriceRelationList(CustBasePriceRelationQuery query);
}
