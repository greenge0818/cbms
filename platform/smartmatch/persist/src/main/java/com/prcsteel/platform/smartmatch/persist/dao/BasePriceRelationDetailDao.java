package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelationDetail;
/**
 * 基价关联设置明细表
 * @author prcsteel
 *
 */
public interface BasePriceRelationDetailDao {
	/**
	 * 根据主表ID获取明细表数据
	 * @param id
	 * @return
	 */
	public List<CustBasePriceRelationDetail> selectDetailsByRelationId(Long id);

	/**
	 * 根据主表ID删除子表的数据
	 * @param id
	 */
	public int deleteDetailsByRelationId(Long relationId);
	
	public int deleteByPrimaryKey(Long id);

	public int insert(CustBasePriceRelationDetail record);

	public int insertSelective(CustBasePriceRelationDetail record);

	public CustBasePriceRelationDetail selectByPrimaryKey(Long id);

	public int updateByPrimaryKeySelective(CustBasePriceRelationDetail record);

	public int updateByPrimaryKey(CustBasePriceRelationDetail record);
}