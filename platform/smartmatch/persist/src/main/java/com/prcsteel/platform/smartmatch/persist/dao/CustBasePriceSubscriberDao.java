package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.smartmatch.model.dto.BasePriceCustSubscribeInfo;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceTraderRelation;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceSubscriberDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceContactRelation;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceSubscriber;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceSubscriberRelation;
import com.prcsteel.platform.smartmatch.model.query.BasePriceSubQuery;
/**
 * 买家订阅DAO
 * @author prcsteel
 *
 */
public interface CustBasePriceSubscriberDao {
	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
    public int deleteByPrimaryKey(Long id);
    /**
     * 保存数据
     * @param record
     * @return
     */
    public int insert(CustBasePriceSubscriber record);
    
    /**
     * 保存数据，可忽略一些NULL的字段
     * @param record
     * @return
     */
    public int insertSelective(CustBasePriceSubscriber record);
    /**
     * 根据ID查询出买家订阅的信息
     * @param id
     * @return
     */
    public CustBasePriceSubscriber selectByPrimaryKey(Long id);
    /**
     * 根据ID更新出买家订阅的信息，可选字段
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(CustBasePriceSubscriber record);
    /**
     * 根据ID更新出买家订阅的信息，全部修改
     * @param record
     * @return
     */
    public int updateByPrimaryKey(CustBasePriceSubscriber record);
    
    /**
     * 根据查询条件查询订阅信息
     * @param basePriceSubQuery
     * @return
     */
	public List<CustBasePriceSubscriberDto> selectBasePriceSubList(
			BasePriceSubQuery basePriceSubQuery);
	/**
     * 根据查询条件查询订阅信息查询总数
     * @param basePriceSubQuery
     * @return
     */
	public int selectBasePriceSubListCount(BasePriceSubQuery basePriceSubQuery);
	/**
	 * 根据ID删除订阅信息
	 * @param ids
	 */
	public void batchDelBasePriceSubByIds(@Param("ids") List<Long> ids);
	/**
	 * 根据ID删除订阅信息关联表
	 * @param ids
	 */
	public void batchDelBasePriceSubRelationByIds(@Param("ids") List<Long> ids);
	/**
	 * 根据id删除订阅的基价联系人关联表
	 * @param ids
	 */
	public void batchDelBasePriceSubContactByIds(@Param("ids") List<Long> ids);
	/**
	 * 根据id删除订阅的基价交易员关联表
	 * @param ids
	 */
	public void batchDelBasePriceSubTraderByIds(@Param("ids") List<Long> ids);
	/**
	 * 根据城市获取基价
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> getBasePriceDataBycityId(@Param("cityName") String cityName);
	/**
	 * 根据买家ID获取所有联系人
	 * @param accountId
	 * @return
	 */
	public List<Map<String, Object>> getContactDataByAccountId(@Param("accountId") Long accountId);

	/**
	 * 获取用户的所在城市
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCityNameByuserOrgId(@Param("id") Long id);
	/**
	 * 新增基价订阅联系人
	 * @param contact
	 */
	public void insertCustBasePriceContactRelation(
			CustBasePriceContactRelation contact);
	/**
	 * 新增基价订阅联系人
	 * @param trader
	 */
	public void insertCustBasePriceTraderRelation(CustBasePriceTraderRelation trader);
	/**
	 * 新增订阅的基价
	 * @param relation
	 */
	public void insertCustBasePriceSubscriberRelation(
			CustBasePriceSubscriberRelation relation);
	/**
	 * 查询原来是否有数据如果有则更新
	 * @param accountId
	 * @return
	 */
	public List<CustBasePriceSubscriber> selectBasePriceSubByAccountId(CustBasePriceSubscriberDto
			custBasePriceSubscriber);
	/**
	 * 根据买家id获取订阅的买家联系人
	 * @param temp
	 * @return
	 */
	public List<CustBasePriceContactRelation> selectBasePriceContactByAccountId(
			CustBasePriceSubscriber temp);

	/**
	 * 根据基价ID查询出对应的订阅的联系人
	 * @param basepriceids
	 * @return
	 */
	public List<Map<String,Object>>selectContactByBasePrice(@Param("basepriceids") List<Long> basepriceids);

	public List<BasePriceCustSubscribeInfo> queryCustSubscribInfo(@Param("userList") List<Long> userList);

}