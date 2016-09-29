package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Account;

public class CustAccountDto extends Account{
	private Long accountId;
	
	private String accountName;

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
}
