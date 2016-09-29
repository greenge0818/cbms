package com.prcsteel.platform.kuandao.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.PaymentOrderSynchLogDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OperateTypeEnum;
import com.prcsteel.platform.kuandao.model.enums.OrderSubmitStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.PaymentOrderStatus;
import com.prcsteel.platform.kuandao.model.model.KuandaoDepositJournal;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;
import com.prcsteel.platform.kuandao.model.model.PaymentOrderSynchLog;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDepositJounalDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoPaymentOrderDao;
import com.prcsteel.platform.kuandao.service.KuandaoAccountService;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderService;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderSupportService;
import com.prcsteel.platform.kuandao.service.KuandaoSequenceService;

@Service
public class KuandaoPaymentOrderSupportServiceImpl implements KuandaoPaymentOrderSupportService {

	private static final Logger logger = LoggerFactory.getLogger(KuandaoPaymentOrderSupportServiceImpl.class);

	private static final String CONSIGNORDERCODE="consignOrderCode";
 
	private static final String PAYMERNAME="payMerName";
 	
	private static final String PAYEEMERNAME="payeeMerName";
 	
	private static final String DATECREATED="dateCreated";
 	
	private static final String DATEEND="dateEnd";
 	
	private static final String START="start";

	private static final String LENGTH="length";
 	
	@Resource
	private KuandaoPaymentOrderDao kuandaoPaymentOrderDao;
	
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	@Resource
	private KuandaoSequenceService kuandaoSequenceService;
	
	@Resource
	private KuandaoPaymentOrderService kuandaoPaymentOrderService;
	
	@Resource
	private KuandaoDepositJounalDao kuandaoDepositJounalDao;
	
	@Resource
	private KuandaoAccountService kuandaoAccountService;
	
	@Override
	public int totalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto) {
		return kuandaoPaymentOrderDao.totalPaymentOrders(paymentOrderDto);
	}
	@Override
	public List<KuandaoPaymentOrderDto> queryPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto, Integer start,
			Integer length) {
		Map<String,Object> param=new HashMap<>();
		param.put(CONSIGNORDERCODE,paymentOrderDto.getConsignOrderCode());
		param.put(PAYMERNAME, paymentOrderDto.getPayMerName());
		param.put(PAYEEMERNAME,paymentOrderDto.getPayeeMerName() );
		param.put("ownerName",paymentOrderDto.getOwnerName() );
		param.put(DATECREATED,paymentOrderDto.getDateCreated() );
		param.put(DATEEND,paymentOrderDto.getDateEnd());
		param.put("submitStatus",paymentOrderDto.getSubmitStatus());
		param.put("chargStatus",paymentOrderDto.getChargStatus());
		param.put(START,start);
		param.put(LENGTH,length);
		return kuandaoPaymentOrderDao.queryPaymentOrders(param);
	}
	
	
	@Override
	public int totalAbnormalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto) {
		return kuandaoPaymentOrderDao.totalAbnormalPaymentOrders(paymentOrderDto);
	}
	
	
	@Override
	public List<KuandaoPaymentOrderDto> queryAbnormalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto,
			Integer start, Integer length) {
		Map<String,Object>  param=new HashMap<>();
		param.put(CONSIGNORDERCODE,paymentOrderDto.getConsignOrderCode());
		param.put(PAYMERNAME, paymentOrderDto.getPayMerName());
		param.put(PAYEEMERNAME,paymentOrderDto.getPayeeMerName() );
		param.put("ownerName",paymentOrderDto.getOwnerName() );
		param.put(DATECREATED,paymentOrderDto.getDateCreated() );
		param.put(DATEEND,paymentOrderDto.getDateEnd());
		param.put(START,start);
		param.put(LENGTH,length);
		return kuandaoPaymentOrderDao.queryAbnormalPaymentOrders(param);
	}
	
	@Override//查询同步日志total
	public int totalSynchronizeLog(PaymentOrderSynchLogDto synLogDto) {
		return kuandaoPaymentOrderDao.totalSynchronizeLog(synLogDto);
	}
	
	/**
	 * 查询同步日志detail，
	 * 备注：默认显示支付单提交的日志，默认显示提交失败的日志
	 */
	@Override
	public List<PaymentOrderSynchLogDto> querySynchronizeLog(PaymentOrderSynchLogDto synLogDto, Integer start,
			Integer length) {
		Map<String,Object>  param=new HashMap<>();
		param.put("paymentOrderCode",synLogDto.getPaymentOrderCode());
		param.put(CONSIGNORDERCODE,synLogDto.getConsignOrderCode());
		param.put(PAYMERNAME, synLogDto.getPayMerName());
		param.put(PAYEEMERNAME,synLogDto.getPayeeMerName() );
		param.put(DATECREATED,synLogDto.getDateCreated() );
		param.put(DATEEND,synLogDto.getDateEnd());
		param.put("result",synLogDto.getResult());
		param.put(START,start);
		param.put(LENGTH,length);
		return kuandaoPaymentOrderDao.querySynchronizeLog(param);
	}
	
	/**
	 * 异常支付单更改change功能，更新
	 */
	@Override 
	public int modifyOrder(KuandaoPaymentOrderDto dto) {
		//获取页面数据
		Integer id=dto.getId();
		String paymentOrderCode=dto.getPaymentOrderCode();
		BigDecimal transactionAmount=dto.getTransactionAmount();
		String nsortName=dto.getNsortName();
		BigDecimal weight=dto.getWeight();
		String userName=dto.getCreatedBy();
		//创建支付单
		KuandaoPaymentOrder paymentOrder=new KuandaoPaymentOrder();
		paymentOrder.setId(id);
		paymentOrder.setPaymentOrderCode(paymentOrderCode);
		paymentOrder.setTransactionAmount(transactionAmount);
		paymentOrder.setWeight(weight);
		paymentOrder.setNsortName(nsortName);
		paymentOrder.setCreated(new Date());
		paymentOrder.setCreatedBy(userName);
		paymentOrder.setLastUpdatedBy(userName);
		paymentOrder.setLastUpdated(new Date());;
		return kuandaoPaymentOrderDao.update(paymentOrder);
	}
	
	/*
	 * 根据id查询支付单
	 */
	private KuandaoPaymentOrderDto selectByPrimaryKey(int id){
		return kuandaoPaymentOrderDao.selectByPrimaryKey(id);
	}


	/**
	 * 订单重新提交功能
	 */
	@Override
	public int orderSubmitAgain(int id,String userName) {
		int resultCode;
		//根据id查找支付单信息
		KuandaoPaymentOrderDto kuandaoPaymentOrderDto =  selectByPrimaryKey(id);
		
		if(kuandaoPaymentOrderDto.getSubmitStatus().compareTo(OrderSubmitStatusEnum.finish.getCode()) == 0
				|| kuandaoPaymentOrderDto.getSubmitStatus().compareTo(OrderSubmitStatusEnum.match.getCode()) == 0){
			logger.error(String.format("can not submit payment order, submit status %d ",kuandaoPaymentOrderDto.getSubmitStatus().intValue()));
			return KuandaoResultEnum.readonly.getCode();
		}
		kuandaoPaymentOrderDto.setOccurType(OperateTypeEnum.manual.getType());
		kuandaoPaymentOrderDto.setCreatedBy(userName);
		kuandaoPaymentOrderDto.setLastUpdatedBy(userName);
		
		//记录日志
		PaymentOrderSynchLog paymentOrderSynchLog = new PaymentOrderSynchLog();
		paymentOrderSynchLog.setPaymentOrderId(kuandaoPaymentOrderDto.getId());
		paymentOrderSynchLog.setConsignOrderCode(kuandaoPaymentOrderDto.getConsignOrderCode());
		paymentOrderSynchLog.setPaymentOrderCode(kuandaoPaymentOrderDto.getPaymentOrderCode());
		paymentOrderSynchLog.setOccurType(OperateTypeEnum.manual.getType());
		paymentOrderSynchLog.setPayMerId(kuandaoPaymentOrderDto.getPayeeMerId());
		paymentOrderSynchLog.setPayMerName(kuandaoPaymentOrderDto.getPayMerName());
		paymentOrderSynchLog.setPayeeMerId(kuandaoPaymentOrderDto.getPayeeMerId());
		paymentOrderSynchLog.setPayeeMerName(kuandaoPaymentOrderDto.getPayeeMerName());
		paymentOrderSynchLog.setPayeeMerVirAcctNo(kuandaoPaymentOrderDto.getPayeeMerVirAcctNo());
		paymentOrderSynchLog.setNsortName(kuandaoPaymentOrderDto.getNsortName());
		paymentOrderSynchLog.setWeight(kuandaoPaymentOrderDto.getWeight());
		paymentOrderSynchLog.setTransactionAmount(kuandaoPaymentOrderDto.getTransactionAmount());
		paymentOrderSynchLog.setCreatedBy(userName);
		paymentOrderSynchLog.setLastUpdatedBy(userName);
		
		resultCode = kuandaoPaymentOrderService.orderSubmit(kuandaoPaymentOrderDto,paymentOrderSynchLog);
		KuandaoPaymentOrder kuandaoPaymentOrder = new KuandaoPaymentOrder();
		try {
			BeanUtils.copyProperties(kuandaoPaymentOrder, kuandaoPaymentOrderDto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convert kuandaoPaymentOrder from dto to domain failed ",e);
		}
		int count = kuandaoPaymentOrderDao.update(kuandaoPaymentOrder);
		if(resultCode == KuandaoResultEnum.success.getCode()) {
			resultCode = count;
		}
		kuandaoPaymentOrderService.insertSynchronizeLog(paymentOrderSynchLog);
		return resultCode;
	}
	

	//款到取号
	@Override
	public PrcsteelAccountInfo kuandaoTakeNumber(){
		PrcsteelAccountInfo prcsteelAccountInfo = new PrcsteelAccountInfo();;
		prcsteelAccountInfo.setMemeberCode(prcsteelAccount.getMemeberCode());
		prcsteelAccountInfo.setMemeberName(prcsteelAccount.getMemeberName());
		prcsteelAccountInfo.setBankName(prcsteelAccount.getBankName());
		prcsteelAccountInfo.setVirAcctNo(prcsteelAccount.getVirAcctNo());
		prcsteelAccountInfo.setAcctNo(prcsteelAccount.getAcctNo());
		prcsteelAccountInfo.setIdNo(prcsteelAccount.getIdNo());
		prcsteelAccountInfo.setMobile(prcsteelAccount.getMobile());
		String paymentOrderCode=kuandaoSequenceService.generateKuandaoPayOrderCode(OperateTypeEnum.manual.getType());
		prcsteelAccountInfo.setPaymentOrderCode(paymentOrderCode);
		return prcsteelAccountInfo;
	}
	
	
	@Override
	public Integer generateOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto) {
		//创建支付单
		KuandaoPaymentOrder paymentOrder = new KuandaoPaymentOrder();
		String impAcqSsn = kuandaoPaymentOrderDto.getImpAcqSsn();
		KuandaoDepositJournalDto queryDepositJournalDto = new KuandaoDepositJournalDto();
		queryDepositJournalDto.setImpAcqSsn(impAcqSsn);
		List<KuandaoDepositJournal> kuandaoDepositJournalList = kuandaoDepositJounalDao.queryDepositByCondition(queryDepositJournalDto);
		KuandaoDepositJournal kuandaoDepositJournal;
		if(kuandaoDepositJournalList.size() == 1){
			kuandaoDepositJournal = kuandaoDepositJournalList.get(0);
		}else{
			return KuandaoResultEnum.businesserror.getCode();
		}
		if(kuandaoDepositJournal.getPayMerBranchId() != null
				&& kuandaoPaymentOrderService.isLimitBank(kuandaoDepositJournal.getPayMerBranchId())){
			return KuandaoResultEnum.limitBank.getCode();
		}
		String payMerName = kuandaoDepositJournal.getPayMerName();
		
		KuandaoAccountDto queryAccountDto = new KuandaoAccountDto();
		queryAccountDto.setMemeberName(payMerName);
		List<KuandaoAccountDto> kuandaoAccountDtoList = kuandaoAccountService.queryAccountByCondition(queryAccountDto);
		KuandaoAccountDto kuandaoAccountDto;
		if(kuandaoAccountDtoList.size() == 1){
			kuandaoAccountDto = kuandaoAccountDtoList.get(0);
		}else{
			return KuandaoResultEnum.nodata.getCode();
		}
		paymentOrder.setImpAcqSsn(kuandaoPaymentOrderDto.getImpAcqSsn());
		paymentOrder.setOccurType(OperateTypeEnum.automatic.getType());
		paymentOrder.setTransactionAmount(kuandaoPaymentOrderDto.getTransactionAmount());
		paymentOrder.setPayeeMerId(prcsteelAccount.getMemeberCode());
		paymentOrder.setPayeeMerName(kuandaoDepositJournal.getPayeeVirtualAcctName());
		paymentOrder.setPayeeMerVirAcctNo(kuandaoDepositJournal.getPayeeVirtualAcctNo());
		paymentOrder.setPayMerName(payMerName);
		paymentOrder.setStatus(PaymentOrderStatus.unmatch.getCode());
		paymentOrder.setSubmitStatus(OrderSubmitStatusEnum.waitSubmit.getCode());
		paymentOrder.setCreated(new Date());
		paymentOrder.setLastUpdated(new Date());
		paymentOrder.setLastUpdatedBy(kuandaoPaymentOrderDto.getCreatedBy());
		paymentOrder.setCreatedBy(kuandaoPaymentOrderDto.getCreatedBy());
		paymentOrder.setPayMerId(kuandaoAccountDto.getMemeberCode());
		
		paymentOrder.setWeight(kuandaoPaymentOrderDto.getWeight());
		paymentOrder.setNsortName(kuandaoPaymentOrderDto.getNsortName());
		paymentOrder.setPaymentOrderCode(kuandaoPaymentOrderDto.getPaymentOrderCode());
		int affectiveCount = kuandaoPaymentOrderDao.insertPaymentOrder(paymentOrder);
		if(affectiveCount != 1){
			return KuandaoResultEnum.dataoperateerror.getCode();
		}
		return orderSubmitAgain(paymentOrder.getId(), kuandaoPaymentOrderDto.getCreatedBy());
	}
	@Override
	public Integer finishOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto) {
		
		return kuandaoPaymentOrderService.finishOrder(kuandaoPaymentOrderDto);
		
	}
	
	@Override
	public String matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode,String transDate,String transAmt) {
		return kuandaoPaymentOrderService.matchOrderNotify(spdbRequstParam, paymentOrderCode,transDate,transAmt);
	}
	@Override
	public ChargResultResponse chargResultNotify(ChargResultNotifyDto chargResultNotifyDto) {
		return kuandaoPaymentOrderService.chargResultNotify(chargResultNotifyDto);
	}
	@Override
	public Integer charg(Integer id) {
		return kuandaoPaymentOrderService.charg(id);
	}
	
}
