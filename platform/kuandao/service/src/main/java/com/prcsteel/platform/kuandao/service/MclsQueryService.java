package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

public interface MclsQueryService {
	/**
	 * 汇入流水查询：付款未匹配
	 * @param
	 * @return
	 */
	SPDBResponseResult queryMclsUnmatch();
	
	/**
	 * 汇入流水查询：已匹配
	 * @return
	 */
	SPDBResponseResult queryMclsMatch();

	/**
	 * 汇入流水查询：已退款
	 * @return
	 */
	SPDBResponseResult queryMclsRefund();
}