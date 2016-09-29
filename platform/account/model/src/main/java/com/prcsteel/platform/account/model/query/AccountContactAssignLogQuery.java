package com.prcsteel.platform.account.model.query;

import java.util.List;

public class AccountContactAssignLogQuery extends AccountAssignLogQuery{
	
	private List<Long> userIds;

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
}
