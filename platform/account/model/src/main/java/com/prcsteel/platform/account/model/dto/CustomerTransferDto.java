package com.prcsteel.platform.account.model.dto;

import java.util.Date;
import java.util.stream.Collectors;

import com.prcsteel.platform.account.model.enums.AccountStatus;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.common.utils.Tools;

public class CustomerTransferDto {
	
	private Long id;
	
    private String name;
    
    private Long accountTag;
    
    private String accountTagStr;
    
    private Long orgId;

    private String orgName;
    
    private Date created;
    
    private String createdStr;
    
    private Integer status;
    
    private String statusStr;
    
    private String managerName;
    
	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public void setAccountTag(Long accountTag) {
		this.accountTag = accountTag;
		setAccountTagStr(AccountTag.getNameByCode(accountTag).stream().map(a -> a.getName()).collect(Collectors.toList()).toString());
	}

	public void setStatus(Integer status) {
		this.status = status;
		setStatusStr(AccountStatus.getNameByCode(status));
	}

	public void setCreated(Date created) {
		this.created = created;
		setCreatedStr(Tools.dateToStr(created));
	}
	
	public String getCreatedStr() {
		return createdStr;
	}

	public void setCreatedStr(String createdStr) {
		this.createdStr = createdStr;
	}

	public String getAccountTagStr() {
		return accountTagStr;
	}

	public void setAccountTagStr(String accountTagStr) {
		this.accountTagStr = accountTagStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAccountTag() {
		return accountTag;
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

	public Date getCreated() {
		return created;
	}

	public Integer getStatus() {
		return status;
	}

}
