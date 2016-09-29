package com.prcsteel.platform.kuandao.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.MatchNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.PaymentOrderTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.service.MCXDService;
@Service("kuandaorderService")
public class MCXDServiceImpl implements MCXDService {
	
	private static final Logger logger = LoggerFactory.getLogger(MCXDServiceImpl.class);
	
	@Resource
	private SpdbHttpsPost spdbHttpsPost;
	
	@Value("${kuandao.mercCode}")
	private String mercCode;
	
	@Value("${kuandao.mercUrl}")
	private String mercUrl;
	
	private static final String CHARSET = "GBK";
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	public SPDBResponseResult orderSubmit(KuandaoPaymentOrderDto dto){
		//构造交易参数
		String tranAbbr=SPDBTransNameConstant.MCXD;
		PaymentOrderTransaction orderTransaction=new PaymentOrderTransaction();
		orderTransaction.setMercCode(mercCode);
		orderTransaction.setMercUrl(mercUrl + "/notify/matchOrderNotify.html");
		orderTransaction.setMercDtTm(DateUtils.getPlainDateTime());
		orderTransaction.setTranAbbr(tranAbbr);
		String paymentOrderCode = dto.getPaymentOrderCode();
		try {
			paymentOrderCode = URLEncoder.encode(paymentOrderCode,CHARSET);
			orderTransaction.setTermSsn(paymentOrderCode);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("支付单号“%s”转码失败", paymentOrderCode),e);
		}
		orderTransaction.setTranAmt(dto.getTransactionAmount().toString());
		orderTransaction.setRcvMerId(prcsteelAccount.getMemeberCode());
		String rcvMerName=prcsteelAccount.getMemeberName();
		try {
			rcvMerName = URLEncoder.encode(rcvMerName,CHARSET);
			orderTransaction.setRcvMerName(rcvMerName);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("收款方名“%s”转码失败", rcvMerName),e);
		}
		orderTransaction.setPayMerId(dto.getPayMerId());
		String payMerName=dto.getPayMerName();
		try{
			payMerName=URLEncoder.encode(payMerName,CHARSET);
			orderTransaction.setPayMerName(payMerName);
		}catch(UnsupportedEncodingException e){
			logger.error(String.format("付款方名“%s”转码失败", payMerName),e);
		}
		String subMercGoodsName=dto.getNsortName();
		try{
			subMercGoodsName=URLEncoder.encode(subMercGoodsName,CHARSET);
			orderTransaction.setSubMercGoodsName(subMercGoodsName);
		}catch(UnsupportedEncodingException e){
			logger.error(String.format("商品名“%s”转码失败", subMercGoodsName),e);
		}
		orderTransaction.setSubMercGoodsCount(dto.getWeight().toString());
		
		SPDBRequstParam spdbequstParam=new SPDBRequstParam(tranAbbr,orderTransaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}

	@Override
	public String generateResponse(String paymentOrderCode, String responseStatus) {
		
		String tranAbbr = SPDBTransNameConstant.MCTZ;  
		MatchNotifyTransaction matchNotifyTransaction = new MatchNotifyTransaction();
		matchNotifyTransaction.setMercCode(mercCode);
		matchNotifyTransaction.setTermSsn(paymentOrderCode);
		matchNotifyTransaction.setStatus(responseStatus);
		
		SPDBRequstParam spdbRequstParam = new SPDBRequstParam(tranAbbr,matchNotifyTransaction.toString());
		return spdbHttpsPost.getSendXml(spdbRequstParam);
	}
	
	
}
