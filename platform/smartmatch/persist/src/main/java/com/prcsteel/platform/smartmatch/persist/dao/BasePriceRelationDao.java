package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
/**
 * 基价设置表DAO
 * @author prcsteel
 *
 */
public interface BasePriceRelationDao {
	
	public int deleteByPrimaryKey(Long id);
    
	public int insert(CustBasePriceRelation record);

	public int insertSelective(CustBasePriceRelation record);

	public CustBasePriceRelation selectByPrimaryKey(Long id);

	public int updateByPrimaryKeySelective(CustBasePriceRelation record);

	public int updateByPrimaryKey(CustBasePriceRelation record);
	
	/**
	 * 根据基价id，地区id，卖家id，获取当前的基价
	 * @param params
	 * @return
	 */
	public CustBasePriceRelation getBasePriceRelationByParams(
			CustBasePriceRelationDto dto);
	
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