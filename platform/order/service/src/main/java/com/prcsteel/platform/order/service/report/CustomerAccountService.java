package com.prcsteel.platform.order.service.report;

import java.util.List;

import com.prcsteel.platform.account.model.dto.AccountTransLogsDto;
import com.prcsteel.platform.order.model.dto.CustomerAccountDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountStateQuery;

/**
 * @author lixiang
 * @version V1.1
 * @Title: CustomerAccountService
 * @Package com.prcsteel.platform.order.service.report
 * @Description: 财务报表
 * @date 2015/8/19
 */

public interface CustomerAccountService {

	/**
	 * 买家账户报表查询
	 * 
	 * @return
	 */
	public List<CustomerAccountDto> queryForCustomer(AccountStateQuery accountStateQuery);

	/**
	 * 买家账户报表记录数
	 * 
	 * @return
	 */
	public Integer queryReportCount(AccountStateQuery accountStateQuery);

	/**
	 * 根据id查客户名称
	 * 
	 * @param AccountStateQuery
	 * @return
	 */
	public Account getAccountName(AccountStateQuery accountStateQuery);
	
	/**
	 * 根据客户ID查期初余额和期初二次结算余额
	 * @param accountStateQuery
	 * @return
	 */
	public AccountTransLog queryForAmount(AccountStateQuery accountStateQuery);

	/**
	 * 根据客户id查询客户账户报表详情
	 * 
	 * @param AccountStateQuery
	 * @return
	 */
	public List<AccountTransLogsDto> queryByAccountId(AccountStateQuery accountStateQuery);

	/**
	 * 根据客户id查询客户账户报表详情记录数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer queryByAccountIdCount(AccountStateQuery accountStateQuery);
}
