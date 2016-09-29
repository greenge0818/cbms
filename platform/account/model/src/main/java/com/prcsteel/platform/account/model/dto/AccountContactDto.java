package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.AccountContact;

/**
 * Created by kongbinheng on 2015/7/16.
 */
public class AccountContactDto extends AccountContact {

    private String isMainName;
    private String statusName;
    private String managerName;

    private String accountName;
    private String departmentName;

    public String getIsMainName() {
        return isMainName;
    }

    public void setIsMainName(String isMainName) {
        this.isMainName = isMainName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getAccountName() {
        return accountName;
    }

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
    
}
