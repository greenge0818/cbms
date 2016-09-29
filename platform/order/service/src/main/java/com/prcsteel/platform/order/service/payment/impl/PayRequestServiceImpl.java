package com.prcsteel.platform.order.service.payment.impl;

import com.prcsteel.platform.account.model.dto.PaymentCreateDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataReminded;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.query.PayMentQuery;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.PayCodeDto;
import com.prcsteel.platform.order.model.dto.PayMentDto;
import com.prcsteel.platform.order.model.dto.PayRequstDto;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayRequestType;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.Status;
import com.prcsteel.platform.order.model.model.PayCustBankInfo;
import com.prcsteel.platform.order.model.model.PayCustInfo;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.model.query.PayRequestQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.PayRequestDao;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;
import com.prcsteel.platform.order.persist.dao.PaymentOrderDao;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lcw on 2015/7/25.
 */
@Service("payRequestService")
@Transactional
public class PayRequestServiceImpl implements PayRequestService {

    @Autowired
    private PayRequestDao payRequestDao;
    @Autowired
    private PayRequestItemsDao payRequestItemsDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountBankDao accountBankDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private PaymentOrderDao paymentOrderDao;
    @Resource
    private AccountFundService accountFundService;

    @Resource
    ConsignOrderDao consignOrderDao;
    
    @Resource
    PayRequestService payRequestService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return payRequestDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PayRequest record) {
        return payRequestDao.insert(record);
    }

    @Override
    public int insertSelective(PayRequest record) {
        return payRequestDao.insertSelective(record);
    }

    @Override
    public PayRequest selectByPrimaryKey(Long id) {
        return payRequestDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PayRequest record) {
        return payRequestDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PayRequest record) {
        return payRequestDao.updateByPrimaryKey(record);
    }

    /**
     * 生成Code
     *
     * @return
     */
    @Override
    public synchronized String createCode() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(date);
    }
    
    /**
	 * 生成付款申请单收款方的付款申请单编号 新的提单号生成规则：类型：01 + 服务中心代码 + 6位年月日 + 5位流水号
	 * 例：杭州服务中心在2016年03月07日先生成了一个付款申请单 无锡服务中心在2016年03月07日后生成了一个付款申请单
	 * 杭州服务中心在2016年03月07日又生成了第二个付款申请单 则：杭州服务中心付款申请单号：01HZ16030700001
	 * 前台显示时隐藏类型号01，显示为HZ16030700001 无锡服务中心付款申请单号：01WX16030700002
	 * 前台显示时隐藏类型号01，显示为WX16030700002 杭州服务中心付款申请单号：01HZ16030700002
	 * 前台显示时隐藏类型号01，显示为HZ16030700002
	 * @author lixiang
	 */
    @Override
    public synchronized String createdPayCode(String orgCode, Long orgId) {
    	String typeCode = "01";  //一个类型
		Date date = new Date();
		
		//得到最后的一条记录，并解析单号，如果是当天，则在原有的基本上加+1
		//如果不是当天，则重新生成。
		PayCodeDto payCodeDto = payRequestDao.queryLastPayCode(orgId);
		List<Object> lastPayCodeChips = null;
		if (payCodeDto != null) {
			String lastPayCode = payCodeDto.getPayCode();
			lastPayCodeChips = parsePayCode(lastPayCode);
		}
		String currDateStr = Tools.dateToStr(date, "yyMMdd");//6位年月日
		int number = 1;
		if(lastPayCodeChips!=null){
			//如果时间相等，则在原有的流水号上面+1,
			if(currDateStr.equals(lastPayCodeChips.get(2))){
				number = ((int) lastPayCodeChips.get(3))+1;
			}
		}

		String newCode = typeCode+orgCode+currDateStr+String.format("%05d", number);
		return newCode;
    }
    
    /**
	 * 解析收款方付款申请单号并返回一个数组
	 * 提单号格式：类型：07 + 服务中心代码 + 6位年月日 + 5位流水号
	 * @return List<Object> 为四条：
	 * 		index=0 : String	类型
	 * 		index=1 : String	服务中心代码
	 * 		index=2 : String	6位年月日yyMMdd
	 * 		index=3 : Integer	流水号
	 */
	private List<Object> parsePayCode(String lastPayCode) {
		
		//由于服务中心代码长度不一定，所以先截取其它的，但服务中心的代码至少大于等于两位
		if(lastPayCode == null || lastPayCode.length()<15)
			return null;
		
		List<Object> chips =  new ArrayList<Object>();
		
		String type = lastPayCode.substring(0,2);
		lastPayCode = lastPayCode.substring(2);
		
		int number = Integer.parseInt(lastPayCode.substring(lastPayCode.length() - 5));
		lastPayCode = lastPayCode.substring(0, lastPayCode.length()-5);
		
		String dateStr = lastPayCode.substring(lastPayCode.length()-6);
		
		//最后的剩下的就是服务中心code
		String orgCode = lastPayCode.substring(0, lastPayCode.length() - 6);
		
		chips.add(0, type);
		chips.add(1, orgCode);
		chips.add(2, dateStr);
		chips.add(3, number);
		
		return chips;
	}
    
    /**
     * 申请提现
     *
     * @param user   操作用户对象
     * @param bankId 提现银行账号的ID
     * @param money  申请提现的金额
     * @param balance  原始金额
     * @return true：成功，false：失败
     */
    @Override
    public void applyWithdraw(User user, Long departmentId, Long bankId, BigDecimal money, BigDecimal balance) {
        if (user == null || bankId == null || money == null || departmentId<=0 || bankId <= 0 || money.compareTo(BigDecimal.ZERO) == 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取数据失败!");
        }
        Date date = new Date();
        // 查找用户的组织
        Long orgId = user.getOrgId();
        Organization organization = organizationDao.queryById(orgId);
        if (organization == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获服务中心相关信息失败!");
        }
        // 查找要提现的银行账号
        AccountBank accountBank = accountBankDao.selectByPrimaryKey(bankId);
        if (accountBank == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取银行相关信息失败!");
        }
        // 查找要提现的客户
//        Account account = accountDao.selectByPrimaryKey(accountBank.getAccountId());
        //查找要提现的部门信息
        Account department = accountDao.selectByPrimaryKey(departmentId);

        if (department == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取相关部门信息失败!");
        }
        if(department.getBalance().compareTo(balance) != 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "帐户余额已发生变化，请刷新页面后重试！");
        }
        if (department.getBalance().compareTo(money) == -1) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"抵扣失败，账户余额不足，请核对后再操作！");
		}
        String code = createCode();
//        // 客户的余额与二次结算余额的转换，冻结客户的余额中需要提现的金额
//        Boolean freezeResult = orderStatusService.updatePayment(department.getId(), code, null, money.doubleValue(), 0d,
//                user.getId(), user.getName(), date, Type.WITHDRAWALW_FREEZE.getCode());
//        if (!freezeResult) {
//        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "调取账户接口失败!");
//        }

        //调用统一支付接口，冻结客户的余额中需要提现的金额
        accountFundService.updateAccountFund(departmentId, AssociationType.PAYMEN_ORDER, code,
                AccountTransApplyType.BALANCES_LOCK, BigDecimal.ZERO.subtract(money), money,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());

   String companyName=accountDao.selectByPrimaryKey(department.getParentId()).getName();
        // 插入操作日志
        // TODO
        String status = PayStatus.REQUESTED.toString();
        String type = PayRequestType.WITHDRAW.getCode();
        Integer modificationNumber = 0;
        PayRequest payRequest = new PayRequest();
        payRequest.setCode(code);
        payRequest.setRequesterId(user.getId());
        payRequest.setRequesterName(user.getName());
        payRequest.setOrgId(orgId);
        payRequest.setOrgName(organization.getName());
        payRequest.setBuyerId(department.getParentId());
        payRequest.setBuyerName(companyName);
        payRequest.setTotalAmount(money);
        payRequest.setStatus(status);
        payRequest.setType(type);
        payRequest.setCreated(date);
        payRequest.setCreatedBy(user.getName());
        payRequest.setLastUpdated(date);
        payRequest.setLastUpdatedBy(user.getName());
        payRequest.setModificationNumber(modificationNumber);
        payRequest.setDepartmentId(department.getId());
        payRequest.setDepartmentName(department.getName());

        // 插入支出记录
        if (payRequestDao.insertSelective(payRequest) == 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入支出记录失败!");
        }
        PayRequestItems payRequestItems = new PayRequestItems();
        // 增加收款方付款申请单号 lixiang
        String payCode = payRequestService.createdPayCode(organization.getSeqCode(), orgId);
		payRequestItems.setPayCode(payCode);
        payRequestItems.setRequestId(payRequest.getId());
        payRequestItems.setReceiverId(department.getParentId());
        payRequestItems.setReceiverName(companyName);
        payRequestItems.setReceiverBankCode(accountBank.getBankCode());
        payRequestItems.setReceiverBankName(accountBank.getBankName());
        payRequestItems.setReceiverBankCity(accountBank.getBankCityName());
        payRequestItems.setReceiverBranchBankName(accountBank.getBankNameBranch());
        payRequestItems.setReceiverAccountCode(accountBank.getBankAccountCode());
        payRequestItems.setReceiverTel(department.getTel());
        payRequestItems.setReceiverRegAddr(department.getAddr());
        payRequestItems.setPayAmount(money);
        payRequestItems.setCreated(date);
        payRequestItems.setCreatedBy(user.getName());
        payRequestItems.setLastUpdated(date);
        payRequestItems.setLastUpdatedBy(user.getName());
        payRequestItems.setModificationNumber(modificationNumber);
        payRequestItems.setReceiverDepartmentId(department.getId());
        payRequestItems.setReceiverDepartmentName(department.getName());
        // 插入支出详情记录
        if (payRequestItemsDao.insert(payRequestItems) == 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入支出详情记录失败!");
        }
    }

    /**
     * 查询列表
     *
     * @param paramMap 查询参数
     * @return 数据集合
     */
    @Override
    public List<PayRequest> query(Map<String, Object> paramMap) {
        paramProcess(paramMap);
        return payRequestDao.query(paramMap);
    }

    /**
     * 查询列表总数
     *
     * @param paramMap 查询参数
     * @return 数据集合总数
     */
    @Override
    public int queryTotal(Map<String, Object> paramMap) {
        paramProcess(paramMap);
        return payRequestDao.queryTotal(paramMap);
    }

    /**
     * 处理参数
     *
     * @param paramMap 参数集合
     */
    private void paramProcess(Map<String, Object> paramMap) {
        String requesterName = (String) paramMap.get("requesterName");
        if (requesterName != null && StringUtils.isNotEmpty(requesterName)) {
            paramMap.put("requesterName", "%" + requesterName.trim() + "%");
        }
    }

    /**
     * 审核提现
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @param check true：通过，false：不通过
     * @return true：成功，false：失败
     */
    @Override
    public Boolean checkWithdraw(User user, Long id, Boolean check,String declineReason) {
        PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
        if (payRequest == null || !PayStatus.REQUESTED.toString().equals(payRequest.getStatus())) {
            return false;
        }
        Date date = new Date();
        payRequest.setLastUpdatedBy(user.getName());
        payRequest.setLastUpdated(date);
        // 审核通过
        if (check) {
            payRequest.setStatus(PayStatus.APPROVED.toString());
        } else {
            payRequest.setStatus(PayStatus.DECLINED.toString());
            payRequest.setDeclineReason(declineReason);
            // 提现解冻，冻结金额返回到用户余额
//            Boolean freezeResult = orderStatusService.updatePayment(payRequest.getBuyerId(), payRequest.getCode(), null, payRequest.getTotalAmount().doubleValue(), 0d,
//                    user.getId(), user.getName(), date, Type.WITHDRAWALW_THAW.getCode());
            accountFundService.updateAccountFund(payRequest.getDepartmentId(),  AssociationType.PAYMEN_ORDER, payRequest.getCode(),AccountTransApplyType.BALANCES_UNLOCK,
                    payRequest.getTotalAmount(),payRequest.getTotalAmount().negate(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,PayType.BALANCE, user.getId(), user.getName(), new Date());

        }
        // 插入操作日志 TODO

        return payRequestDao.updateByPrimaryKeySelective(payRequest) > 0;
    }

    /**
     * 确认提现
     *
     * @param user 审核人
     * @param id   付款申请记录ID
     * @return true：成功，false：失败
     */
    @Override
    public Boolean confirmWithdraw(User user, Long id, String paymentBank, Date bankAccountTime) {
        PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
        if (payRequest == null
                || payRequest.getPrintTimes() == null
                || payRequest.getPrintTimes() == 0
                || !PayStatus.APPLYPRINTED.toString().equals(payRequest.getStatus())) {
            return false;
        }
        List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(id);
		List<Long> itemsIds = payRequestItemses.stream().map(a -> a.getId()).collect(Collectors.toList());
    	payRequestItemsDao.updatePaymentBank(itemsIds, paymentBank, bankAccountTime);//修改付款申请银行
        Date date = new Date();
        payRequest.setLastUpdatedBy(user.getName());
        payRequest.setLastUpdated(date);
        // 确认提现
        payRequest.setStatus(PayStatus.CONFIRMED.toString());
        Integer uResult = payRequestDao.updateByPrimaryKeySelective(payRequest);
        if (uResult == 0)
            return false;

        // 提现解冻，冻结金额返回到用户余额
//        orderStatusService.updatePayment(payRequest.getBuyerId(), payRequest.getCode(), null, payRequest.getTotalAmount().doubleValue(), 0d,
//                user.getId(), user.getName(), date, Type.WITHDRAWALW_THAW.getCode());
        accountFundService.updateAccountFund(payRequest.getDepartmentId(), AssociationType.PAYMEN_ORDER, payRequest.getCode(),
                AccountTransApplyType.BALANCES_UNLOCK, payRequest.getTotalAmount(), BigDecimal.ZERO.subtract(payRequest.getTotalAmount()),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,PayType.BALANCE, user.getId(), user.getName(), new Date());

        // 提现确认，减少用户的余额
        accountFundService.updateAccountFund(payRequest.getDepartmentId(), AssociationType.PAYMEN_ORDER, payRequest.getCode(),
                AccountTransApplyType.CAPITAL_ACCOUNT_TRANSFER, BigDecimal.ZERO.subtract(payRequest.getTotalAmount()), BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,PayType.BALANCE, user.getId(), user.getName(), new Date());

        // 插入操作日志 TODO

        return true;
    }

    /**
     * 更新打印状态
     *
     * @param user 审核人
     * @param id   付款申请记录ID
     * @return true：成功，false：失败
     */
    @Override
    public void updatePrintStatus(User user, Long id, String ip) {
        PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
        if (payRequest == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"指定的付款申请单不存在！");
        }
        List<String> list = new ArrayList<String>();
		list.add(Status.APPROVED.toString());
		list.add(Status.APPLYPRINTED.toString());
		if (!list.contains(payRequest.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该付款申请可能被打回或者还未审核！");
		}
        Date date = new Date();
        payRequest.setLastUpdatedBy(user.getLoginId());
        payRequest.setLastUpdated(date);
        // 更新状态
        Integer printTimes = payRequest.getPrintTimes() == null ? 0 : payRequest.getPrintTimes();
        payRequest.setPrintTimes(printTimes + 1);
		payRequest.setPrintName(user.getName());// 打印人
        payRequest.setLastPrintIp(ip);//ip地址
        payRequest.setLastPrintDate(new Date());//最后一次打印时间
        if(PayStatus.APPROVED.toString().equals(payRequest.getStatus())){
        	payRequest.setStatus(PayStatus.APPLYPRINTED.toString());//Green added 拆分打印申请单和确认付款两个步骤
		}else{
			//未审核通过可以打印吗，已经确认了可以打印吗？
		}
        // 插入操作日志 TODO
        payRequestDao.updateByPrimaryKeySelective(payRequest);
        List<Long> receiverIdList = paymentOrderDao.queryReceiverIdByRequestId(id);
        for(Long receiverId : receiverIdList){
            accountDao.updateBankDataStatusByPrimaryKey(receiverId, null, null, AccountBankDataReminded.No.getCode(), user.getLoginId());
        }
    }

    @Override
    public PayRequest selectAvailablePaymentByOrderId(Long orderId) {
        return payRequestDao.selectAvailablePaymentByOrderId(orderId);
    }
    
    @Override
    public void selectStatus (Long requestId) {
    	PayRequest payRequest = payRequestDao.selectByPrimaryKey(requestId);
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
    }

	@Override
	public void callBackPayRequest(Long requestId, User user) {
		List<String> list = new ArrayList<String>();
		list.add(Status.APPROVED.toString());
		list.add(Status.APPLYPRINTED.toString());
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(requestId);
		if (!list.contains(payRequest.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该付款申请已操作打回，无需再次操作！");
		}
		int num = payRequest.getModificationNumber() + 1;
		payRequest = new PayRequest();
		payRequest.setId(requestId);
	    payRequest.setPrintTimes(0);//还原打印次数
		payRequest.setPrintName(null);// 清空打印人
        payRequest.setLastPrintIp(null);//清空ip地址
        payRequest.setLastPrintDate(null);//清空最后一次打印时间
        payRequest.setStatus(Status.REQUESTED.toString());
        payRequest.setModificationNumber(num);
        payRequest.setLastUpdatedBy(user.getName());
        payRequest.setLastUpdated(new Date());
		payRequestDao.callBackPayRequest(payRequest);
	}

	@Override
	public List<PayMentDto> queryPeyMentRequest(PayMentQuery payMentQuery) {
		return payRequestDao.queryPeyMentRequest(payMentQuery);
	}

	@Override
	public Integer queryPeyMentRequestCount(PayMentQuery payMentQuery) {
		return payRequestDao.queryPeyMentRequestCount(payMentQuery);
	}

	@Override
	public List<PayMentDto> queryPeyMent(PayMentQuery payMentQuery) {
		return payRequestDao.queryPeyMent(payMentQuery);
	}

	@Override
	public Integer queryPeyMentCount(PayMentQuery payMentQuery) {
		return payRequestDao.queryPeyMentCount(payMentQuery);
	}

	@Override
	public void closedPayment(Long requestId, User user) {
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(requestId);
		if (PayStatus.CLOSED.toString().equals(payRequest.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"该付款申请已关闭，无需再次操作！");
		}
		payRequest = new PayRequest();
		payRequest.setId(requestId);
		payRequest.setStatus(PayStatus.CLOSED.toString());
		payRequest.setLastUpdated(new Date());
		payRequest.setLastUpdatedBy(user.getName());
		payRequestDao.updateByPrimaryKeySelective(payRequest);
	}
	
	@Override
	public List<User> queryUserNamesByOrgId(Long id) {
		return payRequestDao.queryUserNamesByOrgId(id);
	}
	
	@Override
	public List<PayCustInfo> queryCustInfoByCustName(String custName) {
		return payRequestDao.queryCustInfoByCustName(custName);
	}
	
	@Override
	public PayCustBankInfo queryCustBankInfoByBankAccount(Long bankId){
		return payRequestDao.queryCustBankInfoByBankAccount(bankId);
	}
	
	@Override
	public boolean insertPayRequest(PaymentCreateDto paymentCreateDto) throws BusinessException{
		//PayCustBankInfo payCustBankInfo = queryCustBankInfoByBankAccount(paymentCreateDto.getBankId());
		//此处拿到银行id即可，如果拿的行号怕存在同样的行号的银行有多个，那此处会报错，所以更改 lixiang 2016-3-31
		AccountBank accountBank = accountBankDao.selectByPrimaryKey(paymentCreateDto.getBankId());
//		if("buyer".equals(payCustBankInfo.getCustType())){
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
//					"预付款单不能提交买家信息！");
//		}
		PayRequest payRequest = new PayRequest();
		payRequest.setRequesterId(paymentCreateDto.getTraderId());
		payRequest.setRequesterName(paymentCreateDto.getTrader());
		//增加部门id及部门名称  lixiang 2016-3-31
		payRequest.setDepartmentId(paymentCreateDto.getDepartmentId());
		payRequest.setDepartmentName(accountDao.selectByPrimaryKey(paymentCreateDto.getDepartmentId()).getName());
		payRequest.setBuyerName(paymentCreateDto.getName());
		payRequest.setStatus(paymentCreateDto.getStatus());
		payRequest.setType(paymentCreateDto.getPaymentType());
		payRequest.setCreated(new Date());
		payRequest.setCreatedBy(paymentCreateDto.getCreatedBy());
		payRequest.setLastUpdated(new Date());
		payRequest.setLastUpdatedBy(paymentCreateDto.getCreatedBy());
		payRequest.setStatus(PayStatus.REQUESTED.toString());
		payRequest.setOrgId(paymentCreateDto.getOrgId());
		payRequest.setOrgName(paymentCreateDto.getOrgName());
		payRequest.setTotalAmount(paymentCreateDto.getPayAmount());
		payRequest.setBuyerId(accountBank.getAccountId());
		int insertPayRequest = payRequestDao.insertSelective(payRequest);
		
		PayRequestItems payRequestItems = new PayRequestItems();
		payRequestItems.setRequestId(payRequest.getId());
		payRequestItems.setPayCode(paymentCreateDto.getCode());
		payRequestItems.setReceiverName(paymentCreateDto.getName());
		//增加部门id及部门名称  lixiang 2016-3-31
		payRequestItems.setReceiverDepartmentId(paymentCreateDto.getDepartmentId());
		payRequestItems.setReceiverDepartmentName(accountDao.selectByPrimaryKey(paymentCreateDto.getDepartmentId()).getName());
		payRequestItems.setReceiverAccountCode(paymentCreateDto.getBankAccountCode());
		payRequestItems.setReceiverBankName(paymentCreateDto.getBankName());
		payRequestItems.setPayAmount(paymentCreateDto.getPayAmount());
		payRequestItems.setSecondBalanceTakeout(paymentCreateDto.getBalanceSecondSettlement());
		payRequestItems.setCreated(new Date());
		payRequestItems.setCreatedBy(paymentCreateDto.getCreatedBy());
		payRequestItems.setLastUpdated(new Date());
		payRequestItems.setLastUpdatedBy(paymentCreateDto.getCreatedBy());
		payRequestItems.setReceiverId(accountBank.getAccountId());
		payRequestItems.setReceiverBankCode(accountBank.getBankCode());
		payRequestItems.setReceiverBankCity(accountBank.getBankCityName());
		payRequestItems.setReceiverBranchBankName(accountBank.getBankNameBranch());
//		payRequestItems.setReceiverTel(payCustBankInfo.getCustTel());
//		payRequestItems.setReceiverRegAddr(payCustBankInfo.getCustRegAddr());
		int insertPayRequestItems = payRequestItemsDao.insertSelective(payRequestItems);
		
		return (insertPayRequest == 1) && (insertPayRequestItems == 1);
	}

	@Override
	public List<PayRequstDto> queryPayMentAudit(PayRequestQuery payRequestQuery) {
		return payRequestDao.queryPayMentAudit(payRequestQuery);
	}

	@Override
	public Integer queryPayMentAuditCount(PayRequestQuery payRequestQuery) {
		return payRequestDao.queryPayMentAuditCount(payRequestQuery);
	}

	@Override
	public void checkAdvance(User user, Long requestId, Boolean check, String declineReason) {
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(requestId);
		if (payRequest == null) {
	        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"该预付款不存在，请核实！");
	    }
		if (!PayStatus.REQUESTED.toString().equals(payRequest.getStatus())) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"该预付款已操作过审核功能，无需再次操作！");
        }
        Date date = new Date();
        payRequest.setLastUpdatedBy(user.getName());
        payRequest.setLastUpdated(date);
        // 审核通过
        if (check) {
            payRequest.setStatus(PayStatus.APPROVED.toString());
        } else {
            payRequest.setStatus(PayStatus.DECLINED.toString());
            payRequest.setDeclineReason(declineReason);
        }
        if (payRequestDao.updateByPrimaryKeySelective(payRequest) == 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "确认审核失败！");
        } 
	}

	@Override
	public void confirmRequest(User user, Long id, String paymentBank,Date bankAccountTime) {
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
		if (payRequest == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该预付款不存在，请核实！");
		}
		if (PayStatus.CONFIRMEDPAY.toString().equals(payRequest.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该预付款已确认付款，无需再次操作！");
		}
		List<PayRequestItems> payRequestItems = payRequestItemsDao.selectByRequestId(id);
		List<Long> itemsIds = payRequestItems.stream().map(a -> a.getId()).collect(Collectors.toList());
		payRequestItemsDao.updatePaymentBank(itemsIds, paymentBank, bankAccountTime);// 修改付款申请银行
		Date date = new Date();
		payRequest.setLastUpdatedBy(user.getName());
		payRequest.setLastUpdated(date);
		// 确认付款
		payRequest.setStatus(PayStatus.CONFIRMEDPAY.toString());
		Integer uResult = payRequestDao.updateByPrimaryKeySelective(payRequest);
		if (uResult > 0) {
			// 修改客户的二结账户余额
//			orderStatusService.updatePayment(payRequest.getBuyerId(), payRequestItems.get(0).getPayCode(), null,
//					payRequest.getTotalAmount().doubleValue(), 0d, user.getId(), user.getName(), date,
//					Type.ADVANCE_PAYMENT_CONFIRM.getCode());
            accountFundService.updateAccountFund(payRequest.getDepartmentId(), AssociationType.PAYMEN_ORDER, payRequestItems.get(0).getPayCode(),
                    AccountTransApplyType.ADVANCE_PAYMENT, BigDecimal.ZERO, BigDecimal.ZERO, payRequest.getTotalAmount().negate(), BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, PayType.ADVANCE_PAYMENT, user.getId(), user.getName(), new Date());

		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "确认付款失败！");
		}
	}
    /**
     *  查询变更订单已付款金额
     * @param changeOrderId 变更订单Id
     * @return
     */
    @Override
    public PayRequest selectAvailablePaymentByChangeOrderId(Integer changeOrderId){
        return payRequestDao.selectAvailablePaymentByChangeOrderId(changeOrderId);
    }

	@Override
	public List<PayRequest> selectAllPaymentByOrderId(Long orderId) {
		return payRequestDao.selectAllPaymentByOrderId(orderId);
	}
}
