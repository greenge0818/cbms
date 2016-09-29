package com.prcsteel.platform.kuandao.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.csii.payment.client.core.MerchantSignVerify;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.prcsteel.platform.kuandao.api.RestAccountService;
import com.prcsteel.platform.kuandao.api.RestOrderService;
import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.BeanUtil;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.ConsignOrderDto;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDailyBillDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoGuaranteedPaymentsDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.PaymentAccountDto;
import com.prcsteel.platform.kuandao.model.dto.RestResultDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.MCDBTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MatchNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.ESBResultStatus;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.MCLSImpStatus;
import com.prcsteel.platform.kuandao.model.enums.OrderSubmitStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.PaymentOrderStatus;
import com.prcsteel.platform.kuandao.model.enums.SPDBNotifyResponseStatus;
import com.prcsteel.platform.kuandao.model.enums.SPDBResponseEnum;
import com.prcsteel.platform.kuandao.model.model.KuandaoCustAccount;
import com.prcsteel.platform.kuandao.model.model.KuandaoDepositJournal;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoAccountDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDailyBillDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDepositJounalDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoPaymentOrderDao;
import com.prcsteel.platform.kuandao.service.DailyBillService;
import com.prcsteel.platform.kuandao.service.KuanDaoProxyService;
import com.prcsteel.platform.kuandao.service.MCDBService;
import com.prcsteel.platform.kuandao.service.MclsQueryService;
import com.prcsteel.platform.kuandao.service.RefundService;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;

@Service
public class KuanDaoProxyServiceImpl implements KuanDaoProxyService {

	private static final Logger logger = LoggerFactory.getLogger(KuanDaoProxyServiceImpl.class);

	@Value("${mockService.switch}")
	private boolean mockServiceSwitch;
	
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	@Resource
	private KuandaoAccountDao kuandaoAccountDao;
	
	@Resource
	private KuandaoPaymentOrderDao kuandaoPaymentOrderDao;
	
	@Resource
	private KuandaoDepositJounalDao kuandaoDepositJounalDao;
	
	@Resource
	private KuandaoDailyBillDao kuandaoDailyBillDao;
	
	@Resource
	private RestAccountService restAccountService;
	
	@Resource
	private RestOrderService restOrderService;
	
	@Resource
	private MclsQueryService mclsQueryService;
	
	@Resource
	private DailyBillService dailyBillService;
	
	@Resource
	private KuandaoToPaymentAccountProcess kuandaoToPaymentAccountProcess;
	
	@Resource
	private MCDBService mcdbService;
	
	@Resource
	private RefundService refundService;
	
	@Resource
	private SpdbHttpsPost spdbHttpsPost;
	
	private static final long KUANDAO_USERID =  0L;
	
	private static final String KUANDAO_USERNAME = "款道";
	
	@Override
	public CustAccountDto queryCustAccountByAcctId(Long acctId) {
		
		CustAccountDto account = null;
		String restAcountResult = null;
		try{
			//查询并修改客户信息
			restAcountResult = restAccountService.queryById(acctId);
			if(StringUtils.isNotEmpty(restAcountResult)){
				Gson gson = new Gson();
				RestResultDto restAccountDto = gson.fromJson(restAcountResult, RestResultDto.class);
				if(ESBResultStatus.success.getCode().equals(restAccountDto.getStatus()) && StringUtils.isNotEmpty(restAccountDto.getData())){
					account = gson.fromJson(restAccountDto.getData(), CustAccountDto.class);
				}
			}
		}catch(ResourceAccessException e){
			account = queryLocalAccountById(acctId);
			logger.error("rest api request failed", e);
		}catch(Exception e){
			account = queryLocalAccountById(acctId);
			logger.error("rest api exchange failed", e);
		}
		return account;
	}

	private CustAccountDto queryLocalAccountById(Long acctId){
		CustAccountDto queryCustAccountDto = new CustAccountDto();
		queryCustAccountDto.setId(acctId);
		return kuandaoAccountDao.queryCustAccountByCondition(queryCustAccountDto);
	}
	@Override
	public List<ConsignOrderDto> queryFinishConsignOrderByAcctId(Long acctId) {
		List<ConsignOrderDto> consignOrderList = Lists.newArrayList();
		if(mockServiceSwitch){
			consignOrderList = queryConsignOrderByAcctId(acctId);
		}else{
			String restResult = null;
			try{
				//查询已完成订单信息
				restResult = restOrderService.queryByAccountId(acctId, ConsignOrderStatus.NEWAPPROVED.name().toLowerCase());
			
				if(StringUtils.isNotEmpty(restResult)){
					Gson gson = new Gson();
					RestResultDto restResultDto = gson.fromJson(restResult, RestResultDto.class);
					
					if(ESBResultStatus.success.getCode().equals(restResultDto.getStatus()) && StringUtils.isNotEmpty(restResultDto.getData())){
						JsonParser parser = new JsonParser();
						JsonArray jsonArray = parser.parse(restResultDto.getData()).getAsJsonArray();
						
						for(JsonElement obj : jsonArray ){
							ConsignOrderDto consignOrder = gson.fromJson( obj , ConsignOrderDto.class);
							consignOrderList.add(consignOrder);
						}
					}
				}
			}catch(ResourceAccessException e){
				consignOrderList = queryConsignOrderByAcctId(acctId);
				logger.error("rest api request failed", e);
			}catch(Exception e){
				consignOrderList = queryConsignOrderByAcctId(acctId);
				logger.error("rest api exchange failed", e);
			}
		}
		return consignOrderList;
	}

	/**
	 * 本地查询交易单信息
	 * @param acctId
	 * @return
	 */
	private List<ConsignOrderDto> queryConsignOrderByAcctId(Long acctId){
		ConsignOrderDto consignOrderDto = new ConsignOrderDto();
		consignOrderDto.setAccountId(acctId);
		consignOrderDto.setStatus(ConsignOrderStatus.NEWAPPROVED.getCode());
		return kuandaoPaymentOrderDao.queryFinishConsignOrderByCondition(consignOrderDto);
	}
	@Override
	public SPDBResponseResult queryMclsByStatus(String status) {
		SPDBResponseResult result = null;
		if(mockServiceSwitch){
			KuandaoDepositJournalDto kuandaoDepositJournalDto = new KuandaoDepositJournalDto();
			kuandaoDepositJournalDto.setImpStatus(status);
			kuandaoDepositJournalDto.setPayeeVirtualAcctNo(prcsteelAccount.getVirAcctNo());
			kuandaoDepositJournalDto.setImpDate(DateUtils.getDashDate());
			List<KuandaoDepositJournal> kuandaoDepositJournalList = kuandaoDepositJounalDao.queryTestDataByCondition(kuandaoDepositJournalDto);
			if(!kuandaoDepositJournalList.isEmpty()){
				result = new SPDBResponseResult();
				StringBuilder plainSB = new StringBuilder("TranAbbr=");
				plainSB.append(SPDBTransNameConstant.MCLS);
				plainSB.append("|MercCode=");
				plainSB.append(prcsteelAccount.getMemeberCode());
				plainSB.append("|OrderList=");
				kuandaoDepositJournalList.forEach(kuandaoDepositJournal -> 
						plainSB.append("ImpAcqSsn=").append(kuandaoDepositJournal.getImpAcqSsn())
							.append("|VirAcctNo=").append(kuandaoDepositJournal.getPayeeVirtualAcctNo())
							.append("|VirAcctName=").append(kuandaoDepositJournal.getPayeeVirtualAcctName())
							.append("|ImpDate=").append(kuandaoDepositJournal.getImpDate())
							.append("|TranAmt=").append(kuandaoDepositJournal.getTransactionAmount())
							.append("|ImpStatus=").append(kuandaoDepositJournal.getImpStatus())
							.append("|PayMerName=").append(kuandaoDepositJournal.getPayMerName())
							.append("|PayMerBranchId=").append(kuandaoDepositJournal.getPayMerBranchId())
							.append("|PayMerAcctNo=").append(kuandaoDepositJournal.getPayMerAcctNo())
							.append("|Purpose=").append(kuandaoDepositJournal.getPurpose())
							.append("|RemitDetails=").append(kuandaoDepositJournal.getRemitDetails())
							.append("\r\n"));
				result.setSuccess(true);
				result.setPlain(plainSB.toString());
				result.setTransName(SPDBTransNameConstant.MCLS);
			}
		}else{
			if(MCLSImpStatus.unmatch.getCode().equals(status)){
				result = mclsQueryService.queryMclsUnmatch();
			}else if(MCLSImpStatus.match.getCode().equals(status)){
				result = mclsQueryService.queryMclsMatch();
			}else if(MCLSImpStatus.refund.getCode().equals(status)){
				result = mclsQueryService.queryMclsRefund();
			}
		}
		return result;
	}

	@Override
	public SPDBResponseResult queryDailyBill() {
		SPDBResponseResult result = null;
		if(mockServiceSwitch){
			KuandaoDailyBillDto kuandaoDailyBillDto = new KuandaoDailyBillDto();
			String lastDay = DateUtils.getPlainDate(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -1));
			kuandaoDailyBillDto.setImpDate(lastDay);
			List<KuandaoDailyBillDto> kuandaoDailyBillList = kuandaoDailyBillDao.queryTestDataByCondition(kuandaoDailyBillDto);
			if(!kuandaoDailyBillList.isEmpty()){
				result = new SPDBResponseResult();
				StringBuilder plainSB = new StringBuilder("setFile=");
				kuandaoDailyBillList.forEach(kuandaoDailyBill ->
						plainSB.append(kuandaoDailyBill.getImpAcqSsn())
						.append("|").append(kuandaoDailyBill.getPayeeAcctNo())
							.append("|").append(kuandaoDailyBill.getPayeeVirtualAcctNo())
							.append("|").append(kuandaoDailyBill.getPayeeVirtualAcctName())
							.append("|").append(kuandaoDailyBill.getIsDebit())
							.append("|").append(kuandaoDailyBill.getTellerId())
							.append("|").append(kuandaoDailyBill.getTransactionAmount())
							.append("|").append(kuandaoDailyBill.getBalance())
							.append("|").append(kuandaoDailyBill.getImpDate())
							.append("|").append(kuandaoDailyBill.getVirtualShareDate())
							.append("|").append(kuandaoDailyBill.getTellerSerialNo())
							.append("|").append(kuandaoDailyBill.getDigestCode())
							.append("|").append(kuandaoDailyBill.getPayMerBranchId())
							.append("|").append(kuandaoDailyBill.getPayMerAcctNo())
							.append("|").append(kuandaoDailyBill.getPayMerName())
							.append("|").append(kuandaoDailyBill.getPaymentOrderCode())
							.append("|").append(kuandaoDailyBill.getCorrectionEntriesMark())
							.append("|").append(kuandaoDailyBill.getVoucherNo())
							.append("|").append(kuandaoDailyBill.getPaymentStatus())
							.append("\r\n"));
				result.setSuccess(true);
				result.setPlain(plainSB.toString());
				result.setTransName(SPDBTransNameConstant.MCLS);
			}
		}else{
			result = dailyBillService.queryDailyBill();
		}
		return result;
	}

	@Override
	public Integer finishOrder(KuandaoPaymentOrder kuandaoPaymentOrder) {
		//已匹配的发起到货确认
		if(OrderSubmitStatusEnum.match.getCode() == kuandaoPaymentOrder.getSubmitStatus()){

			KuandaoGuaranteedPaymentsDto kuandaoGuaranteedPaymentsDto = new KuandaoGuaranteedPaymentsDto();
			kuandaoGuaranteedPaymentsDto.setAcqSsn(kuandaoPaymentOrder.getAcqSsn());
			kuandaoGuaranteedPaymentsDto.setGuaranteedPaymentsCode(kuandaoPaymentOrder.getImpAcqSsn());
			kuandaoGuaranteedPaymentsDto.setOacqSsn(kuandaoPaymentOrder.getAcqSsn());
			kuandaoGuaranteedPaymentsDto.setOsttDate(kuandaoPaymentOrder.getSettDate());
			kuandaoGuaranteedPaymentsDto.setPayeeMerId(kuandaoPaymentOrder.getPayeeMerId());
			kuandaoGuaranteedPaymentsDto.setTransactionAmount(kuandaoPaymentOrder.getTransactionAmount().toString());
			SPDBResponseResult result = mcdbService.paymentGuaranteed(kuandaoGuaranteedPaymentsDto);
			
			if(mockServiceSwitch){//模拟接口直接进行账户充值
				return finishOrderIndeed(kuandaoPaymentOrder);
			}
			if(result == null){
				logger.error("request spdb MCDB failed !");
				return KuandaoResultEnum.timeout.getCode();
			}else if(result.isSuccess()){
				BeanUtil<MCDBTransaction> beanUtil = new BeanUtil<>();
				MCDBTransaction mcdbTransaction = beanUtil.stringToBean(result.getPlain(), MCDBTransaction.class);
				String responseCode = mcdbTransaction.getRespCode();
				if(SPDBResponseEnum.success.getCode().equals(responseCode)){
					//浦发交易成功，进行账户充值
					return finishOrderIndeed(kuandaoPaymentOrder);
				}else{
					logger.error(String.format("spdb MCDB response error, error code : %s; error msg : %s", responseCode,SPDBResponseEnum.getTextByCode(responseCode)));
					return KuandaoResultEnum.systemerror.getCode();
				}
			}else{
				logger.error(String.format("spdb MCDB response failed, error code : %s; error msg : %s", result.getErrCode(),result.getErrMsg()));
				return KuandaoResultEnum.systemerror.getCode();
			}
		}else{
			return KuandaoResultEnum.dataoperateerror.getCode();
		}
		
	}

	/***
	 * 完成支付单
	 * <ol>1，进行充值</ol>
	 * <ol>2，更新支付单</ol>
	 * <ol>3，更新汇入流水</ol>
	 * @param kuandaoPaymentOrder
	 * @return
	 */
	private int finishOrderIndeed(KuandaoPaymentOrder kuandaoPaymentOrder) {
		
		charg(kuandaoPaymentOrder);
		//更新支付单提交状态和支付单状态
		kuandaoPaymentOrder.setStatus(PaymentOrderStatus.confirm.getCode());
		kuandaoPaymentOrder.setSubmitStatus(OrderSubmitStatusEnum.finish.getCode());
		int affectiveCount = kuandaoPaymentOrderDao.update(kuandaoPaymentOrder);
		
		//更新汇入流水为已完成
		KuandaoDepositJournal kuandaoDepositJournal = queryDepositByImpAcqSsn(kuandaoPaymentOrder);
		if(kuandaoDepositJournal != null){
			kuandaoDepositJournal.setImpStatus(MCLSImpStatus.finish.getCode());
			kuandaoDepositJounalDao.update(kuandaoDepositJournal);
		}else{
			return KuandaoResultEnum.businesserror.getCode();
		}
		if(affectiveCount == 1){
			return KuandaoResultEnum.success.getCode();
		}else{
			return KuandaoResultEnum.dataoperateerror.getCode();
		}
	}
	
	@Override
	public Boolean charg(KuandaoPaymentOrder kuandaoPaymentOrder) {
		Boolean result = false;
		KuandaoAccountDto kuandaoAccountDto = new KuandaoAccountDto();
		kuandaoAccountDto.setMemeberCode(kuandaoPaymentOrder.getPayMerId());
		List<KuandaoCustAccount> kuandaoCustAccountList =  kuandaoAccountDao.queryKuandaoAccountByCondition(kuandaoAccountDto);
		KuandaoCustAccount kuandaoCustAccount;
		if(kuandaoCustAccountList.size() == 1){
			kuandaoCustAccount = kuandaoCustAccountList.get(0);
			PaymentAccountDto paymentAccountDto = new PaymentAccountDto();
			paymentAccountDto.setAccountId(kuandaoCustAccount.getAcctId());
			paymentAccountDto.setAmount(kuandaoPaymentOrder.getTransactionAmount());
			paymentAccountDto.setKuandaoPayorderId(kuandaoPaymentOrder.getId().toString());
			paymentAccountDto.setOperatorId(KUANDAO_USERID);
			paymentAccountDto.setOperatorName(KUANDAO_USERNAME);
			paymentAccountDto.setTransDate(new Date().getTime());
			result = kuandaoToPaymentAccountProcess.sendPaymentAccount(paymentAccountDto);
		}
		return result;
	}

	@Override
	public MatchNotifyTransaction matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode, String transDate, String tranAmount) {
		
		MatchNotifyTransaction transaction = new MatchNotifyTransaction();
		
		String termSsn = null;
		String settDate = null;  //取交易日期
		BigDecimal tranAmt = BigDecimal.ZERO;
		String status = null;
		
		if(mockServiceSwitch){
			termSsn = paymentOrderCode;
			settDate = transDate;  //取交易日期
			tranAmt = new BigDecimal(tranAmount);
			status = MCLSImpStatus.match.getCode();
		}else{
			String plain  = spdbRequstParam.getPlain();
			String signature = spdbRequstParam.getSignature();
			if(MerchantSignVerify.merchantVerifyPayGate_ABA(signature,plain)){
				BeanUtil<MatchNotifyTransaction> beanUtil = new BeanUtil<>();
				MatchNotifyTransaction matchNotifyTransaction = beanUtil.stringToBean(plain, MatchNotifyTransaction.class);
				termSsn = matchNotifyTransaction.getTermSsn();
				settDate = matchNotifyTransaction.getTransDtTm().substring(0, 8);  //取交易日期
				tranAmt = new BigDecimal(matchNotifyTransaction.getTranAmt());
				status = matchNotifyTransaction.getStatus();
			}
		}
		transaction.setTermSsn(termSsn);
		transaction.setTransDtTm(settDate);
		transaction.setStatus(SPDBNotifyResponseStatus.fail.getCode());
		//根据支付单号、交易日期取支付单信息，如果匹配通知不是支付单提交的当天，则会有问题
		if(StringUtils.isNotEmpty(termSsn) && StringUtils.isNotEmpty(settDate)){
			
			KuandaoPaymentOrderDto queryPaymentOrderDto = new KuandaoPaymentOrderDto();
			queryPaymentOrderDto.setPaymentOrderCode(termSsn);
			queryPaymentOrderDto.setSettDate(settDate);
			List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(queryPaymentOrderDto);
		
			boolean needUpdate = false;
			if(kuandaoPaymentOrderList.size() == 1){
				KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
				
				KuandaoDepositJournal kuandaoDepositJournal = queryDepositByImpAcqSsn(kuandaoPaymentOrder);
				//支付单的提交状态
				Integer submitStatus = kuandaoPaymentOrder.getSubmitStatus();
				BigDecimal transactionAmount = kuandaoPaymentOrder.getTransactionAmount();
				//支付金额和匹配通知的金额不相等，则记录日志
				if(transactionAmount.compareTo(tranAmt) != 0){
					logger.warn("paymentOrder's transactionAmount dismatch spdb notifiction's");
				}
				boolean isMatch = MCLSImpStatus.match.getCode().equals(status);
				
				if(isMatch){
					if(OrderSubmitStatusEnum.unmatch.getCode() == submitStatus ){  //未匹配的订单并且是匹配通知才能修改为已匹配
						kuandaoPaymentOrder.setStatus(PaymentOrderStatus.match.getCode());
						kuandaoPaymentOrder.setSubmitStatus(OrderSubmitStatusEnum.match.getCode());
						needUpdate = true;
					}else if(OrderSubmitStatusEnum.match.getCode() == submitStatus){  //已匹配的订单并且是匹配通知直接返回成功
						transaction.setStatus(PaymentOrderStatus.match.getCode());
						return transaction;
					}
				}
				
				if(needUpdate){
					kuandaoPaymentOrder.setLastUpdated(new Date());
					int affectedCount = kuandaoPaymentOrderDao.update(kuandaoPaymentOrder);
					//更新汇入流水状态
					if(kuandaoDepositJournal != null){
						kuandaoDepositJournal.setImpStatus(MCLSImpStatus.match.getCode());
					}
					kuandaoDepositJounalDao.update(kuandaoDepositJournal);
					if(affectedCount == 1){
						transaction.setStatus(SPDBNotifyResponseStatus.success.getCode());
					}
				}
			}else{
				logger.error(String.format("query PaymentOrder by paymentOrderCode[%s] and settDate[%s] data exception", termSsn,settDate));
			}
		}
		return transaction;
	}

	/**
	 * 根据汇入流水号查询汇入流水
	 * @param kuandaoPaymentOrder
	 * @return
	 */
	private KuandaoDepositJournal queryDepositByImpAcqSsn(KuandaoPaymentOrder kuandaoPaymentOrder) {
		KuandaoDepositJournal kuandaoDepositJournal = null;
		KuandaoDepositJournalDto kuandaoDepositJournalDto = new KuandaoDepositJournalDto();
		String impAcqSsn = kuandaoPaymentOrder.getImpAcqSsn();
		kuandaoDepositJournalDto.setImpAcqSsn(impAcqSsn);
		List<KuandaoDepositJournal> kuandaoDepositJournalList = kuandaoDepositJounalDao.queryDepositByCondition(kuandaoDepositJournalDto); 
		if(kuandaoDepositJournalList.size() == 1){
			kuandaoDepositJournal = kuandaoDepositJournalList.get(0);
		}else{
			logger.error(String.format("query DepositJournal by impAcqSsn[%s] data exception", impAcqSsn));
		}
		return kuandaoDepositJournal;
	}

	@Override
	public SPDBResponseResult refund(KuandaoDepositJournalDto kuandaoDepositJournalDto) {
		
		SPDBResponseResult result = new SPDBResponseResult();
		if(mockServiceSwitch){
			StringBuilder plainSB = new StringBuilder("TranAbbr=");
			plainSB.append(SPDBTransNameConstant.MCTH)
			.append("|MercCode=")
			.append(prcsteelAccount.getMemeberCode())
			.append("|AcqSsn=mock")
			.append(kuandaoDepositJournalDto.getImpAcqSsn())
			.append("|MercDtTm=").append(DateUtils.getPlainDateTime())
			.append("|RespCode=").append(SPDBResponseEnum.success.getCode())
			.append("|TermCode=").append("000000")
			.append("|TranAmt=").append(kuandaoDepositJournalDto.getTransactionAmount())
			.append("|TermSsn=").append(kuandaoDepositJournalDto.getRefundCode())
			.append("|SettDate=").append(DateUtils.getDashDate());
			result.setSuccess(true);
			result.setPlain(plainSB.toString());
			result.setTransName(SPDBTransNameConstant.MCTH);
		}else{
			result = refundService.refundFromBank(kuandaoDepositJournalDto);
		}
		return result;
	}

	@Override
	public SPDBResponseResult boundNotify(SPDBNotifyRequstParam spdbRequstParam, String membercode, String operType) {
		SPDBResponseResult result = new SPDBResponseResult();
		if(mockServiceSwitch){
			StringBuilder plainSB = new StringBuilder("TranAbbr=");
			plainSB.append(SPDBTransNameConstant.SMBD)
			.append("|MercCode=")
			.append(prcsteelAccount.getMemeberCode())
			.append("|SubMerId=")
			.append(membercode)
			.append("|SubMerName=Name").append(membercode)
			.append("|SubMerCifNo=CifNo").append(membercode)
			.append("|OperType=").append(operType)
			.append("|TransDtTm=").append(DateUtils.getPlainDateTime());
			result.setSuccess(true);
			result.setPlain(plainSB.toString());
			result.setTransName(SPDBTransNameConstant.SMBD);
		}else{
			String plain  = spdbRequstParam.getPlain();
			String signature = spdbRequstParam.getSignature();
			if(MerchantSignVerify.merchantVerifyPayGate_ABA(signature,plain)){
				result.setSuccess(true);
				result.setPlain(plain);
				result.setTransName(spdbRequstParam.getTransName());
			}else{
				result.setSuccess(false);
				result.setErrCode("-1");
				result.setErrMsg("浦发返回报文验签失败");
			}
		}
		return result;
	}

	
}
