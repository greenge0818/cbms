package com.prcsteel.platform.kuandao.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.KuandaoAccountConstant;
import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.BoundNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MemeberQueryTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MemeberTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.AccountTranstypeEnum;
import com.prcsteel.platform.kuandao.model.enums.AccountTypeEnum;
import com.prcsteel.platform.kuandao.model.enums.AccountVirAcctFlagEnum;
import com.prcsteel.platform.kuandao.service.MemeberService;

import cn.jpush.api.utils.StringUtils;

@Service("kuandao.service.MemeberService")
public class MemeberServiceImpl implements MemeberService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemeberServiceImpl.class);
	
	@Resource
	private SpdbHttpsPost spdbHttpsPost;
	
	@Value("${kuandao.mercCode}")
	private String mercCode;
	
	@Value("${kuandao.mercUrl}")
	private String mercUrl;
	
	@Override
	public SPDBResponseResult regiterMemeber(KuandaoAccountDto kuandaoAccountDto) {

		//构造交易参数
		String tranAbbr = SPDBTransNameConstant.SMMG;  
		MemeberTransaction transaction = new MemeberTransaction();
		transaction.setMercCode(mercCode);
		transaction.setTranAbbr(tranAbbr);
		transaction.setMercDtTm(DateUtils.getPlainDateTime());
		transaction.setTransType(AccountTranstypeEnum.open.getCode());
		transaction.setVirAcctFlag(AccountVirAcctFlagEnum.YES.getCode());
		transaction.setSubMerId(kuandaoAccountDto.getMemeberCode());
		String memeberName = kuandaoAccountDto.getMemeberName();
		try {
			memeberName = URLEncoder.encode(memeberName,"GBK");
			transaction.setSubMerName(memeberName);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("客户名“%s”转码失败", memeberName),e);
		}
		String idNo = kuandaoAccountDto.getIdNo();
		try {
			idNo = URLEncoder.encode(idNo,"GBK");
			transaction.setIdNo(idNo);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("组织机构代码证“%s”转码失败", idNo),e);
		}
		transaction.setMobileNo(kuandaoAccountDto.getMobile());
		transaction.setSubMerType(AccountTypeEnum.company.getCode());
		transaction.setIdType(KuandaoAccountConstant.ID_TYPE);
		transaction.setMercUrl(mercUrl + "/notify/boundNotify.html");
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,transaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}


	
	@Override
	public SPDBResponseResult modifyMemeber(KuandaoAccountDto kuandaoAccountDto) {
		//构造交易参数
		String tranAbbr = SPDBTransNameConstant.SMMG;  
		MemeberTransaction transaction = new MemeberTransaction();
		transaction.setMercCode(mercCode);
		transaction.setTranAbbr(tranAbbr);
		transaction.setMercDtTm(DateUtils.getPlainDateTime());
		transaction.setTransType(AccountTranstypeEnum.modify.getCode());
		transaction.setSubMerId(kuandaoAccountDto.getMemeberCode());
		String memeberName = kuandaoAccountDto.getMemeberName();
		try {
			memeberName = URLEncoder.encode(memeberName,"GBK");
			transaction.setSubMerName(memeberName);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("客户名“%s”转码失败", memeberName),e);
		}
		String idNo = kuandaoAccountDto.getIdNo();
		try {
			if(StringUtils.isNotEmpty(idNo)){
				idNo = URLEncoder.encode(idNo,"GBK");
			}
			transaction.setIdNo(idNo);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("组织机构代码证“%s”转码失败", idNo),e);
		}
		transaction.setMobileNo(kuandaoAccountDto.getMobile());
		transaction.setSubMerType(AccountTypeEnum.company.getCode());
		transaction.setIdType(KuandaoAccountConstant.ID_TYPE);
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,transaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}


	@Override
	public SPDBResponseResult deleteMemeber(String memeberCode) {
		
		String tranAbbr = SPDBTransNameConstant.SMMG;  
		MemeberTransaction transaction = new MemeberTransaction();
		transaction.setMercCode(mercCode);
		transaction.setTranAbbr(tranAbbr);
		transaction.setMercDtTm(DateUtils.getPlainDateTime());
		transaction.setTransType(AccountTranstypeEnum.delete.getCode());
		transaction.setSubMerId(memeberCode);
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,transaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
		
	}
	
	@Override
	public SPDBResponseResult queryMemeberInfo(String memeberCode) {
		//构造交易参数
		String tranAbbr = SPDBTransNameConstant.SMCX;  
		MemeberQueryTransaction transaction = new MemeberQueryTransaction();
		transaction.setMercCode(mercCode);
		transaction.setTranAbbr(tranAbbr);
		transaction.setSubMerId(memeberCode);
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,transaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}



	@Override
	public String generateResponse(String memeberCode, String status) {
		String tranAbbr = SPDBTransNameConstant.SMBD;  
		BoundNotifyTransaction boundNotifyTransaction = new BoundNotifyTransaction();
		boundNotifyTransaction.setMercCode(mercCode);
		boundNotifyTransaction.setSubMerId(memeberCode);
		boundNotifyTransaction.setStatus(status);
		
		SPDBRequstParam spdbRequstParam = new SPDBRequstParam(tranAbbr,boundNotifyTransaction.toString());
		return spdbHttpsPost.getSendXml(spdbRequstParam);
	}


}
