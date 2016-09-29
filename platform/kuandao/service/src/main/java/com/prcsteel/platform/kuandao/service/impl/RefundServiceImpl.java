package com.prcsteel.platform.kuandao.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.RefundTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.service.RefundService;
@Service("refundService")
public class RefundServiceImpl implements RefundService{

	@Resource
	private SpdbHttpsPost spdbHttpsPost;
	
	@Value("${kuandao.mercCode}")
	private String mercCode;
	
	@Override
	public SPDBResponseResult refundFromBank(KuandaoDepositJournalDto dto) {
		//构造交易参数
		String tranAbbr = SPDBTransNameConstant.MCTH;
		RefundTransaction refundTransaction=new RefundTransaction();
		refundTransaction.setTranAbbr(tranAbbr);
		refundTransaction.setMercCode(mercCode);
		refundTransaction.setMercDtTm(DateUtils.getPlainDateTime());
		refundTransaction.setImpAcqSsn(dto.getImpAcqSsn());
		//退货标识
		Integer transactionType=dto.getTransactionType();
		refundTransaction.setTransType(transactionType.toString());
		refundTransaction.setTermSsn(dto.getRefundCode());
		
		if(transactionType==0){
			refundTransaction.setoAcqSsn(dto.getAcqSsn());
			refundTransaction.setoSttDate(dto.getSettDate());
		}else{
			refundTransaction.setoAcqSsn(null);
			refundTransaction.setoSttDate(dto.getImpDate().replaceAll("-", ""));
		}
		
		refundTransaction.setTranAmt(dto.getTransactionAmount().toString());
		refundTransaction.setTermCode("000000");
		
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,refundTransaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}
}
