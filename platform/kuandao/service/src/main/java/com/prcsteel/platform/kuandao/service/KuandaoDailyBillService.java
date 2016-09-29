package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDailyBillDto;

public interface KuandaoDailyBillService {
	
	/**
	 * 日终对账单同步到本地
	 * @param userName
	 * @return
	 */
	public Integer insertDailyBill(String userName);
	
	/**
	 * 日终对账单列表查询
	 * @param dto
	 * @param start
	 * @param length
	 * @return
	 */
	public List<KuandaoDailyBillDto> queryDailyBill(KuandaoDailyBillDto dto,Integer start,Integer length); 
	
	/**
	 * 日终对账单总数查询
	 * @param dto
	 * @return
	 */
	public Integer queryTotalDailyBill(KuandaoDailyBillDto dto);
}
