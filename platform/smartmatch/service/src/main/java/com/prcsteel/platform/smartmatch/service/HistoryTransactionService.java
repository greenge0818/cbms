package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;

import java.util.List;

/**
 * 历史成交资源service
 * @author peanut <p>2016年2月24日 上午11:51:16</p>  
 */
public interface HistoryTransactionService {
	
	/**
	 * 执行历史成交job
	 */
	void doHistoryTransactionJob();
	
	/**
	 * 查询历史成交(订单状态大于等4)的资源明细
	 * @return
	 */
	List<ResourceDto> queryHistoryTransactionResourceItem();
}
