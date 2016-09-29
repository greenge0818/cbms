package com.prcsteel.platform.account.model.dto;

import java.util.Date;

/**
 * Created by dengxiyan on 2016/1/14.
 */
public class AccountAssignLogDto {
	
    private String accountName;
    private String orgNextName;
    private String orgExName;
    private Date created;
    private String assignName;
    private String managerExName;
    private String managerNextName;
    private String contactName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOrgNextName() {
        return orgNextName;
    }

    public void setOrgNextName(String orgNextName) {
        this.orgNextName = orgNextName;
    }

    public String getOrgExName() {
        return orgExName;
    }

    public void setOrgExName(String orgExName) {
        this.orgExName = orgExName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getManagerExName() {
        return managerExName;
    }

    public void setManagerExName(String managerExName) {
        this.managerExName = managerExName;
    }

    public String getManagerNextName() {
        return managerNextName;
    }

    public void setManagerNextName(String managerNextName) {
        this.managerNextName = managerNextName;
    }

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
   
    
}
