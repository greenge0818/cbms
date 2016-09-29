package com.prcsteel.platform.acl.model.dto;

import java.util.Date;

public class UserOrgsDto{
	private Date created;

	private String createdBy;

	private Long id;
	
	private Date lastUpdated;
	
	private String lastUpdatedBy;
	
	private Long orgId;
	
	private Long roleId;

	private String roleName;

	private Long userId;

	private String userName;
	
	private String orgName;

	public Date getCreated() {
		return created;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Long getId() {
		return id;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public Long getOrgId() {
		return orgId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}