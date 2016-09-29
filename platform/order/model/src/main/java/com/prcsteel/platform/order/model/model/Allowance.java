package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class Allowance {
    private Long id;

    private String allowanceCode;

    private String allowanceType;

    private Long allowanceId;

    private Long orgId;

    private String orgName;

    private Long accountId;

    private String accountName;

    private Integer totalQuantity = 0;

    private BigDecimal totalWeight = BigDecimal.ZERO;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BigDecimal actualTotalWeight = BigDecimal.ZERO;

    private BigDecimal actualTotalAmount = BigDecimal.ZERO;

    private BigDecimal allowanceTotalWeight = BigDecimal.ZERO;

    private BigDecimal allowanceTotalAmount = BigDecimal.ZERO;

    private String allowanceManner;

    private String imgUrl;

    private String status;
    
    private Date auditDate;

    private String remark;

    private Byte isDeleted;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;
    
    private String note;

    private Long departmentId;

    private String departmentName;
    
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAllowanceCode() {
        return allowanceCode;
    }

    public void setAllowanceCode(String allowanceCode) {
        this.allowanceCode = allowanceCode;
    }

    public String getAllowanceType() {
        return allowanceType;
    }

    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }

    public Long getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(Long allowanceId) {
        this.allowanceId = allowanceId;
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

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getActualTotalWeight() {
        return actualTotalWeight;
    }

    public void setActualTotalWeight(BigDecimal actualTotalWeight) {
        this.actualTotalWeight = actualTotalWeight;
    }

    public BigDecimal getActualTotalAmount() {
        return actualTotalAmount;
    }

    public void setActualTotalAmount(BigDecimal actualTotalAmount) {
        this.actualTotalAmount = actualTotalAmount;
    }

    public BigDecimal getAllowanceTotalWeight() {
        return allowanceTotalWeight;
    }

    public void setAllowanceTotalWeight(BigDecimal allowanceTotalWeight) {
        this.allowanceTotalWeight = allowanceTotalWeight;
    }

    public BigDecimal getAllowanceTotalAmount() {
        return allowanceTotalAmount;
    }

    public void setAllowanceTotalAmount(BigDecimal allowanceTotalAmount) {
        this.allowanceTotalAmount = allowanceTotalAmount;
    }

    public String getAllowanceManner() {
        return allowanceManner;
    }

    public void setAllowanceManner(String allowanceManner) {
        this.allowanceManner = allowanceManner;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
