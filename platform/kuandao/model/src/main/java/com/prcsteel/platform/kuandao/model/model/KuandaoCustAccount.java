package com.prcsteel.platform.kuandao.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @Title KuandaoAccount.java
 * @Package com.prcsteel.platform.kuandao.model
 * @Description 款道账户Domain类
 * @author zjshan
 *
 * @date 2016年5月25日 上午9:40:20
 */
public class KuandaoCustAccount {

	private Integer id;
	
	private Long acctId;
	
	private String memeberCode;
	
	private String virAcctNo;
	
	private String mobile;
	
	private Integer acctType;
	
	private String branchId;
	
	private String departmentId;
	
	private String acctNo;
	
	private BigDecimal drawAmt;
	
	private String status;
	
	private String type;
	
	private String idType;
	
	private Integer modificationStatus;
	
	private Integer result;
	
	private Integer failureCount;
	
	private Date applyTime;
	
	private Date boundTime;
	
	private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getAcctId() {
		return acctId;
	}

	public void setAcctId(Long acctId) {
		this.acctId = acctId;
	}

	public String getMemeberCode() {
		return memeberCode;
	}

	public void setMemeberCode(String memeberCode) {
		this.memeberCode = memeberCode;
	}

	public String getVirAcctNo() {
		return virAcctNo;
	}

	public void setVirAcctNo(String virAcctNo) {
		this.virAcctNo = virAcctNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAcctType() {
		return acctType;
	}

	public void setAcctType(Integer acctType) {
		this.acctType = acctType;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public BigDecimal getDrawAmt() {
		return drawAmt;
	}

	public void setDrawAmt(BigDecimal drawAmt) {
		this.drawAmt = drawAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public Integer getModificationStatus() {
		return modificationStatus;
	}

	public void setModificationStatus(Integer modificationStatus) {
		this.modificationStatus = modificationStatus;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getBoundTime() {
		return boundTime;
	}

	public void setBoundTime(Date boundTime) {
		this.boundTime = boundTime;
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

}
