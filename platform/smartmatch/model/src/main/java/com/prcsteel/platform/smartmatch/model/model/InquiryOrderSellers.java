package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

public class InquiryOrderSellers {
	private Long id;

	private Long inquiryOrderId;

	private Long accountId;

	private String accountName;

	private Long contactId;// 联系人ID

	private Date created;

	private String createdBy;

	private Date lastUpdated;

	private String lastUpdatedBy;

	private Integer modificationNumber;

	private String rowId;

	private String parentRowId;

	private String ext1;

	private String ext2;

	private String ext3;

	private Integer ext4;

	private Integer ext5;

	private Integer ext6;

	private Date ext7;

	private Long ext8;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInquiryOrderId() {
		return inquiryOrderId;
	}

	public void setInquiryOrderId(Long inquiryOrderId) {
		this.inquiryOrderId = inquiryOrderId;
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

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public Integer getExt4() {
		return ext4;
	}

	public void setExt4(Integer ext4) {
		this.ext4 = ext4;
	}

	public Integer getExt5() {
		return ext5;
	}

	public void setExt5(Integer ext5) {
		this.ext5 = ext5;
	}

	public Integer getExt6() {
		return ext6;
	}

	public void setExt6(Integer ext6) {
		this.ext6 = ext6;
	}

	public Date getExt7() {
		return ext7;
	}

	public void setExt7(Date ext7) {
		this.ext7 = ext7;
	}

	public Long getExt8() {
		return ext8;
	}

	public void setExt8(Long ext8) {
		this.ext8 = ext8;
	}

	public InquiryOrderSellers() {
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public InquiryOrderSellers(Long inquiryOrderId, Long accountId, String accountName, String createdBy,
			String lastUpdatedBy) {
		this.inquiryOrderId = inquiryOrderId;
		this.accountId = accountId;
		this.accountName = accountName;
		this.createdBy = createdBy;
		this.lastUpdatedBy = lastUpdatedBy;

	}
}