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

import com.prcsteel.platform.kuandao.common.util.BeanUtil;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoRefundDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.RefundTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.MCLSImpStatus;
import com.prcsteel.platform.kuandao.model.enums.OperateResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OrderSubmitStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.PaymentOrderStatus;
import com.prcsteel.platform.kuandao.model.enums.RefundTransType;
import com.prcsteel.platform.kuandao.model.enums.SPDBResponseEnum;
import com.prcsteel.platform.kuandao.model.model.KuandaoDepositJournal;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;
import com.prcsteel.platform.kuandao.model.model.KuandaoRefund;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDepositJounalDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoPaymentOrderDao;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoRefundDao;
import com.prcsteel.platform.kuandao.service.KuanDaoProxyService;
import com.prcsteel.platform.kuandao.service.KuandaoRefundService;
import com.prcsteel.platform.kuandao.service.KuandaoSequenceService;
@Service
public class KuandaoRefundServiceImpl implements KuandaoRefundService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(KuandaoRefundServiceImpl.class);
	@Resource
	private KuandaoDepositJounalDao kuandaoDepositJounalDao;
	@Resource
	private KuandaoRefundDao kuandaoRefundDao;
	@Resource
	private KuandaoSequenceService kuandaoSequenceService;
	@Resource
	private KuandaoPaymentOrderDao kuandaoPaymentOrderDao;
	@Resource
	private KuanDaoProxyService kuanDaoProxyService;
	
	@Override
	public int queryToTalDeposit(KuandaoDepositJournalDto dto) {
		return kuandaoDepositJounalDao.queryToTalDeposit(dto);
	}
	@Override
	public List<KuandaoDepositJournalDto> queryDepositAndOrder(KuandaoDepositJournalDto dto,Integer start,Integer length) {
		Map<String,Object> param=new HashMap<>();
		
		param.put("impDate", dto.getImpDate());
		param.put("payeeVirtualAcctNo",dto.getPayeeVirtualAcctNo());
		param.put("payMerName",dto.getPayMerName());
		param.put("impStatus",dto.getImpStatus());
		param.put("start",start);
		param.put("length",length);
		return kuandaoDepositJounalDao.queryDepositAndOrder(param);
	}

	
	@Override
	public int insertRefund(KuandaoRefund kuandaoRefund) {
		return kuandaoRefundDao.insertRefund(kuandaoRefund);
	}
	
	@Override
	public int queryTotalRefund(KuandaoRefundDto dto) {
		return kuandaoRefundDao.queryTotalRefund(dto);
	}
	
	@Override
	public List<KuandaoRefundDto> queryRefundByCondition(KuandaoRefundDto dto,Integer start,Integer length){
		Map<String,Object> param=new HashMap<>();
		param.put("startDate", dto.getStartDate());
		param.put("endDate", dto.getEndDate());
		param.put("impStatus", dto.getImpStatus());
		param.put("payMerName", dto.getPayMerName());
		param.put("paymentOrderCode",dto.getPaymentOrderCode());
		param.put("start",start);
		param.put("length",length);
		return kuandaoRefundDao.queryRefund(param);
	}
	
	@Override
	public int applyRefund(KuandaoRefundDto kuandaoRefundDto) {
		int resultCode = KuandaoResultEnum.success.getCode();
		
		KuandaoDepositJournalDto queryDepositJournalDto = new KuandaoDepositJournalDto();
		Integer impId = kuandaoRefundDto.getImpId();
		queryDepositJournalDto.setId(impId);
		List<KuandaoDepositJournalDto> kuandaoDepositJournalList = kuandaoDepositJounalDao.queryDepositAndOrderByCondition(queryDepositJournalDto);
		KuandaoDepositJournalDto kuandaoDepositJournalDto;
		if(kuandaoDepositJournalList.size() == 1){
			kuandaoDepositJournalDto = kuandaoDepositJournalList.get(0);
		}else{
			logger.error("query  kuandaoDepositJournalDto data exception impId : %d",impId);
			return KuandaoResultEnum.businesserror.getCode();
		}
		//已完成和已退款的汇入流水不能退款
		if(MCLSImpStatus.finish.getCode().equals(kuandaoDepositJournalDto.getImpStatus()) 
				||MCLSImpStatus.refund.getCode().equals(kuandaoDepositJournalDto.getImpStatus())){
			return KuandaoResultEnum.readonly.getCode();
		}
		boolean isAdd = false;
		// 查询汇入流水是否有退款记录，如果有，更新原因
		KuandaoRefund kuandaoRefund;
		List<KuandaoRefund> kuandaoRefundList = kuandaoRefundDao.queryRefundByCondition(kuandaoRefundDto);
		if(!kuandaoRefundList.isEmpty()){
			if(kuandaoRefundList.size() > 1){ //多条退款申请
				return KuandaoResultEnum.businesserror.getCode();
			}else{
				kuandaoRefund = kuandaoRefundList.get(0);
			}
		}else{ //没有退款申请，生成退款申请
			isAdd = true;
			kuandaoRefund = new KuandaoRefund();
			kuandaoRefund.setCreated(new Date());
			kuandaoRefund.setCreatedBy(kuandaoRefundDto.getCreatedBy());
			kuandaoRefund.setImpId(kuandaoRefundDto.getImpId());
		}
		kuandaoRefund.setLastUpdated(new Date());
		kuandaoRefund.setLastUpdatedBy(kuandaoRefundDto.getCreatedBy());
		kuandaoRefund.setRefundReason(kuandaoRefundDto.getRefundReason());
		
		//设置退款类型
		if(MCLSImpStatus.match.getCode().equals(kuandaoDepositJournalDto.getImpStatus())){
			if(kuandaoDepositJournalDto.getId() == null){
				return KuandaoResultEnum.businesserror.getCode();
			}
			kuandaoRefund.setTransactionType(RefundTransType.good.getCode());
			kuandaoRefund.setOacqSsn(kuandaoDepositJournalDto.getAcqSsn());
			kuandaoRefund.setOsttDate(kuandaoDepositJournalDto.getSettDate());
		}else{
			kuandaoRefund.setTransactionType(RefundTransType.fund.getCode());
			kuandaoRefund.setOacqSsn(null);
			kuandaoRefund.setOsttDate(kuandaoDepositJournalDto.getImpDate());
		}
		kuandaoRefund.setTransactionAmount(kuandaoDepositJournalDto.getTransactionAmount());
		kuandaoDepositJournalDto.setTransactionType(kuandaoRefund.getTransactionType());
			
		//生成退款编号，发送退款交易
		Integer result = kuandaoRefund.getResult();
		if(result == null || result == OperateResultEnum.fail.getResult()){
			String refundCode = kuandaoSequenceService.generateKuandaoRefundCode();
			kuandaoRefund.setRefundCode(refundCode);
			kuandaoDepositJournalDto.setRefundCode(refundCode);
		}
		SPDBResponseResult spdbResult = kuanDaoProxyService.refund(kuandaoDepositJournalDto);
		if(spdbResult == null){
			logger.error("request spdb MCTH failed !");
			kuandaoRefund.setResult(OperateResultEnum.fail.getResult());
			resultCode = KuandaoResultEnum.systemerror.getCode();
		}else if(spdbResult.isSuccess()){
			String plain = spdbResult.getPlain();
			BeanUtil<RefundTransaction> beanUtil = new BeanUtil<>();
			RefundTransaction refundTransaction = beanUtil.stringToBean(plain, RefundTransaction.class);
			String reponseCode = refundTransaction.getRespCode();
			if(SPDBResponseEnum.success.getCode().equals(reponseCode)){
				kuandaoRefund.setAcqSsn(refundTransaction.getAcqSsn());
				kuandaoRefund.setSettDate(refundTransaction.getSettDate());
				kuandaoRefund.setResult(OperateResultEnum.success.getResult());
				kuandaoRefund.setTransactionAmount(new BigDecimal(refundTransaction.getTranAmt()));
				kuandaoRefund.setTermCode("000000");
			}else{
				logger.error(String.format("request spdb MCTH error, error code: %s; error msg: %s", reponseCode, SPDBResponseEnum.getTextByCode(reponseCode)));
				kuandaoRefund.setResult(OperateResultEnum.fail.getResult());
				resultCode = KuandaoResultEnum.systemerror.getCode();
			}
		}else{
			logger.error(String.format("request spdb MCTH error ,error code: %s error msg : %s",spdbResult.getErrCode(),spdbResult.getErrMsg()));
			kuandaoRefund.setResult(OperateResultEnum.fail.getResult());
			resultCode = KuandaoResultEnum.systemerror.getCode();
		}
		if(isAdd){ //生成退款申请
			kuandaoRefundDao.insertRefund(kuandaoRefund);
		}else{
			//更新退款申请状态
			kuandaoRefundDao.updateRefundByCondition(kuandaoRefund);
		}
		if(OperateResultEnum.success.getResult() == kuandaoRefund.getResult()){
			//跟新汇入流水
			KuandaoDepositJournal kuandaoDepositJournal = new KuandaoDepositJournal();
			try {
				BeanUtils.copyProperties(kuandaoDepositJournal, kuandaoDepositJournalDto);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("copy from kuandaoDepositJournalDto to kuandaoDepositJournal failed !", e);
			}
			kuandaoDepositJournal.setImpStatus(MCLSImpStatus.refund.getCode());
			kuandaoDepositJounalDao.update(kuandaoDepositJournal);
			
			Integer paymentOrderId = kuandaoDepositJournalDto.getPaymentOrderId();
			if(paymentOrderId != null){
				KuandaoPaymentOrderDto queryPaymentOrderDto = new KuandaoPaymentOrderDto();
				queryPaymentOrderDto.setId(paymentOrderId);
				List<KuandaoPaymentOrder> kuandaoPaymentOrderList = kuandaoPaymentOrderDao.queryPaymentOrderByCondition(queryPaymentOrderDto);
				if(kuandaoPaymentOrderList.size() == 1){
					KuandaoPaymentOrder kuandaoPaymentOrder = kuandaoPaymentOrderList.get(0);
					kuandaoPaymentOrder.setStatus(PaymentOrderStatus.refund.getCode());
					// 此处状态需确认
					kuandaoPaymentOrder.setSubmitStatus(OrderSubmitStatusEnum.refund.getCode());
					kuandaoPaymentOrderDao.update(kuandaoPaymentOrder);
				}else{
					logger.error(String.format("refund query payment order data exception id : %d", paymentOrderId));
					resultCode = KuandaoResultEnum.businesserror.getCode();
				}
			}
		}
		return resultCode;
	}
	
}
