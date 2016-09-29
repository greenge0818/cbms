package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.*;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.AccountUserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.model.BusiPayRequest;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;

public interface BusiSecondSettlementLogDao {

	/**
	 * 通过当前登录用户查询客户二次结算付款待申请列表
	 * @param paramMap
	 * @return
	 */
	public List<AccountUserDto> queryById(Map<String, Object> paramMap);

	/**
	 * 查询客户二次结算付款待申请列表总数
	 * @param paramMap
	 * @return
	 */
	public Integer queryByIdCount(Map<String, Object> paramMap);

	/**
	 * 通过id查询客户信息
	 * 
	 * @param id
	 * @return
	 */
	public CustAccountDto queryForAccountId(@Param("id") Long id);

	/**
	 * 查询当前客户公司账目划转详情(带分页)
	 * 
	 * @param id
	 * @return
	 */
	public List<CustAccountTransLogDto> queryByPayType(@Param("id") Long id,
			@Param("start") Integer start, @Param("length") Integer length,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	/**
	 * 查询总数
	 * 
	 * @param id
	 * @return
	 */
	public Integer queryByPayTypeCount(@Param("id") Long id,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	/**
	 * 通过当前登录用户查询所在服务中心ID和服务中心名称
	 * 
	 * @param id
	 *            orgId
	 * @return
	 */
	public List<UserOrgDto> qureyByOid(@Param("id") Long id);

	/**
	 * 新增二次结算付款申请的数据
	 * 
	 * @param busiPayRequest
	 * @return
	 */
	public int insertPayRequest(BusiPayRequest busiPayRequest);

	/**
	 * 查询二次结算付款申请付款待审核的数据
	 * @param paramMap
	 * @return
	 */
	public List<PayRequestOrgDto> queryPayRequst(Map<String, Object> paramMap);

	/**
	 * 查询二次结算付款申请付款待审核记录总数
	 */
	public Integer payRequstCount(Map<String, Object> paramMap);

	/**
	 * 二次结算付款申请单查询
	 */

	public PayRequestItemDto queryByBuyerId(
	@Param("requestId") Long requestId, @Param("status") String status, @Param("type") String type);

	/**
	 * 通过id查pay_requst数据
	 */
	public BusiPayRequest queryByRequestId(@Param("id") Long id);

	/**
	 * 审核通过修改pay_request表的状态
	 */
	public int updateById(BusiPayRequest busiPayRequest);

	/**
	 * 审核通过增加代运营订单审核状态
	 */
	public int addOrderAuditTrail(OrderAuditTrail orderAuditTrail);

	/**
	 * 通过客户ID查余额，二次结算余额
	 * 
	 * @param id
	 * @return
	 */
	public PayAccountDto queryBalance(Long id);

	/**
	 * 首页显示二次结算应收应付专用
	 * @param userIds
	 * @return
	 */
	public AccountUserDto queryByUserId(@Param("userIds") List<Long> userIds);

	/**
	 * 通过当前用户查询对应的服务中心
	 * 
	 * @param userId
	 * @return
	 */
	public PayRequestOrgDto queryForOrg(Long userId);

	/**
	 * 通过客户ID查询当前服务中心额度
	 * 
	 * @param accountId
	 * @return
	 */
	public OrgLimitDto queryCreditLimit(Long accountId);

	public BigDecimal queryOrgCreditUsed(Long orgId);

	/**
	 * 查询当前登录者所在服务中心所以人员
	 * @param orgId
	 * @return
	 */
	List<User> queryUserNameByOrgId (@Param("orgId") Long orgId);

}