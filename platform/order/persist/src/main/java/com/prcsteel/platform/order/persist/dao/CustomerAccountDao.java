package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.CustomerDto;
import com.prcsteel.platform.order.model.dto.OrderItemDto;
import com.prcsteel.platform.order.model.dto.OrderSerialDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountStateQuery;

/**
 * @author lixiang
 * @version V1.1
 * @Title: CustomerAccountDao
 * @Package com.prcsteel.platform.order.persist.dao
 * @Description: 财务报表 
 * @date 2015/8/19
 */

public interface CustomerAccountDao {
	
	/**
	 * 查询买家客户信息
	 * @param accountStateQuery
	 * @return
	 */
	public List<CustomerDto> queryForBuyer(AccountStateQuery accountStateQuery);
	
	/**
	 * 查询卖家客户信息
	 * @param accountStateQuery
	 * @return
	 */
	public List<CustomerDto> queryForSeller(AccountStateQuery accountStateQuery);
	
	/**
	 * 买家账户报表查询
	 * @param accountStateQuery
	 * @return
	 */
	public List<OrderItemDto> queryOrderBuyer(AccountStateQuery accountStateQuery);

	/**
	 * 卖家账户报表查询
	 * @param accountStateQuery
	 * @return
	 */
	public List<OrderItemDto> queryOrderSeller(AccountStateQuery accountStateQuery);
	
	/**
	 * 买家账户报表记录数
	 * @param accountStateQuery
	 * @return
	 */
	public Integer queryBuyerCount(AccountStateQuery accountStateQuery);
	
	/**
	 * 卖家账户报表记录数
	 * @param accountStateQuery
	 * @return
	 */
	public Integer querySellerCount(AccountStateQuery accountStateQuery);
	
	/**
	 * 根据id查客户名称
	 * @param accountStateQuery
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
	 * @param paramMap
	 * @return
	 */
	public List<AccountTransLog> queryByAccountId(AccountStateQuery accountStateQuery);
	
	/**
	 * 根据客户id查询客户账户报表详情记录数
	 * @param accountStateQuery
	 * @return
	 */
	public Integer queryByAccountIdCount(AccountStateQuery accountStateQuery);
	
	/**
	 * 根据客户id和时间统计这段时间内的余额和二结余额的总和
	 * @param accountStateQuery
	 * @return
	 */
	public List<OrderSerialDto> totalBalance(AccountStateQuery accountStateQuery);
}
