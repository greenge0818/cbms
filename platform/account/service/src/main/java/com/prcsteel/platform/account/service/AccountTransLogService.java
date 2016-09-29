package com.prcsteel.platform.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.model.dto.AccountInAndOutDto;
import com.prcsteel.platform.account.model.dto.AccountTransLogDto;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountInAndOutQuery;
import com.prcsteel.platform.account.model.query.AccountInfoFlowSearchQuery;

/**
 * Created by kongbinheng on 2015/7/16.
 */
public interface AccountTransLogService {

    /**
     * 根据客户id查询账户信息
     * @param accountId
     * @param start
     * @param length
     * @return
     */
    List<AccountTransLogDto> queryTransLogByAccountId(Long accountId, String consignOrderCode, String applyType,
                                                      String startTime, String endTime, Integer start, Integer length);

    int totalTransLog(Long accountId, String consignOrderCode, String applyType, String startTime, String endTime);

    /**
     * 查询指定条件的资金往来
     * @param query
     * @return
     */
    List<AccountInAndOutDto> queryAccountInAndOut(AccountInAndOutQuery query);

    /**
     * 查询指定条件的资金往来总数
     * @param query
     * @return
     */
    int countAccountInAndOut(AccountInAndOutQuery query);

    /**
     * 查询指定条件的资金往来合计
     * @param query
     * @return
     */
    List<AccountInAndOutDto> queryAccountInAndOutTotal(AccountInAndOutQuery query);

    /**
     * 查询起初余额
     * @param query
     * @return
     */
    BigDecimal queryPreBlance(AccountInAndOutQuery query);

    /**
     * 查询期末余额
     * @param query
     * @return
     */
    BigDecimal queryLastBlance(AccountInAndOutQuery query);
    
    /**
     * 根据查询对象查询账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
	List<AccountTransLogDto> searchFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);

	/**
	 * 构建导出流水excel
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	ModelAndView doExportExcel(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);
	
	/**
	 * 根据客户部门id查询该部门二结流水
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	List<AccountTransLogDto> getAccountInfoSettlementFlows(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);

    /**
     * 根据查询对象统计账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
    int countFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);
    
    /**
     * 根据查询对象查询账户流水信息（精确查询）
     * @param accountInfoFlowSearchQuery
     * @return
     */
    List<AccountTransLog> queryFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);
}
