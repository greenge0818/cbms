package com.prcsteel.platform.account.model.model;

import java.util.Date;

/**
 * Updated by chengui on 2016/3/17.
 */
public class AccountContact {
	
	private Long id;

    private Long accountId;
    
    private Integer contactId;

    private Long manager;

	private Integer status;

    private Date created;
    
    private String createdBy;
    
    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;
    
    private String type;

    private Boolean isDeleted;

	private String openId;

	/*
     * 以下为废弃字段
     */
    private String name;

    private String tel;

    private String deptName;

    private String qq;

    private String email;

    private Integer isMain;

    private String note;

    private String traderName;//操作员
    
    public AccountContact() {
    }

    public AccountContact(Long accountId, Integer contactId, String lastUpdatedBy) {
        this.accountId = accountId;
        this.contactId = contactId;
        this.lastUpdatedBy = lastUpdatedBy;
    }


	public Long getId() {
		return id;
	}

	public AccountContact setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getAccountId() {
		return accountId;
	}

	public AccountContact setAccountId(Long accountId) {
		this.accountId = accountId;
		return this;
	}

	public Integer getContactId() {
		return contactId;
	}

	public AccountContact setContactId(Integer contactId) {
		this.contactId = contactId;
		return this;
	}

	public Long getManager() {
		return manager;
	}

	public AccountContact setManager(Long manager) {
		this.manager = manager;
		return this;
	}

	public Date getCreated() {
		return created;
	}

	public AccountContact setCreated(Date created) {
		this.created = created;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public AccountContact setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public AccountContact setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public AccountContact setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
		return this;
	}

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public AccountContact setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
		return this;
	}

	public String getRowId() {
		return rowId;
	}

	public AccountContact setRowId(String rowId) {
		this.rowId = rowId;
		return this;
	}

	public String getParentRowId() {
		return parentRowId;
	}

	public AccountContact setParentRowId(String parentRowId) {
		this.parentRowId = parentRowId;
		return this;
	}

	public String getType() {
		return type;
	}

	public AccountContact setType(String type) {
		this.type = type;
		return this;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public AccountContact setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
		return this;
	}
	
	/*
     * 以下为废弃字段方法
     */
	public String getName() {
		return name;
	}

	public AccountContact setName(String name) {
		this.name = name;
		return this;
	}

	public String getTel() {
		return tel;
	}

	public AccountContact setTel(String tel) {
		this.tel = tel;
		return this;
	}

	public String getDeptName() {
		return deptName;
	}

	public AccountContact setDeptName(String deptName) {
		this.deptName = deptName;
		return this;
	}

	public String getQq() {
		return qq;
	}

	public AccountContact setQq(String qq) {
		this.qq = qq;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public AccountContact setEmail(String email) {
		this.email = email;
		return this;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public AccountContact setIsMain(Integer isMain) {
		this.isMain = isMain;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public AccountContact setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public String getNote() {
		return note;
	}

	public AccountContact setNote(String note) {
		this.note = note;
		return this;
	}

	public String getOpenId() {
		return openId;
	}

	public AccountContact setOpenId(String openId) {
		this.openId = openId;
		return this;
	}

	public String getTraderName() {
		return traderName;
	}

	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
}