package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.query.PagedQuery;
/*
 * modiyfy by zhoucai@prcsteel.com
 * modify_description :新增客户类型字段accountTag,并新增对应的get、set方法
 * modify_date:2016-3-18
 */

public class CustomerTransferQuery extends PagedQuery {

	private Long managerId;
	
	private String managerName;
	
	private String transferType;
	
	private Long orgId;
	
	private String orgName;
	
	private Long accountId;
	
	private String accountName;
	
	private User user;
	
	private Long accountTag;

	public Long getAccountTag() {
		return accountTag;
	}

	public void setAccountTag(Long accountTag) {
		this.accountTag = accountTag;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
