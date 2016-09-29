package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author lixiang
 *
 */
public class CustGroupingInforQuery extends PagedQuery {
	private String accountName;
	
	private String groupInfoName;

	private List<String> groupStatusList;

	private List<String> accountStatusList;
	
	private List<Long> userIds;
	
	private Long departmentId;
	
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getGroupInfoName() {
		return groupInfoName;
	}

	public void setGroupInfoName(String groupInfoName) {
		this.groupInfoName = groupInfoName;
	}

	public List<String> getGroupStatusList() {
		return groupStatusList;
	}

	public void setGroupStatusList(List<String> groupStatusList) {
		this.groupStatusList = groupStatusList;
	}

	public List<String> getAccountStatusList() {
		return accountStatusList;
	}

	public void setAccountStatusList(List<String> accountStatusList) {
		this.accountStatusList = accountStatusList;
	}
}
