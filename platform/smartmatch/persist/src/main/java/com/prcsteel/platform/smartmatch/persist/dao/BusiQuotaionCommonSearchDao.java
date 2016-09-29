package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.BusiQuotaionCommonSearchDto;
import com.prcsteel.platform.smartmatch.model.model.BusiQuotaionCommonSearch;
/**
 *业务找货常用资源 DAO
 * @author prcsteel
 *
 */
public interface BusiQuotaionCommonSearchDao {
	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Long id);
	/**
	 * 新增常搜资源
	 * @param record
	 * @return
	 */
	public int insert(BusiQuotaionCommonSearch record);
	/**
	 * 新增常搜资源，字段可为空
	 * @param record
	 * @return
	 */
	public int insertSelective(BusiQuotaionCommonSearch record);
	/**
	 * 查询常搜资源根据ID
	 * @param id
	 * @return
	 */
	public BusiQuotaionCommonSearch selectByPrimaryKey(Long id);
	
	/**
	 * 查询资源，所有条件
	 * @return
	 */
	public List<BusiQuotaionCommonSearch> selectByAllCondition(BusiQuotaionCommonSearchDto dto);
	
	/**
	 * 更新常搜资源根据ID，字段可为空
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BusiQuotaionCommonSearch record);
	/**
	 * 更新常搜资源根据ID
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(BusiQuotaionCommonSearch record);
	/**
	 * 查询当前用户所有的常用资源
	 * @param dto
	 * @return
	 */
	public List<BusiQuotaionCommonSearchDto> selectAllDataByUser(BusiQuotaionCommonSearchDto dto);
}
