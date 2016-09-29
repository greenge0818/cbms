package com.prcsteel.platform.kuandao.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.KuandaoGuaranteedPaymentsDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.MCDBTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.service.MCDBService;
@Service
public class MCDBServiceImpl implements MCDBService {
	
	@Resource
	private SpdbHttpsPost spdbHttpsPost;

	@Value("${kuandao.mercCode}")
	private String mercCode;
	@Override
	public SPDBResponseResult paymentGuaranteed(KuandaoGuaranteedPaymentsDto dto) {
		//构造交易参数
		String tranAbbr=SPDBTransNameConstant.MCDB;
		MCDBTransaction mcdbTransaction=new MCDBTransaction();
		//查询条件封装
		mcdbTransaction.setTranAbbr(tranAbbr);
		mcdbTransaction.setMercCode(mercCode);
		mcdbTransaction.setMercDtTm(DateUtils.getPlainDateTime());
		mcdbTransaction.setTermSsn(dto.getGuaranteedPaymentsCode());
		mcdbTransaction.setoAcqSsn(dto.getOacqSsn());
		mcdbTransaction.setoSttDate(dto.getOsttDate());
		mcdbTransaction.setTranAmt(dto.getTransactionAmount());
		mcdbTransaction.setSubMerId(dto.getPayeeMerId());
		mcdbTransaction.setTermCode("000000");

		SPDBRequstParam spdbequstParam=new SPDBRequstParam(tranAbbr,mcdbTransaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}

}
