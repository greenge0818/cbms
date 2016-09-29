package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.acl.model.model.User;

import java.util.Date;
import java.util.List;

/**
 * 
 * 卖家联系人DTO
 */
public class ContactDto {
	private Integer id;

	private String name;

	private String tel;

	private String deptName;

	private String qq;

	private String email;

	private String note;

	private String type;

	private String openId;

	private Date created;

	private String createdBy;

	private Date lastUpdated;

	private String lastUpdatedBy;

	private Integer modificationNumber;

	private String rowId;

	private String parentRowId;

	private Boolean isDeleted;

	private Integer ecUserId; // 超市用户ID

	private String source; // 来源,APP 手机,MARKET 超市,PICK 分检

	private String companyName; // 企业名称 来自超市和分检

	private String managerName;
	private Long deptId;
	private Long managerId;
	private boolean hiddenBtn = false;
	private String managerIds;
	private Integer status;

	private List<User> users;

	private List<Long> contactTags;

	public Integer getEcUserId() {
		return ecUserId;
	}

	public void setEcUserId(Integer ecUserId) {
		this.ecUserId = ecUserId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getParentRowId() {
		return parentRowId;
	}

	public void setParentRowId(String parentRowId) {
		this.parentRowId = parentRowId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public boolean isHiddenBtn() {
		return hiddenBtn;
	}

	public void setHiddenBtn(boolean hiddenBtn) {
		this.hiddenBtn = hiddenBtn;
	}

	public List<Long> getContactTags() {
		return contactTags;
	}

	public void setContactTags(List<Long> contactTags) {
		this.contactTags = contactTags;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getManagerIds() {
		return managerIds;
	}

	public void setManagerIds(String managerIds) {
		this.managerIds = managerIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
