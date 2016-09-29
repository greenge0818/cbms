package com.prcsteel.platform.kuandao.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.utils.SendMailHelper;
import com.prcsteel.platform.core.service.CommonService;
import com.prcsteel.platform.kuandao.api.RestOrderService;
import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.common.util.BeanUtil;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.MD5SignVerifyUtil;
import com.prcsteel.platform.kuandao.model.dto.AccountContactDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.ConsignOrderDto;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.MatchNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MclsTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.PaymentOrderTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.AccountStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.DepositWarnType;
import com.prcsteel.platform.kuandao.model.enums.ErrorMessage;
import com.prcsteel.platform.kuandao.model.enums.KuandaoChargStatus;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.MCLSImpStatus;
import com.prcsteel.platform.kuandao.model.enums.OperateResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OperateTypeEnum;
import com.prcsteel.platform.kuandao.model.enums.OrderSubmitStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.PaymentOrderStatus;
import com.prcsteel.platform.kuandao.model.enums.ResponseStatus;
import com.prcsteel.platform.kuandao.model.enums.SPDBNotifyResponseStatus;
import com.prcsteel.platform.kuandao.model.enums.SPDBResponseEnum;
import com.prcsteel.platform.kuandao.model.model.KuandaoDepositJournal;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;
import com.prcsteel.platform.kuandao.model.model.PaymentOrderSynchLog;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoAccountDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDepositJounalDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoPaymentOrderDao;
import com.prcsteel.platform.kuandao.service.KuanDaoProxyService;
import com.prcsteel.platform.kuandao.service.KuandaoAccountService;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;
import com.prcsteel.platform.kuandao.service.KuandaoSequenceService;
import com.prcsteel.platform.kuandao.service.MCXDService;
import com.prcsteel.platform.kuandao.service.MclsQueryService;

@Service
public class KuandaoPaymentOrderServiceImpl implements KuandaoPaymentOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(KuandaoPaymentOrderServiceImpl.class);

	public static final String USERNAME = "system";
	
	private static final String ORDERLISTKEY = "OrderList";
	
	private static final String SETTING_SMS_MOBILE = "KuandaoSmsMobile";

	private static final String SETTING_EMAIL_TOADDRESS = "KuandaoEmailtoAddress";
	
	private static final String SETTING_LIMIT_BANK = "KuandaoLimitBank";
	
	private static final String HELLO="您好，汇入流水";
	
	@Value("${kuandao.sign.cbms.secret}")
	private String cbmsSignSecret;
	
	@Value("${kuandao.charset.cbms}")
	private String cbmsCharset;
	
	@Value("${kuandao.charg.wait.minute}")
	private Integer waitMinute;
	
	@Resource
	private KuandaoPaymentOrderDao kuandaoPaymentOrderDao;

	@Resource
	private MCXDService mcxdService;
	//汇入流水查询
	@Resource
	private MclsQueryService mclsQueryService;
	//本地汇入流水操作
	@Resource
	KuandaoDepositJounalDao kuandaoDepositJounalDao;
	//客户操作
	@Resource
	private KuandaoAccountService kuandaoAccountService;
	//支付单号获取
	@Resource
	private KuandaoSequenceService kuandaoSequenceService;
	@Resource
	private RestOrderService restOrderService;
	
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	@Resource
    private CommonService commonService;
	
	@Resource
	private SysSettingService sysSettingService;
	
	@Resource
	private KuanDaoProxyService kuanDaoProxyService;
	
	@Resource
	private KuandaoAccountDao kuandaoAccountDao;
	
	//同步支付单提交信息到日志
	public void insertSynchronizeLog(PaymentOrderSynchLog synchronizeLog){
		Date now = new Date();
		synchronizeLog.setCreated(now);
		synchronizeLog.setLastUpdated(now);
		kuandaoPaymentOrderDao.insertSynchronizeLog(synchronizeLog);
	}
	
	//汇入流水匹配未关联的买家订单
	@Override
	public void processMclsUnmatch(String userName){
		
		String[] transOrderList = getDepositJournalByStatus(MCLSImpStatus.unmatch.getCode());
		//获取汇入流水查询明细信息
		BeanUtil<MclsTransaction> beanUtil=new BeanUtil<>();
		//查询数据库中的未处理汇入流水
		List<KuandaoDepositJournal> KuandaoDepositJournalList =  queryLocalUnMatch();
		//未匹配汇入流水落地
		if(transOrderList != null){
			for (String transaction : transOrderList) {
				MclsTransaction transResult=beanUtil.stringToBean(transaction,MclsTransaction.class);
				
				KuandaoDepositJournal kuandaoDepositJournal = addDepositJournal(transResult);
				if(kuandaoDepositJournal != null){
					KuandaoDepositJournalList.add(kuandaoDepositJournal);
				}
			}
		}		
		KuandaoDepositJournalList.forEach(kuandaoDepositJournal ->{
			
			String impAcqSsn = kuandaoDepositJournal.getImpAcqSsn();
			String payMerName = kuandaoDepositJournal.getPayMerName();
			Integer warnType = kuandaoDepositJournal.getWarnType();
			
			/**根据付款方用户名，查询付款方是否开户，如果没有开户则开户;如果客户不存在或者存在多条，则发送邮件短信通知**/
			KuandaoAccountDto queryAccountDto = new KuandaoAccountDto();
			queryAccountDto.setMemeberName(payMerName);
			List<KuandaoAccountDto> kuandaoAccountDtoList = kuandaoAccountService.queryAccountByCondition(queryAccountDto);
			
			//是否需要通知
			boolean notifyFlag = false;
			//是否需要通知
			boolean generateFlag = false;
			//短信内容
			String smsContent = null;
			//邮件主题
			String emailSubject = null;
			//邮件内容
			String emailContent = null;
			//付款方会员号
			KuandaoAccountDto kuandaoAccountDto = null;
			//查找付款方信息
			if(kuandaoAccountDtoList.size() == 1){
				kuandaoAccountDto = kuandaoAccountDtoList.get(0);
				String status = kuandaoAccountDto.getStatus();
				//客户未开户
				if(kuandaoAccountDto.getId() == null 
						|| AccountStatusEnum.close.getCode().equals(status) 
						|| AccountStatusEnum.fail.getCode().equals(status) 
						|| AccountStatusEnum.init.getCode().equals(status)){
					Long acctId = kuandaoAccountDto.getAcctId();
					//开户
					Integer openResult=kuandaoAccountService.openAccount(acctId, userName);
					if(openResult.compareTo(KuandaoResultEnum.success.getCode()) == 0){
						//开户成功，重新查询，获取会员号
						queryAccountDto.setAcctId(acctId);
						List<KuandaoAccountDto> accountDtoList = kuandaoAccountService.queryAccountByCondition(queryAccountDto);
						kuandaoAccountDto = accountDtoList.get(0);
						generateFlag = true;
					}else if(warnType == null || DepositWarnType.noaccount.getCode().compareTo(warnType) != 0){ //会员开户失败,不能进行后续生成订单操作
						kuandaoDepositJournal.setWarnType(DepositWarnType.noaccount.getCode());
						notifyFlag = true;
						logger.error(String.format("款道用户开户异常，ID号为%s的客户，开户失败", acctId));
						smsContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂开户失败，请及时处理";
						emailSubject = "付款方＂"+ payMerName +"＂开户失败【款道】";
						emailContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂开户失败，请及时处理";
					}
				}else{ //已存在已开户客户，生成支付单
					generateFlag = true;
				}
			}else if(kuandaoAccountDtoList.isEmpty() && (warnType == null || DepositWarnType.nopayer.getCode().compareTo(warnType) != 0)){
				kuandaoDepositJournal.setWarnType(DepositWarnType.nopayer.getCode());
				notifyFlag = true;
				smsContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂不存在，请及时处理";
				emailSubject = "付款方＂"+ payMerName +"＂不存在【款道】";
				emailContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂不存在，请及时处理";
			}else if(kuandaoAccountDtoList.size() > 1 && (warnType == null || DepositWarnType.duplicate.getCode().compareTo(warnType) != 0)){
				kuandaoDepositJournal.setWarnType(DepositWarnType.duplicate.getCode());
				notifyFlag = true;
				smsContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂存在多条客户信息，请及时处理";
				emailSubject = "付款方＂"+ payMerName +"＂存在多条客户信息【款道】";
				emailContent = HELLO + impAcqSsn + "付款方＂"+ payMerName +"＂存在多条客户信息，请及时处理";
			}
			//判断是否受限银行
			if(StringUtils.isNotEmpty(kuandaoDepositJournal.getPayMerBranchId()) 
					&& isLimitBank(kuandaoDepositJournal.getPayMerBranchId())){ //如果受限行不生成支付单
				generateFlag = false;
				if(warnType == null || DepositWarnType.limitbank.getCode().compareTo(warnType) != 0){ //防止受限行短信重发
					kuandaoDepositJournal.setWarnType(DepositWarnType.limitbank.getCode());
					notifyFlag = true;
					smsContent = HELLO + impAcqSsn + "＂"+ payMerName +"＂"
							+ "付款银行受限";
					emailSubject = "＂"+ payMerName +"＂付款银行受限【款道】";
					emailContent = HELLO + impAcqSsn + "＂"+ payMerName +"＂付款银行受限";
				}
			}
					
			if(notifyFlag){ //款道客户信息异常，不需要后续处理
				kuandaoDepositJounalDao.update(kuandaoDepositJournal);
				sendMsg(smsContent);
				sendEmail(emailSubject, emailContent);
			}else if(generateFlag){
				//根据汇入流水，查询支付单表是否有过此汇入流水记录
				KuandaoPaymentOrder kuandaoPaymentOrder = queryPaymentOrder(impAcqSsn);
				//如果支付单表无汇入流水记录，则生成支付单；如果有，不处理
				if(kuandaoPaymentOrder == null ){
					generatePaymentOrder(kuandaoDepositJournal,kuandaoAccountDto);
				}
			}
			
		});
				
	}
	
	@Override
	public boolean isLimitBank(String bankId){
		List<SysSetting> limitBankList = getLimitBank();
		long limitCount = limitBankList.stream().filter(limitBank -> {
			if(bankId.equals(limitBank.getSettingValue())){
				return true;
			}else{
				return false;
			}
		} ).count();
		return limitCount > 0;
	}

	/**
	 * 根据汇入流水号查询本地汇入流水
	 * @param impAcqSsn
	 * @return
	 */
	private KuandaoPaymentOrder queryPaymentOrder(String impAcqSsn){
		KuandaoPaymentOrder kuandaoPaymentOrder = null;
		KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
		kuandaoPaymentOrderDto.setImpAcqSsn(impAcqSsn);
		List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(kuandaoPaymentOrderDto);
		if(kuandaoPaymentOrderList.size() == 1){
			kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
		}else if(kuandaoPaymentOrderList.size() > 1){
			logger.error(String.format("duplicate depositJournal, impAcqSsn:%s",impAcqSsn));
		}
		return kuandaoPaymentOrder;
	}
	
	/**
	 * 开户后续操作---落地汇入流水---落地支付单---创建日志
	 * @param transResult
	 * @param memeberCode
	 * @return
	 */
	private void generatePaymentOrder(KuandaoDepositJournal kuandaoDepositJournal,KuandaoAccountDto kuandaoAccountDto){
		//获取值支付单需要信息
		String memeberCode = kuandaoAccountDto.getMemeberCode();
		PaymentOrderSynchLog paymentOrderSynchLog = new PaymentOrderSynchLog();
		KuandaoPaymentOrder paymentOrder = this.getPaymentOrder(kuandaoDepositJournal,kuandaoAccountDto,paymentOrderSynchLog);
		//创建日志对象，记录日志
		
		if(paymentOrder != null){
			
			
			paymentOrderSynchLog.setConsignOrderCode(paymentOrder.getConsignOrderCode());
			paymentOrderSynchLog.setPaymentOrderCode(paymentOrder.getPaymentOrderCode());
			paymentOrderSynchLog.setOccurType(OperateTypeEnum.automatic.getType());
			paymentOrderSynchLog.setPayMerId(memeberCode);
			paymentOrderSynchLog.setPayMerName(kuandaoDepositJournal.getPayMerName());
			paymentOrderSynchLog.setPayeeMerId(paymentOrder.getPayeeMerId());
			paymentOrderSynchLog.setPayeeMerName(paymentOrder.getPayeeMerName());
			paymentOrderSynchLog.setPayeeMerVirAcctNo(paymentOrder.getPayeeMerVirAcctNo());
			paymentOrderSynchLog.setNsortName(paymentOrder.getNsortName());
			paymentOrderSynchLog.setWeight(paymentOrder.getWeight());
			paymentOrderSynchLog.setTransactionAmount(paymentOrder.getTransactionAmount());
			paymentOrderSynchLog.setCreatedBy(USERNAME);
			paymentOrderSynchLog.setLastUpdatedBy(USERNAME);
			//落地支付单
			int j = this.insertPaymentOrder(paymentOrder);
			if(j == 1){
				//设置支付单id
				paymentOrderSynchLog.setPaymentOrderId(paymentOrder.getId());
				paymentOrderSynchLog.setResult(OperateResultEnum.success.getResult());
				KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
				try {
					BeanUtils.copyProperties(kuandaoPaymentOrderDto, paymentOrder);
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.error("convert paymentOrder to kuandaoPaymentOrderDto failed", e);
				}
				
				//提交支付单到浦发，进行会员预下单
				int submitResult = this.orderSubmit(kuandaoPaymentOrderDto,paymentOrderSynchLog);
				
				//更新支付单
				paymentOrder.setSettDate(kuandaoPaymentOrderDto.getSettDate());
				paymentOrder.setSubmitStatus(kuandaoPaymentOrderDto.getSubmitStatus());
				paymentOrder.setStatus(kuandaoPaymentOrderDto.getStatus());
				paymentOrder.setAcqSsn(kuandaoPaymentOrderDto.getAcqSsn());
				paymentOrder.setSubmitErrorMsg(kuandaoPaymentOrderDto.getSubmitErrorMsg());
				int updateCount = this.update(paymentOrder);
				if(submitResult == KuandaoResultEnum.success.getCode() && updateCount != 1){ //优先记录浦发的错误
					paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
					paymentOrderSynchLog.setErrorMsg("下单成功，支付单更新失败");
				}
			}else{
				//支付单落地失败,记录日志
				logger.error(String.format("保存支付单失败，支付单号为%s", paymentOrder.getPaymentOrderCode()));
				paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
				paymentOrderSynchLog.setErrorMsg("保存支付单失败");
			}
			//记录支付单日志
			this.insertSynchronizeLog(paymentOrderSynchLog);
		}
	}


	/***
	 * 更新支付单信息
	 * @param paymentOrder
	 * @return
	 */
	private int update(KuandaoPaymentOrder paymentOrder){
		paymentOrder.setLastUpdated(new Date());
		return kuandaoPaymentOrderDao.update(paymentOrder);
	}
	
	/**
	 * 会员预下单
	 * @param paymentOrder
	 * @return
	 */
	//提交支付单到网关，并判断成功与否
	public int orderSubmit(KuandaoPaymentOrderDto kuandaoPaymentOrderDto,PaymentOrderSynchLog paymentOrderSynchLog){
		int resultCode;
		SPDBResponseResult result=mcxdService.orderSubmit(kuandaoPaymentOrderDto);
		if(result == null){
			String errMsg = ErrorMessage.spdbTransFailed.getErrMsg();
			
			kuandaoPaymentOrderDto.setSubmitErrorMsg(errMsg);
			kuandaoPaymentOrderDto.setSubmitStatus(OrderSubmitStatusEnum.submitFailed.getCode());
			kuandaoPaymentOrderDto.setStatus(PaymentOrderStatus.unmatch.getCode());
			paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
			paymentOrderSynchLog.setErrorMsg(errMsg);
			
			resultCode = KuandaoResultEnum.systemerror.getCode();
		}else if(result.isSuccess()){
			String plain=result.getPlain();
			BeanUtil<PaymentOrderTransaction> beanUtil=new BeanUtil<>();
			PaymentOrderTransaction transResult=beanUtil.stringToBean(plain,PaymentOrderTransaction.class);
			//订单信息记录到数据库
			kuandaoPaymentOrderDto.setCreated(new Date());
			kuandaoPaymentOrderDto.setStatus(PaymentOrderStatus.unmatch.getCode());
			if(SPDBResponseEnum.success.getCode().equals(transResult.getRespCode())){
				kuandaoPaymentOrderDto.setAcqSsn(transResult.getAcqSsn());
				kuandaoPaymentOrderDto.setSettDate(transResult.getSettDate());
				kuandaoPaymentOrderDto.setSubmitStatus(OrderSubmitStatusEnum.unmatch.getCode());
				resultCode = KuandaoResultEnum.success.getCode();
			}else{
				String errMsg = String.format("订单提交失败，错误码：%s,描述：%s", transResult.getRespCode(),SPDBResponseEnum.getTextByCode(transResult.getRespCode()));
				
				kuandaoPaymentOrderDto.setSubmitErrorMsg(errMsg);
				kuandaoPaymentOrderDto.setSubmitStatus(OrderSubmitStatusEnum.submitFailed.getCode());
				kuandaoPaymentOrderDto.setStatus(PaymentOrderStatus.unmatch.getCode());
				paymentOrderSynchLog.setErrorMsg(errMsg);
				paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
				
				resultCode =  KuandaoResultEnum.systemerror.getCode();
			}
		}else{
			kuandaoPaymentOrderDto.setSubmitErrorMsg(result.getErrMsg());
			kuandaoPaymentOrderDto.setSubmitStatus(OrderSubmitStatusEnum.submitFailed.getCode());
			
			paymentOrderSynchLog.setErrorMsg(result.getErrMsg());
			paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
			kuandaoPaymentOrderDto.setStatus(PaymentOrderStatus.unmatch.getCode());
			resultCode = KuandaoResultEnum.systemerror.getCode();
		}
		return resultCode;
	}
		
	/*
	 * 汇入流水入本地数据库
	 */
	private KuandaoDepositJournal insertDepositJournal(MclsTransaction transResult){
		//汇入流水本地包装
		KuandaoDepositJournal depositJournal=new KuandaoDepositJournal();
		depositJournal.setImpAcqSsn(transResult.getImpAcqSsn());
		depositJournal.setPayeeVirtualAcctNo(transResult.getVirAcctNo());
		depositJournal.setPayeeVirtualAcctName(transResult.getVirAcctName());
		depositJournal.setPayMerName(transResult.getPayMerName());
		depositJournal.setPayMerBranchId(transResult.getPayMerBranchId());
		depositJournal.setPayMerAcctNo(transResult.getPayMerAcctNo());
		depositJournal.setImpDate(transResult.getImpDate());
		depositJournal.setTransactionAmount(new BigDecimal(transResult.getTranAmt()));
		depositJournal.setImpStatus(transResult.getImpStatus());
		depositJournal.setPurpose(transResult.getPurpose());
		depositJournal.setRemitDetails(transResult.getRemitDetails());
		depositJournal.setCreatedBy(USERNAME);
		depositJournal.setLastUpdatedBy(USERNAME);
		depositJournal.setCreated(new Date());
		depositJournal.setLastUpdated(new Date());
		//汇入流水落地本地
		int count = kuandaoDepositJounalDao.insertDepositJournal(depositJournal);
		if(count == 1){
			return depositJournal;
		}else{
			return null;
		}
	}
	//支付单落入本地数据库
	private int insertPaymentOrder(KuandaoPaymentOrder paymentOrder){
		return kuandaoPaymentOrderDao.insertPaymentOrder(paymentOrder);
	}
	

	//根据买家名匹配待关联交易单
	private KuandaoPaymentOrder getPaymentOrder(KuandaoDepositJournal kuandaoDepositJournal,KuandaoAccountDto kuandaoAccountDto,PaymentOrderSynchLog paymentOrderSynchLog){
		
		BigDecimal amount = kuandaoDepositJournal.getTransactionAmount();
		String remitDetails=kuandaoDepositJournal.getRemitDetails();
		
		//是否生成支付单信息
		boolean isGenerate = true;
		Long acctId = kuandaoAccountDto.getAcctId();
		List<ConsignOrderDto> consignOrderList = kuanDaoProxyService.queryFinishConsignOrderByAcctId(acctId);
		
		//根据买家姓名查找busi_consign_order中订单号
		//定义字段用于接收订单、重量、品名
		String code = null;
		String nsort="";
		BigDecimal weight=null;
		Long consignOrderId=null;
		
		//如果客户有交易单，则对比是否有未匹配的交易单
		List<ConsignOrderDto> unMatchConsignOrderList = Lists.newArrayList(); //未匹配的交易单
		if(consignOrderList != null && !consignOrderList.isEmpty()){
			KuandaoPaymentOrderDto dto=new KuandaoPaymentOrderDto();
			List<KuandaoPaymentOrder> paymentOrders = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(dto);
			for(ConsignOrderDto consignOrderDto : consignOrderList){
				boolean isExists = false; //是否存在已匹配的交易单
				for(KuandaoPaymentOrder kuandaoPaymentOrder : paymentOrders){
					//如果支付单中已存在交易单，结束本次循环
					if(kuandaoPaymentOrder.getConsignOrderId() != null && kuandaoPaymentOrder.getConsignOrderId().compareTo(consignOrderDto.getId()) == 0){
						isExists = true;
						break;
					}
				}
				if(!isExists){
					unMatchConsignOrderList.add(consignOrderDto);
				}
					
			}
		}
		if(unMatchConsignOrderList.isEmpty()){
			//如果交易单不存在,查看补二结
			CustAccountDto account = kuanDaoProxyService.queryCustAccountByAcctId(acctId);
			if(account != null){
				//查看买家二结账户
				BigDecimal balSecSetmt=account.getKdBalanceSecondSettlement();
				if(balSecSetmt.compareTo(BigDecimal.ZERO)<0){
					//生成补交二结款的支付单给浦发
					nsort="补交二结款";
					weight=new BigDecimal(1);
				}else{ //非二结款汇入流水
					isGenerate = false;
				}
			}else{ //客户信息不存在
				isGenerate = false;
				paymentOrderSynchLog.setResult(OperateResultEnum.fail.getResult());
				paymentOrderSynchLog.setErrorMsg("付款方信息不存在");
			}
			
		}else{
			//获取多个交易单中和给定金额相近的交易单
			ConsignOrderDto consignOrderDto = this.closerAmount(unMatchConsignOrderList,amount);
			code = consignOrderDto.getCode();
			weight = consignOrderDto.getTotalWeight();
			consignOrderId = consignOrderDto.getId();
			List<String> nsorts=kuandaoPaymentOrderDao.queryNsorts(code);
			for(String s:nsorts){
				nsort = nsort + s + "/";
			}
			if(nsort.length() > 0){
				nsort = nsort.substring(0,nsort.length() - 1);
			}
		}
		
		if(isGenerate){
			//获取支付单号
			if(remitDetails==null){
				//自动支付单生成发送到浦发
				remitDetails=kuandaoSequenceService.generateKuandaoPayOrderCode(OperateTypeEnum.automatic.getType());
			}
			KuandaoPaymentOrder paymentOrder=new KuandaoPaymentOrder();
			paymentOrder.setImpAcqSsn(kuandaoDepositJournal.getImpAcqSsn());
			paymentOrder.setOccurType(OperateTypeEnum.automatic.getType());
			paymentOrder.setTransactionAmount(kuandaoDepositJournal.getTransactionAmount());
			paymentOrder.setPayeeMerId(prcsteelAccount.getMemeberCode());
			paymentOrder.setPayeeMerName(prcsteelAccount.getMemeberName());
			paymentOrder.setPayeeMerVirAcctNo(prcsteelAccount.getVirAcctNo());
			paymentOrder.setPayMerName(kuandaoDepositJournal.getPayMerName());
			paymentOrder.setStatus(PaymentOrderStatus.unmatch.getCode());
			paymentOrder.setSubmitStatus(OrderSubmitStatusEnum.waitSubmit.getCode());
			paymentOrder.setCreated(new Date());
			paymentOrder.setLastUpdated(new Date());
			paymentOrder.setLastUpdatedBy(USERNAME);
			paymentOrder.setCreatedBy(USERNAME);
			paymentOrder.setPayMerId(kuandaoAccountDto.getMemeberCode());
			
			paymentOrder.setWeight(weight);
			paymentOrder.setNsortName(nsort);
			paymentOrder.setPaymentOrderCode(remitDetails);
			paymentOrder.setConsignOrderCode(code);
			paymentOrder.setConsignOrderId(consignOrderId);
			return paymentOrder;
		}else{
			return null;
		}
	}
	//获取交易单金额中，最接近给定交易金额的交易单，如果有两个金额一样接近，取单号id小的
	private ConsignOrderDto closerAmount(List<ConsignOrderDto> consignOrders,BigDecimal amount){
		ConsignOrderDto consignOrderDto = consignOrders.get(0);
		BigDecimal firstAmout = consignOrderDto.getTotalAmount();
		BigDecimal diffNum = firstAmout.subtract(amount).abs();
		for(ConsignOrderDto dto:consignOrders){
			BigDecimal diffNumTemp = dto.getTotalAmount().subtract(amount).abs();
			if(diffNumTemp.compareTo(diffNum)<0){
				diffNum = diffNumTemp;
				consignOrderDto=dto;
			}
		}
		return consignOrderDto;
	}
	
	private void sendMsg(String smsContent){
		//获取短信接收号码
		List<SysSetting> mobileSettings = sysSettingService.queryByTypeAndValue(SETTING_SMS_MOBILE, null);
		List<String> mobileList = Lists.newArrayList(); 
		mobileSettings.forEach(sysSetting -> mobileList.add(sysSetting.getSettingValue()));
		if(!mobileList.isEmpty()){
			String mobile = StringUtils.join(mobileList, ",");
			boolean isSuccess = commonService.sendSMS(mobile, smsContent);
			if(!isSuccess){
				logger.error(String.format("kuandao sms send fail, toMobile: %s, content: %s", mobile,smsContent));
			}
		}else{
			logger.warn("not kuandao sms mobile config");
		}
	}
	
	private void sendEmail(String emailSubject,String emailContent){

		//获取邮件接收地址
		List<SysSetting> emailSettings = sysSettingService.queryByTypeAndValue(SETTING_EMAIL_TOADDRESS, null);
		List<String> emailList = Lists.newArrayList(); 
		emailSettings.forEach(emailSetting -> emailList.add(emailSetting.getSettingValue()));
		if(!emailList.isEmpty()){
			
			String toAddress = StringUtils.join(emailList,";");
			boolean isSuccess = SendMailHelper.send(emailContent,emailSubject,toAddress);
			if(!isSuccess){
				logger.error(String.format("kuandao emial send fail, toAddress: %s, subject: %s", toAddress,emailSubject));
			}else{
				logger.warn("not kuandao emial toAddress config");
			}
		}
	}

	private List<SysSetting> getLimitBank(){
		return sysSettingService.queryByTypeAndValue(SETTING_LIMIT_BANK, null);
	}
	
	@Override
	public void processMclsMatch(String userName) {

		String[] transOrderList = getDepositJournalByStatus(MCLSImpStatus.match.getCode());

		//获取汇入流水查询明细信息
		BeanUtil<MclsTransaction> beanUtil=new BeanUtil<>();
			
		if(transOrderList != null){
			for (String transaction : transOrderList) {
				MclsTransaction transResult = beanUtil.stringToBean(transaction,MclsTransaction.class);
				KuandaoPaymentOrder kuandaoPaymentOrder = updateDepositJournalAndPaymentOrderToMatch(transResult);
				if(kuandaoPaymentOrder != null){
					KuandaoPaymentOrderDto queryPaymentDto = new KuandaoPaymentOrderDto();
					queryPaymentDto.setId(kuandaoPaymentOrder.getId());		
					finishOrder(queryPaymentDto);
				}
			}
		}
	}
	
	/**
	 * 根据从浦发获取的汇入流水更新本地汇入流水和支付单为匹配状态
	 * @param transResult
	 * @return
	 */
	private KuandaoPaymentOrder updateDepositJournalAndPaymentOrderToMatch(MclsTransaction transResult) {
		KuandaoPaymentOrder kuandaoPaymentOrder = null;
		String impAcqSsn = transResult.getImpAcqSsn();
		KuandaoDepositJournal depositJournal = queryDepositJournalByImpAcqSsn(impAcqSsn);
		if(depositJournal != null && MCLSImpStatus.unmatch.getCode().equals(depositJournal.getImpStatus())){ //未匹配才能更新，进行后续操作
			depositJournal.setImpStatus(transResult.getImpStatus());
			kuandaoDepositJounalDao.update(depositJournal);
		}else{
			return kuandaoPaymentOrder;
		}
		kuandaoPaymentOrder = queryPaymentOrder(impAcqSsn);
		if(kuandaoPaymentOrder != null){
			kuandaoPaymentOrder.setSubmitStatus(OrderSubmitStatusEnum.match.getCode());
			kuandaoPaymentOrder.setStatus(PaymentOrderStatus.match.getCode());
			this.update(kuandaoPaymentOrder);
		}
		return kuandaoPaymentOrder;
	}

	/**
	 * 获取交易结果的orderlist
	 * 报文中OrderList以数组形式存放，整个报文不符合规则，特殊处理
	 * @param result
	 * @return
	 */
	private String[] getTransOrderList(SPDBResponseResult result){
		String plain = result.getPlain();
		String[] transOrderList = null;
		int orderListIndex = plain.indexOf(ORDERLISTKEY);
		if(orderListIndex > 0){
			plain = plain.substring(orderListIndex + ORDERLISTKEY.length() + 1);
			transOrderList = plain.split("\\r\\n");
		}
		return transOrderList;
	}
	
	/**
	 * 将查询到的汇入流水保存到数据库
	 * @param transResult
	 */
	private KuandaoDepositJournal addDepositJournal(MclsTransaction transResult) {
		
		KuandaoDepositJournal depositJournal = queryDepositJournalByImpAcqSsn(transResult.getImpAcqSsn());
		//判断不存在存在汇入流水则落地
		if(depositJournal == null){
			depositJournal = this.insertDepositJournal(transResult);
			if(depositJournal == null){  //汇入流水会重复查询，本次插入失败，可以继续后面的操作
				logger.error(String.format("save depositJournal failed, impAcqSsn:%s",transResult.getImpAcqSsn()));
			}
		}
		return depositJournal;
	}

	/**
	 * 根据汇入流水查询结果查询本地数据库的汇入流水记录
	 * @param transResult
	 * @return
	 */
	private KuandaoDepositJournal queryDepositJournalByImpAcqSsn(String impAcqSsn){
		KuandaoDepositJournal depositJournal = null;
		//判断是否已存在汇入流水再落地
		List<KuandaoDepositJournal> kuandaoDepositJounalList = getDepositJournalByImpAcqSsn(impAcqSsn);
		if(kuandaoDepositJounalList.size() == 1){
			depositJournal = kuandaoDepositJounalList.get(0);
		}else if(kuandaoDepositJounalList.size() > 1){
			logger.error(String.format("duplicate depositJournal, impAcqSsn:%s",impAcqSsn));
		}
		return depositJournal;
	}
	
	@Override
	public void processNonPaymentOrderDeposit(){
		List<KuandaoDepositJournal> kuandaoDepositJounalList = kuandaoDepositJounalDao.queryNonPaymentOrderDeposit();
		kuandaoDepositJounalList.forEach( kuandaoDepositJournal -> {
				String impAcqssn = kuandaoDepositJournal.getImpAcqSsn();
				String smsContent = HELLO + impAcqssn + "已过半小时没有匹配成功，请及时处理";
				String emailSubject = "汇入流水"+ impAcqssn +"已过半小时没有匹配成功【款道】";
				String emailContent =  HELLO + impAcqssn + "已过半小时没有匹配成功，请及时处理";
				
				sendMsg(smsContent);
				
				sendEmail(emailSubject, emailContent);
		});
	}

	@Override
	public void processMclsRefund(String userName) {
		String[] transOrderList = getDepositJournalByStatus(MCLSImpStatus.refund.getCode());

		//获取汇入流水查询明细信息
		BeanUtil<MclsTransaction> beanUtil=new BeanUtil<>();
			
		if(transOrderList != null){
			for (String transaction : transOrderList) {
				MclsTransaction transResult = beanUtil.stringToBean(transaction,MclsTransaction.class);
				List<KuandaoDepositJournal> kuandaoDepositJounalList = getDepositJournalByImpAcqSsn(transResult.getImpAcqSsn());
				if(kuandaoDepositJounalList == null || kuandaoDepositJounalList.isEmpty()){
					KuandaoDepositJournal kuandaoDepositJournal = this.insertDepositJournal(transResult);
					if(kuandaoDepositJournal == null){  //汇入流水会重复查询，本次插入失败，可以继续后面的操作
						logger.error(String.format("save depositJournal failed, impAcqSsn:%s",transResult.getImpAcqSsn()));
					}
				}else if(kuandaoDepositJounalList.size() == 1){ //已经存在，并且状态不是已退款则改为已退款
					KuandaoDepositJournal kuandaoDepositJournal = kuandaoDepositJounalList.get(0);
					if(!MCLSImpStatus.refund.getCode().equals(kuandaoDepositJournal.getImpStatus())){
						kuandaoDepositJournal.setImpStatus(MCLSImpStatus.refund.getCode());
						kuandaoDepositJounalDao.update(kuandaoDepositJournal);
					}
					
				}else{
					logger.error(String.format("depositJournal data exception, impAcqSsn:%s",transResult.getImpAcqSsn()));
				}
				
				//通过汇入流水查询支付单，如果支付单存在，也更新为退款
				KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
				kuandaoPaymentOrderDto.setImpAcqSsn(transResult.getImpAcqSsn());
				List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(kuandaoPaymentOrderDto);
				if(kuandaoPaymentOrderList.isEmpty()){
					logger.error(String.format("kuandaoPaymentOrder do not exists,ImpAcqSsn:%s", transResult.getImpAcqSsn()));
				}else if(kuandaoPaymentOrderList.size() == 1){
					KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
					kuandaoPaymentOrder.setStatus(PaymentOrderStatus.refund.getCode());
					kuandaoPaymentOrderDao.update(kuandaoPaymentOrder);
				}else{
					logger.error(String.format("duplicate kuandaoPaymentOrder,ImpAcqSsn:%s", transResult.getImpAcqSsn()));
				}
			}
		}
	}
	
	/**
	 * 根据状态查询汇入流水
	 * @param status
	 * @return
	 */
	private String[] getDepositJournalByStatus(String status){
		String[] transOrderList = null;
		SPDBResponseResult result=kuanDaoProxyService.queryMclsByStatus(status);
		if(result == null){
			logger.error(ErrorMessage.spdbTransFailed.getErrMsg());
		}else if(result.isSuccess()){
			//获取汇入流水查询明细信息
			transOrderList = getTransOrderList(result);
			if(transOrderList == null){
				logger.error("报文格式有误，无法解析。报文内容为：" + result.getPlain());
			}
		}else{
			logger.error(String.format("%s汇入流水查询失败：%s", MCLSImpStatus.getTextByCode(status), result.getErrMsg()));
		}
		return transOrderList;
	}
	
	/**
	 * 根据汇入流水号查询汇入流水
	 * @param transResult
	 * @return
	 */
	private List<KuandaoDepositJournal> getDepositJournalByImpAcqSsn(String impAcqSsn){
		KuandaoDepositJournalDto kuandaoDepositJournalDto = new KuandaoDepositJournalDto();
		kuandaoDepositJournalDto.setImpAcqSsn(impAcqSsn);
		return kuandaoDepositJounalDao.queryDepositByCondition(kuandaoDepositJournalDto);
	}

	@Override
	public Integer finishOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto) {
		List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(kuandaoPaymentOrderDto);
		if(kuandaoPaymentOrderList.size() == 1){
			KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
			return kuanDaoProxyService.finishOrder(kuandaoPaymentOrder);
		}else{
			return KuandaoResultEnum.businesserror.getCode();
		}
	}

	@Override
	public String matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode, String settDate, String tranAmt) {
		
		MatchNotifyTransaction matchNotifyTransaction = kuanDaoProxyService.matchOrderNotify(spdbRequstParam, paymentOrderCode,settDate,tranAmt);
		String status = matchNotifyTransaction.getStatus();
		if(SPDBNotifyResponseStatus.success.getCode().equals(status)){ //匹配通知处理成功则调用到货确认
			KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
			kuandaoPaymentOrderDto.setPaymentOrderCode(matchNotifyTransaction.getTermSsn());
			kuandaoPaymentOrderDto.setSettDate(matchNotifyTransaction.getTransDtTm());
			finishOrder(kuandaoPaymentOrderDto);
		}else if(PaymentOrderStatus.match.getCode().equals(status)){	//已匹配直接返回成功
			status = SPDBNotifyResponseStatus.success.getCode();
		}
		//返回报文
		return mcxdService.generateResponse(matchNotifyTransaction.getTermSsn(), status);
	}

	@Override
	public ChargResultResponse chargResultNotify(ChargResultNotifyDto chargResultNotifyDto) {
		ChargResultResponse chargResultResponse = new ChargResultResponse();
		chargResultResponse.setStatus(ResponseStatus.fail.getCode());
		//必输项为空，直接返回失败
		Integer paymentOrderId = chargResultNotifyDto.getKuandaoPayorderId();
		String status = chargResultNotifyDto.getStatus();
		Long accountId = chargResultNotifyDto.getAccountId();
		Long transDate = chargResultNotifyDto.getTransDate();
		String sign = chargResultNotifyDto.getSign();
		chargResultResponse.setKuandaoPayorderId(paymentOrderId);
		if(paymentOrderId == null || StringUtils.isEmpty(status) || accountId == null || ResponseStatus.fail.getCode().equals(status)){ 
			chargResultResponse.setStatus(ResponseStatus.fail.getCode());
			return chargResultResponse;
		}
		Map<String,String> signSource = new HashMap<>();
		signSource.put("kuandaoPayorderId", paymentOrderId.toString());
		signSource.put("accountId", accountId.toString());
		signSource.put("transDate", transDate.toString());
		signSource.put("status", status);
		//验签不通过直接返回失败
		if(!MD5SignVerifyUtil.verify(signSource, cbmsSignSecret, sign, cbmsCharset)){
			return chargResultResponse;
		}
		KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
		kuandaoPaymentOrderDto.setId(chargResultNotifyDto.getKuandaoPayorderId());
		List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(kuandaoPaymentOrderDto);
		int affactiveCount = 0;
		if(kuandaoPaymentOrderList.size() == 1){
			KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
			//支付完成并且待充值才能将充值状态更新为已充值
			if(kuandaoPaymentOrder.getStatus().equals(PaymentOrderStatus.confirm.getCode()) 
					&& kuandaoPaymentOrder.getChargStatus().compareTo(KuandaoChargStatus.init.getCode()) == 0){
				kuandaoPaymentOrder.setChargStatus(KuandaoChargStatus.finish.getCode());
				kuandaoPaymentOrder.setChargTime(new Date());
				affactiveCount = this.update(kuandaoPaymentOrder);
			}
			List<String> mobileList = Lists.newArrayList() ;
			
			//有交易单，发送给交易单的联系人
			Long consignOrderId = kuandaoPaymentOrder.getConsignOrderId(); 
			if(consignOrderId != null){
				User user = kuandaoPaymentOrderDao.queryContractByOrderId(consignOrderId);
				String tel = user.getTel();
				if(StringUtils.isNotEmpty(tel)){
					mobileList.add(tel);
				}
			}else{//发给客户联系人关联的所有交易员
				List<AccountContactDto>  accountContactDtoList = kuandaoAccountDao.queryContactByAccountId(accountId);
				accountContactDtoList.forEach(accountContactDto -> {
					String tel = accountContactDto.getTel();
					if(!mobileList.contains(tel)){
						mobileList.add(tel);
					}
				});
			}
			String smsContent = "您好，客户：" + kuandaoPaymentOrder.getPayMerName() + "通过款道成功充值"+ kuandaoPaymentOrder.getTransactionAmount() +"元，请及时跟进处理";
			//充值成功发送短信，短信发送失败，发送邮件
			if(affactiveCount == 1) {
				if(!mobileList.isEmpty() && commonService.sendSMS(StringUtils.join(mobileList, ","), smsContent)){
					chargResultResponse.setStatus(ResponseStatus.success.getCode());
				}else{
					String subject = "客户：" + kuandaoPaymentOrder.getPayMerName() + "充值，短信通知联系人失败，请及时跟进处理【款道】";
					String content = "您好，客户：" + kuandaoPaymentOrder.getPayMerName() + "通过款道成功充值"+ kuandaoPaymentOrder.getTransactionAmount() +"元，但短信通知联系人失败，请及时跟进处理";
					sendEmail(subject,content);
				}
			}
			
		}
		return chargResultResponse;
	}
	
	private List<KuandaoDepositJournal> queryLocalUnMatch(){
		KuandaoDepositJournalDto kuandaoDepositJournalDto = new KuandaoDepositJournalDto();
		kuandaoDepositJournalDto.setImpDate(DateUtils.getDashDate());
		return kuandaoDepositJounalDao.queryDepositWithoutOrder(kuandaoDepositJournalDto);
	}

	@Override
	public Integer charg(Integer id) {
		Integer resultCode;
		KuandaoPaymentOrderDto queryPaymentOrderDto = new KuandaoPaymentOrderDto();
		queryPaymentOrderDto.setId(id);
		List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(queryPaymentOrderDto);
		if(kuandaoPaymentOrderList.isEmpty()){
			resultCode = KuandaoResultEnum.nodata.getCode();
		}else if(kuandaoPaymentOrderList.size() == 1){
			KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
			Date now = new Date();
			long pastTime = (now.getTime()) - kuandaoPaymentOrder.getChargTime().getTime();
			//支付单在支付完成，待充值并且充值时间早于等待时间
			if(kuandaoPaymentOrder.getSubmitStatus().compareTo(OrderSubmitStatusEnum.finish.getCode()) == 0
					&& kuandaoPaymentOrder.getChargStatus().compareTo(KuandaoChargStatus.init.getCode()) == 0 
					&& pastTime > waitMinute * 60 * 1000){ //大于等待时间可以再次充值
				Boolean result = kuanDaoProxyService.charg(kuandaoPaymentOrder);
				if(result){
					//发送MQ成功，则更新充值时间
					kuandaoPaymentOrder.setChargTime(now);
					this.update(kuandaoPaymentOrder);
					resultCode = KuandaoResultEnum.success.getCode();
				}else{
					resultCode = KuandaoResultEnum.businesserror.getCode();
				}
			}else{
				resultCode = KuandaoResultEnum.readonly.getCode();
			}
		}else{
			resultCode = KuandaoResultEnum.duplicate.getCode();
		}
		return resultCode;
	}
}
