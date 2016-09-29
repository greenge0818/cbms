package com.prcsteel.platform.account.service;

import java.util.List;

import com.prcsteel.platform.account.model.dto.CustomerTransferDto;
import com.prcsteel.platform.account.model.query.CustomerTransferQuery;
/*
 * modify_date:2016-3-16
 * modify_author :zhoucai@prcsteel.com
 * descript:新增接口queryIsAdminCount 查询当前客户是否为管理员
 */
public interface CustomerTransferService {

	 public List<CustomerTransferDto> querCustomerTransferList(CustomerTransferQuery query);
	 
	 public Integer querCustomerTransferCount(CustomerTransferQuery query);
	 
	 public void customerTransferAction(CustomerTransferQuery query);
	 
	 public int queryIsAdminCount(String loginId);
}
