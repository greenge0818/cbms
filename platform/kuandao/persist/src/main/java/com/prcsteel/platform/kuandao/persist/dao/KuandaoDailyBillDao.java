package com.prcsteel.platform.kuandao.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDailyBillDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoDailyBill;

public interface KuandaoDailyBillDao {
	/**
	 * 日终对账单记录到本地
	 * @param 
	 * @return 
	 * */
	public Integer insertDailyBill(KuandaoDailyBill kuandaoDailyBill);
	/**
	 * 日终对账单查询
	 * @param
	 * @return
	 * */
	public List<KuandaoDailyBillDto> queryDailyBillByCondition(Map<String,Object> param);
	/**
	 * 日终对账单总数查询
	 * @param
	 * @return
	 * */
	public Integer queryTotalDailyBill(KuandaoDailyBillDto dto);
	
	/**
	 * 
	 * @param kuandaoDailyBillDto
	 * @return
	 */
	public List<KuandaoDailyBillDto> queryTestDataByCondition(KuandaoDailyBillDto kuandaoDailyBillDto);
}
