package com.prcsteel.platform.kuandao.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.spdb.MclsTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.MCLSImpStatus;
import com.prcsteel.platform.kuandao.service.MclsQueryService;

@Service
public class MclsQueryServiceImpl implements MclsQueryService {
	
	
	@Resource
	private SpdbHttpsPost spdbHttpsPost;

	@Value("${kuandao.mercCode}")
	private String mercCode;
	
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	@Override
	public SPDBResponseResult queryMclsUnmatch(){
		String impStatus = MCLSImpStatus.unmatch.getCode();
		return queryMclsByCondition(impStatus);
	}
	
	
	@Override
	public SPDBResponseResult queryMclsMatch(){
		String impStatus = MCLSImpStatus.match.getCode();
		return queryMclsByCondition(impStatus);
	}
	
	//根据条件进行汇入流水查询
	private SPDBResponseResult queryMclsByCondition(String impStatus){
		//构造交易参数
		String tranAbbr=SPDBTransNameConstant.MCLS;
		MclsTransaction mclsTransaction=new MclsTransaction();
		//查询条件封装
		mclsTransaction.setTranAbbr(tranAbbr);
		mclsTransaction.setMercCode(mercCode);
		
		String impDate=DateUtils.getPlainDate();
		mclsTransaction.setImpDate(impDate);
		mclsTransaction.setVirAcctNo(prcsteelAccount.getVirAcctNo());
		mclsTransaction.setImpStatus(impStatus);
		
		SPDBRequstParam spdbequstParam=new SPDBRequstParam(tranAbbr,mclsTransaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}


	@Override
	public SPDBResponseResult queryMclsRefund() {
		String impStatus = MCLSImpStatus.refund.getCode();
		return queryMclsByCondition(impStatus);
	}
}
