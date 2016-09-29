package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.account.model.dto.AccountUserDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.dto.CustAccountTransLogDto;
import com.prcsteel.platform.order.model.dto.PayAccountDto;
import com.prcsteel.platform.order.model.dto.PayRequestItemDto;
import com.prcsteel.platform.order.model.dto.PayRequestOrgDto;
import com.prcsteel.platform.order.model.dto.RequestOrgDto;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.Status;
import com.prcsteel.platform.order.model.enums.Type;
import com.prcsteel.platform.order.model.model.BusiPayRequest;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.persist.dao.BusiSecondSettlementLogDao;
import com.prcsteel.platform.order.persist.dao.PayRequestDao;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lixiang 2015/7/28
 */

@Service("secondSettlementLogService")
public class SecondSettlementLogServiceImpl implements SecondSettlementLogService {

	@Resource
	private BusiSecondSettlementLogDao busiSecondSettlementLogDao;

	@Resource
	private AccountTransLogDao accountTransLogDao;

	@Resource
	private AccountBankDao accountBankDao;

	@Resource
	private PayRequestItemsDao payRequestItemsDao;
	@Resource
	private PayRequestDao payRequestDao;
	@Resource
	private OrganizationDao organizationDao;
	@Resource
	private PayRequestService payRequestService;
	@Resource
	private AccountDao accountDao;
	@Resource
	private OrderStatusService orderStatusService;
	@Resource
	AccountFundService accountFundService;

	@Override
	public List<AccountUserDto> findBydId(Map<String, Object> paramMap) {
		List<AccountUserDto> list = busiSecondSettlementLogDao
				.queryById(paramMap);
		return list;
	}

	@Override
	public int findByIdOfCount(Map<String, Object> paramMap) {
		return busiSecondSettlementLogDao.queryByIdCount(paramMap);
	}

	@Override
	public CustAccountDto queryForAccountId(Long id) {
		return busiSecondSettlementLogDao.queryForAccountId(id);
	}

	@Override
	public List<CustAccountTransLogDto> queryByPayType(Long id, Integer start,
			Integer length, Date dateStart, Date dateEnd) {
		List<CustAccountTransLogDto> list = busiSecondSettlementLogDao
				.queryByPayType(id, start, length, dateStart, dateEnd);
		return list;
	}

	@Override
	public int queryByPayTypeCount(Long id, Date dateStart, Date dateEnd) {
		int count = busiSecondSettlementLogDao.queryByPayTypeCount(id,
				dateStart, dateEnd);
		return count;
	}

	@Override
	public boolean addAccountTransLog(Long accountId, String consignOrderCode,
			BigDecimal amount, Long applyerId, String applyerName,
			BigDecimal currentBalance, Long userId, String loginId,
			String userName) {
		boolean folg = false;
		AccountTransLog accountTransLog = new AccountTransLog();
		accountTransLog.setAccountId(accountId);// 客户id
		accountTransLog.setApplyType(ApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode());// 申请类型
		accountTransLog.setAmount(amount);// 发生金额
		accountTransLog.setConsignOrderCode(consignOrderCode);// 关联单号
		accountTransLog.setApplyerId(userId);// 申请人ID
		accountTransLog.setApplyerName(userName);// 申请人姓名
		accountTransLog.setSerialTime(new Date());// 流水时间
		accountTransLog.setCurrentBalance(currentBalance);// 当前余额
		accountTransLog.setPayType(PayType.SETTLEMENT.getCode());// 支付类型
		accountTransLog.setAssociationType(AssociationType.REIMBUSEMENT_SERIAL_NUMBER.getCode());// 关联类型
		accountTransLog.setCreatedBy(loginId);
		accountTransLog.setLastUpdated(new Date());
		accountTransLog.setLastUpdatedBy(userName);
		int num = accountTransLogDao.insertSelective(accountTransLog);
		if (num > 0)
			folg = true;
		return folg;
	}

	@Override
	public List<UserOrgDto> findByOrgId(Long orgId) {
		List<UserOrgDto> list = busiSecondSettlementLogDao.qureyByOid(orgId);
		return list;
	}

	@Override
	public Boolean insertPayRequest(Long userId, Long accountId,
			String accountName, BigDecimal payAmount, String loginId,
			String userName, String code, Long orgId, String orgName,
			Long bankId, Long departmentId, String departmentName) {
		BusiPayRequest payRequest = new BusiPayRequest();
		payRequest.setCode(code);
		payRequest.setRequesterId(userId);
		payRequest.setRequesterName(userName);
		payRequest.setBuyerId(accountId);
		payRequest.setBuyerName(accountName);
		payRequest.setDepartmentId(departmentId);
		payRequest.setDepartmentName(departmentName);
		payRequest.setTotalAmount(payAmount);
		payRequest.setStatus(Status.REQUESTED.getCode());
		payRequest.setType(Type.SECONDARY_SETTLEMENT.getCode());
		payRequest.setOrgId(orgId);
		payRequest.setOrgName(orgName);
		payRequest.setCreated(new Date());
		payRequest.setCreatedBy(loginId);
		payRequest.setLastUpdated(new Date());
		payRequest.setLastUpdatedBy(userName);
		payRequest.setModificationNumber(0);

		Integer addPayResult = busiSecondSettlementLogDao.insertPayRequest(payRequest);
		if (addPayResult == 0)
			return false;

		// 查找要提现的银行账号
		AccountBank accountBank = accountBankDao.selectByPrimaryKey(bankId);
		if (accountBank == null)
			return false;

		Long payId = payRequest.getId();
		PayRequestItems payRequestItems = new PayRequestItems();
		payRequestItems.setRequestId(payId);
		// 增加收款方付款申请单号 lixiang
        Organization organization = organizationDao.queryById(orgId);
        String payCode = payRequestService.createdPayCode(organization.getSeqCode(), orgId);
		payRequestItems.setPayCode(payCode);
		payRequestItems.setReceiverId(accountId);
		payRequestItems.setReceiverName(accountName);
		payRequestItems.setReceiverDepartmentId(departmentId);
		payRequestItems.setReceiverDepartmentName(departmentName);
		payRequestItems.setReceiverBankName(accountBank.getBankName());
		payRequestItems.setReceiverBankCode(accountBank.getBankCode());
		payRequestItems.setReceiverBankCity(accountBank.getBankCityName());
		payRequestItems.setReceiverBranchBankName(accountBank.getBankNameBranch());
		payRequestItems.setReceiverAccountCode(accountBank.getBankAccountCode());
		payRequestItems.setPayAmount(payAmount);
		payRequestItems.setCreated(new Date());
		payRequestItems.setCreatedBy(userName);
		payRequestItems.setLastUpdated(new Date());
		payRequestItems.setLastUpdatedBy(userName);
		payRequestItems.setModificationNumber(0);
		// 插入支出详情记录
		return payRequestItemsDao.insert(payRequestItems) > 0;

	}

	@Override
	public List<PayRequestOrgDto> findPayRequest(Map<String, Object> paramMap) {
		return busiSecondSettlementLogDao.queryPayRequst(paramMap);
	}

	@Override
	public int getPayCounts(Map<String, Object> paramMap) {
		int counts = busiSecondSettlementLogDao.payRequstCount(paramMap);
		return counts;
	}

	@Override
	public PayRequestItemDto findByBuyerId(Long requestId, String status, String type) {
		PayRequestItemDto payRequestItemDto = busiSecondSettlementLogDao
				.queryByBuyerId(requestId, status, type);
		return payRequestItemDto;
	}

	@Override
	public BusiPayRequest selectById(Long id) {
		BusiPayRequest busiPayRequest = busiSecondSettlementLogDao.queryByRequestId(id);
		return busiPayRequest;
	}

	@Override
	public RequestOrgDto queryByOrgId(Long orgId, BigDecimal totalAmount) {
		RequestOrgDto requestOrgDto = new RequestOrgDto();
		Organization Organization = organizationDao.queryById(orgId);
		BigDecimal number = new BigDecimal("1");
		/** 如果服务中心已使用额度为负值,则服务中心可使用额度=负值的绝对值+服务中心可透支额度 */
		if (Organization.getCreditLimitUsed().compareTo(number) < 0) {
			requestOrgDto.setResidueLimit(Organization.getCreditLimit());
			BigDecimal limitAmount = scale(Organization.getCreditLimitUsed())
					.add(requestOrgDto.getResidueLimit());// 服务中心可用提现金额
			requestOrgDto.setLimitAmount(limitAmount); // 用于存放应收减应付为负数时的值
		} else {
			BigDecimal residueLimit = Organization.getCreditLimit().subtract(
					Organization.getCreditLimitUsed());// 服务中心剩余透支额度
			requestOrgDto.setResidueLimit(residueLimit);
		}
		return requestOrgDto;
	}

	/**
	 * BigDecimal取绝对值方法
	 * 
	 * @param limit
	 * @return
	 */
	private BigDecimal scale(BigDecimal limit) {
		BigDecimal bigDecimal = new BigDecimal(limit.toString());
		BigDecimal limits = bigDecimal.abs();
		return limits;
	}

	@Override
	public void updatePayRequest(Long id, String declineReason, User user) {
		String payStatus;
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
		if (!Status.REQUESTED.toString().equals(payRequest.getStatus())) {
			if(Status.APPROVED.toString().equals(payRequest.getStatus())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该二次结算付款申请已审核通过，无需再次操作！");
			}
			if(Status.DECLINED.toString().equals(payRequest.getStatus())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"该二次结算付款申请已审核不通过，无需再次操作！");
			}
		}		
		BusiPayRequest busiPayRequest = busiSecondSettlementLogDao
				.queryByRequestId(id);
		if (declineReason == null) {
			BigDecimal residueLimit = queryCreditLimit(busiPayRequest
					.getOrgId());// 已用额度
			BigDecimal limitAmount = organizationDao.queryById(
					busiPayRequest.getOrgId()).getCreditLimit(); // 设定额度
			if (limitAmount.compareTo(residueLimit) >= 0) {
				busiPayRequest.setStatus(payStatus = Status.APPROVED.getCode());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"已超过服务中心可用额度，不能付款");
			}
		} else {
			busiPayRequest.setStatus(payStatus = Status.DECLINED.getCode());
			busiPayRequest.setDeclineReason(declineReason);
		}
		if (busiSecondSettlementLogDao.updateById(busiPayRequest) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"更新付款申请失败");
		}
		if (addOrderAuditTrail(user, payStatus) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"新增订单状态更新记录失败");
		}
	}

	@Override
	public int updConfirmedPay(Long id, String paymentBank, Date bankAccountTime) {
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
		List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(id);
		List<Long> itemsIds = payRequestItemses.stream().map(a -> a.getId()).collect(Collectors.toList());
    	payRequestItemsDao.updatePaymentBank(itemsIds, paymentBank, bankAccountTime);//修改付款申请银行
		if (!payRequest.getStatus().equals(Status.APPLYPRINTED.toString())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该二次结算付款申请已确认付款，无需再次操作！");
		}
		BusiPayRequest busiPayRequest = new BusiPayRequest();
		busiPayRequest.setId(id);
		busiPayRequest.setStatus(Status.CONFIRMEDPAY.getCode());
		return busiSecondSettlementLogDao.updateById(busiPayRequest);
	}

	@Override
	public int addOrderAuditTrail(User user, String PayStatus) {
		OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
		orderAuditTrail.setOperatorId(user.getId());
		orderAuditTrail.setOperatorName(user.getName());
		orderAuditTrail.setSetToStatus(PayStatus);
		orderAuditTrail.setCreated(new Date());
		orderAuditTrail.setCreatedBy(user.getLoginId());
		orderAuditTrail.setLastUpdated(new Date());
		orderAuditTrail.setLastUpdatedBy(user.getName());
		orderAuditTrail.setModificationNumber(0);
		return busiSecondSettlementLogDao.addOrderAuditTrail(orderAuditTrail);
	}

	@Override
	public PayAccountDto findById(Long id) {
		PayAccountDto payAccountDto = busiSecondSettlementLogDao
				.queryBalance(id);
		return payAccountDto;
	}

	@Override
	public Map<String, Float> findByUserId(List<Long> userId) {
		Map<String, Float> map = new HashMap<String, Float>();
		AccountUserDto accountUserDto = busiSecondSettlementLogDao.queryByUserId(userId);
		if (accountUserDto != null) {
			map.put("minusBalanceSecond", accountUserDto.getMinusBalanceSecond().floatValue());// 应付
			map.put("positiveBalanceSecond", accountUserDto.getPositiveBalanceSecond().floatValue());// 应收
			return map;
		} else {
			return null;
		}
	}

	@Override
	public PayRequestOrgDto queryForOrg(Long userId) {
		PayRequestOrgDto payRequestOrgDto = busiSecondSettlementLogDao
				.queryForOrg(userId);
		return payRequestOrgDto;
	}

	// @Override
	// public OrgLimitDto queryCreditLimit(Long accountId, BigDecimal
	// amount,String type) {
	// OrgLimitDto orgLimitDto =
	// busiSecondSettlementLogDao.queryCreditLimit(accountId);
	// if(type == Type.DEDUCTIONS.getCode() || type ==
	// Type.SECONDWITHDRAWALW_THAW.getCode()){//抵扣情况或者审核不通过，服务中心已用额度加上发生额
	// BigDecimal creditLimitUsed =
	// orgLimitDto.getCreditLimitUsed().add(amount);
	// Organization organization = new Organization();
	// organization.setId(orgLimitDto.getId());
	// organization.setCreditLimitUsed(creditLimitUsed);
	// organizationDao.update(organization);
	// }
	// if(type == Type.SECONDWITHDRAWALW.getCode()){//提现情况
	// BigDecimal creditLimitUsed =
	// orgLimitDto.getCreditLimitUsed().subtract(amount);
	// Organization organization = new Organization();
	// organization.setId(orgLimitDto.getId());
	// organization.setCreditLimitUsed(creditLimitUsed);
	// organizationDao.update(organization);
	// }
	// return orgLimitDto;
	// }

	@Override
	public BigDecimal queryCreditLimit(Long orgId) {
		return busiSecondSettlementLogDao.queryOrgCreditUsed(orgId);
	}

	@Override
	@Transactional
	public void addSecondPayAmount(Long departmentId, BigDecimal deductionPay,BigDecimal originalAmount, Long bankId, User user, Long accountId) {
		Account account = accountDao.selectByPrimaryKey(accountId);
		Account departmentAccount = accountDao.selectByPrimaryKey(departmentId);
		if(departmentAccount.getBalanceSecondSettlement().compareTo(originalAmount) != 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "二结余额已发生变化，请刷新页面后重试。");
		}
		if (departmentAccount.getBalanceSecondSettlement().compareTo(deductionPay) == -1) {
			throw new BusinessException(
					Constant.EXCEPTIONCODE_BUSINESS,"提现失败，二结账户余额不够此次提现，请核对后再操作！");
		}

		//add by dengxiyan 20160419
		Boolean isPayCredit = Boolean.FALSE;//是否还信用额度
		//已使用额度大于0 先进行还款
		if(departmentAccount.getCreditAmountUsed().compareTo(BigDecimal.ZERO) > 0){
			//可提现金额 = 二结余额-已使用信用额度 自动还款已使用额度
			BigDecimal usableDeductionPay = departmentAccount.getBalanceSecondSettlement().subtract(departmentAccount.getCreditAmountUsed());
			if (usableDeductionPay.compareTo(BigDecimal.ZERO) < 0 || deductionPay.compareTo(usableDeductionPay) > 0){
				throw new BusinessException(
						Constant.EXCEPTIONCODE_BUSINESS,"该客户有信用额度欠款，无法提现成功");
			}else{
				//输入的提现金额 <=  可提现金额 产生还款流水（直接扣除二结余额） 走正常提现流程
				isPayCredit	= Boolean.TRUE;
			}
		}

		Organization organization = organizationDao.queryById(user.getOrgId());
		String code = payRequestService.createCode();
		if (!insertPayRequest(user.getId(), accountId, account.getName(),deductionPay.setScale(2, BigDecimal.ROUND_HALF_UP), user.getLoginId(),
				user.getName(), code, organization.getId(), organization.getName(), bankId, departmentId, 
				departmentAccount.getName())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"新增提现申请失败");
		}

		// 还款信用额度 add by dengxiyan 20160419
		if (isPayCredit){ //产生还款流水（直接扣除二结余额）
			accountFundService.updateAccountFund(departmentId, AssociationType.PAYMEN_ORDER, code, AccountTransApplyType.CREDITLIMI_TREPAYMENT, BigDecimal.ZERO, BigDecimal.ZERO
					, departmentAccount.getCreditAmountUsed().negate(), BigDecimal.ZERO, departmentAccount.getCreditAmountUsed().negate(), BigDecimal.ZERO, PayType.BALANCE
					, 0l, Constant.SYSTEMNAME, new Date());
		}

		//冻结二次结算余额
//		if (!orderStatusService.updatePayment(departmentId, code, null,
//				deductionPay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(), null, user.getId(), user.getName(), new Date(),
//				Type.SECONDWITHDRAWALW.getCode())) {
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
//					"冻结二次结算账户余额失败");
//		}
		accountFundService.updateAccountFund(departmentId, AssociationType.PAYMEN_ORDER, code,
				AccountTransApplyType.SECONDARY_SETTLEMENT_LOCK, BigDecimal.ZERO, BigDecimal.ZERO, deductionPay.negate(), deductionPay,
				BigDecimal.ZERO, BigDecimal.ZERO, PayType.FREEZE, user.getId(), user.getName(), new Date());
	}

	@Override
	public void deduction(Long id, BigDecimal amount, BigDecimal originalAmount, User user) {
		Account account = accountDao.selectByPrimaryKey(id);
		if(account.getBalance().compareTo(originalAmount) != 0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "帐户余额已发生变化，请刷新页面后重试。");
		}
		if (account.getBalance().compareTo(amount) == -1) {
			throw new BusinessException(
					Constant.EXCEPTIONCODE_BUSINESS,"抵扣失败，账户余额不足，请核对后再操作！");
		}
		String code = payRequestService.createCode(); // 生成关联单号
//		boolean adds = orderStatusService.updatePayment(id, code, null,amount.doubleValue(), null, user.getId(), user.getName(),
//				new Date(), Type.DEDUCTIONS.getCode());
//		if (!adds) {
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"抵扣余额失败！");
//		}
		accountFundService.updateAccountFund(id, AssociationType.PAYMEN_ORDER, code,
				AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES, amount.negate(), BigDecimal.ZERO, amount, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, PayType.SETTLEMENT, user.getId(), user.getName(), new Date());
	}

}