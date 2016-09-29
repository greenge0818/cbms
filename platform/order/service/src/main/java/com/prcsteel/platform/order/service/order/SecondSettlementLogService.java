package com.prcsteel.platform.order.service.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.account.model.dto.AccountUserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.dto.CustAccountTransLogDto;
import com.prcsteel.platform.order.model.dto.PayAccountDto;
import com.prcsteel.platform.order.model.dto.PayRequestItemDto;
import com.prcsteel.platform.order.model.dto.PayRequestOrgDto;
import com.prcsteel.platform.order.model.dto.RequestOrgDto;
import com.prcsteel.platform.order.model.model.BusiPayRequest;

/**
 * 
 * @author lixiang 2015/7/22
 *
 */
public interface SecondSettlementLogService {

	/**
	 * 通过当前登录用户查询客户二次结算付款待申请列表(带分页)
	 * @param paramMap
	 * @return
	 */
	public List<AccountUserDto> findBydId(Map<String, Object> paramMap);

	/**
	 * 查询客户二次结算付款待申请列表总数
	 * @param paramMap
	 * @return
	 */
	public int findByIdOfCount(Map<String, Object> paramMap);

	/**
	 * 通过ID查询客户信息
	 * 
	 * @param id
	 * @return
	 */
	public CustAccountDto queryForAccountId(Long id);

	/**
	 * 查询当前客户二次结算记账明细
	 * 
	 * @param id
	 * @return
	 */
	public List<CustAccountTransLogDto> queryByPayType(Long id, Integer start,
			Integer length, Date dateStart, Date dateEnd);

	/**
	 * 查询总数
	 * 
	 * @param id
	 * @return
	 */
	public int queryByPayTypeCount(Long id, Date dateStart, Date dateEnd);

	/**
	 * 应收抵扣划转记录增加
	 * @param accountId
	 * @param consignOrderCode
	 * @param amount
	 * @param applyerId
	 * @param applyerName
	 * @param currentBalance
	 * @param userId
	 * @param loginId
	 * @param userName
	 * @return
	 */
	public boolean addAccountTransLog(Long accountId, String consignOrderCode,
			BigDecimal amount, Long applyerId, String applyerName,
			BigDecimal currentBalance, Long userId, String loginId,
			String userName);

	/**
	 * 通过当前登录用户查询所在服务中心ID和服务中心名称
	 * 
	 * @param orgId
	 * @return
	 */
	public List<UserOrgDto> findByOrgId(Long orgId);

	/**
	 * 新增二次结算付款申请的数据（待审核）
	 * 
	 * @param accountId
	 *            客户ID
	 * @param accountName
	 *            客户名称
	 * @param loginId
	 *            当前登录用户ID
	 * @param userId
	 *            当前登录ID
	 * @param payAmount
	 *            付款金额
	 * @param userName
	 *            当前登录用户
	 * @param orgId
	 *            服务中心ID
	 * @param orgName
	 *            服务中心
	 * @param bankId
	 *            银行ID
	 * @return
	 */
	public Boolean insertPayRequest(Long userId, Long accountId,
			String accountName, BigDecimal payAmount, String loginId,
			String userName, String code, Long orgId, String orgName,
			Long bankId, Long departmentId, String departmentName);

	/**
	 * 二次结算付款申请查询
	 */
	public List<PayRequestOrgDto> findPayRequest(Map<String, Object> paramMap);

	/**
	 * 二次结算付款申请总记录数
	 * 
	 * @param id
	 * @return
	 */
	public int getPayCounts(Map<String, Object> paramMap);

	/**
	 * 通过客户Id查二次结算付款申请单
	 * 
	 * @param requestId
	 * @return
	 */
	public PayRequestItemDto findByBuyerId(Long requestId, String status, String type);

	/**
	 * 通过ID查pay_requst记录
	 */
	public BusiPayRequest selectById(Long id);

	/**
	 * 通过orgId查服务中心额度，比较申请提现金额是否超过服务中心剩余额度
	 * 
	 * @param orgId
	 *            服务中心ID
	 * @param totalAmount
	 *            申请金额
	 * @return
	 */
	public RequestOrgDto queryByOrgId(Long orgId, BigDecimal totalAmount);

	/**
	 * 通过审核修改pay_request表的状态
	 * 
	 * @param id
	 * @param declineReason
	 *            不通过原因
	 * @return
	 */
	public void updatePayRequest(Long id, String declineReason, User user);

	/**
	 * 确认付款成功修改状态
	 * 
	 * @param id
	 * @return
	 */
	public int updConfirmedPay(Long id, String paymentBank, Date bankAccountTime);

	/**
	 * 审核不通过增加代运营订单审核状态
	 *
	 * @param user
	 * @param PayStatus
	 *            状态类别
	 * @return
	 */
	public int addOrderAuditTrail(User user, String PayStatus);

	/**
	 * 通过客户ID查余额，二次结算余额
	 * 
	 * @param id
	 * @return
	 */
	public PayAccountDto findById(Long id);

	/**
	 * 首页显示二次结算应收应付专用
	 * 
	 * @param userId
	 *            当前登陆者ID
	 * @return
	 */
	public Map<String, Float> findByUserId(List<Long> userId);

	/**
	 * 通过当前用户查询对应的服务中心
	 * 
	 * @param userId
	 * @return
	 */
	public PayRequestOrgDto queryForOrg(Long userId);

	/**
	 * 查询当前服务中心额度
	 *
	 * @param orgId
	 *            服务中心ID
	 * @return
	 */
	public BigDecimal queryCreditLimit(Long orgId);


	public void addSecondPayAmount(Long departmentId, BigDecimal deductionPay,BigDecimal originalAmount, Long bankId, User user,
			Long accountId);
	
	/**
	 * 二结应收金额抵扣
	 * @param id 客户部门id
	 * @param amount 申请抵扣金额
	 * @param user
	 * @param originalAmount 页面上的余额
	 * @author lixiang
	 */
	void deduction (Long id, BigDecimal amount, BigDecimal originalAmount, User user);
	
}
