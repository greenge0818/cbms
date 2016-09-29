package com.prcsteel.platform.smartmatch.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.BasePriceCustSubscribeInfo;
import com.prcsteel.platform.smartmatch.model.dto.BusiQuotaionCommonSearchDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceSubscriberDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBusinessDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceMsg;
import com.prcsteel.platform.smartmatch.model.model.Resource;
import com.prcsteel.platform.smartmatch.model.query.BasePriceSubQuery;
import com.prcsteel.platform.smartmatch.model.query.MsgQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceBusinessQuery;

/**
 * 询报价接口
 * @author prcsteel
 *
 */
public interface QuotationService {

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
	 * 批量删除订阅信息
	 * 
	 * @param ids
	 *            id集
	 * @return
	 */
	public void delBasePriceSub(List<Long> ids);

	/**
	 * 获取公用买家信息
	 * @return
	 */
	public Map<String, Object> getCommonData();

	/**
	 * 根据城市获取基价
	 * @param cityId
	 * @return
	 */
	public List<Map<String, Object>> getBasePriceDataBycityId(String cityName);

	/**
	 * 根据买家ID获取所有联系人
	 * @param accountId
	 * @return
	 */
	public List<Map<String, Object>> getContactDataByAccountId(Long accountId);

	/**
	 * 查询登录用户的所在城市
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getCityNameByuserOrgId(Long userId);

	/**
	 * 保存订阅信息
	 * @param basePriceSubscriberDto
	 */
	public void saveBasePriceSub(
			CustBasePriceSubscriberDto basePriceSubscriberDto);
	
	/**
	 * 查询所有基价
	 * @return
	 */
	List<CustBasePriceDto> selectBasePrice(CustBasePrice custBasePrice);
	
	/**
	 * 新增/修改基价
	 * @param record
	 * @return
	 */
	int saveSelective(CustBasePrice record, User user);
	
	/**
	 * 根据城市id查城市名称
	 * @param id
	 * @return
	 */
	City selectCity(Long id);
	
	/**
	 * 逻辑删除基价数据
	 * @param id
	 * @return
	 */
	int deleteBybasePriceId(Long id);
	
	/**
	 * 根据条件查询资源信息
	 * @param query
	 * @return
	 */
	List<ResourceBaseDto> getResourceListByConditions(ResourceBusinessQuery query);
	
	 /** 发布报价
	 * @param releaseDateList
	 * @return
	 */
	boolean releaseQuote(String releaseDateList, boolean sendMessage, User operator);
	
	 /**
     * 历史短信查询
     * @param msgQuery
     * @return
     */
    List<CustBasePriceMsg> getHistoricalMsg (MsgQuery msgQuery);
    
    /**
     * 历史短信记录数
     * @param msgQuery
     * @return
     */
    int getHistoricalMsgCount (MsgQuery msgQuery);
    
	/**
	 * 单条搜索条件业务找货分页统计
	 * @param query
	 * @return
	 */
	Integer getSingleBusinessResourceListTotal(ResourceBusinessQuery query);

	/**
	 * 保存常用资源
	 * @param busiQuotaionCommonSearchDto
	 */
	public void saveCommonSearch(
			BusiQuotaionCommonSearchDto busiQuotaionCommonSearchDto);
    
	/**
	 * 删除常用资源
	 * @param id
	 */
	public void delCommonSearch(Long id);

	/**
	 * 查询所有的常用资源
	 * 
	 */
	public List<BusiQuotaionCommonSearchDto> getCommonSearchAll(String loginId);
    
	/**
	 * 单条搜索条件业务找货分页列表
	 * @param query
	 * @return
	 */
	List<ResourceBusinessDto> getSingleBusinessResourceList(ResourceBusinessQuery query);
    
    
	/**
	 * 单条搜索条件业务找货分页统计
	 * @param query
	 * @return
	 */
	Integer getMultiBusinessResourceListTotal(ResourceBusinessQuery query);
	    
	/**
	 * 单条搜索条件业务找货分页列表
	 * @param query
	 * @return
	 */
	List<ResourceBusinessDto> getMultiBusinessResourceList(ResourceBusinessQuery query);

	/**
	 * 新增/修改业务找货资源信息
	 * @param record
	 * @return
	 */
	ResourceBaseDto saveResource(String resourceDateList, User user);


	/***
	 * 新增/业务找货询价资源
	 * @param resourceDateList
	 * @param user
	 * @return
	 */
	Resource saveInquiryResource(String resourceDateList, User user);

	/**
	 * 获取所有的基价类别
	 * @return
	 */
	public List<String> getAllBasePriceType(Long cityId);
	
	
	List<BasePriceCustSubscribeInfo> queryCustSubscribInfo(List<Long> userList);
}
