package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

public interface MCXDService {
	/**
	 * 会员预下单
	 * param:
	 * return:
	 * */
	
	public SPDBResponseResult orderSubmit(KuandaoPaymentOrderDto dto);

	/**
	 * 获取汇入流水匹配通知结果反馈报文
	 * 
	 * @param paymentOrderCode
	 * @param responseStatus
	 * @return
	 */
	public String generateResponse(String paymentOrderCode, String responseStatus);
	
	
}
