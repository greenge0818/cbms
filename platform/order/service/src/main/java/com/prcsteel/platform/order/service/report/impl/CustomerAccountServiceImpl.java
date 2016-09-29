package com.prcsteel.platform.order.service.report.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.model.dto.AccountTransLogsDto;
import com.prcsteel.platform.order.model.dto.CustomerAccountDto;
import com.prcsteel.platform.order.model.dto.CustomerDto;
import com.prcsteel.platform.order.model.dto.OrderItemDto;
import com.prcsteel.platform.order.model.dto.OrderSerialDto;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountStateQuery;
import com.prcsteel.platform.order.persist.dao.CustomerAccountDao;
import com.prcsteel.platform.order.service.report.CustomerAccountService;

/**
 * @author lixiang
 * @version V1.1
 * @Title: CustomerAccountServiceImpl
 * @Package com.prcsteel.platform.order.service.impl.report
 * @Description: 财务报表
 * @date 2015/8/19
 */

@Service("customerAccountService")
public class CustomerAccountServiceImpl implements CustomerAccountService {

	@Resource
	private CustomerAccountDao customerAccountDao;

	@Override
	public List<CustomerAccountDto> queryForCustomer(
			AccountStateQuery accountStateQuery) {
		List<CustomerDto> custList = null;
		if (AccountType.buyer.toString().equals(accountStateQuery.getType())) {
			custList = customerAccountDao.queryForBuyer(accountStateQuery);
		} else if (AccountType.seller.toString().equals(accountStateQuery.getType())) {
			custList = customerAccountDao.queryForSeller(accountStateQuery);
		}
		if ( custList == null || custList.size() == 0 ) {
			return null;
		}
		List<OrderItemDto> orderItemList = null;
		List<Long> accountIds = custList.stream().map(a -> a.getAccountId()).collect(Collectors.toList());
		accountStateQuery.setAccountIds(accountIds);
		if (AccountType.buyer.toString().equals(accountStateQuery.getType())) {
			orderItemList = customerAccountDao.queryOrderBuyer(accountStateQuery);
		} else if (AccountType.seller.toString().equals(accountStateQuery.getType())) {
			orderItemList = customerAccountDao.queryOrderSeller(accountStateQuery);
		}
		if (orderItemList == null || orderItemList.size() == 0 ) {
			return null;
		}
		List<OrderSerialDto> orderSerialList = customerAccountDao.totalBalance(accountStateQuery);
//		if (orderSerialList == null || orderSerialList.size() == 0) {
//			return null;
//		}
		//在客户公司账目划转详情里如果没有数据，则设置为零，防止出现空页面 afeng 2016/6/2
 		return setCustListAmount(custList, orderItemList, orderSerialList);
	}

	private List<CustomerAccountDto> setCustListAmount(List<CustomerDto> custList,
			List<OrderItemDto> orderItemList, List<OrderSerialDto> orderSerialList) {
		List<CustomerAccountDto> list = new LinkedList<CustomerAccountDto>();
		for (CustomerDto customerDto : custList) {
			CustomerAccountDto customerAccountDto = new CustomerAccountDto();
			customerAccountDto.setAccountId(customerDto.getAccountId());
			customerAccountDto.setcName(customerDto.getcName());
			for (OrderItemDto orderItemDto : orderItemList) {
				if (customerDto.getAccountId().equals(orderItemDto.getAccountId())) {
					customerAccountDto.setBringAmount(orderItemDto.getBringAmount());
					customerAccountDto.setAmount(orderItemDto.getAmount());
					customerAccountDto.setTotalAmount(orderItemDto.getTotalAmount());
				}
			}
			for (OrderSerialDto orderSerialDto : orderSerialList) {
				if (customerDto.getAccountId().equals(orderSerialDto.getAccountId())) {
					customerAccountDto.setBalance(orderSerialDto.getBalance());
					customerAccountDto.setBalanceSecondSettlement(orderSerialDto.getBalanceSecondSettlement());
				}
			}
			list.add(customerAccountDto);
		}
		return list;
	}

	@Override
	public Integer queryReportCount(AccountStateQuery accountStateQuery) {
		if (AccountType.buyer.toString().equals(accountStateQuery.getType())) {
			return customerAccountDao.queryBuyerCount(accountStateQuery);
		} else if (AccountType.seller.toString().equals(accountStateQuery.getType())) {
			return customerAccountDao.querySellerCount(accountStateQuery);
		}
		return 0;
	}

	@Override
	public Account getAccountName(AccountStateQuery accountStateQuery) {
		return customerAccountDao.getAccountName(accountStateQuery);
	}

	@Override
	public AccountTransLog queryForAmount(AccountStateQuery accountStateQuery) {
		return customerAccountDao.queryForAmount(accountStateQuery);
	}

	@Override
	public List<AccountTransLogsDto> queryByAccountId(
			AccountStateQuery accountStateQuery) {
		List<AccountTransLog> list = customerAccountDao.queryByAccountId(accountStateQuery);
		List<AccountTransLogsDto> logList = new LinkedList<AccountTransLogsDto>();

		if (list != null) {
			for (AccountTransLog log : list) {
				AccountTransLogsDto logDto = new AccountTransLogsDto();
				logDto.setSerialTime(log.getSerialTime());
				if (log.getAmount().doubleValue() < 0) {
					logDto.setAmountReduce(log.getAmount());// 二次结算余额减少
				} else if (log.getAmount().doubleValue() == 0) {
					logDto.setAmountReduce(new BigDecimal("0"));
					logDto.setAmountAdd(new BigDecimal("0"));
				} else {
					logDto.setAmountAdd(log.getAmount());// 二次结算余额增加
				}
				if (log.getCashHappenBalance().doubleValue() < 0) {
					logDto.setBalanceReduce(log.getCashHappenBalance());// 账户余额减少
				} else if (log.getCashHappenBalance().doubleValue() == 0) {
					logDto.setBalanceReduce(new BigDecimal("0"));
					logDto.setBalanceAdd(new BigDecimal("0"));
				} else {
					logDto.setBalanceAdd(log.getCashHappenBalance());// 账户余额增加
				}
				logDto.setAmount(log.getAmount());
				logDto.setCashHappenBalance(log.getCashHappenBalance());
				logDto.setCurrentBalance(log.getCurrentBalance());// 二次结算余额
				logDto.setCashCurrentBalance(log.getCashCurrentBalance());// 账户余额
				logDto.setAssociationType(log.getAssociationType());
				logDto.setApplyType(ApplyType.getNameByCode(log.getApplyType()));
				logDto.setAssociationType(AssociationType.getNameByCode(log.getAssociationType()));
				logDto.setApplyerName(log.getApplyerName());
				logDto.setConsignOrderCode(log.getConsignOrderCode());
				logList.add(logDto);
			}
		}
		return logList;
	}

	@Override
	public Integer queryByAccountIdCount(AccountStateQuery accountStateQuery) {
		return customerAccountDao.queryByAccountIdCount(accountStateQuery);
	}

}
